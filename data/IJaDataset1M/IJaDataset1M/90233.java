package uk.ac.ebi.pride.gui.task.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.data.controller.impl.PrideDBAccessControllerImpl;
import uk.ac.ebi.pride.gui.GUIUtilities;
import uk.ac.ebi.pride.gui.PrideInspectorContext;
import uk.ac.ebi.pride.gui.component.dialog.TaskDialog;
import uk.ac.ebi.pride.gui.component.reviewer.PrivateDownloadSelectionPane;
import uk.ac.ebi.pride.gui.desktop.Desktop;
import uk.ac.ebi.pride.gui.task.TaskAdapter;
import uk.ac.ebi.pride.gui.task.TaskEvent;
import uk.ac.ebi.pride.gui.task.TaskListener;
import uk.ac.ebi.pride.gui.utils.DefaultGUIBlocker;
import uk.ac.ebi.pride.gui.utils.GUIBlocker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Open only valid pride experiment, show warning message to invalid accessions.
 * <p/>
 * User: rwang
 * Date: 06/01/11
 * Time: 14:12
 */
public class OpenValidPrideExperimentTask extends TaskAdapter<Void, Void> implements TaskListener<List<Map<String, String>>, String> {

    private static final Logger logger = LoggerFactory.getLogger(PrideDBAccessControllerImpl.class);

    /**
     * This defines the default experiment accession to open
     */
    private List<Comparable> experimentAccessions;

    /**
     * User name
     */
    private String username;

    /**
     * Password
     */
    private String password;

    /**
     * Reference to PRIDE context
     */
    PrideInspectorContext context;

    /**
     * Open a connection to pride database
     *
     * @param expAccs  experiment accession
     * @param username pride user name
     * @param password pride password
     */
    public OpenValidPrideExperimentTask(List<Comparable> expAccs, String username, String password) {
        String msg = "Validate and loading PRIDE Experiments";
        this.setName(msg);
        this.setDescription(msg);
        this.experimentAccessions = expAccs;
        this.username = username;
        this.password = password;
        context = ((PrideInspectorContext) Desktop.getInstance().getDesktopContext());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Void doInBackground() throws Exception {
        boolean emptySetOfAccs = (experimentAccessions == null || experimentAccessions.isEmpty());
        boolean emptyUserCredentials = (username == null || password == null);
        List<Comparable> vpubExps = validatePublicExperiments();
        openValidPublicExperiments(vpubExps);
        if (emptySetOfAccs && !emptyUserCredentials) {
            openAllPrivateExperiments();
        } else if (!emptySetOfAccs && !experimentAccessions.isEmpty()) {
            if (emptyUserCredentials) {
                username = context.getProperty("default.pride.username");
                password = context.getProperty("default.pride.password");
            }
            openRemainingExperiments(experimentAccessions);
        }
        return null;
    }

    /**
     * Validate public experiments and return the results
     *
     * @return List<Comparable>    a list of public experiments
     * @throws Exception exception while initializing data access controller
     */
    private List<Comparable> validatePublicExperiments() throws Exception {
        List<Comparable> results = new ArrayList<Comparable>();
        if (experimentAccessions != null) {
            PrideDBAccessControllerImpl controller = new PrideDBAccessControllerImpl();
            Collection<Comparable> fullExpAccs = controller.getExperimentAccs();
            List<Comparable> tooLargeToOpen = getLargeExperimentAccs();
            fullExpAccs.retainAll(experimentAccessions);
            fullExpAccs.removeAll(tooLargeToOpen);
            experimentAccessions.removeAll(fullExpAccs);
            results.addAll(fullExpAccs);
        }
        return results;
    }

    /**
     * Open all the valid public experiments
     *
     * @param vpubExps a list of valid public experiments
     */
    private void openValidPublicExperiments(List<Comparable> vpubExps) {
        for (Comparable vpubExp : vpubExps) {
            OpenPrideDatabaseTask prideDBTask = new OpenPrideDatabaseTask(vpubExp);
            prideDBTask.setGUIBlocker(new DefaultGUIBlocker(prideDBTask, GUIBlocker.Scope.NONE, null));
            context.addTask(prideDBTask);
        }
    }

    /**
     * Open all the remaing experiments, they could be public or private
     */
    private void openRemainingExperiments(List<Comparable> remainingExps) {
        OpenReviewerConnectionTask task = new OpenReviewerConnectionTask(username, password, remainingExps);
        task.addTaskListener(this);
        task.setGUIBlocker(new DefaultGUIBlocker(task, GUIBlocker.Scope.NONE, null));
        context.addTask(task);
    }

    /**
     * Open all the private experiment associated with the user
     */
    private void openAllPrivateExperiments() {
        OpenReviewerConnectionTask task = new OpenReviewerConnectionTask(username, password);
        task.addTaskListener(this);
        task.setGUIBlocker(new DefaultGUIBlocker(task, GUIBlocker.Scope.NONE, null));
        context.addTask(task);
    }

    /**
     * Show warning dialog
     */
    private void showWarning() {
        String warningMsg = buildWarningMessage();
        showWarningDialog(warningMsg);
    }

    /**
     * Get a list of experiment accessions which are too big to open in pride inspector
     *
     * @return List<Comparable>    a list of experiment accessions
     */
    private List<Comparable> getLargeExperimentAccs() {
        List<Comparable> accs = new ArrayList<Comparable>();
        String accStr = context.getProperty("large.pride.experiments");
        if (accStr != null && !"".equals(accStr.trim())) {
            String[] parts = accStr.split(",");
            for (String part : parts) {
                accs.add(part.trim());
            }
        }
        return accs;
    }

    /**
     * Create a warning message
     *
     * @return String  warning message
     */
    private String buildWarningMessage() {
        StringBuilder str = new StringBuilder();
        StringTokenizer st = new StringTokenizer(context.getProperty("invalid.experiment.accession"), "\n");
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (s.contains("[insert]")) {
                str.append(experimentAccessions.toString());
            } else {
                str.append(s);
            }
        }
        return str.toString();
    }

    /**
     * show a warning dialog for invalid pride accessions
     *
     * @param warningMsg warning message to show
     */
    private void showWarningDialog(String warningMsg) {
        JDialog dialog = new JDialog(Desktop.getInstance().getMainComponent());
        dialog.setAlwaysOnTop(true);
        dialog.setSize(new Dimension(400, 200));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation((d.width - dialog.getWidth()) / 2, (d.height - dialog.getHeight()) / 2);
        dialog.setTitle("Experiment Not Found");
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel label = new JLabel(warningMsg);
        panel.add(label);
        mainPanel.add(panel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
        buttonPanel.setBackground(Color.white);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new CloseListener(dialog));
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    @Override
    public void succeed(TaskEvent<List<Map<String, String>>> listTaskEvent) {
        List<Map<String, String>> metaData = listTaskEvent.getValue();
        Map<Comparable, Double> accs = new LinkedHashMap<Comparable, Double>();
        List<Map<String, String>> validMetaData = new ArrayList<Map<String, String>>();
        double sumOfSize = 0;
        for (Map<String, String> exp : metaData) {
            String accession = exp.get("Accession");
            if (experimentAccessions == null || experimentAccessions.contains(accession)) {
                double size = Double.valueOf(exp.get("Size"));
                accs.put(accession, size);
                sumOfSize += size / (1024 * 1024);
                validMetaData.add(exp);
            }
        }
        double downloadThreshold = Double.parseDouble(context.getProperty("download.size.threshold"));
        if (sumOfSize > downloadThreshold) {
            JDialog dialog = new JDialog();
            dialog.setAlwaysOnTop(true);
            ImageIcon icon = GUIUtilities.loadImageIcon(context.getProperty("pride.inspector.logo.small.icon"));
            dialog.setIconImage(icon.getImage());
            dialog.setTitle("About to download large experiments");
            dialog.setSize(new Dimension(650, 600));
            dialog.setLayout(new BorderLayout());
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            dialog.setLocation((d.width - dialog.getWidth()) / 2, (d.height - dialog.getHeight()) / 2);
            JPanel msgPanel = new JPanel(new BorderLayout());
            msgPanel.setBorder(BorderFactory.createTitledBorder("Warning"));
            JLabel warningMsg = new JLabel("<html>Experiments require download, the size of this download is over <b><i>" + Math.round(downloadThreshold) + "M</i></b>. <br/> To continue, select experiments from below and click the Download button.<html>");
            warningMsg.setIcon(GUIUtilities.loadIcon(context.getProperty("download.warning.icon.small")));
            Font originalFont = warningMsg.getFont();
            Font newFont = originalFont.deriveFont(14.0f);
            warningMsg.setFont(newFont);
            msgPanel.add(warningMsg, BorderLayout.CENTER);
            dialog.add(msgPanel, BorderLayout.NORTH);
            PrivateDownloadSelectionPane selectionPane = new PrivateDownloadSelectionPane(dialog, true, username, password);
            selectionPane.addExperimentMetaData(validMetaData);
            dialog.add(selectionPane, BorderLayout.CENTER);
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
        } else {
            TaskDialog dialog = new TaskDialog(Desktop.getInstance().getMainComponent(), "Download PRIDE Experiment", "Downloading...Please be aware that this may take a few minutes");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            DownloadExperimentTask downloadTask = new DownloadExperimentTask(accs, new File(System.getProperty("java.io.tmpdir")), username, password, true);
            downloadTask.addTaskListener(dialog);
            downloadTask.setGUIBlocker(new DefaultGUIBlocker(downloadTask, GUIBlocker.Scope.NONE, null));
            context.addTask(downloadTask);
        }
        if (experimentAccessions != null) {
            experimentAccessions.removeAll(accs.keySet());
            if (experimentAccessions.size() > 0) {
                showWarning();
            }
        }
    }

    @Override
    public void started(TaskEvent<Void> event) {
    }

    @Override
    public void process(TaskEvent<List<String>> listTaskEvent) {
    }

    @Override
    public void finished(TaskEvent<Void> event) {
    }

    @Override
    public void failed(TaskEvent<Throwable> event) {
    }

    @Override
    public void cancelled(TaskEvent<Void> event) {
    }

    @Override
    public void interrupted(TaskEvent<InterruptedException> iex) {
    }

    @Override
    public void progress(TaskEvent<Integer> progress) {
    }

    /**
     * Action listener to close a dialog
     */
    private static class CloseListener implements ActionListener {

        private JDialog dialog;

        private CloseListener(JDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.setVisible(false);
        }
    }
}
