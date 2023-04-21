package play.ide.handler;

import higraph.model.interfaces.Node;
import higraph.view.layout.NestedTreeLayoutManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import play.higraph.swing.PLAYHigraphJComponent;
import play.higraph.swing.PLAYHigraphJComponentKeyAdapter;
import play.higraph.swing.PLAYSubgraphMouseAdapter;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYSubgraphEventObserver;
import play.higraph.view.PLAYViewFactory;
import play.higraph.view.layout.PLAYBoxesInBoxesLayout;
import play.ide.view.ExpsPallet;
import play.ide.view.NodesOutline;
import play.ide.view.SyntaxPallet;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class ViewHandler {

    private JFrame mainFrame;

    private PLAYWholeGraph wholeGraph;

    private PLAYSubgraph subgraph;

    private PLAYHigraphJComponent higraphJComponent;

    private PLAYHigraphView higraphView;

    public ViewHandler() {
        this.mainFrame = new JFrame();
        BTTimeManager btTimeManager = new BTTimeManager();
        this.wholeGraph = new PLAYWholeGraph(btTimeManager);
        this.subgraph = new PLAYSubgraph(this.wholeGraph);
        this.higraphJComponent = new PLAYHigraphJComponent();
        PLAYViewFactory viewFactory = new PLAYViewFactory(btTimeManager);
        this.higraphView = viewFactory.makeHigraphView(this.wholeGraph, this.higraphJComponent);
        PLAYSubgraphEventObserver subgraphEventObserver = new PLAYSubgraphEventObserver(this.higraphView, this.wholeGraph, viewFactory, this.subgraph);
        PLAYSubgraphMouseAdapter subgraphMouseAdapter = new PLAYSubgraphMouseAdapter(this.higraphView, subgraphEventObserver);
        this.higraphJComponent.addKeyListener(new PLAYHigraphJComponentKeyAdapter(this.higraphView));
        this.higraphJComponent.setBackground(Color.WHITE);
        this.higraphJComponent.setAutoscrolls(true);
        this.higraphJComponent.setSubgraphView(this.higraphView);
        this.higraphView.setLayoutManager(new PLAYBoxesInBoxesLayout(NestedTreeLayoutManager.Axis.Y));
        subgraphMouseAdapter.installIn(this.higraphJComponent);
        this.mainFrame.setTitle("PLAY - PLAY Language IDE");
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.addWindowListener(new WindowAdapter() {

            /**
	     * Exit application and save data file
	     */
            private void close() {
                System.exit(0);
            }

            @Override
            public void windowClosing(WindowEvent arg0) {
                this.close();
            }

            public void windowClosed(WindowEvent e) {
                this.close();
            }
        });
        this.mainFrame.setJMenuBar(this.getMenuBar());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(1024, 768));
        mainPanel.add(this.getFrameSplitPane(), BorderLayout.CENTER);
        JButton button = new JButton("Temp");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (Node<?, ?, ?, ?, ?, ?, ?> node : wholeGraph.getTops()) {
                    node.delete();
                }
                higraphJComponent.scrollRectToVisible(new Rectangle(0, (int) higraphJComponent.getSize().getHeight() - 50, 100, 100));
                higraphJComponent.setPreferredSize(new Dimension((int) higraphJComponent.getSize().getWidth() + 100, (int) higraphJComponent.getSize().getHeight() + 100));
                higraphView.refresh();
                higraphJComponent.revalidate();
                higraphJComponent.repaint();
            }
        });
        mainPanel.add(button, BorderLayout.SOUTH);
        this.mainFrame.getContentPane().add(mainPanel);
        this.mainFrame.pack();
    }

    private JSplitPane getFrameSplitPane() {
        JSplitPane frameSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        frameSplitPane.setOneTouchExpandable(true);
        frameSplitPane.setDividerLocation(850);
        frameSplitPane.setLeftComponent(this.getLeftCenterSplitPane());
        frameSplitPane.setRightComponent(this.getRightTopBottomSplitPane());
        return frameSplitPane;
    }

    private JSplitPane getLeftCenterSplitPane() {
        JSplitPane leftCenterSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftCenterSplitPane.setOneTouchExpandable(true);
        leftCenterSplitPane.setDividerLocation(150);
        leftCenterSplitPane.setLeftComponent(this.getLeftTopBottomSplitPane());
        JScrollPane scrollPane = new JScrollPane(this.higraphJComponent);
        scrollPane.setFocusable(true);
        leftCenterSplitPane.setRightComponent(scrollPane);
        return leftCenterSplitPane;
    }

    private JSplitPane getLeftTopBottomSplitPane() {
        JSplitPane leftTopBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftTopBottomSplitPane.setOneTouchExpandable(true);
        leftTopBottomSplitPane.setDividerLocation(300);
        leftTopBottomSplitPane.setTopComponent(new SyntaxPallet());
        leftTopBottomSplitPane.setBottomComponent(new ExpsPallet());
        return leftTopBottomSplitPane;
    }

    private JSplitPane getRightTopBottomSplitPane() {
        JSplitPane rightTopBottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        rightTopBottomSplitPane.setOneTouchExpandable(true);
        rightTopBottomSplitPane.setDividerLocation(500);
        JScrollPane scrollPane = new JScrollPane(new NodesOutline(this.higraphView));
        scrollPane.setFocusable(true);
        rightTopBottomSplitPane.setTopComponent(scrollPane);
        rightTopBottomSplitPane.setBottomComponent(new JPanel());
        return rightTopBottomSplitPane;
    }

    private JMenuBar getMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        fileMenu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_E);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        exitMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ViewHandler.this.mainFrame.dispose();
            }
        });
        fileMenu.add(exitMenuItem);
        return menuBar;
    }

    public void showMainFrame() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                ViewHandler.this.mainFrame.setVisible(true);
            }
        });
    }
}
