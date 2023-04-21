package cvosteen.sqltool.gui;

import cvosteen.sqltool.database.*;
import cvosteen.sqltool.gui.components.*;
import cvosteen.sqltool.memory.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class DatabaseManagerFrame extends JFrame implements DatabasePanelParent {

    private String name;

    private String version;

    private DatabaseManager databaseManager;

    private JTabbedPane tabbedPane;

    private PageFormat pageFormat = new PageFormat();

    private PageSetup pageSetup = new PageSetup();

    public DatabaseManagerFrame(DatabaseManager databaseManager, String name, String version) {
        super(name + " v" + version);
        this.name = name;
        this.version = version;
        this.databaseManager = databaseManager;
        createComponents();
    }

    public void createComponents() {
        setMinimumSize(new Dimension(620, 460));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenuItem printItem = new JMenuItem("Print...");
        printItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = tabbedPane.getSelectedIndex();
                if (index != -1) {
                    printRequested((DatabasePanel) tabbedPane.getComponentAt(index));
                }
            }
        });
        fileMenu.add(printItem);
        fileMenu.addSeparator();
        JMenuItem printSetupItem = new JMenuItem("Print Setup...");
        printSetupItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doPrintSetup();
            }
        });
        fileMenu.add(printSetupItem);
        JMenuItem pageSetupItem = new JMenuItem("Page Setup...");
        pageSetupItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doPageSetup();
            }
        });
        fileMenu.add(pageSetupItem);
        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                shutdown();
            }
        });
        fileMenu.add(exitItem);
        JMenu databaseMenu = new JMenu("Database");
        menuBar.add(databaseMenu);
        JMenuItem connectItem = new JMenuItem("Connect...");
        connectItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });
        databaseMenu.add(connectItem);
        JMenuItem disconnectItem = new JMenuItem("Disconnect");
        disconnectItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });
        databaseMenu.add(disconnectItem);
        JMenu transactionMenu = new JMenu("Transaction");
        menuBar.add(transactionMenu);
        JMenuItem commitItem = new JMenuItem("Commit");
        commitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                commit();
            }
        });
        transactionMenu.add(commitItem);
        JMenuItem rollbackItem = new JMenuItem("Rollback");
        rollbackItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                rollback();
            }
        });
        transactionMenu.add(rollbackItem);
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new AboutDialog(null, name, version);
            }
        });
        helpMenu.add(aboutItem);
        setJMenuBar(menuBar);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        tabbedPane = new JTabbedPane();
        panel.add(tabbedPane);
        getContentPane().add(panel);
        pack();
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenDim.width - getWidth()) / 2, (screenDim.height - getHeight()) / 2);
        setVisible(true);
    }

    /**
	 * Allow the user to modify the current PageSetup.
	 */
    private void doPageSetup() {
        PageSetupDialog dialog = new PageSetupDialog(this, pageSetup);
        pageSetup = dialog.getResponse();
        dialog.dispose();
    }

    /**
	 * Allow the user to modify the current PageFormat.
	 */
    private void doPrintSetup() {
        PrinterJob job = PrinterJob.getPrinterJob();
        pageFormat = job.pageDialog(pageFormat);
    }

    /**
	 * Bring up the DatabaseManagerDialog for the user to connect to
	 * one of the databases.
	 * Creates a new DatabasePanel for the selected Database and
	 * adds it to the Tabbed Pane.
	 */
    private void connect() {
        System.gc();
        LowMemoryMonitor monitor = LowMemoryMonitor.getInstance();
        if (monitor.isMemoryLow()) {
            JOptionPane.showMessageDialog(this, "Not enough memory to connect.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ResponseGetter<Database> dialog = new DatabaseManagerDialog(this, databaseManager);
        Database database = dialog.getResponse();
        if (database != null) {
            try {
                JComponent newPanel = new ConcreteDatabasePanel(this, database);
                tabbedPane.addTab(database.getName(), newPanel);
                tabbedPane.setSelectedComponent(newPanel);
            } catch (SQLException f) {
                f.printStackTrace();
                JOptionPane.showMessageDialog(this, f.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException f) {
                f.printStackTrace();
                JOptionPane.showMessageDialog(this, f.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
	 * Tells the selected DatabasePanel to shutdown().
	 */
    private void disconnect() {
        int index = tabbedPane.getSelectedIndex();
        if (index != -1) {
            ((DatabasePanel) tabbedPane.getComponentAt(index)).shutdown();
            tabbedPane.remove(index);
        }
    }

    /**
	 * Tells the selected DatabasePanel to commit().
	 */
    private void commit() {
        DatabasePanel activePanel = (DatabasePanel) tabbedPane.getSelectedComponent();
        if (activePanel != null) activePanel.commit();
    }

    /**
	 * Tells the selected DatabasePanel to rollback().
	 */
    private void rollback() {
        DatabasePanel activePanel = (DatabasePanel) tabbedPane.getSelectedComponent();
        if (activePanel != null) activePanel.rollback();
    }

    /**
	 * Called when the program is exiting.
	 * All open DatabasePanels will be asked to shutdown.
	 */
    private void shutdown() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) ((DatabasePanel) tabbedPane.getComponentAt(i)).shutdown();
        dispose();
    }

    /**
	 * When requested by a DatabasePanel, all databases are saved to disk.
	 */
    public void saveRequested(DatabasePanel databasePanel) {
        try {
            databaseManager.save();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
	 * When requested by a DatabasePanel, the component to be printed is
	 * retrieved from that DatabasePanel and printed.
	 */
    public void printRequested(DatabasePanel databasePanel) {
        System.gc();
        LowMemoryMonitor monitor = LowMemoryMonitor.getInstance();
        if (monitor.isMemoryLow()) {
            JOptionPane.showMessageDialog(this, "Not enough memory to print.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        PrinterJob job = PrinterJob.getPrinterJob();
        JTablePrintable p = (JTablePrintable) databasePanel.getPrintableComponent();
        p.setPageSetup(pageSetup);
        job.setPrintable(p, pageFormat);
        job.setJobName(name);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
