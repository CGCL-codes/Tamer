package us.wthr.jdem846.render.shapelayer;

import java.util.List;
import us.wthr.jdem846.ModelContext;
import us.wthr.jdem846.ModelOptions;
import us.wthr.jdem846.exception.CanvasException;
import us.wthr.jdem846.exception.DataSourceException;
import us.wthr.jdem846.exception.RenderEngineException;
import us.wthr.jdem846.gis.exceptions.MapProjectionException;
import us.wthr.jdem846.gis.projections.MapPoint;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;
import us.wthr.jdem846.rasterdata.RasterDataContext;
import us.wthr.jdem846.render.InterruptibleProcess;
import us.wthr.jdem846.canvas.ModelCanvas;
import us.wthr.jdem846.render.RenderEngine.TileCompletionListener;
import us.wthr.jdem846.render.RenderPipeline;
import us.wthr.jdem846.shapedata.ShapeDataContext;
import us.wthr.jdem846.shapefile.PointTranslateHandler;
import us.wthr.jdem846.shapefile.Shape;
import us.wthr.jdem846.shapefile.ShapeBase;
import us.wthr.jdem846.shapefile.ShapeConstants;
import us.wthr.jdem846.shapefile.ShapeFileRequest;
import us.wthr.jdem846.shapefile.ShapeLayer;
import us.wthr.jdem846.shapefile.ShapePath;
import us.wthr.jdem846.shapefile.exception.ShapeFileException;
import us.wthr.jdem846.shapefile.modeling.FeatureTypeStroke;
import us.wthr.jdem846.shapefile.modeling.LineStroke;

public class ShapeLayerRenderer extends InterruptibleProcess {

    private static Log log = Logging.getLog(ShapeLayerRenderer.class);

    private ModelContext modelContext;

    private List<TileCompletionListener> tileCompletionListeners;

    private ModelCanvas modelCanvas;

    private RenderPipeline renderPipeline;

    public ShapeLayerRenderer(ModelContext modelContext, ModelCanvas modelCanvas, List<TileCompletionListener> tileCompletionListeners) {
        this(modelContext, null, modelCanvas, tileCompletionListeners);
    }

    public ShapeLayerRenderer(ModelContext modelContext, List<TileCompletionListener> tileCompletionListeners) {
        this(modelContext, null, null, tileCompletionListeners);
    }

    public ShapeLayerRenderer(ModelContext modelContext, RenderPipeline renderPipeline, List<TileCompletionListener> tileCompletionListeners) {
        this(modelContext, renderPipeline, null, tileCompletionListeners);
    }

    public ShapeLayerRenderer(ModelContext modelContext, RenderPipeline renderPipeline, ModelCanvas modelCanvas, List<TileCompletionListener> tileCompletionListeners) {
        this.modelContext = modelContext;
        this.modelCanvas = modelCanvas;
        this.tileCompletionListeners = tileCompletionListeners;
        this.renderPipeline = renderPipeline;
    }

    public void render() throws RenderEngineException {
        if (isCancelled()) {
            log.warn("Render process cancelled, model not complete.");
            return;
        }
        if (this.modelCanvas == null) {
            this.modelCanvas = modelContext.getModelCanvas();
        }
        int numLayers = getShapeDataContext().getShapeDataListSize();
        int layerNumber = 0;
        for (ShapeFileRequest shapeFilePath : getShapeDataContext().getShapeFiles()) {
            layerNumber++;
            try {
                log.info("Loading shapefile from " + shapeFilePath.getPath());
                ShapeBase shapeBase = shapeFilePath.open();
                ShapeLayer shapeLayer = buildShapeLayer(shapeBase);
                shapeBase.close();
                renderLayer(shapeLayer);
                fireTileCompletionListeners(modelCanvas, ((double) layerNumber) / ((double) numLayers));
            } catch (OutOfMemoryError err) {
                throw err;
            } catch (Exception ex) {
                throw new RenderEngineException("Error occured rendering shape files", ex);
            }
            checkPause();
            if (isCancelled()) {
                log.warn("Render process cancelled, model not complete.");
                break;
            }
        }
        log.info("Completed shapefile rendering");
    }

    public ShapeLayer buildShapeLayer(ShapeBase shapeBase) throws RenderEngineException, ShapeFileException {
        ShapeLayer shapeLayer = new ShapeLayer(shapeBase.getShapeType());
        log.info("Loading " + shapeBase.getShapeCount() + " shapes");
        for (int i = 0; i < shapeBase.getShapeCount(); i++) {
            Shape shape = shapeBase.getShape(i);
            shapeLayer.addShape(shape);
        }
        log.info("Translating coordinates...");
        final MapPoint mapPoint = new MapPoint();
        shapeLayer.translate(new PointTranslateHandler() {

            public void translatePoint(double[] coords) {
                try {
                    double elevation = getElevationAtPoint(coords[1], coords[0]);
                    modelCanvas.getMapProjection().getPoint(coords[1], coords[0], elevation, mapPoint);
                    coords[0] = (int) mapPoint.column;
                    coords[1] = (int) mapPoint.row;
                    coords[2] = (int) mapPoint.z;
                } catch (MapProjectionException ex) {
                    log.warn("Error projecting coordinates to map: " + ex.getMessage(), ex);
                } catch (DataSourceException ex) {
                    log.warn("Error retrieving elevation for point: " + ex.getMessage(), ex);
                }
            }
        }, false);
        log.info("Combining shapes...");
        return shapeLayer;
    }

    protected double getElevationAtPoint(double latitude, double longitude) throws DataSourceException {
        double data = 0.0;
        if (getRasterDataContext() != null) {
            data = getRasterDataContext().getData(latitude, longitude, false, true);
        }
        return data;
    }

    public void renderLayer(ShapeLayer shapeLayer) throws RenderEngineException {
        int shapeType = shapeLayer.getType();
        log.info("Shape Type: " + shapeType);
        for (ShapePath path : shapeLayer.getShapePaths()) {
            renderPath(shapeType, path);
            checkPause();
            if (isCancelled()) {
                log.warn("Render process cancelled, model not complete.");
                break;
            }
        }
    }

    protected void renderPath(int shapeType, ShapePath path) throws RenderEngineException {
        FeatureTypeStroke featureStroke = null;
        if (path.getFeatureType() != null) {
            featureStroke = path.getFeatureType().getFeatureTypeStroke();
        }
        if (featureStroke == null) {
            featureStroke = FeatureTypeStroke.getDefaultFeatureTypeStroke();
        }
        List<LineStroke> lineStrokes = featureStroke.getLineStrokes();
        int[] color = new int[4];
        for (LineStroke lineStroke : lineStrokes) {
            lineStroke.getColor(color);
            color[3] = 255;
            boolean fill = (shapeType == ShapeConstants.TYPE_POLYGON || shapeType == ShapeConstants.TYPE_POLYGONM || shapeType == ShapeConstants.TYPE_POLYGONZ);
            ShapeFill shapeFill = new ShapeFill(color, shapeType, path, lineStroke, fill);
            try {
                shapeFill.fill(modelCanvas);
            } catch (CanvasException ex) {
                throw new RenderEngineException("Shape fill error: " + shapeType);
            }
        }
    }

    protected RasterDataContext getRasterDataContext() {
        return modelContext.getRasterDataContext();
    }

    protected ShapeDataContext getShapeDataContext() {
        return modelContext.getShapeDataContext();
    }

    protected ModelOptions getModelOptions() {
        return modelContext.getModelOptions();
    }

    protected void fireTileCompletionListeners(ModelCanvas modelCanvas, double pctComplete) {
        if (tileCompletionListeners != null) {
            for (TileCompletionListener listener : tileCompletionListeners) {
                listener.onTileCompleted(modelCanvas, pctComplete);
            }
        }
    }

    public static void render(ModelContext modelContext) throws RenderEngineException {
        ShapeLayerRenderer.render(modelContext, modelContext.getModelCanvas(), null);
    }

    public static void render(ModelContext modelContext, ModelCanvas modelCanvas) throws RenderEngineException {
        ShapeLayerRenderer.render(modelContext, modelCanvas, null);
    }

    public static void render(ModelContext modelContext, ModelCanvas modelCanvas, List<TileCompletionListener> tileCompletionListeners) throws RenderEngineException {
        ShapeLayerRenderer renderer = new ShapeLayerRenderer(modelContext, modelCanvas, tileCompletionListeners);
        renderer.render();
    }
}
