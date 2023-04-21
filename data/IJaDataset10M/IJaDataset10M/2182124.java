package pl.edu.wat.wcy.jit.editor.menu;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;

public class BasicGraphEditor extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -6561623072112577140L;

    /**
     * Adds required resources for i18n
     */
    static {
        try {
            mxResources.add("/pl/edu/wat/wcy/jit/editor/resources/editor");
        } catch (Exception e) {
        }
    }

    /**
     *
     */
    protected mxGraphComponent graphComponent;

    /**
     *
     */
    protected mxGraphOutline graphOutline;

    /**
     *
     */
    protected JTabbedPane libraryPane;

    /**
     *
     */
    protected mxUndoManager undoManager;

    /**
     *
     */
    protected String appTitle;

    /**
     *
     */
    protected JLabel statusBar;

    /**
     *
     */
    protected File currentFile;

    /**
     * Flag indicating whether the current graph has been modified
     */
    protected boolean modified = false;

    /**
     *
     */
    protected mxRubberband rubberband;

    /**
     *
     */
    protected mxKeyboardHandler keyboardHandler;

    /**
     *
     */
    protected mxIEventListener undoHandler = new mxIEventListener() {

        public void invoke(Object source, mxEventObject evt) {
            undoManager.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
        }
    };

    /**
     *
     */
    protected mxIEventListener changeTracker = new mxIEventListener() {

        public void invoke(Object source, mxEventObject evt) {
            setModified(true);
        }
    };

    /**
     *
     */
    public BasicGraphEditor(String appTitle, mxGraphComponent component) {
        this.appTitle = appTitle;
        graphComponent = component;
        final mxGraph graph = graphComponent.getGraph();
        undoManager = createUndoManager();
        graph.setResetViewOnRootChange(false);
        graph.getModel().addListener(mxEvent.CHANGE, changeTracker);
        graph.getModel().addListener(mxEvent.UNDO, undoHandler);
        graph.getView().addListener(mxEvent.UNDO, undoHandler);
        mxIEventListener undoHandler = new mxIEventListener() {

            public void invoke(Object source, mxEventObject evt) {
                List<mxUndoableChange> changes = ((mxUndoableEdit) evt.getProperty("edit")).getChanges();
                graph.setSelectionCells(graph.getSelectionCellsForChanges(changes));
            }
        };
        undoManager.addListener(mxEvent.UNDO, undoHandler);
        undoManager.addListener(mxEvent.REDO, undoHandler);
        graphOutline = new mxGraphOutline(graphComponent);
        libraryPane = new JTabbedPane();
        JSplitPane inner = new JSplitPane(JSplitPane.VERTICAL_SPLIT, libraryPane, graphOutline);
        inner.setDividerLocation(320);
        inner.setResizeWeight(1);
        inner.setDividerSize(6);
        inner.setBorder(null);
        JSplitPane outer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inner, graphComponent);
        outer.setOneTouchExpandable(true);
        outer.setDividerLocation(200);
        outer.setDividerSize(6);
        outer.setBorder(null);
        statusBar = createStatusBar();
        installRepaintListener();
        setLayout(new BorderLayout());
        add(outer, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        installToolBar();
        installHandlers();
        installListeners();
        updateTitle();
    }

    /**
     *
     */
    protected mxUndoManager createUndoManager() {
        return new mxUndoManager();
    }

    /**
     *
     */
    protected void installHandlers() {
        rubberband = new mxRubberband(graphComponent);
        keyboardHandler = new EditorKeyboardHandler(graphComponent);
    }

    /**
     *
     */
    protected void installToolBar() {
        add(new EditorToolBar(this, JToolBar.HORIZONTAL), BorderLayout.NORTH);
    }

    /**
     *
     */
    protected JLabel createStatusBar() {
        JLabel statusBar = new JLabel(mxResources.get("ready"));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        return statusBar;
    }

    /**
     *
     */
    protected void installRepaintListener() {
        graphComponent.getGraph().addListener(mxEvent.REPAINT, new mxIEventListener() {

            public void invoke(Object source, mxEventObject evt) {
                String buffer = (graphComponent.getTripleBuffer() != null) ? "" : " (unbuffered)";
                mxRectangle dirty = (mxRectangle) evt.getProperty("region");
                if (dirty == null) {
                    status("Repaint all" + buffer);
                } else {
                    status("Repaint: x=" + (int) (dirty.getX()) + " y=" + (int) (dirty.getY()) + " w=" + (int) (dirty.getWidth()) + " h=" + (int) (dirty.getHeight()) + buffer);
                }
            }
        });
    }

    /**
     *
     */
    public EditorPalette insertPalette(String title) {
        final EditorPalette palette = new EditorPalette();
        final JScrollPane scrollPane = new JScrollPane(palette);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        libraryPane.add(title, scrollPane);
        libraryPane.addComponentListener(new ComponentAdapter() {

            /**
             *
             */
            public void componentResized(ComponentEvent e) {
                int w = scrollPane.getWidth() - scrollPane.getVerticalScrollBar().getWidth();
                palette.setPreferredWidth(w);
            }
        });
        return palette;
    }

    /**
     *
     */
    protected void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            graphComponent.zoomIn();
        } else {
            graphComponent.zoomOut();
        }
        status(mxResources.get("scale") + ": " + (int) (100 * graphComponent.getGraph().getView().getScale()) + "%");
    }

    /**
     *
     */
    protected void showOutlinePopupMenu(MouseEvent e) {
        Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), graphComponent);
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(mxResources.get("magnifyPage"));
        item.setSelected(graphOutline.isFitPage());
        item.addActionListener(new ActionListener() {

            /**
             *
             */
            public void actionPerformed(ActionEvent e) {
                graphOutline.setFitPage(!graphOutline.isFitPage());
                graphOutline.repaint();
            }
        });
        JCheckBoxMenuItem item2 = new JCheckBoxMenuItem(mxResources.get("showLabels"));
        item2.setSelected(graphOutline.isDrawLabels());
        item2.addActionListener(new ActionListener() {

            /**
             *
             */
            public void actionPerformed(ActionEvent e) {
                graphOutline.setDrawLabels(!graphOutline.isDrawLabels());
                graphOutline.repaint();
            }
        });
        JCheckBoxMenuItem item3 = new JCheckBoxMenuItem(mxResources.get("buffering"));
        item3.setSelected(graphOutline.isTripleBuffered());
        item3.addActionListener(new ActionListener() {

            /**
             *
             */
            public void actionPerformed(ActionEvent e) {
                graphOutline.setTripleBuffered(!graphOutline.isTripleBuffered());
                graphOutline.repaint();
            }
        });
        JPopupMenu menu = new JPopupMenu();
        menu.add(item);
        menu.add(item2);
        menu.add(item3);
        menu.show(graphComponent, pt.x, pt.y);
        e.consume();
    }

    /**
     *
     */
    protected void showGraphPopupMenu(MouseEvent e) {
        Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), graphComponent);
        EditorPopupMenu menu = new EditorPopupMenu(BasicGraphEditor.this);
        menu.show(graphComponent, pt.x, pt.y);
        e.consume();
    }

    /**
     *
     */
    protected void mouseLocationChanged(MouseEvent e) {
        status(e.getX() + ", " + e.getY());
    }

    /**
     *
     */
    protected void installListeners() {
        MouseWheelListener wheelTracker = new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getSource() instanceof mxGraphOutline || e.isControlDown()) {
                    BasicGraphEditor.this.mouseWheelMoved(e);
                }
            }
        };
        graphOutline.addMouseWheelListener(wheelTracker);
        graphComponent.addMouseWheelListener(wheelTracker);
        graphOutline.addMouseListener(new MouseAdapter() {

            /**
             *
             */
            public void mousePressed(MouseEvent e) {
                mouseReleased(e);
            }

            /**
             *
             */
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showOutlinePopupMenu(e);
                }
            }
        });
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

            /**
             *
             */
            public void mousePressed(MouseEvent e) {
                mouseReleased(e);
            }

            /**
             *
             */
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showGraphPopupMenu(e);
                }
            }
        });
        graphComponent.getGraphControl().addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                mouseLocationChanged(e);
            }

            public void mouseMoved(MouseEvent e) {
                mouseDragged(e);
            }
        });
    }

    /**
     *
     */
    public void setCurrentFile(File file) {
        File oldValue = currentFile;
        currentFile = file;
        firePropertyChange("currentFile", oldValue, file);
        if (oldValue != file) {
            updateTitle();
        }
    }

    /**
     *
     */
    public File getCurrentFile() {
        return currentFile;
    }

    /**
     *
     * @param modified
     */
    public void setModified(boolean modified) {
        boolean oldValue = this.modified;
        this.modified = modified;
        firePropertyChange("modified", oldValue, modified);
        if (oldValue != modified) {
            updateTitle();
        }
    }

    /**
     *
     * @return whether or not the current graph has been modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     *
     */
    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }

    /**
     *
     */
    public mxGraphOutline getGraphOutline() {
        return graphOutline;
    }

    /**
     *
     */
    public JTabbedPane getLibraryPane() {
        return libraryPane;
    }

    /**
     *
     */
    public mxUndoManager getUndoManager() {
        return undoManager;
    }

    /**
     *
     * @param name
     * @param action
     * @return a new Action bound to the specified string name
     */
    public Action bind(String name, final Action action) {
        return bind(name, action, null);
    }

    /**
     *
     * @param name
     * @param action
     * @return a new Action bound to the specified string name and icon
     */
    @SuppressWarnings("serial")
    public Action bind(String name, final Action action, String iconUrl) {
        return new AbstractAction(name, (iconUrl != null) ? new ImageIcon(BasicGraphEditor.class.getResource(iconUrl)) : null) {

            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(new ActionEvent(getGraphComponent(), e.getID(), e.getActionCommand()));
            }
        };
    }

    /**
     *
     * @param msg
     */
    public void status(String msg) {
        statusBar.setText(msg);
    }

    /**
     *
     */
    public void updateTitle() {
        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
        if (frame != null) {
            String title = (currentFile != null) ? currentFile.getAbsolutePath() : mxResources.get("newDiagram");
            if (modified) {
                title += "*";
            }
            frame.setTitle(title + " - " + appTitle);
        }
    }

    /**
     *
     */
    public void about() {
        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
        if (frame != null) {
            EditorAboutFrame about = new EditorAboutFrame(frame);
            about.setModal(true);
            int x = frame.getX() + (frame.getWidth() - about.getWidth()) / 2;
            int y = frame.getY() + (frame.getHeight() - about.getHeight()) / 2;
            about.setLocation(x, y);
            about.setVisible(true);
        }
    }

    /**
     *
     */
    public void exit() {
        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
        if (frame != null) {
            frame.dispose();
        }
    }

    /**
     *
     */
    public void setLookAndFeel(String clazz) {
        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
        if (frame != null) {
            try {
                UIManager.setLookAndFeel(clazz);
                SwingUtilities.updateComponentTreeUI(frame);
                keyboardHandler = new EditorKeyboardHandler(graphComponent);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     *
     */
    public JFrame createFrame(JMenuBar menuBar) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        frame.setSize(870, 640);
        updateTitle();
        return frame;
    }

    /**
     * Creates an action that executes the specified layout.
     *
     * @param key Key to be used for getting the label from mxResources and also
     * to create the layout instance for the commercial graph editor example.
     * @return an action that executes the specified layout
     */
    @SuppressWarnings("serial")
    public Action graphLayout(final String key, boolean animate) {
        final mxIGraphLayout layout = createLayout(key, animate);
        if (layout != null) {
            return new AbstractAction(mxResources.get(key)) {

                public void actionPerformed(ActionEvent e) {
                    final mxGraph graph = graphComponent.getGraph();
                    Object cell = graph.getSelectionCell();
                    if (cell == null || graph.getModel().getChildCount(cell) == 0) {
                        cell = graph.getDefaultParent();
                    }
                    graph.getModel().beginUpdate();
                    try {
                        long t0 = System.currentTimeMillis();
                        layout.execute(cell);
                        status("Layout: " + (System.currentTimeMillis() - t0) + " ms");
                    } finally {
                        mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);
                        morph.addListener(mxEvent.DONE, new mxIEventListener() {

                            public void invoke(Object sender, mxEventObject evt) {
                                graph.getModel().endUpdate();
                            }
                        });
                        morph.startAnimation();
                    }
                }
            };
        } else {
            return new AbstractAction(mxResources.get(key)) {

                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(graphComponent, mxResources.get("noLayout"));
                }
            };
        }
    }

    /**
     * Creates a layout instance for the given identifier.
     */
    protected mxIGraphLayout createLayout(String ident, boolean animate) {
        mxIGraphLayout layout = null;
        if (ident != null) {
            mxGraph graph = graphComponent.getGraph();
            if (ident.equals("verticalHierarchical")) {
                layout = new mxHierarchicalLayout(graph);
            } else if (ident.equals("horizontalHierarchical")) {
                layout = new mxHierarchicalLayout(graph, JLabel.WEST);
            } else if (ident.equals("verticalTree")) {
                layout = new mxCompactTreeLayout(graph, false);
            } else if (ident.equals("horizontalTree")) {
                layout = new mxCompactTreeLayout(graph, true);
            } else if (ident.equals("parallelEdges")) {
                layout = new mxParallelEdgeLayout(graph);
            } else if (ident.equals("placeEdgeLabels")) {
                layout = new mxEdgeLabelLayout(graph);
            } else if (ident.equals("organicLayout")) {
                layout = new mxOrganicLayout(graph);
            }
            if (ident.equals("verticalPartition")) {
                layout = new mxPartitionLayout(graph, false) {

                    /**
                     * Overrides the empty implementation to return the size of the
                     * graph control.
                     */
                    public mxRectangle getContainerSize() {
                        return graphComponent.getLayoutAreaSize();
                    }
                };
            } else if (ident.equals("horizontalPartition")) {
                layout = new mxPartitionLayout(graph, true) {

                    /**
                     * Overrides the empty implementation to return the size of the
                     * graph control.
                     */
                    public mxRectangle getContainerSize() {
                        return graphComponent.getLayoutAreaSize();
                    }
                };
            } else if (ident.equals("verticalStack")) {
                layout = new mxStackLayout(graph, false) {

                    /**
                     * Overrides the empty implementation to return the size of the
                     * graph control.
                     */
                    public mxRectangle getContainerSize() {
                        return graphComponent.getLayoutAreaSize();
                    }
                };
            } else if (ident.equals("horizontalStack")) {
                layout = new mxStackLayout(graph, true) {

                    /**
                     * Overrides the empty implementation to return the size of the
                     * graph control.
                     */
                    public mxRectangle getContainerSize() {
                        return graphComponent.getLayoutAreaSize();
                    }
                };
            } else if (ident.equals("circleLayout")) {
                layout = new mxCircleLayout(graph);
            }
        }
        return layout;
    }
}
