package com.enerjy.analyzer.ui.wizard;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import com.enerjy.analyzer.eclipse.AnalyzerActivator;
import com.enerjy.analyzer.ui.CompositeBuilder;
import com.enerjy.analyzer.ui.GuiConstants;
import com.enerjy.common.util.StringUtils;

class BaselinePage extends AbstractWizardPage {

    static final String ID = "baseline";

    private Table table = null;

    private Label prompt = null;

    BaselinePage() {
        super(ID);
    }

    @Override
    protected void internalCreateControl(Composite main) {
        CompositeBuilder builder = new CompositeBuilder(main);
        builder.createTextBlock(getString("intro"));
        table = new Table(main, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = GuiConstants.VERTICAL_BUMP * 5;
        gd.verticalIndent = GuiConstants.VERTICAL_BUMP;
        table.setLayoutData(gd);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        GuiConstants.bumpDown(builder.createTextBlock(getString("intro2")));
        Composite panel = new Composite(main, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.DEFAULT, true, false);
        gd.verticalIndent = GuiConstants.VERTICAL_BUMP;
        panel.setLayoutData(gd);
        GridLayout gridLayout = new GridLayout(3, false);
        gridLayout.marginWidth = 0;
        panel.setLayout(gridLayout);
        prompt = new Label(panel, SWT.LEFT);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        prompt.setLayoutData(gd);
        prompt.setText(MessageFormat.format(getString("prompt"), ""));
        Button yes = new Button(panel, SWT.RADIO);
        yes.setText("Yes");
        Button no = new Button(panel, SWT.RADIO);
        no.setText("No");
        TableColumn col = new TableColumn(table, SWT.RIGHT);
        col.setText("Est. V");
        col = new TableColumn(table, SWT.LEFT);
        col.setText("Rule");
        yes.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                for (TableItem item : table.getItems()) {
                    RuleCount count = (RuleCount) item.getData();
                    count.setAction(RuleCount.Action.BASELINE);
                }
            }
        });
        no.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                for (TableItem item : table.getItems()) {
                    RuleCount count = (RuleCount) item.getData();
                    count.setAction(RuleCount.Action.ENABLE);
                }
            }
        });
        yes.setSelection(true);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            loadTable();
        }
        super.setVisible(visible);
    }

    private void loadTable() {
        table.removeAll();
        AnalysisResults results = ((SetupWizard) getWizard()).getResults();
        if (null == results) {
            return;
        }
        Collection<String> bugRules = new HashSet<String>(Arrays.asList(StringUtils.split(getString("bugs.rules"), ",")));
        RuleCount[] counts = results.getCounts();
        Arrays.sort(counts);
        int projectedCount = 0;
        for (RuleCount count : counts) {
            if (bugRules.contains(count.getKey()) || (RuleCount.Action.DISABLE == count.getAction())) {
                continue;
            }
            TableItem item = new TableItem(table, SWT.NONE);
            int violations = results.calcProjectedViolations(count.getCount());
            projectedCount += violations;
            item.setText(0, String.valueOf(violations));
            item.setText(1, count.getDescription());
            Image image = AnalyzerActivator.getDefault().getPriorityImage(count.getPriority());
            item.setImage(1, image);
            item.setData(count);
        }
        for (TableColumn fix : table.getColumns()) {
            fix.pack();
        }
        prompt.setText(MessageFormat.format(getString("prompt"), projectedCount));
        prompt.getParent().pack(true);
    }
}
