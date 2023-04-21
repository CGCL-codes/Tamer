package cranks.ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.print.*;
import java.util.Vector;
import java.lang.reflect.*;
import cranks.geom.GeometricalObject;
import cranks.mech.FourBarMechanism;
import cranks.undo.RedesignMechanism;
import cranks.undo.ConstructionProcedure;

public class MainFrame extends JFrame {

    public static final String TITLE = "CRANKS 1.0";

    private static MainFrame mfInstance;

    private static SplashScreen splash;

    private static DrawingPanel drawPanel;

    private static BaseApplet baseApp;

    private static PrinterJob printJob;

    private static FourBarMechanism mechanism;

    private static Vector<GeometricalObject> objects;

    public static ConstructionProcedure constructProcedure;

    private static Vector<Editor> editors;

    public static CoordinateCircle coordCircle;

    public static CoordinateLine coordLine;

    public static CoordinatePoint coordPoint;

    public static ModifyPoint changePoint;

    public static CoordinateTriangle coordTriangle;

    public static CopyMoveTriangle copyMoveTri;

    public static FieldValue objectFieldValue;

    public static FileChoose fChoose;

    public static HideRemoveObject hideRemObject;

    public static Intersect intersectObjects;

    public static Move moveObject;

    public static Settings setting;

    public static UpdateMechanism updateMech;

    public static CreateMechanism createMech;

    public static RedesignMechanism redesignMech;

    public static Help userHelp;

    private static boolean menubarEnabled = true;

    public Vector<MenuAction> actions;

    public MenuAction ActionNewWorkspace;

    public MenuAction ActionOpenWorkspace;

    public MenuAction ActionSaveWorkspace;

    public MenuAction ActionSaveWorkspaceAs;

    public MenuAction ActionPageSetup;

    public MenuAction ActionPrint;

    public MenuAction ActionExit;

    public MenuAction ActionPanView;

    public MenuAction ActionSettings;

    public MenuAction ActionEnableAnimationMode;

    public MenuAction ActionSetLinkLengths;

    public MenuAction ActionStartAnimation;

    public MenuAction ActionPauseAnimation;

    public MenuAction ActionToggleTrace;

    public MenuAction ActionEnableDrawingMode;

    public MenuAction ActionPointAddMouse;

    public MenuAction ActionPointAddCoordinates;

    public MenuAction ActionPointModify;

    public MenuAction ActionLineAddMouse;

    public MenuAction ActionLineAddCoordinates;

    public MenuAction ActionCircleAddCoordinates;

    public MenuAction ActionTriangleAddMouse;

    public MenuAction ActionTriangleAddCoordinates;

    public MenuAction ActionTriangleCopyMove;

    public MenuAction ActionIntersect;

    public MenuAction ActionTranslateRotate;

    public MenuAction ActionHide;

    public MenuAction ActionClearAll;

    public MenuAction ActionShowFieldValue;

    public MenuAction ActionUndo;

    public MenuAction ActionRedo;

    public MenuAction ActionCreateMechanism;

    public MenuAction ActionRedesign;

    public MenuAction ActionHelp;

    public MenuAction ActionAbout;

    public MainFrame() {
        super(TITLE);
        objects = new Vector<GeometricalObject>();
        mechanism = new FourBarMechanism();
    }

    public MainFrame(BaseApplet applet) {
        baseApp = applet;
    }

    public static void main(String args[]) {
        MainFrame.start(null);
    }

    public static void start(BaseApplet applet) {
        (new MainFrame(applet)).createAndShowFrame();
    }

    private void createAndShowFrame() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        mfInstance = new MainFrame();
        splash = new SplashScreen(mfInstance);
        if (isApplet()) {
            mfInstance.baseApp = baseApp;
            baseApp.add(splash.getSplash());
        } else {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    splash.setVisible(true);
                }
            });
            mfInstance.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            mfInstance.addWindowListener(new MainFrameWindowAdapter(mfInstance));
        }
        constructProcedure = new ConstructionProcedure(mfInstance);
        mfInstance.createMenuBar();
        drawPanel = new DrawingPanel(mfInstance, objects, mechanism);
        mfInstance.setContentPane(drawPanel);
        coordCircle = new CoordinateCircle(mfInstance, "Draw Circles", objects);
        coordLine = new CoordinateLine(mfInstance, "Draw Lines", objects);
        coordPoint = new CoordinatePoint(mfInstance, "Add Points", objects);
        changePoint = new ModifyPoint(mfInstance, "Modify Points", objects);
        coordTriangle = new CoordinateTriangle(mfInstance, "Draw Triangles", objects);
        copyMoveTri = new CopyMoveTriangle(mfInstance, "Copy and Move Triangle", objects);
        objectFieldValue = new FieldValue(mfInstance, "Get Field Value", objects);
        if (!isApplet()) fChoose = new FileChoose(mfInstance, objects, mechanism, constructProcedure);
        hideRemObject = new HideRemoveObject(mfInstance, "Hide Objects", objects);
        intersectObjects = new Intersect(mfInstance, "Intersect Objects", objects);
        updateMech = new UpdateMechanism(mfInstance, "Set Link Lengths", mechanism);
        moveObject = new Move(mfInstance, "Translate and Rotate Objects", objects);
        setting = new Settings(mfInstance, "Settings", mechanism);
        createMech = new CreateMechanism(mfInstance, "Create Mechanism", objects, mechanism);
        userHelp = new Help(mfInstance, "Help");
        redesignMech = new RedesignMechanism(mfInstance, "Redesign Mechanism", constructProcedure, mechanism);
        mfInstance.registerEditors();
        if (!isApplet()) {
            printJob = PrinterJob.getPrinterJob();
            Book bk = new Book();
            bk.append(drawPanel, printJob.defaultPage());
            printJob.setPageable(bk);
        }
        mfInstance.updateUndoRedoStatus();
        mfInstance.addComponentListener(new MainframeComponentAdapter(mfInstance));
        mfInstance.setTitleString();
        mfInstance.pack();
        mfInstance.setLocationRelativeTo(null);
        try {
            Thread.currentThread().sleep(2000);
        } catch (Exception e) {
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                mfInstance.setVisible(true);
            }
        });
        if (!isApplet()) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    splash.setVisible(false);
                }
            });
        }
    }

    public void componentResized(ComponentEvent ce) {
        if (ce.getComponent() == mfInstance) {
            drawPanel.resizeComponents();
        }
    }

    public boolean isApplet() {
        return (baseApp != null);
    }

    private void registerEditors() {
        editors = new Vector<Editor>();
        Field[] fields = mfInstance.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                Object obj = fields[i].get(mfInstance);
                if (obj instanceof Editor) editors.addElement((Editor) obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Editor[] editorArray = new Editor[1];
        editorArray = editors.toArray(editorArray);
        for (int i = 0; i < editorArray.length; i++) editors.setElementAt(editorArray[i], editorArray[i].getID() - 1);
    }

    public Editor getEditor(int number) {
        return editors.elementAt(number - 1);
    }

    public double getZoom() {
        return drawPanel.getZoom();
    }

    public void setZoom(double value) {
        drawPanel.setZoom(value);
    }

    public void setTitleString() {
        String fileName = (fChoose != null) ? (fChoose.getCurrentFileName()) : ("");
        String title = TITLE + " - ";
        if (fileName.length() > 0) title += fileName; else title += "Untitled";
        if (constructProcedure.hasBeenEdited()) title += "*";
        setTitle(title);
    }

    public void updateUndoRedoStatus() {
        JMenuBar mb = mfInstance.getJMenuBar();
        ActionUndo.setEnabled(constructProcedure.canUndo());
        ActionRedo.setEnabled(constructProcedure.canRedo());
        ActionUndo.putValue(Action.NAME, constructProcedure.getUndoPresentationName());
        ActionRedo.putValue(Action.NAME, constructProcedure.getRedoPresentationName());
        setTitleString();
    }

    public void enableMenus(boolean value) {
        menubarEnabled = value;
        for (int i = 0; i < actions.size(); i++) actions.elementAt(i).setEnabled(value);
    }

    private void createMenuBar() {
        createActions();
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        menu1.setMnemonic(KeyEvent.VK_F);
        menu1.add(ActionNewWorkspace);
        menu1.add(ActionOpenWorkspace);
        menu1.addSeparator();
        menu1.add(ActionSaveWorkspace);
        menu1.add(ActionSaveWorkspaceAs);
        menu1.addSeparator();
        menu1.add(ActionPageSetup);
        menu1.add(ActionPrint);
        menu1.addSeparator();
        menu1.add(ActionExit);
        JMenu menu2 = new JMenu("View");
        menu2.setMnemonic(KeyEvent.VK_V);
        menu2.add(ActionPanView);
        menu2.add(ActionSettings);
        JMenu menu3 = new JMenu("Animation");
        menu3.setMnemonic(KeyEvent.VK_A);
        menu3.add(ActionEnableAnimationMode);
        menu3.add(ActionSetLinkLengths);
        menu3.add(ActionStartAnimation);
        menu3.add(ActionPauseAnimation);
        menu3.add(ActionToggleTrace);
        JMenu menu4 = new JMenu("Drawing");
        menu4.setMnemonic(KeyEvent.VK_D);
        menu4.add(ActionEnableDrawingMode);
        JMenu menu4_1 = new JMenu("Point");
        menu4_1.setMnemonic(KeyEvent.VK_P);
        menu4_1.add(ActionPointAddMouse);
        menu4_1.add(ActionPointAddCoordinates);
        menu4_1.add(ActionPointModify);
        menu4.add(menu4_1);
        JMenu menu4_2 = new JMenu("Line");
        menu4_2.setMnemonic(KeyEvent.VK_L);
        menu4_2.add(ActionLineAddMouse);
        menu4_2.add(ActionLineAddCoordinates);
        menu4.add(menu4_2);
        JMenu menu4_3 = new JMenu("Circle");
        menu4_3.setMnemonic(KeyEvent.VK_C);
        menu4_3.add(ActionCircleAddCoordinates);
        menu4.add(menu4_3);
        JMenu menu4_4 = new JMenu("Triangle");
        menu4_4.setMnemonic(KeyEvent.VK_T);
        menu4_4.add(ActionTriangleAddMouse);
        menu4_4.add(ActionTriangleAddCoordinates);
        menu4_4.add(ActionTriangleCopyMove);
        menu4.add(menu4_4);
        menu4.add(ActionIntersect);
        menu4.add(ActionTranslateRotate);
        menu4.add(ActionHide);
        menu4.add(ActionClearAll);
        menu4.add(ActionShowFieldValue);
        JMenu menu5 = new JMenu("Construction");
        menu5.setMnemonic(KeyEvent.VK_C);
        menu5.add(ActionUndo);
        menu5.add(ActionRedo);
        menu5.add(ActionCreateMechanism);
        menu5.add(ActionRedesign);
        JMenu menu6 = new JMenu("Help");
        menu6.setMnemonic(KeyEvent.VK_H);
        menu6.add(ActionHelp);
        menu6.add(ActionAbout);
        if (!isApplet()) menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);
        menuBar.add(menu4);
        menuBar.add(menu5);
        menuBar.add(menu6);
        setJMenuBar(menuBar);
    }

    private void createActions() {
        ActionNewWorkspace = new MenuAction("New WorkSpace", KeyEvent.VK_N, "1_1", KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                boolean proceed = true;
                if (constructProcedure.hasBeenEdited()) proceed = fChoose.askForSavingWorkspace();
                if (proceed) {
                    fChoose.newWorkspace();
                    drawPanel.changeMode(DrawingPanel.OPERATING_MODE_CLEAR, DrawingPanel.MOUSE_MODE_NONE);
                    constructProcedure.irreversibleEditHappened();
                }
            }
        };
        ActionOpenWorkspace = new MenuAction("Open WorkSpace", KeyEvent.VK_O, "1_2", KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                boolean proceed = true;
                if (constructProcedure.hasBeenEdited()) proceed = fChoose.askForSavingWorkspace();
                if (proceed) {
                    if (fChoose.initDialog(FileChoose.OPEN_WORKSPACE)) drawPanel.changeMode(DrawingPanel.OPERATING_MODE_DRAWING, DrawingPanel.MOUSE_MODE_NONE);
                }
            }
        };
        ActionSaveWorkspace = new MenuAction("Save WorkSpace", KeyEvent.VK_S, "1_3", KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                fChoose.initDialog(FileChoose.SAVE_WORKSPACE);
            }
        };
        ActionSaveWorkspaceAs = new MenuAction("Save WorkSpace As", KeyEvent.VK_A, "1_4", null) {

            public void actionPerformed(ActionEvent ae) {
                fChoose.initDialog(FileChoose.SAVE_WORKSPACE_AS);
            }
        };
        ActionPageSetup = new MenuAction("Page Setup", KeyEvent.VK_G, "1_5", null) {

            public void actionPerformed(ActionEvent ae) {
                printJob.pageDialog(printJob.defaultPage());
            }
        };
        ActionPrint = new MenuAction("Print", KeyEvent.VK_P, "1_6", KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                if (printJob.printDialog()) try {
                    printJob.print();
                } catch (PrinterException pe) {
                    pe.printStackTrace();
                }
            }
        };
        ActionExit = new MenuAction("Exit", KeyEvent.VK_X, "1_7", KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                drawPanel.pauseAnimation();
                if (!isApplet()) {
                    if (constructProcedure.hasBeenEdited()) {
                        if (!fChoose.askForSavingWorkspace()) return;
                    }
                    System.exit(0);
                }
            }
        };
        ActionPanView = new MenuAction("Pan View", KeyEvent.VK_P, "2_2", null) {

            public void actionPerformed(ActionEvent ae) {
                drawPanel.changeMode(DrawingPanel.OPERATING_MODE_PANNING, DrawingPanel.MOUSE_MODE_NONE);
            }
        };
        ActionSettings = new MenuAction("Settings", KeyEvent.VK_T, "2_2", null) {

            public void actionPerformed(ActionEvent ae) {
                drawPanel.pauseAnimation();
                setting.initDialog();
            }
        };
        ActionEnableAnimationMode = new MenuAction("Enable Animation Mode", KeyEvent.VK_A, "3_1", KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                drawPanel.changeMode(DrawingPanel.OPERATING_MODE_ANIMATION, DrawingPanel.MOUSE_MODE_NONE);
            }
        };
        ActionSetLinkLengths = new MenuAction("Set Link Lengths", KeyEvent.VK_L, "3_2", KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableAnimationMode.invoke();
                drawPanel.pauseAnimation();
                updateMech.initDialog();
            }
        };
        ActionStartAnimation = new MenuAction("Start Animation", KeyEvent.VK_T, "3_3", KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableAnimationMode.invoke();
                drawPanel.startAnimation();
            }
        };
        ActionPauseAnimation = new MenuAction("Pause Animation", KeyEvent.VK_U, "3_4", KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableAnimationMode.invoke();
                drawPanel.pauseAnimation();
            }
        };
        ActionToggleTrace = new MenuAction("Toggle Trace", KeyEvent.VK_R, "3_5", null) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableAnimationMode.invoke();
                mechanism.toggleTrace();
            }
        };
        ActionEnableDrawingMode = new MenuAction("Enable Drawing Mode", KeyEvent.VK_D, "4_1", KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                drawPanel.pauseAnimation();
                drawPanel.changeMode(DrawingPanel.OPERATING_MODE_DRAWING, DrawingPanel.MOUSE_MODE_NONE);
            }
        };
        ActionPointAddMouse = new MenuAction("Add using Mouse", KeyEvent.VK_M, "4_1_1", KeyStroke.getKeyStroke(KeyEvent.VK_P, (ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK))) {

            public void actionPerformed(ActionEvent ae) {
                drawPanel.changeMode(DrawingPanel.OPERATING_MODE_DRAWING, DrawingPanel.MOUSE_MODE_POINT);
            }
        };
        ActionPointAddCoordinates = new MenuAction("Add using Co-ordinates", KeyEvent.VK_C, "4_1_2", KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.SHIFT_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                coordPoint.initDialog();
            }
        };
        ActionPointModify = new MenuAction("Modify Co-ordinates", KeyEvent.VK_D, "4_1_3", null) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                changePoint.initDialog();
            }
        };
        ActionLineAddMouse = new MenuAction("Add using Mouse", KeyEvent.VK_M, "4_2_1", KeyStroke.getKeyStroke(KeyEvent.VK_L, (ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK))) {

            public void actionPerformed(ActionEvent ae) {
                drawPanel.changeMode(DrawingPanel.OPERATING_MODE_DRAWING, DrawingPanel.MOUSE_MODE_LINE);
            }
        };
        ActionLineAddCoordinates = new MenuAction("Add using Co-ordinates", KeyEvent.VK_C, "4_2_2", KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.SHIFT_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                coordLine.initDialog();
            }
        };
        ActionCircleAddCoordinates = new MenuAction("Add using Coordinates", KeyEvent.VK_C, "4_3_1", KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.SHIFT_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                coordCircle.initDialog();
            }
        };
        ActionTriangleAddMouse = new MenuAction("Add using Mouse", KeyEvent.VK_M, "4_4_1", KeyStroke.getKeyStroke(KeyEvent.VK_T, (ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK))) {

            public void actionPerformed(ActionEvent ae) {
                drawPanel.changeMode(DrawingPanel.OPERATING_MODE_DRAWING, DrawingPanel.MOUSE_MODE_TRIANGLE);
            }
        };
        ActionTriangleAddCoordinates = new MenuAction("Add using Co-ordinates", KeyEvent.VK_C, "4_4_2", KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.SHIFT_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                coordTriangle.initDialog();
            }
        };
        ActionTriangleCopyMove = new MenuAction("Copy and Move", KeyEvent.VK_P, "4_4_3", null) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                copyMoveTri.initDialog();
            }
        };
        ActionIntersect = new MenuAction("Intersect Objects", KeyEvent.VK_I, "4_2", KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                intersectObjects.initDialog();
            }
        };
        ActionTranslateRotate = new MenuAction("Translate/Rotate Object", KeyEvent.VK_R, "4_3", KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                moveObject.initDialog();
            }
        };
        ActionHide = new MenuAction("Hide Object", KeyEvent.VK_H, "4_4", KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                hideRemObject.initDialog();
            }
        };
        ActionClearAll = new MenuAction("Clear All", KeyEvent.VK_A, "4_5", null) {

            public void actionPerformed(ActionEvent ae) {
                int n = JOptionPane.showConfirmDialog(mfInstance, "Are you sure you want to clear ?", "Clear Drawing", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    drawPanel.changeMode(DrawingPanel.OPERATING_MODE_CLEAR, DrawingPanel.MOUSE_MODE_NONE);
                    drawPanel.repaint();
                    constructProcedure.irreversibleEditHappened();
                }
            }
        };
        ActionShowFieldValue = new MenuAction("Properties", KeyEvent.VK_S, "4_6", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, ActionEvent.ALT_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                objectFieldValue.initDialog();
            }
        };
        ActionUndo = new MenuAction("Undo", KeyEvent.VK_U, "5_1", KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                constructProcedure.undo();
            }
        };
        ActionRedo = new MenuAction("Redo", KeyEvent.VK_R, "5_2", KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                constructProcedure.redo();
            }
        };
        ActionCreateMechanism = new MenuAction("Create Mechanism", KeyEvent.VK_C, "5_3", null) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                createMech.initDialog();
            }
        };
        ActionRedesign = new MenuAction("Redesign Mechanism", KeyEvent.VK_D, "5_4", null) {

            public void actionPerformed(ActionEvent ae) {
                ActionEnableDrawingMode.invoke();
                redesignMech.initDialog();
            }
        };
        ActionHelp = new MenuAction("Help Contents", KeyEvent.VK_H, "6_1", KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.SHIFT_MASK)) {

            public void actionPerformed(ActionEvent ae) {
                userHelp.initDialog();
            }
        };
        ActionAbout = new MenuAction("About", KeyEvent.VK_A, "6_2", null) {

            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(mfInstance, splash.getSplash(), "About", JOptionPane.PLAIN_MESSAGE);
            }
        };
        actions = new Vector<MenuAction>();
        int counter = 0;
        Field[] fields = mfInstance.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                Object obj = fields[i].get(mfInstance);
                if (obj instanceof MenuAction) actions.addElement((MenuAction) obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

class MainframeComponentAdapter extends ComponentAdapter {

    MainFrame mfInstance;

    public MainframeComponentAdapter(MainFrame Adaptee) {
        mfInstance = Adaptee;
    }

    public void componentResized(ComponentEvent ce) {
        mfInstance.componentResized(ce);
    }
}

class MainFrameWindowAdapter extends WindowAdapter {

    MainFrame mfInstance;

    public MainFrameWindowAdapter(MainFrame mf) {
        mfInstance = mf;
    }

    public void windowClosing(WindowEvent we) {
        mfInstance.ActionExit.invoke();
    }
}
