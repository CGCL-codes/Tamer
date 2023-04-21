package com.gerodp.view.newWidgets;

import com.gerodp.controller.ArticlesLabeProvider;
import com.gerodp.controller.ArticlesTableContentProvider;
import com.gerodp.controller.Messages;
import com.gerodp.controller.ConfigTestDialog.GenerateWordTestCommand;
import com.gerodp.controller.ConfigTestDialog.SearchArticleForWordTestListener;
import com.gerodp.model.ModelSingleton;
import com.gerodp.view.AppMessagesSingleton;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @name ConfigWordTestDialog
 * @description Dialog for configuring a new test
 * 
 * @author Ger�nimo Di Pierro
 * @creationDate 05/01/2008
 */
public class ConfigWordTestDialog extends Dialog {

    private Text numOfQuestionText;

    private Text startDateSearchText;

    private Text endDateSearchText;

    private Text titleSearchText;

    private TableViewer tableViewer;

    private Table articleTable;

    private GenerateWordTestCommand command;

    public ConfigWordTestDialog(Shell parentShell, GenerateWordTestCommand command) {
        super(parentShell);
        this.command = command;
    }

    private void createContents(final Shell shell) {
        shell.setLayout(new GridLayout(2, true));
        shell.setText(AppMessagesSingleton.getMessage("WordTestTitle"));
        shell.setMinimumSize(800, 600);
        Label explanationLabel = new Label(shell, SWT.WRAP);
        explanationLabel.setText(AppMessagesSingleton.getMessage("TestExplanation"));
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        explanationLabel.setLayoutData(data);
        Group group = new Group(shell, SWT.SHADOW_ETCHED_IN);
        group.setText(AppMessagesSingleton.getMessage("NumberQuestions"));
        GridLayout grid = new GridLayout();
        grid.numColumns = 1;
        group.setLayout(grid);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        group.setLayoutData(data);
        numOfQuestionText = new Text(group, SWT.SINGLE);
        numOfQuestionText.setLayoutData(new GridData());
        numOfQuestionText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                command.setNumberOfQuestions(Integer.parseInt(numOfQuestionText.getText()));
            }
        });
        createSearchPanel(shell);
        createShowList(shell);
        Button generateTest = new Button(shell, SWT.RIGHT);
        generateTest.setText(AppMessagesSingleton.getMessage("GenerateTest"));
        generateTest.addSelectionListener(new ButtonsListener());
        Button cancel = new Button(shell, SWT.RIGHT);
        cancel.setText(AppMessagesSingleton.getMessage("Cancel"));
        cancel.addSelectionListener(new ButtonsListener());
    }

    private class ButtonsListener implements SelectionListener {

        public void widgetDefaultSelected(SelectionEvent arg0) {
        }

        public void widgetSelected(SelectionEvent event) {
            if (((Button) event.getSource()).getText().equals(AppMessagesSingleton.getMessage("GenerateTest"))) {
                if (command.getNumberOfQuestions() > 0 && command.getSelectedArticles().getArticles().size() > 0) {
                    command.setExecute(true);
                    getParentShell().dispose();
                } else {
                    Messages.showError(AppMessagesSingleton.getMessage("ConfigTestError"));
                }
            } else {
                getParentShell().dispose();
            }
        }
    }

    private void createSearchPanel(Composite parent) {
        Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
        group.setText(AppMessagesSingleton.getMessage("GetWords"));
        GridLayout grid = new GridLayout();
        grid.numColumns = 4;
        group.setLayout(grid);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        group.setLayoutData(data);
        Label label = new Label(group, SWT.WRAP);
        label.setText(AppMessagesSingleton.getMessage("StartDate"));
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        label = new Label(group, SWT.WRAP);
        label.setText(AppMessagesSingleton.getMessage("EndDate"));
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        label = new Label(group, SWT.WRAP);
        label.setText(AppMessagesSingleton.getMessage("Title2"));
        data.horizontalSpan = 2;
        label.setLayoutData(data);
        startDateSearchText = new Text(group, SWT.SINGLE);
        startDateSearchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        endDateSearchText = new Text(group, SWT.SINGLE);
        endDateSearchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        titleSearchText = new Text(group, SWT.SINGLE);
        titleSearchText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Button button = new Button(group, SWT.NONE);
        button.setText(AppMessagesSingleton.getMessage("Search"));
        button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        button.addSelectionListener(new SearchArticleForWordTestListener(this));
    }

    private void createShowList(Composite parent) {
        Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
        group.setText(AppMessagesSingleton.getMessage("SearchResults"));
        GridLayout grid = new GridLayout();
        grid.numColumns = 1;
        group.setLayout(grid);
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
        data.horizontalSpan = 2;
        group.setLayoutData(data);
        articleTable = new Table(group, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        articleTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
        articleTable.setHeaderVisible(true);
        final TableColumn tc1 = new TableColumn(articleTable, SWT.LEFT);
        final TableColumn tc2 = new TableColumn(articleTable, SWT.LEFT);
        final TableColumn tc3 = new TableColumn(articleTable, SWT.LEFT);
        tc1.setText(AppMessagesSingleton.getMessage("Date"));
        tc2.setText(AppMessagesSingleton.getMessage("Title"));
        tc3.setText(AppMessagesSingleton.getMessage("NumberOfWords"));
        tc1.setWidth(100);
        tc2.setWidth(200);
        tc3.setWidth(120);
        tableViewer = new TableViewer(articleTable);
        tableViewer.setContentProvider(new ArticlesTableContentProvider());
        tableViewer.setLabelProvider(new ArticlesLabeProvider());
        tableViewer.setInput(command.getSelectedArticles());
    }

    public TableViewer getTableViewer() {
        return tableViewer;
    }

    public Table getArticleTable() {
        return articleTable;
    }

    public Text getStartDateSearchText() {
        return startDateSearchText;
    }

    public Text getEndDateSearchText() {
        return endDateSearchText;
    }

    public Text getTitleSearchText() {
        return titleSearchText;
    }

    public int open() {
        Shell shell = new Shell(getParentShell(), getShellStyle());
        createContents(shell);
        shell.pack();
        shell.open();
        Display display = getParentShell().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return 0;
    }

    public void clear() {
    }

    public GenerateWordTestCommand getCommand() {
        return command;
    }
}
