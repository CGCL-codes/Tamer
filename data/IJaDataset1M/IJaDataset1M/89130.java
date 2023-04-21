package demv.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import visad.DisplayImpl;
import visad.GraphicsModeControl;
import visad.ScalarMap;
import visad.VisADException;
import visad.java3d.DisplayImplJ3D;
import visad.util.ContourWidget;
import visad.util.GMCWidget;
import visad.util.RangeWidget;
import demv.DEMViewer;
import demv.data.DEMVConstants;
import demv.gui.img.IconLoader;

public class DEMVControl extends JFrame implements ActionListener {

    protected DEMViewer demViewer;

    protected String currentDir = ".";

    protected JTabbedPane tabbedPane;

    protected JPanel basicPanel;

    protected JPanel advancedPanel;

    protected JPanel colorsPanel;

    protected AttributeGUI[] attGUIs;

    protected GMCWidget gmcWidget;

    protected FramedContourControl contourCtrl;

    protected DisplayControl dispCtrl;

    protected SkyControl skyCtrl;

    protected ConstantPlaneControl constPlaneCtrl;

    protected ColorTableCombo colorCombo;

    protected LegendControl legendCtrl;

    private JSlider zExag;

    /**Field to hold minimum elevation value (to use when setting DEM block mode)*/
    private final float minDemValue = Float.POSITIVE_INFINITY;

    private final boolean isMissingTransparent = true;

    /** @todo needed? */
    private javax.swing.AbstractAction floatingLayer;

    public DEMVControl(DEMViewer demv) {
        super("DEM Viewer Control");
        setIconImage(IconLoader.loadImage("dem_icon.png"));
        this.demViewer = demv;
        int x = 375;
        int y = 640;
        setBounds(550, 0, x, y);
        setBounds(demv.getFrame().getX() + demv.getFrame().getSize().width, demv.getFrame().getY(), x, y);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                openExitDialog(DEMVControl.this.demViewer);
            }
        });
        Box colorBox = Box.createVerticalBox();
        createTabbedPanes();
        createMenu();
        createDemTypeChecks();
        JPanel cPanel = new JPanel();
        this.basicPanel.add(cPanel);
        cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.X_AXIS));
        colorBox.add(Box.createVerticalStrut(15));
        colorBox.add(createColorTableCombo());
        colorBox.add(Box.createVerticalStrut(15));
        colorBox.add(createBoxAxesChecks());
        colorBox.add(Box.createVerticalStrut(20));
        cPanel.add(createdAttributeGUI());
        cPanel.add(Box.createHorizontalStrut(10));
        cPanel.add(colorBox);
        createVESlider();
        this.basicPanel.add(createDisplayColorCombos());
        createGraphicsControl();
        createRangeWidgets();
        createColorWidgets();
        File f = new File(".");
        this.currentDir = f.getAbsolutePath();
        System.out.println(this.currentDir);
        setVisible(true);
    }

    public void popDataDialog() {
        ((DEMVMenu) this.getJMenuBar()).loadNewDem.doClick();
    }

    private void createTabbedPanes() {
        this.tabbedPane = new JTabbedPane(SwingConstants.BOTTOM);
        this.getContentPane().add(this.tabbedPane);
        this.basicPanel = new JPanel();
        this.tabbedPane.addTab("Basic", null, this.basicPanel, "Basic Controls");
        this.advancedPanel = new JPanel();
        this.advancedPanel.setLayout(new BoxLayout(this.advancedPanel, BoxLayout.Y_AXIS));
        this.tabbedPane.addTab("Advanced", null, this.advancedPanel, "Advanced Controls");
        this.colorsPanel = new JPanel();
        this.tabbedPane.addTab("Color", null, this.colorsPanel, "Color Controls");
    }

    private void createDemTypeChecks() {
        String[] demTypes = new String[] { "Surface", "Points", "Isolines", "Mesh", "Texture" };
        String[] tips = new String[] { "DEM as a smooth surface", "Show points", "Draw isolines", "Show wireframe", "Turn texture on" };
        JToggleButton[] checkBoxes = new JToggleButton[demTypes.length];
        ButtonGroup demTypeGroup = new ButtonGroup();
        JPanel checkPanel = new JPanel();
        checkPanel.setBorder(BorderFactory.createTitledBorder("DEM Type"));
        ActionListener dtListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String actCmd = e.getActionCommand();
                try {
                    if (DEMVControl.this.contourCtrl != null) {
                        DEMVControl.this.contourCtrl.setVisible(false);
                        ((DEMVMenu) DEMVControl.this.getJMenuBar()).contCtlrMItem.setEnabled(false);
                        DEMVControl.this.contourCtrl = null;
                    }
                    if (actCmd.equals("Surface")) {
                        DEMVControl.this.demViewer.setDisplayMode(DEMVConstants.SMOOTH);
                    } else if (actCmd.equals("Points")) {
                        DEMVControl.this.demViewer.setDisplayMode(DEMVConstants.POINT);
                    } else if (actCmd.equals("Isolines")) {
                        DEMVControl.this.demViewer.setDisplayMode(DEMVConstants.ISOLINES);
                        createContourControl();
                    } else if (actCmd.equals("Mesh")) {
                        DEMVControl.this.demViewer.setDisplayMode(DEMVConstants.MESH);
                    } else if (actCmd.equals("Texture")) {
                        DEMVControl.this.demViewer.setDisplayMode(DEMVConstants.TEXTURE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i] = new JRadioButton(demTypes[i], false);
            checkBoxes[i].setActionCommand(demTypes[i]);
            checkBoxes[i].addActionListener(dtListener);
            checkBoxes[i].setToolTipText(tips[i]);
            checkPanel.add(checkBoxes[i]);
            demTypeGroup.add(checkBoxes[i]);
        }
        checkBoxes[0].setSelected(true);
        this.basicPanel.add(checkPanel);
    }

    private void createVESlider() {
        this.zExag = new JSlider(0, 10, 1);
        this.zExag.setPaintTicks(true);
        this.zExag.setMajorTickSpacing(5);
        this.zExag.setMinorTickSpacing(1);
        this.zExag.setPaintLabels(true);
        this.zExag.setSnapToTicks(true);
        this.zExag.setBorder(BorderFactory.createTitledBorder("Vertical Exaggeration"));
        this.basicPanel.add(this.zExag);
        this.zExag.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int srcVal = source.getValue();
                if (!source.getValueIsAdjusting()) {
                    DEMVControl.this.demViewer.setVerticalExaggeration(srcVal);
                    if (DEMVControl.this.constPlaneCtrl != null) {
                        DEMVControl.this.constPlaneCtrl.updatePlane();
                    }
                }
            }
        });
    }

    private Component createColorTableCombo() {
        this.colorCombo = new ColorTableCombo(this.demViewer);
        return this.colorCombo;
    }

    private Component createDisplayColorCombos() {
        JComboBox dispColc = new JComboBox();
        dispColc.setEditable(false);
        dispColc.setBorder(BorderFactory.createTitledBorder("Foreground"));
        dispColc.addItem("White");
        dispColc.addItem("Black   ");
        dispColc.addItem("Other Color");
        dispColc.setSize(110, 10);
        dispColc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent E) {
                int index = ((JComboBox) E.getSource()).getSelectedIndex();
                switch(index) {
                    case 0:
                        DEMVControl.this.demViewer.setForegroundColor(Color.white);
                        break;
                    case 1:
                        DEMVControl.this.demViewer.setForegroundColor(Color.black);
                        break;
                    case 2:
                        DEMVControl.this.demViewer.setForegroundColor(JColorChooser.showDialog(null, "Pick a Color!", Color.white));
                        break;
                }
            }
        });
        JComboBox dispColc2 = new JComboBox();
        dispColc2.setEditable(false);
        dispColc2.setBorder(BorderFactory.createTitledBorder("Background"));
        dispColc2.addItem("Black    ");
        dispColc2.addItem("White");
        dispColc2.addItem("Sky Blue");
        dispColc2.addItem("Other Color");
        dispColc2.setSize(110, 10);
        dispColc2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent E) {
                int index = ((JComboBox) E.getSource()).getSelectedIndex();
                switch(index) {
                    case 0:
                        DEMVControl.this.demViewer.setBackgroundColor(Color.black);
                        break;
                    case 1:
                        DEMVControl.this.demViewer.setBackgroundColor(Color.white);
                        break;
                    case 2:
                        DEMVControl.this.demViewer.setBackgroundColor(new Color(139, 216, 242));
                        break;
                    case 3:
                        DEMVControl.this.demViewer.setBackgroundColor(JColorChooser.showDialog(null, "Pick a Color!", Color.black));
                        break;
                }
            }
        });
        Box b = Box.createHorizontalBox();
        b.add(dispColc);
        b.add(Box.createHorizontalStrut(5));
        b.add(dispColc2);
        JPanel c = new JPanel();
        c.setBorder(BorderFactory.createTitledBorder("Display Colors"));
        c.add(b);
        return c;
    }

    private Component createBoxAxesChecks() {
        Box b = Box.createVerticalBox();
        JCheckBox axesCB = new JCheckBox("Draw Axes", false);
        axesCB.setSize(20, 10);
        axesCB.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent E) {
                boolean on = ((JCheckBox) E.getSource()).isSelected();
                DEMVControl.this.demViewer.setAxesOn(on);
            }
        });
        JCheckBox dBoxcb = new JCheckBox("Draw Box", true);
        dBoxcb.setSize(20, 10);
        dBoxcb.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent E) {
                boolean on = ((JCheckBox) E.getSource()).isSelected();
                DEMVControl.this.demViewer.setBoxOn(on);
            }
        });
        b.add(axesCB);
        b.add(dBoxcb);
        Box b2 = Box.createHorizontalBox();
        b2.add(b);
        b2.add(Box.createHorizontalStrut(20));
        return b2;
    }

    private void createGraphicsControl() {
        GraphicsModeControl mode = this.demViewer.getDisplay().getGraphicsModeControl();
        this.gmcWidget = new GMCWidget(mode);
        JToolBar jt = new JToolBar("GraphicsModeControl", SwingConstants.HORIZONTAL);
        jt.setFloatable(true);
        jt.add(this.gmcWidget);
        this.advancedPanel.add(jt);
    }

    private void createRangeWidgets() {
        try {
            visad.ScalarMap[] sm = this.demViewer.getXYZMaps();
            for (int i = 0; i < sm.length; i++) {
                this.advancedPanel.add(new RangeWidget(sm[i]));
            }
            this.advancedPanel.add(new RangeWidget(this.demViewer.getCurrentRgbMap()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createColorWidgets() {
        try {
            this.colorsPanel.removeAll();
            visad.ScalarMap sm = this.demViewer.getColorMap();
            if (sm != null) {
                this.colorsPanel.add(new ColorTableWidget(sm, null, true));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createContourControl() {
        try {
            visad.ScalarMap sm = this.demViewer.getContourMap();
            if (this.contourCtrl == null) {
                this.contourCtrl = new FramedContourControl(sm);
                this.addWindowListener(this.contourCtrl);
                ((DEMVMenu) this.getJMenuBar()).contCtlrMItem.setEnabled(true);
            }
        } catch (Exception ex) {
        }
    }

    private Component createdAttributeGUI() {
        JPanel agPanel = new JPanel();
        agPanel.setBorder(BorderFactory.createTitledBorder("Color to Attribute"));
        agPanel.setLayout(new BoxLayout(agPanel, BoxLayout.Y_AXIS));
        String[] iNames = new String[] { "dem_icon.png", "dem_icon3.png" };
        this.attGUIs = new AttributeGUI[2];
        this.attGUIs[0] = new AttributeGUI(0, "DEM", true, false, iNames[0], -99);
        this.attGUIs[1] = new AttributeGUI(3, "Image", true, true, iNames[1], 0);
        ButtonGroup bGroup = new ButtonGroup();
        for (int i = 0; i < this.attGUIs.length; i++) {
            agPanel.add(this.attGUIs[i]);
            ((JComponent) this.attGUIs[i]).setAlignmentX(0f);
        }
        return agPanel;
    }

    public void setSelectedLayer(int attIndex) {
        this.attGUIs[attIndex].setSelected(true);
    }

    public int getSelectedLayer() {
        for (int i = 0; i < this.attGUIs.length; i++) {
            if (this.attGUIs[i].isSelected()) {
                return i;
            }
        }
        return 0;
    }

    private void createMenu() {
        JMenuBar mb = new DEMVMenu(this.demViewer);
        this.setJMenuBar(mb);
    }

    public void actionPerformed(ActionEvent e) {
        String actCmd = e.getActionCommand();
        System.out.println("actCmd: " + actCmd);
        try {
            if (actCmd.equals("paraProj")) {
                this.demViewer.getDisplay().getGraphicsModeControl().setProjectionPolicy(DisplayImplJ3D.PARALLEL_PROJECTION);
            } else if (actCmd.equals("aCtrl")) {
                this.tabbedPane.setSelectedIndex(1);
            } else if (actCmd.equals("bCtrl")) {
                this.tabbedPane.setSelectedIndex(0);
            } else if (actCmd.equals("cCtrl")) {
                this.tabbedPane.setSelectedIndex(2);
            } else if (actCmd.equals("showDEMInfo")) {
                DemInfoPane.showInfo(this, this.demViewer.getVisADData());
            } else if (actCmd.equals("showInfo")) {
                DemInfoPane.showProgramInfo(this);
            } else if (actCmd.equals("perspProj")) {
                this.demViewer.getDisplay().getGraphicsModeControl().setProjectionPolicy(DisplayImplJ3D.PERSPECTIVE_PROJECTION);
            } else if (actCmd.equals("dispCtrl")) {
                if (this.dispCtrl == null) {
                    this.dispCtrl = new DisplayControl((DisplayImplJ3D) this.demViewer.getDisplay());
                } else {
                    this.dispCtrl.getFrame().setVisible(true);
                    this.dispCtrl.getFrame().toFront();
                }
            } else if (actCmd.equals("contCtrl")) {
                this.contourCtrl.setVisible(true);
                this.contourCtrl.toFront();
            }
            if (actCmd.equals("saveShot")) {
                saveScreenShot(this.demViewer.getDisplay());
            }
            if (actCmd.equals("backSky")) {
                if (getSelectedLayer() == 0) {
                    DemInfoPane.showMessagePane(this, "Cannot show sky when DEM is the selected color attribute.\n" + "\"Color to Attribute\" is being automatically set to the first layer.", JOptionPane.INFORMATION_MESSAGE);
                }
                if (this.skyCtrl == null) {
                    this.skyCtrl = new SkyControl(this.demViewer);
                } else {
                    this.skyCtrl.setVisible(true);
                    this.skyCtrl.toFront();
                }
            }
            if (actCmd.equals("constPlane")) {
                if (this.constPlaneCtrl == null) {
                    this.constPlaneCtrl = new ConstantPlaneControl(this.demViewer);
                } else {
                    this.constPlaneCtrl.setVisible(true);
                    this.constPlaneCtrl.toFront();
                }
            }
            if (actCmd.equals("addLegend")) {
                if (this.legendCtrl == null) {
                    this.legendCtrl = new LegendControl(this.demViewer.getFrame().getContentPane(), this.demViewer.getCurrentRgbMap());
                } else {
                    this.legendCtrl.setVisible(true);
                    this.legendCtrl.toFront();
                }
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        } catch (VisADException e1) {
            e1.printStackTrace();
        }
    }

    public void setCurrentDir(String dir) {
        if (dir == null) {
            return;
        }
        this.currentDir = dir;
    }

    public String getCurrentDir() {
        return this.currentDir;
    }

    public static final void openExitDialog(DEMViewer demv) {
        if (demv.getFrame().getDefaultCloseOperation() == WindowConstants.DISPOSE_ON_CLOSE) {
            demv.getFrame().setVisible(false);
            demv.getFrame().dispose();
            demv.getDEMVControl().setVisible(false);
            demv.getDEMVControl().dispose();
            return;
        }
        final Object[] options = { "Yes", "No" };
        int n = JOptionPane.showOptionDialog(demv.getFrame(), "Exit DEM Viewer?", "Exit DEM Viewer", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (n == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /** Save screenshot of a display as a JPG file. If fromDisplay equals "1",
  * the main display will be saved. If fromDisplay equals "2", the display
  * with the profile of the DEM will be saved (if available; s. enableSlicer()).
  * @param String jpgFile name of the image to be created
  * @param int fromDisplay "1" to save main display or "2" to save profile display (if available)
  */
    public static void saveScreenShot(DisplayImpl d) {
        final String cfn = "";
        final DisplayImpl display = d;
        Runnable captureImage = new Runnable() {

            public void run() {
                try {
                    JFileChooser fc = new JFileChooser();
                    fc.setDialogTitle("Save screenshot as Jpeg/PNG");
                    String dir = fc.getCurrentDirectory().getAbsolutePath();
                    if (fc.showDialog(null, "Save") == JFileChooser.APPROVE_OPTION) {
                        System.out.println("Saving image...");
                        File f = fc.getSelectedFile().getAbsoluteFile();
                        String cfn = f.getName();
                        String ext = "JPG";
                        if (cfn.toLowerCase().endsWith("png")) {
                            ext = "PNG";
                        } else {
                        }
                        FileOutputStream fos = new FileOutputStream(f);
                        javax.imageio.ImageIO.write(display.getImage(), ext, fos);
                        fos.flush();
                        fos.close();
                        System.out.println("Saved " + cfn);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoClassDefFoundError err) {
                    System.err.println("\nError: JPEG codec not found");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        Thread t = new Thread(captureImage);
        t.start();
    }

    public void rerollData() {
        try {
            DisplayImpl d = this.demViewer.getDisplay();
            if (this.dispCtrl != null) {
                this.dispCtrl.setDisplay((DisplayImplJ3D) d);
            }
            if (this.constPlaneCtrl != null) {
                d.addReference(this.constPlaneCtrl.getDataReference(), this.constPlaneCtrl.getConstantMaps());
            }
            if (this.skyCtrl != null) {
                this.skyCtrl.setDisplay((DisplayImplJ3D) d);
                d.addReference(this.skyCtrl.getDataReference(), this.skyCtrl.getConstantMaps());
            }
            this.advancedPanel.removeAll();
            createColorWidgets();
            createGraphicsControl();
            createRangeWidgets();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class AttributeGUI extends JPanel {

        private DEMVControl dc;

        private final JToggleButton checkBox;

        private final JButton loadButton;

        AttributeGUI(int attrIndex, String dataName, boolean selected, boolean jpgMode, String iconName, final int index) {
            super(new FlowLayout());
            this.checkBox = new JCheckBox(dataName, selected);
            this.checkBox.setActionCommand(dataName);
            this.checkBox.setToolTipText(dataName);
            this.checkBox.setPreferredSize(new Dimension(90, 20));
            this.checkBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JToggleButton toggleButton = (JToggleButton) e.getSource();
                    String actComm = toggleButton.getActionCommand();
                    boolean enabled = AttributeGUI.this.checkBox.isSelected();
                    try {
                        if (actComm.equalsIgnoreCase("dem")) {
                            DEMVControl.this.demViewer.setDemEnabled(enabled);
                        } else {
                            DEMVControl.this.demViewer.setTextureImageEnabled(enabled);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            add(this.checkBox);
            this.loadButton = new JButton("...");
            this.loadButton.setPreferredSize(new Dimension(25, 25));
            add(this.loadButton);
            String suffix = "layer";
            this.loadButton.setToolTipText("Load new " + suffix);
        }

        @Override
        public void setEnabled(boolean b) {
            this.checkBox.setEnabled(b);
        }

        public void setSelected(boolean b) {
            this.checkBox.setSelected(b);
        }

        public boolean isSelected() {
            return this.checkBox.isSelected();
        }

        public AbstractButton getAbstractButton() {
            return this.checkBox;
        }
    }

    protected class SlicerAction extends AbstractAction {

        public SlicerAction(int direction, String s, String iconName) {
            super(s);
        }

        public void actionPerformed(ActionEvent ae) {
            this.setEnabled(false);
        }
    }

    class FramedContourControl extends JFrame implements WindowListener {

        protected ContourWidget contourWidget;

        public FramedContourControl(ScalarMap contourMap) {
            super("Contour Control");
            addWindowListener(new WindowAdapter() {

                @Override
                public void windowDeiconified(WindowEvent we) {
                    ((DEMVMenu) DEMVControl.this.getJMenuBar()).contCtlrMItem.setEnabled(true);
                }

                @Override
                public void windowIconified(WindowEvent we) {
                    ((DEMVMenu) DEMVControl.this.getJMenuBar()).contCtlrMItem.setEnabled(false);
                }

                @Override
                public void windowClosing(WindowEvent we) {
                    ((DEMVMenu) DEMVControl.this.getJMenuBar()).contCtlrMItem.setEnabled(true);
                }
            });
            updateWidget(contourMap);
        }

        public void updateWidget(ScalarMap newContourMap) {
            try {
                if (this.contourWidget != null) {
                    getContentPane().remove(this.contourWidget);
                }
                this.contourWidget = new ContourWidget(newContourMap);
                getContentPane().add(this.contourWidget);
                pack();
                setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void windowActivated(WindowEvent e) {
        }

        public void windowClosed(WindowEvent e) {
            setVisible(false);
            dispose();
        }

        public void windowClosing(WindowEvent e) {
        }

        public void windowDeactivated(WindowEvent e) {
        }

        public void windowDeiconified(WindowEvent e) {
            setVisible(false);
        }

        public void windowIconified(WindowEvent e) {
        }

        public void windowOpened(WindowEvent e) {
        }
    }

    public void toggleMenuItem(String mItemActionCmd, boolean on) {
        DEMVMenu menu = (DEMVMenu) this.getJMenuBar();
        if (mItemActionCmd.equals("xSlicer")) {
            menu.xSlicer.setEnabled(on);
        } else if (mItemActionCmd.equals("ySlicer")) {
            menu.ySlicer.setEnabled(on);
        } else if (mItemActionCmd.equals("zSlicer")) {
            menu.zSlicer.setEnabled(on);
        } else if (mItemActionCmd.equals("worldDisplay")) {
            menu.worldDisplayItem.setEnabled(on);
        } else if (mItemActionCmd.equals("backSky")) {
            menu.skyItem.setEnabled(on);
        } else if (mItemActionCmd.equals("constPlane")) {
            if (menu.constPlane != null) {
                menu.constPlane.setEnabled(on);
            }
        }
    }
}
