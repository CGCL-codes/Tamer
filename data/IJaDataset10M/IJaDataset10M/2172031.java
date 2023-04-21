package org.bluemarble.gui;

import gov.nasa.worldwind.WorldWind;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.bluemarble.BlueMarble3D;
import org.bluemarble.util.BlueMarbeUtils;
import org.bluemarble.util.NewServerDialog;
import org.fenggui.Button;
import org.fenggui.CheckBox;
import org.fenggui.ComboBox;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.LayoutManager;
import org.fenggui.List;
import org.fenggui.ListItem;
import org.fenggui.ScrollContainer;
import org.fenggui.TextEditor;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.ISelectionChangedListener;
import org.fenggui.event.SelectionChangedEvent;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.event.mouse.MouseReleasedEvent;
import org.fenggui.layout.BorderLayout;
import org.fenggui.layout.BorderLayoutData;
import org.fenggui.layout.FormAttachment;
import org.fenggui.layout.FormData;
import org.fenggui.layout.FormLayout;
import org.fenggui.layout.GridLayout;
import org.fenggui.table.Table;
import org.fenggui.util.Color;
import org.fenggui.util.Spacing;
import worldwind.contrib.layers.GroundOverlayLayer;
import worldwind.contrib.layers.TiledWMSLayer;
import worldwind.contrib.layers.loop.TimeLoopGroundOverlay;
import worldwind.contrib.parsers.KMLSource;
import worldwind.contrib.parsers.ParserUtils;
import worldwind.contrib.parsers.SimpleHTTPClient;
import worldwind.contrib.parsers.SimpleWMSParser;
import worldwind.contrib.parsers.WMS_Capabilities;
import worldwind.contrib.parsers.ParserUtils.PublicWMSServer;
import worldwind.contrib.parsers.WMS_Capabilities.Layer;

/**
 * WMS Tab
 * @author Owner
 *
 */
public class WMSTab extends Container {

    private static final Logger logger = Logger.getLogger(WMSTab.class);

    private WMS_Capabilities capabilities;

    private ComboBox<ParserUtils.PublicWMSServer> cmbServers;

    private Display display;

    private final ScrollContainer listSC = new ScrollContainer();

    private final WMSLayersTable layersList = new WMSLayersTable();

    private Label lStatus;

    private static Hashtable<String, WMS_Capabilities> capabilitiesCache = new Hashtable<String, WMS_Capabilities>();

    private TextEditor latMin, latMax, lonMin, lonMax, tmin, tmax;

    private ComboBox<String> formats;

    private Label l3, l4;

    @SuppressWarnings("unchecked")
    private CheckBox chkUseTiles;

    /**
	 * WMS Layers Table class
	 * @author Owner
	 *
	 */
    @SuppressWarnings("unchecked")
    class WMSLayersTable extends Table {

        public WMSLayersTable() {
            super();
            setupTheme(WMSLayersTable.class);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void mouseReleased(MouseReleasedEvent mr) {
            Table list = (Table) mr.getSource();
            if (list.getModel() == null) return;
            int row = list.getSelection();
            if (row < 0) return;
            handleListSelection(row);
        }

        @Override
        public void mousePressed(MousePressedEvent mp) {
            try {
                super.mousePressed(mp);
            } catch (Exception e) {
            }
        }

        /**
		 * Get all selected indices
		 */
        public int[] getSelectionIndices() {
            int[] indices = new int[getSelectionCount()];
            int j = 0;
            if (getModel() == null) throw new IllegalStateException("No table model set!");
            for (int i = 0; i < selected.length; i++) {
                if (selected[i] == true) indices[j++] = i;
            }
            return indices;
        }
    }

    /**
	 * Default constructor
	 */
    public WMSTab(Display display) {
        this(display, new FormLayout());
    }

    /**
	 * Constructor 
	 * @param lm
	 */
    public WMSTab(Display display, LayoutManager lm) {
        super(lm);
        this.display = display;
        Container c1 = buildServersSection();
        Container c2 = buildLayersSection(c1);
        Container c3 = buildCoverageSection(c2);
        addWidget(c1);
        addWidget(c2);
        addWidget(c3);
        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
            BlueMarbeUtils.MessageBox(display, e.getMessage());
        }
    }

    /**
	 * WMS LayersA A label plus Layers list
	 * @param prev
	 * @return
	 */
    private Container buildLayersSection(Container prev) {
        Container c = new Container(new BorderLayout());
        c.getAppearance().setPadding(new Spacing(5, 5));
        lStatus = new Label("Layers");
        lStatus.setLayoutData(BorderLayoutData.NORTH);
        layersList.getAppearance().setHeaderVisible(false);
        layersList.getAppearance().setGridVisible(false);
        layersList.setMultipleSelection(true);
        layersList.setHeight(150);
        listSC.setInnerWidget(layersList);
        listSC.setLayoutData(BorderLayoutData.CENTER);
        c.addWidget(lStatus);
        c.addWidget(listSC);
        FormData fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        fd.top = new FormAttachment(prev, 0);
        fd.bottom = new FormAttachment(30, 0);
        c.setLayoutData(fd);
        return c;
    }

    /**
	 * Coverage
	 * @param prev
	 * @return
	 */
    @SuppressWarnings("unchecked")
    private Container buildCoverageSection(Container prev) {
        Container c = new Container(new GridLayout(7, 3));
        c.getAppearance().setPadding(new Spacing(5, 5));
        FengGUI.createLabel(c, "Latitude");
        latMin = FengGUI.createTextField(c);
        latMin.setShrinkable(false);
        latMin.getAppearance().getCursorPainter().setCursorColor(Color.WHITE);
        latMax = FengGUI.createTextField(c);
        latMax.setShrinkable(false);
        latMax.getAppearance().getCursorPainter().setCursorColor(Color.WHITE);
        c.addWidget(new Label("Longitude"));
        lonMin = FengGUI.createTextField(c);
        lonMax = FengGUI.createTextField(c);
        lonMin.getAppearance().getCursorPainter().setCursorColor(Color.WHITE);
        lonMax.getAppearance().getCursorPainter().setCursorColor(Color.WHITE);
        c.addWidget(new Label("Format"));
        formats = FengGUI.createComboBox(c);
        c.addWidget(new Label(""));
        l3 = FengGUI.createLabel(c, "Start time");
        tmin = FengGUI.createTextField(c);
        tmin.getAppearance().getCursorPainter().setCursorColor(Color.WHITE);
        c.addWidget(new Label(""));
        l4 = FengGUI.createLabel(c, "End time");
        tmax = FengGUI.createTextField(c);
        tmax.getAppearance().getCursorPainter().setCursorColor(Color.WHITE);
        c.addWidget(new Label(""));
        c.addWidget(new Label(""));
        chkUseTiles = FengGUI.createCheckBox(c, "Tile images");
        c.addWidget(new Label(""));
        Button goButton = FengGUI.createButton(c, "Go");
        goButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                performFinish();
            }
        });
        c.addWidget(new Label(""));
        c.addWidget(new Label(""));
        FormData fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        fd.top = new FormAttachment(prev, 0);
        c.setLayoutData(fd);
        return c;
    }

    /**
	 * WMS Servers section: Label (Servers), A Servers combo, and a 'New' button
	 * @return
	 */
    private Container buildServersSection() {
        Container c = new Container(new GridLayout(1, 3));
        c.getAppearance().setPadding(new Spacing(5, 5));
        cmbServers = new ComboBox<ParserUtils.PublicWMSServer>();
        cmbServers.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent e) {
                int row = cmbServers.getList().getMouseOverRow();
                if (row < 0) return;
                handleComboSeletion(cmbServers.getList(), row);
            }
        });
        c.addWidget(new Label("Server"));
        c.addWidget(cmbServers);
        Button btnNew = FengGUI.createButton(c, "New");
        btnNew.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                handleNewServer();
            }
        });
        FormData fd = new FormData();
        fd.left = new FormAttachment(0, 0);
        fd.right = new FormAttachment(100, 0);
        fd.top = new FormAttachment(100, 0);
        c.setLayoutData(fd);
        return c;
    }

    int currRow = -1;

    /**
	 * Handle combo selection. This method fires twice per selection
	 * @param servers
	 * @param row
	 */
    private void handleComboSeletion(List<ParserUtils.PublicWMSServer> servers, int row) {
        final PublicWMSServer server = servers.getItem(row).getValue();
        if (currRow != row) {
            currRow = row;
            URL url = server.capabilitiesURL;
            if (capabilitiesCache.containsKey(url.toString())) {
                capabilities = capabilitiesCache.get(url.toString());
                layersList.setModel(new WMSLayersTableModel(capabilities.getLayers()));
                listSC.layout();
                lStatus.setText(server.name + " has " + capabilities.getLayers().size() + " layers.");
                return;
            }
            new Thread(new Runnable() {

                public void run() {
                    try {
                        lStatus.setText("Loading " + server.name + "...");
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        SimpleHTTPClient client = new SimpleHTTPClient(server.capabilitiesURL);
                        client.doGet(bos);
                        bos.close();
                        SimpleWMSParser parser = new SimpleWMSParser();
                        parser.parse(server.name, new ByteArrayInputStream(bos.toString().getBytes()));
                        capabilities = parser.getCapabilities();
                        capabilitiesCache.put(server.capabilitiesURL.toString(), capabilities);
                        layersList.setModel(new WMSLayersTableModel(capabilities.getLayers()));
                        listSC.layout();
                        lStatus.setText(server.name + " has " + capabilities.getLayers().size() + " layers.");
                    } catch (NullPointerException e0) {
                        e0.printStackTrace();
                    } catch (Exception e) {
                        BlueMarbeUtils.MessageBox(display, e.getMessage());
                        setStatusMessage(null);
                    }
                }
            }).start();
        }
    }

    /**
	 * Handle List selection
	 * @param row
	 */
    private void handleListSelection(int row) {
        try {
            if (capabilities != null) {
                Layer layer = capabilities.getLayers().get(row);
                boolean showDates = false;
                String[] dates = null;
                if (layer.ISOTimeSpan != null) {
                    showDates = true;
                    logger.debug("Building ISO time list for " + layer.ISOTimeSpan);
                    final String csvDates = WMS_Capabilities.buildWMSTimeList(layer.ISOTimeSpan);
                    dates = csvDates.split(",");
                }
                String[] formats = new String[capabilities.getMapRequest().formats.size()];
                capabilities.getMapRequest().formats.toArray(formats);
                logger.debug("Using WMS layer " + layer.Name + " " + ((dates != null) ? dates.length : 0) + " time steps " + " formats size=" + formats.length + " Show dates=" + showDates);
                setStatusMessage("Using " + layer.Name + " with " + ((dates != null) ? dates.length : 0) + " time steps ");
                loadCoverage(showDates, layer.bbox.isValid(), dates, layer.bbox, formats);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Update the coverage depending on the selected layer
	 */
    private void loadCoverage(boolean showDates, boolean showLatLon, String[] dates, Object bbox, String[] formats) {
        updateVisibility(showDates, showLatLon, formats != null);
        if (showDates) {
            tmin.setText(dates[0]);
            tmax.setText(dates[dates.length - 1]);
        }
        if (showLatLon) {
            if (bbox != null) {
                if (bbox instanceof WMS_Capabilities.BBox) {
                    WMS_Capabilities.BBox box = (WMS_Capabilities.BBox) bbox;
                    latMax.setText(box.north);
                    latMin.setText(box.south);
                    lonMax.setText(box.east);
                    lonMin.setText(box.west);
                }
            }
        }
        if (formats != null) {
            this.formats.getList().clear();
            for (String format : formats) {
                this.formats.addItem(format);
            }
        }
        chkUseTiles.setSelected(!tmin.isVisible());
    }

    /**
	 * Parse WMS URLs from a local HTML list
	 */
    private void initialize() throws Exception {
        String list = "config/skylab_public_wmslist.html";
        InputStream buffer = new BufferedInputStream(BlueMarbeUtils.getInputStream(WMSTab.class, list));
        OutputStream os = new ByteArrayOutputStream();
        ParserUtils.readStream(buffer, os);
        os.close();
        Vector<ParserUtils.PublicWMSServer> servers = ParserUtils.parsePublicWmsHTTPPage(os.toString());
        loadServers(servers);
    }

    private void loadServers(Vector<ParserUtils.PublicWMSServer> servers) {
        String[] bookmarks = loadBookmarks();
        try {
            if (bookmarks != null) {
                logger.debug("Loading " + bookmarks.length + " WMS srvs from bookmarks.");
                for (String bm : bookmarks) {
                    final String[] tmp = bm.split("\\|");
                    final String name = tmp[0];
                    final String url = tmp[1];
                    logger.debug("Adding WMS bookmark: " + name + " " + url);
                    ParserUtils.PublicWMSServer wms = new ParserUtils.PublicWMSServer(name, new URL(url));
                    servers.add(0, wms);
                }
            }
        } catch (Exception e) {
        }
        for (ParserUtils.PublicWMSServer server : servers) {
            if (server.name != null) {
                ListItem<ParserUtils.PublicWMSServer> li = new ListItem<ParserUtils.PublicWMSServer>(server.name);
                li.setValue(server);
                cmbServers.addItem(li);
            }
        }
    }

    private void setStatusMessage(String text) {
        if (text == null) lStatus.setText("Layers"); else lStatus.setText(text);
    }

    /**
	 * load WMS book marks
	 * @return
	 */
    private String[] loadBookmarks() {
        try {
            File file = WorldWind.getDataFileCache().newFile("wms-bookmarks.txt");
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            byte[] bytes = new byte[(int) raf.length()];
            raf.readFully(bytes);
            raf.close();
            return new String(bytes).split(BlueMarble3D.NL);
        } catch (Exception e) {
            return null;
        }
    }

    private void updateVisibility(boolean showDates, boolean showLatLon, boolean showFormats) {
        tmin.setVisible(showDates);
        tmax.setVisible(showDates);
        l3.setVisible(showDates);
        l4.setVisible(showDates);
    }

    /**
	 * Submit routine
	 */
    private void performFinish() {
        try {
            if (layersList.getModel() == null) return;
            NavigatorWindow view = (NavigatorWindow) getParent().getParent().getParent();
            int[] indices = layersList.getSelectionIndices();
            final String format = formats.getSelectedValue();
            if (indices == null || indices.length == 0) {
                BlueMarbeUtils.MessageBox(display, "Select one or more layers.");
                return;
            }
            WMS_Capabilities.Layer[] selectedLayers = getSelectedLayers(indices);
            logger.debug("WMS Capabilities ver=" + capabilities.getVersion());
            logger.debug("Number of selected layers=" + indices.length + " selected fmt=" + format);
            if (chkUseTiles.isSelected()) {
                handleTiledWMS(selectedLayers, format, view);
            } else {
                logger.debug("Using Overlays.");
                boolean isKML = format.equals(SimpleHTTPClient.CT_KML) || format.equals(SimpleHTTPClient.CT_KMZ);
                if (isKML) {
                    logger.debug("KML/KMZ detected.");
                    handleKmlKmz(selectedLayers, format, view);
                } else if (!tmin.isVisible()) {
                    logger.debug("Using GroundOverlays.");
                    GroundOverlayLayer[] ovs = ParserUtils.newGroundOverlay(selectedLayers, format);
                    view.addLayers(capabilities.getService().Title, ovs);
                } else {
                    handleTimeLoop(selectedLayers, format, view);
                }
            }
            view.showLayers();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            BlueMarbeUtils.MessageBox(display, e.getMessage());
        }
    }

    /**
	 * Get user selected WMS layers
	 */
    private WMS_Capabilities.Layer[] getSelectedLayers(int[] indices) {
        WMS_Capabilities.Layer[] layers = new WMS_Capabilities.Layer[indices.length];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = capabilities.getLayers().get(indices[i]);
        }
        return layers;
    }

    /**
	 * Tiled WMS 
	 * @param selectedLayers
	 * @param format
	 * @param view
	 */
    private void handleTiledWMS(WMS_Capabilities.Layer[] selectedLayers, String format, NavigatorWindow view) {
        TiledWMSLayer[] wwLayers = ParserUtils.newWMSTiledLayer(selectedLayers, format);
        String nodeName = (capabilities.getService().Title != null) ? capabilities.getService().Title : capabilities.getService().Name;
        logger.debug("Using tiled WMS layers. Parent node name=" + nodeName + " Num layers=" + wwLayers.length);
        view.addLayers(nodeName, wwLayers);
    }

    /**
	 * Handle Time series WMS layers
	 * @param selectedLayers
	 * @param format
	 * @param view
	 */
    private void handleTimeLoop(WMS_Capabilities.Layer[] selectedLayers, String format, NavigatorWindow view) throws MalformedURLException {
        String[] dates = getSelectedTimes();
        int MAX_DATE_LEN = 100;
        logger.debug("Using TimeLoopOverlays. Dates size=" + dates.length);
        if (dates.length > MAX_DATE_LEN) {
            BlueMarbeUtils.MessageBox(display, "Time span too large: " + dates.length + " . Max =" + MAX_DATE_LEN);
            return;
        }
        TimeLoopGroundOverlay loopLayer;
        for (int i = 0; i < selectedLayers.length; i++) {
            loopLayer = ParserUtils.newTimeLoopGroundOverlay(selectedLayers[i], dates, format);
            loopLayer.setEnabled(false);
            logger.debug("Submitting loop layer: " + loopLayer);
            view.addLayer(loopLayer);
        }
    }

    /**
	 * Handle Google Earth Docs
	 * @param selectedLayers
	 * @param format
	 * @param view
	 */
    private void handleKmlKmz(WMS_Capabilities.Layer[] selectedLayers, final String format, final NavigatorWindow view) throws Exception {
        final GroundOverlayLayer[] overlays = ParserUtils.newGroundOverlay(selectedLayers, format);
        for (GroundOverlayLayer groundOverlay : overlays) {
            String newURL = groundOverlay.getTextureURL().toString() + "&time=" + tmin.getText() + "/" + tmax.getText();
            logger.debug("Changing URL for " + groundOverlay + " to " + newURL);
            groundOverlay.setTextureURL(new URL(newURL));
        }
        setStatusMessage("Fetching " + selectedLayers.length + " layers...");
        new Thread(new Runnable() {

            public void run() {
                for (GroundOverlayLayer overlay : overlays) {
                    overlay.fetchOverlay(true);
                    try {
                        view.addKMLSource(new KMLSource(overlay.getFileFromCache(), format));
                    } catch (Exception e) {
                        BlueMarbeUtils.MessageBox(display, "Unable to load KML: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                setStatusMessage(null);
            }
        }).start();
    }

    /**
	 * Build an array of ISO dates for the selected layers
	 * @return String array of dates for the user selected time span
	 */
    private String[] getSelectedTimes() {
        int[] selectedIndices = layersList.getSelectionIndices();
        String isoTime = capabilities.getLayers().get(selectedIndices[0]).ISOTimeSpan;
        String[] tmp = isoTime.split("/");
        String period = (tmp != null && tmp.length == 3) ? tmp[2] : null;
        logger.debug("Manual input for time span? Layer iso time=" + isoTime + " Period=" + period);
        if (period != null) {
            String iso = tmin.getText() + "/" + tmax.getText() + "/" + period;
            try {
                return WMS_Capabilities.buildWMSTimeList(iso).split(",");
            } catch (Exception e) {
                return new String[] { tmin.getText(), tmax.getText() };
            }
        } else return new String[] { tmin.getText(), tmax.getText() };
    }

    /**
	 * Add a new WMS server using the {@link NewServerDialog}
	 */
    private void handleNewServer() {
        NewServerDialog d = new NewServerDialog();
        d.setVisible(true);
        if (!d.isCanceled()) {
            final String name = d.getServerName();
            final String url = d.getServerURL();
            logger.debug("Adding Server=" + name + " u=" + url);
            try {
                PublicWMSServer server = new ParserUtils.PublicWMSServer(name, new URL(url));
                ListItem<ParserUtils.PublicWMSServer> li = new ListItem<ParserUtils.PublicWMSServer>(server.name);
                li.setValue(server);
                cmbServers.addItem(li);
                layout();
            } catch (Exception e) {
                BlueMarbeUtils.MessageBox(display, "Error adding server " + name + ": " + e.getMessage());
            }
        }
        d.dispose();
    }
}
