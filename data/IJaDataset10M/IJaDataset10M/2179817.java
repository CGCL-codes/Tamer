package jpatch.boundary.action;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jpatch.boundary.*;
import jpatch.boundary.laf.*;
import jpatch.boundary.settings.*;
import jpatch.entity.*;

public final class Command implements KeyListener {

    private static final boolean DEBUG = false;

    private static final Command INSTANCE = new Command();

    private Map commandActionMap = new HashMap();

    private Map commandButtonMap = new HashMap();

    private Map commandMenuItemMap = new HashMap();

    private Map commandKeyMap = new HashMap();

    private Map keyCommandMap = new HashMap();

    public static Command getInstance() {
        return INSTANCE;
    }

    public static AbstractButton getButtonFor(String command) {
        AbstractButton button = (AbstractButton) INSTANCE.commandButtonMap.get(command);
        AbstractButton newButton;
        if (button instanceof JPatchToggleButton) newButton = new JPatchToggleButton(); else newButton = new JPatchButton();
        newButton.setModel(button.getModel());
        newButton.setText(button.getText());
        newButton.setIcon(button.getIcon());
        newButton.setSelectedIcon(button.getSelectedIcon());
        String toolTipText = (String) ((Action) INSTANCE.commandActionMap.get(command)).getValue(Action.SHORT_DESCRIPTION);
        if (toolTipText == null) toolTipText = command;
        String key = (String) INSTANCE.commandKeyMap.get(command);
        if (key != null) toolTipText = toolTipText + " [" + key + "]";
        newButton.setToolTipText(toolTipText);
        return newButton;
    }

    public static JMenuItem getMenuItemFor(String command) {
        JMenuItem menuItem = (JMenuItem) INSTANCE.commandMenuItemMap.get(command);
        JMenuItem newItem;
        if (menuItem instanceof JRadioButtonMenuItem) newItem = new JRadioButtonMenuItem(); else if (menuItem instanceof JCheckBoxMenuItem) newItem = new JCheckBoxMenuItem(); else newItem = new JMenuItem();
        String itemText = menuItem.getText();
        String key = (String) INSTANCE.commandKeyMap.get(command);
        if (key != null) itemText = itemText + " [" + key + "]";
        newItem.setText(itemText);
        newItem.setIcon(menuItem.getIcon());
        newItem.setModel(menuItem.getModel());
        return newItem;
    }

    public static Action getActionFor(String command) {
        return (Action) INSTANCE.commandActionMap.get(command);
    }

    public static void setViewDefinition(ViewDefinition viewDef) {
        ((JMenuItem) INSTANCE.commandMenuItemMap.get("show points")).setSelected(viewDef.renderPoints());
        ((JMenuItem) INSTANCE.commandMenuItemMap.get("show curves")).setSelected(viewDef.renderCurves());
        ((JMenuItem) INSTANCE.commandMenuItemMap.get("show patches")).setSelected(viewDef.renderPatches());
        ((JMenuItem) INSTANCE.commandMenuItemMap.get("show rotoscope")).setSelected(viewDef.showRotoscope());
        ((JMenuItem) INSTANCE.commandMenuItemMap.get("lock view")).setSelected(viewDef.isLocked());
        INSTANCE.enableCommand("unlock view", viewDef.isLocked());
        INSTANCE.enableCommand("show patches", viewDef.getDrawable().isShadingSupported());
        INSTANCE.enableCommand("clear rotoscope image", MainFrame.getInstance().getModel() != null && MainFrame.getInstance().getModel().getRotoscope(viewDef.getView()) != null);
    }

    public Command() {
        LookAndFeel jpatch = null, crossplatform = null, system = null;
        try {
            jpatch = new SmoothLookAndFeel();
            crossplatform = (LookAndFeel) Class.forName(UIManager.getCrossPlatformLookAndFeelClassName()).newInstance();
            system = (LookAndFeel) Class.forName(UIManager.getSystemLookAndFeelClassName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        put("delete", new DeleteControlPointAction(), new JMenuItem());
        put("remove", new RemoveControlPointAction(), new JMenuItem());
        put("insert point", new InsertControlPointAction(), new JMenuItem());
        put("next curve", new NextCurveAction(), new JMenuItem());
        put("new model", new NewModelAction(), new JMenuItem(), new JPatchButton());
        put("new animation", new NewAnimAction(), new JMenuItem(), new JPatchButton());
        put("open", new ImportJPatchAction(), new JMenuItem(), new JPatchButton());
        put("save", new SaveAsAction(false), new JMenuItem(), new JPatchButton());
        put("single view", new ViewSingleAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("horizontally split view", new ViewSplitHorizontalAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("vertically split view", new ViewSplitVerticalAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("quad view", new ViewQuadAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("rotate view", new ViewRotateAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("move view", new ViewMoveAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("zoom view", new ViewZoomAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("zoom to fit", new ZoomToFitAction(), new JMenuItem(), new JPatchButton());
        put("undo", new UndoAction(), new JMenuItem(), new JPatchButton());
        put("redo", new RedoAction(), new JMenuItem(), new JPatchButton());
        put("lock x", new XLockAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("lock y", new YLockAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("lock z", new ZLockAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("snap to grid", new GridSnapAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("hide", new HideAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("stop edit morph", new StopEditMorphAction(), new JMenuItem(), new JPatchButton());
        put("select points", new SelectPointsAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("select bones", new SelectBonesAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("lock points", new LockPointsAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("lock bones", new LockBonesAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("default tool", new SelectMoveAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("add curve segment", new AddControlPointAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("add bone", new AddBoneAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("rotate tool", new RotateAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("weight selection tool", new WeightSelectionAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("knife tool", new KnifeAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("detach", new DetachControlPointsAction(), new JMenuItem(), new JPatchButton());
        put("rotoscope tool", new RotoscopeAction(), new JRadioButtonMenuItem(), new JPatchToggleButton());
        put("tangent tool", new TangentAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("peak tangents", new PeakAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("round tangents", new RoundAction(), new JCheckBoxMenuItem(), new JPatchToggleButton());
        put("clone", new CloneAction(), new JMenuItem(), new JPatchButton());
        put("extrude", new ExtrudeAction(), new JMenuItem(), new JPatchButton());
        put("lathe", new LatheAction(), new JMenuItem(), new JPatchButton());
        put("lathe editor", new LatheEditorAction(), new JMenuItem(), new JPatchButton());
        put("make patch", new MakeFivePointPatchAction(), new JMenuItem(), new JPatchButton());
        put("compute patches", new ComputePatchesAction(), new JMenuItem(), new JPatchButton());
        put("open animation", new ImportJPatchAnimationAction(), new JMenuItem());
        put("append", new ImportJPatchAction(false), new JMenuItem());
        put("save as", new SaveAsAction(true), new JMenuItem());
        put("import spatch", new ImportSPatchAction(), new JMenuItem());
        put("import animationmaster", new ImportAnimationMasterAction(), new JMenuItem());
        put("export aliaswavefront", new ExportWavefrontAction(), new JMenuItem());
        put("export povray", new ExportPovrayAction(), new JMenuItem());
        put("export renderman", new ExportRibAction(), new JMenuItem());
        put("quit", new QuitAction(), new JMenuItem());
        put("synchronize viewports", new SyncScreensAction(), new JCheckBoxMenuItem());
        put("settings", new EditSettingsAction(), new JMenuItem());
        put("grid spacing settings", new SetGridSpacingAction(), new JMenuItem());
        put("install jogl", new InstallJoglAction(), new JMenuItem());
        put("jpatch lookandfeel", new SwitchLookAndFeelAction("JPatch", jpatch), new JRadioButtonMenuItem());
        put("crossplatform lookandfeel", new SwitchLookAndFeelAction("Metal", crossplatform), new JRadioButtonMenuItem());
        put("system lookandfeel", new SwitchLookAndFeelAction("System", system), new JRadioButtonMenuItem());
        put("phoneme morph mapping", new EditPhonemesAction(), new JMenuItem());
        put("show anim controls", new AnimControlsAction(), new JMenuItem());
        put("dump", new DumpAction(), new JMenuItem());
        put("dump xml", new XmlDumpAction(), new JMenuItem());
        put("dump undo stack", new DumpUndoStackAction(), new JMenuItem());
        put("check model", new CheckModelAction(), new JMenuItem());
        put("controlpoint browser", new ControlPointBrowserAction(), new JMenuItem());
        put("show reference", new ShowReferenceAction(), new JCheckBoxMenuItem());
        put("show about", new AboutAction(), new JMenuItem());
        put("show splashscreen", new ShowSplashAction(), new JMenuItem());
        put("show points", new ShowPointsAction(), new JCheckBoxMenuItem());
        put("show curves", new ShowCurvesAction(), new JCheckBoxMenuItem());
        put("show patches", new ShowPatchesAction(), new JCheckBoxMenuItem());
        put("show rotoscope", new ShowRotoscopeAction(), new JCheckBoxMenuItem());
        put("front view", new ViewAction(ViewDefinition.FRONT), new JRadioButtonMenuItem());
        put("rear view", new ViewAction(ViewDefinition.REAR), new JRadioButtonMenuItem());
        put("top view", new ViewAction(ViewDefinition.TOP), new JRadioButtonMenuItem());
        put("bottom view", new ViewAction(ViewDefinition.BOTTOM), new JRadioButtonMenuItem());
        put("left view", new ViewAction(ViewDefinition.LEFT), new JRadioButtonMenuItem());
        put("right view", new ViewAction(ViewDefinition.RIGHT), new JRadioButtonMenuItem());
        put("bird's eye view", new ViewAction(ViewDefinition.BIRDS_EYE), new JRadioButtonMenuItem());
        put("set rotoscope image", new SetRotoscopeAction(), new JMenuItem());
        put("clear rotoscope image", new ClearRotoscopeAction(), new JMenuItem());
        put("lock view", new SetViewLockAction(true), new JCheckBoxMenuItem());
        put("unlock view", new SetViewLockAction(false), new JMenuItem());
        put("select none", new SelectNoneAction(), new JMenuItem());
        put("select all", new SelectAllAction(), new JMenuItem());
        put("invert selection", new InvertSelectionAction(), new JMenuItem());
        put("expand selection", new ExtendSelectionAction(), new JMenuItem());
        put("flip x", new FlipAction(FlipAction.X), new JMenuItem());
        put("flip y", new FlipAction(FlipAction.Y), new JMenuItem());
        put("flip z", new FlipAction(FlipAction.Z), new JMenuItem());
        put("flip patches", new FlipPatchesAction(), new JMenuItem());
        put("align patches", new AlignPatchesAction(), new JMenuItem());
        put("align controlpoints", new AlignAction(), new JMenuItem());
        put("automirror", new AutoMirrorAction(), new JMenuItem());
        put("add stubs", new AddStubsAction(), new JMenuItem());
        put("remove stubs", new RemoveStubsAction(), new JMenuItem());
        put("change tangents: round", new ChangeTangentModeAction(ChangeTangentModeAction.JPATCH), new JMenuItem());
        put("change tangents: peak", new ChangeTangentModeAction(ChangeTangentModeAction.PEAK), new JMenuItem());
        put("change tangents: spatch", new ChangeTangentModeAction(ChangeTangentModeAction.SPATCH), new JMenuItem());
        put("assign controlpoints to bones", new AssignPointsToBonesAction(), new JMenuItem());
        ((AbstractButton) commandButtonMap.get("lock x")).setSelectedIcon(new ImageIcon(ClassLoader.getSystemResource("jpatch/images/xlocked.png")));
        ((AbstractButton) commandButtonMap.get("lock y")).setSelectedIcon(new ImageIcon(ClassLoader.getSystemResource("jpatch/images/ylocked.png")));
        ((AbstractButton) commandButtonMap.get("lock z")).setSelectedIcon(new ImageIcon(ClassLoader.getSystemResource("jpatch/images/zlocked.png")));
        ((AbstractButton) commandButtonMap.get("snap to grid")).setSelectedIcon(new ImageIcon(ClassLoader.getSystemResource("jpatch/images/grid_snap.png")));
        ((AbstractButton) commandButtonMap.get("hide")).setSelectedIcon(new ImageIcon(ClassLoader.getSystemResource("jpatch/images/hide2.png")));
        ((AbstractButton) commandButtonMap.get("select points")).setSelectedIcon(new ImageIcon(ClassLoader.getSystemResource("jpatch/images/cp_selected.png")));
        ((AbstractButton) commandButtonMap.get("select bones")).setSelectedIcon(new ImageIcon(ClassLoader.getSystemResource("jpatch/images/bone_selected.png")));
        ((AbstractButton) commandButtonMap.get("lock points")).setSelectedIcon(new ImageIcon(ClassLoader.getSystemResource("jpatch/images/cp_locked.png")));
        ((AbstractButton) commandButtonMap.get("lock bones")).setSelectedIcon(new ImageIcon(ClassLoader.getSystemResource("jpatch/images/bone_locked.png")));
        createGroup(new String[] { "default tool", "add curve segment", "add bone", "weight selection tool", "rotate tool", "rotoscope tool", "rotate view", "move view", "zoom view", "knife tool" }, 0);
        createGroup(new String[] { "single view", "horizontally split view", "vertically split view", "quad view" }, Settings.getInstance().viewports.viewportMode.ordinal());
        createGroup(new String[] { "front view", "rear view", "top view", "bottom view", "left view", "right view", "bird's eye view" }, 0);
        String laf = UIManager.getLookAndFeel().getClass().getName();
        int i = 0;
        if (laf.equals(UIManager.getCrossPlatformLookAndFeelClassName())) i = 1; else if (laf.equals(UIManager.getSystemLookAndFeelClassName())) i = 2;
        createGroup(new String[] { "jpatch lookandfeel", "crossplatform lookandfeel", "system lookandfeel" }, i);
        enableCommand("clear rotoscope image", false);
        enableCommand("stop edit morph", false);
        ((AbstractButton) commandMenuItemMap.get("select points")).setSelected(true);
        ((AbstractButton) commandMenuItemMap.get("select bones")).setSelected(true);
        ((AbstractButton) commandMenuItemMap.get("snap to grid")).setSelected(Settings.getInstance().viewports.snapToGrid);
        ((AbstractButton) commandMenuItemMap.get("select points")).getModel().addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                MainFrame.getInstance().getJPatchScreen().setSelectPoints(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        ((AbstractButton) commandMenuItemMap.get("select bones")).getModel().addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                MainFrame.getInstance().getJPatchScreen().setSelectBones(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        ((AbstractButton) commandMenuItemMap.get("lock points")).getModel().addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                MainFrame.getInstance().getJPatchScreen().setLockPoints(selected);
                ((Action) commandActionMap.get("add curve segment")).setEnabled(!selected);
            }
        });
        ((AbstractButton) commandMenuItemMap.get("lock bones")).getModel().addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                MainFrame.getInstance().getJPatchScreen().setLockBones(selected);
                ((Action) commandActionMap.get("add bone")).setEnabled(!selected);
            }
        });
    }

    public void executeCommand(String command) {
        checkCommand(command);
        AbstractButton button = (AbstractButton) commandButtonMap.get(command);
        if (button != null) {
            button.doClick();
            return;
        }
        Action action = (Action) commandActionMap.get(command);
        if (action != null) {
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, (String) action.getValue(Action.ACTION_COMMAND_KEY)));
        }
    }

    public void enableCommand(String command, boolean enable) {
        checkCommand(command);
        ((Action) commandActionMap.get(command)).setEnabled(enable);
    }

    public void enableCommands(String[] commands, boolean enable) {
        for (int i = 0; i < commands.length; i++) enableCommand(commands[i], enable);
    }

    public boolean isCommandEnabled(String command) {
        checkCommand(command);
        return ((Action) commandActionMap.get(command)).isEnabled();
    }

    public void keyTyped(KeyEvent e) {
        String command = (String) keyCommandMap.get(KeyStroke.getKeyStrokeForEvent(e));
        if (DEBUG) System.out.println(KeyStroke.getKeyStrokeForEvent(e) + ": Command = " + command);
    }

    public void keyPressed(KeyEvent e) {
        String command = (String) keyCommandMap.get(KeyStroke.getKeyStrokeForEvent(e));
        if (DEBUG) System.out.println(KeyStroke.getKeyStrokeForEvent(e) + ": Command = " + command);
        if (command != null) {
            executeCommand(command);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    private void put(String command, Action action, JMenuItem menuItem) {
        put(command, action, menuItem, null);
    }

    private void put(String command, Action action, JMenuItem menuItem, AbstractButton button) {
        commandActionMap.put(command, action);
        if (button != null) {
            button.setIcon((Icon) action.getValue(Action.SMALL_ICON));
            button.setModel(menuItem.getModel());
            commandButtonMap.put(command, button);
        }
        if (menuItem != null) {
            menuItem.setAction(action);
            commandMenuItemMap.put(command, menuItem);
            menuItem.setText((String) action.getValue(Action.SHORT_DESCRIPTION));
            if (menuItem.getText() == null) menuItem.setText(command);
        }
    }

    public void setKeyBinding(String key, String command) {
        keyCommandMap.put(KeyStroke.getKeyStroke(key), command);
        commandKeyMap.put(command, key);
    }

    private void createGroup(String[] commands, int selectedIndex) {
        ButtonGroup items = new ButtonGroup();
        for (int i = 0; i < commands.length; i++) {
            checkCommand(commands[i]);
            items.add((JMenuItem) commandMenuItemMap.get(commands[i]));
        }
        items.setSelected(((JMenuItem) commandMenuItemMap.get(commands[selectedIndex])).getModel(), true);
    }

    private void checkCommand(String command) {
        if (!commandActionMap.containsKey(command)) throw new IllegalArgumentException("unknown command: " + command);
    }
}
