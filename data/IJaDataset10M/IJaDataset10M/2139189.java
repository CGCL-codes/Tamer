package com.nhncorp.cubridqa.result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import com.nhncorp.cubridqa.Activator;
import com.nhncorp.cubridqa.CUBRIDAdvisor;
import com.nhncorp.cubridqa.console.bean.CaseResult;
import com.nhncorp.cubridqa.console.bean.SummaryInfo;
import com.nhncorp.cubridqa.console.util.TestUtil;
import com.nhncorp.cubridqa.swtdesigner.ResourceManager;
import com.nhncorp.cubridqa.utils.FileUtil;
import com.nhncorp.cubridqa.utils.PropertiesUtil;
import com.nhncorp.cubridqa.utils.XstreamHelper;

/**
 * 
 * Display summary as table viewer.
 * @ClassName: ShowSummary
 * @date 2009-9-4
 * @version V1.0 
 * Copyright (C) www.nhn.com
 */
public class ShowSummary extends Composite {

    private TableViewer tableViewer;

    private TableColumn newColumnTableColumn;

    private TableColumn column2;

    private TableColumn column3;

    private Composite parent;

    private RealSummary realSummary;

    private SummaryInfo parentSummaryInfo;

    /**
	 * 
	 * @Description: Display summary under the tree item.
	 * @param parent
	 * @param style
	 * @param view
	 * @param treeItem
	 */
    public ShowSummary(Composite parent, int style, ViewPart view, TreeItem treeItem) {
        super(parent, SWT.NONE);
        this.setLayout(new FillLayout());
        tableViewer = new TableViewer(this, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        tableViewer.getTable().setLinesVisible(true);
        tableViewer.getTable().setHeaderVisible(false);
        tableViewer.getTable().setLayout(new FillLayout());
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new LabelProvider());
        newColumnTableColumn = new TableColumn(tableViewer.getTable(), SWT.NONE);
        newColumnTableColumn.setWidth(parent.getParent().getParent().getBounds().width);
        if (treeItem != null) {
            String path = FileUtil.getFile(treeItem).getAbsolutePath();
            tableViewer.getTable().setHeaderVisible(false);
            tableViewer.setInput(FileUtil.readFile(path + "/summary_info"));
        }
    }

    /**
	 * 
	 * @Description: Create composite under the contents of parent result's
	 *               summary.
	 * @param parent
	 * @param style
	 * @param view
	 * @param parentSummaryInfo
	 */
    public ShowSummary(Composite parent, int style, ViewPart view, final SummaryInfo parentSummaryInfo) {
        super(parent, SWT.NONE);
        this.parent = parent;
        this.parentSummaryInfo = parentSummaryInfo;
        String localPathInResult = parentSummaryInfo.getLocalPath();
        String filePath = PropertiesUtil.getValue("local.path") + parentSummaryInfo.getResultDir().substring(localPathInResult.length());
        String summaryPath = filePath + "/summary.info";
        Object o = XstreamHelper.fromXml(summaryPath);
        SummaryInfo summaryInfo = (SummaryInfo) o;
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);
        final Label total = new Label(this, SWT.NONE);
        final GridData gd_totalLabel = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        total.setLayoutData(gd_totalLabel);
        total.setText("Total: " + summaryInfo.getTotalCount());
        final Label success = new Label(this, SWT.NONE);
        success.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        success.setText("Success: " + (summaryInfo.getSuccessCount()));
        final Label fail = new Label(this, SWT.NONE);
        fail.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        fail.setText("Fail: " + summaryInfo.getFailCount());
        final Label totalTime = new Label(this, SWT.NONE);
        totalTime.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        totalTime.setText("TotalTime: " + summaryInfo.getTotalTime() + "ms");
        tableViewer = new TableViewer(this, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        final GridData gd_table = new GridData(SWT.LEFT, SWT.CENTER, true, true);
        gd_table.horizontalSpan = 2;
        gd_table.horizontalAlignment = GridData.FILL;
        gd_table.verticalAlignment = GridData.FILL;
        tableViewer.getTable().setLayoutData(gd_table);
        tableViewer.getTable().setLinesVisible(true);
        tableViewer.getTable().setHeaderVisible(true);
        tableViewer.getTable().setLayout(new FillLayout());
        newColumnTableColumn = new TableColumn(tableViewer.getTable(), SWT.NONE);
        column2 = new TableColumn(tableViewer.getTable(), SWT.NONE);
        column3 = new TableColumn(tableViewer.getTable(), SWT.NONE);
        newColumnTableColumn.setWidth(50);
        column2.setWidth(50);
        column3.setWidth(parent.getParent().getParent().getBounds().width - 140);
        tableViewer.getTable().setHeaderVisible(false);
        List<CaseResult> okList = summaryInfo.getOkList();
        List<CaseResult> nokList = summaryInfo.getNokList();
        List<CaseResult> notRunList = summaryInfo.getNotRunList();
        List<SummaryInfo> childList = summaryInfo.getChildList();
        List<String> list = new ArrayList<String>();
        if (childList.size() > 0) {
            for (SummaryInfo child : childList) {
                list.add(child.getCatPath());
            }
        } else {
            for (CaseResult caseResult : nokList) {
                list.add(caseResult.getSiteRunTimes() + "::runTimes::" + PropertiesUtil.getValue("local.path") + caseResult.getCaseFile().substring(localPathInResult.length()) + ":nok");
            }
            for (CaseResult caseResult : okList) {
                list.add(caseResult.getSiteRunTimes() + "::runTimes::" + PropertiesUtil.getValue("local.path") + caseResult.getCaseFile().substring(localPathInResult.length()) + ":ok");
            }
            for (CaseResult caseResult : notRunList) {
                list.add(caseResult.getSiteRunTimes() + "::runTimes::" + PropertiesUtil.getValue("local.path") + caseResult.getCaseFile().substring(localPathInResult.length()) + ":norun");
            }
        }
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new TableLabelProvider());
        tableViewer.setInput(list.toArray());
        tableViewer.getTable().addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
                String string = (String) selection.getFirstElement();
                String needCompare = needCompare(string);
                String filePath = getFilePath(string);
                realSummary.setContents(filePath);
                if (string.indexOf(PropertiesUtil.getValue("local.path")) >= 0) {
                    string = string.substring(string.indexOf(PropertiesUtil.getValue("local.path")), string.lastIndexOf(":"));
                    ResultView view = (ResultView) CUBRIDAdvisor.showView(ResultView.ID);
                    string = string.substring(0, string.lastIndexOf("."));
                    String resultPath = "";
                    resultPath = PropertiesUtil.getValue("local.path") + parentSummaryInfo.getResultDir().substring(parentSummaryInfo.getLocalPath().length()) + "/" + string.substring(string.lastIndexOf("/") + 1, string.length()) + ".result";
                    if (new File(resultPath).exists()) {
                        view.setResultFilePath(resultPath);
                    } else {
                        view.setResultFilePath(string + ".result");
                    }
                    String answerPath = null;
                    String tmpPath = string.replaceAll("\\\\", "/").replaceAll("/cases", "/" + TestUtil.OTHER_ANSWERS_32) + ".answer";
                    if (com.nhncorp.cubridqa.console.util.FileUtil.isFileExist(tmpPath)) {
                        answerPath = tmpPath;
                    } else {
                        answerPath = string.replaceAll("\\\\", "/").replaceAll("/cases", "/answers") + ".answer";
                    }
                    view.setAnswerFilePath(answerPath);
                    view.setContents("compareResult", needCompare);
                }
            }
        });
    }

    private String getFilePath(String input) {
        int last = input.lastIndexOf(":");
        String result = input.substring(0, last);
        int first = result.lastIndexOf(":");
        result = result.substring(first + 1);
        return result;
    }

    private String needCompare(String input) {
        String[] strings = input.split(":");
        return strings[strings.length - 1];
    }

    /**
	 * 
	 * @Description: Display the contents of a specified result file.
	 * @param parent
	 * @param style
	 * @param view
	 * @param filePath
	 */
    public ShowSummary(Composite parent, int style, ViewPart view, final String filePath) {
        super(parent, SWT.NONE);
        String summaryPath = filePath + "/summary.info";
        Object o = XstreamHelper.fromXml(summaryPath);
        SummaryInfo summaryInfo = (SummaryInfo) o;
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);
        final Label total = new Label(this, SWT.NONE);
        final GridData gd_totalLabel = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        total.setLayoutData(gd_totalLabel);
        total.setText("Total: " + summaryInfo.getTotalCount());
        final Label success = new Label(this, SWT.NONE);
        success.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        success.setText("Success: " + (summaryInfo.getSuccessCount()));
        final Label fail = new Label(this, SWT.NONE);
        fail.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        fail.setText("Fail: " + summaryInfo.getFailCount());
        final Label totalTime = new Label(this, SWT.NONE);
        totalTime.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        totalTime.setText("TotalTime: " + summaryInfo.getTotalTime() + "ms");
        tableViewer = new TableViewer(this, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
        final GridData gd_table = new GridData(SWT.LEFT, SWT.CENTER, true, true);
        gd_table.horizontalSpan = 2;
        gd_table.horizontalAlignment = GridData.FILL;
        gd_table.verticalAlignment = GridData.FILL;
        tableViewer.getTable().setLayoutData(gd_table);
        tableViewer.getTable().setLinesVisible(true);
        tableViewer.getTable().setHeaderVisible(false);
        tableViewer.getTable().setLayout(new FillLayout());
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new LabelProvider());
        newColumnTableColumn = new TableColumn(tableViewer.getTable(), SWT.NONE);
        newColumnTableColumn.setWidth(parent.getParent().getParent().getBounds().width);
        tableViewer.getTable().setHeaderVisible(false);
        List<CaseResult> okList = summaryInfo.getOkList();
        List<CaseResult> nokList = summaryInfo.getNokList();
        List<CaseResult> notRunList = summaryInfo.getNotRunList();
        List<SummaryInfo> childList = summaryInfo.getChildList();
        List<String> list = new ArrayList<String>();
        if (childList.size() > 0) {
            for (SummaryInfo child : childList) {
                list.add(child.getCatPath());
            }
        } else {
            for (CaseResult caseResult : okList) {
                list.add(caseResult.getCaseFile() + ":ok");
            }
            for (CaseResult caseResult : nokList) {
                list.add(caseResult.getCaseFile() + ":nok");
            }
            for (CaseResult caseResult : notRunList) {
                list.add(caseResult.getCaseFile() + ":norun");
            }
        }
        tableViewer.setInput(list.toArray());
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new TableLabelProvider());
        tableViewer.getTable().addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
                String string = (String) selection.getFirstElement();
                realSummary.setContents(getFilePath(string));
                if (string.indexOf(PropertiesUtil.getValue("local.path")) >= 0) {
                    string = string.substring(0, string.lastIndexOf(":"));
                    ResultView view = (ResultView) CUBRIDAdvisor.showView(ResultView.ID);
                    string = string.substring(0, string.lastIndexOf("."));
                    String resultPath = "";
                    resultPath = filePath + "/" + string.substring(string.lastIndexOf("/") + 1, string.length()) + ".result";
                    if (new File(resultPath).exists()) {
                        view.setResultFilePath(resultPath);
                    } else {
                        view.setResultFilePath(string + ".result");
                    }
                    String answerPath = null;
                    String tmpPath = string.replaceAll("\\\\", "/").replaceAll("/cases", "/" + TestUtil.OTHER_ANSWERS_32) + ".answer";
                    if (com.nhncorp.cubridqa.console.util.FileUtil.isFileExist(tmpPath)) {
                        answerPath = tmpPath;
                    } else {
                        answerPath = string.replaceAll("\\\\", "/").replaceAll("/cases", "/answers") + ".answer";
                    }
                    view.setAnswerFilePath(answerPath);
                    view.setContents("compareResult", "");
                }
            }
        });
    }

    class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

        public String getColumnText(Object element, int columnIndex) {
            if (element != null) {
                String string = (String) element;
                String content = "";
                if (string.endsWith(":ok") || string.endsWith(":nok") || string.endsWith(":norun")) {
                    String[] strings = string.split("::runTimes::");
                    newColumnTableColumn.setWidth(50);
                    switch(columnIndex) {
                        case 1:
                            content = strings[0];
                            break;
                        case 2:
                            content = strings[1].substring(PropertiesUtil.getValue("local.path").length(), strings[1].lastIndexOf(":"));
                            break;
                    }
                } else {
                    newColumnTableColumn.setWidth(parent.getParent().getParent().getBounds().width);
                    if (columnIndex == 0) {
                        content = string;
                    }
                }
                return content;
            }
            return null;
        }

        public Image getColumnImage(Object element, int columnIndex) {
            if (columnIndex == 0 && element != null) {
                String string = (String) element;
                String gif = null;
                if (string.endsWith(":nok")) {
                    gif = "icons/fail.png";
                } else if (string.endsWith(":ok")) {
                    gif = "icons/success.png";
                } else if (string.endsWith(":norun")) {
                    gif = "icons/noexecute.png";
                }
                if (gif != null) {
                    return ResourceManager.getPluginImage(Activator.getDefault(), gif);
                }
            }
            return null;
        }
    }

    public RealSummary getRealSummary() {
        return realSummary;
    }

    public void setRealSummary(RealSummary realSummary) {
        this.realSummary = realSummary;
    }

    public TableViewer getTableViewer() {
        return tableViewer;
    }

    public void setTableViewer(TableViewer tableViewer) {
        this.tableViewer = tableViewer;
    }

    public SummaryInfo getParentSummaryInfo() {
        return parentSummaryInfo;
    }

    public void setParentSummaryInfo(SummaryInfo parentSummaryInfo) {
        this.parentSummaryInfo = parentSummaryInfo;
    }
}
