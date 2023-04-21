package ru.spbau.bytecode.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import ru.spbau.bytecode.graph.Graph;
import ru.spbau.bytecode.graph.GraphBuilder;
import ru.spbau.bytecode.graph.GraphException;
import ru.spbau.bytecode.graph.GraphSaver;

public class MainWindow extends JFrame {

    private final GraphViewer graphViewer = new GraphViewer();

    private File currentDirectory;

    private final List<GraphBuilder> graphBuilders;

    private GraphBuilder currentGraphBuilder;

    private final GraphBuilder graphReader;

    private final GraphSaver graphSaver;

    private Graph graph;

    private final List<ButtonModel> graphSwitchers = new LinkedList<ButtonModel>();

    private CheckBoxTree filter = new CheckBoxTree();

    private JFileChooser projectDirectoryChooser;

    private JFileChooser graphFileManager;

    private JScrollPane filterScroll = new JScrollPane(filter);

    private JSplitPane splitPane;

    private DefaultButtonModel filterSwitcherButtonModel = new DefaultButtonModel();

    public MainWindow(List<GraphBuilder> graphBuilders, GraphBuilder graphReader, GraphSaver graphSaver) {
        this.graphReader = graphReader;
        this.graphSaver = graphSaver;
        this.graphBuilders = graphBuilders;
        currentGraphBuilder = graphBuilders.get(0);
        setBounds(100, 100, 1200, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowClosingListener());
        setTitle("Dependencies viewer");
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.add(new JScrollPane(graphViewer));
        splitPane.add(filterScroll);
        splitPane.setSize(this.getSize());
        splitPane.setDividerLocation(0.8);
        splitPane.setDividerSize(5);
        add(splitPane, BorderLayout.CENTER);
        projectDirectoryChooser = createProjectDirectoryOpener();
        graphFileManager = createGraphFileManager(graphSaver);
        setJMenuBar(createMenu());
        add(createToolBar(), BorderLayout.NORTH);
        FilterSwitcherListener filterSwitcherListener = new FilterSwitcherListener();
        filterSwitcherButtonModel.addActionListener(filterSwitcherListener);
        filterSwitcherButtonModel.setSelected(true);
        filter.addListUpdateListener(new ListUpdateListener() {

            @Override
            public void listUpdated(ListUpdateEvent e) {
                MainWindow.this.repaint();
            }
        });
    }

    private JFileChooser createGraphFileManager(final GraphSaver graphSaver) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setLocation(200, 10);
        fileChooser.setSize(800, 400);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public String getDescription() {
                return graphSaver.getExtension();
            }

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.isFile() && f.getName().endsWith(graphSaver.getExtension());
            }
        });
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        return fileChooser;
    }

    private JFileChooser createProjectDirectoryOpener() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setLocation(200, 10);
        fileChooser.setSize(800, 400);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }
        });
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        return fileChooser;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton openProject = new JButton(new ImageIcon("resources/images/open project.png"));
        openProject.addActionListener(new ProjectDirectoryChooserListener());
        openProject.setToolTipText("Open project");
        toolBar.add(openProject);
        toolBar.addSeparator();
        JButton openGraph = new JButton(new ImageIcon("resources/images/open graph.png"));
        openGraph.addActionListener(new GraphOpenerListener());
        openGraph.setToolTipText("Open graph");
        toolBar.add(openGraph);
        JButton saveGraph = new JButton(new ImageIcon("resources/images/save graph.png"));
        saveGraph.addActionListener(new GraphSaverListener());
        saveGraph.setToolTipText("Save graph");
        toolBar.add(saveGraph);
        toolBar.addSeparator();
        JToggleButton filterSwitcher = new JToggleButton(new ImageIcon("resources/images/filter.png"));
        filterSwitcher.setModel(filterSwitcherButtonModel);
        filterSwitcher.setToolTipText("Choose show filter or not");
        toolBar.add(filterSwitcher);
        toolBar.addSeparator();
        ButtonGroup graphTypeSwitcher = new ButtonGroup();
        boolean first = true;
        for (int i = 0; i < graphBuilders.size(); i++) {
            GraphBuilder graphBuilder = graphBuilders.get(i);
            JToggleButton graphType = new JToggleButton(new ImageIcon("resources/images/" + graphBuilder.getName() + ".png"));
            graphType.setToolTipText(graphBuilder.getName());
            graphType.setModel(graphSwitchers.get(i));
            toolBar.add(graphType);
            graphTypeSwitcher.add(graphType);
            graphTypeSwitcher.setSelected(graphType.getModel(), first);
            first = false;
        }
        return toolBar;
    }

    private JMenuBar createMenu() {
        JMenuBar menu = new JMenuBar();
        menu.add(createFileMenuItem());
        menu.add(createToolsMenuItem());
        menu.add(createHelpMenuItem());
        return menu;
    }

    private JMenu createFileMenuItem() {
        JMenu file = new JMenu("File");
        JMenuItem openProject = new JMenuItem("Open project...");
        openProject.addActionListener(new ProjectDirectoryChooserListener());
        file.add(openProject);
        JMenuItem openGraph = new JMenuItem("Open graph...");
        openGraph.addActionListener(new GraphOpenerListener());
        file.add(openGraph);
        JMenuItem saveGraph = new JMenuItem("Save graph...");
        saveGraph.addActionListener(new GraphSaverListener());
        file.add(saveGraph);
        file.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new WindowClosingListener());
        file.add(exit);
        return file;
    }

    private JMenu createToolsMenuItem() {
        ButtonGroup graphTypeSwitcher = new ButtonGroup();
        JMenu toolsMenuItem = new JMenu("Tools");
        boolean first = true;
        for (int i = 0; i < graphBuilders.size(); i++) {
            GraphBuilder graphBuilder = graphBuilders.get(i);
            JRadioButtonMenuItem graphType = new JRadioButtonMenuItem(graphBuilder.getName());
            toolsMenuItem.add(graphType);
            graphTypeSwitcher.add(graphType);
            graphTypeSwitcher.setSelected(graphType.getModel(), first);
            first = false;
            graphType.getModel().addActionListener(new GraphTypeChooserListener(graphBuilder));
            graphType.getModel().setEnabled(false);
            graphSwitchers.add(graphType.getModel());
        }
        toolsMenuItem.addSeparator();
        JCheckBoxMenuItem filterSwitcher = new JCheckBoxMenuItem("Filter");
        filterSwitcher.setModel(filterSwitcherButtonModel);
        toolsMenuItem.add(filterSwitcher);
        return toolsMenuItem;
    }

    private JMenu createHelpMenuItem() {
        JMenu helpMenuItem = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = "Dependencies viewer for Java programs\n" + "SPbAU, 2011";
                JOptionPane.showMessageDialog(MainWindow.this, msg, "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        helpMenuItem.add(about);
        return helpMenuItem;
    }

    private class GraphTypeChooserListener implements ActionListener {

        private final GraphBuilder gBuilder;

        GraphTypeChooserListener(GraphBuilder gBuilder) {
            this.gBuilder = gBuilder;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentGraphBuilder = gBuilder;
            if (currentDirectory == null) {
                return;
            }
            Thread graphBuildingThread = new Thread(new GraphBuilderThread(gBuilder, currentDirectory, true));
            graphBuildingThread.start();
            validate();
        }
    }

    private class ProjectDirectoryChooserListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int openResult = projectDirectoryChooser.showOpenDialog(MainWindow.this);
            if (openResult == JFileChooser.APPROVE_OPTION) {
                currentDirectory = projectDirectoryChooser.getSelectedFile();
                for (ButtonModel bm : graphSwitchers) {
                    bm.setEnabled(true);
                }
                Thread graphBuildingThread = new Thread(new GraphBuilderThread(currentGraphBuilder, currentDirectory, true));
                graphBuildingThread.start();
            }
        }
    }

    private class GraphSaverListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (MainWindow.this.graph == null) {
                return;
            }
            int openResult = graphFileManager.showSaveDialog(MainWindow.this);
            if (openResult == JFileChooser.APPROVE_OPTION) {
                File f = graphFileManager.getSelectedFile();
                if (!f.getName().endsWith(MainWindow.this.graphSaver.getExtension())) {
                    f = new File(f.getAbsolutePath() + MainWindow.this.graphSaver.getExtension());
                }
                Thread graphSavingThread = new Thread(new GraphSaverTherad(graphSaver, f));
                graphSavingThread.start();
            }
        }
    }

    private class GraphOpenerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int openResult = graphFileManager.showOpenDialog(MainWindow.this);
            if (openResult == JFileChooser.APPROVE_OPTION) {
                currentDirectory = null;
                for (ButtonModel bm : graphSwitchers) {
                    bm.setEnabled(false);
                }
                Thread graphReadingThread = new Thread(new GraphBuilderThread(graphReader, graphFileManager.getSelectedFile(), false));
                graphReadingThread.start();
            }
        }
    }

    private class FilterSwitcherListener implements ActionListener {

        private boolean shouldShowFilter = false;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!shouldShowFilter) {
                splitPane.remove(filterScroll);
            } else {
                splitPane.add(filterScroll);
                splitPane.setDividerLocation(0.8);
            }
            ((DefaultButtonModel) e.getSource()).setSelected(shouldShowFilter);
            shouldShowFilter = !shouldShowFilter;
            validate();
        }
    }

    private class WindowClosingListener extends WindowAdapter implements ActionListener {

        @Override
        public void windowClosing(WindowEvent e) {
            showClosingDialog();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            showClosingDialog();
        }

        private void showClosingDialog() {
            int answer = JOptionPane.showConfirmDialog(MainWindow.this, "Are you sure you want to exit?", "Closing", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    private class GraphBuilderThread implements Runnable {

        private final GraphBuilder gBuilder;

        private final File file;

        private final boolean shouldPlaceVertices;

        public GraphBuilderThread(GraphBuilder gBuilder, File file, boolean shouldPlaceVertices) {
            this.gBuilder = gBuilder;
            this.file = file;
            this.shouldPlaceVertices = shouldPlaceVertices;
        }

        @Override
        public void run() {
            try {
                Graph graph = gBuilder.buildGraph(file);
                if (shouldPlaceVertices) {
                    graph.placeVertices(new Point(0, 0));
                }
                graphViewer.setGraph(graph);
                filter.setGraph(graph);
                MainWindow.this.graph = graph;
            } catch (final GraphException e) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(MainWindow.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        }
    }

    private class GraphSaverTherad implements Runnable {

        private final GraphSaver graphSaver;

        private final File file;

        public GraphSaverTherad(GraphSaver graphSaver, File file) {
            this.graphSaver = graphSaver;
            this.file = file;
        }

        @Override
        public void run() {
            try {
                graphSaver.saveGraph(MainWindow.this.graph, file);
            } catch (GraphException e) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(MainWindow.this, "Can't create file", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        }
    }
}
