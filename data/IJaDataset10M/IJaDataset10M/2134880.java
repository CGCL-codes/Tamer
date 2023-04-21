package wjhk.jupload2.gui;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import wjhk.jupload2.policies.UploadPolicy;
import wjhk.jupload2.policies.UploadPolicyFactory;
import wjhk.jupload2.upload.FileUploadThread;
import wjhk.jupload2.upload.FileUploadThreadFTP;
import wjhk.jupload2.upload.FileUploadThreadHTTP;

/**
 * Overwriting the default Append class such that it scrolls to the bottom of
 * the text. The JFC doesn't always remember to do that. <BR>
 */
final class JUploadPopupMenu extends JPopupMenu implements ActionListener, ItemListener {

    /** A generated serialVersionUID */
    private static final long serialVersionUID = -5473337111643079720L;

    /**
     * Identifies the menu item that will set debug mode on or off (on means:
     * debugLevel=100)
     */
    JCheckBoxMenuItem cbMenuItemDebugOnOff = null;

    /**
     * The current upload policy.
     */
    private UploadPolicy uploadPolicy;

    JUploadPopupMenu(UploadPolicy uploadPolicy) {
        this.uploadPolicy = uploadPolicy;
        this.cbMenuItemDebugOnOff = new JCheckBoxMenuItem("Debug on");
        this.cbMenuItemDebugOnOff.setState(this.uploadPolicy.getDebugLevel() == 100);
        add(this.cbMenuItemDebugOnOff);
        this.cbMenuItemDebugOnOff.addItemListener(this);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(@SuppressWarnings("unused") ActionEvent action) {
    }

    /**
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        if (this.cbMenuItemDebugOnOff == e.getItem()) {
            this.uploadPolicy.setDebugLevel((this.cbMenuItemDebugOnOff.isSelected() ? 100 : 0));
        }
    }
}

/**
 * Main code for the applet (or frame) creation. It contains all creation for
 * necessary elements, or calls to {@link wjhk.jupload2.policies.UploadPolicy}
 * to allow easy personalization.
 * 
 * @author William JinHua Kwong
 * @version $Revision: 172 $
 */
public class JUploadPanel extends JPanel implements ActionListener, MouseListener {

    /**
     * 
     */
    private static final long serialVersionUID = -1212601012568225757L;

    private static final double gB = 1024L * 1024L * 1024L;

    private static final double mB = 1024L * 1024L;

    private static final double kB = 1024L;

    private String speedunit_gb_per_second = "Gb/s";

    private String speedunit_mb_per_second = "Mb/s";

    private String speedunit_kb_per_second = "Kb/s";

    private String speedunit_b_per_second = "b/s";

    private String timefmt_hms = "%1$dh, %2$d min. and %3$d sec.";

    private String timefmt_ms = "%1$d min. and %2$d sec.";

    private String timefmt_s = "%1$d seconds";

    private String status_msg = "JUpload %1$d%% done, Transfer rate: %2$,3.2f %3$s, ETA: %4$s";

    /** The popup menu of the applet */
    private JUploadPopupMenu jUploadPopupMenu;

    private static final int DEFAULT_TIMEOUT = 100;

    /**
     * The upload status (progressbar) gets updated every (DEFAULT_TIMEOUT *
     * PROGRESS_INTERVAL) ms.
     */
    private static final int PROGRESS_INTERVAL = 10;

    /**
     * The counter for updating the upload status. The upload status
     * (progressbar) gets updated every (DEFAULT_TIMEOUT * PROGRESS_INTERVAL)
     * ms.
     */
    private int update_counter = 0;

    private JPanel topPanel;

    private JButton browseButton, removeButton, removeAllButton;

    private JFileChooser fileChooser = null;

    private JUploadFileFilter fileFilter = null;

    private JUploadFileView fileView = null;

    private FilePanel filePanel = null;

    private JPanel progressPanel;

    private JButton uploadButton, stopButton;

    private JProgressBar progressBar = null;

    private JScrollPane jLogWindowPane = null;

    private JUploadTextArea logWindow = null;

    private boolean isLogWindowVisible = false;

    private Timer timer = null;

    private UploadPolicy uploadPolicy = null;

    protected FileUploadThread fileUploadThread = null;

    /**
     * Constructor to call, when running from outside of an applet : this is
     * taken from the version 1 of Jupload. It can be used for test (like
     * originally), but this is now useless as eclipse allow direct execution of
     * applet, for tests. It can also be used to use this code from within a
     * java application.
     * 
     * @param containerParam The container, where all GUI elements are to be
     *            created.
     * @param logWindow The log window that should already have been created.
     *            This allows putting text into it, before the effective
     *            creation of the layout.
     * @param uploadPolicyParam The current UploadPolicy. Null if a new one must
     *            be created.
     * @see UploadPolicyFactory#getUploadPolicy(wjhk.jupload2.JUploadApplet)
     */
    public JUploadPanel(@SuppressWarnings("unused") Container containerParam, JUploadTextArea logWindow, UploadPolicy uploadPolicyParam) throws Exception {
        this.logWindow = logWindow;
        this.uploadPolicy = uploadPolicyParam;
        this.jUploadPopupMenu = new JUploadPopupMenu(this.uploadPolicy);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        logWindow.addMouseListener(this);
        setupTopPanel();
        this.filePanel = (null == this.filePanel) ? new FilePanelTableImp(this, this.uploadPolicy) : this.filePanel;
        this.add((Container) this.filePanel);
        setupProgressPanel(this.uploadButton, this.progressBar, this.stopButton);
        setupLogWindow();
        try {
            this.fileChooser = new JFileChooser();
            this.fileFilter = new JUploadFileFilter(this.uploadPolicy);
            this.fileView = new JUploadFileView(this.uploadPolicy, this.fileChooser);
            this.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            this.fileChooser.setMultiSelectionEnabled(true);
            this.fileChooser.setFileView(this.fileView);
            if (this.uploadPolicy.fileFilterGetDescription() != null) {
                this.fileChooser.setFileFilter(this.fileFilter);
            }
        } catch (Exception e) {
            this.uploadPolicy.displayErr(e);
        }
    }

    private void setupTopPanel() {
        this.browseButton = new JButton(this.uploadPolicy.getString("buttonBrowse"));
        this.browseButton.setIcon(new ImageIcon(getClass().getResource("/images/explorer.gif")));
        this.browseButton.addActionListener(this);
        this.removeButton = new JButton(this.uploadPolicy.getString("buttonRemoveSelected"));
        this.removeButton.setIcon(new ImageIcon(getClass().getResource("/images/recycle.gif")));
        this.removeButton.setEnabled(false);
        this.removeButton.addActionListener(this);
        this.removeAllButton = new JButton(this.uploadPolicy.getString("buttonRemoveAll"));
        this.removeAllButton.setIcon(new ImageIcon(getClass().getResource("/images/cross.gif")));
        this.removeAllButton.setEnabled(false);
        this.removeAllButton.addActionListener(this);
        this.topPanel = this.uploadPolicy.createTopPanel(this.browseButton, this.removeButton, this.removeAllButton, this);
        this.add(this.topPanel);
    }

    private void setupProgressPanel(JButton jbUpload, JProgressBar jpbProgress, JButton jbStop) {
        this.progressPanel = new JPanel();
        if (null == jbUpload) {
            this.uploadButton = new JButton(this.uploadPolicy.getString("buttonUpload"));
            this.uploadButton.setIcon(new ImageIcon(getClass().getResource("/images/up.gif")));
        } else {
            this.uploadButton = jbUpload;
        }
        this.uploadButton.setEnabled(false);
        this.uploadButton.addActionListener(this);
        if (null == jpbProgress) {
            this.progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
            this.progressBar.setStringPainted(true);
        } else {
            this.progressBar = jpbProgress;
        }
        if (null == jbStop) {
            this.stopButton = new JButton(this.uploadPolicy.getString("buttonStop"));
            this.stopButton.setIcon(new ImageIcon(getClass().getResource("/images/cross.gif")));
        } else {
            this.stopButton = jbStop;
        }
        this.stopButton.setEnabled(false);
        this.stopButton.addActionListener(this);
        this.progressPanel = this.uploadPolicy.createProgressPanel(this.progressBar, this.uploadButton, this.stopButton, this);
        this.add(this.progressPanel);
    }

    private void setupLogWindow() {
        this.jLogWindowPane = new JScrollPane();
        this.jLogWindowPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.jLogWindowPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.jLogWindowPane.getViewport().add(this.logWindow);
        showOrHideLogWindow();
    }

    protected void addFiles(File[] f) {
        this.filePanel.addFiles(f);
        if (0 < this.filePanel.getFilesLength()) {
            this.removeButton.setEnabled(true);
            this.removeAllButton.setEnabled(true);
            this.uploadButton.setEnabled(true);
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            if ((this.update_counter++ > PROGRESS_INTERVAL) || (!this.fileUploadThread.isAlive())) {
                this.update_counter = 0;
                if (null != this.progressBar && (this.fileUploadThread.getStartTime() != 0)) {
                    long duration = (System.currentTimeMillis() - this.fileUploadThread.getStartTime()) / 1000;
                    double done = this.fileUploadThread.getUploadedLength();
                    double total = this.fileUploadThread.getTotalLength();
                    double percent;
                    double cps;
                    long remaining;
                    String eta;
                    try {
                        percent = 100.0 * done / total;
                    } catch (ArithmeticException e1) {
                        percent = 100;
                    }
                    try {
                        cps = done / duration;
                    } catch (ArithmeticException e1) {
                        cps = done;
                    }
                    try {
                        remaining = (long) ((total - done) / cps);
                        if (remaining > 3600) {
                            eta = String.format(this.timefmt_hms, new Long(remaining / 3600), new Long((remaining / 60) % 60), new Long(remaining % 60));
                        } else if (remaining > 60) {
                            eta = String.format(this.timefmt_ms, new Long(remaining / 60), new Long(remaining % 60));
                        } else eta = String.format(this.timefmt_s, new Long(remaining));
                    } catch (ArithmeticException e1) {
                        eta = "unknown";
                    }
                    this.progressBar.setValue((int) percent);
                    String unit = this.speedunit_b_per_second;
                    if (cps >= gB) {
                        cps /= gB;
                        unit = this.speedunit_gb_per_second;
                    } else if (cps >= mB) {
                        cps /= mB;
                        unit = this.speedunit_mb_per_second;
                    } else if (cps >= kB) {
                        cps /= kB;
                        unit = this.speedunit_kb_per_second;
                    }
                    this.uploadPolicy.getApplet().getAppletContext().showStatus(String.format(this.status_msg, new Integer((int) percent), new Double(cps), unit, eta));
                }
            }
            if (!this.fileUploadThread.isAlive()) {
                this.uploadPolicy.displayDebug("JUploadPanel: after !fileUploadThread.isAlive()", 60);
                this.timer.stop();
                String svrRet = this.fileUploadThread.getResponseMsg();
                Exception ex = this.fileUploadThread.getException();
                this.stopButton.setEnabled(false);
                this.browseButton.setEnabled(true);
                this.fileUploadThread.close();
                this.fileUploadThread = null;
                this.uploadPolicy.afterUpload(ex, svrRet);
                boolean haveFiles = (0 < this.filePanel.getFilesLength());
                this.uploadButton.setEnabled(haveFiles);
                this.removeButton.setEnabled(haveFiles);
                this.removeAllButton.setEnabled(haveFiles);
                this.uploadPolicy.getApplet().getAppletContext().showStatus("");
            }
            return;
        }
        this.uploadPolicy.displayDebug("Action : " + e.getActionCommand(), 1);
        if (e.getActionCommand() == this.browseButton.getActionCommand()) {
            if (null != this.fileChooser) {
                try {
                    int ret = this.fileChooser.showOpenDialog(new Frame());
                    if (JFileChooser.APPROVE_OPTION == ret) addFiles(this.fileChooser.getSelectedFiles());
                    this.fileView.shutdownNow();
                } catch (Exception ex) {
                    this.uploadPolicy.displayErr(ex);
                }
            }
        } else if (e.getActionCommand() == this.removeButton.getActionCommand()) {
            this.filePanel.removeSelected();
            if (0 >= this.filePanel.getFilesLength()) {
                this.removeButton.setEnabled(false);
                this.removeAllButton.setEnabled(false);
                this.uploadButton.setEnabled(false);
            }
        } else if (e.getActionCommand() == this.removeAllButton.getActionCommand()) {
            this.filePanel.removeAll();
            this.removeButton.setEnabled(false);
            this.removeAllButton.setEnabled(false);
            this.uploadButton.setEnabled(false);
        } else if (e.getActionCommand() == this.uploadButton.getActionCommand()) {
            if (this.uploadPolicy.isUploadReady()) {
                this.uploadPolicy.beforeUpload();
                this.browseButton.setEnabled(false);
                this.removeButton.setEnabled(false);
                this.removeAllButton.setEnabled(false);
                this.uploadButton.setEnabled(false);
                this.stopButton.setEnabled(true);
                if (this.uploadPolicy.getPostURL().substring(0, 4).equals("ftp:")) {
                    this.fileUploadThread = new FileUploadThreadFTP(this.filePanel.getFiles(), this.uploadPolicy, this.progressBar);
                } else {
                    this.fileUploadThread = new FileUploadThreadHTTP(this.filePanel.getFiles(), this.uploadPolicy, this.progressBar);
                }
                this.fileUploadThread.start();
                this.timer = new Timer(DEFAULT_TIMEOUT, this);
                this.timer.start();
                this.uploadPolicy.displayDebug("Timer started", 60);
            }
        } else if (e.getActionCommand() == this.stopButton.getActionCommand()) {
            this.fileUploadThread.stopUpload();
        }
        this.filePanel.focusTable();
    }

    /**
     * @return the uploadPolicy
     */
    public UploadPolicy getUploadPolicy() {
        return this.uploadPolicy;
    }

    /**
     * @return the filePanel
     */
    public FilePanel getFilePanel() {
        return this.filePanel;
    }

    /**
     * This methods show or hides the logWindow, depending on the following
     * applet parameters. The following conditions must be met, to hide the log
     * window: <DIR>
     * <LI>showLogWindow (must be False)
     * <LI>debugLevel (must be 0 or less) </DIR>
     */
    public void showOrHideLogWindow() {
        if (this.uploadPolicy.getShowLogWindow() || this.uploadPolicy.getDebugLevel() > 0) {
            if (!this.isLogWindowVisible) {
                add(this.jLogWindowPane, -1);
                this.isLogWindowVisible = true;
                validate();
            }
        } else {
            if (this.isLogWindowVisible) {
                remove(this.jLogWindowPane);
                this.isLogWindowVisible = false;
                validate();
            }
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        maybeOpenPopupMenu(mouseEvent);
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent mouseEvent) {
        maybeOpenPopupMenu(mouseEvent);
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent mouseEvent) {
        maybeOpenPopupMenu(mouseEvent);
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent mouseEvent) {
        maybeOpenPopupMenu(mouseEvent);
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent mouseEvent) {
        maybeOpenPopupMenu(mouseEvent);
    }

    /**
     * This method opens the popup menu, if the mouseEvent is relevant. In this
     * case it returns true. Otherwise, it does nothing and returns false.
     * 
     * @param mouseEvent The triggered mouse event.
     * @return true if the popup menu was opened, false otherwise.
     */
    boolean maybeOpenPopupMenu(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger() && ((mouseEvent.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK)) {
            if (this.jUploadPopupMenu != null) {
                this.jUploadPopupMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                return true;
            }
        }
        return false;
    }
}
