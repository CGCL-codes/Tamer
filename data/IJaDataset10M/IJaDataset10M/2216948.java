package org.vikamine.gui.subgroup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import org.vikamine.app.DMManager;
import org.vikamine.app.VIKAMINE;
import org.vikamine.gui.subgroup.export.SGStatisticsExporter;
import org.vikamine.gui.subgroup.export.SGTuningTableExcelExport;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.SGSet;
import org.vikamine.kernel.subgroup.SGSets;
import org.vikamine.kernel.subgroup.search.AttributeValuesMap;
import org.vikamine.kernel.subgroup.target.SGTarget;
import org.vikamine.kernel.util.VKMUtil;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Note: when ontomanager changes, visible instances cannot show the results of
 * former ontomanagers anymore. But it's suitable to show multiple instances for
 * the same ontomanager
 * 
 * @author atzmueller
 */
public class SubgroupDiscoveryResultsComponent {

    private Map targetToResultMap;

    private Map targetToPanelMap;

    private JFrame internalFrame;

    private AttributeValuesMap attributeValuesMap;

    public SubgroupDiscoveryResultsComponent() {
        super();
    }

    /**
     * @return Returns the targetToPanelMap.
     */
    public Map getTargetToPanelMap() {
        return targetToPanelMap;
    }

    /**
     * @param targetToPanelMap
     *            The targetToPanelMap to set.
     */
    public void setTargetToPanelMap(Map targetToPanelMap) {
        this.targetToPanelMap = targetToPanelMap;
    }

    /**
     * @return Returns the targetToResultMap.
     */
    public Map getTargetToResultMap() {
        return targetToResultMap;
    }

    /**
     * @param targetToResultMap
     *            The targetToResultMap to set.
     */
    public void setTargetToResultMap(Map targetToResultMap) {
        this.targetToResultMap = targetToResultMap;
    }

    public void show() {
        setup();
        internalFrame.pack();
        internalFrame.setMinimumSize(internalFrame.getSize());
        internalFrame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                JFrame tmp = (JFrame) e.getSource();
                if (tmp.getWidth() < internalFrame.getMinimumSize().getWidth() || tmp.getHeight() < internalFrame.getMinimumSize().getHeight()) {
                    tmp.setSize((int) internalFrame.getMinimumSize().getWidth(), (int) internalFrame.getMinimumSize().getHeight());
                }
            }
        });
        internalFrame.setVisible(true);
    }

    private void setup() {
        internalFrame = new JFrame();
        internalFrame.setTitle(VIKAMINE.I18N.getString("vikamine.subgroupAnalysisDiscoverer.title"));
        FormLayout layout = new FormLayout("3dlu, f:max(120dlu;pref):n, 5dlu, f:p:n, 3dlu, f:p:n, 3dlu, f:p:n, f:p:g, 3dlu", "3dlu, c:p:n, 3dlu, c:p:n, 3dlu, b:p:g, 3dlu");
        internalFrame.getContentPane().setLayout(layout);
        final CellConstraints cc = new CellConstraints();
        final JPanel lowerPanel = new JPanel(new BorderLayout());
        JButton exportAllResultsAsTextButton = new JButton();
        exportAllResultsAsTextButton.setAction(new AbstractAction(VIKAMINE.I18N.getString("vikamine.subgroupAnalysisDiscoverer.actions.exportAllSGsAsText")) {

            private static final long serialVersionUID = -2240528153286220569L;

            public void actionPerformed(ActionEvent e) {
                exportAllResultsAsText();
            }
        });
        JButton exportAllResultsAsTuningTableButton = new JButton();
        exportAllResultsAsTuningTableButton.setAction(new AbstractAction(VIKAMINE.I18N.getString("vikamine.SGSetTree.actions.exportAllAsExcelTuningTable")) {

            private static final long serialVersionUID = 5124605662412621946L;

            public void actionPerformed(ActionEvent e) {
                exportAllResultsAsExcelTuningTable();
            }
        });
        JButton moveToInterestingSGs = new JButton();
        moveToInterestingSGs.setAction(new AddAction(getListOfSGs(targetToResultMap)));
        List targetSelectorList = new LinkedList(targetToPanelMap.keySet());
        sortTargetSelectorList(targetSelectorList);
        final JComboBox comboBox = new JComboBox(targetSelectorList.toArray());
        comboBox.setRenderer(new DefaultListCellRenderer() {

            private static final long serialVersionUID = 1023917920982316586L;

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent(list, ((SGTarget) value).getDescription(), index, isSelected, cellHasFocus);
            }
        });
        Object selected = comboBox.getSelectedItem();
        JComponent resultPanel = (JComponent) targetToPanelMap.get(selected);
        lowerPanel.add(resultPanel, BorderLayout.CENTER);
        lowerPanel.setBorder(BorderFactory.createTitledBorder(VIKAMINE.I18N.getString("vikamine.subgroupAnalysisDiscoverer.subgroupBorder.title")));
        comboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object selectedObject = comboBox.getSelectedItem();
                JComponent resultPanel = (JComponent) targetToPanelMap.get(selectedObject);
                lowerPanel.removeAll();
                lowerPanel.add(resultPanel, BorderLayout.CENTER);
                lowerPanel.validate();
                lowerPanel.repaint();
            }
        });
        internalFrame.getContentPane().add(new JLabel(VIKAMINE.I18N.getString("vikamine.subgroupAnalysisDiscoverer.targetVariableBorder.title")), cc.xy(2, 2));
        internalFrame.getContentPane().add(comboBox, cc.xy(2, 4));
        internalFrame.getContentPane().add(new JSeparator(SwingConstants.VERTICAL), cc.xy(3, 4));
        internalFrame.getContentPane().add(exportAllResultsAsTextButton, cc.xy(4, 4));
        internalFrame.getContentPane().add(exportAllResultsAsTuningTableButton, cc.xy(6, 4));
        internalFrame.getContentPane().add(moveToInterestingSGs, cc.xy(8, 4));
        internalFrame.getContentPane().add(lowerPanel, cc.xyw(2, 6, 8));
    }

    static class AddAction extends AbstractAction {

        private static final long serialVersionUID = 6246177549879458479L;

        private List sgNodes;

        public AddAction(List sgNodes) {
            super(VIKAMINE.I18N.getString("vikamine.SGSetTree.actions.addAll"));
            this.sgNodes = sgNodes;
            for (Iterator iter = sgNodes.iterator(); iter.hasNext(); ) {
                SG sg = (SG) iter.next();
                if (!DMManager.getInstance().getSubgroups().contains(sg)) {
                    setEnabled(true);
                    return;
                }
            }
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            DMManager.getInstance().addSubgroups(sgNodes);
        }
    }

    /**
     * @param targetSelectorList
     * @return
     */
    private void sortTargetSelectorList(List targetSelectorList) {
        Collections.sort(targetSelectorList, new Comparator() {

            public int compare(Object o1, Object o2) {
                SGTarget target1 = (SGTarget) o1;
                SGTarget target2 = (SGTarget) o2;
                String sel1String = target1.getDescription();
                String sel2String = target2.getDescription();
                return sel1String.compareTo(sel2String);
            }
        });
    }

    private List getListOfSGs(Map targetToResultMap) {
        List targetSelectorList = new LinkedList(targetToResultMap.keySet());
        List mergedSGSets = new LinkedList();
        for (Iterator iter = targetSelectorList.iterator(); iter.hasNext(); ) {
            SGTarget target = (SGTarget) iter.next();
            List sgSetResults = (List) targetToResultMap.get(target);
            SGSet subgroups = SGSets.mergeSGSetListToUniqueSGUnionSet(sgSetResults);
            mergedSGSets.add(subgroups);
        }
        SGSet mergedSGSet = SGSets.mergeSGSetListToUniqueSGUnionSet(mergedSGSets);
        return VKMUtil.asList(mergedSGSet.iterator());
    }

    /**
	 * 
	 */
    protected void exportAllResultsAsText() {
        List targetSelectorList = new LinkedList(targetToResultMap.keySet());
        sortTargetSelectorList(targetSelectorList);
        String path = Preferences.userNodeForPackage(getClass()).get("loadVidamineExportSGAsTextPath", System.getProperty("user.home"));
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setCurrentDirectory(new File(path));
        int status = fileChooser.showSaveDialog(internalFrame);
        if (status == JFileChooser.APPROVE_OPTION) {
            Preferences.userNodeForPackage(getClass()).put("loadVidamineExportSGAsTextPath", fileChooser.getCurrentDirectory().getPath());
            BufferedWriter out = null;
            try {
                File theFile = fileChooser.getSelectedFile();
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(theFile)));
                SGStatInfoSettings settings = AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().getSubgroupExportAsTextStatInfoSettings();
                SGStatisticsExporter statCreator = new SGStatisticsExporter(settings);
                out.write(statCreator.createSubgroupTSVerbalizationHeader());
                for (Iterator iter = targetSelectorList.iterator(); iter.hasNext(); ) {
                    SGTarget target = (SGTarget) iter.next();
                    List sgSetResults = (List) targetToResultMap.get(target);
                    SGSet subgroupsToExport = SGSets.mergeSGSetListToUniqueSGUnionSet(sgSetResults);
                    statCreator.writeOutSGsAsRules(out, subgroupsToExport);
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "exportAllResultsAsText", ex);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "exportAllResultsAsText", ex);
                    }
                }
            }
        }
    }

    /**
	 * 
	 */
    protected void exportAllResultsAsExcelTuningTable() {
        String path = Preferences.userNodeForPackage(getClass()).get("loadVidamineExportSGAsExcelTuningTablePath", System.getProperty("user.home"));
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setCurrentDirectory(new File(path));
        int status = fileChooser.showSaveDialog(internalFrame);
        if (status == JFileChooser.APPROVE_OPTION) {
            File theFile = fileChooser.getSelectedFile();
            if (!theFile.getName().endsWith(".xls") && !theFile.getName().endsWith(".XLS")) {
                theFile = new File((theFile.getPath() + ".xls"));
            }
            Preferences.userNodeForPackage(getClass()).put("loadVidamineExportSGAsExcelTuningTablePath", fileChooser.getCurrentDirectory().getPath());
            new SGTuningTableExcelExport(DMManager.getInstance().getOntology(), attributeValuesMap).exportTabletoExcel(getListOfSGs(targetToResultMap), theFile);
        }
    }

    public void setAttributeValuesMap(AttributeValuesMap map) {
        this.attributeValuesMap = map;
    }

    public void closeFrame() {
        if (internalFrame.isShowing()) {
            internalFrame.dispose();
        }
    }
}
