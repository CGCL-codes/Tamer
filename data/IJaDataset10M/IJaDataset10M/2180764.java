package aurora.hwc.config;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.Point;
import java.awt.event.*;
import aurora.*;
import aurora.hwc.*;

/**
 * Implementation of action pane split vertically into
 * a desktop and table panes.
 * @author Alex Kurzhanskiy
 * @version $Id: ActionPane.java 38 2010-02-08 22:59:00Z akurzhan $
 */
public final class ActionPane extends JPanel {

    private static final long serialVersionUID = -7827597418764664986L;

    private TreePane treePane;

    private ContainerHWC mySystem;

    private JDesktopPane desktopPane = new JDesktopPane();

    private JTabbedPane tablePane = new JTabbedPane();

    private errorsTableModel errTM = new errorsTableModel();

    private Vector<ErrorConfiguration> cfgErrors = new Vector<ErrorConfiguration>();

    protected JTextArea console = new JTextArea();

    protected BufferedReader iis;

    protected boolean stopped = false;

    public ActionPane() {
    }

    public ActionPane(TreePane tpane, ContainerHWC ctnr) {
        super(new GridLayout(1, 0));
        treePane = tpane;
        mySystem = ctnr;
        if ((mySystem != null) && (mySystem.getMyNetwork() != null)) cfgErrors = mySystem.getMyNetwork().getConfigurationErrors();
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(desktopPane);
        splitPane.setBottomComponent(tablePane);
        Dimension minimumSize = new Dimension(50, 50);
        desktopPane.setMinimumSize(minimumSize);
        desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        desktopPane.setBackground(Color.GRAY);
        tablePane.setMinimumSize(minimumSize);
        tablePane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        final JTable ferrors = new JTable(errTM);
        ferrors.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        ferrors.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = ferrors.rowAtPoint(new Point(e.getX(), e.getY()));
                    Object src = cfgErrors.get(row).getSource();
                    if (src instanceof AbstractNetworkElement) {
                        Vector<AbstractNetworkElement> nelist = new Vector<AbstractNetworkElement>();
                        nelist.add((AbstractNetworkElement) src);
                        treePane.actionSelected(nelist, true, true);
                    }
                }
                return;
            }
        });
        console.setFont(new Font("Helvetica", Font.PLAIN, 11));
        tablePane.addTab("Console", new JScrollPane(console));
        tablePane.addTab("Errors", new JScrollPane(ferrors));
        catchIO();
        splitPane.setDividerLocation((int) Math.round(0.75 * mySystem.getMySettings().getWindowSize().getHeight()));
        add(splitPane);
    }

    public void catchIO() {
        try {
            PipedInputStream is = new PipedInputStream();
            PipedOutputStream os = new PipedOutputStream(is);
            iis = new BufferedReader(new InputStreamReader(is, "ISO8859_1"));
            mySystem.getMySettings().setOutputStream(new PrintStream(os));
            System.setOut(mySystem.getMySettings().getOutputStream());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
        }
        new Thread() {

            public void run() {
                try {
                    String line;
                    while (((line = iis.readLine()) != null) && (!stopped)) {
                        console.append(line + "\n");
                        console.setCaretPosition(console.getText().length());
                    }
                } catch (Exception e) {
                    catchIO();
                }
            }
        }.start();
        return;
    }

    /**
     * Returns the desktop subframe.
     */
    JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    /**
     * Stops running threads.
     */
    public void stop() {
        stopped = true;
        System.out.println("Cleanup!");
    }

    /**
     * Updates error table.
     */
    public void updateErrorTable() {
        errTM.fireTableDataChanged();
        if (!cfgErrors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Errors in Configuration!", "", JOptionPane.ERROR_MESSAGE);
            tablePane.setSelectedIndex(1);
            tablePane.setTitleAt(1, "Errors (" + cfgErrors.size() + ")");
        } else {
            JOptionPane.showMessageDialog(this, "No Errors!", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
	 * Class needed for displaying table configuration errors.
	 */
    private class errorsTableModel extends AbstractTableModel {

        private static final long serialVersionUID = -5935829659775765273L;

        public String getColumnName(int col) {
            String buf = null;
            switch(col) {
                case 0:
                    buf = "Network Element";
                    break;
                case 1:
                    buf = "Error Message";
                    break;
            }
            return buf;
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return cfgErrors.size();
        }

        public Object getValueAt(int row, int column) {
            if ((row < 0) || (row >= cfgErrors.size()) || (column < 0) || (column > 1)) return null;
            if (column == 0) return cfgErrors.get(row).getSource();
            return cfgErrors.get(row).getMessage();
        }
    }
}
