package launcher.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import launcher.contentpanel.AbstractContentPanel;
import launcher.contentpanel.ContentPanelFactory;
import launcher.indexpanel.AbstractIndexPanel;
import launcher.indexpanel.IndexPanelFactory;
import launcher.util.GraphicsUtils;
import launcher.util.IconLibrary;
import launcher.util.MessageUtils;
import launcher.util.ThreadUtils;

/**
 * The main frame for the launcher
 * 
 * @author Ramon Servadei
 * @version $Revision: 1.5 $
 * 
 */
public class MainJFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public MainJFrame() {
        super();
        GraphicsUtils.setRootFrame(this);
        GraphicsUtils.setLookAndFeelFromProperties();
        setTitle(Constants.Application.NAME);
        setIconImage(IconLibrary.ProcessIcons.RUNNING_NODES_EMPTY_ICON().getImage());
        Handler[] handlers = Logger.getLogger("").getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            Handler handler = handlers[i];
            handler.setLevel(Level.ALL);
        }
        try {
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            ContentPanelFactory cpFactory = new ContentPanelFactory();
            AbstractContentPanel contentPanel = cpFactory.createDefaultContentPanel();
            IndexPanelFactory ipFactory = new IndexPanelFactory();
            AbstractIndexPanel indexPanel = ipFactory.createDefaultIndexPanel();
            cpFactory.initialise(contentPanel);
            ipFactory.initialise(indexPanel);
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setBorder(BorderFactory.createEmptyBorder());
            splitPane.add(indexPanel);
            splitPane.add(contentPanel);
            getContentPane().add(splitPane);
            createMemoryBar();
            createMenuBar((MenuBarHandler) indexPanel);
            pack();
            setVisible(true);
        } catch (Throwable e) {
            MessageUtils.showExceptionMessage(e);
        }
    }

    private void createMemoryBar() {
        final JProgressBar memoryBar = new JProgressBar();
        memoryBar.setForeground(Color.GRAY.darker());
        memoryBar.setPreferredSize(new Dimension(100, 16));
        memoryBar.setMinimum(0);
        memoryBar.setStringPainted(true);
        memoryBar.setString("0M | 0M");
        final JButton threadCount = new JButton(" " + Application.RESOURCES.getString("Button.MainJFrame.activeThreads.text") + ":0 ");
        threadCount.setToolTipText(Application.RESOURCES.getString("Tooltip.MainJFrame.activeThreads.text"));
        threadCount.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Map<Thread, StackTraceElement[]> threadMap = Thread.getAllStackTraces();
                DefaultMutableTreeNode root = new DefaultMutableTreeNode("Threads");
                DefaultTreeModel model = new DefaultTreeModel(root);
                Set<Thread> threads = threadMap.keySet();
                for (Thread thread : threads) {
                    DefaultMutableTreeNode threadNode = new DefaultMutableTreeNode(thread.toString());
                    root.add(threadNode);
                    StackTraceElement[] stacks = threadMap.get(thread);
                    for (int i = 0; i < stacks.length; i++) {
                        StackTraceElement stackTrace = stacks[i];
                        threadNode.add(new DefaultMutableTreeNode(stackTrace.toString()));
                    }
                }
                JTree threadTree = new JTree(model);
                JPanel threadPanel = new JPanel(new BorderLayout());
                threadPanel.add(new JScrollPane(threadTree));
                JOptionPane.showMessageDialog(MainJFrame.this, threadPanel, "Thread Dump", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JPanel southBarPanel = new JPanel();
        BoxLayout box = new BoxLayout(southBarPanel, BoxLayout.LINE_AXIS);
        southBarPanel.setLayout(box);
        southBarPanel.add(threadCount);
        southBarPanel.add(Box.createHorizontalStrut(10));
        southBarPanel.add(new JLabel(Application.RESOURCES.getString("Label.MainJFrame.memoryBar.text")));
        southBarPanel.add(memoryBar);
        JPanel southBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southBar.add(southBarPanel);
        southBar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        getContentPane().add(southBar, BorderLayout.SOUTH);
        final Runnable progressBarProcessor = new Runnable() {

            public void run() {
                long free = Runtime.getRuntime().freeMemory();
                long total = Runtime.getRuntime().totalMemory();
                int max = (int) (total / 1000000f);
                int current = (int) ((total - free) / 1000000f);
                memoryBar.setMaximum(max);
                memoryBar.setValue(current);
                memoryBar.setString("" + current + "M | " + max + "M");
                threadCount.setText(" " + Application.RESOURCES.getString("Button.MainJFrame.activeThreads.text") + ":" + Thread.activeCount() + " ");
            }
        };
        final Thread memoryBarThread = new Thread(new Runnable() {

            public void run() {
                while (Application.isActive()) {
                    ThreadUtils.pause(500);
                    SwingUtilities.invokeLater(progressBarProcessor);
                }
            }
        }, "Memory bar thread");
        memoryBarThread.setDaemon(true);
        memoryBarThread.start();
    }

    private void createMenuBar(MenuBarHandler indexPanel) {
        setJMenuBar(indexPanel.getMenuBar());
    }

    /**
   * Kicks off the application
   * 
   * @param args
   */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new MainJFrame();
            }
        });
    }
}
