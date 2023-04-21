package us.wthr.jdem846.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import us.wthr.jdem846.JDem846Properties;
import us.wthr.jdem846.ModelContext;
import us.wthr.jdem846.exception.CanvasException;
import us.wthr.jdem846.exception.DataSourceException;
import us.wthr.jdem846.exception.ModelContextException;
import us.wthr.jdem846.exception.RenderEngineException;
import us.wthr.jdem846.gis.exceptions.MapProjectionException;
import us.wthr.jdem846.gis.projections.MapPoint;
import us.wthr.jdem846.gis.projections.MapProjection;
import us.wthr.jdem846.i18n.I18N;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;
import us.wthr.jdem846.rasterdata.RasterData;
import us.wthr.jdem846.render.Dem2dGenerator;
import us.wthr.jdem846.canvas.ModelCanvas;
import us.wthr.jdem846.render.OutputProduct;
import us.wthr.jdem846.tasks.RunnableTask;
import us.wthr.jdem846.tasks.TaskControllerService;
import us.wthr.jdem846.tasks.TaskStatusAdapter;
import us.wthr.jdem846.tasks.TaskStatusListener;
import us.wthr.jdem846.ui.panels.RoundedPanel;

@SuppressWarnings("serial")
@Deprecated
public class ModelPreviewPane extends RoundedPanel {

    private static Log log = Logging.getLog(ModelPreviewPane.class);

    private ModelContext modelContext;

    private int previewWidth = 250;

    private int previewHeight = 100;

    private ModelCanvas rasterLayerCanvas;

    private ModelCanvas shapeLayerCanvas;

    private boolean shapeLayerNeedsUpdate = false;

    private boolean rasterLayerNeedsUpdate = false;

    private SimpleImagePanel imagePanel;

    private boolean shapePreviewEnabled = true;

    private boolean rasterPreviewEnabled = true;

    private boolean useSimpleRasterPreview = true;

    public ModelPreviewPane(ModelContext modelContext) {
        super();
        setBackground(Color.WHITE);
        rasterPreviewEnabled = JDem846Properties.getBooleanProperty("us.wthr.jdem846.ui.preview.rasterData");
        shapePreviewEnabled = JDem846Properties.getBooleanProperty("us.wthr.jdem846.ui.preview.shapeData");
        useSimpleRasterPreview = JDem846Properties.getBooleanProperty("us.wthr.jdem846.ui.preview.useSimplifiedRasterPreview");
        imagePanel = new SimpleImagePanel();
        this.modelContext = modelContext;
        setDefaultImage();
        this.setOpaque(false);
        this.addComponentListener(new ComponentListener() {

            public void componentHidden(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentResized(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
                if (rasterLayerNeedsUpdate || shapeLayerNeedsUpdate) {
                    update(rasterLayerNeedsUpdate, shapeLayerNeedsUpdate);
                }
            }
        });
        setLayout(new BorderLayout());
        add(imagePanel, BorderLayout.CENTER);
        shapeLayerNeedsUpdate = true;
        rasterLayerNeedsUpdate = true;
    }

    protected void setDefaultImage() {
        imagePanel.setImage(null);
    }

    public void update(boolean updateRasterData, boolean updateShapes) {
        rasterLayerNeedsUpdate = updateRasterData;
        shapeLayerNeedsUpdate = updateShapes;
        if (!rasterPreviewEnabled) {
            rasterLayerNeedsUpdate = false;
        }
        if (!shapePreviewEnabled) {
            shapeLayerNeedsUpdate = false;
        }
        float width = getWidth();
        float height = getHeight();
        if (width == 0 || height == 0) return;
        if (isVisible() == false) {
            shapeLayerNeedsUpdate = true;
            rasterLayerNeedsUpdate = true;
            return;
        }
        TaskStatusListener taskStatusListener = new TaskStatusAdapter() {

            public void taskCompleted(RunnableTask task) {
            }

            public void taskFailed(RunnableTask task, Throwable thrown) {
                JOptionPane.showMessageDialog(getRootPane(), I18N.get("us.wthr.jdem846.ui.modelPreviewPane.modelFailed.message") + ": " + thrown.getMessage(), I18N.get("us.wthr.jdem846.ui.modelPreviewPane.modelFailed.title"), JOptionPane.ERROR_MESSAGE);
            }
        };
        updateShapeLayers(taskStatusListener);
        updateRasterLayers(taskStatusListener);
    }

    protected void updateShapeLayers(TaskStatusListener taskStatusListener) {
        if (!shapeLayerNeedsUpdate) {
            return;
        }
        if (modelContext.getShapeDataContext().getShapeDataListSize() > 0) {
            updateLayer(taskStatusListener, false, true);
        }
        shapeLayerNeedsUpdate = false;
    }

    protected void updateRasterLayers(TaskStatusListener taskStatusListener) {
        if (!rasterLayerNeedsUpdate) {
            return;
        }
        if (modelContext.getRasterDataContext().getRasterDataListSize() > 0) {
            updateLayer(taskStatusListener, true, false);
        }
        rasterLayerNeedsUpdate = true;
    }

    protected void updateLayer(TaskStatusListener taskStatusListener, final boolean renderRasterData, final boolean renderShapes) {
        ModelContext modelContext;
        try {
            modelContext = this.modelContext.copy();
        } catch (DataSourceException ex) {
            log.error("Failed to copy model context: " + ex.getMessage(), ex);
            return;
        }
        modelContext.getModelOptions().setBackgroundColor("0;0;0;0");
        modelContext.getModelOptions().setColoringType("hypsometric-tint");
        modelContext.getModelOptions().setAntialiased(true);
        modelContext.getModelOptions().setWidth(500);
        modelContext.getModelOptions().setHeight(500);
        if (modelContext.getRasterDataContext().getDataMinimumValue() >= modelContext.getRasterDataContext().getDataMaximumValue()) {
            modelContext.getRasterDataContext().setDataMaximumValue(8850);
            modelContext.getRasterDataContext().setDataMinimumValue(-10971);
        }
        try {
            modelContext.updateContext();
        } catch (ModelContextException ex) {
            log.error("Exception updating model context: " + ex.getMessage(), ex);
        }
        ModelCanvas canvas = modelContext.getModelCanvas(true);
        if (useSimpleRasterPreview && rasterPreviewEnabled) {
            try {
                applySimpleRasterPreview(canvas);
                if (renderRasterData) {
                    rasterLayerCanvas = canvas;
                    mergeAndSetLayers();
                    return;
                }
            } catch (CanvasException ex) {
                log.error("Failed to apply simple raster preview to canvas: " + ex.getMessage(), ex);
            }
        }
        final Dem2dGenerator dem2d = new Dem2dGenerator(modelContext);
        RunnableTask renderTask = new RunnableTask("Vector Rendering Task") {

            public void run() throws RenderEngineException {
                log.info("Model rendering task starting");
                this.setStoppable(true);
                OutputProduct<ModelCanvas> product = dem2d.generate(!renderRasterData, !renderShapes);
                ModelCanvas modelCanvas = product.getProduct();
                if (modelCanvas != null) {
                    if (renderRasterData) {
                        rasterLayerCanvas = modelCanvas;
                    }
                    if (renderShapes) {
                        shapeLayerCanvas = modelCanvas;
                    }
                    mergeAndSetLayers();
                }
            }

            public void cancel() {
                dem2d.cancel();
            }
        };
        TaskControllerService.addTask(renderTask, taskStatusListener);
    }

    protected void mergeAndSetLayers() {
        ModelContext modelContext;
        try {
            modelContext = this.modelContext.copy();
        } catch (DataSourceException ex) {
            log.error("Failed to copy model context: " + ex.getMessage(), ex);
            return;
        }
        modelContext.getModelOptions().setBackgroundColor("0;0;0;0");
        modelContext.getModelOptions().setAntialiased(true);
        modelContext.getModelOptions().setWidth(1000);
        modelContext.getModelOptions().setHeight(1000);
        try {
            modelContext.updateContext();
        } catch (ModelContextException ex) {
            log.error("Exception updating model context: " + ex.getMessage(), ex);
        }
        ModelCanvas canvas = modelContext.getModelCanvas(true);
        log.info("Merging layers onto canvas of W/H: " + canvas.getWidth() + "/" + canvas.getHeight());
        if (rasterLayerCanvas != null && rasterPreviewEnabled) {
        }
        if (shapeLayerCanvas != null && shapePreviewEnabled) {
        }
        imagePanel.setImage((BufferedImage) canvas.getFinalizedImage());
        repaint();
    }

    protected void applySimpleRasterPreview(ModelCanvas canvas) throws CanvasException {
        log.info("Drawing simple raster preview...");
        int[] i_strokeColor = { Color.DARK_GRAY.getRed(), Color.DARK_GRAY.getGreen(), Color.DARK_GRAY.getBlue(), 255 };
        int[] i_fillColor = { Color.LIGHT_GRAY.getRed(), Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(), 255 };
        int[] textColor = { 0x0, 0x0, 0x0, 0xFF };
        MapProjection projection = canvas.getMapProjection();
        MapPoint point = new MapPoint();
        double latStep = (canvas.getNorth() - canvas.getSouth()) / canvas.getHeight();
        double lonStep = (canvas.getEast() - canvas.getWest()) / canvas.getWidth();
        log.info("*** Canvas Width/Height: " + canvas.getWidth() + "/" + canvas.getHeight());
        log.info("Lat/Lon Steps: " + latStep + "/" + lonStep);
        for (int i = modelContext.getRasterDataContext().getRasterDataListSize() - 1; i >= 0; i--) {
            RasterData rasterData = modelContext.getRasterDataContext().getRasterDataList().get(i);
            double north = rasterData.getNorth();
            double south = rasterData.getSouth();
            double east = rasterData.getEast();
            double west = rasterData.getWest();
            Path2D.Double path = new Path2D.Double();
            try {
                int pointCount = 0;
                for (double latitude = north; latitude >= south; latitude -= latStep) {
                    double longitude = west;
                    projection.getPoint(latitude, longitude, 0.0, point);
                    if (pointCount == 0) {
                        path.moveTo(point.column, point.row);
                    } else {
                        path.lineTo(point.column, point.row);
                    }
                    pointCount = 1;
                }
                for (double longitude = west; longitude <= east; longitude += lonStep) {
                    double latitude = south;
                    projection.getPoint(latitude, longitude, 0.0, point);
                    path.lineTo(point.column, point.row);
                }
                for (double latitude = south; latitude <= north; latitude += latStep) {
                    double longitude = east;
                    projection.getPoint(latitude, longitude, 0.0, point);
                    path.lineTo(point.column, point.row);
                }
                for (double longitude = east; longitude >= west; longitude -= lonStep) {
                    double latitude = north;
                    projection.getPoint(latitude, longitude, 0.0, point);
                    path.lineTo(point.column, point.row);
                }
                path.closePath();
            } catch (MapProjectionException ex) {
                log.error("Failed to project point: " + ex.getMessage(), ex);
                return;
            }
            double coordWidthLat = (Math.abs(90) + Math.abs(-90)) / 12;
            double coordWidthLon = (Math.abs(-180) + Math.abs(180)) / 24;
            for (double latitude = 90; latitude >= -90; latitude -= coordWidthLat) {
                for (double longitude = -180; longitude <= 180; longitude += coordWidthLon) {
                    if (latitude > -90) {
                        canvas.drawLine(i_strokeColor, latitude, longitude, 0.0, latitude - coordWidthLat, longitude, 0.0);
                    }
                    if (longitude < 180) {
                        canvas.drawLine(i_strokeColor, latitude, longitude, 0.0, latitude, longitude + coordWidthLon, 0.0);
                    }
                }
            }
            String label = "#" + (i + 1);
            canvas.drawText(label, textColor, (east + west) / 2.0, (north + south) / 2.0, true);
        }
    }

    public int getPreviewWidth() {
        return previewWidth;
    }

    public void setPreviewWidth(int previewWidth) {
        this.previewWidth = previewWidth;
    }

    public int getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewHeight(int previewHeight) {
        this.previewHeight = previewHeight;
    }

    protected Rectangle getWindowLocation() {
        Point pt = this.getLocationOnScreen();
        Rectangle rec = new Rectangle(pt.x, pt.y, getWidth(), getHeight());
        return rec;
    }
}
