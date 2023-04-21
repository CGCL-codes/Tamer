package taskblocks.app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import taskblocks.graph.GraphActionListener;
import taskblocks.graph.TaskGraphComponent;
import taskblocks.io.BugzillaExportDialog;
import taskblocks.io.ProjectSaveLoad;
import taskblocks.io.WrongDataException;
import taskblocks.modelimpl.ManImpl;
import taskblocks.modelimpl.TaskImpl;
import taskblocks.modelimpl.TaskModelImpl;
import taskblocks.modelimpl.TaskPainterImpl;
import taskblocks.modelimpl.UndoActionManModify;
import taskblocks.modelimpl.UndoActionTaskModify;
import taskblocks.modelimpl.UndoManager;

public class ProjectFrame extends JFrame implements WindowListener, GraphActionListener {

    static int _numWindows;

    static List<JMenuItem> _windowMenuItems = new ArrayList<JMenuItem>();

    TaskModelImpl _taskModel;

    TaskGraphComponent _graph;

    URL _file;

    boolean _newCleanProject;

    JCheckBoxMenuItem _myWindowMenuItem;

    Preferences _prefs = Preferences.userNodeForPackage(this.getClass());

    Action _shrinkAction = new MyAction("Shrink", TaskBlocks.getImage("shrink.png"), "Shrink tasks as near as possible") {

        public void actionPerformed(ActionEvent e) {
            _graph.getGraphRepresentation().shrinkTasks();
            _graph.repaint();
        }
    };

    Action _scaleDownAction = new MyAction("Zoom Out", TaskBlocks.getImage("zoomOut.png")) {

        public void actionPerformed(ActionEvent arg0) {
            _graph.scaleDown();
        }
    };

    Action _scaleUpAction = new MyAction("Zoom In", TaskBlocks.getImage("zoomIn.png")) {

        public void actionPerformed(ActionEvent arg0) {
            _graph.scaleUp();
        }
    };

    Action _closeFileAction = new MyAction("Close") {

        public void actionPerformed(ActionEvent arg0) {
            tryClose();
        }
    };

    Action _loadFileAction = new MyAction("Open...", TaskBlocks.getImage("folder.gif"), "Lets you open an existing project") {

        public void actionPerformed(ActionEvent e) {
            File f = null;
            if (TaskBlocks.RUNNING_ON_MAC || TaskBlocks.RUNNING_ON_WINDOWS) {
                FileDialog fd = new FileDialog(ProjectFrame.this, "blabla", FileDialog.LOAD);
                fd.setVisible(true);
                if (fd.getFile() != null) {
                    f = new File(fd.getDirectory(), fd.getFile());
                }
            } else {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.showOpenDialog(ProjectFrame.this);
                f = fc.getSelectedFile();
            }
            if (f != null) {
                openFile(f);
            }
        }
    };

    class OpenRecentFileAction extends MyAction {

        String _path;

        public OpenRecentFileAction(String path) {
            super(path);
            _path = path;
        }

        public void actionPerformed(ActionEvent e) {
            openFile(new File(_path));
        }
    }

    Action _newFileAction = new MyAction("New Project") {

        public void actionPerformed(ActionEvent e) {
            new ProjectFrame();
        }
    };

    Action _saveAction = new MyAction("Save", TaskBlocks.getImage("save.png")) {

        public void actionPerformed(ActionEvent e) {
            save();
        }
    };

    MyAction _undoAction = new MyAction("Undo") {

        public void actionPerformed(ActionEvent e) {
            final UndoManager um = _taskModel.getUndoManager();
            if (um.canUndo()) {
                _graph.getGraphRepresentation().updateModel();
                um.undo();
                _graph.setModel(_taskModel);
                _graph.getGraphRepresentation().setDirty();
                _graph.repaint();
            }
        }

        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
        }
    };

    MyAction _redoAction = new MyAction("Redo") {

        public void actionPerformed(ActionEvent e) {
            final UndoManager um = _taskModel.getUndoManager();
            if (um.canRedo()) {
                _graph.getGraphRepresentation().updateModel();
                um.redo();
                _graph.setModel(_taskModel);
                _graph.getGraphRepresentation().setDirty();
                _graph.repaint();
            }
        }
    };

    Action _saveAsAction = new MyAction("Save As...") {

        public void actionPerformed(ActionEvent e) {
            URL oldFile = _file;
            _file = null;
            if (!save()) {
                _file = oldFile;
            }
        }
    };

    Action _leftAction = new MyAction("Left", TaskBlocks.getImage("left.gif"), "Scrolls left") {

        public void actionPerformed(ActionEvent e) {
            _graph.moveLeft();
        }
    };

    Action _focusTodayAction = new MyAction("Focus on today", TaskBlocks.getImage("down.gif"), "Scrolls to current day") {

        public void actionPerformed(ActionEvent e) {
            _graph.focusOnToday();
        }
    };

    Action _rightAction = new MyAction("Right", TaskBlocks.getImage("right.gif"), "Scrolls right") {

        public void actionPerformed(ActionEvent e) {
            _graph.moveRight();
        }
    };

    ChangeListener _graphChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            _newCleanProject = false;
            updateActionsEnableState();
        }
    };

    Action _minimizeAction = new MyAction("Minimize") {

        public void actionPerformed(ActionEvent e) {
            ProjectFrame.this.setExtendedState(JFrame.ICONIFIED);
        }
    };

    Action _newTaskAction = new MyAction("New Task...", TaskBlocks.getImage("newtask.png"), "Opens the New Task Wizard") {

        public void actionPerformed(ActionEvent e) {
            TaskConfigDialog.openDialog(ProjectFrame.this, null, _taskModel, _graph, true);
        }
    };

    Action _newManAction = new MyAction("New Worker...", TaskBlocks.getImage("newman.png"), "Opens the New Worker Wizard") {

        public void actionPerformed(ActionEvent e) {
            ManConfigDialog.openDialog(ProjectFrame.this, null, _taskModel, _graph, true);
        }
    };

    Action _aboutAction = new MyAction("About...") {

        public void actionPerformed(ActionEvent e) {
            AboutDialog.showAbout(ProjectFrame.this);
        }
    };

    Action _bugzillaSubmit = new MyAction("Export to Bugzilla...", TaskBlocks.getImage("bugzilla.png"), "Opens the Bugzilla Export dialog") {

        public void actionPerformed(ActionEvent e) {
            BugzillaExportDialog.openDialog(ProjectFrame.this, _taskModel._tasks, new ChangeListener() {

                public void stateChanged(ChangeEvent arg0) {
                    _graph.getGraphRepresentation().setDirty();
                }
            });
        }
    };

    Action _deleteSel = new MyAction("Delete Selection", TaskBlocks.getImage("delete.gif"), "Deletes selected objects") {

        public void actionPerformed(ActionEvent e) {
            _graph.deleteSelection();
        }
    };

    ChangeListener _undoRedoChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            updateUndoRedoMenu();
        }
    };

    /**
	 * creates window with empty project.
	 */
    public ProjectFrame() {
        this(TaskModelImpl.createEmptyModel());
        setTitle("New project");
        _newCleanProject = true;
        updateActionsEnableState();
        updateUndoRedoMenu();
    }

    private ProjectFrame(TaskModelImpl model) {
        this.setIconImage(TaskBlocks.getImage("frameicon32.png").getImage());
        _taskModel = model;
        _taskModel.getUndoManager().setChangeListener(_undoRedoChangeListener);
        buildGui();
        pack();
        setSize(800, 400);
        fillMenu();
        _graph.setGraphChangeListener(_graphChangeListener);
        this.addWindowListener(this);
        setLocationRelativeTo(null);
        setVisible(true);
        _numWindows++;
    }

    /**
	 * Opens the specified file. If this frame contains empty project, opens the file in this frame.
	 * If project in this frame is modified, opens new window with it.
	 * 
	 * @param f
	 */
    public void openFile(File f) {
        try {
            openURL(f.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void openURL(URL url) {
        try {
            _graph.getGraphRepresentation().updateModel();
            if (_taskModel._tasks.length == 0) {
                _taskModel = new ProjectSaveLoad().loadProject(url);
                _taskModel.getUndoManager().setChangeListener(_undoRedoChangeListener);
                _graph.setModel(_taskModel);
                setFile(url);
                updateActionsEnableState();
            } else {
                new ProjectFrame().openURL(url);
            }
        } catch (WrongDataException e) {
            JOptionPane.showMessageDialog(null, "<html><b>Couldn't Open File</b><br><br><font size=\"-2\">" + e.getMessage() + "<br><br>");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "<html><b>Couldn't Open File</b><br><br><font size=\"-2\">" + e.getMessage() + "<br><br>");
        }
    }

    public void setTitle(String title) {
        super.setTitle(title + " - Task Blocks");
        _myWindowMenuItem.setText(title);
    }

    public void windowActivated(WindowEvent arg0) {
    }

    public void windowClosed(WindowEvent arg0) {
        _numWindows--;
        _windowMenuItems.remove(_myWindowMenuItem);
        if (_numWindows <= 0) {
            System.exit(0);
        }
    }

    public void windowClosing(WindowEvent arg0) {
        tryClose();
    }

    public void windowDeactivated(WindowEvent arg0) {
    }

    public void windowDeiconified(WindowEvent arg0) {
    }

    public void windowIconified(WindowEvent arg0) {
    }

    public void windowOpened(WindowEvent arg0) {
    }

    private void buildGui() {
        JPanel mainP = new JPanel(new BorderLayout());
        MyToolBar toolB = new MyToolBar();
        _graph = new TaskGraphComponent(_taskModel, new TaskPainterImpl());
        toolB.add(_loadFileAction);
        toolB.add(_saveAction);
        toolB.add(Box.createHorizontalStrut(4));
        toolB.add(new FixedSeparator(FixedSeparator.VERTICAL));
        toolB.add(Box.createHorizontalStrut(4));
        toolB.add(_newTaskAction);
        toolB.add(_newManAction);
        toolB.add(_shrinkAction);
        toolB.add(Box.createHorizontalStrut(4));
        toolB.add(new FixedSeparator(FixedSeparator.VERTICAL));
        toolB.add(Box.createHorizontalStrut(4));
        toolB.add(_scaleUpAction);
        toolB.add(_scaleDownAction);
        toolB.add(Box.createHorizontalStrut(8));
        toolB.add(_leftAction);
        toolB.add(_rightAction);
        toolB.add(_focusTodayAction);
        toolB.add(Box.createHorizontalStrut(4));
        toolB.add(new FixedSeparator(FixedSeparator.VERTICAL));
        toolB.add(Box.createHorizontalStrut(4));
        toolB.add(_deleteSel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainP.add(toolB, BorderLayout.NORTH);
        mainP.add(_graph, BorderLayout.CENTER);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainP);
        toolB.setFloatable(false);
        _graph.setGraphActionListener(this);
    }

    private void fillMenu() {
        JMenuBar menu = getJMenuBar();
        if (menu == null) {
            menu = new JMenuBar();
            this.setJMenuBar(menu);
        }
        JMenu menuFile = new JMenu("File");
        menuFile.add(_newFileAction).setAccelerator(getAcceleratorStroke('N'));
        menuFile.add(_loadFileAction).setAccelerator(getAcceleratorStroke('O'));
        menuFile.add(new JSeparator());
        menuFile.add(_saveAction).setAccelerator(getAcceleratorStroke('S'));
        menuFile.add(_saveAsAction);
        menuFile.add(new JSeparator());
        fillRecentFilesMenu(menuFile);
        menuFile.add(new JSeparator());
        menuFile.add(_closeFileAction).setAccelerator(getAcceleratorStroke('W'));
        menu.add(menuFile);
        JMenu menuEdit = new JMenu("Edit");
        menuEdit.add(_undoAction).setAccelerator(getAcceleratorStroke('Z'));
        menuEdit.add(_redoAction).setAccelerator(getAcceleratorStroke('Y'));
        menu.add(menuEdit);
        JMenu menuProject = new JMenu("Project");
        menuProject.add(_newTaskAction).setAccelerator(getAcceleratorStroke('T'));
        menuProject.add(_newManAction).setAccelerator(getAcceleratorStroke('U'));
        menuProject.add(new JSeparator());
        JMenuItem mi = menuProject.add(_deleteSel);
        if (TaskBlocks.RUNNING_ON_MAC) {
            mi.setAccelerator(getAcceleratorStroke(KeyEvent.VK_BACK_SPACE));
        } else {
            mi.setAccelerator(getAcceleratorStroke(KeyEvent.VK_DELETE));
        }
        menuProject.add(new JSeparator());
        menuProject.add(_shrinkAction).setAccelerator(getAcceleratorStroke('R'));
        menuProject.add(_bugzillaSubmit);
        menu.add(menuProject);
        final JMenu menuWindow = new JMenu("Window");
        _myWindowMenuItem = new JCheckBoxMenuItem("???");
        _myWindowMenuItem.setFont(_myWindowMenuItem.getFont().deriveFont(Font.PLAIN));
        _myWindowMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ProjectFrame.this.toFront();
            }
        });
        _windowMenuItems.add(_myWindowMenuItem);
        menuWindow.addMenuListener(new MenuListener() {

            public void menuCanceled(MenuEvent e) {
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuSelected(MenuEvent e) {
                menuWindow.removeAll();
                JMenuItem minItem = menuWindow.add(new JMenuItem(_minimizeAction));
                minItem.setAccelerator(getAcceleratorStroke('M'));
                minItem.setFont(minItem.getFont().deriveFont(Font.PLAIN));
                menuWindow.add(new JSeparator());
                for (JMenuItem winItem : _windowMenuItems) {
                    menuWindow.add(winItem);
                    winItem.setSelected(winItem == _myWindowMenuItem);
                }
            }
        });
        menu.add(menuWindow);
        if (!TaskBlocks.RUNNING_ON_MAC) {
            JMenu menuHelp = new JMenu("Help");
            menuHelp.add(_aboutAction);
            menu.add(menuHelp);
        }
        for (int i = 0; i < menu.getMenuCount(); i++) {
            JMenu subMenu = menu.getMenu(i);
            subMenu.setFont(subMenu.getFont().deriveFont(Font.PLAIN));
            for (Component c : subMenu.getMenuComponents()) {
                if (c instanceof JMenuItem) {
                    if (TaskBlocks.RUNNING_ON_MAC) {
                        ((JMenuItem) c).setIcon(null);
                    }
                    ((JMenuItem) c).setFont(c.getFont().deriveFont(Font.PLAIN));
                }
            }
        }
    }

    private void fillRecentFilesMenu(JMenu menuFile) {
        try {
            Preferences p = _prefs.node("recentFiles");
            for (String child : p.childrenNames()) {
                String path = p.node(child).get("path", null);
                if (path != null) {
                    menuFile.add(new OpenRecentFileAction(path));
                }
            }
        } catch (BackingStoreException e) {
        }
    }

    private void addToRecentFiles(File f) {
        try {
            Preferences p = _prefs.node("recentFiles");
            String[] childs = p.childrenNames();
            for (String child : childs) {
                String path = p.node(child).get("path", null);
                if (path != null) {
                    if (path.equals(f.getAbsolutePath())) {
                        return;
                    }
                }
            }
            if (childs.length >= 5) {
                p.node(childs[0]).removeNode();
            }
            Preferences newNode = p.node(String.valueOf(System.currentTimeMillis()));
            newNode.put("path", f.getAbsolutePath());
        } catch (BackingStoreException e) {
        }
    }

    private KeyStroke getAcceleratorStroke(char key) {
        return KeyStroke.getKeyStroke(key, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    }

    private KeyStroke getAcceleratorStroke(int keyCode) {
        return KeyStroke.getKeyStroke(keyCode, 0);
    }

    private void setFile(URL url) {
        _file = url;
        setTitle(getShortName(_file));
    }

    private void setFile(File f) {
        try {
            _file = f.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        setTitle(f.getName());
        addToRecentFiles(f);
    }

    private String getShortName(URL url) {
        if (url == null) {
            return null;
        }
        String path = url.getPath();
        if (path == null) {
            return null;
        }
        int idx = path.lastIndexOf("/");
        if (idx >= 0) {
            return path.substring(idx + 1);
        }
        return path;
    }

    private void tryClose() {
        if (_graph.getGraphRepresentation().isSaveDirty()) {
            String SAVE = "Save";
            String DONT_SAVE = "Don't Save";
            String CANCEL = "Cancel";
            Object[] options;
            if (TaskBlocks.RUNNING_ON_MAC) {
                options = new Object[] { SAVE, CANCEL, DONT_SAVE };
            } else {
                options = new Object[] { DONT_SAVE, SAVE, CANCEL };
            }
            JLabel l = new JLabel("<html><b>Do you want to save changes to this document<br>before closing?</b><br><br><font size=\"-2\">If you don't save, your changes will be lost.<br></font><br>");
            l.setFont(l.getFont().deriveFont(Font.PLAIN));
            JOptionPane op = new JOptionPane(l, JOptionPane.QUESTION_MESSAGE, 0, null, options);
            op.createDialog(this, _file == null ? "Unsaved project" : getShortName(_file)).setVisible(true);
            op.setInitialSelectionValue(CANCEL);
            Object choice = op.getValue();
            if (choice == null) {
                return;
            }
            if (choice.equals(SAVE)) {
                if (!save()) {
                    return;
                } else {
                    this.dispose();
                }
            } else if (choice.equals(DONT_SAVE)) {
                this.dispose();
            }
        } else {
            this.dispose();
        }
    }

    private void updateActionsEnableState() {
        boolean unsaved = _newCleanProject || _graph.getGraphRepresentation().isSaveDirty();
        _saveAction.setEnabled(unsaved);
        getRootPane().putClientProperty("windowModified", unsaved ? Boolean.TRUE : Boolean.FALSE);
        _newTaskAction.setEnabled(_graph.getGraphRepresentation().getManCount() > 0);
    }

    private boolean save() {
        _graph.getGraphRepresentation().updateModel();
        try {
            URL f = _file;
            if (f == null) {
                if (TaskBlocks.RUNNING_ON_MAC || TaskBlocks.RUNNING_ON_WINDOWS) {
                    FileDialog fd = new FileDialog(ProjectFrame.this, "Save", FileDialog.SAVE);
                    fd.setVisible(true);
                    if (fd.getFile() != null) {
                        File f2 = new File(fd.getDirectory(), fd.getFile());
                        if (f2 != null) {
                            f = f2.toURI().toURL();
                        }
                    }
                } else {
                    JFileChooser fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fc.showSaveDialog(ProjectFrame.this);
                    File file = fc.getSelectedFile();
                    if (file != null) {
                        f = file.toURI().toURL();
                    }
                }
            }
            if (f != null) {
                new ProjectSaveLoad().saveProject(f, _taskModel);
                setFile(f);
                _graph.getGraphRepresentation().clearSaveDirtyFlag();
                return true;
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null, "<html><b>Couldn't Save</b><br><br><font size=\"-2\">" + e1.getMessage() + "<br><br>");
            e1.printStackTrace();
        }
        return false;
    }

    private void configureTask(TaskImpl t) {
        _graph.getGraphRepresentation().updateModel();
        TaskImpl before = t.clone();
        if (TaskConfigDialog.openDialog(this, t, _taskModel, _graph, false)) {
            _graph.setModel(_taskModel);
            _graph.getGraphRepresentation().setDirty();
            _graph.repaint();
            _taskModel.getUndoManager().addAction(new UndoActionTaskModify(_taskModel, before, t));
        }
    }

    private void configureMan(ManImpl man) {
        _graph.getGraphRepresentation().updateModel();
        ManImpl before = man.clone();
        if (ManConfigDialog.openDialog(this, man, _taskModel, _graph, false)) {
            _graph.setModel(_taskModel);
            _graph.getGraphRepresentation().setDirty();
            _graph.repaint();
            _taskModel.getUndoManager().addAction(new UndoActionManModify(_taskModel, before, man));
        }
    }

    public void graphClicked(MouseEvent e) {
    }

    public void manClicked(Object man, MouseEvent e) {
        if (man != null && e.getClickCount() >= 2) {
            configureMan((ManImpl) man);
        }
    }

    public void taskClicked(Object task, MouseEvent e) {
        if (task != null && e.getClickCount() >= 2) {
            configureTask((TaskImpl) task);
        }
    }

    private void updateUndoRedoMenu() {
        final UndoManager um = _taskModel.getUndoManager();
        if (um.canUndo()) {
            String name = um.getFirstUndoActionLabel();
            if (name.length() > 23) {
                name = name.substring(0, 20) + "...";
            }
            _undoAction.putValue(Action.NAME, "Undo - " + name);
            _undoAction.setEnabled(true);
        } else {
            _undoAction.putValue(Action.NAME, "Undo");
            _undoAction.setEnabled(false);
        }
        if (um.canRedo()) {
            String name = um.getFirstRedoActionLabel();
            if (name.length() > 23) {
                name = name.substring(0, 20) + "...";
            }
            _redoAction.putValue(Action.NAME, "Redo - " + name);
            _redoAction.setEnabled(true);
        } else {
            _redoAction.putValue(Action.NAME, "Redo");
            _redoAction.setEnabled(false);
        }
    }
}
