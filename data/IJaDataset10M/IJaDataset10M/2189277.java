package jsattrak.gui;

import gov.nasa.worldwind.Configuration;
import jsattrak.objects.GroundStation;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.AWTInputHandler;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.PositionListener;
import gov.nasa.worldwind.examples.WMSLayersPanel;
import gov.nasa.worldwind.examples.sunlight.AtmosphereLayer;
import gov.nasa.worldwind.examples.sunlight.LensFlareLayer;
import gov.nasa.worldwind.examples.sunlight.RectangularNormalTessellator;
import gov.nasa.worldwind.examples.sunlight.SunPositionProvider;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Earth.CountryBoundariesLayer;
import gov.nasa.worldwind.layers.Earth.LandsatI3;
import gov.nasa.worldwind.layers.Earth.USGSTopographicMaps;
import gov.nasa.worldwind.layers.Earth.USGSUrbanAreaOrtho;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.Mercator.examples.OSMCycleMapLayer;
import gov.nasa.worldwind.layers.Mercator.examples.OSMMapnikLayer;
import gov.nasa.worldwind.layers.Mercator.examples.OSMMapnikTransparentLayer;
import gov.nasa.worldwind.layers.Mercator.examples.VirtualEarthLayer;
import gov.nasa.worldwind.layers.Mercator.examples.YahooMapsLayer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.layers.StarsLayer;
import gov.nasa.worldwind.layers.TerrainProfileLayer;
import gov.nasa.worldwind.layers.TiledImageLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.render.Polyline;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwind.view.BasicOrbitView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jsattrak.coverage.CoverageAnalyzer;
import jsattrak.objects.AbstractSatellite;
import jsattrak.utilities.ECEFModelRenderable;
import jsattrak.utilities.J3DEarthComponent;
import jsattrak.utilities.OrbitModelRenderable;
import name.gano.file.SaveImageFile;
import name.gano.swingx.fullscreen.ToggleFullscreen;
import name.gano.worldwind.WwjUtils;
import name.gano.worldwind.geom.ECIRadialGrid;
import name.gano.worldwind.layers.Earth.CoverageRenderableLayer;
import name.gano.worldwind.layers.Earth.ECEFRenderableLayer;
import name.gano.worldwind.layers.Earth.ECIRenderableLayer;
import name.gano.worldwind.layers.Earth.EcefTimeDepRenderableLayer;
import name.gano.worldwind.sunshader.CustomSunPositionProvider;
import name.gano.worldwind.view.AutoClipBasicOrbitView;
import name.gano.worldwind.view.BasicModelView3;
import name.gano.worldwind.view.BasicModelViewInputHandler3;

/**
 *
 * @author  Shawn
 */
public class J3DEarthInternalPanel extends javax.swing.JPanel implements J3DEarthComponent {

    private WorldWindowGLJPanel wwd;

    StatusBar statusBar;

    JInternalFrame parent;

    ECIRenderableLayer eciLayer;

    ECEFRenderableLayer ecefLayer;

    EcefTimeDepRenderableLayer timeDepLayer;

    OrbitModelRenderable orbitModel;

    ECEFModelRenderable ecefModel;

    TerrainProfileLayer terrainProfileLayer;

    CoverageRenderableLayer cel;

    private boolean viewModeECI = true;

    private static final String[] servers = new String[] { "http://neowms.sci.gsfc.nasa.gov/wms/wms", "http://mapserver.flightgear.org/cgi-bin/landcover", "http://wms.jpl.nasa.gov/wms.cgi", "http://labs.metacarta.com/wms/vmap0" };

    private JSatTrak app;

    StarsLayer starsLayer;

    Hashtable<String, AbstractSatellite> satHash;

    Hashtable<String, GroundStation> gsHash;

    private String terrainProfileSat = "";

    private double terrainProfileLongSpan = 10.0;

    private boolean modelViewMode = false;

    private String modelViewString = "";

    private double modelViewNearClip = 10000;

    private double modelViewFarClip = 5.0E7;

    private boolean smoothViewChanges = true;

    private double farClippingPlaneDistOrbit = -1;

    private double nearClippingPlaneDistOrbit = -1;

    ViewControlsLayer viewControlsLayer;

    private RectangularNormalTessellator tessellator;

    private LensFlareLayer lensFlareLayer;

    private AtmosphereLayer atmosphereLayer;

    private SunPositionProvider spp;

    private boolean sunShadingOn = false;

    private ECIRadialGrid eciRadialGrid = new ECIRadialGrid();

    /** Creates new form J3DEarthPanel
     * @param parent
     * @param satHash
     * @param gsHash
     * @param currentMJD
     * @param app 
     */
    public J3DEarthInternalPanel(JInternalFrame parent, Hashtable<String, AbstractSatellite> satHash, Hashtable<String, GroundStation> gsHash, double currentMJD, JSatTrak app) {
        this.parent = parent;
        this.app = app;
        this.satHash = satHash;
        this.gsHash = gsHash;
        initComponents();
        Configuration.setValue(AVKey.INITIAL_LATITUDE, 38.0);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, -90.0);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 1.913445320360136E7);
        Configuration.setValue(AVKey.INITIAL_HEADING, 0.0);
        Configuration.setValue(AVKey.INITIAL_PITCH, 0.0);
        Configuration.setValue(AVKey.TESSELLATOR_CLASS_NAME, RectangularNormalTessellator.class.getName());
        wwd = new WorldWindowGLJPanel(app.getWwd());
        wwd.setPreferredSize(new java.awt.Dimension(600, 400));
        this.add(wwd, java.awt.BorderLayout.CENTER);
        Model m = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
        m.setShowWireframeExterior(false);
        m.setShowWireframeInterior(false);
        m.setShowTessellationBoundingVolumes(false);
        m.getLayers().add(new CountryBoundariesLayer());
        VirtualEarthLayer ve = new VirtualEarthLayer();
        ve.setEnabled(false);
        m.getLayers().add(ve);
        YahooMapsLayer ya = new YahooMapsLayer();
        ya.setEnabled(false);
        m.getLayers().add(ya);
        OSMMapnikLayer ol = new OSMMapnikLayer();
        ol.setEnabled(false);
        m.getLayers().add(ol);
        OSMCycleMapLayer ol2 = new OSMCycleMapLayer();
        ol2.setEnabled(false);
        m.getLayers().add(ol2);
        OSMMapnikTransparentLayer ol3 = new OSMMapnikTransparentLayer();
        ol3.setEnabled(false);
        m.getLayers().add(ol3);
        viewControlsLayer = new ViewControlsLayer();
        viewControlsLayer.setLayout(AVKey.VERTICAL);
        viewControlsLayer.setScale(6 / 10d);
        viewControlsLayer.setPosition(AVKey.SOUTHEAST);
        viewControlsLayer.setLocationOffset(new Vec4(15, 35, 0, 0));
        viewControlsLayer.setEnabled(true);
        m.getLayers().add(viewControlsLayer);
        this.getWwd().addSelectListener(new ViewControlsSelectListener(wwd, viewControlsLayer));
        for (Layer layer : m.getLayers()) {
            if (layer instanceof TiledImageLayer) {
                ((TiledImageLayer) layer).setShowImageTileOutlines(false);
            }
            if (layer instanceof LandsatI3) {
                ((TiledImageLayer) layer).setDrawBoundingVolumes(false);
                ((TiledImageLayer) layer).setEnabled(false);
            }
            if (layer instanceof CompassLayer) {
                ((CompassLayer) layer).setShowTilt(true);
                ((CompassLayer) layer).setEnabled(false);
            }
            if (layer instanceof PlaceNameLayer) {
                ((PlaceNameLayer) layer).setEnabled(false);
            }
            if (layer instanceof WorldMapLayer) {
                ((WorldMapLayer) layer).setEnabled(false);
            }
            if (layer instanceof USGSUrbanAreaOrtho) {
                ((USGSUrbanAreaOrtho) layer).setEnabled(false);
            }
            if (layer instanceof StarsLayer) {
                starsLayer = (StarsLayer) layer;
                starsLayer.setRadius(starsLayer.getRadius() * 10.0);
            }
            if (layer instanceof CountryBoundariesLayer) {
                ((CountryBoundariesLayer) layer).setEnabled(false);
            }
        }
        wwd.setModel(m);
        USGSTopographicMaps topo = new USGSTopographicMaps();
        topo.setEnabled(false);
        WwjUtils.insertBeforePlacenames(getWwd(), topo);
        cel = new CoverageRenderableLayer(app.getCoverageAnalyzer());
        m.getLayers().add(cel);
        timeDepLayer = new EcefTimeDepRenderableLayer(currentMJD, app);
        m.getLayers().add(timeDepLayer);
        eciLayer = new ECIRenderableLayer(currentMJD);
        orbitModel = new OrbitModelRenderable(satHash, wwd.getModel().getGlobe());
        eciLayer.addRenderable(orbitModel);
        eciLayer.setCurrentMJD(currentMJD);
        m.getLayers().add(eciLayer);
        eciLayer.addRenderable(eciRadialGrid);
        ecefLayer = new ECEFRenderableLayer();
        ecefModel = new ECEFModelRenderable(satHash, gsHash, wwd.getModel().getGlobe());
        ecefLayer.addRenderable(ecefModel);
        m.getLayers().add(ecefLayer);
        terrainProfileLayer = new TerrainProfileLayer();
        m.getLayers().add(terrainProfileLayer);
        terrainProfileLayer.setEventSource(this.getWwd());
        terrainProfileLayer.setStartLatLon(LatLon.fromDegrees(0.0, 0.0));
        terrainProfileLayer.setEndLatLon(LatLon.fromDegrees(50.0, 50.0));
        terrainProfileLayer.setFollow(TerrainProfileLayer.FOLLOW_NONE);
        terrainProfileLayer.setEnabled(false);
        RenderableLayer latLongLinesLayer = createLatLongLinesLayer();
        latLongLinesLayer.setName("Lat/Long Lines");
        latLongLinesLayer.setEnabled(false);
        m.getLayers().add(latLongLinesLayer);
        statusBar = new StatusBar();
        this.add(statusBar, java.awt.BorderLayout.PAGE_END);
        statusBar.setEventSource(wwd);
        starsLayer.setLongitudeOffset(Angle.fromDegrees(-eciLayer.getRotateECIdeg()));
        spp = new CustomSunPositionProvider(app.getSun());
        this.atmosphereLayer = new AtmosphereLayer();
        this.lensFlareLayer = LensFlareLayer.getPresetInstance(LensFlareLayer.PRESET_BOLD);
        this.getWwd().getModel().getLayers().add(this.lensFlareLayer);
        this.tessellator = (RectangularNormalTessellator) getWwd().getModel().getGlobe().getTessellator();
        this.tessellator.setAmbientColor(new Color(0.50f, 0.50f, 0.50f));
        getWwd().addPositionListener(new PositionListener() {

            Vec4 eyePoint;

            public void moved(PositionEvent event) {
                if (eyePoint == null || eyePoint.distanceTo3(getWwd().getView().getEyePoint()) > 1000) {
                    update(true);
                    eyePoint = getWwd().getView().getEyePoint();
                }
            }
        });
        setSunShadingOn(true);
        setupView();
    }

    private void update(boolean redraw) {
        if (sunShadingOn) {
            LatLon sunPos = spp.getPosition();
            Vec4 sun = getWwd().getModel().getGlobe().computePointFromPosition(new Position(sunPos, 0)).normalize3();
            Vec4 light = sun.getNegative3();
            this.tessellator.setLightDirection(light);
            this.lensFlareLayer.setSunDirection(sun);
            this.atmosphereLayer.setSunDirection(sun);
            if (redraw) {
                this.getWwd().redraw();
            }
        }
    }

    public void setSunShadingOn(boolean useSunShading) {
        if (useSunShading == sunShadingOn) {
            return;
        }
        sunShadingOn = useSunShading;
        if (sunShadingOn) {
            for (int i = 0; i < this.getWwd().getModel().getLayers().size(); i++) {
                Layer l = this.getWwd().getModel().getLayers().get(i);
                if (l instanceof SkyGradientLayer) {
                    this.getWwd().getModel().getLayers().set(i, this.atmosphereLayer);
                }
            }
        } else {
            this.tessellator.setLightDirection(null);
            this.lensFlareLayer.setSunDirection(null);
            this.atmosphereLayer.setSunDirection(null);
            for (int i = 0; i < this.getWwd().getModel().getLayers().size(); i++) {
                Layer l = this.getWwd().getModel().getLayers().get(i);
                if (l instanceof AtmosphereLayer) {
                    this.getWwd().getModel().getLayers().set(i, new SkyGradientLayer());
                }
            }
        }
        this.update(true);
    }

    public boolean isSunShadingOn() {
        return sunShadingOn;
    }

    /**
     * set the ambient light level for sun shading
     * @param level 0-100
     */
    public void setAmbientLightLevel(int level) {
        this.tessellator.setAmbientColor(new Color(level / 100.0f, level / 100.0f, level / 100.0f));
        this.update(true);
    }

    /**
     *
     * @return ambient light level 0-100
     */
    public int getAmbientLightLevel() {
        return (int) Math.round(100.0 * this.tessellator.getAmbientColor().getRed() / 255.0);
    }

    /**
     * if lens flair rendering is enabeled
     * @return
     */
    public boolean isLensFlareEnabled() {
        return lensFlareLayer.isEnabled();
    }

    /**
     * Set if the lens flair and sun are rendered if sun shading is enabeled
     * @param enabled
     */
    public void setLensFlare(boolean enabled) {
        lensFlareLayer.setEnabled(enabled);
    }

    private RenderableLayer createLatLongLinesLayer() {
        RenderableLayer shapeLayer = new RenderableLayer();
        ArrayList<Position> positions = new ArrayList<Position>(3);
        double height = 30e3;
        for (double lon = -180; lon < 180; lon += 10) {
            Angle longitude = Angle.fromDegrees(lon);
            positions.clear();
            positions.add(new Position(Angle.NEG90, longitude, height));
            positions.add(new Position(Angle.ZERO, longitude, height));
            positions.add(new Position(Angle.POS90, longitude, height));
            Polyline polyline = new Polyline(positions);
            polyline.setFollowTerrain(false);
            polyline.setNumSubsegments(30);
            if (lon == -180 || lon == 0) {
                polyline.setColor(new Color(1f, 1f, 0f, 0.5f));
            } else {
                polyline.setColor(new Color(1f, 1f, 1f, 0.5f));
            }
            shapeLayer.addRenderable(polyline);
        }
        for (double lat = -80; lat < 90; lat += 10) {
            Angle latitude = Angle.fromDegrees(lat);
            positions.clear();
            positions.add(new Position(latitude, Angle.NEG180, height));
            positions.add(new Position(latitude, Angle.ZERO, height));
            positions.add(new Position(latitude, Angle.POS180, height));
            Polyline polyline = new Polyline(positions);
            polyline.setPathType(Polyline.LINEAR);
            polyline.setFollowTerrain(false);
            polyline.setNumSubsegments(30);
            if (lat == 0) {
                polyline.setColor(new Color(1f, 1f, 0f, 0.5f));
            } else {
                polyline.setColor(new Color(1f, 1f, 1f, 0.5f));
            }
            shapeLayer.addRenderable(polyline);
        }
        return shapeLayer;
    }

    public void setFocusWWJ() {
        wwd.requestFocusInWindow();
    }

    private void initComponents() {
        jToolBar1 = new javax.swing.JToolBar();
        viewPropButton = new javax.swing.JButton();
        globeLayersButton = new javax.swing.JButton();
        wmsButton = new javax.swing.JButton();
        terrainProfileButton = new javax.swing.JButton();
        screenCaptureButton = new javax.swing.JButton();
        genMovieButton = new javax.swing.JButton();
        fullScreenButton = new javax.swing.JButton();
        wwjPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        setLayout(new java.awt.BorderLayout());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        viewPropButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/other/eye24.png")));
        viewPropButton.setToolTipText("View Properties");
        viewPropButton.setFocusable(false);
        viewPropButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        viewPropButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        viewPropButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPropButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(viewPropButton);
        globeLayersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/other/applications-internet.png")));
        globeLayersButton.setToolTipText("Globe Layers");
        globeLayersButton.setFocusable(false);
        globeLayersButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        globeLayersButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        globeLayersButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                globeLayersButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(globeLayersButton);
        wmsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/other/folder-remote.png")));
        wmsButton.setToolTipText("Manage Web Map Services");
        wmsButton.setFocusable(false);
        wmsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        wmsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        wmsButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wmsButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(wmsButton);
        terrainProfileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/gnome_2_18/stock_chart-autoformat.png")));
        terrainProfileButton.setToolTipText("Terrain Profiler");
        terrainProfileButton.setFocusable(false);
        terrainProfileButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        terrainProfileButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        terrainProfileButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terrainProfileButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(terrainProfileButton);
        screenCaptureButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/gnome_2_18/applets-screenshooter22.png")));
        screenCaptureButton.setToolTipText("Screenshot");
        screenCaptureButton.setFocusable(false);
        screenCaptureButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        screenCaptureButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        screenCaptureButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                screenCaptureButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(screenCaptureButton);
        genMovieButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/other/applications-multimedia.png")));
        genMovieButton.setToolTipText("Create Movie");
        genMovieButton.setFocusable(false);
        genMovieButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        genMovieButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        genMovieButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genMovieButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(genMovieButton);
        fullScreenButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/other/view-fullscreen.png")));
        fullScreenButton.setToolTipText("Fullscreen Mode - press Esc to exit");
        fullScreenButton.setFocusable(false);
        fullScreenButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fullScreenButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        fullScreenButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullScreenButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(fullScreenButton);
        add(jToolBar1, java.awt.BorderLayout.PAGE_START);
        wwjPanel.setBackground(new java.awt.Color(0, 0, 0));
        javax.swing.GroupLayout wwjPanelLayout = new javax.swing.GroupLayout(wwjPanel);
        wwjPanel.setLayout(wwjPanelLayout);
        wwjPanelLayout.setHorizontalGroup(wwjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        wwjPanelLayout.setVerticalGroup(wwjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 252, Short.MAX_VALUE));
        add(wwjPanel, java.awt.BorderLayout.CENTER);
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 15));
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 15, Short.MAX_VALUE));
        add(jPanel2, java.awt.BorderLayout.PAGE_END);
    }

    private void globeLayersButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String windowName = "Globe Layers";
        JDialog iframe = new JDialog(app, windowName, false);
        Container cp = iframe.getContentPane();
        LayerList layerList = wwd.getModel().getLayers();
        JPanel westContainer = new JPanel(new BorderLayout());
        {
            JPanel westPanel = new JPanel(new GridLayout(0, 1, 0, 10));
            westPanel.setBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9));
            {
                JPanel layersPanel = new JPanel(new GridLayout(0, 1, 0, layerList.size()));
                layersPanel.setBorder(new TitledBorder("Layers"));
                for (Layer currentLayer : layerList) {
                    LayerAction la = new LayerAction(currentLayer, currentLayer.isEnabled(), wwd);
                    JCheckBox jcb = new JCheckBox(la);
                    jcb.setSelected(la.selected);
                    layersPanel.add(jcb);
                }
                westPanel.add(layersPanel);
                westContainer.add(westPanel, BorderLayout.NORTH);
            }
        }
        wwd.getModel().setLayers(layerList);
        JScrollPane jsp = new JScrollPane(westContainer);
        cp.add(jsp);
        iframe.setSize(200 + 50, 350 + 40);
        Point p = this.getWwdLocationOnScreen();
        iframe.setLocation(p.x + 15, p.y + 15);
        setLookandFeel(iframe);
        iframe.setVisible(true);
    }

    private int previousTabIndex = 0;

    private void wmsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(new JPanel());
        tabbedPane.setTitleAt(0, "+");
        tabbedPane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent changeEvent) {
                if (tabbedPane.getSelectedIndex() != 0) {
                    previousTabIndex = tabbedPane.getSelectedIndex();
                    return;
                }
                String server = JOptionPane.showInputDialog("Enter wms server URL");
                if (server == null || server.length() < 1) {
                    tabbedPane.setSelectedIndex(previousTabIndex);
                    return;
                }
                if (addWMSTab(tabbedPane.getTabCount(), server.trim(), tabbedPane) != null) {
                    tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                }
            }
        });
        for (int i = 0; i < servers.length; i++) {
            addWMSTab(i + 1, servers[i], tabbedPane);
        }
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() > 0 ? 1 : 0);
        previousTabIndex = tabbedPane.getSelectedIndex();
        String windowName = "Web Map Services";
        JDialog iframe = new JDialog(app, windowName, false);
        Container cp = iframe.getContentPane();
        cp.add(tabbedPane);
        iframe.setSize(480, 350);
        Point p = parent.getLocation();
        iframe.setLocation(p.x + 15, p.y + 15);
        setLookandFeel(iframe);
        iframe.setVisible(true);
    }

    private void screenCaptureButtonActionPerformed(java.awt.event.ActionEvent evt) {
        createScreenCapture();
    }

    private void viewPropButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JThreeDViewPropPanel newPanel = new JThreeDViewPropPanel(app, this, wwd);
        String windowName = "3D View Settings";
        JDialog iframe = new JDialog(app, windowName, false);
        iframe.setContentPane(newPanel);
        iframe.setSize(220 + 330, 260 + 65 + 185);
        Point p = this.getLocationOnScreen();
        iframe.setLocation(p.x + 15, p.y + 55);
        newPanel.setParentDialog(iframe);
        setLookandFeel(iframe);
        iframe.setVisible(true);
    }

    private void setLookandFeel(JDialog iframe) {
        iframe.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/logo/JSatTrakLogo_16.png")));
        boolean canBeDecoratedByLAF = UIManager.getLookAndFeel().getSupportsWindowDecorations();
        if (canBeDecoratedByLAF != iframe.isUndecorated()) {
            iframe.dispose();
            if (!canBeDecoratedByLAF) {
                iframe.setUndecorated(false);
                iframe.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            } else {
                iframe.setUndecorated(true);
                iframe.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
            }
        }
    }

    private void genMovieButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JCreateMovieDialog panel = new JCreateMovieDialog(app, false, this, app);
        Point p = this.getLocationOnScreen();
        panel.setLocation(p.x + 15, p.y + 55);
        setLookandFeel(panel);
        panel.setVisible(true);
    }

    private void terrainProfileButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JTerrainProfileDialog panel = new JTerrainProfileDialog(app, false, app, this);
        Point p = this.getLocationOnScreen();
        panel.setLocation(p.x + 15, p.y + 55);
        setLookandFeel(panel);
        panel.setVisible(true);
    }

    private void fullScreenButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new ToggleFullscreen(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(), wwd, this);
    }

    private WMSLayersPanel addWMSTab(int position, String server, JTabbedPane tabbedPane) {
        try {
            WMSLayersPanel layersPanel = new WMSLayersPanel(wwd, server, new Dimension(200, 200));
            tabbedPane.add(layersPanel, BorderLayout.CENTER);
            String title = layersPanel.getServerDisplayString();
            tabbedPane.setTitleAt(position, title != null && title.length() > 0 ? title : server);
            layersPanel.addPropertyChangeListener("LayersPanelUpdated", new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                }
            });
            return layersPanel;
        } catch (URISyntaxException e) {
            JOptionPane.showMessageDialog(null, "Server URL is invalid", "Invalid Server URL", JOptionPane.ERROR_MESSAGE);
            tabbedPane.setSelectedIndex(previousTabIndex);
            return null;
        }
    }

    private javax.swing.JButton fullScreenButton;

    private javax.swing.JButton genMovieButton;

    private javax.swing.JButton globeLayersButton;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JButton screenCaptureButton;

    private javax.swing.JButton terrainProfileButton;

    private javax.swing.JButton viewPropButton;

    private javax.swing.JButton wmsButton;

    private javax.swing.JPanel wwjPanel;

    public boolean isViewModeECI() {
        return viewModeECI;
    }

    public void setViewModeECI(boolean viewModeECI) {
        this.viewModeECI = viewModeECI;
        if (viewModeECI) {
            starsLayer.setLongitudeOffset(Angle.fromDegrees(-eciLayer.getRotateECIdeg()));
        } else {
            starsLayer.setLongitudeOffset(Angle.fromDegrees(0.0));
        }
    }

    public JSatTrak getApp() {
        return app;
    }

    public WorldWindow getWwd() {
        return wwd;
    }

    public int getWwdWidth() {
        return wwd.getWidth();
    }

    public int getWwdHeight() {
        return wwd.getHeight();
    }

    public Point getWwdLocationOnScreen() {
        return wwd.getLocationOnScreen();
    }

    public String getTerrainProfileSat() {
        return terrainProfileSat;
    }

    public void setTerrainProfileSat(String terrainProfileSat) {
        this.terrainProfileSat = terrainProfileSat;
    }

    public double getTerrainProfileLongSpan() {
        return terrainProfileLongSpan;
    }

    public void setTerrainProfileLongSpan(double terrainProfileLongSpan) {
        this.terrainProfileLongSpan = terrainProfileLongSpan;
    }

    public boolean isModelViewMode() {
        return modelViewMode;
    }

    public void setModelViewMode(boolean viewMode) {
        if (viewMode == modelViewMode) {
            return;
        }
        this.modelViewMode = viewMode;
        setupView();
    }

    public String getModelViewString() {
        return modelViewString;
    }

    public void setModelViewString(String modelString) {
        if (!modelViewString.equalsIgnoreCase(modelString) && modelViewMode) {
            this.modelViewString = modelString;
            setupView();
        }
        this.modelViewString = modelString;
    }

    public double getModelViewNearClip() {
        return modelViewNearClip;
    }

    public void setModelViewNearClip(double modelViewNearClip) {
        this.modelViewNearClip = modelViewNearClip;
        if (this.isModelViewMode()) {
            wwd.getView().setNearClipDistance(modelViewNearClip);
        }
    }

    public double getModelViewFarClip() {
        return modelViewFarClip;
    }

    public void setModelViewFarClip(double modelViewFarClip) {
        this.modelViewFarClip = modelViewFarClip;
        if (this.isModelViewMode()) {
            wwd.getView().setFarClipDistance(modelViewFarClip);
        }
    }

    private void setupView() {
        if (modelViewMode == false) {
            AutoClipBasicOrbitView bov = new AutoClipBasicOrbitView();
            wwd.setView(bov);
            wwd.getInputHandler().setEventSource(null);
            AWTInputHandler awth = new AWTInputHandler();
            awth.setEventSource(wwd);
            wwd.setInputHandler(awth);
            awth.setSmoothViewChanges(smoothViewChanges);
            wwd.getView().setNearClipDistance(this.nearClippingPlaneDistOrbit);
            wwd.getView().setFarClipDistance(this.farClippingPlaneDistOrbit);
            Configuration.setValue(AVKey.INPUT_HANDLER_CLASS_NAME, AWTInputHandler.class.getName());
            this.getWwd().addSelectListener(new ViewControlsSelectListener(wwd, viewControlsLayer));
        } else {
            this.setViewModeECI(false);
            if (!satHash.containsKey(modelViewString)) {
                System.out.println("NO Current Satellite Selected, can't switch to Model Mode: " + modelViewString);
                return;
            }
            AbstractSatellite sat = satHash.get(modelViewString);
            BasicModelView3 bmv;
            if (wwd.getView() instanceof BasicOrbitView) {
                bmv = new BasicModelView3(((BasicOrbitView) wwd.getView()).getOrbitViewModel(), sat);
            } else {
                bmv = new BasicModelView3(((BasicModelView3) wwd.getView()).getOrbitViewModel(), sat);
            }
            if (wwd.getInputHandler() instanceof AWTInputHandler) {
                ((AWTInputHandler) wwd.getInputHandler()).removeHoverSelectListener();
            } else if (wwd.getInputHandler() instanceof BasicModelViewInputHandler3) {
                ((BasicModelViewInputHandler3) wwd.getInputHandler()).removeHoverSelectListener();
            }
            wwd.setView(bmv);
            wwd.getInputHandler().setEventSource(null);
            BasicModelViewInputHandler3 mih = new BasicModelViewInputHandler3();
            mih.setEventSource(wwd);
            wwd.setInputHandler(mih);
            mih.setSmoothViewChanges(smoothViewChanges);
            wwd.getView().setNearClipDistance(modelViewNearClip);
            wwd.getView().setFarClipDistance(modelViewFarClip);
            bmv.setZoom(900000);
            bmv.setPitch(Angle.fromDegrees(45));
            Configuration.setValue(AVKey.INPUT_HANDLER_CLASS_NAME, BasicModelViewInputHandler3.class.getName());
            this.getWwd().addSelectListener(new ViewControlsSelectListener(wwd, viewControlsLayer));
        }
    }

    /**
     * @return the smoothViewChanges
     */
    public boolean isSmoothViewChanges() {
        return smoothViewChanges;
    }

    /**
     * @param smoothViewChanges the smoothViewChanges to set
     */
    @Override
    public void setSmoothViewChanges(boolean smoothViewChanges) {
        if (smoothViewChanges == this.smoothViewChanges) {
            return;
        }
        this.smoothViewChanges = smoothViewChanges;
        setupView();
    }

    /**
     * @return the eciRadialGrid
     */
    public ECIRadialGrid getEciRadialGrid() {
        return eciRadialGrid;
    }

    private static class LayerAction extends AbstractAction {

        private Layer layer;

        private boolean selected;

        private WorldWindowGLJPanel wwd;

        public LayerAction(Layer layer, boolean selected, WorldWindowGLJPanel wwd) {
            super(layer.getName());
            this.layer = layer;
            this.selected = selected;
            this.layer.setEnabled(this.selected);
            this.wwd = wwd;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (((JCheckBox) actionEvent.getSource()).isSelected()) {
                this.layer.setEnabled(true);
            } else {
                this.layer.setEnabled(false);
            }
            wwd.repaint();
        }
    }

    public void setMJD(double mjd) {
        if (viewModeECI) {
            wwd.getView().stopStateIterators();
            wwd.getView().stopMovement();
            double theta0 = eciLayer.getRotateECIdeg();
            eciLayer.setCurrentMJD(mjd);
            double thetaf = eciLayer.getRotateECIdeg();
            Position pos = ((BasicOrbitView) wwd.getView()).getCenterPosition();
            double rotateEarthDelta = thetaf - theta0;
            Position newPos = pos.add(new Position(Angle.fromDegrees(0), Angle.fromDegrees(-rotateEarthDelta), 0.0));
            ((BasicOrbitView) wwd.getView()).setCenterPosition(newPos);
            starsLayer.setLongitudeOffset(Angle.fromDegrees(-eciLayer.getRotateECIdeg()));
        } else {
            eciLayer.setCurrentMJD(mjd);
            starsLayer.setLongitudeOffset(Angle.fromDegrees(-eciLayer.getRotateECIdeg()));
        }
        if (terrainProfileLayer.isEnabled()) {
            try {
                AbstractSatellite sat = satHash.get(terrainProfileSat);
                double[] lla = sat.getLLA();
                terrainProfileLayer.setStartLatLon(LatLon.fromRadians(lla[0], lla[1] - terrainProfileLongSpan * Math.PI / 180.0));
                terrainProfileLayer.setEndLatLon(LatLon.fromRadians(lla[0], lla[1] + terrainProfileLongSpan * Math.PI / 180.0));
            } catch (Exception e) {
            }
        }
        timeDepLayer.setCurrentMJD(mjd);
    }

    public void repaintWWJ() {
        wwd.redrawNow();
    }

    public void createScreenCapture() {
        try {
            Point pt = new Point();
            int width = 0;
            int height = 0;
            pt = wwd.getLocationOnScreen();
            width = wwd.getWidth();
            height = wwd.getHeight();
            if (height <= 0 || width <= 0) {
                JOptionPane.showInternalMessageDialog(this, "A Screenshot was not possible - too small of size", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(pt.x, pt.y, width, height));
            final JFileChooser fc = new JFileChooser();
            jsattrak.utilities.CustomFileFilter pngFilter = new jsattrak.utilities.CustomFileFilter("png", "*.png");
            fc.addChoosableFileFilter(pngFilter);
            jsattrak.utilities.CustomFileFilter jpgFilter = new jsattrak.utilities.CustomFileFilter("jpg", "*.jpg");
            fc.addChoosableFileFilter(jpgFilter);
            fc.setDialogTitle("Save Screenshot");
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String fileExtension = "png";
                if (fc.getFileFilter() == pngFilter) {
                    fileExtension = "png";
                }
                if (fc.getFileFilter() == jpgFilter) {
                    fileExtension = "jpg";
                }
                String extension = getExtension(file);
                if (extension != null) {
                    fileExtension = extension;
                } else {
                    file = new File(file.getAbsolutePath() + "." + fileExtension);
                }
                Exception e = SaveImageFile.saveImage(fileExtension, file, screencapture, 0.9f);
                if (e != null) {
                    System.out.println("ERROR SCREEN CAPTURE:" + e.toString());
                    return;
                }
            } else {
            }
        } catch (Exception e4) {
            System.out.println("ERROR SCREEN CAPTURE:" + e4.toString());
        }
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public void closeWindow() {
        try {
            parent.dispose();
        } catch (Exception e) {
        }
    }

    public JInternalFrame getParentDialog() {
        return parent;
    }

    public String getDialogTitle() {
        return parent.getTitle();
    }

    public void setTerrainProfileEnabled(boolean enabled) {
        terrainProfileLayer.setEnabled(enabled);
        if (enabled) {
            try {
                AbstractSatellite sat = satHash.get(terrainProfileSat);
                double[] lla = sat.getLLA();
                terrainProfileLayer.setStartLatLon(LatLon.fromRadians(lla[0], lla[1] - terrainProfileLongSpan * Math.PI / 180.0));
                terrainProfileLayer.setEndLatLon(LatLon.fromRadians(lla[0], lla[1] + terrainProfileLongSpan * Math.PI / 180.0));
            } catch (Exception e) {
            }
        }
    }

    public boolean getTerrainProfileEnabled() {
        return terrainProfileLayer.isEnabled();
    }

    public LayerList getLayerList() {
        return wwd.getModel().getLayers();
    }

    public void setOrbitFarClipDistance(double clipDist) {
        farClippingPlaneDistOrbit = clipDist;
        if (!this.isModelViewMode()) {
            wwd.getView().setFarClipDistance(farClippingPlaneDistOrbit);
        }
    }

    public double getOrbitFarClipDistance() {
        return farClippingPlaneDistOrbit;
    }

    public void setOrbitNearClipDistance(double clipDist) {
        nearClippingPlaneDistOrbit = clipDist;
        if (!this.isModelViewMode()) {
            wwd.getView().setNearClipDistance(nearClippingPlaneDistOrbit);
        }
    }

    public double getOrbitNearClipDistance() {
        return nearClippingPlaneDistOrbit;
    }

    public void updateCoverageLayerObject(CoverageAnalyzer ca) {
        cel.updateNewCoverageObject(ca);
    }

    public void resetWWJdisplay() {
        wwd.setSize(100, 100);
        this.add(wwd, java.awt.BorderLayout.CENTER);
        super.repaint();
    }

    public EcefTimeDepRenderableLayer getEcefTimeDepRenderableLayer() {
        return timeDepLayer;
    }
}
