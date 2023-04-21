package us.wthr.jdem846.sandbox;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import us.wthr.jdem846.AbstractLockableService;
import us.wthr.jdem846.ModelContext;
import us.wthr.jdem846.ModelOptions;
import us.wthr.jdem846.annotations.Destroy;
import us.wthr.jdem846.annotations.Initialize;
import us.wthr.jdem846.annotations.Service;
import us.wthr.jdem846.annotations.ServiceRuntime;
import us.wthr.jdem846.exception.CanvasException;
import us.wthr.jdem846.exception.ModelContextException;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;
import us.wthr.jdem846.rasterdata.RasterData;
import us.wthr.jdem846.rasterdata.RasterDataContext;
import us.wthr.jdem846.rasterdata.RasterDataProviderFactory;
import us.wthr.jdem846.render.Dem2dGenerator;
import us.wthr.jdem846.canvas.ModelCanvas;
import us.wthr.jdem846.render.OutputProduct;
import us.wthr.jdem846.shapedata.ShapeDataContext;
import us.wthr.jdem846.shapefile.PointTranslateHandler;
import us.wthr.jdem846.shapefile.Shape;
import us.wthr.jdem846.shapefile.ShapeBase;
import us.wthr.jdem846.shapefile.ShapeConstants;
import us.wthr.jdem846.shapefile.ShapeFileRequest;
import us.wthr.jdem846.shapefile.ShapeLayer;
import us.wthr.jdem846.shapefile.ShapePath;
import us.wthr.jdem846.shapefile.modeling.FeatureTypeStroke;
import us.wthr.jdem846.shapefile.modeling.LineStroke;

@Service(name = "us.wthr.jdem846.sandbox.shapefiletest", enabled = false)
public class ShapefileTestService extends AbstractLockableService {

    private static Log log = Logging.getLog(ShapefileTestService.class);

    private List<String> elevationPaths = new LinkedList<String>();

    private List<ShapeFileRequest> shapeFilePaths = new LinkedList<ShapeFileRequest>();

    private String saveToPath;

    public ShapefileTestService() {
    }

    @Initialize
    public void init() {
        saveToPath = "C:/srv/elevation/Shapefiles/Nashua NH/";
        try {
            shapeFilePaths.add(new ShapeFileRequest("C:/srv/elevation/Shapefiles/Nashua NH/hydrography/NHDArea.shp", "usgs-hydrography"));
            shapeFilePaths.add(new ShapeFileRequest("C:/srv/elevation/Shapefiles/Nashua NH/hydrography/NHDFlowline.shp", "usgs-hydrography"));
            shapeFilePaths.add(new ShapeFileRequest("C:/srv/elevation/Shapefiles/Nashua NH/hydrography/NHDWaterbody.shp", "usgs-hydrography"));
            shapeFilePaths.add(new ShapeFileRequest("C:/srv/elevation/Shapefiles/Nashua NH/transportation/Trans_RailFeature.shp", "usgs-transportation-rail"));
            shapeFilePaths.add(new ShapeFileRequest("C:/srv/elevation/Shapefiles/Nashua NH/transportation/Trans_AirportRunway.shp", "usgs-transportation-runways"));
            shapeFilePaths.add(new ShapeFileRequest("C:/srv/elevation/Shapefiles/Nashua NH/transportation/Trans_RoadSegment.shp", "usgs-transportation-roads"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        elevationPaths.add("C:/srv/elevation/Shapefiles/Nashua NH/Elevation 1-3 Arc Second/21440075.flt");
    }

    @ServiceRuntime
    public void runtime() {
        this.setLocked(true);
        final RasterDataContext rasterDataContext = new RasterDataContext();
        final ShapeDataContext shapeDataContext = new ShapeDataContext();
        try {
            for (String inputPath : elevationPaths) {
                log.info("Adding elevation data: " + inputPath);
                RasterData rasterData = RasterDataProviderFactory.loadRasterData(inputPath);
                rasterDataContext.addRasterData(rasterData);
            }
            for (ShapeFileRequest shapeFilePath : shapeFilePaths) {
                shapeDataContext.addShapeFile(shapeFilePath);
            }
            log.info("Preparing dataPackage");
            rasterDataContext.prepare();
            shapeDataContext.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        ModelOptions modelOptions = new ModelOptions();
        modelOptions.setColoringType("green-tint");
        modelOptions.setBackgroundColor("Blue");
        ModelContext modelContext = null;
        try {
            modelContext = ModelContext.createInstance(rasterDataContext, shapeDataContext, modelOptions);
        } catch (ModelContextException ex) {
            ex.printStackTrace();
        }
        Dem2dGenerator dem2d = new Dem2dGenerator(modelContext);
        log.info("Generating DEM2D image");
        OutputProduct<ModelCanvas> output = null;
        try {
            output = dem2d.generate();
        } catch (Exception ex) {
            log.error("Failed to generate model: " + ex.getMessage(), ex);
        }
        ModelCanvas canvas = output.getProduct();
        try {
            canvas.save(saveToPath + "dem2d.png");
        } catch (CanvasException e) {
            e.printStackTrace();
        }
        for (ShapeFileRequest shapeFilePath : shapeDataContext.getShapeFiles()) {
            try {
                log.info("Loading shapefile from " + shapeFilePath.getPath());
                ShapeBase shapeBase = new ShapeBase(shapeFilePath.getPath(), shapeFilePath.getShapeDataDefinitionId());
                ShapeLayer shapeLayer = new ShapeLayer(shapeBase.getShapeType());
                log.info("Loading " + shapeBase.getShapeCount() + " shapes");
                for (int i = 0; i < shapeBase.getShapeCount(); i++) {
                    Shape shape = shapeBase.getShape(i);
                    shapeLayer.addShape(shape);
                }
                shapeLayer.translate(new PointTranslateHandler() {

                    public void translatePoint(double[] coords) {
                        double x = rasterDataContext.longitudeToColumn((float) coords[0]);
                        double y = rasterDataContext.latitudeToRow((float) coords[1]);
                        coords[0] = x;
                        coords[1] = y;
                    }
                }, false);
                shapeLayer = shapeLayer.getCombinedPathsByTypes();
                Image layerImage = renderLayer(rasterDataContext, shapeLayer);
                layerImage = layerImage.getScaledInstance(canvas.getWidth(), canvas.getHeight(), Image.SCALE_SMOOTH);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        log.info("Saving image");
        try {
            canvas.save(saveToPath + "dem2d-filled-polygons.png");
        } catch (CanvasException e) {
            e.printStackTrace();
        }
        log.info("Complete");
        this.setLocked(false);
    }

    public Image renderLayer(RasterDataContext rasterDataContext, ShapeLayer shapeLayer) {
        BufferedImage image = new BufferedImage(rasterDataContext.getDataColumns(), rasterDataContext.getDataRows(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int shapeType = shapeLayer.getType();
        log.info("Shape Type: " + shapeType);
        fillShapes(shapeLayer, g2d);
        g2d.dispose();
        return image;
    }

    public void fillShapes(ShapeLayer shapeLayer, Graphics2D g2d) {
        log.info("Creating shape paths");
        log.info("Drawing " + shapeLayer.size() + " polylines");
        int shapeType = shapeLayer.getType();
        for (ShapePath path : shapeLayer.getShapePaths()) {
            FeatureTypeStroke featureStroke = null;
            if (path.getFeatureType() != null) {
                featureStroke = path.getFeatureType().getFeatureTypeStroke();
            }
            if (featureStroke == null) {
                featureStroke = FeatureTypeStroke.getDefaultFeatureTypeStroke();
            }
            List<LineStroke> lineStrokes = featureStroke.getLineStrokes();
            for (LineStroke lineStroke : lineStrokes) {
                g2d.setStroke(lineStroke);
                g2d.setColor(lineStroke.getColor());
                if (shapeType == ShapeConstants.TYPE_POLYGON || shapeType == ShapeConstants.TYPE_POLYGONM || shapeType == ShapeConstants.TYPE_POLYGONZ) {
                } else if (shapeType == ShapeConstants.TYPE_POLYLINE || shapeType == ShapeConstants.TYPE_POLYLINEM || shapeType == ShapeConstants.TYPE_POLYLINEZ) {
                }
            }
        }
    }

    @Destroy
    public void destroy() {
    }
}
