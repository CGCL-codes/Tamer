package com.vividsolutions.jump.demo.layerviewpanel;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.io.SimpleGMLReader;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerTreeModel;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelContext;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelListener;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelProxy;
import com.vividsolutions.jump.workbench.ui.TreeLayerNamePanel;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
import com.vividsolutions.jump.workbench.ui.cursortool.MeasureTool;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.plugin.test.RandomTrianglesPlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.PanTool;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomTool;

/**
 *  To use this panel, set the "jump-demo-data-directory" system property to
 *  the directory containing tenures-extract.xml and ownership-extract.xml.
 */
public class MapTab extends JPanel implements LayerViewPanelContext {

    private static final String LAYERS = "Layers";

    private BorderLayout borderLayout1 = new BorderLayout();

    private JLabel statusLabel = new JLabel();

    private ErrorHandler errorHandler;

    private WorkbenchToolBar toolBar = new WorkbenchToolBar(new LayerViewPanelProxy() {

        public LayerViewPanel getLayerViewPanel() {
            return layerViewPanel;
        }
    });

    private LayerManager layerManager = new LayerManager();

    private TreeLayerNamePanel layerNamePanel;

    private JPanel centrePanel = new JPanel();

    private LayerViewPanel layerViewPanel;

    private GridLayout gridLayout1 = new GridLayout();

    private JLabel layerViewPanelLabel = new JLabel();

    private JLabel layerNamePanelLabel = new JLabel();

    private JPanel toolbarPanel = new JPanel();

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel toolbarPanelLabel = new JLabel();

    private WorkbenchContext workbenchContext = new WorkbenchContext() {

        public ErrorHandler getErrorHandler() {
            return MapTab.this;
        }

        public LayerNamePanel getLayerNamePanel() {
            return layerNamePanel;
        }

        public LayerViewPanel getLayerViewPanel() {
            return layerViewPanel;
        }

        public LayerManager getLayerManager() {
            return layerManager;
        }
    };

    public MapTab(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        layerViewPanel = new LayerViewPanel(layerManager, this);
        layerNamePanel = new TreeLayerNamePanel(layerViewPanel, new LayerTreeModel(layerViewPanel), layerViewPanel.getRenderingManager(), new HashMap());
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        layerViewPanel.addListener(new LayerViewPanelListener() {

            public void painted(Graphics graphics) {
            }

            public void selectionChanged() {
            }

            public void cursorPositionChanged(String x, String y) {
                setStatusMessage("(" + x + ", " + y + ")");
            }
        });
    }

    public void handleThrowable(final Throwable t) {
        errorHandler.handleThrowable(t);
    }

    public void warnUser(String warning) {
        setStatusMessage(warning);
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(((message == null) || (message.length() == 0)) ? " " : message);
    }

    void jbInit() throws Exception {
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel.setText(" ");
        this.setLayout(borderLayout1);
        centrePanel.setLayout(gridLayout1);
        gridLayout1.setColumns(2);
        gridLayout1.setHgap(4);
        gridLayout1.setRows(3);
        gridLayout1.setVgap(4);
        layerViewPanelLabel.setText("<HTML>This is a LayerViewPanel. (It might take a few moments " + "for the data to load). Try moving your mouse " + "over the image and note the coordinates reported " + "on the status bar below." + "<BR><BR>" + "</HTML>");
        layerViewPanelLabel.setVerticalAlignment(SwingConstants.TOP);
        layerNamePanelLabel.setText("<HTML>And this is a LayerNamePanel. It is independent of " + "the LayerViewPanel above, although they share a common model. Try clicking the checkboxes to turn " + "the layers on and off." + "<BR><BR>" + "LayerNamePanels can put layers in multiple categories. In this demo there is " + "just one category (\"Layer\").</HTML>");
        layerNamePanelLabel.setVerticalAlignment(SwingConstants.TOP);
        layerViewPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        layerNamePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        toolbarPanel.setLayout(gridBagLayout1);
        toolbarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        toolbarPanelLabel.setText("<HTML>The WorkbenchToolBar class makes it easy " + "to add CursorTools and PlugIns as toolbar buttons. Here the first " + "three buttons are CursorTools and the fourth is a PlugIn. " + "CursorTools allow the user to interact with the LayerViewPanel; PlugIns " + "perform their action immediately." + "<BR><BR>" + "Try clicking a button and seeing what it does to the " + "LayerViewPanel above." + "<BR><BR>" + "The WorkbenchToolBar adds CursorTools " + "as mutually exclusive toggle buttons. It " + "takes care of ensuring that the currently pressed CursorTool is " + "registered with the LayerViewPanel.</HTML>");
        toolbarPanelLabel.setVerticalAlignment(SwingConstants.TOP);
        add(statusLabel, BorderLayout.SOUTH);
        this.add(centrePanel, BorderLayout.CENTER);
        centrePanel.add(layerViewPanel, null);
        centrePanel.add(layerViewPanelLabel, null);
        centrePanel.add(layerNamePanel, null);
        centrePanel.add(layerNamePanelLabel, null);
        centrePanel.add(toolbarPanel, null);
        centrePanel.add(toolbarPanelLabel, null);
        toolbarPanel.add(toolBar, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    public void initialize() throws Exception {
        toolBar.addCursorTool("Zoom In/Out", new ZoomTool());
        toolBar.addCursorTool("Pan", new PanTool());
        toolBar.addCursorTool("Measure", new MeasureTool());
        toolBar.addSeparator();
        toolBar.addPlugIn(IconLoader.icon("World.gif"), new ZoomToFullExtentPlugIn(), new MultiEnableCheck(), workbenchContext);
        loadData();
    }

    private void loadData() throws Exception {
        removeAllCategories(layerManager);
        new RandomTrianglesPlugIn().execute(workbenchContext.createPlugInContext());
    }

    private File toFile(String filename) {
        String parent = System.getProperty("jump-demo-data-directory");
        Assert.isTrue(parent != null);
        return new File(parent, filename);
    }

    private void removeAllCategories(LayerManager layerManager) {
        for (Iterator i = layerManager.getCategories().iterator(); i.hasNext(); ) {
            Category cat = (Category) i.next();
            layerManager.removeIfEmpty(cat);
        }
    }
}
