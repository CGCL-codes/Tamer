package org.vikamine.gui.subgroup.visualization.stratification;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import org.vikamine.app.VIKAMINE;
import org.vikamine.kernel.subgroup.analysis.CFSGStratification;
import org.vikamine.kernel.subgroup.analysis.CFStratifiedStatisticsCreator;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author atzmueller
 */
public class CFStratificationVisualizationComponent {

    static class XLSFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getPath().toLowerCase().endsWith(".xls");
        }

        @Override
        public String getDescription() {
            return "XLS-Files";
        }
    }

    private CFStratifiedStatisticsCreator.CFStratifiedStatistics stats;

    private CFSGStratification cfSGStratification;

    JFrame cfFrame;

    CFStratificationSGHoldersTableModel model;

    public CFStratificationVisualizationComponent(CFStratifiedStatisticsCreator.CFStratifiedStatistics stats, CFSGStratification cfSGStratification) {
        super();
        this.stats = stats;
        this.cfSGStratification = cfSGStratification;
        cfFrame = new JFrame();
        model = new CFStratificationSGHoldersTableModel(this.cfSGStratification);
    }

    public void show() {
        cfFrame.getContentPane().setLayout(new BorderLayout());
        cfFrame.setTitle(VIKAMINE.I18N.getString("vikamine.cfStratifiedAnalysisDialog.title"));
        JPanel panel = new JPanel(new FormLayout("15dlu, d, 3dlu, d, f:300px:g", "5dlu, d, 3dlu, d, 3dlu, d, 3dlu, d, 10dlu, f:300px:g"));
        CellConstraints cc = new CellConstraints();
        String attributeVerbalization = stats.getStratifyingAttribute().getDescription();
        JLabel strataDescription = new JLabel(VIKAMINE.I18N.getString("vikamine.cfStratifiedAnalysisDialog.strataDescription") + ": " + attributeVerbalization);
        panel.add(strataDescription, cc.xy(2, 2));
        JLabel tvDescription = new JLabel(VIKAMINE.I18N.getString("vikamine.cfStratifiedAnalysisDialog.tv") + ": " + (stats.getSg().getTarget() != null ? stats.getSg().getTarget().getDescription() : VIKAMINE.I18N.getString("vikamine.cfStratifiedAnalysisDialog.graphPanel.noTV")));
        panel.add(tvDescription, cc.xy(2, 4));
        JLabel sgDescription = new JLabel(VIKAMINE.I18N.getString("vikamine.cfStratifiedAnalysisDialog.sg") + ": " + stats.getSg().getSGDescription().getDescription());
        panel.add(sgDescription, cc.xy(2, 6));
        DefaultComponentFactory cf = DefaultComponentFactory.getInstance();
        panel.add(cf.createSeparator(""), cc.xyw(2, 7, 4));
        final CFStratificationGraphpanel stratGraphPanel = new CFStratificationGraphpanel(stats);
        JCheckBox enableSGPopStuffCheckBox = new JCheckBox(VIKAMINE.I18N.getString("vikamine.cfStratifiedAnalysisDialog.enableSGInPop"));
        enableSGPopStuffCheckBox.setSelected(stratGraphPanel.isEnableSGInPopStrata());
        enableSGPopStuffCheckBox.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                stratGraphPanel.setEnableSGInPopStrata(((JCheckBox) e.getSource()).getModel().isSelected());
                stratGraphPanel.repaint();
            }
        });
        panel.add(enableSGPopStuffCheckBox, cc.xy(2, 8));
        JCheckBox enableTVInComplementaryPopBox = new JCheckBox(VIKAMINE.I18N.getString("vikamine.cfStratifiedAnalysisDialog.enableTVInComplementaryPop"));
        enableTVInComplementaryPopBox.setSelected(stratGraphPanel.isEnableZVinComplPop());
        enableTVInComplementaryPopBox.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                stratGraphPanel.setEnableZVinComplPop(((JCheckBox) e.getSource()).getModel().isSelected());
                stratGraphPanel.repaint();
            }
        });
        panel.add(enableTVInComplementaryPopBox, cc.xy(4, 8));
        panel.add(stratGraphPanel, cc.xyw(2, 10, 4));
        cfFrame.getContentPane().add(panel, BorderLayout.NORTH);
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        final JTable cfSGHoldersTable = new JTable(model);
        tablePanel.add(new JScrollPane(cfSGHoldersTable), BorderLayout.CENTER);
        cfSGHoldersTable.setDefaultRenderer(String.class, new CFStratificationTableCellRenderer());
        cfSGHoldersTable.getTableHeader().setDefaultRenderer(new CFStratificationTableHeaderRenderer(cfSGHoldersTable.getTableHeader().getDefaultRenderer()));
        cfSGHoldersTable.getTableHeader().setReorderingAllowed(false);
        cfSGHoldersTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        JPanel southPanel = new JPanel(new FormLayout("15dlu, fill:pref:grow, 15dlu", "pref, 15dlu"));
        JScrollPane scrollPane = new JScrollPane(cfSGHoldersTable);
        southPanel.add(scrollPane, cc.xy(2, 1));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                CFStratificationVisualizationComponent.this.cfFrame.dispose();
            }
        });
        JButton exportButton = new JButton("Excel Export");
        exportButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                CFStratificationVisualizationComponent.this.excelExport();
            }
        });
        buttonPanel.add(exportButton);
        buttonPanel.add(exitButton);
        cfFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        cfFrame.getContentPane().add(southPanel, BorderLayout.CENTER);
        scrollPane.setPreferredSize(new Dimension(cfSGHoldersTable.getPreferredSize().width, cfSGHoldersTable.getPreferredSize().height + cfSGHoldersTable.getTableHeader().getPreferredSize().height));
        cfFrame.pack();
        cfFrame.setVisible(true);
    }

    private void excelExport() {
        String path = Preferences.userNodeForPackage(getClass()).get("exportContingencyTablesToExcelPath", System.getProperty("user.home"));
        JFileChooser fileChooser = new JFileChooser();
        FileFilter xlsFilter = new XLSFileFilter();
        fileChooser.addChoosableFileFilter(xlsFilter);
        fileChooser.setFileFilter(xlsFilter);
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setCurrentDirectory(new File(path));
        int status = fileChooser.showSaveDialog(cfFrame);
        if (status == JFileChooser.APPROVE_OPTION) {
            File theFile = fileChooser.getSelectedFile();
            Preferences.userNodeForPackage(getClass()).put("exportContingencyTablesToExcelPath", fileChooser.getCurrentDirectory().getPath());
            model.doExcelExport(theFile);
        }
    }
}
