package org.codemon.gui.listener;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.codemon.analysis.AnalysisInfo;
import org.codemon.analysis.BranchPoint;
import org.codemon.analysis.ConditionPoint;
import org.codemon.analysis.Project;
import org.codemon.analysis.TestCase;
import org.codemon.gui.CodemonGui;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TreeItem;

/**
 * The SelectionListener for ProjectViewTree.
 * @author Xu Mingming(������)
 * @see CodemonGui
 */
public class ProjectViewTreeListener extends SelectionAdapter {

    private CodemonGui demo;

    public ProjectViewTreeListener(CodemonGui demo) {
        this.demo = demo;
    }

    public void widgetSelected(SelectionEvent e) {
        TreeItem[] items1 = demo.projectViewTree.getSelection();
        if (items1 != null && items1.length > 0 && items1[0].getParentItem() != null && items1[0].getParent() != null) {
            int index2 = demo.getItemIndex(items1[0]);
            int index1 = demo.getItemIndex(items1[0].getParentItem());
            int index0 = demo.getItemIndex(items1[0].getParentItem().getParentItem());
            demo.projectViewIndexes = new ArrayList<Integer>();
            demo.projectViewIndexes.add(index0);
            demo.projectViewIndexes.add(index1);
            demo.projectViewIndexes.add(index2);
            String name = items1[0].getParentItem().getText();
            String testCaseName = items1[0].getText().replaceAll("TestCase", "");
            int number = 0;
            if (items1[0].getParentItem() == null) {
                demo.setCurrentProject(null);
                demo.setCurrentTestCase(null);
                demo.doFileViewRefresh();
                demo.doPointViewRefresh();
                demo.doMenu_ToolBarRefresh();
                demo.doCoverageInfoRefresh();
                return;
            }
            if (items1[0].getItemCount() != 0) {
                name = items1[0].getText();
                demo.setCurrentTestCase(null);
                for (int i = 0; i < demo.getProjects().size(); i++) {
                    Project p = demo.getProjects().get(i);
                    if (name.equals(demo.getProjects().get(i).getName())) {
                        demo.setCurrentProject(p);
                        break;
                    }
                }
                demo.doFileViewRefresh();
                demo.doPointViewRefresh();
                demo.doMenu_ToolBarRefresh();
                demo.doCoverageInfoRefresh();
                {
                    demo.newMenuItem.setEnabled(true);
                    demo.deleteMenuItem.setEnabled(false);
                    demo.newToolItem.setEnabled(true);
                    demo.deleteToolItem.setEnabled(false);
                    demo.startMenuItem.setEnabled(false);
                    demo.startToolItem.setEnabled(false);
                    demo.pauseMenuItem.setEnabled(false);
                    demo.pauseToolItem.setEnabled(false);
                    demo.descriptionToolItem.setEnabled(false);
                    demo.testCaseDescriptionMenuItem.setEnabled(false);
                }
            } else {
                if (testCaseName.equals("All")) {
                    number = 0;
                } else {
                    try {
                        number = Integer.parseInt(testCaseName);
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
                for (int i = 0; i < demo.getProjects().size(); i++) {
                    Project p = demo.getProjects().get(i);
                    if (name.equals(demo.getProjects().get(i).getName())) {
                        demo.setCurrentProject(p);
                        for (int j = 0; j < p.getTestCaseList().size(); j++) {
                            TestCase tc = p.getTestCaseList().get(j);
                            if (number == tc.getNumber()) {
                                demo.setCurrentTestCase(tc);
                                for (AnalysisInfo ai : demo.getCurrentTestCase().getAnalysisInfoList()) {
                                    if (ai.getType().equals(BranchPoint.type)) {
                                        demo.setTestCaseDetermCoverage(ai.getCoveredPointQuantity(), ai.getTotalPointQuantity());
                                    } else if (ai.getType().equals(ConditionPoint.type)) {
                                        demo.setTestCaseCondCoverage(ai.getCoveredPointQuantity(), ai.getTotalPointQuantity());
                                    }
                                }
                                demo.setTestCaseDescription(demo.getCurrentTestCase().getDescription());
                                break;
                            }
                        }
                        break;
                    }
                }
                demo.doFileViewRefresh();
                demo.doPointViewRefresh();
                demo.doMenu_ToolBarRefresh();
                demo.doCoverageInfoRefresh();
            }
        } else {
            demo.setCurrentProject(null);
            demo.setCurrentTestCase(null);
            demo.doFileViewRefresh();
            demo.doPointViewRefresh();
            demo.doMenu_ToolBarRefresh();
            demo.doCoverageInfoRefresh();
        }
    }
}
