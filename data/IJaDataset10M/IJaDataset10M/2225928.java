package net.community.chest.javaagent.dumper.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.AbstractButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import net.community.apps.common.BaseMainFrame;
import net.community.chest.awt.attributes.AttrUtils;
import net.community.chest.io.FileUtil;
import net.community.chest.javaagent.dumper.ui.data.SelectiblePackageInfo;
import net.community.chest.javaagent.dumper.ui.resources.ResourcesAnchor;
import net.community.chest.javaagent.dumper.ui.tree.AbstractInfoNode;
import net.community.chest.javaagent.dumper.ui.tree.NodeCellRenderer;
import net.community.chest.javaagent.dumper.ui.tree.NodeExpansionHandler;
import net.community.chest.javaagent.dumper.ui.tree.PackageNode;
import net.community.chest.lang.ExceptionUtil;
import net.community.chest.resources.SystemPropertiesResolver;
import net.community.chest.swing.component.menu.MenuExplorer;
import net.community.chest.swing.component.menu.MenuItemExplorer;
import net.community.chest.swing.component.menu.MenuUtil;
import net.community.chest.swing.component.tree.BaseTree;
import net.community.chest.swing.component.tree.DefaultTreeScroll;
import net.community.chest.ui.components.text.FileAutoCompleter;
import net.community.chest.ui.components.text.FolderAutoCompleter;
import net.community.chest.ui.helpers.panel.input.LRFieldWithButtonPanel;
import net.community.chest.util.logging.LoggerWrapper;
import net.community.chest.util.logging.factory.WrapperFactoryManager;
import org.w3c.dom.Element;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Aug 14, 2011 11:04:39 AM
 */
class JavaAgentDumperMainFrame extends BaseMainFrame<ResourcesAnchor> implements SelectiblePackageInfoHandler, AspectGeneratorHandler {

    private static final LoggerWrapper _logger = WrapperFactoryManager.getLogger(JavaAgentDumperMainFrame.class);

    JavaAgentDumperMainFrame(String... args) throws Exception {
        super(true, args);
        processMainArguments(args);
    }

    @Override
    public ResourcesAnchor getResourcesAnchor() {
        return ResourcesAnchor.getInstance();
    }

    @Override
    protected LoggerWrapper getLogger() {
        return _logger;
    }

    public static final String RUN_CMD = "run", STOP_CMD = "stop";

    @Override
    protected Map<String, ? extends ActionListener> getActionListenersMap(boolean createIfNotExist) {
        final Map<String, ? extends ActionListener> org = super.getActionListenersMap(createIfNotExist);
        if (((org != null) && (org.size() > 0)) || (!createIfNotExist)) return org;
        final Map<String, ActionListener> lm = new TreeMap<String, ActionListener>(String.CASE_INSENSITIVE_ORDER);
        lm.put(EXIT_CMD, getExitActionListener());
        lm.put(ABOUT_CMD, getShowManifestActionListener());
        lm.put(LOAD_CMD, getLoadFileListener());
        lm.put(SAVE_CMD, getSaveFileListener());
        lm.put(RUN_CMD, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                generateAspects();
            }
        });
        lm.put(STOP_CMD, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stopAspectsGeneration();
            }
        });
        lm.put("select-all", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectAllPackages();
            }
        });
        lm.put("mark-selected", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedNodesMarking(true);
            }
        });
        lm.put("unmark-selected", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedNodesMarking(false);
            }
        });
        setActionListenersMap(lm);
        return lm;
    }

    private JMenuItem _saveMenuItem, _loadMenuItem, _runMenuItem, _stopMenuItem;

    @Override
    protected Map<String, JMenuItem> setMainMenuItemsActionHandlers(MenuItemExplorer ie) {
        final Map<String, JMenuItem> im = super.setMainMenuItemsActionHandlers(ie);
        _loadMenuItem = (null == im) ? null : im.get(LOAD_CMD);
        _saveMenuItem = (null == im) ? null : im.get(SAVE_CMD);
        _runMenuItem = (null == im) ? null : im.get(RUN_CMD);
        _stopMenuItem = (null == im) ? null : im.get(STOP_CMD);
        return im;
    }

    private JPopupMenu _contextMenu;

    @Override
    protected void setMainMenuActionHandlers(final MenuItemExplorer ie, final MenuExplorer me) {
        super.setMainMenuActionHandlers(ie, me);
        _contextMenu = MenuUtil.createPopupFromMenu(me.findMenuByCommand("edit"));
    }

    private BaseTree _pkgsTree;

    void selectAllPackages() {
        final TreeNode rootNode = (_pkgsTree == null) ? null : _pkgsTree.getRoot();
        final int numChildren = (rootNode == null) ? 0 : rootNode.getChildCount();
        final TreePath[] paths = (numChildren <= 0) ? null : new TreePath[numChildren];
        for (int cIndex = 0; cIndex < numChildren; cIndex++) {
            final PackageNode node = (PackageNode) rootNode.getChildAt(cIndex);
            paths[cIndex] = new TreePath(node.getPath());
        }
        if ((paths != null) && (paths.length > 0)) _pkgsTree.setSelectionPaths(paths);
    }

    void updateSelectedNodesMarking(final boolean markIt) {
        final TreePath[] paths = (_pkgsTree == null) ? null : _pkgsTree.getSelectionPaths();
        if ((paths == null) || (paths.length <= 0)) return;
        final DefaultTreeModel model = (DefaultTreeModel) _pkgsTree.getModel();
        for (final TreePath selPath : paths) {
            final AbstractInfoNode<?> selNode = (AbstractInfoNode<?>) ((selPath == null) ? null : selPath.getLastPathComponent());
            markNode(model, selNode, markIt);
            if (!markIt) deselectTillRoot(model, selNode);
        }
    }

    private void deselectTillRoot(final DefaultTreeModel model, final AbstractInfoNode<?> selNode) {
        final TreeNode parent = (selNode == null) ? null : selNode.getParent();
        if ((parent == null) || (!(parent instanceof AbstractInfoNode<?>))) return;
        final AbstractInfoNode<?> node = (AbstractInfoNode<?>) parent;
        markNode(model, node, false);
        deselectTillRoot(model, node);
    }

    private boolean markNode(final DefaultTreeModel model, final AbstractInfoNode<?> selNode, final boolean markIt) {
        if (selNode.isSelected() == markIt) return false;
        selNode.setSelected(markIt);
        model.nodeStructureChanged(selNode);
        return true;
    }

    @Override
    public void layoutSection(String name, Element elem) throws RuntimeException {
        try {
            if ("packages-tree".equalsIgnoreCase(name)) {
                if (_pkgsTree != null) throw new IllegalStateException("Element=" + name + " re-specified");
                _pkgsTree = new BaseTree((TreeModel) null).fromXml(elem);
                _pkgsTree.setRootVisible(false);
                _pkgsTree.setShowsRootHandles(true);
                final NodeExpansionHandler expHandler = new NodeExpansionHandler(_pkgsTree);
                _pkgsTree.addMouseListener(expHandler);
                _pkgsTree.addTreeWillExpandListener(expHandler);
                _pkgsTree.addTreeExpansionListener(expHandler);
                _pkgsTree.setCellRenderer(new NodeCellRenderer(getResourcesAnchor()));
            } else if ("aspect-target".equalsIgnoreCase(name)) {
                if (_targetAutoComplete != null) throw new IllegalStateException("Element=" + name + " re-specified");
                _targetPanel = new LRFieldWithButtonPanel(elem);
                _targetPanel.addActionListener(getSaveFileListener());
                _targetAutoComplete = new FolderAutoCompleter<JTextComponent>(_targetPanel.getTextField());
            } else super.layoutSection(name, elem);
        } catch (Exception e) {
            throw ExceptionUtil.toRuntimeException(e, true);
        }
    }

    @Override
    public void layoutComponent() throws RuntimeException {
        super.layoutComponent();
        final JPanel northPanel = new JPanel(new GridLayout(0, 1));
        {
            final JToolBar b = getMainToolBar();
            final Map<String, ? extends AbstractButton> hm = setToolBarHandlers(b);
            if ((hm != null) && (hm.size() > 0)) {
                _loadBtn = hm.get(LOAD_CMD);
                _saveBtn = hm.get(SAVE_CMD);
                _runBtn = hm.get(RUN_CMD);
                _stopBtn = hm.get(STOP_CMD);
            }
            if (b != null) northPanel.add(b);
            if (_targetPanel != null) northPanel.add(_targetPanel);
        }
        final Container ctPane = getContentPane();
        ctPane.add(northPanel, BorderLayout.NORTH);
        if (_pkgsTree != null) {
            if (_contextMenu != null) _pkgsTree.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) doPop(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) doPop(e);
                }

                @SuppressWarnings("synthetic-access")
                private void doPop(MouseEvent e) {
                    _contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            });
            ctPane.add(new DefaultTreeScroll(_pkgsTree), BorderLayout.CENTER);
        }
    }

    private AbstractButton _loadBtn, _saveBtn, _runBtn, _stopBtn;

    protected void updateActionsState(final boolean running) {
        AttrUtils.setComponentEnabledState(!running, _loadMenuItem, _loadBtn, _saveMenuItem, _saveBtn, _runMenuItem, _runBtn);
        AttrUtils.setComponentEnabledState(running, _stopMenuItem, _stopBtn);
    }

    private AspectGeneratorThread _generator;

    @Override
    public void handleProcessedNode(AbstractInfoNode<?> node) {
        if (node == null) return;
    }

    @Override
    public void doneGenerating(final AspectGeneratorThread instance) {
        if (instance == null) return;
        if (instance == _generator) {
            _generator = null;
            updateActionsState(false);
        }
    }

    protected void generateAspects() {
        final String outputPath = getTargetLocation();
        final File outputFolder = ((outputPath == null) || (outputPath.length() <= 0)) ? null : new File(outputPath);
        if (outputFolder == null) return;
        if (_generator != null) return;
        final TreeModel model = (_pkgsTree == null) ? null : _pkgsTree.getModel();
        final TreeNode root = (model == null) ? null : (TreeNode) model.getRoot();
        if (root == null) return;
        if (outputFolder.exists()) {
            final int nRes = JOptionPane.showConfirmDialog(this, "Target folder exists and will be emptied - continue ?", "Confirm target folder removal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (nRes != JOptionPane.YES_OPTION) return;
            final Collection<? extends Map.Entry<File, Boolean>> delResult = FileUtil.deleteAll(outputFolder, false);
            if ((delResult != null) && (delResult.size() > 0)) {
                for (final Map.Entry<File, Boolean> delEntry : delResult) {
                    final File f = delEntry.getKey();
                    final Boolean v = delEntry.getValue();
                    if ((v == null) || v.booleanValue()) continue;
                    JOptionPane.showMessageDialog(this, "Failed to delete " + f.getAbsolutePath(), "Some files could not be deleted", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        _generator = new AspectGeneratorThread(root, outputFolder, this);
        updateActionsState(true);
        _generator.execute();
    }

    protected void stopAspectsGeneration() {
        if (_generator != null) _generator.cancel(true);
    }

    private DumperDataLoaderThread _loader;

    @Override
    public void loadFile(File f, String cmd, Element dlgElement) {
        if (_loader != null) return;
        final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root node, should be invisible");
        final DefaultTreeModel defaultTreeModel = new DefaultTreeModel(rootNode);
        _pkgsTree.setModel(defaultTreeModel);
        _loader = new DumperDataLoaderThread(this, f);
        updateActionsState(true);
        _loader.execute();
    }

    @Override
    public void doneLoadingDumperData(DumperDataLoaderThread loader) {
        if (loader == null) return;
        if (_loader == loader) {
            _pkgsTree.expandRow(0);
            _loader = null;
            updateActionsState(false);
        }
    }

    @Override
    public void processSelectiblePackageInfo(SelectiblePackageInfo pkgInfo) {
        if (pkgInfo == null) return;
        final DefaultTreeModel defaultTreeModel = (DefaultTreeModel) _pkgsTree.getModel();
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
        rootNode.add(new PackageNode(pkgInfo));
        defaultTreeModel.nodeStructureChanged(rootNode);
    }

    private LRFieldWithButtonPanel _targetPanel;

    private FileAutoCompleter<JTextComponent> _targetAutoComplete;

    public String getTargetLocation() {
        return (null == _targetAutoComplete) ? null : _targetAutoComplete.getText();
    }

    @Override
    public void saveFile(File f, Element dlgElement) {
        final String path = (f == null) ? null : f.getAbsolutePath();
        if ((path == null) || (path.length() <= 0)) return;
        if (f.exists()) {
            if (!f.isDirectory()) {
                JOptionPane.showMessageDialog(this, "Specified target is not a folder", "Bad target folder", JOptionPane.WARNING_MESSAGE);
                return;
            }
            setInitialFileChooserFolder(f, Boolean.TRUE);
        }
        _targetPanel.setText(path);
    }

    private void processMainArguments(final String... args) {
        final int numArgs = (args == null) ? 0 : args.length;
        for (int aIndex = 0; aIndex < numArgs; aIndex++) {
            final String optName = args[aIndex];
            if (optName.startsWith("--load")) loadFile(new File(extractOptionValue(optName)), LOAD_CMD, null); else if (optName.startsWith("--save")) saveFile(new File(extractOptionValue(optName)), null);
        }
    }

    private static String extractOptionValue(final String opt) {
        return SystemPropertiesResolver.SYSTEM.format(opt.substring(opt.indexOf('=') + 1));
    }
}
