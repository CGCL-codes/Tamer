package org.phylowidget.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.andrewberman.ui.EventManager;
import org.andrewberman.ui.FocusManager;
import org.andrewberman.ui.ShortcutManager;
import org.andrewberman.ui.menu.MenuItem;
import org.andrewberman.ui.menu.ToolDock;
import org.andrewberman.ui.menu.Toolbar;
import org.andrewberman.ui.unsorted.FileUtils;
import org.andrewberman.ui.unsorted.MethodAndFieldSetter;
import org.phylowidget.PWContext;
import org.phylowidget.PWPlatform;
import org.phylowidget.PhyloTree;
import org.phylowidget.PhyloWidget;
import org.phylowidget.net.PhyloTransformServices;
import org.phylowidget.net.SecurityChecker;
import org.phylowidget.render.BasicTreeRenderer;
import org.phylowidget.render.LayoutCladogram;
import org.phylowidget.render.NodeRange;
import org.phylowidget.tree.CachedRootedTree;
import org.phylowidget.tree.PhyloNode;
import org.phylowidget.tree.RootedTree;
import org.phylowidget.tree.TreeIO;
import processing.core.PApplet;

public class PhyloUI implements Runnable {

    PhyloWidget p;

    PWContext context;

    public FocusManager focus;

    public EventManager event;

    public ShortcutManager keys;

    public TreeClipboard clipboard;

    public PhyloTextField text;

    public PhyloContextMenu contextMenu;

    public Toolbar toolbar;

    public SearchBox search;

    public PhyloUI(PhyloWidget p) {
        this.p = p;
        context = PWPlatform.getInstance().getThisAppContext();
    }

    public Thread thread;

    public void setup() {
        focus = context.focus();
        event = context.event();
        keys = context.shortcuts();
        text = new PhyloTextField(p);
        clipboard = new TreeClipboard(p);
        thread = context.createThread(this);
        thread.start();
    }

    public ArrayList<MenuItem> menus;

    public void run() {
        try {
            p.callMethod("setMenusIfNull");
            loadFromAppletParams(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkPermissions();
        layout();
    }

    public void setMenusIfNull() {
        if (menus == null) setMenus();
    }

    boolean runningInBrowser() {
        String appViewer = p.getAppletContext().getClass().getCanonicalName();
        if (appViewer.toLowerCase().contains("appletviewer")) return false; else return true;
    }

    void disposeMenus() {
        if (menus != null) {
            for (MenuItem i : menus) {
                i.dispose();
            }
        }
    }

    public synchronized void setMenus() {
        disposeMenus();
        String[] menuFiles = context.config().menus.split(";");
        ArrayList<MenuItem> allMenus = new ArrayList<MenuItem>();
        for (String menuFile : menuFiles) {
            if (menuFile.trim().length() == 0) continue;
            if (context.config().debug) System.out.println("PhyloUI setMenus(): " + menuFile);
            menuFile.replaceAll("'", "");
            menuFile.replaceAll("\"", "");
            PhyloMenuIO io = new PhyloMenuIO();
            Reader r = null;
            InputStream in = null;
            Exception asdf = null;
            if (menuFile.toLowerCase().startsWith("http")) {
                try {
                    URL url = new URL(menuFile);
                    in = url.openStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    in = p.createInput("menus/" + menuFile);
                    if (in == null) {
                        in = p.createInput(menuFile);
                    }
                    if (in == null) {
                        String path = p.getDocumentBase().toString();
                        int ind = path.lastIndexOf("/");
                        if (ind != -1) path = path.substring(0, ind);
                        in = p.createInput(path + "/" + menuFile);
                    }
                } catch (Exception e) {
                    asdf = e;
                }
            }
            if (in == null) {
                r = new StringReader(menuFile);
            } else {
                r = new InputStreamReader(in);
            }
            if (in == null) {
                asdf.printStackTrace();
            }
            ArrayList<MenuItem> theseMenus = io.loadFromXML(r, p, context.ui(), p, context.config());
            configureMenus(theseMenus);
            allMenus.addAll(theseMenus);
        }
        this.menus = allMenus;
        if (context.config().debug) System.out.println("Finished!");
    }

    String streamToString(InputStream in) {
        int c = 0;
        InputStreamReader r = new InputStreamReader(in);
        StringBuffer sb = new StringBuffer();
        try {
            while ((c = r.read()) != -1) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public void loadFromAppletParams(PApplet app) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        Field[] fields = PhyloConfig.class.getDeclaredFields();
        for (Field f : fields) {
            try {
                String param = app.getParameter(f.getName());
                if (param != null) {
                    p.changeSetting(f.getName(), param);
                }
            } catch (Exception e) {
            }
        }
    }

    protected synchronized void configureMenus(ArrayList menus) {
        for (int i = 0; i < menus.size(); i++) {
            MenuItem menu = (MenuItem) menus.get(i);
            if (menu instanceof PhyloContextMenu) {
                this.contextMenu = (PhyloContextMenu) menu;
                continue;
            } else if (menu.getClass() == Toolbar.class) {
                toolbar = (Toolbar) menu;
                MenuItem item = toolbar.get("Search:");
                if (item != null) {
                    search = (SearchBox) item;
                    search.setText(context.config().search);
                }
            } else if (menu.getClass() == ToolDock.class) {
                ToolDock td = (ToolDock) menu;
            }
        }
    }

    void checkPermissions() {
        SecurityChecker sc = new SecurityChecker(context.getPW());
        canWriteFiles = sc.canWriteFiles();
        canReadFiles = sc.canReadFiles();
        canAccessInternet = sc.canAccessInternet();
    }

    /**
	 * Some utility methods and fields for UI items dependent on security
	 * permissions.
	 */
    boolean canWriteFiles;

    boolean canReadFiles;

    boolean canAccessInternet;

    public boolean canWriteFiles() {
        return canWriteFiles;
    }

    public boolean canReadFiles() {
        return canReadFiles;
    }

    public boolean canAccessInternet() {
        return canAccessInternet;
    }

    public RootedTree getCurTree() {
        return context.trees().getTree();
    }

    public PhyloTree getTree() {
        return (PhyloTree) getCurTree();
    }

    public void layout() {
        if (context.trees().getRenderer() != null) context.trees().getRenderer().layoutTrigger();
        context.trees().fireCallback();
    }

    public void forceLayout() {
        if (context.trees().getRenderer() != null) {
            BasicTreeRenderer render = (BasicTreeRenderer) context.trees().getRenderer();
            render.forceLayout();
        }
        context.trees().fireCallback();
    }

    public PhyloNode getCurNode() {
        if (curRange() == null) return null; else return curRange().node;
    }

    public void search() {
        if (search != null) {
            search.setText(context.config().search);
        } else {
            PhyloTree t = (PhyloTree) getCurTree();
            if (t != null) t.searchAndMarkFound(context.config().search);
        }
    }

    public NodeRange curRange() {
        if (contextMenu == null) return null;
        return contextMenu.curNodeRange;
    }

    public void nodeEditBranchLength() {
        text.startEditing(curRange(), PhyloTextField.BRANCH_LENGTH);
    }

    public void nodeEditName() {
        text.startEditing(curRange(), PhyloTextField.LABEL);
    }

    FontChooserDialog fontChooser;

    public void changeFont() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (fontChooser == null) fontChooser = new FontChooserDialog(getFrame(), context);
                fontChooser.setVisible(true);
            }
        });
    }

    AnnotationEditorDialog annotation;

    public void nodeEditAnnotation() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (annotation == null) annotation = new AnnotationEditorDialog(getFrame(), p);
                annotation.setNode(getCurNode());
                annotation.setVisible(true);
            }
        });
    }

    public void nodeReroot() {
        NodeRange r = curRange();
        synchronized (r.render.getTree()) {
            r.render.getTree().reroot(getCurNode());
        }
    }

    public void reroot() {
        nodeReroot();
    }

    public void nodeSwitchChildren() {
        NodeRange r = curRange();
        r.render.getTree().flipChildren(getCurNode());
        r.render.layoutTrigger();
    }

    public void nodeFlipSubtree() {
        NodeRange r = curRange();
        r.render.getTree().reverseSubtree(getCurNode());
        getCurTree().modPlus();
        r.render.layoutTrigger();
    }

    public void nodeAddSister() {
        NodeRange r = curRange();
        RootedTree tree = r.render.getTree();
        PhyloNode sis = (PhyloNode) tree.createAndAddVertex();
        tree.addSisterNode(getCurNode(), sis);
    }

    public void nodeAddChild() {
        NodeRange r = curRange();
        RootedTree tree = r.render.getTree();
        tree.addChildNode(getCurNode());
    }

    public void nodeCut() {
        NodeRange r = curRange();
        clipboard.cut(r.render.getTree(), r.node);
    }

    public void nodeCopy() {
        NodeRange r = curRange();
        clipboard.copy(r.render.getTree(), r.node);
    }

    public void selectNode(String s) {
        PhyloTree tree = (PhyloTree) getCurTree();
        List<PhyloNode> nodes = tree.search(s);
        if (nodes.size() == 0) {
            System.err.println("Node " + s + " not found!");
        }
        contextMenu.curNodeRange = nodes.get(0).range;
    }

    void setMessage(String s) {
        context.getPW().setMessage(s);
    }

    public void nodeSwap() {
        final NodeRange r = curRange();
        setMessage("Swapping clipboard...");
        new Thread() {

            public void run() {
                try {
                    synchronized (r.render.getTree()) {
                        clipboard.swap(r.render.getTree(), r.node);
                    }
                    setMessage("");
                } catch (Exception e) {
                    e.printStackTrace();
                    setMessage("Swap failed! Make sure the clipboard is not empty.");
                }
            }
        }.start();
    }

    public void nodePaste() {
        final NodeRange r = curRange();
        setMessage("Pasting clipboard...");
        new Thread() {

            public void run() {
                try {
                    clipboard.paste((CachedRootedTree) r.render.getTree(), r.node);
                    setMessage("");
                } catch (Exception e) {
                    e.printStackTrace();
                    setMessage("Paste failed! Make sure the clipboard is not empty.");
                }
            }
        }.start();
    }

    public void nodeClearClipboard() {
        clipboard.clearClipboard();
    }

    public void nodeDelete() {
        NodeRange r = curRange();
        RootedTree g = r.render.getTree();
        synchronized (g) {
            g.deleteNode(getCurNode());
        }
    }

    public void nodeDeleteSubtree() {
        NodeRange r = curRange();
        RootedTree g = r.render.getTree();
        final PhyloNode n = getCurNode();
        synchronized (g) {
            g.deleteSubtree(n);
        }
    }

    public void nodeCollapse() {
        NodeRange r = curRange();
        RootedTree g = r.render.getTree();
        PhyloNode n = getCurNode();
        n.setAnnotation("layout_size", g.getNumEnclosedLeaves(n));
        g.collapseNode(n);
        g.modPlus();
        layout();
    }

    public void viewUnrooted() {
        context.config().setLayout("unrooted");
    }

    public void viewRectangular() {
        context.config().setLayout("rectangle");
    }

    public void viewDiagonal() {
        context.config().setLayout("diagonal");
    }

    public void viewCircular() {
        context.config().setLayout("circular");
    }

    public void zoomToFull() {
        context.trees().fillScreen();
    }

    public void treeNew() {
        synchronized (context.trees().getTree()) {
            context.trees().setTree(PhyloConfig.DEFAULT_TREE);
        }
        layout();
    }

    public void treeFlip() {
        PhyloTree t = (PhyloTree) getCurTree();
        t.reverseSubtree(t.getRoot());
        t.modPlus();
        layout();
    }

    public void treeAutoSort() {
        RootedTree tree = getCurTree();
        tree.ladderizeSubtree(tree.getRoot());
        layout();
    }

    public void treeRemoveElbows() {
        RootedTree tree = getCurTree();
        synchronized (tree) {
            tree.removeElbowsBelow(tree.getRoot());
        }
        layout();
    }

    public void treeUncollapseAll() {
        getCurTree().uncollapseAllNodes();
        layout();
    }

    public void treeAlignLeaves() {
        new Thread() {

            @Override
            public void run() {
                setMessage("Aligning leaves...");
                RootedTree tree = getCurTree();
                tree.makeSubtreeUltrametric(tree.getRoot());
                layout();
                setMessage("");
            }
        }.start();
    }

    public void treeLogTransform() {
        new Thread() {

            @Override
            public void run() {
                setMessage("Log transforming tree...");
                RootedTree tree = getCurTree();
                tree.logTransform(tree.getRoot(), 1000);
                layout();
                setMessage("");
            }
        }.start();
    }

    public void treeMutateOnce() {
        context.trees().mutateTree();
    }

    public void treeMutateSlow() {
        context.trees().startMutatingTree(1000);
    }

    public void treeMutateFast() {
        context.trees().startMutatingTree(50);
    }

    public void treeStopMutating() {
        context.trees().stopMutatingTree();
    }

    public void treeSaveConfigIntoTree() {
        Map<String, String> changedFields = PhyloConfig.getConfigSnapshot(context.config());
        PhyloNode root = (PhyloNode) getCurTree().getRoot();
        for (String key : changedFields.keySet()) {
            root.setAnnotation(key, changedFields.get(key));
        }
    }

    public void nodeLoadImage() {
        new Thread() {

            public void run() {
                getCurNode().loadThumbImage();
            }
        }.start();
    }

    public void treeLoadImages() {
        new Thread() {

            public void run() {
                RootedTree tree = getCurTree();
                ArrayList<PhyloNode> leaves = new ArrayList<PhyloNode>();
                tree.getAll(tree.getRoot(), leaves, null);
                for (PhyloNode n : leaves) {
                    n.loadThumbImage();
                }
            }
        }.start();
    }

    public void treeSave() {
        FileDialog fd = new FileDialog(context.ui().getFrame(), "Choose your desination file. Tree will be in Newick / NHX format.", FileDialog.SAVE);
        fd.pack();
        fd.setVisible(true);
        String directory = fd.getDirectory();
        String filename = fd.getFile();
        if (filename == null) {
            context.getPW().setMessage("Tree save cancelled.");
            return;
        }
        final File f = new File(directory, filename);
        setMessage("Saving tree...");
        new Thread() {

            public void run() {
                p.noLoop();
                File dir = f.getParentFile();
                TreeIO.outputTreeImages(context.trees().getTree(), dir);
                String ext = FileUtils.getFileExtension(f);
                String s;
                RootedTree tree = context.trees().getTree();
                if (ext.equals("nh")) {
                    s = tree.getNewick();
                } else if (ext.equals("xml")) {
                    s = tree.getNeXML();
                } else {
                    s = tree.getNHX();
                }
                try {
                    f.createNewFile();
                    BufferedWriter r = new BufferedWriter(new FileWriter(f));
                    r.append(s);
                    r.close();
                    p.loop();
                    setMessage("");
                } catch (IOException e) {
                    e.printStackTrace();
                    p.loop();
                    setMessage("Error writing file. Whoops!");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    setMessage("");
                    layout();
                    return;
                }
            }
        }.start();
    }

    public void treeLoad() {
        FileDialog fd = new FileDialog(context.ui().getFrame(), "Locate a Newick/NHX/Nexus format file.", FileDialog.LOAD);
        fd.pack();
        fd.setVisible(true);
        String directory = fd.getDirectory();
        String filename = fd.getFile();
        if (filename == null) {
            context.getPW().setMessage("Tree load cancelled.");
            return;
        }
        final File f = new File(directory, filename);
        setMessage("Loading tree...");
        new Thread() {

            public void run() {
                PhyloTree t = (PhyloTree) TreeIO.parseFile(new PhyloTree(), f);
                p.noLoop();
                if (t != null) {
                    context.trees().setTree(t);
                    setMessage("");
                } else {
                    setMessage("Error loading tree!");
                }
                p.loop();
                layout();
            }
        }.start();
    }

    public Frame getFrame() {
        Frame parentFrame = null;
        Component comp = p.getParent();
        while (comp != null) {
            if (comp instanceof Frame) {
                parentFrame = (Frame) comp;
                break;
            }
            comp = comp.getParent();
        }
        if (parentFrame == null) parentFrame = new Frame();
        return parentFrame;
    }

    public PhyloNode getHoveredNode() {
        PhyloNode nearest = contextMenu.getNearestNode();
        if (nearest != null) {
            boolean contains = contextMenu.traverser.containsPoint(nearest.range, contextMenu.traverser.pt);
            if (contains) return nearest;
        }
        return null;
    }

    public void treeInput() {
        Frame parentFrame = getFrame();
        final InputDialog d = new InputDialog(parentFrame, "Enter your Newick-formatted tree here.");
        SecurityChecker sc = new SecurityChecker(p);
        if (sc.canAccessInternet()) {
            Label l = new Label("A URL pointing to a Newick/NHX/Nexus file is also valid input.");
            d.add(l, BorderLayout.NORTH);
        }
        d.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                final String treeString = d.text.getText();
                if (treeString == null || treeString.length() == 0) return;
                new Thread() {

                    public void run() {
                        setMessage("Loading tree...");
                        PhyloTree t = null;
                        try {
                            t = (PhyloTree) TreeIO.parseNewickString(new PhyloTree(), treeString);
                            p.noLoop();
                        } catch (Exception e) {
                            t = null;
                        }
                        if (t != null) {
                            context.trees().setTree(t);
                            setMessage("");
                        } else {
                            setMessage("Error loading tree!");
                        }
                        p.loop();
                        layout();
                    }
                }.start();
            }
        });
        d.setVisible(true);
    }

    public void fileOutput() {
        ImageExportDialog ied = new ImageExportDialog(getFrame());
    }

    public boolean hasClipboard() {
        return !clipboard.isEmpty();
    }

    public boolean isNotRoot() {
        if (getCurTree() == null || getCurNode() == null) return true;
        return (getCurTree().getRoot() != getCurNode());
    }

    public boolean isLeafNode() {
        return getCurTree().isLeaf(getCurNode());
    }

    public boolean isInternalNode() {
        if (getCurTree() == null || getCurNode() == null) return true;
        return !getCurTree().isLeaf(getCurNode());
    }

    public boolean isRectangleRender() {
        if (context.trees() == null) return false;
        if (context.trees().getRenderer() == null) return false;
        BasicTreeRenderer rend = context.trees().getRenderer();
        return rend.getLayout() instanceof LayoutCladogram;
    }

    public void destroy() {
        if (annotation != null) annotation.dispose();
        annotation = null;
        disposeMenus();
        p = null;
        focus = null;
        event = null;
        keys = null;
        clipboard = null;
        text = null;
        contextMenu = null;
        toolbar = null;
        search = null;
        thread = null;
        menus = null;
    }
}
