package de.kout.wlFxp.view;

import de.kout.wlFxp.MyFile;
import de.kout.wlFxp.Transfer;
import de.kout.wlFxp.Utilities;
import de.kout.wlFxp.wlFxp;
import de.kout.wlFxp.ftp.FtpServer;
import de.kout.wlFxp.ftp.FtpSession;
import de.kout.wlFxp.interfaces.Session;
import de.kout.wlFxp.interfaces.wlFrame;
import de.kout.wlFxp.interfaces.wlPanel;
import de.kout.wlFxp.view.dialogs.ConnectDialog;
import de.kout.wlFxp.view.dialogs.ResumeDialog;
import de.kout.wlFxp.view.list.MainList;
import de.kout.wlFxp.view.list.MainListModel;
import de.kout.wlFxp.view.table.MainTable;
import de.kout.wlFxp.view.table.MainTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;

/**
 * the panel that holds the toolbar, table and label
 *
 * @author Alexander Kout
 *
 * 30. März 2002
 */
public class MainPanel extends JPanel implements wlPanel, FocusListener, ComponentListener, ActionListener {

    /** Description of the Field */
    public static final int LOCAL = 0;

    /** Description of the Field */
    public static final int FTP = 1;

    /** Description of the Field */
    public MainFrame frame;

    /** Description of the Field */
    public MainPanel otherPanel;

    /** Description of the Field */
    public int mode;

    /** Description of the Field */
    public int position;

    View view;

    JScrollPane scrollPane;

    MainToolBar toolbar;

    JLabel dirLabel;

    JLabel sizeLabel;

    long dirSize;

    /** Description of the Field */
    public String color;

    /** Description of the Field */
    public String dir;

    /** Description of the Field */
    public String ftpDir;

    /** Description of the Field */
    public FtpSession ftpSession;

    /** Description of the Field */
    public Vector<MyFile> files;

    MyFile renameFile;

    ViewContextMenu contextMenu;

    boolean isSelected;

    boolean SwingWorkerRunning;

    /**
         * Constructor for the MainPanel object
         * 
         * @param frame
         *            parent frame
         * @param mode
         *            the mode this panel initially has
         * @param color
         *            color for text in statusArea
         * @param position
         *            Description of the Parameter
         */
    public MainPanel(MainFrame frame, int mode, String color, int position) {
        super();
        this.frame = frame;
        this.mode = mode;
        this.color = color;
        this.position = position;
        buildGui();
    }

    /**
         * Constructor for the MainPanel object
         * 
         * @param frame
         *            parent frame
         * @param mode
         *            the mode this panel initially has
         * @param panel
         *            the other panel
         * @param color
         *            color for text in statusbar
         * @param position
         *            Description of the Parameter
         */
    public MainPanel(MainFrame frame, int mode, MainPanel panel, String color, int position) {
        super();
        this.frame = frame;
        this.mode = mode;
        otherPanel = panel;
        otherPanel.setOtherPanel(this);
        this.color = color;
        this.position = position;
        buildGui();
    }

    /**
	 * Sets otherPanel attribute of the MainPanel object
	 *
	 * @param panel new otherPanel value
	 */
    private void setOtherPanel(MainPanel panel) {
        otherPanel = panel;
    }

    /**
	 * created label, toolbar and list
	 */
    private void buildGui() {
        setLayout(new BorderLayout());
        sizeLabel = new JLabel(" ");
        sizeLabel.setHorizontalAlignment(JLabel.CENTER);
        dirLabel = new JLabel(" ");
        dirLabel.setHorizontalAlignment(JLabel.CENTER);
        String file = new File(".").getAbsolutePath();
        dir = file.substring(0, file.length() - 2);
        ftpSession = new FtpSession(this);
        toolbar = new MainToolBar(this);
        contextMenu = new ViewContextMenu(this);
        view = new MainTable(this);
        if (mode == LOCAL) {
            setDir(dir);
        } else {
            setDir(ftpDir);
        }
        scrollPane = new JScrollPane((MainTable) view);
        scrollPane.addComponentListener(this);
        add(toolbar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(dirLabel, BorderLayout.SOUTH);
    }

    /**
	 * switches the panel between local- and ftp-mode
	 */
    public void switchIt() {
        mode = (mode + 1) % 2;
        toolbar.updateView();
        contextMenu = new ViewContextMenu(this);
        updateView();
    }

    /**
	 * switches view to JList
	 */
    protected void viewAsJList() {
        remove(scrollPane);
        scrollPane.removeAll();
        view = new MainList(this);
        if (mode == LOCAL) {
            setDir(dir);
        } else {
            setDir(ftpDir);
        }
        scrollPane = new JScrollPane((MainList) view);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
	 * switches view to JTable
	 */
    protected void viewAsJTable() {
        remove(scrollPane);
        scrollPane.removeAll();
        view = new MainTable(this);
        if (mode == LOCAL) {
            setDir(dir);
        } else {
            setDir(ftpDir);
        }
        scrollPane = new JScrollPane((MainTable) view);
        scrollPane.addComponentListener(this);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
	 * deletes the selected files and calls updateView
	 *
	 * @param e Description of Parameter
	 */
    protected void delete(ActionEvent e) {
        int[] rows = view.getSelectedIndices();
        int failed = 0;
        Vector<MyFile> vDelete = new Vector<MyFile>(100);
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] > 0) {
                if (mode == LOCAL) {
                    MyFile entry = (MyFile) view.getElementAt(rows[i] - i + failed);
                    if (entry.isFile()) {
                        if (!entry.delete()) {
                            frame.statusArea.append("delete " + entry.getName() + " failed\n", "red");
                            failed++;
                        } else {
                            view.removeElementAt(rows[i] - i - 1 + failed);
                        }
                    } else if (entry.isDirectory()) {
                        if (!recursiveDelete(entry)) {
                            frame.statusArea.append("delete " + entry.getName() + " failed\n", "red");
                            failed++;
                        } else {
                            view.removeElementAt(rows[i] - i - 1 + failed);
                        }
                    }
                } else if (mode == FTP) {
                    vDelete.addElement((MyFile) view.getElementAt(rows[i]));
                }
            }
        }
        if (mode == LOCAL) {
            lightUpdateView();
        } else {
            ftpSession.delete(vDelete);
        }
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param f DOCUMENT ME!
	 */
    public void removeElement(MyFile f) {
        while (SwingWorkerRunning) try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }
        view.removeElement(f);
    }

    /**
	 * recursive delete method
	 *
	 * @param dir Description of the Parameter
	 *
	 * @return DOCUMENT ME!
	 */
    protected boolean recursiveDelete(MyFile dir) {
        MyFile[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                recursiveDelete(files[i]);
            } else {
                files[i].delete();
            }
        }
        if (dir.delete()) {
            return true;
        }
        return false;
    }

    /**
	 * Description of the Method
	 */
    protected void abort() {
        ftpSession.abort();
    }

    /**
	 * Description of the Method
	 *
	 * @param filename Description of Parameter
	 */
    public void makeDir(String filename) {
        if (mode == LOCAL) {
            (new File(dir + File.separator + filename)).mkdir();
            updateView();
        } else if (mode == FTP) {
            ftpSession.makeDir(filename);
        }
    }

    /**
	 * Renames one or more files to a target file or directory.
	 *
	 * @param s Target file or directory. Must be a directory if multiple files
	 * 		  are selected.
	 */
    public void rename(String s) {
        if (mode == LOCAL) {
            File t = new File(renameFile.getAbsolutePath());
            t.renameTo(new File(renameFile.getAbsolutePath().substring(0, renameFile.getAbsolutePath().lastIndexOf(File.separator) + 1) + s));
            updateView();
        } else if (mode == FTP) {
            int[] rows = view.getSelectedIndices();
            for (int i = 0; i < rows.length; i++) {
                MyFile myRenameFile = (MyFile) view.getElementAt(rows[i]);
                ftpSession.rename(myRenameFile.getName(), s);
            }
            ftpSession.list();
        }
    }

    /**
	 * Add selected files to the queue
	 */
    protected void addTransfers() {
        String destDir = "";
        FtpServer s1 = null;
        FtpServer s2 = null;
        if (otherPanel.mode == LOCAL) {
            destDir = otherPanel.dir;
            if (!destDir.endsWith("\\")) {
                destDir += File.separator;
            }
            if (mode == FTP) {
                s1 = ftpSession.currentServer;
            }
        } else {
            destDir = otherPanel.ftpDir;
            if (!destDir.endsWith("/")) {
                destDir += "/";
            }
            s2 = otherPanel.ftpSession.currentServer;
            if (mode == FTP) {
                s1 = ftpSession.currentServer;
            }
        }
        int[] rows = view.getSelectedIndices();
        Vector<MyFile> tmp_files = new Vector<MyFile>(100);
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] != 0) {
                tmp_files.addElement((MyFile) view.getElementAt(rows[i]));
            }
        }
        Utilities.sortFiles(tmp_files, "Name", true, wlFxp.getConfig().getPrioList());
        for (int i = 0; i < tmp_files.size(); i++) {
            MyFile tmp = new MyFile(((MyFile) tmp_files.elementAt(i)).getName());
            if (otherPanel.mode == LOCAL) {
                tmp.setFtpMode(false);
            }
            tmp.setAbsolutePath(destDir + tmp.getName());
            frame.queueList.addElement(new Transfer((MyFile) tmp_files.elementAt(i), tmp, mode, otherPanel.mode, position, s1, s2));
        }
        frame.queueList.updateView();
    }

    /**
	 * this method sets the var dir and calls updateView()
	 *
	 * @param dir The new dir value
	 */
    public void setDir(String dir) {
        if (mode == LOCAL) {
            if (new File(dir).canRead()) {
                this.dir = dir;
                updateView();
            } else {
                frame.statusArea.append("cannot change to " + dir + "\n", color);
            }
        } else if (mode == FTP) {
            ftpDir = dir;
            ftpSession.changeDir(ftpDir);
        }
        if ((scrollPane != null) && (mode != FTP)) {
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
        }
    }

    /**
	 * DOCUMENT ME!
	 */
    public void lightUpdateView() {
        Runnable update = new Runnable() {

            public void run() {
                SwingWorkerRunning = true;
                if (view instanceof MainTable) {
                    MainTableModel mtm = (MainTableModel) view.getOModel();
                    mtm.fireTableDataChanged();
                } else if (view instanceof MainList) {
                    ((MainList) view).repaint();
                    scrollPane.revalidate();
                    scrollPane.repaint();
                }
                SwingWorkerRunning = false;
            }
        };
        SwingUtilities.invokeLater(update);
    }

    /**
	 * method lists files and creates new Tablemodel with it if in ftp
	 * mode it calls ftpSession.list()
	 */
    public void updateView() {
        if (mode == LOCAL) {
            files = parseList();
            Runnable update = new Runnable() {

                public void run() {
                    SwingWorkerRunning = true;
                    if (view instanceof MainTable) {
                        MainTableModel mtm = (MainTableModel) view.getOModel();
                        mtm.setFiles(files, wlFxp.getConfig().getShowHidden());
                    } else if (view instanceof MainList) {
                        MainListModel mlm = (MainListModel) view.getOModel();
                        mlm.setFiles(files, wlFxp.getConfig().getShowHidden());
                    }
                    dirLabel.setText(dir + " - " + Utilities.humanReadable(dirSize));
                    toolbar.setDir(dir);
                    view.setSelectedIndex(0);
                    SwingWorkerRunning = false;
                }
            };
            SwingUtilities.invokeLater(update);
        } else if (mode == 1) {
            ftpSession.list();
        }
    }

    /**
	 * extra method for this so that the ViewModel has no problems with
	 * the SwingWorker
	 *
	 * @return Description of the Return Value
	 */
    private Vector<MyFile> parseList() {
        dirSize = 0;
        File file = new File(dir);
        String[] list = file.list();
        Vector<MyFile> files = new Vector<MyFile>(list.length, 100);
        MyFile ftpfile;
        for (int i = 0; i < list.length; i++) {
            ftpfile = new MyFile(list[i]);
            files.addElement(ftpfile);
            File f = new File(dir + File.separator + list[i]);
            ftpfile.setSize(f.length());
            String first = (f.isFile()) ? "-" : "d";
            String sec = (f.canRead()) ? "r" : "-";
            String third = (f.canWrite()) ? "w" : "-";
            ftpfile.setMode(first + sec + third);
            ftpfile.setFtpMode(false);
            ftpfile.setAbsolutePath(dir + File.separator + ftpfile.getName());
            ftpfile.setDate(Utilities.parseDate(f.lastModified()));
        }
        return files;
    }

    /**
	 * here the new MainListModel is created and label and url field
	 * updated
	 *
	 * @param output Description of Parameter
	 */
    public void updateFtpView(String output) {
        Utilities.print("updateView()\n");
        dirSize = 0;
        files = Utilities.parseList(output, ftpDir);
        Runnable update = new Runnable() {

            public void run() {
                SwingWorkerRunning = true;
                if (view instanceof MainTable) {
                    MainTableModel mtm = (MainTableModel) view.getOModel();
                    mtm.setFiles(files, wlFxp.getConfig().getShowHidden());
                } else if (view instanceof MainList) {
                    MainListModel mlm = (MainListModel) view.getOModel();
                    mlm.setFiles(files, wlFxp.getConfig().getShowHidden());
                }
                if ((ftpDir != null) && !ftpDir.equals(" ")) {
                    dirLabel.setText(ftpDir + " - " + Utilities.humanReadable(dirSize));
                } else {
                    dirLabel.setText(" ");
                }
                toolbar.setDir(ftpDir);
                scrollPane.getViewport().setViewPosition(new Point(0, 0));
                if (ftpSession.connected()) {
                    view.setSelectedIndex(0);
                }
                SwingWorkerRunning = false;
            }
        };
        SwingUtilities.invokeLater(update);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param file DOCUMENT ME!
	 */
    public void addElement(MyFile file) {
        while (SwingWorkerRunning) try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }
        view.addElement(file);
    }

    /**
	 * Gets the dir attribute of the MainPanel object
	 *
	 * @return The dir value
	 */
    public String getDir() {
        if (mode == LOCAL) {
            return dir;
        } else {
            return ftpDir;
        }
    }

    /**
	 * Sets the ftpDir attribute of the MainPanel object
	 *
	 * @param dir The new ftpDir value
	 */
    public void setFtpDir(String dir) {
        ftpDir = dir;
    }

    /**
	 * Gets the frame attribute of the MainPanel object
	 *
	 * @return The frame value
	 */
    public wlFrame getFrame() {
        return frame;
    }

    /**
	 * Gets the color attribute of the MainPanel object
	 *
	 * @return The color value
	 */
    public String getColor() {
        return color;
    }

    /**
	 * Gets the files attribute of the MainPanel object
	 *
	 * @return The files value
	 */
    public Vector<MyFile> getFiles() {
        while (SwingWorkerRunning) try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }
        Utilities.sortFiles(files, "Name", true, wlFxp.getConfig().getPrioList());
        return files;
    }

    /**
	 * Gets the otherPanel attribute of the MainPanel object
	 *
	 * @return The otherPanel value
	 */
    public wlPanel getOtherPanel() {
        return otherPanel;
    }

    /**
	 * Gets the mode attribute of the MainPanel object
	 *
	 * @return The mode value
	 */
    public int getMode() {
        return mode;
    }

    /**
	 * Gets the position attribute of the MainPanel object
	 *
	 * @return The position value
	 */
    public int getPosition() {
        return position;
    }

    /**
	 * Gets the ftpSession attribute of the MainPanel object
	 *
	 * @return The ftpSession value
	 */
    public Session getFtpSession() {
        return ftpSession;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public long dirSize() {
        return dirSize;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param d DOCUMENT ME!
	 */
    public void dirSize(long d) {
        dirSize = d;
    }

    /**
	 * Description of the Method
	 *
	 * @param t Description of the Parameter
	 */
    public void newResumeDialog(Transfer t) {
        new ResumeDialog(this, t);
    }

    /**
	 * Sets the selected attribute of the MainPanel object
	 *
	 * @param b The new selected value
	 */
    public void setSelected(boolean b) {
        if (b) {
            dirLabel.setBackground(Color.yellow);
            isSelected = true;
            dirLabel.setOpaque(true);
        } else {
            dirLabel.setBackground(Color.black);
            isSelected = false;
            dirLabel.setOpaque(false);
        }
    }

    /**
	 * Description of the Method
	 *
	 * @param event Description of the Parameter
	 */
    public void focusGained(FocusEvent event) {
        setSelected(true);
        otherPanel.setSelected(false);
    }

    /**
	 * Description of the Method
	 *
	 * @param event Description of the Parameter
	 */
    public void focusLost(FocusEvent event) {
    }

    /**
	 * Description of the Method
	 *
	 * @param e Description of the Parameter
	 */
    public void componentHidden(ComponentEvent e) {
    }

    /**
	 * Description of the Method
	 *
	 * @param e Description of the Parameter
	 */
    public void componentMoved(ComponentEvent e) {
    }

    /**
	 * Description of the Method
	 *
	 * @param e Description of the Parameter
	 */
    public void componentResized(ComponentEvent e) {
        Dimension d = scrollPane.getViewport().getViewRect().getSize();
        if (view instanceof MainTable && (d.getWidth() > ((MainTable) view).getWidth())) {
            MainTable t = (MainTable) view;
            TableColumnModel cm = t.getColumnModel();
            int a = cm.getColumn(1).getWidth();
            int b = cm.getColumn(2).getWidth();
            int c = cm.getColumn(3).getWidth();
            cm.getColumn(0).setPreferredWidth(new Double(d.getWidth()).intValue() - a - b - c);
        }
    }

    /**
	 * Description of the Method
	 *
	 * @param e Description of the Parameter
	 */
    public void componentShown(ComponentEvent e) {
    }

    /**
	 * Description of the Method
	 */
    public void open() {
        if (mode == LOCAL) {
            String file = dir + File.separator + ((MyFile) view.getElementAt(view.getSelectedIndex())).getName();
            if (new File(file).isFile()) {
                new Editor(file, frame, null);
            }
        }
    }

    /**
	 * Description of the Method
	 */
    public void updateCommandsMenu() {
        contextMenu = new ViewContextMenu(this);
    }

    /**
	 * Description of the Method
	 *
	 * @param e Description of the Parameter
	 */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Connect")) {
            new ConnectDialog(this, "Connect", false);
        }
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public JScrollPane scrollPane() {
        return scrollPane;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public View view() {
        return view;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public boolean isSelected() {
        return isSelected;
    }

    public void setEnabled(boolean b) {
        if (view instanceof MainTable) ((MainTable) view).setEnabled(b); else ((MainList) view).setEnabled(b);
        toolbar.url.setEnabled(b);
        lightUpdateView();
    }
}
