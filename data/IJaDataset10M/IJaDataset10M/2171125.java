package fi.hip.gb.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.common.CoGProperties;
import org.globus.tools.proxy.GridProxyInit;
import fi.hip.gb.client.ui.AgentProgressIndicator;
import fi.hip.gb.client.ui.JobListItem;
import fi.hip.gb.client.ui.Utils;
import fi.hip.gb.client.ui.Window;
import fi.hip.gb.client.ui.myproxy.MyProxyGUI;
import fi.hip.gb.core.Config;
import fi.hip.gb.core.JobAttachment;
import fi.hip.gb.core.SessionHandler;
import fi.hip.gb.core.SessionListener;
import fi.hip.gb.core.Storage;
import fi.hip.gb.core.WorkDescription;
import fi.hip.gb.core.WorkResult;
import fi.hip.gb.core.WorkStatus;
import fi.hip.gb.net.ChangeListener;
import fi.hip.gb.net.ComputingInterface;
import fi.hip.gb.net.ServiceListener;
import fi.hip.gb.net.discovery.DiscoveryPacket;
import fi.hip.gb.utils.ArrayUtils;
import fi.hip.gb.utils.FileUtils;
import fi.hip.gb.utils.GSIUtils;
import fi.hip.gb.utils.NetUtils;
import fi.hip.gb.utils.TextUtils;

/**
 * The default user interface for main program and showing jobs stored inside
 * it.
 * <p>
 * On the left is a list of all existing jobs. Selected job is shown detailed on
 * the right, where are progressbars showing status for each remote location.
 * After processes are completed, there is a list of result files that are
 * available.
 * <p>
 * All job handlers (Client) are stored inside Main-class. This class is only
 * the user interface to handle jobs, their status, results and deletions.
 * 
 * @author Juho Karppinen
 */
public class DefaultUI extends JFrame implements ServiceListener, SessionListener, ChangeListener {

    private static final long serialVersionUID = 1L;

    /** currently selected job */
    private Long selectedJob;

    /** shows a list of discovered servers */
    private JComboBox servers;

    /** selected observer class name */
    private String observerClass;

    /** list of the jobs found from server */
    private JList jobsList;

    /** common listener for swing components */
    private DefaultUIListener listener;

    /** panel which shows the process of the job */
    private JComponent detailPane;

    /** split pane contains joblist and current jobinfo */
    private JSplitPane splitPane;

    /** label which shows the status of the proxy */
    private JLabel proxyInfo;

    public static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

    public static final Cursor WAIT_CURSOR = new Cursor(Cursor.WAIT_CURSOR);

    private static Log log = LogFactory.getLog(DefaultUI.class);

    /**
     * Main program isn't initialized yet at this point. So you can't use any
     * Configs or Loggers.
     */
    public DefaultUI() {
    }

    /**
     * Prepares and shows the main dialog GUI. When calling the constructor all
     * loggers and configurations are not yet loaded, so initializations should
     * not be done until in here.
     */
    public void showUI() {
        this.listener = new DefaultUIListener(this);
        setTitle(Config.getLocalized("analysis.title.mainDialog"));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JComponent content = (JComponent) getContentPane();
        content.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        content.setLayout(new BorderLayout(0, 0));
        content.add(createContents());
        addWindowListener(this.listener);
        setJMenuBar(new DefaultUIMenu(this));
        pack();
        Utils.center(this);
        setVisible(true);
    }

    /**
     * Combines all ui elements into one component
     * 
     * @return JComponent containing all sub components
     */
    private JComponent createContents() {
        JComponent pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = Utils.getEqualSpace();
        c.gridy++;
        c.gridwidth = 2;
        c.gridheight = 4;
        c.weightx = 0.1f;
        c.weighty = 1.0f;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        pane.add(createServerPanel(), c);
        JButton btnClose = new JButton(Config.getLocalized("analysis.buttons.close"));
        btnClose.setToolTipText(Config.getLocalized("analysis.buttons.close.tip"));
        btnClose.setMargin(new Insets(0, 0, 0, 0));
        btnClose.setActionCommand(DefaultUIListener.CLOSE_CMD);
        btnClose.addActionListener(this.listener);
        c.gridy += 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        pane.add(btnClose, c);
        JCheckBox chkDownload = new JCheckBox(Config.getLocalized("analysis.checkboxes.autoDownload"));
        chkDownload.setToolTipText(Config.getLocalized("analysis.checkboxes.autoDownload.tip"));
        chkDownload.addItemListener(this.listener);
        chkDownload.setSelected(Config.getInstance().getAutoDownload());
        c.gridx++;
        c.gridwidth = 5;
        c.weightx = 0.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        pane.add(chkDownload, c);
        return pane;
    }

    /**
     * Handles statuspanel for currently selected job
     * 
     * @param storage
     *            all status informations about the job
     */
    private void handleStatusPanel(final Storage storage) {
        this.detailPane.removeAll();
        WorkStatus ws = null;
        if (storage == null || (ws = storage.getStatus()) == null) {
            this.detailPane.setVisible(false);
            resizeSplitPane();
            return;
        }
        this.detailPane.setVisible(true);
        final WorkDescription wds = storage.getDescription();
        final WorkResult wr = storage.getResult();
        this.detailPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0f;
        c.insets = Utils.getEqualSpace();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridwidth = 10;
        JTable table = new JTable(getInfo(wds, ws));
        table.setShowGrid(false);
        TableColumn column = table.getColumnModel().getColumn(1);
        JTextField editorField = new JTextField();
        editorField.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
        Utils.addPopupMenu(editorField);
        DefaultCellEditor editor = new DefaultCellEditor(editorField);
        editor.setClickCountToStart(1);
        column.setCellEditor(editor);
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        table.getColumnModel().getColumn(1).setResizable(true);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        double height = (table.getRowHeight() + table.getIntercellSpacing().getHeight() + 1);
        height *= table.getRowCount();
        height += table.getTableHeader().getPreferredSize().getHeight();
        table.setPreferredScrollableViewportSize(new Dimension(150, (int) height));
        table.getColumnModel().getColumn(0).setCellRenderer(new MultiLineCellRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new MultiLineCellRenderer());
        JScrollPane infoScroll = new JScrollPane(table);
        infoScroll.setBorder(BorderFactory.createTitledBorder(Config.getLocalized("analysis.labels.infoStatus")));
        c.gridy = 0;
        c.weightx = 1.0f;
        c.weighty = 1.0f;
        c.fill = GridBagConstraints.BOTH;
        this.detailPane.add(infoScroll, c);
        c.gridy++;
        c.weighty = 0.0f;
        c.weightx = 1.f;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.detailPane.add(getButtons(ws.getError() != null), c);
        c.gridy++;
        c.weightx = 1.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 10;
        this.detailPane.add(getExecutionStatus(ws), c);
        int state = ws.getState();
        if (wr != null && (wr.getResults().length > 0 || wr.getChildren().length > 0)) {
            c.gridy++;
            JComponent comp = getResultsStatus(storage);
            if (state >= WorkStatus.NO_RESULT_STATE) {
                comp.removeAll();
                comp.add(new JLabel("No results returned"), -1);
            }
            this.detailPane.add(comp, c);
        }
        JComponent btns = new JPanel();
        btns.setLayout(new FlowLayout());
        if (state == WorkStatus.COMPLETED_STATE || state == WorkStatus.RESULT_STATE || state == WorkStatus.NO_RESULT_STATE || state == WorkStatus.CANCELED_STATE) {
            JButton removeBtn = new JButton(Config.getLocalized("analysis.buttons.remove"));
            removeBtn.setToolTipText(Config.getLocalized("analysis.buttons.remove.tip"));
            removeBtn.setMnemonic(KeyEvent.VK_R);
            removeBtn.setActionCommand(DefaultUIListener.REMOVE_CMD);
            removeBtn.addActionListener(this.listener);
            btns.add(removeBtn);
        } else if (state == WorkStatus.WAITING_STATE) {
            JButton finishBtn = new JButton(Config.getLocalized("analysis.buttons.finish"));
            finishBtn.setToolTipText(Config.getLocalized("analysis.buttons.finish.tip"));
            finishBtn.setMnemonic(KeyEvent.VK_F);
            finishBtn.setActionCommand(DefaultUIListener.FINISH_CMD);
            finishBtn.addActionListener(this.listener);
            btns.add(finishBtn);
        } else {
            JButton cancelBtn = new JButton(Config.getLocalized("analysis.buttons.cancel"));
            cancelBtn.setToolTipText(Config.getLocalized("analysis.buttons.cancel.tip"));
            cancelBtn.setMnemonic(KeyEvent.VK_C);
            cancelBtn.setActionCommand(DefaultUIListener.CANCEL_CMD);
            cancelBtn.addActionListener(this.listener);
            btns.add(cancelBtn);
        }
        c.gridy++;
        this.detailPane.add(btns, c);
        resizeSplitPane();
    }

    /**
     * Because of split pane, preferred size have to set manually
     */
    private void resizeSplitPane() {
        if (this.splitPane.getRightComponent() instanceof JScrollPane) {
            Dimension d = this.detailPane.getPreferredSize();
            d.width += this.splitPane.getDividerSize() + this.splitPane.getDividerLocation() + this.splitPane.getInsets().left + this.splitPane.getInsets().right;
            d.height += this.splitPane.getInsets().top + this.splitPane.getInsets().bottom;
            ((JScrollPane) this.splitPane.getRightComponent()).setPreferredSize(d);
        }
        this.detailPane.invalidate();
        Utils.pack(this);
    }

    /**
     * Gets the model for job info table
     * 
     * @param wds
     *            description of the job
     * @param ws
     *            status of the job
     * @return TableModel containing wanted data
     */
    public static TableModel getInfo(WorkDescription wds, WorkStatus ws) {
        final Vector<String[]> data = new Vector<String[]>();
        String jarURL = (wds.jarFiles().length > 0) ? wds.jarFiles()[0].toString() : null;
        data.add(new String[] { "Job class:", jarURL + wds.getExecutable().getClassName() });
        data.add(new String[] { "Owner:", (wds.getInfo().getOwner() != null ? wds.getInfo().getOwner() : "myself") });
        data.add(new String[] { "Simulation name:", wds.getInfo().getJobName() + " id= " + ArrayUtils.toString(wds.getJobID()) });
        if (ws.getStartTime() != null) {
            data.add(new String[] { "Execution started:", TextUtils.getDateFormat(ws.getStartTime()) });
        }
        if (ws.getEndTime() != null) {
            data.add(new String[] { "Execution ended:", TextUtils.getDateFormat(ws.getEndTime()) });
            if (ws.getStartTime() != null) {
                data.add(new String[] { "Execution time:", TextUtils.getInterval(ws.getEndTime().longValue() - ws.getStartTime().longValue(), true) });
            }
        }
        StringBuffer result = new StringBuffer();
        for (WorkDescription w : wds) {
            result.append("[");
            for (Object o : w.getExecutable().parameters()) {
                if (result.length() > 1) result.append(",");
                result.append(o.toString());
            }
            result.append("]");
        }
        data.add(new String[] { "Ranges:", result.toString() });
        TableModel model = new AbstractTableModel() {

            private static final long serialVersionUID = 1L;

            public String getColumnName(int col) {
                return null;
            }

            public int getColumnCount() {
                return 2;
            }

            public int getRowCount() {
                return data.size();
            }

            public Object getValueAt(int row, int col) {
                return ((String[]) data.get(row))[col];
            }

            public boolean isCellEditable(int row, int col) {
                return (col == 1);
            }

            public void setValueAt(Object value, int row, int col) {
            }
        };
        return model;
    }

    private JComponent getButtons(final boolean hasError) {
        JComponent pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = Utils.getEqualSpace();
        if (hasError) {
            JButton errorBtn = new JButton(Config.getLocalized("analysis.buttons.showError"));
            errorBtn.setMnemonic(KeyEvent.VK_E);
            errorBtn.setToolTipText(Config.getLocalized("analysis.buttons.showError.tip"));
            errorBtn.setActionCommand(DefaultUIListener.SHOW_ERROR);
            errorBtn.addActionListener(this.listener);
            c.gridwidth = 2;
            pane.add(errorBtn, c);
        }
        JButton showBtn = new JButton(Config.getLocalized("analysis.buttons.showWds"));
        showBtn.setMnemonic(KeyEvent.VK_W);
        showBtn.setToolTipText(Config.getLocalized("analysis.buttons.showWds.tip"));
        showBtn.setActionCommand(DefaultUIListener.SHOW_WDS);
        showBtn.addActionListener(this.listener);
        c.gridwidth = 3;
        pane.add(showBtn, c);
        return pane;
    }

    /**
     * Gets all information about the status
     * 
     * @param status
     *            status of the job
     * @return JComponent JComponent containing all informations
     */
    private JComponent getExecutionStatus(WorkStatus status) {
        JComponent pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        pane.setBorder(BorderFactory.createTitledBorder(Config.getLocalized("analysis.labels.executionStatus")));
        GridBagConstraints c = new GridBagConstraints();
        Insets equalSpace = new Insets(2, 2, 2, 2);
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0f;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = equalSpace;
        c.gridy = -1;
        String execLabel = Config.getLocalized("analysis.labels.executionProgress");
        String transLabel = Config.getLocalized("analysis.labels.transferProgress");
        if (status.getChildren().length != 1) {
            AgentProgressIndicator totalIndicator = new AgentProgressIndicator();
            totalIndicator.insertTitle(Config.getLocalized("analysis.labels.overallStatus"));
            if (status.getState() >= WorkStatus.EXECUTING_STATE) {
                totalIndicator.insertProgress("<font " + Config.getInstance().getFont() + ">" + execLabel + "</font>", status.getExecModel(), "%value / %maximum", null);
                totalIndicator.insertProgress("<font " + Config.getInstance().getFont() + ">" + transLabel + "</font>", status.getTransferModel(), "%value_kb / %maximum_kb", null);
            }
            c.gridy++;
            pane.add(totalIndicator, c);
        }
        if (status.getChildren().length > 0) {
            AgentProgressIndicator execIndicator = new AgentProgressIndicator();
            execIndicator.insertTitle(Config.getLocalized("analysis.labels.individualStatus"));
            for (WorkStatus host : status.getChildren()) {
                if (status.getState() >= WorkStatus.EXECUTING_STATE) {
                    execIndicator.insertProgress("<font " + Config.getInstance().getFont() + ">" + NetUtils.getHost(host.getServiceURL()) + "</font>", host.getExecModel(), execLabel + ": %value / %maximum", host.getTransferModel(), transLabel + ": %value_kb / %maximum_kb");
                }
            }
            c.gridy++;
            pane.add(execIndicator, c);
        }
        return pane;
    }

    /**
     * Gets results panel
     * 
     * @param storage
     *            the storage to be printed
     * @return JComponent component containing all information about results
     */
    private JComponent getResultsStatus(Storage storage) {
        final WorkResult wr = storage.getResult();
        final String showText = Config.getLocalized("analysis.buttons.resultShow");
        final String showTip = Config.getLocalized("analysis.buttons.resultShow.tip");
        final String showAllTip = Config.getLocalized("analysis.buttons.resultsShow.tip");
        final String downloadText = Config.getLocalized("analysis.buttons.resultDownload");
        final String downloadTip = Config.getLocalized("analysis.buttons.resultDownload.tip");
        final String downloadAllTip = Config.getLocalized("analysis.buttons.resultsDownload.tip");
        final String infoText = Config.getLocalized("analysis.buttons.resultInfo");
        final String infoTip = Config.getLocalized("analysis.buttons.resultInfo.tip");
        JComponent pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        pane.setBorder(BorderFactory.createTitledBorder(Config.getLocalized("analysis.labels.resultStatus")));
        AgentProgressIndicator transferIndicator = new AgentProgressIndicator();
        Iterator<JobAttachment> ite = wr.results();
        if (ite.hasNext() && ite.hasNext()) {
            JPanel allPane = new JPanel();
            allPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 1));
            boolean finished = (wr.getTransferTotals()[WorkResult.VALUE] > 0 && wr.getTransferTotals()[WorkResult.VALUE] == wr.getTransferTotals()[WorkResult.MAXIMUM]);
            String buttonText = finished ? showText : downloadText;
            String buttonTip = finished ? showAllTip : downloadAllTip;
            JButton btnAll = new JButton(buttonText);
            btnAll.setMargin(Utils.getEmpty());
            btnAll.setFont(btnAll.getFont().deriveFont(8));
            btnAll.setActionCommand("*");
            btnAll.addActionListener(this.listener);
            btnAll.setToolTipText(buttonTip);
            Insets insets = btnAll.getBorder().getBorderInsets(btnAll);
            FontMetrics metrics = btnAll.getFontMetrics(btnAll.getFont());
            Graphics g = getGraphics();
            Rectangle2D textBounds = metrics.getStringBounds(buttonText, g);
            int width = (int) textBounds.getWidth() + insets.left + insets.right;
            btnAll.setPreferredSize(new Dimension(width, (int) textBounds.getHeight()));
            allPane.add(btnAll);
            final String totalsString = "<font " + Config.getInstance().getFont() + ">" + Config.getLocalized("analysis.labels.totals") + "</font>";
            transferIndicator.insertProgress(totalsString, wr.getTransferTotals(), "%value_kb / %maximum_kb", allPane);
        }
        for (Iterator<JobAttachment> results = wr.results(); results.hasNext(); ) {
            JobAttachment res = results.next();
            String name = res.fileName();
            int[] transModel = new int[] { (int) res.payloadSize(), (int) res.getSize() };
            String buttonText = transModel[0] > 0 ? showText : downloadText;
            String buttonTip = transModel[0] > 0 ? showTip : downloadTip;
            JButton actionBtn = new JButton(buttonText);
            actionBtn.setMargin(Utils.getEmpty());
            actionBtn.setFont(actionBtn.getFont().deriveFont(8));
            actionBtn.setActionCommand(res.fileName());
            actionBtn.addActionListener(this.listener);
            actionBtn.setToolTipText(buttonTip);
            Insets insets = actionBtn.getBorder().getBorderInsets(actionBtn);
            FontMetrics metrics = actionBtn.getFontMetrics(actionBtn.getFont());
            Graphics g = getGraphics();
            Rectangle2D textBounds = metrics.getStringBounds(buttonText, g);
            int width = (int) textBounds.getWidth() + insets.left + insets.right;
            actionBtn.setPreferredSize(new Dimension(width, (int) textBounds.getHeight()));
            JButton infoBtn = new JButton(infoText);
            infoBtn.setMargin(new Insets(0, 0, 0, 0));
            infoBtn.setFont(infoBtn.getFont().deriveFont(8));
            infoBtn.setActionCommand(res.fileName());
            infoBtn.addActionListener(this.listener);
            infoBtn.setToolTipText(infoTip);
            textBounds = metrics.getStringBounds(infoText, g);
            width = (int) textBounds.getWidth() + insets.left + insets.right;
            infoBtn.setPreferredSize(new Dimension(width, (int) textBounds.getHeight()));
            JPanel cmp = new JPanel();
            cmp.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 1));
            cmp.add(actionBtn);
            cmp.add(infoBtn);
            try {
                name = FileUtils.getFilename(URLDecoder.decode(name, "iso-8859-1"));
            } catch (UnsupportedEncodingException e1) {
            }
            name = "<font " + Config.getInstance().getFont() + ">" + name + "</font>";
            transferIndicator.insertProgress(name, transModel, "%value_kb / %maximum_kb", cmp);
        }
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = c.gridy = 0;
        c.insets = Utils.getEqualSpace();
        pane.add(transferIndicator, c);
        return pane;
    }

    /**
     * Shows grid proxy init dialog and initializes the proxy
     * 
     * @param myproxy
     *            are we using myproxy or normal grid proxy
     * @return validity of the proxy after initialization
     */
    public boolean gridProxyInit(boolean myproxy) {
        if (myproxy == false) {
            log.debug("starting grid proxy init");
            log.debug(CoGProperties.getDefault().getUserKeyFile());
            GridProxyInit gpiFrame = new GridProxyInit(DefaultUI.this, true);
            gpiFrame.setCloseOnSuccess(true);
            Utils.center(gpiFrame);
            gpiFrame.pack();
            gpiFrame.setVisible(true);
        } else {
            log.debug("starting my proxy init");
            MyProxyGUI mpiFrame = new MyProxyGUI(DefaultUI.this, false);
            Utils.center(mpiFrame);
            mpiFrame.pack();
            mpiFrame.setVisible(true);
        }
        log.debug("quiting proxy init");
        return updateProxyInfo();
    }

    /**
     * Informs an error message to the user.
     * 
     * @param title title for the dialog
     * @param message message to the user
     * @param options give user a choise
     * @param owner owner component, normally <code>Frame</code> or
     *            <code>Dialog</code>. If null the main window is used
     * @return number of the option which was clicked, or -1 if none
     */
    public int errorMessage(String title, String message, String[] options, Component owner) {
        if (owner == null) owner = this;
        message = message.replaceAll("\n", "<br>");
        if (options == null) {
            message = "<html><table width=500><tr><td><font face=Arial>" + message.trim() + "</font></td></tr></table></html>";
            JLabel label = new JLabel(message);
            JScrollPane scroll = new JScrollPane(label);
            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            Window window = (owner instanceof Frame) ? new Window((Frame) owner, title) : new Window((Dialog) owner, title);
            window.setContent(scroll);
            window.setIcon(0);
            window.addCloseButton();
            window.showWindow();
            return -1;
        }
        return Utils.showConfirmDialog(title, "<html>" + message.trim() + "</html>", options, 0, owner);
    }

    public void sessionCreated(Storage storage) {
        this.selectedJob = storage.getDescription().getJobID()[0];
        JobListItem newItem = new JobListItem(this.selectedJob, storage.getDescription().getInfo().getJobName(), "", storage.getStatus().getStartTime());
        DefaultListModel m = (DefaultListModel) this.jobsList.getModel();
        if (newItem.getStartTime() == 0 || m.getSize() == 0) {
            m.addElement(newItem);
            this.jobsList.setSelectedIndex(m.getSize() - 1);
        } else {
            for (int i = 0; i < m.getSize(); i++) {
                JobListItem item = (JobListItem) m.get(i);
                if (newItem.getStartTime() < item.getStartTime()) {
                    m.add(i, newItem);
                    this.jobsList.setSelectedIndex(i + 1);
                    i = m.getSize();
                } else if (i + 1 >= m.getSize()) {
                    m.addElement(newItem);
                    this.jobsList.setSelectedIndex(i + 1);
                    i = m.getSize();
                }
            }
        }
    }

    /**
     * Changes the active job state to the list of jobs
     * 
     * @param jobID
     *            to be changed
     * @param postfix
     *            status message to be added after the name
     */
    private void changeJobList(Long jobID, String postfix) {
        DefaultListModel m = (DefaultListModel) this.jobsList.getModel();
        for (int index = 0; index < m.getSize(); index++) {
            JobListItem item = (JobListItem) m.get(index);
            if (jobID.equals(item.getID())) {
                item.setPostfix(postfix);
                m.set(index, item);
                return;
            }
        }
    }

    /**
     * Gets the currently selected job
     * 
     * @return ID of the selected job
     */
    public Long getSelectedJob() {
        return this.selectedJob;
    }

    /**
     * Gets the selected class name
     * 
     * @return name of the observer class
     */
    public String getSelectedObserver() {
        return this.observerClass;
    }

    /**
     * Selection in joblist has changed
     */
    public void selectionChanged() {
        JobListItem item = (JobListItem) this.jobsList.getSelectedValue();
        Storage storage = null;
        if (item != null) {
            this.selectedJob = item.getID();
            try {
                storage = SessionHandler.getInstance().getSession(this.selectedJob);
            } catch (RemoteException e) {
            }
        } else {
            this.selectedJob = null;
        }
        handleStatusPanel(storage);
    }

    /**
     * Remove job from local list and release all resources from the server
     */
    protected void removeSelectedJob() {
        JobListItem item = (JobListItem) this.jobsList.getSelectedValue();
        ((DefaultListModel) this.jobsList.getModel()).removeElement(item);
        try {
            SessionHandler.getService().abort(item.getID(), ComputingInterface.ABORT_AND_REMOVE);
        } catch (RemoteException e) {
        }
    }

    /**
     * Remove all jobs from local list and release their resources from the
     * server
     */
    protected void removeAllJobs() {
        ((DefaultListModel) this.jobsList.getModel()).removeAllElements();
        try {
            SessionHandler.getService().abort(new Long(-1), ComputingInterface.ABORT_AND_REMOVE);
        } catch (RemoteException e) {
        }
    }

    /**
     * Updates status of the proxy on the screen
     * 
     * @return validity of the proxy
     */
    protected boolean updateProxyInfo() {
        GSIUtils gsiUtils = new GSIUtils(Config.getInstance().getProxyFile());
        this.proxyInfo.setText("<html><font " + Config.getInstance().getFont() + ">" + gsiUtils.getProxyStatusMessage() + "</font></html>");
        return gsiUtils.getProxyStatus();
    }

    public void statusChanged(final WorkStatus status) throws RemoteException {
        Runnable updateRoutine = new Runnable() {

            public void run() {
                int state = status.getState();
                Long jobID = status.getJobID()[0];
                changeJobList(jobID, status.getStateString());
                if (state == WorkStatus.IDLE_STATE) {
                } else if (state == WorkStatus.DISPATCHING_STATE) {
                } else if (state == WorkStatus.EXECUTING_STATE) {
                    DefaultListModel m = (DefaultListModel) DefaultUI.this.jobsList.getModel();
                    for (int i = 0; i < m.getSize(); i++) {
                        JobListItem item = (JobListItem) m.get(i);
                        if (item.getID().equals(jobID)) {
                            item.setStartTime(status.getStartTime());
                            break;
                        }
                    }
                } else if (state == WorkStatus.FINISHING_STATE) {
                } else if (state == WorkStatus.WAITING_STATE) {
                } else if (state == WorkStatus.COMPLETED_STATE) {
                } else if (state == WorkStatus.TRANSFER_STATE) {
                } else if (state == WorkStatus.RESULT_STATE) {
                } else if (state == WorkStatus.NO_RESULT_STATE) {
                } else if (state == WorkStatus.CANCELED_STATE) {
                }
                if (jobID == null) handleStatusPanel(null); else if (jobID.equals(DefaultUI.this.selectedJob) == true) {
                    Storage storage = null;
                    try {
                        storage = SessionHandler.getInstance().getSession(jobID);
                    } catch (RemoteException e) {
                    }
                    handleStatusPanel(storage);
                }
                if (status.getError() != null) {
                    log.error("Job has errors: " + status.getError());
                    errorMessage("Job has errors", status.getError(), null, DefaultUI.this);
                }
            }
        };
        SwingUtilities.invokeLater(updateRoutine);
    }

    public void resultChanged(WorkResult result) throws RemoteException {
    }

    /**
     * Shows the UI for creating a new job.
     * 
     * @param wds the default values for the new ui component. If NULL tries to
     *            load recently used description or use default values.
     */
    public void openNewJobUI(WorkDescription wds) {
        String[] list = new String[this.servers.getItemCount()];
        for (int i = 0; i < this.servers.getItemCount(); i++) {
            list[i] = (String) this.servers.getItemAt(i);
        }
        new DispatchUI(list, wds);
    }

    /**
     * Creates panel for showing proxy state
     * 
     * @return newly created panel
     */
    private Component createProxyPanel() {
        JComponent proxyPane = new JPanel();
        proxyPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = Utils.getEqualSpace();
        c.gridy = 0;
        c.gridwidth = 4;
        c.gridheight = 1;
        c.weighty = 0.0f;
        Border proxyBorder = BorderFactory.createTitledBorder(Config.getLocalized("analysis.labels.proxyStatus"));
        proxyPane.setBorder(proxyBorder);
        this.proxyInfo = new JLabel("Status", SwingConstants.LEADING);
        updateProxyInfo();
        c.gridx = 0;
        c.weightx = 1.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        proxyPane.add(this.proxyInfo, c);
        JButton btnGridProxy = new JButton(Config.getLocalized("analysis.buttons.GridProxy"));
        btnGridProxy.setToolTipText(Config.getLocalized("analysis.buttons.GridProxy.tip"));
        btnGridProxy.setMnemonic(KeyEvent.VK_G);
        btnGridProxy.setActionCommand(DefaultUIListener.GRIDPROXY_CMD);
        btnGridProxy.addActionListener(this.listener);
        c.gridx = GridBagConstraints.RELATIVE;
        c.weightx = 0.0f;
        c.fill = GridBagConstraints.NONE;
        proxyPane.add(btnGridProxy, c);
        JButton btnMyProxy = new JButton(Config.getLocalized("analysis.buttons.MyProxy"));
        btnMyProxy.setToolTipText(Config.getLocalized("analysis.buttons.MyProxy.tip"));
        btnMyProxy.setMnemonic(KeyEvent.VK_M);
        btnMyProxy.setActionCommand(DefaultUIListener.MYPROXY_CMD);
        btnMyProxy.addActionListener(this.listener);
        proxyPane.add(btnMyProxy, c);
        JButton btnGridOptions = new JButton(Config.getLocalized("analysis.buttons.GridProxyOptions"));
        btnGridOptions.setToolTipText(Config.getLocalized("analysis.buttons.GridProxyOptions.tip"));
        btnGridOptions.setMnemonic(KeyEvent.VK_O);
        btnGridOptions.setActionCommand(DefaultUIListener.PROXY_OPTIONS_CMD);
        btnGridOptions.addActionListener(this.listener);
        proxyPane.add(btnGridOptions, c);
        return proxyPane;
    }

    /**
     * Creates panel for changing servers and loading jobs from them
     * 
     * @return newly created panel
     */
    private Component createServerPanel() {
        JComponent pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = Utils.getEqualSpace();
        c.gridy = 0;
        c.gridheight = 1;
        Border jobBorder = BorderFactory.createTitledBorder(Config.getLocalized("analysis.labels.currentJobs"));
        pane.setBorder(jobBorder);
        pane.setLayout(new GridBagLayout());
        this.servers = new JComboBox(Config.getInstance().getHostList());
        this.servers.setEditable(true);
        c.gridx = 0;
        c.gridwidth = 7;
        c.weightx = 1.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        pane.add(this.servers, c);
        JButton btnSync = new JButton(Config.getLocalized("analysis.buttons.sync"));
        btnSync.setToolTipText(Config.getLocalized("analysis.buttons.sync.tip"));
        btnSync.setMnemonic(KeyEvent.VK_S);
        btnSync.setActionCommand(DefaultUIListener.SYNC_CMD);
        btnSync.addActionListener(this.listener);
        c.gridx = 8;
        c.gridwidth = 2;
        c.weightx = 0.0f;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        pane.add(btnSync, c);
        this.detailPane = new JPanel();
        this.detailPane.setLayout(new GridBagLayout());
        JComponent jobList = createJobList();
        JComponent jobInfo = new JScrollPane(this.detailPane);
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jobList, jobInfo);
        this.splitPane.setOneTouchExpandable(true);
        this.splitPane.setContinuousLayout(true);
        this.splitPane.setDividerLocation(120);
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 10;
        c.weightx = 1.0f;
        c.weighty = 1.0f;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(this.splitPane, c);
        return pane;
    }

    /**
     * Creates panel for selecting topmost job from the jobs running on server
     * @return newly created panel
     */
    private JComponent createJobList() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        Insets equalSpace = new Insets(2, 2, 2, 2);
        c.anchor = GridBagConstraints.CENTER;
        c.insets = equalSpace;
        c.gridx = 0;
        c.weightx = 1.0f;
        c.gridwidth = 1;
        c.gridheight = 1;
        DefaultListModel m = new DefaultListModel();
        this.jobsList = new JList(m);
        this.jobsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.jobsList.addListSelectionListener(this.listener);
        JScrollPane spJob = new JScrollPane(this.jobsList);
        c.gridy = 0;
        c.weighty = 1.0f;
        c.fill = GridBagConstraints.BOTH;
        pane.add(spJob, c);
        JButton btnNewJob = new JButton(Config.getLocalized("analysis.buttons.newJob"));
        btnNewJob.setMnemonic(KeyEvent.VK_N);
        btnNewJob.setToolTipText(Config.getLocalized("analysis.buttons.newJob.tip"));
        btnNewJob.setMargin(Utils.getEmpty());
        btnNewJob.setActionCommand(DefaultUIListener.NEWJOB_CMD);
        btnNewJob.addActionListener(this.listener);
        c.gridy = 1;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(btnNewJob, c);
        JButton btnClear = new JButton(Config.getLocalized("analysis.buttons.clear"));
        btnClear.setMnemonic(KeyEvent.VK_A);
        btnClear.setToolTipText(Config.getLocalized("analysis.buttons.clear.tip"));
        btnClear.setMargin(Utils.getEmpty());
        btnClear.setActionCommand(DefaultUIListener.CLEAR_CMD);
        btnClear.addActionListener(this.listener);
        c.gridy = 2;
        c.weighty = 0.0f;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(btnClear, c);
        return pane;
    }

    /**
     * This class is executed by the timer in every 60 seconds. 
     * It just updates the state of proxy.
     */
    private class UpdateProxyInfoTask extends TimerTask {

        public void run() {
            updateProxyInfo();
        }
    }

    private class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

        private static final long serialVersionUID = 1L;

        public MultiLineCellRenderer() {
            setLineWrap(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
            setFont(table.getFont());
            setBorder(new EmptyBorder(1, 2, 1, 2));
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    public void serviceChanged(DiscoveryPacket[] services) {
        this.servers.removeAllItems();
        for (DiscoveryPacket s : services) {
            this.servers.addItem(s.getServiceURL());
        }
    }
}
