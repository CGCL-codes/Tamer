package vavi.apps.treeView;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeModel;
import vavi.swing.event.EditorEvent;
import vavi.swing.event.EditorListener;
import vavi.swing.event.EditorSupport;
import vavi.util.Debug;
import vavi.util.RegexFileFilter;

/**
 * �c���[�r���[�ł��D
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.01 020322 nsano change closing procedure order <br>
 */
public class TreeView {

    /** ���\�[�X�o���h�� */
    private static final ResourceBundle rb = ResourceBundle.getBundle("vavi.apps.treeView.TreeViewResource", Locale.getDefault());

    /** Tree �� UI */
    private TreeViewTree tree;

    /** ���[�g�̃c���[�m�[�h */
    private TreeViewTreeNode root;

    /** �X�e�[�^�X�o�[ */
    private JLabel statusBar = new JLabel(rb.getString("statusBar.welcome"));

    /** The popup menu */
    private JPopupMenu popupMenu;

    private TreeViewTreeEditor editor;

    /**
     * TreeView ���쐬���܂��D
     */
    public TreeView() {
        popupMenu = createPopupMenu();
        setActionStates(null);
        init();
        tree = new TreeViewTree(root);
        tree.addEditorListener(el);
        editor = new TreeViewTreeEditor(tree);
        editor.addEditorListener(el);
    }

    /**
     * Tree �� UI ��Ԃ��܂��D
     */
    public JTree getUI() {
        return tree;
    }

    /**
     * ���j���[�o�[���擾���܂��D
     */
    public JMenuBar getMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem menuItem;
        JMenu menu = new JMenu(rb.getString("menu.file"));
        menu.setMnemonic(KeyEvent.VK_F);
        menu.addSeparator();
        menuItem = menu.add(saveAction);
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuBar.add(menu);
        menu.addSeparator();
        menuItem = menu.add(exitAction);
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuBar.add(menu);
        menu = new JMenu(rb.getString("menu.edit"));
        menu.setMnemonic(KeyEvent.VK_E);
        menuItem = menu.add(cutAction);
        menuItem = menu.add(copyAction);
        menuItem = menu.add(pasteAction);
        menu.addSeparator();
        menuItem = menu.add(deleteAction);
        menuBar.add(menu);
        objectMenu = new JMenu(rb.getString("menu.object"));
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuBar.add(objectMenu);
        windowMenu = new JMenu(rb.getString("menu.window"));
        windowMenu.setMnemonic(KeyEvent.VK_W);
        menuBar.add(windowMenu);
        menu = new JMenu(rb.getString("menu.help"));
        menu.setMnemonic(KeyEvent.VK_H);
        menuItem = menu.add(showManualAction);
        menu.addSeparator();
        menuItem = menu.add(showVersionAction);
        menuBar.add(menu);
        return menuBar;
    }

    /**
     * �c�[���o�[���擾���܂��D
     */
    public JToolBar getToolBar() {
        JToolBar toolBar = new JToolBar();
        JButton button;
        button = toolBar.add(cutAction);
        button.setToolTipText(button.getText());
        button = toolBar.add(copyAction);
        button.setToolTipText(button.getText());
        button = toolBar.add(pasteAction);
        button.setToolTipText(button.getText());
        toolBar.addSeparator();
        button = toolBar.add(deleteAction);
        button.setToolTipText(button.getText());
        ToolTipManager.sharedInstance().registerComponent(toolBar);
        return toolBar;
    }

    /** �X�e�[�^�X�o�[���擾���܂��D */
    public JLabel getStatusBar() {
        return statusBar;
    }

    /** ""���j���[ */
    private JMenu objectMenu;

    /** "�E�C���h�E"���j���[ */
    private JMenu windowMenu;

    /**
     * "�E�B���h�E"���j���[���擾���܂��D
     */
    public JMenu getWindowMenu() {
        return windowMenu;
    }

    /**
     * �|�b�v�A�b�v���j���[�̐ݒ�����܂��D
     */
    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(openAction);
        popupMenu.addSeparator();
        popupMenu.add(copyAction);
        popupMenu.add(cutAction);
        popupMenu.add(pasteAction);
        popupMenu.add(deleteAction);
        popupMenu.addSeparator();
        return popupMenu;
    }

    /**
     * �����Ԃɂ��܂��D
     */
    private void init() {
        try {
            InputStream is = getClass().getResourceAsStream(props.getProperty("tv.resource.tree"));
            root = new DomXMLLoader(is).readRootTreeNode();
        } catch (Exception e) {
            Debug.println(props.getProperty("tv.resource.tree") + " cannot read: tv.resource.tree");
            Debug.printStackTrace(e);
        }
    }

    /**
     * �c���[�m�[�h�����[�h���܂��D
     */
    private void load(InputStream is) throws IOException {
        root = new DomXMLLoader(is).readRootTreeNode();
        ((DefaultTreeModel) tree.getModel()).setRoot(root);
    }

    /**
     * �c���[�m�[�h���Z�[�u���܂��D
     */
    private void save(OutputStream os) throws IOException {
        new DomXMLSaver(os).writeRootTreeNode(root);
    }

    /**
     * �I�����܂��D
     */
    private void exit() {
        System.exit(0);
    }

    private boolean isPastable = false;

    /**
     * �I������Ă���m�[�h�ɉ����ă��j���[�\���𐧌䂵�܂��D
     * 
     * @param node �I�����ꂽ�m�[�h
     */
    private void setActionStates(TreeViewTreeNode node) {
        if (node != null) {
            cutAction.setEnabled(node.canCut());
            copyAction.setEnabled(node.canCopy());
            pasteAction.setEnabled(node.canCopy() && isPastable);
            deleteAction.setEnabled(node.canDelete());
        } else {
            cutAction.setEnabled(false);
            copyAction.setEnabled(false);
            pasteAction.setEnabled(false);
            deleteAction.setEnabled(false);
        }
    }

    /** �m�[�h���J���A�N�V���� */
    private Action openAction = new AbstractAction(rb.getString("action.open"), (ImageIcon) UIManager.get("treeView.openIcon")) {

        public void actionPerformed(ActionEvent ev) {
            TreeViewTreeNode node = tree.getTreeNode();
            if (node == null) return;
            open(node);
        }
    };

    /**
     * �I�[�v���̏������s���܂��D
     * 
     * @param node
     */
    private void open(TreeViewTreeNode node) {
        try {
            statusBar.setText(rb.getString("action.open.start"));
            node.open();
            statusBar.setText(rb.getString("action.open.end"));
        } catch (Exception e) {
            showError(e);
        }
    }

    /** �I�u�W�F�N�g���J�b�g����A�N�V���� */
    private Action cutAction = new AbstractAction(rb.getString("action.cut"), (ImageIcon) UIManager.get("treeView.cutIcon")) {

        public void actionPerformed(ActionEvent ev) {
            try {
                statusBar.setText(rb.getString("action.cut.start"));
                editor.cut();
                statusBar.setText(rb.getString("action.cut.end"));
            } catch (Exception e) {
                showError(e);
            }
        }
    };

    /** �I�u�W�F�N�g���R�s�[����A�N�V���� */
    private Action copyAction = new AbstractAction(rb.getString("action.copy"), (ImageIcon) UIManager.get("treeView.copyIcon")) {

        public void actionPerformed(ActionEvent ev) {
            try {
                statusBar.setText(rb.getString("action.copy.start"));
                editor.copy();
                statusBar.setText(rb.getString("action.copy.end"));
            } catch (Exception e) {
                showError(e);
            }
        }
    };

    /** �I�u�W�F�N�g���y�[�X�g����A�N�V���� */
    private Action pasteAction = new AbstractAction(rb.getString("action.paste"), (ImageIcon) UIManager.get("treeView.pasteIcon")) {

        public void actionPerformed(ActionEvent ev) {
            try {
                statusBar.setText(rb.getString("action.paste.start"));
                editor.paste();
                statusBar.setText(rb.getString("action.paste.end"));
            } catch (Exception e) {
                showError(e);
            }
        }
    };

    /** �I�u�W�F�N�g���폜����A�N�V���� */
    private Action deleteAction = new AbstractAction(rb.getString("action.delete"), (ImageIcon) UIManager.get("treeView.deleteIcon")) {

        public void actionPerformed(ActionEvent ev) {
            try {
                statusBar.setText(rb.getString("action.delete.start"));
                if (JOptionPane.showConfirmDialog(null, rb.getString("action.delete.dialog"), rb.getString("dialog.title.confirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    editor.delete();
                }
                statusBar.setText(rb.getString("action.delete.end"));
            } catch (Exception e) {
                showError(e);
            }
        }
    };

    /** �c���[�r���[���I������A�N�V���� */
    private Action exitAction = new AbstractAction(rb.getString("action.exit")) {

        public void actionPerformed(ActionEvent ev) {
            exit();
        }
    };

    /** */
    private static final RegexFileFilter fileFilter = new RegexFileFilter(".+\\.xml", "XML File");

    /** ����c���[�̃Z�[�u���s���A�N�V���� */
    private Action saveAction = new AbstractAction(rb.getString("action.save")) {

        private JFileChooser fc = new JFileChooser();

        {
            File cwd = new File(System.getProperty("user.home"));
            fc.setCurrentDirectory(cwd);
            fc.setFileFilter(fileFilter);
        }

        public void actionPerformed(ActionEvent ev) {
            try {
                if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File file = fc.getSelectedFile();
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                save(os);
            } catch (Exception e) {
                showError(e);
            }
        }
    };

    /** �o�[�W�����\�����s���A�N�V���� */
    private Action showVersionAction = new AbstractAction(rb.getString("action.showVersion")) {

        public void actionPerformed(ActionEvent ev) {
            String version = rb.getString("version.title") + "\n" + rb.getString("version.copyright") + "\n" + rb.getString("version.revision") + "\n" + rb.getString("version.build");
            JOptionPane.showMessageDialog(null, version, rb.getString("dialog.title.showVersion"), JOptionPane.INFORMATION_MESSAGE);
        }
    };

    /** �g�p�����̕\�����s���A�N�V���� */
    private Action showManualAction = new AbstractAction(rb.getString("action.showManual")) {

        public void actionPerformed(ActionEvent ev) {
            try {
                Runtime.getRuntime().exec(props.getProperty("tv.path.browser") + " " + props.getProperty("tv.url.manual"));
            } catch (Exception e) {
                showError(e);
            }
        }
    };

    /** �G���[���b�Z�[�W�̃_�C�A���O��\�����܂��D */
    private void showError(Exception e) {
        statusBar.setText(e.getMessage());
        JOptionPane.showMessageDialog(null, e.getMessage(), rb.getString("dialog.title.error"), JOptionPane.ERROR_MESSAGE);
    }

    /** */
    private EditorListener el = new EditorListener() {

        @SuppressWarnings("unchecked")
        public void editorUpdated(EditorEvent ev) {
            String name = ev.getName();
            if ("select".equals(name)) {
                select((List<TreeViewTreeNode>) ev.getArgument());
            } else if ("expand".equals(name)) {
                expand((TreeViewTreeNode) ev.getArgument());
            } else if ("popupMenu".equals(name)) {
                Object[] args = (Object[]) ev.getArgument();
                showPopupMenu((TreeViewTreeNode) args[0], (Point) args[1]);
            } else if ("rename".equals(name)) {
                Object[] args = (Object[]) ev.getArgument();
                rename((TreeViewTreeNode) args[0], (String) args[1]);
            } else if ("cut".equals(name)) {
                isPastable = true;
            } else if ("copy".equals(name)) {
                isPastable = true;
            } else if ("lostOwnership".equals(name)) {
                isPastable = false;
            }
        }
    };

    /** */
    private void select(List<TreeViewTreeNode> selection) {
        if (selection.size() == 1) {
            TreeViewTreeNode node = selection.get(0);
            statusBar.setText(node.getUserObject().toString());
            setActionStates(node);
            node.setActionStates();
        } else {
            setActionStates(null);
        }
    }

    /** */
    private void expand(TreeViewTreeNode node) {
        if (node.canOpen()) {
            open(node);
        }
    }

    /** */
    private void showPopupMenu(TreeViewTreeNode node, Point point) {
        setActionStates(node);
        popupMenu.show(tree, point.x, point.y);
    }

    /** */
    private void rename(TreeViewTreeNode node, String newValue) {
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        try {
            if (!node.canRename()) return;
            node.rename(newValue);
            treeModel.nodeChanged(node);
        } catch (Exception e) {
            showError(e);
        }
    }

    /** EditorEvent �@�\�̃��[�e�B���e�B */
    private EditorSupport editorSupport = new EditorSupport();

    /** Editor ���X�i�[��ǉ����܂��D */
    public void addEditorListener(EditorListener l) {
        editorSupport.addEditorListener(l);
    }

    /** Editor ���X�i�[���폜���܂��D */
    public void removeEditorListener(EditorListener l) {
        editorSupport.removeEditorListener(l);
    }

    /** EditorEvent �𔭍s���܂��D */
    protected void fireEditorUpdated(EditorEvent ev) {
        editorSupport.fireEditorUpdated(ev);
    }

    /** �v���p�e�B */
    static Properties props = new Properties();

    /**
     * �����܂��D
     */
    static {
        final String path = "TreeView.properties";
        final Class<?> clazz = TreeView.class;
        try {
            InputStream is = clazz.getResourceAsStream(path);
            props.load(is);
            is.close();
            Toolkit t = Toolkit.getDefaultToolkit();
            UIDefaults table = UIManager.getDefaults();
            int i = 0;
            while (true) {
                String key = "tv.action." + i + ".iconName";
                String name = props.getProperty(key);
                if (name == null) {
                    Debug.println("no property for: tv.action." + i + ".iconName");
                    break;
                }
                key = "tv.action." + i + ".icon";
                String icon = props.getProperty(key);
                table.put(name, new ImageIcon(t.getImage(clazz.getResource(icon))));
                i++;
            }
        } catch (Exception e) {
            Debug.println(Level.SEVERE, rb.getString("property.file"));
            System.exit(1);
        }
    }

    /**
     * �v���O�����G���g���ł��D
     */
    public static void main(String[] args) throws Exception {
        TreeView tree = new TreeView();
        JFrame frame = new TreeViewFrame(tree);
        frame.setVisible(true);
    }
}
