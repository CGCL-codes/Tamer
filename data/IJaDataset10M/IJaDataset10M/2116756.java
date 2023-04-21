package org.gerhardb.jibs.optimizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.gerhardb.jibs.Jibs;
import org.gerhardb.jibs.util.FileLoopHelper;
import org.gerhardb.lib.dirtree.rdp.PathManager;
import org.gerhardb.lib.io.DirectoriesOnlyFileFilter;
import org.gerhardb.lib.swing.JPanelRows;
import com.saic.isd.printscreen.Util;
import com.saic.isd.util.report.Report;

/**
 * User interface for the directory utilities.
 */
public class OptimizerPanel extends JPanelRows {

    Optimizer myOptimizer;

    JProgressBar myProgressBar = new JProgressBar();

    JCheckBox myDedupCheckBox = new JCheckBox(Jibs.getString("DownloadPanel.0"));

    JCheckBox myAcrossCheckBox = new JCheckBox(Jibs.getString("DownloadPanel.1"));

    JCheckBox myRenameUniqueCheckBox = new JCheckBox(Jibs.getString("DownloadPanel.2"));

    JCheckBox myEnforceWindows = new JCheckBox(Jibs.getString("DownloadPanel.3"));

    JCheckBox myCleanUpEnding = new JCheckBox(Jibs.getString("DownloadPanel.4"));

    JRadioButton myDeleteRadioBtn = new JRadioButton(Jibs.getString("DownloadPanel.5"));

    JRadioButton myMoveRadioBtn;

    JRadioButton myDoNothingBtn = new JRadioButton(Jibs.getString("DownloadPanel.7"));

    JCheckBox myKillSmallFilesCheckBox = new JCheckBox("Remove");

    JCheckBox myBothSmallerCheckBox = new JCheckBox("Both dimensions must be smaller");

    JFormattedTextField myKillHeight = new JFormattedTextField(new Integer(0));

    JFormattedTextField myKillWidth = new JFormattedTextField(new Integer(0));

    JLabel myCurrentAction = new JLabel(Jibs.getString("DownloadPanel.10"));

    JCheckBox mySkipRecheckBox = new JCheckBox("Don't reprocess previously renamed files");

    JButton myStartBtn = new JButton(Jibs.getString("DownloadPanel.11"));

    JButton myStopBtn = new JButton(Jibs.getString("DownloadPanel.12"));

    File myLogFile;

    private static final int DIR_COL_WIDTH = 50;

    JFormattedTextField pictureNumberFld;

    JFormattedTextField logNumberFld;

    JTextField logFld;

    public OptimizerPanel(Optimizer optimizer) {
        super();
        this.myOptimizer = optimizer;
        this.myMoveRadioBtn = new JRadioButton(Jibs.getString("DownloadPanel.6") + ":    " + this.myOptimizer.myRDPplugins.getPathManager().getDirectoryAbsolute(PathManager.DIR_PARK));
        ButtonGroup moveOrStrainGroup = new ButtonGroup();
        moveOrStrainGroup.add(this.myDoNothingBtn);
        moveOrStrainGroup.add(this.myDeleteRadioBtn);
        moveOrStrainGroup.add(this.myMoveRadioBtn);
        this.pictureNumberFld = FileLoopHelper.makeRelabelFld();
        this.logNumberFld = FileLoopHelper.makeRelabelFld();
        this.pictureNumberFld.setValue(new Integer(OptimizerPreferences.getRelabelNumber()));
        this.pictureNumberFld.setColumns(10);
        this.logNumberFld.setValue(new Integer(OptimizerPreferences.getLogNumber()));
        this.logNumberFld.setColumns(10);
        this.logFld = new JTextField(OptimizerPreferences.getLogDirectory());
        this.logFld.setColumns(DIR_COL_WIDTH);
        this.logFld.setToolTipText(Jibs.getString("DownloadPanel.16"));
        this.myDedupCheckBox.setSelected(OptimizerPreferences.getDedup());
        this.myAcrossCheckBox.setSelected(OptimizerPreferences.getDirectoryAcross());
        this.myRenameUniqueCheckBox.setSelected(OptimizerPreferences.getUniqueRenamingDownload());
        this.myEnforceWindows.setSelected(OptimizerPreferences.getWindowsFileConvention());
        this.myCleanUpEnding.setSelected(OptimizerPreferences.getFirstEndingConvention());
        switch(OptimizerPreferences.getStrain()) {
            case OptimizerPreferences.STRAIN_NOTHING:
                this.myDoNothingBtn.setSelected(true);
                break;
            case OptimizerPreferences.STRAIN_MOVE:
                this.myMoveRadioBtn.setSelected(true);
                break;
            case OptimizerPreferences.STRAIN_DELETE:
                this.myDeleteRadioBtn.setSelected(true);
                break;
        }
        this.myKillSmallFilesCheckBox.setSelected(OptimizerPreferences.getKill());
        this.myBothSmallerCheckBox.setSelected(OptimizerPreferences.getBothSmaller());
        this.myKillWidth.setText(Integer.toString(OptimizerPreferences.getKillWidth()));
        this.myKillWidth.setPreferredSize(new java.awt.Dimension(40, 21));
        this.myKillWidth.setMinimumSize(new java.awt.Dimension(40, 21));
        this.myKillHeight.setText(Integer.toString(OptimizerPreferences.getKillHeight()));
        this.myKillHeight.setPreferredSize(new java.awt.Dimension(40, 21));
        this.myKillHeight.setMinimumSize(new java.awt.Dimension(40, 21));
        this.mySkipRecheckBox.setSelected(OptimizerPreferences.getSkipRecheck());
        this.mySkipRecheckBox.setToolTipText("Skip the file if it has been processed before.\nOnly works if you checked \"Rename Unique\"\nThis check for \"" + FileLoopHelper.RELABEL_POSTFIX + "\" in the name.\nDoes not affect removing duplicates.");
        adjustForPreferences();
        if (OptimizerPreferences.getUniqueRenamingDownload()) {
            this.myRenameUniqueCheckBox.setSelected(true);
            this.pictureNumberFld.setEditable(true);
        } else {
            this.myRenameUniqueCheckBox.setSelected(false);
            this.pictureNumberFld.setEditable(false);
        }
        this.myRenameUniqueCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                OptimizerPanel.this.pictureNumberFld.setEditable(OptimizerPanel.this.myRenameUniqueCheckBox.isSelected());
            }
        });
        JButton btnLog = new JButton("...");
        btnLog.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                String dir = OptimizerPanel.this.logFld.getText();
                if (dir == null) {
                    dir = System.getProperty("user.home");
                }
                dir = DirectoriesOnlyFileFilter.lookupDir(Jibs.getString("DownloadPanel.20"), dir, OptimizerPanel.this);
                OptimizerPanel.this.logFld.setText(dir);
                OptimizerPreferences.setLogDirectory(dir);
            }
        });
        JPanel dedupPanel = new JPanel();
        dedupPanel.add(this.myDedupCheckBox);
        dedupPanel.add(this.myAcrossCheckBox);
        dedupPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), Jibs.getString("DownloadPanel.21"), TitledBorder.LEFT, TitledBorder.TOP));
        JPanelRows strainPanel = new JPanelRows();
        strainPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), Jibs.getString("DownloadPanel.22"), TitledBorder.LEFT, TitledBorder.TOP));
        JPanel aRow = strainPanel.topRow();
        aRow.add(this.myDoNothingBtn);
        aRow = strainPanel.nextRow();
        aRow.add(this.myMoveRadioBtn);
        aRow = strainPanel.nextRow();
        aRow.add(this.myDeleteRadioBtn);
        JPanelRows renamePanel = new JPanelRows();
        aRow = renamePanel.topRow();
        renamePanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), Jibs.getString("DownloadPanel.24"), TitledBorder.LEFT, TitledBorder.TOP));
        aRow.add(this.myRenameUniqueCheckBox);
        aRow.add(new JLabel(Jibs.getString("DownloadPanel.70") + ": "));
        aRow.add(this.pictureNumberFld);
        aRow = renamePanel.nextRow();
        aRow.add(this.myCleanUpEnding);
        aRow.add(this.myEnforceWindows);
        JPanelRows killPanel = new JPanelRows();
        killPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Remove Small Files", TitledBorder.LEFT, TitledBorder.TOP));
        aRow = killPanel.topRow();
        aRow.add(new JLabel("Warning: This will delete without going into recycle bin."));
        aRow = killPanel.nextRow();
        aRow.add(this.myKillSmallFilesCheckBox);
        aRow.add(new JLabel("    " + Jibs.getString("DownloadPanel.28") + ":  "));
        aRow.add(this.myKillWidth);
        aRow.add(new JLabel("  " + Jibs.getString("DownloadPanel.31") + ":  "));
        aRow.add(this.myKillHeight);
        aRow.add(this.myBothSmallerCheckBox);
        JPanel recheckPanel = new JPanel();
        recheckPanel.add(this.mySkipRecheckBox);
        recheckPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Recheck Options", TitledBorder.LEFT, TitledBorder.TOP));
        JPanelRows logPanel = new JPanelRows();
        logPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), Jibs.getString("DownloadPanel.38"), TitledBorder.LEFT, TitledBorder.TOP));
        aRow = logPanel.topRow();
        aRow.add(new JLabel(Jibs.getString("DownloadPanel.39") + ": "));
        aRow.add(this.logNumberFld);
        aRow = logPanel.nextRow();
        aRow.add(new JLabel(Jibs.getString("DownloadPanel.41") + ": "));
        aRow.add(this.logFld);
        aRow.add(btnLog);
        JPanel currentRow = this.topRow();
        currentRow.add(dedupPanel);
        currentRow.add(recheckPanel);
        currentRow = this.nextRow();
        currentRow.add(strainPanel);
        currentRow = this.nextRow();
        currentRow.add(renamePanel);
        currentRow.add(killPanel);
        currentRow = this.nextRow();
        currentRow.add(logPanel);
        currentRow = this.nextRow();
        currentRow.add(layoutAction());
    }

    private JPanel layoutAction() {
        final JButton btnReport = new JButton(Jibs.getString("report"));
        btnReport.setEnabled(false);
        JPanelRows rowMaker = new JPanelRows();
        JPanel currentRow = rowMaker.topRow();
        currentRow.add(this.myStartBtn);
        currentRow.add(this.myStopBtn);
        currentRow.add(btnReport);
        currentRow = rowMaker.nextRow();
        currentRow.add(new JLabel("  " + Jibs.getString("DownloadPanel.45") + ": "));
        currentRow.add(this.myCurrentAction);
        currentRow = rowMaker.nextRow();
        currentRow.add(this.myProgressBar);
        Dimension dim = this.myProgressBar.getPreferredSize();
        dim.width = 500;
        this.myProgressBar.setPreferredSize(dim);
        this.myStartBtn.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OptimizerPanel.this.myOptimizer.myKiller.setButtons(OptimizerPanel.this.myStartBtn, OptimizerPanel.this.myStopBtn);
                workDownload();
                save();
                btnReport.setEnabled(true);
            }
        });
        this.myStopBtn.setEnabled(false);
        this.myStopBtn.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OptimizerPanel.this.myOptimizer.myKiller.kill();
            }
        });
        btnReport.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    String text = Util.getTextFromFile(OptimizerPanel.this.myLogFile);
                    Report.showReport(Jibs.getString("DownloadPanel.47"), text, false);
                } catch (Exception ex) {
                    Report.showReport(Jibs.getString("DownloadPanel.48"), ex.getMessage(), false);
                }
            }
        });
        JPanel rtnMe = new JPanel(new BorderLayout());
        rtnMe.add(rowMaker, BorderLayout.NORTH);
        return rtnMe;
    }

    void adjustForPreferences() {
        if (this.myOptimizer.myRDPplugins.getPathManager().getDirectoryAbsolute(PathManager.DIR_PARK) == null) {
            this.myMoveRadioBtn.setEnabled(false);
            this.myMoveRadioBtn.setSelected(false);
            this.myMoveRadioBtn.setToolTipText(Jibs.getString("DownloadPanel.49"));
        } else {
            this.myMoveRadioBtn.setEnabled(true);
            this.myMoveRadioBtn.setToolTipText(Jibs.getString("DownloadPanel.50"));
        }
    }

    /**
    */
    void workDownload() {
        int logNumber = FileLoopHelper.getIntFromField(this.logNumberFld, 0);
        int nextLogNumber = logNumber + 1;
        this.logNumberFld.setValue(new Integer(Integer.toString(nextLogNumber)));
        String logFileName = OptimizerPreferences.getLogDirectory() + System.getProperty("file.separator") + "jibsLog_" + logNumber + ".txt";
        this.myLogFile = new File(logFileName);
        int killHeight = 0;
        int killWidth = 0;
        try {
            killWidth = NumberFormat.getIntegerInstance().parse(this.myKillWidth.getText()).intValue();
            killHeight = NumberFormat.getIntegerInstance().parse(this.myKillHeight.getText()).intValue();
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), Jibs.getString("DownloadPanel.55"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        OptimizerPreferences.setBothSmaller(this.myBothSmallerCheckBox.isSelected());
        OptimizerPreferences.setKill(this.myKillSmallFilesCheckBox.isSelected());
        OptimizerPreferences.setKillWidth(killWidth);
        OptimizerPreferences.setKillHeight(killHeight);
        OptimizerPreferences.setDedup(this.myDedupCheckBox.isSelected());
        OptimizerPreferences.setDirectoryAcross(this.myAcrossCheckBox.isSelected());
        OptimizerPreferences.setUniqueRenamingDownload(this.myRenameUniqueCheckBox.isSelected());
        OptimizerPreferences.setWindowsFileConvention(this.myEnforceWindows.isSelected());
        OptimizerPreferences.setFirstEndingConvention(this.myCleanUpEnding.isSelected());
        OptimizerPreferences.setStrain(getStrain());
        OptimizerPreferences.setSkipRecheck(this.mySkipRecheckBox.isSelected());
        this.myCurrentAction.setText("");
        try {
            FileLoopHelper helper = new FileLoopHelper(this.myOptimizer, "JIBS", this.pictureNumberFld, this.myCurrentAction, this.myProgressBar, this.myLogFile);
            OptimizerWorker worker = new OptimizerWorker(this.myOptimizer.myWorkingList.getDirArray(), this.myOptimizer.myStableList.getDirArray(), this.myDedupCheckBox.isSelected(), this.myAcrossCheckBox.isSelected(), this.myRenameUniqueCheckBox.isSelected(), this.myEnforceWindows.isSelected(), this.myCleanUpEnding.isSelected(), getStrain(), helper, this.myOptimizer.myKiller, this.myOptimizer.myRDPplugins, this.mySkipRecheckBox.isSelected(), this.myBothSmallerCheckBox.isSelected(), this.myKillSmallFilesCheckBox.isSelected(), killWidth, killHeight);
            Thread t = new Thread(worker);
            t.start();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), Jibs.getString("DownloadPanel.58"), JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    int getStrain() {
        if (this.myMoveRadioBtn.isSelected()) {
            return OptimizerPreferences.STRAIN_MOVE;
        } else if (this.myDeleteRadioBtn.isSelected()) {
            return OptimizerPreferences.STRAIN_DELETE;
        }
        return OptimizerPreferences.STRAIN_NOTHING;
    }

    void save() {
        String log = this.logFld.getText();
        if (log != null && log.length() > 0 && !FileLoopHelper.checkDirectory(log, this.myStartBtn)) {
            JOptionPane.showMessageDialog(this.myStartBtn, Jibs.getString("DownloadPanel.59"), Jibs.getString("save.failed"), JOptionPane.WARNING_MESSAGE);
            return;
        }
        OptimizerPreferences.setLogDirectory(this.logFld.getText());
        try {
            OptimizerPreferences.setRelabelNumber(FileLoopHelper.getIntFromField(this.pictureNumberFld, 0));
            OptimizerPreferences.setLogNumber(FileLoopHelper.getIntFromField(this.logNumberFld, 0));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this.myStartBtn, ex.getMessage(), Jibs.getString("save.failed"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
