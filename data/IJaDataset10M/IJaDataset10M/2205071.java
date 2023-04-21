package g4p.tool.gui;

import g4p.tool.components.DActivityBar;
import g4p.tool.components.DApplication;
import g4p.tool.components.DButton;
import g4p.tool.components.DCheckbox;
import g4p.tool.components.DCombo;
import g4p.tool.components.DHorzSlider;
import g4p.tool.components.DImageButton;
import g4p.tool.components.DKnob;
import g4p.tool.components.DLabel;
import g4p.tool.components.DOption;
import g4p.tool.components.DOptionGroup;
import g4p.tool.components.DPanel;
import g4p.tool.components.DTextField;
import g4p.tool.components.DTimer;
import g4p.tool.components.DVertSlider;
import g4p.tool.components.DWSlider;
import g4p.tool.components.DWindow;
import g4p.tool.gui.propertygrid.CtrlPropView;
import g4p.tool.gui.tabview.CtrlTabView;
import g4p.tool.gui.treeview.CtrlSketchView;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import processing.app.Editor;

/**
 * The GUI designer window for the tool.
 * 
 * @author Peter Lager
 */
@SuppressWarnings("serial")
public class GuiDesigner extends javax.swing.JFrame {

    private static GuiDesigner instance = null;

    private static Editor editor = null;

    private static FontMetrics metrics = null;

    private static boolean stayOpen = false;

    private static boolean autoHide = false;

    public static GuiDesigner instance() {
        return instance;
    }

    public static Editor editor() {
        return editor;
    }

    public static FontMetrics metrics() {
        return metrics;
    }

    public static void keepOpen(boolean mode) {
        stayOpen = mode;
    }

    /**
	 * This is provided because the GuiDesigner window is specified as 
	 * always-on-top and this conflicts with using a new Frame with
	 * JOptionPane.
	 * 
	 * Use this in preference to the static method in the class 
	 * processing.app.Base
	 * 
	 * @param title
	 * @param message
	 * @param e option exception
	 */
    public static void showWarning(String title, String message, Exception e) {
        stayOpen = true;
        if (title == null || title.equals("")) title = "Warning";
        if (instance == null) {
            JOptionPane.showMessageDialog(new Frame(), message, title, JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(instance, message, title, JOptionPane.WARNING_MESSAGE);
        }
        stayOpen = false;
        if (e != null) e.printStackTrace();
    }

    private WindowListener winAdapt;

    private CtrlSketchView treeSketchView;

    private CtrlPropView tblPropView;

    private CtrlTabView tabWindows;

    private GuiControl guiControl;

    /** 
	 * Creates new form GuiDesignFrame 
	 */
    public GuiDesigner() {
        instance = this;
        initComponents();
        initCustomComponents();
        guiControl = new GuiControl(null, tabWindows, treeSketchView, tblPropView);
        createWindowAdapter();
    }

    /**
	 * Creates new form GuiDesignFrame <br>
	 * Keep a reference to the editor
	 * @param theEditor
	 * @param size 
	 */
    public GuiDesigner(Editor theEditor) {
        instance = this;
        editor = theEditor;
        initComponents();
        initCustomComponents();
        metrics = tabWindows.getGraphics().getFontMetrics();
        guiControl = new GuiControl(editor, tabWindows, treeSketchView, tblPropView);
        guiControl.loadGuiLayout();
        Dimension size = guiControl.getSketchSizeFromCode();
        if (size == null) {
            final String message = "The size of this sketch could not automatically be\n" + "determined from your code. You'll have to set the\n" + "width and height in the designer window.";
            showWarning("Could not find applet size", message, null);
        }
        createWindowAdapter();
    }

    private void createWindowAdapter() {
        winAdapt = new WindowAdapter() {

            /**
			 * Invoked when a window is in the process of being closed.
			 * The close operation can be overridden at this point.
			 */
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                setExtendedState(ICONIFIED);
            }

            /**
			 * Invoked when a window has been closed.
			 */
            public void windowClosed(WindowEvent e) {
            }

            /**
			 * Invoked when a window is iconified.
			 */
            public void windowIconified(WindowEvent e) {
            }

            /**
			 * Invoked when a window is de-iconified.
			 */
            public void windowDeiconified(WindowEvent e) {
            }

            /**
			 * Invoked when a window is activated.
			 */
            public void windowActivated(WindowEvent e) {
                setVisible(true);
                setExtendedState(NORMAL);
                guiControl.setSketchSize(guiControl.getSketchSizeFromCode());
                guiControl.codeCapture();
            }

            /**
			 * Invoked when a window is de-activated.
			 */
            public void windowDeactivated(WindowEvent e) {
                if (!stayOpen) {
                    if (autoHide) setExtendedState(ICONIFIED);
                    guiControl.saveGuiLayout();
                    guiControl.codeGeneration();
                }
            }
        };
        addWindowListener(winAdapt);
    }

    /**
	 * A fix since to make it work in Processing
	 */
    private void getIcons() {
        ToolIcon.instance().addElement(DApplication.class, btnWindow.getIcon());
        ToolIcon.instance().addElement(DWindow.class, btnWindow.getIcon());
        ToolIcon.instance().addElement(DPanel.class, btnPanel.getIcon());
        ToolIcon.instance().addElement(DButton.class, btnButton.getIcon());
        ToolIcon.instance().addElement(DLabel.class, btnLabel.getIcon());
        ToolIcon.instance().addElement(DHorzSlider.class, btnHorzSlider.getIcon());
        ToolIcon.instance().addElement(DVertSlider.class, btnVertSlider.getIcon());
        ToolIcon.instance().addElement(DTextField.class, btnTextfield.getIcon());
        ToolIcon.instance().addElement(DCheckbox.class, btnCheckbox.getIcon());
        ToolIcon.instance().addElement(DOptionGroup.class, btnOptGroup.getIcon());
        ToolIcon.instance().addElement(DOption.class, btnOption.getIcon());
        ToolIcon.instance().addElement(DTimer.class, btnTimer.getIcon());
        ToolIcon.instance().addElement(DWSlider.class, btnCoolSlider.getIcon());
        ToolIcon.instance().addElement(DImageButton.class, btnImgButton.getIcon());
        ToolIcon.instance().addElement(DCombo.class, btnCombo.getIcon());
        ToolIcon.instance().addElement(DKnob.class, btnKnob.getIcon());
        ToolIcon.instance().addElement(DActivityBar.class, btnActivityBar.getIcon());
        ToolIcon.instance().addElement("CB_ICON1", new javax.swing.ImageIcon(getClass().getResource("/g4p/cbox_icon1.png")));
        ToolIcon.instance().addElement("CB_ICON2", new javax.swing.ImageIcon(getClass().getResource("/g4p/cbox_icon2.png")));
    }

    private void initCustomComponents() {
        getIcons();
        treeSketchView = new CtrlSketchView();
        tblPropView = new CtrlPropView();
        tabWindows = new CtrlTabView();
        spTop.setViewportView(treeSketchView);
        spBot.setViewportView(tblPropView);
        pnlWindowsView.setLayout(new BorderLayout());
        pnlWindowsView.add(tabWindows, BorderLayout.CENTER);
        treeSketchView.setViewLinks(tabWindows, tblPropView);
        tabWindows.setViewLinks(treeSketchView, tblPropView);
        tblPropView.setViewLinks(tabWindows, treeSketchView);
        tblPropView.setFillsViewportHeight(true);
        mitemGS8.setSelected(true);
        tabWindows.setGridSize(8);
    }

    /** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    private void initComponents() {
        bgGridSize = new javax.swing.ButtonGroup();
        tbarComponents = new javax.swing.JToolBar();
        btnWindow = new javax.swing.JButton();
        btnPanel = new javax.swing.JButton();
        btnButton = new javax.swing.JButton();
        btnImgButton = new javax.swing.JButton();
        btnLabel = new javax.swing.JButton();
        btnTextfield = new javax.swing.JButton();
        btnHorzSlider = new javax.swing.JButton();
        btnVertSlider = new javax.swing.JButton();
        btnCoolSlider = new javax.swing.JButton();
        btnKnob = new javax.swing.JButton();
        btnCheckbox = new javax.swing.JButton();
        btnOption = new javax.swing.JButton();
        btnOptGroup = new javax.swing.JButton();
        btnCombo = new javax.swing.JButton();
        btnTimer = new javax.swing.JButton();
        btnActivityBar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnScale = new javax.swing.JButton();
        splitControl = new javax.swing.JSplitPane();
        pnlTreeView = new java.awt.Panel();
        jLabel2 = new javax.swing.JLabel();
        spTop = new javax.swing.JScrollPane();
        tbarControls = new javax.swing.JToolBar();
        btnRemove = new javax.swing.JButton();
        pnlPropViiew = new java.awt.Panel();
        jLabel1 = new javax.swing.JLabel();
        spBot = new javax.swing.JScrollPane();
        pnlWindowsView = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuDisplay = new javax.swing.JMenu();
        mitemAuto = new javax.swing.JCheckBoxMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mitemSnapToGrid = new javax.swing.JCheckBoxMenuItem();
        mitemShowGrid = new javax.swing.JCheckBoxMenuItem();
        menuGridSize = new javax.swing.JMenu();
        mitemGS4 = new javax.swing.JRadioButtonMenuItem();
        mitemGS5 = new javax.swing.JRadioButtonMenuItem();
        mitemGS8 = new javax.swing.JRadioButtonMenuItem();
        mitemGS10 = new javax.swing.JRadioButtonMenuItem();
        menuZoom = new javax.swing.JMenu();
        mitemScaleWindow = new javax.swing.JMenuItem();
        mitem200 = new javax.swing.JMenuItem();
        mitem150 = new javax.swing.JMenuItem();
        mitem100 = new javax.swing.JMenuItem();
        mitem75 = new javax.swing.JMenuItem();
        mitem50 = new javax.swing.JMenuItem();
        mitem25 = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("GUI Builder");
        setBackground(new java.awt.Color(255, 255, 255));
        setName("frmDesigner");
        tbarComponents.setFloatable(false);
        tbarComponents.setRollover(true);
        tbarComponents.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnWindow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolWindow.png")));
        btnWindow.setToolTipText("Window");
        btnWindow.setFocusable(false);
        btnWindow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnWindow.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnWindow.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWindowActionPerformed(evt);
            }
        });
        tbarComponents.add(btnWindow);
        btnPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolPanel.png")));
        btnPanel.setToolTipText("Floating Panel");
        btnPanel.setFocusable(false);
        btnPanel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPanel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPanel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPanelActionPerformed(evt);
            }
        });
        tbarComponents.add(btnPanel);
        btnButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolButton.png")));
        btnButton.setToolTipText("Button");
        btnButton.setFocusable(false);
        btnButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnButtonActionPerformed(evt);
            }
        });
        tbarComponents.add(btnButton);
        btnImgButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolButtonImg.png")));
        btnImgButton.setToolTipText("Image Button");
        btnImgButton.setFocusable(false);
        btnImgButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImgButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImgButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImgButtonActionPerformed(evt);
            }
        });
        tbarComponents.add(btnImgButton);
        btnLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolLabel.png")));
        btnLabel.setToolTipText("Label");
        btnLabel.setFocusable(false);
        btnLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLabel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLabelActionPerformed(evt);
            }
        });
        tbarComponents.add(btnLabel);
        btnTextfield.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolTextField.png")));
        btnTextfield.setToolTipText("Textfield");
        btnTextfield.setFocusable(false);
        btnTextfield.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTextfield.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTextfield.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTextfieldActionPerformed(evt);
            }
        });
        tbarComponents.add(btnTextfield);
        btnHorzSlider.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolSliderH.png")));
        btnHorzSlider.setToolTipText("Slider");
        btnHorzSlider.setFocusable(false);
        btnHorzSlider.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHorzSlider.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHorzSlider.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHorzSliderActionPerformed(evt);
            }
        });
        tbarComponents.add(btnHorzSlider);
        btnVertSlider.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolSliderV.png")));
        btnVertSlider.setToolTipText("Slider");
        btnVertSlider.setFocusable(false);
        btnVertSlider.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVertSlider.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnVertSlider.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVertSliderActionPerformed(evt);
            }
        });
        tbarComponents.add(btnVertSlider);
        btnCoolSlider.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolCoolSlider.png")));
        btnCoolSlider.setToolTipText("Cool Slider");
        btnCoolSlider.setFocusable(false);
        btnCoolSlider.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCoolSlider.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCoolSlider.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCoolSliderActionPerformed(evt);
            }
        });
        tbarComponents.add(btnCoolSlider);
        btnKnob.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolKnob.png")));
        btnKnob.setToolTipText("Knob");
        btnKnob.setFocusable(false);
        btnKnob.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKnob.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnKnob.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKnobActionPerformed(evt);
            }
        });
        tbarComponents.add(btnKnob);
        btnCheckbox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolCheckbox.png")));
        btnCheckbox.setToolTipText("Checkbox");
        btnCheckbox.setFocusable(false);
        btnCheckbox.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCheckbox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCheckbox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckboxActionPerformed(evt);
            }
        });
        tbarComponents.add(btnCheckbox);
        btnOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolOption.png")));
        btnOption.setToolTipText("Option");
        btnOption.setFocusable(false);
        btnOption.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOption.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOption.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptionActionPerformed(evt);
            }
        });
        tbarComponents.add(btnOption);
        btnOptGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolOptGroup.png")));
        btnOptGroup.setToolTipText("Option group");
        btnOptGroup.setFocusable(false);
        btnOptGroup.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOptGroup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOptGroup.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOptGroupActionPerformed(evt);
            }
        });
        tbarComponents.add(btnOptGroup);
        btnCombo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolCombo.png")));
        btnCombo.setToolTipText("Combo Box");
        btnCombo.setFocusable(false);
        btnCombo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCombo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComboActionPerformed(evt);
            }
        });
        tbarComponents.add(btnCombo);
        btnTimer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolTimer.png")));
        btnTimer.setToolTipText("Timer");
        btnTimer.setFocusable(false);
        btnTimer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTimer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTimer.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimerActionPerformed(evt);
            }
        });
        tbarComponents.add(btnTimer);
        btnActivityBar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolActivityBar.png")));
        btnActivityBar.setToolTipText("Activity Bar");
        btnActivityBar.setFocusable(false);
        btnActivityBar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnActivityBar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnActivityBar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivityBar(evt);
            }
        });
        tbarComponents.add(btnActivityBar);
        jSeparator2.setPreferredSize(new java.awt.Dimension(50, 0));
        jSeparator2.setRequestFocusEnabled(false);
        jSeparator2.setSeparatorSize(new java.awt.Dimension(50, 0));
        tbarComponents.add(jSeparator2);
        btnScale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolResize2.png")));
        btnScale.setToolTipText("Scale to fit");
        btnScale.setFocusable(false);
        btnScale.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnScale.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnScale.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScaleActionPerformed(evt);
            }
        });
        tbarComponents.add(btnScale);
        splitControl.setDividerLocation(260);
        splitControl.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitControl.setDoubleBuffered(true);
        splitControl.setMinimumSize(new java.awt.Dimension(3, 5));
        splitControl.setPreferredSize(new java.awt.Dimension(250, 525));
        jLabel2.setBackground(new java.awt.Color(255, 255, 153));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("CONTROLS");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.setOpaque(true);
        spTop.setBackground(new java.awt.Color(255, 255, 255));
        tbarControls.setFloatable(false);
        tbarControls.setRollover(true);
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/bin.png")));
        btnRemove.setText("Remove");
        btnRemove.setFocusable(false);
        btnRemove.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnRemove.setMaximumSize(new java.awt.Dimension(80, 47));
        btnRemove.setPreferredSize(new java.awt.Dimension(80, 29));
        btnRemove.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        tbarControls.add(btnRemove);
        javax.swing.GroupLayout pnlTreeViewLayout = new javax.swing.GroupLayout(pnlTreeView);
        pnlTreeView.setLayout(pnlTreeViewLayout);
        pnlTreeViewLayout.setHorizontalGroup(pnlTreeViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE).addComponent(tbarControls, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE).addComponent(spTop, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE));
        pnlTreeViewLayout.setVerticalGroup(pnlTreeViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlTreeViewLayout.createSequentialGroup().addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tbarControls, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(spTop, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)));
        splitControl.setTopComponent(pnlTreeView);
        jLabel1.setBackground(new java.awt.Color(255, 255, 153));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PROPERTIES");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(56, 14));
        spBot.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout pnlPropViiewLayout = new javax.swing.GroupLayout(pnlPropViiew);
        pnlPropViiew.setLayout(pnlPropViiewLayout);
        pnlPropViiewLayout.setHorizontalGroup(pnlPropViiewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(spBot, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE).addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE));
        pnlPropViiewLayout.setVerticalGroup(pnlPropViiewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlPropViiewLayout.createSequentialGroup().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(spBot, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)));
        splitControl.setRightComponent(pnlPropViiew);
        pnlWindowsView.setToolTipText("Winodws Panel");
        pnlWindowsView.setDoubleBuffered(false);
        javax.swing.GroupLayout pnlWindowsViewLayout = new javax.swing.GroupLayout(pnlWindowsView);
        pnlWindowsView.setLayout(pnlWindowsViewLayout);
        pnlWindowsViewLayout.setHorizontalGroup(pnlWindowsViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 535, Short.MAX_VALUE));
        pnlWindowsViewLayout.setVerticalGroup(pnlWindowsViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 534, Short.MAX_VALUE));
        menuDisplay.setText("Display");
        mitemAuto.setText("Auto Hide");
        mitemAuto.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemAutoActionPerformed(evt);
            }
        });
        menuDisplay.add(mitemAuto);
        menuDisplay.add(jSeparator1);
        mitemSnapToGrid.setText("Snap to grid");
        mitemSnapToGrid.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemSnapToGridActionPerformed(evt);
            }
        });
        menuDisplay.add(mitemSnapToGrid);
        mitemShowGrid.setText("Show grid");
        mitemShowGrid.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemShowGridActionPerformed(evt);
            }
        });
        menuDisplay.add(mitemShowGrid);
        menuGridSize.setText("Grid Size");
        bgGridSize.add(mitemGS4);
        mitemGS4.setText("4px");
        mitemGS4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemGS4ActionPerformed(evt);
            }
        });
        menuGridSize.add(mitemGS4);
        bgGridSize.add(mitemGS5);
        mitemGS5.setText("5px");
        mitemGS5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemGS5ActionPerformed(evt);
            }
        });
        menuGridSize.add(mitemGS5);
        bgGridSize.add(mitemGS8);
        mitemGS8.setSelected(true);
        mitemGS8.setText("8px");
        mitemGS8.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemGS8ActionPerformed(evt);
            }
        });
        menuGridSize.add(mitemGS8);
        bgGridSize.add(mitemGS10);
        mitemGS10.setText("10px");
        mitemGS10.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemGS10ActionPerformed(evt);
            }
        });
        menuGridSize.add(mitemGS10);
        menuDisplay.add(menuGridSize);
        jMenuBar1.add(menuDisplay);
        menuZoom.setText("Zoom");
        mitemScaleWindow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/g4p/toolResize.png")));
        mitemScaleWindow.setText("Scale window to fit");
        mitemScaleWindow.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemScaleWindowActionPerformed(evt);
            }
        });
        menuZoom.add(mitemScaleWindow);
        mitem200.setText("200%");
        mitem200.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemScale200(evt);
            }
        });
        menuZoom.add(mitem200);
        mitem150.setText("150%");
        mitem150.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemScale150(evt);
            }
        });
        menuZoom.add(mitem150);
        mitem100.setText("100%");
        mitem100.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemScale100(evt);
            }
        });
        menuZoom.add(mitem100);
        mitem75.setText("75%");
        mitem75.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemScale75(evt);
            }
        });
        menuZoom.add(mitem75);
        mitem50.setText("50%");
        mitem50.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemScale50(evt);
            }
        });
        menuZoom.add(mitem50);
        mitem25.setText("25%");
        mitem25.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitemScale25(evt);
            }
        });
        menuZoom.add(mitem25);
        jMenuBar1.add(menuZoom);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(pnlWindowsView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(splitControl, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(tbarComponents, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(tbarComponents, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(splitControl, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE).addComponent(pnlWindowsView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));
        pack();
    }

    private void btnPanelActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DPanel());
    }

    private void btnWindowActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DWindow(false));
    }

    private void btnButtonActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DButton());
    }

    private void btnImgButtonActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DImageButton());
    }

    private void btnLabelActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DLabel());
    }

    private void btnTextfieldActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DTextField());
    }

    private void btnHorzSliderActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DHorzSlider());
    }

    private void btnVertSliderActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DVertSlider());
    }

    private void btnCoolSliderActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DWSlider());
    }

    private void btnKnobActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DKnob());
    }

    private void btnCheckboxActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DCheckbox());
    }

    private void btnOptionActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DOption());
    }

    private void btnOptGroupActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DOptionGroup());
    }

    private void btnComboActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DCombo());
    }

    private void btnTimerActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DTimer());
    }

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.removeComponent();
    }

    private void mitemSnapToGridActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.snapGrid(((JCheckBoxMenuItem) evt.getSource()).isSelected());
    }

    private void mitemShowGridActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.showGrid(((JCheckBoxMenuItem) evt.getSource()).isSelected());
    }

    private void mitemGS4ActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.setGridSize(4);
    }

    private void mitemGS5ActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.setGridSize(5);
    }

    private void mitemGS8ActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.setGridSize(8);
    }

    private void mitemGS10ActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.setGridSize(10);
    }

    private void mitemScaleWindowActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.makeWindowSizeToFit();
    }

    private void btnScaleActionPerformed(java.awt.event.ActionEvent evt) {
        guiControl.makeWindowSizeToFit();
    }

    private void mitemScale200(java.awt.event.ActionEvent evt) {
        guiControl.setScale(200);
    }

    private void mitemScale150(java.awt.event.ActionEvent evt) {
        guiControl.setScale(150);
    }

    private void mitemScale100(java.awt.event.ActionEvent evt) {
        guiControl.setScale(100);
    }

    private void mitemScale75(java.awt.event.ActionEvent evt) {
        guiControl.setScale(75);
    }

    private void mitemScale50(java.awt.event.ActionEvent evt) {
        guiControl.setScale(50);
    }

    private void mitemScale25(java.awt.event.ActionEvent evt) {
        guiControl.setScale(25);
    }

    private void mitemAutoActionPerformed(java.awt.event.ActionEvent evt) {
        autoHide = mitemAuto.isSelected();
    }

    private void btnActivityBar(java.awt.event.ActionEvent evt) {
        guiControl.addComponent(new DActivityBar());
    }

    /**
	 * @param args the command line arguments
	 */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new GuiDesigner().setVisible(true);
            }
        });
    }

    private javax.swing.ButtonGroup bgGridSize;

    private javax.swing.JButton btnActivityBar;

    private javax.swing.JButton btnButton;

    private javax.swing.JButton btnCheckbox;

    private javax.swing.JButton btnCombo;

    private javax.swing.JButton btnCoolSlider;

    private javax.swing.JButton btnHorzSlider;

    private javax.swing.JButton btnImgButton;

    private javax.swing.JButton btnKnob;

    private javax.swing.JButton btnLabel;

    private javax.swing.JButton btnOptGroup;

    private javax.swing.JButton btnOption;

    private javax.swing.JButton btnPanel;

    private javax.swing.JButton btnRemove;

    private javax.swing.JButton btnScale;

    private javax.swing.JButton btnTextfield;

    private javax.swing.JButton btnTimer;

    private javax.swing.JButton btnVertSlider;

    private javax.swing.JButton btnWindow;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JPopupMenu.Separator jSeparator1;

    private javax.swing.JToolBar.Separator jSeparator2;

    private javax.swing.JMenu menuDisplay;

    private javax.swing.JMenu menuGridSize;

    private javax.swing.JMenu menuZoom;

    private javax.swing.JMenuItem mitem100;

    private javax.swing.JMenuItem mitem150;

    private javax.swing.JMenuItem mitem200;

    private javax.swing.JMenuItem mitem25;

    private javax.swing.JMenuItem mitem50;

    private javax.swing.JMenuItem mitem75;

    private javax.swing.JCheckBoxMenuItem mitemAuto;

    private javax.swing.JRadioButtonMenuItem mitemGS10;

    private javax.swing.JRadioButtonMenuItem mitemGS4;

    private javax.swing.JRadioButtonMenuItem mitemGS5;

    private javax.swing.JRadioButtonMenuItem mitemGS8;

    private javax.swing.JMenuItem mitemScaleWindow;

    private javax.swing.JCheckBoxMenuItem mitemShowGrid;

    private javax.swing.JCheckBoxMenuItem mitemSnapToGrid;

    private java.awt.Panel pnlPropViiew;

    private java.awt.Panel pnlTreeView;

    private javax.swing.JPanel pnlWindowsView;

    private javax.swing.JScrollPane spBot;

    private javax.swing.JScrollPane spTop;

    private javax.swing.JSplitPane splitControl;

    private javax.swing.JToolBar tbarComponents;

    private javax.swing.JToolBar tbarControls;
}
