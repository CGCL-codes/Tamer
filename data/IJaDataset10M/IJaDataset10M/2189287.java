package com.ynhenc.gis.ui.viewer_01.map_viewer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.net.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.event.*;
import com.ynhenc.comm.DebugInterface;
import com.ynhenc.comm.file.FileFilter_01_Simple;
import com.ynhenc.comm.file.FileSystem;
import com.ynhenc.comm.util.*;
import com.ynhenc.gis.*;
import com.ynhenc.gis.file.*;
import com.ynhenc.gis.file.esri_shape.*;
import com.ynhenc.gis.model.*;
import com.ynhenc.gis.model.map.*;
import com.ynhenc.gis.model.mapobj.*;
import com.ynhenc.gis.model.renderer.*;
import com.ynhenc.gis.model.shape.*;
import com.ynhenc.gis.model.style.*;
import com.ynhenc.gis.projection.*;
import com.ynhenc.gis.ui.comp.*;
import com.ynhenc.gis.ui.resource.*;
import com.ynhenc.gis.ui.viewer_02.style_editor.*;
import com.ynhenc.gis.ui.viewer_03.dbf_viewer.*;
import com.ynhenc.gis.web.*;
import java.awt.print.*;

public class MapViewer extends JPanel implements Printable, DebugInterface {

    private static final long serialVersionUID = 5463651833144937805L;

    public Request getRequest() {
        return Request.getNulRequest();
    }

    public MapListener getMapViewerListener() {
        return this.mapListener;
    }

    public void setMapViewerListener(MapListener mapListener) {
        this.mapListener = mapListener;
    }

    public JTextField getMemory_TextField() {
        return this.memory_TextField;
    }

    public void setMemory_TextField(JTextField memory_TextField) {
        this.memory_TextField = memory_TextField;
    }

    public LevelScaleEditor getLevelScaleEditor() {
        return this.levelScaleEditor;
    }

    public void setLevelScaleEditor(LevelScaleEditor levelScaleEditor) {
        this.levelScaleEditor = levelScaleEditor;
    }

    public JTextField getScale_TextField() {
        return this.scale_TextField;
    }

    public void setScale_TextField(JTextField scale_TextField) {
        this.scale_TextField = scale_TextField;
    }

    public MapController getMapController() {
        return this.mapController;
    }

    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }

    public LayerFontStyleShower getLayerFontStyleShower() {
        return this.layerFontStyleShower;
    }

    public void setLayerFontStyleShower(LayerFontStyleShower layerFontStyleShower) {
        this.layerFontStyleShower = layerFontStyleShower;
    }

    public JDbfGridPane getDbfGrid() {
        return this.dbfGrid;
    }

    public void setJDbfViewer(JDbfGridPane dbfViewer) {
        this.dbfGrid = dbfViewer;
    }

    public JLayerStyleEditor getJLayerStyleEditor() {
        return this.jLayerStyleEditor;
    }

    public void setJLayerStyleEditor(JLayerStyleEditor layerStyleEditor) {
        this.jLayerStyleEditor = layerStyleEditor;
        layerStyleEditor.setMapViewer(this);
    }

    public void setXPos_TextField(JTextField pos_TextField) {
        this.xPos_TextField = pos_TextField;
    }

    public void setYPos_TextField(JTextField pos_TextField) {
        this.yPos_TextField = pos_TextField;
    }

    public JProgressBar getProgressBar() {
        return this.progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
    }

    public void repaint_MapViewer_After_Creating_Map_Again() {
        final MapViewer mapViewer = this;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                mapViewer.createMapImageAgain();
                mapViewer.repaint();
            }
        });
    }

    public void createMapImageAgain() {
        this.mapImage = null;
    }

    public void whenComponentResized() {
        Dimension size = this.getSize();
        if (size.width < 1 || size.height < 1) {
        } else {
            this.synchProjectionSizeAndMapViwerSize();
            this.repaint();
        }
    }

    public void synchProjectionSizeAndMapViwerSize() {
        GisProject gisProject = this.getGisProject();
        Projection projection = gisProject.getProjection();
        Dimension size = this.getSize();
        if (projection != null) {
            if (!size.equals(projection.getPixelSize())) {
                projection = Projection.getProject(projection.getMbr(), size);
                gisProject.setProjection(projection);
            }
        }
    }

    /**
	 * ���� �޽��� ��� ������Ʈ ���� �ϱ�
	 */
    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void setMbrBar(JTextField mbrBar) {
        this.mbrBar = mbrBar;
    }

    private void debug(String msg) {
        debug.println(this, msg);
    }

    private void debug(Exception e) {
        debug.println(this, e);
    }

    private void debug(Error e) {
        debug.println(this, e);
    }

    public void message(String msg) {
        debug.println(this, msg);
    }

    private void layer_03_AddToGisProjectAfterStyling(Layer layer, int index) {
        if (layer != null) {
            GisProject gisProject = this.getGisProject();
            if (true) {
                LayerStyleFactory.initLayerStyle(layer, index);
                gisProject.getMapData_Base().addLayer(layer);
            }
        }
    }

    public void gisProject_00_Create() {
        MapViewer mapViewer = this;
        GisProjectCreator creator = new GisProjectCreator(mapViewer);
        GisProject gisProject = null;
        try {
            gisProject = creator.getProjectNew();
        } catch (Exception e) {
            this.debug(e);
        }
        if (gisProject != null) {
            mapViewer.setGisProject(gisProject);
        }
    }

    public void layer_00_OpenMultipleFile() {
        if (this.progressBar != null) {
            this.progressBar.setString("");
        }
        GisProject gisProject = this.gisProject;
        if (gisProject != null) {
            File projectFolder = gisProject.getFolderProject();
            if (projectFolder == null || !projectFolder.exists()) {
                String message = "�ű�������Ʈ��  ���� ���Ͽ��� �մϴ�.";
                gisProject.messageDialog(this, message);
            } else {
                FileSystem fs = new FileSystem();
                if (projectFolder != null && projectFolder.exists()) {
                    JFileChooser chooserLayer = this.getFileChooser_01_LayerList();
                    if (true) {
                        File folderMapLayer = gisProject.getFolderMapLayer();
                        chooserLayer.setCurrentDirectory(folderMapLayer);
                    }
                    final File[] fileList = fs.selectFile_Multiple(this, chooserLayer);
                    if (fileList != null && fileList.length > 0) {
                        File fileFirst = fileList[0];
                        if (!fileFirst.getParentFile().equals(gisProject.getFolderMapLayer())) {
                            Component comp = this;
                            String message = "������Ʈ ���ϳ��� SHP ������ �����Ͽ� �ּ���!";
                            gisProject.messageDialog(comp, message);
                        } else {
                            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    try {
                                        MapViewer.this.layer_00_OpenMultipleFile_Runnable(fileList);
                                    } catch (Exception e) {
                                        debug.println(e);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private void executeEsriReaderStateThread(final EsriReaderShpSingleLayer esriReader) {
        final Thread thread = new Thread(new Runnable() {

            public void run() {
                StateEvent stateEvent = esriReader.getStateEvent();
                while (stateEvent != null && stateEvent.getWorkPercent() < 100) {
                    MapViewer.this.paintEsriReaderState_Runnable(esriReader);
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                MapViewer.this.paintEsriReaderState_Runnable(esriReader);
            }
        });
        thread.start();
    }

    private void layer_00_OpenMultipleFile_Runnable(File[] fileList) throws Exception {
        GisProject gisProject = this.gisProject;
        CoordinateConversion coordinateConversion = gisProject.getCoordinateConversion();
        int index = gisProject.getMapData_Base().getLayerSize();
        boolean isNeedStyling = true;
        for (File file : fileList) {
            if (file != null) {
                String urlParentText = "file:///" + file.getParentFile().getAbsoluteFile();
                this.debug("READING LAYER FILE = " + file);
                URL urlParent = new URL(urlParentText);
                String layerName = file.getName();
                if (layerName.toUpperCase().endsWith(".SHP")) {
                    layerName = layerName.substring(0, layerName.length() - 4);
                }
                Layer layer = new Layer(layerName, urlParent);
                if (false) {
                    layer.setSelected(true);
                }
                this.layer_02_OpenSingleUrl(layer, index, isNeedStyling, coordinateConversion);
                index++;
            }
        }
        if (true) {
            this.mapImage = null;
            this.setViewFullExtent();
        }
    }

    public void layer_02_OpenSingleUrl(Layer layer, int index, boolean needStyling, CoordinateConversion coordConv) throws Exception {
        if (false) {
            this.paint_07_Status(layer.getName() + "�� �ε����Դϴ�!");
        }
        EsriReaderShpSingleLayer esriReader = EsriReaderShpSingleLayer.getEsriShapeReader(layer, coordConv);
        this.debug("Reading Main File ......");
        esriReader.readMainFileHeader();
        this.executeEsriReaderStateThread(esriReader);
        if (needStyling) {
            this.layer_03_AddToGisProjectAfterStyling(layer, index);
        }
        if (true) {
            this.layerStyleEditor_01_Repaint();
        }
        try {
            esriReader.readMainFileRecordContents();
        } catch (RuntimeException e) {
            this.debug(e);
            this.debug("FAILED READING LAYER =" + layer.getName());
        } catch (Error e) {
            this.debug(e);
            this.debug("FAILED READING LAYER =" + layer.getName());
        }
        this.debug("Done reading main file.");
    }

    public void layerStyleEditor_01_Repaint() {
        if (false) {
            MapViewer.this.layerStyleEditor_01_Repaint_Runnable();
        } else if (true) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    MapViewer.this.layerStyleEditor_01_Repaint_Runnable();
                }
            });
        }
    }

    private void layerStyleEditor_01_Repaint_Runnable() {
        JLayerStyleEditor jLayerStyleEditor = this.jLayerStyleEditor;
        if (jLayerStyleEditor != null) {
            TableModelGisProject model = jLayerStyleEditor.getTableModel();
            model.setGisProject(this.gisProject);
            if (model.getRowCount() > 0) {
                model.fireTableDataChanged();
                jLayerStyleEditor.validate();
                jLayerStyleEditor.repaint();
            }
        }
        MapViewer mapViewer = this;
        LayerFontStyleShower fontStyleShower = mapViewer.getLayerFontStyleShower();
        if (fontStyleShower != null) {
            Layer layer = null;
            GisProject gisProject = this.getGisProject();
            if (gisProject != null) {
                layer = gisProject.getTargetLayer();
            }
            fontStyleShower.showLayerFontSyle(layer);
        }
    }

    public void removeAllLayerList() {
        this.getGisProject().getMapData_Base().removeAllLayerList();
        this.createMapImageAgain();
        this.layerStyleEditor_01_Repaint();
    }

    private JFileChooser getFileChooser_01_LayerList() {
        if (this.fileChooser_layerList != null) {
            return this.fileChooser_layerList;
        } else {
            JFileChooser chooser = new JFileChooser();
            if (true) {
                chooser.setFileFilter(new FileFilter_01_Simple(new String[] { ".gml", ".xml" }, "GML, XML"));
            }
            if (true) {
                chooser.setFileFilter(new FileFilter_01_Simple(new String[] { ".shp", ".shp" }, "SHP"));
            }
            chooser.setMultiSelectionEnabled(true);
            if (true) {
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            }
            this.fileChooser_layerList = chooser;
            return chooser;
        }
    }

    public JFileChooser getFileChooser_02_GisProject() {
        if (this.fileChooser_gisProject != null) {
            return this.fileChooser_gisProject;
        } else {
            JFileChooser fileChooser = new JFileChooser();
            if (true) {
                fileChooser.setFileFilter(new FileFilter_01_Simple(new String[] { ".XML" }, "XML (Y&H E&C Gis Project)"));
            }
            fileChooser.setMultiSelectionEnabled(false);
            this.fileChooser_gisProject = fileChooser;
            return fileChooser;
        }
    }

    public void paint_07_Status(String status) {
        if (status == null) {
            status = GisRegistry.getGisRegistry().getAppFullName();
            this.status = status;
        } else {
            status = GisRegistry.getGisRegistry().getAppFullName() + status;
        }
        JLabel statusLabel = this.statusLabel;
        if (statusLabel != null) {
            statusLabel.setText(status);
        }
        JTextField mbrBar = this.mbrBar;
        if (mbrBar != null) {
            GisProject gisProject = this.getGisProject();
            Projection projection = gisProject.getProjection();
            if (projection != null && projection.getMbr() != null) {
                mbrBar.setText("" + projection.getMbr());
            }
        }
        GisProject gisProject = this.gisProject;
        if (gisProject != null) {
            int zoomLevel = gisProject.getZoomLevelList().getZoomLevelCurr();
            ArrayList<JSpinner> zoomLevelSpinnerList = this.zoomLevelSpinnerList;
            for (JSpinner spinner : zoomLevelSpinnerList) {
                if (spinner.getValue().equals(zoomLevel)) {
                } else {
                    spinner.setValue(zoomLevel);
                }
            }
        }
        JTextField scale_TextField = this.getScale_TextField();
        if (scale_TextField != null) {
            Projection projection = gisProject != null ? gisProject.getProjection() : null;
            if (projection != null) {
                double mapScale = projection.getMapScale();
                NumberFormat formatter = NumberFormat.getInstance();
                String msg = formatter.format(mapScale);
                scale_TextField.setText(msg);
            }
        }
        JTextField memory_TextField = this.getMemory_TextField();
        if (memory_TextField != null) {
            Runtime runtime = Runtime.getRuntime();
            String max = "T:" + (runtime.maxMemory() / 1024);
            String alloc = "A:" + (runtime.totalMemory() / 1024);
            String free = "F:" + (runtime.freeMemory() / 1024);
            memory_TextField.setText(max + "/" + alloc + "/" + free);
        }
    }

    public void paint_02_Layers(Graphics2D g2, Projection projection) {
        GisProject gisProject = this.gisProject;
        if (gisProject != null) {
            LayerPainter painter = LayerPainter.newLayerPainter();
            int zoomLevelCurr = gisProject.getZoomLevelList().getZoomLevelCurr();
            Request req = this.getRequest();
            long then = System.currentTimeMillis();
            painter.paint_01_GisProject(g2, gisProject, zoomLevelCurr, projection, req);
            painter.paint_03_Option(g2, gisProject, projection, zoomLevelCurr, then);
        }
        if (false) {
            RenderingHints renderingHints = GraphicsUtil.getRenderingHints_High();
            this.paint_04_SelShapeLayer(g2, projection, renderingHints);
        }
    }

    @Override
    public String toString() {
        return "size = " + this.getSize() + ", project = " + this.gisProject.getProjection();
    }

    public GeoPoint getSpatialPoint(int x, int y) {
        return this.gisProject.getProjection().toSpatial(x, y);
    }

    public PntShort toGraphics(double x, double y) {
        return this.gisProject.getProjection().toGraphics(x, y);
    }

    private void doShowAttributeModeMousePressed(MouseEvent e) {
        debug.println(this, "Showing attribute ......");
        int x = e.getX(), y = e.getY();
        GisProject gisProject = this.getGisProject();
        if (gisProject != null) {
            MapDataObject mapDataBase = gisProject.getMapData_Base();
            Layer layer = mapDataBase.getLayer(mapDataBase.getLayerSize() - 1);
            if (layer != null) {
                Projection projection = gisProject.getProjection();
                Layer srchLyr = layer.getShapesIncludes(x, y, projection);
                this.selShpLayer = srchLyr;
                Graphics2D g2 = (Graphics2D) (this.getGraphics());
                RenderingHints renderingHints = GraphicsUtil.getRenderingHints_High();
                this.paint_04_SelShapeLayer(g2, projection, renderingHints);
                ArrayList<ShapeObject> shapeList = layer.getShapeList();
                for (ShapeObject shape : shapeList) {
                    this.showAttribute(shape);
                }
            }
        }
        this.debug("End of Showing attribute ......");
    }

    public void showAttribute(ShapeObject shape) {
        JOptionPane.showMessageDialog(this, shape.toString(), "�Ӽ�", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setSelectViewExtent() {
        this.getMapController().setZoomIn();
        this.repaint();
    }

    public void setZoomLevel(int zoomLevel) {
        this.getMapController().setZoomLevel(zoomLevel);
        this.repaint();
        this.layerStyleEditor_01_Repaint();
    }

    public void setZoomIn() {
        this.getMapController().setZoomIn();
        this.repaint();
        this.layerStyleEditor_01_Repaint();
    }

    public void setZoomOut() {
        this.getMapController().setZoomOut();
        this.repaint();
        this.layerStyleEditor_01_Repaint();
    }

    public void setViewFullExtent() {
        this.getMapController().setViewFullExtent();
        this.repaint();
        this.layerStyleEditor_01_Repaint();
    }

    public void setPan() {
    }

    private void setPan(int dx, int dy) {
        this.getMapController().setPan(dx, dy);
        this.repaint();
    }

    private void init() {
        this.inited = true;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paint(g);
        if (!this.inited) {
            this.init();
            this.setDoubleBuffered(false);
        }
        if (!this.isVisible() || !this.isShowing()) {
            return;
        }
        Dimension size = this.getSize();
        if (size.width < 1 || size.height < 1) {
            return;
        }
        if (this.isPainting) {
            return;
        }
        this.isPainting = true;
        try {
            this.paint_07_Status(this.status);
            boolean doubleBuffered = true;
            if (doubleBuffered) {
                this.paint_10_DoubleBuffered(g2);
            } else {
                this.paint_11_Directly(g2);
            }
            this.debug("Done painting.");
        } catch (Exception e) {
            this.isPainting = false;
            this.debug(e);
        } finally {
            this.isPainting = false;
        }
    }

    public void paint_10_DoubleBuffered(Graphics2D g2) {
        GisProject gisProject = this.getGisProject();
        BufferedImage mapImage = this.getMapImage(gisProject);
        g2.drawImage(mapImage, 0, 0, this);
    }

    public void paint_11_Directly(Graphics2D g2) {
        Dimension size = this.getSize();
        try {
            GisProject gisProject = this.getGisProject();
            Projection projection = gisProject.getProjection();
            this.paint_01_MapViewer(g2, projection);
        } catch (Exception ex) {
            debug.println(ex);
        } catch (Error ex) {
            debug.println(ex);
        }
    }

    private void paint_01_MapViewer(Graphics2D g2, Projection projection) {
        java.awt.Shape orgClip = g2.getClip();
        Dimension size = this.getSize();
        g2.setClip(0, 0, size.width, size.height);
        g2.setColor(Color.white);
        g2.fillRect(0, 0, size.width, size.height);
        this.paint_02_Layers(g2, projection);
        g2.setClip(orgClip);
    }

    private void paint_04_SelShapeLayer(Graphics2D g2, Projection projection, RenderingHints renderingHints) {
        Layer layer = this.selShpLayer;
        ShapeObjectList shapeObjListIntersects;
        if (layer != null) {
            shapeObjListIntersects = layer.getShapeListIntersects(projection);
            layer.paintShapeList(g2, projection, shapeObjListIntersects, renderingHints, 3);
        }
    }

    public void goViewNext() {
    }

    public void goViewPrev() {
    }

    public void paintEsriReaderState_Runnable(EsriReaderShpSingleLayer esriReader) {
        StateEvent stateEvent = esriReader.getStateEvent();
        JProgressBar progressBar = this.progressBar;
        if (progressBar != null && progressBar.isVisible() && progressBar.isShowing()) {
            int workPercent = stateEvent.getWorkPercent();
            progressBar.setValue(workPercent);
            progressBar.setString("" + workPercent + "%");
            if (true) {
                progressBar.repaint();
            } else {
                progressBar.updateUI();
                progressBar.update(progressBar.getGraphics());
            }
            if (false && workPercent >= 100) {
                progressBar.setValue(0);
                if (false) {
                    progressBar.setString("");
                }
            }
        }
    }

    public void gisProject_01_OpenFile() {
        GisProjectOpener opener = new GisProjectOpener(this);
        opener.gisProject_01_OpenFile();
    }

    public void gisProject_01_OpenFile_RecentProject(String projectFilePath) {
        GisProjectOpener opener = new GisProjectOpener(this);
        opener.gisProject_01_OpenFile_RecentProject(projectFilePath);
    }

    public void gisProject_02_Save() {
        GisProjectSaver saver = new GisProjectSaver(this);
        saver.gisProject_02_Save();
    }

    public GisProject getGisProject() {
        return this.gisProject;
    }

    public void setGisProject(GisProject gisProject) {
        this.gisProject = gisProject;
        this.mapController.setGisProject(gisProject);
        if (this.jLayerStyleEditor != null) {
            this.jLayerStyleEditor.setGisProject(gisProject);
        }
        if (this.levelScaleEditor != null) {
            this.levelScaleEditor.repaint();
        }
        MapViewer mapViewer = this;
        if (true) {
            mapViewer.layerStyleEditor_01_Repaint();
        }
        if (true) {
            mapViewer.synchProjectionSizeAndMapViwerSize();
            mapViewer.repaint();
        }
    }

    public void print() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (PrinterException pe) {
                System.out.println("Error printing: " + pe);
            }
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return (NO_SUCH_PAGE);
        } else {
            Graphics2D g2 = (Graphics2D) g;
            double x = pageFormat.getImageableX();
            double y = pageFormat.getImageableY();
            double w = pageFormat.getImageableWidth();
            double h = pageFormat.getImageableHeight();
            GisProject gisProject = this.getGisProject();
            Projection projection = gisProject.getProjection();
            Mbr mbr = projection.getMbr();
            Projection projectionPrint = Projection.getProject(mbr, (int) w, (int) h);
            g2.translate(x, y);
            this.paint_01_MapViewer(g2, projectionPrint);
            g2.translate(-x, -y);
            return (PAGE_EXISTS);
        }
    }

    public synchronized BufferedImage getMapImage(GisProject gisProject) {
        this.synchProjectionSizeAndMapViwerSize();
        Projection projection = gisProject.getProjection();
        if (true) {
            this.debug("Level = " + gisProject.getZoomLevelCurr() + ", " + projection.getMbr());
        }
        Projection projectionPrev = gisProject.getProjectionPrev();
        if (this.mapImage == null || !projection.equals(projectionPrev)) {
            this.mapImage = this.createMapImage(projection);
            gisProject.setProjectionPrev(projection);
        }
        return this.mapImage;
    }

    private BufferedImage createMapImage(Projection projection) {
        Dimension size = this.getSize();
        BufferedImage mapImage = ImageUtil.createImage(size.width, size.height);
        Graphics2D g2 = (Graphics2D) (mapImage.getGraphics());
        this.paint_01_MapViewer(g2, projection);
        this.debug("Done Creating Map Image.");
        return mapImage;
    }

    public void setStatus(GeoPoint geoPoint) {
        if (geoPoint != null) {
            if (this.xPos_TextField != null && this.yPos_TextField != null) {
                this.xPos_TextField.setText("" + geoPoint.getX());
                this.yPos_TextField.setText("" + geoPoint.getY());
            }
        }
    }

    public void setAddZoomLevelSpinner(JSpinner spinner) {
        this.zoomLevelSpinnerList.add(spinner);
    }

    public MapViewer() {
        this.isPainting = false;
        this.gisProject = new GisProject();
        this.mapController = new MapController_02_Level(this.gisProject);
        this.progressBar = new RealProgressBar(this, new Dimension(150, 25));
        this.setFont(FontManager.getFont("Serif", Font.PLAIN, 12));
        MapListener mapListener = this.getMapViewerListener();
        this.addMouseWheelListener(mapListener);
        this.addMouseMotionListener(mapListener);
        this.addMouseListener(mapListener);
        this.addComponentListener(mapListener);
        this.addKeyListener(mapListener);
    }

    private MapListener mapListener = new MapListener(this);

    private BufferedImage mapImage;

    private boolean isPainting;

    private JProgressBar progressBar;

    private JTextField xPos_TextField;

    private JTextField yPos_TextField;

    private JTextField scale_TextField;

    private JTextField memory_TextField;

    private JLayerStyleEditor jLayerStyleEditor;

    private JDbfGridPane dbfGrid;

    private LayerFontStyleShower layerFontStyleShower;

    private ArrayList<JSpinner> zoomLevelSpinnerList = new ArrayList<JSpinner>();

    private LevelScaleEditor levelScaleEditor;

    private String status;

    private JLabel statusLabel;

    private JTextField mbrBar;

    private Layer selShpLayer;

    private GisProject gisProject;

    private static JFileChooser fileChooser_layerList;

    private static JFileChooser fileChooser_gisProject;

    private boolean inited;

    private MapController mapController;
}
