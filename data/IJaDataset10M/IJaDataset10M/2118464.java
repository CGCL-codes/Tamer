package phex.gui.tabs.download;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import org.apache.commons.lang.SystemUtils;
import phex.common.FileHandlingException;
import phex.common.file.ManagedFileException;
import phex.common.format.NumberFormatUtils;
import phex.common.log.NLogger;
import phex.download.swarming.SWDownloadFile;
import phex.gui.common.GUIRegistry;
import phex.gui.common.GUIUtils;
import phex.gui.common.IconPack;
import phex.gui.common.progressbar.DownloadFileScopeProvider;
import phex.gui.common.progressbar.MultiScopeProgressBar;
import phex.utils.Localizer;
import phex.utils.SystemShellExecute;
import phex.xml.sax.gui.DGuiSettings;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class DownloadOverviewPanel extends JPanel {

    private DateFormat dateFormat;

    private SWDownloadFile lastDownloadFile;

    private long lastFinBufScopeLength;

    private Icon defProgressIcon;

    private Icon twinkleProgressIcon;

    private MultiScopeProgressBar progressBar;

    private JLabel progressLabel;

    private Timer resetProgressTwinkleTimer;

    private JLabel progressIconLabel;

    private JLabel downloadedLabel;

    private JLabel remainingLabel;

    private JLabel etaLabel;

    private JLabel createdLabel;

    private JLabel totalSizeLabel;

    private JLabel lastDownloadedLabel;

    private JLabel downloadRateLabel;

    private JLabel maxRateLabel;

    private JLabel downloadingCandidatesLabel;

    private JLabel queuedCandidatesLabel;

    private JLabel connectingCandidatesLabel;

    private JLabel goodCandidatesLabel;

    private JLabel badCandidatesLabel;

    private JLabel totalCandidatesLabel;

    private JTextField fileNameTxt;

    private JTextField incompleteFileTxt;

    /**
     * The explore file button is only available on Windows or Mac systems,
     * otherwise it is null.
     */
    private JButton exploreFileBtn;

    public DownloadOverviewPanel() {
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    }

    public void initializeComponent(DGuiSettings guiSettings) {
        CellConstraints cc = new CellConstraints();
        FormLayout layout = new FormLayout("6dlu, fill:d:grow, 6dlu", "6dlu, p, 6dlu, p, 6dlu, p");
        PanelBuilder panelBuilder = new PanelBuilder(layout, this);
        JPanel progressPanel = buildProgressPanel();
        panelBuilder.add(progressPanel, cc.xy(2, 2));
        JPanel infoPanel = buildInfoPanel();
        panelBuilder.add(infoPanel, cc.xy(2, 4));
        JPanel info2Panel = buildInfo2Panel();
        panelBuilder.add(info2Panel, cc.xy(2, 6));
        setupIcons();
        ActionListener updateInterfaceAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    updateInterface();
                } catch (Throwable th) {
                    NLogger.error(DownloadOverviewPanel.class, th, th);
                }
            }
        };
        GUIRegistry.getInstance().getGuiUpdateTimer().addActionListener(updateInterfaceAction);
    }

    private JPanel buildProgressPanel() {
        JPanel subPanel = new JPanel();
        CellConstraints cc = new CellConstraints();
        FormLayout layout = new FormLayout("d, 2dlu, d, 2dlu, fill:d:grow, 2dlu, right:25dlu", "p");
        PanelBuilder panelBuilder = new PanelBuilder(layout, subPanel);
        progressIconLabel = new JLabel(Localizer.getString("DownloadOverview_Progress"));
        panelBuilder.add(progressIconLabel, cc.xy(3, 1));
        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    progressIconLabel.setIcon(defProgressIcon);
                } catch (Throwable th) {
                    NLogger.error(DownloadOverviewPanel.class, th, th);
                }
            }
        };
        resetProgressTwinkleTimer = new Timer(175, actionListener);
        resetProgressTwinkleTimer.setRepeats(false);
        progressBar = new MultiScopeProgressBar();
        panelBuilder.add(progressBar, cc.xy(5, 1));
        progressLabel = new JLabel(" 100 %");
        panelBuilder.add(progressLabel, cc.xy(7, 1));
        return subPanel;
    }

    private JPanel buildInfoPanel() {
        JPanel subPanel = new JPanel();
        CellConstraints cc = new CellConstraints();
        FormLayout layout = new FormLayout("d, 4dlu, right:d, " + "fill:8dlu:grow, d, 4dlu, right:d, " + "fill:8dlu:grow, d, 4dlu, right:d", "p, 2dlu, p, 2dlu, p, 2dlu, p, 6dlu, p, 2dlu, p, 2dlu, p");
        layout.setColumnGroups(new int[][] { { 1, 5, 9 }, { 3, 7, 11 } });
        PanelBuilder panelBuilder = new PanelBuilder(layout, subPanel);
        panelBuilder.addSeparator(Localizer.getString("DownloadOverview_Transfer"), cc.xywh(1, 1, layout.getColumnCount(), 1));
        JLabel label = new JLabel(Localizer.getString("DownloadOverview_Downloaded"));
        panelBuilder.add(label, cc.xy(1, 3));
        downloadedLabel = new JLabel();
        panelBuilder.add(downloadedLabel, cc.xy(3, 3));
        label = new JLabel(Localizer.getString("DownloadOverview_Remaining"));
        panelBuilder.add(label, cc.xy(5, 3));
        remainingLabel = new JLabel();
        panelBuilder.add(remainingLabel, cc.xy(7, 3));
        label = new JLabel(Localizer.getString("DownloadOverview_DownloadRate"));
        panelBuilder.add(label, cc.xy(9, 3));
        downloadRateLabel = new JLabel();
        panelBuilder.add(downloadRateLabel, cc.xy(11, 3));
        label = new JLabel(Localizer.getString("DownloadOverview_FileSize"));
        panelBuilder.add(label, cc.xy(1, 5));
        totalSizeLabel = new JLabel();
        panelBuilder.add(totalSizeLabel, cc.xy(3, 5));
        label = new JLabel(Localizer.getString("DownloadOverview_EstimatedTime"));
        panelBuilder.add(label, cc.xy(5, 5));
        etaLabel = new JLabel();
        panelBuilder.add(etaLabel, cc.xy(7, 5));
        label = new JLabel(Localizer.getString("DownloadOverview_MaximalRate"));
        panelBuilder.add(label, cc.xy(9, 5));
        maxRateLabel = new JLabel();
        panelBuilder.add(maxRateLabel, cc.xy(11, 5));
        label = new JLabel(Localizer.getString("DownloadOverview_LastDownloaded"));
        panelBuilder.add(label, cc.xy(1, 7));
        lastDownloadedLabel = new JLabel();
        panelBuilder.add(lastDownloadedLabel, cc.xy(3, 7));
        label = new JLabel(Localizer.getString("DownloadOverview_Created"));
        panelBuilder.add(label, cc.xy(9, 7));
        createdLabel = new JLabel();
        panelBuilder.add(createdLabel, cc.xy(11, 7));
        panelBuilder.addSeparator(Localizer.getString("DownloadOverview_Candidates"), cc.xywh(1, 9, 11, 1));
        label = new JLabel(Localizer.getString("DownloadOverview_DownloadingCandidates"));
        panelBuilder.add(label, cc.xy(1, 11));
        downloadingCandidatesLabel = new JLabel();
        panelBuilder.add(downloadingCandidatesLabel, cc.xy(3, 11));
        label = new JLabel(Localizer.getString("DownloadOverview_QueuedCandidates"));
        panelBuilder.add(label, cc.xy(5, 11));
        queuedCandidatesLabel = new JLabel();
        panelBuilder.add(queuedCandidatesLabel, cc.xy(7, 11));
        label = new JLabel(Localizer.getString("DownloadOverview_ConnectingCandidates"));
        panelBuilder.add(label, cc.xy(9, 11));
        connectingCandidatesLabel = new JLabel();
        panelBuilder.add(connectingCandidatesLabel, cc.xy(11, 11));
        label = new JLabel(Localizer.getString("DownloadOverview_GoodCandidates"));
        panelBuilder.add(label, cc.xy(1, 13));
        goodCandidatesLabel = new JLabel();
        panelBuilder.add(goodCandidatesLabel, cc.xy(3, 13));
        label = new JLabel(Localizer.getString("DownloadOverview_BadCandidates"));
        panelBuilder.add(label, cc.xy(5, 13));
        badCandidatesLabel = new JLabel();
        panelBuilder.add(badCandidatesLabel, cc.xy(7, 13));
        label = new JLabel(Localizer.getString("DownloadOverview_TotalCandidates"));
        panelBuilder.add(label, cc.xy(9, 13));
        totalCandidatesLabel = new JLabel();
        panelBuilder.add(totalCandidatesLabel, cc.xy(11, 13));
        return subPanel;
    }

    private JPanel buildInfo2Panel() {
        JPanel subPanel = new JPanel();
        CellConstraints cc = new CellConstraints();
        String systemExtraCols = "";
        if (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_MAC_OSX) {
            systemExtraCols = ", 4dlu, d";
        }
        FormLayout layout = new FormLayout("d, 4dlu, 1dlu:grow" + systemExtraCols, "p, 2dlu, p, 2dlu, p, 2dlu, p, 2dlu, p");
        PanelBuilder panelBuilder = new PanelBuilder(layout, subPanel);
        panelBuilder.addSeparator(Localizer.getString("DownloadOverview_Information"), cc.xywh(1, 1, layout.getColumnCount(), 1));
        JLabel label = new JLabel(Localizer.getString("DownloadOverview_FileName"));
        panelBuilder.add(label, cc.xy(1, 3));
        fileNameTxt = new JTextField();
        fileNameTxt.setEditable(false);
        fileNameTxt.setFont(UIManager.getFont("Label.font"));
        fileNameTxt.setForeground(UIManager.getColor("Label.foreground"));
        fileNameTxt.setBackground(UIManager.getColor("Label.background"));
        fileNameTxt.setMinimumSize(new Dimension(0, 0));
        panelBuilder.add(fileNameTxt, cc.xy(3, 3));
        label = new JLabel(Localizer.getString("DownloadOverview_IncompleteFile"));
        panelBuilder.add(label, cc.xy(1, 5));
        incompleteFileTxt = new JTextField();
        incompleteFileTxt.setEditable(false);
        incompleteFileTxt.setFont(UIManager.getFont("Label.font"));
        incompleteFileTxt.setForeground(UIManager.getColor("Label.foreground"));
        incompleteFileTxt.setBackground(UIManager.getColor("Label.background"));
        panelBuilder.add(incompleteFileTxt, cc.xy(3, 5));
        if (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_MAC_OSX) {
            exploreFileBtn = new JButton();
            exploreFileBtn.setToolTipText(Localizer.getString("DownloadOverview_Explore"));
            exploreFileBtn.setMargin(GUIUtils.EMPTY_INSETS);
            exploreFileBtn.addActionListener(new ExploreActionListener());
            panelBuilder.add(exploreFileBtn, cc.xy(5, 5));
        }
        return subPanel;
    }

    private void setupIcons() {
        IconPack iconPack = GUIRegistry.getInstance().getPlafIconPack();
        defProgressIcon = iconPack.getIcon("Download.Overview.Progress");
        twinkleProgressIcon = iconPack.getIcon("Download.Overview.ProgressAni");
        progressIconLabel.setIcon(defProgressIcon);
        if (exploreFileBtn != null) {
            exploreFileBtn.setIcon(iconPack.getIcon("Download.Overview.Explore"));
        }
    }

    public void updateDownloadFileInfo(SWDownloadFile file) {
        lastDownloadFile = file;
        if (lastDownloadFile != null) {
            lastFinBufScopeLength = lastDownloadFile.getMemoryFile().getDownloadedLength();
        } else {
            lastFinBufScopeLength = -1;
        }
        updateInterface();
    }

    private void updateInterface() {
        if (lastDownloadFile == null) {
            progressBar.setProvider(null);
            progressLabel.setText("");
            downloadedLabel.setText("");
            downloadedLabel.setToolTipText("");
            remainingLabel.setText("");
            remainingLabel.setToolTipText("");
            totalSizeLabel.setText("");
            totalSizeLabel.setToolTipText("");
            createdLabel.setText("");
            lastDownloadedLabel.setText("");
            downloadRateLabel.setText("");
            downloadRateLabel.setToolTipText("");
            maxRateLabel.setText("");
            etaLabel.setText("");
            downloadingCandidatesLabel.setText("");
            queuedCandidatesLabel.setText("");
            connectingCandidatesLabel.setText("");
            goodCandidatesLabel.setText("");
            badCandidatesLabel.setText("");
            totalCandidatesLabel.setText("");
            fileNameTxt.setText("");
            incompleteFileTxt.setText("");
            return;
        }
        progressBar.setProvider(new DownloadFileScopeProvider(lastDownloadFile));
        progressLabel.setText(lastDownloadFile.getProgress().toString() + " %");
        downloadedLabel.setText(NumberFormatUtils.formatSignificantByteSize(lastDownloadFile.getTransferredDataSize()));
        downloadedLabel.setToolTipText(NumberFormatUtils.formatFullByteSize(lastDownloadFile.getTransferredDataSize()));
        long remaining = lastDownloadFile.getTotalDataSize() - lastDownloadFile.getTransferredDataSize();
        remainingLabel.setText(NumberFormatUtils.formatSignificantByteSize(remaining));
        remainingLabel.setToolTipText(NumberFormatUtils.formatFullByteSize(remaining));
        totalSizeLabel.setText(NumberFormatUtils.formatSignificantByteSize(lastDownloadFile.getTotalDataSize()));
        totalSizeLabel.setToolTipText(NumberFormatUtils.formatFullByteSize(lastDownloadFile.getTotalDataSize()));
        createdLabel.setText(dateFormat.format(lastDownloadFile.getCreatedDate()));
        lastDownloadedLabel.setText(dateFormat.format(lastDownloadFile.getDownloadedDate()));
        downloadRateLabel.setText(NumberFormatUtils.formatSignificantByteSize(lastDownloadFile.getTransferSpeed()) + Localizer.getString("PerSec"));
        downloadRateLabel.setToolTipText(NumberFormatUtils.formatFullByteSize(lastDownloadFile.getTransferSpeed()) + Localizer.getString("PerSec"));
        long maxRate = lastDownloadFile.getDownloadThrottlingRate();
        String maxRateStr;
        if (maxRate >= Integer.MAX_VALUE) {
            maxRateStr = Localizer.getDecimalFormatSymbols().getInfinity();
        } else {
            maxRateStr = NumberFormatUtils.formatSignificantByteSize(maxRate) + Localizer.getString("PerSec");
        }
        maxRateLabel.setText(maxRateStr);
        downloadingCandidatesLabel.setText(String.valueOf(lastDownloadFile.getDownloadingCandidatesCount()));
        queuedCandidatesLabel.setText(String.valueOf(lastDownloadFile.getQueuedCandidatesCount()));
        connectingCandidatesLabel.setText(String.valueOf(lastDownloadFile.getConnectingCandidatesCount()));
        goodCandidatesLabel.setText(String.valueOf(lastDownloadFile.getGoodCandidateCount()));
        badCandidatesLabel.setText(String.valueOf(lastDownloadFile.getBadCandidateCount()));
        totalCandidatesLabel.setText(String.valueOf(lastDownloadFile.getCandidatesCount()));
        String destFile = lastDownloadFile.getFileName();
        if (!fileNameTxt.getText().equals(destFile)) {
            fileNameTxt.setText(destFile);
            fileNameTxt.setCaretPosition(0);
        }
        try {
            String path = lastDownloadFile.getIncompleteDownloadFile().getAbsolutePath();
            if (!incompleteFileTxt.getText().equals(path)) {
                incompleteFileTxt.setText(path);
                incompleteFileTxt.setCaretPosition(0);
            }
        } catch (ManagedFileException exp) {
            NLogger.error(DownloadOverviewPanel.class, exp);
        } catch (FileHandlingException exp) {
            NLogger.error(DownloadOverviewPanel.class, exp);
        }
        long curFinBufScopeLength = lastDownloadFile.getMemoryFile().getDownloadedLength();
        if (lastFinBufScopeLength + NumberFormatUtils.ONE_MB < curFinBufScopeLength) {
            lastFinBufScopeLength = curFinBufScopeLength;
            progressIconLabel.setIcon(twinkleProgressIcon);
            resetProgressTwinkleTimer.restart();
        }
    }

    public class ExploreActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (lastDownloadFile == null) {
                return;
            }
            File file = null;
            try {
                file = lastDownloadFile.getIncompleteDownloadFile().getFile();
            } catch (ManagedFileException exp) {
                NLogger.error(DownloadOverviewPanel.class, exp);
            } catch (FileHandlingException exp) {
                NLogger.error(DownloadOverviewPanel.class, exp);
            }
            if (file == null) {
                return;
            }
            File dir = file.getParentFile();
            try {
                SystemShellExecute.exploreFolder(dir);
            } catch (IOException exp) {
            }
        }
    }
}
