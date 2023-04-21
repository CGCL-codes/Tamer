package com.peterhi.classroom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public abstract class AbstractDialog<T> extends Dialog {

    public AbstractDialog(Shell parent, int style) {
        super(parent, style);
    }

    public T open() {
        Shell dialogShell = new Shell(getParent(), getStyle());
        dialogShell.setData(this);
        GridLayout dialogShell_layout = new GridLayout();
        dialogShell.setLayout(dialogShell_layout);
        Composite topPanel = new Composite(dialogShell, SWT.NONE);
        GridData topPanel_data = new GridData();
        topPanel_data.horizontalAlignment = GridData.FILL;
        topPanel_data.verticalAlignment = GridData.FILL;
        topPanel_data.grabExcessHorizontalSpace = true;
        topPanel.setLayoutData(topPanel);
        Label topMiddleSeparator = new Label(dialogShell, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData topMiddleSeparator_data = new GridData();
        topMiddleSeparator_data.horizontalAlignment = GridData.FILL;
        topMiddleSeparator_data.verticalAlignment = GridData.FILL;
        topMiddleSeparator_data.grabExcessHorizontalSpace = true;
        topMiddleSeparator.setLayoutData(topMiddleSeparator_data);
        Composite middlePanel = new Composite(dialogShell, SWT.NONE);
        GridData middlePanel_data = new GridData();
        middlePanel_data.horizontalAlignment = GridData.FILL;
        middlePanel_data.verticalAlignment = GridData.FILL;
        middlePanel_data.grabExcessHorizontalSpace = true;
        middlePanel_data.grabExcessVerticalSpace = true;
        middlePanel.setLayoutData(middlePanel_data);
        GridLayout middlePanel_layout = new GridLayout();
        middlePanel.setLayout(middlePanel_layout);
        Label middleBottomSeparator = new Label(dialogShell, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData middleBottomSeparator_data = new GridData();
        middleBottomSeparator_data.horizontalAlignment = GridData.FILL;
        middleBottomSeparator_data.verticalAlignment = GridData.FILL;
        middleBottomSeparator_data.grabExcessHorizontalSpace = true;
        middleBottomSeparator.setLayoutData(middleBottomSeparator_data);
        Composite bottomPanel = new Composite(dialogShell, SWT.NONE);
        GridData bottomPanel_data = new GridData();
        bottomPanel_data.horizontalAlignment = GridData.FILL;
        bottomPanel_data.verticalAlignment = GridData.FILL;
        bottomPanel_data.grabExcessHorizontalSpace = true;
        bottomPanel.setLayoutData(bottomPanel_data);
        GridLayout bottomPanel_layout = new GridLayout();
        bottomPanel_layout.numColumns = 3;
        bottomPanel_layout.makeColumnsEqualWidth = false;
        bottomPanel.setLayout(bottomPanel_layout);
        ToolBar quickHelpToolBar = new ToolBar(bottomPanel, SWT.NONE);
        GridData quickHelpToolBar_data = new GridData();
        quickHelpToolBar_data.horizontalAlignment = GridData.FILL;
        quickHelpToolBar_data.verticalAlignment = GridData.FILL;
        quickHelpToolBar_data.grabExcessVerticalSpace = true;
        quickHelpToolBar.setLayoutData(quickHelpToolBar_data);
        ToolItem quickHelpToolItem = new ToolItem(quickHelpToolBar, SWT.PUSH);
        quickHelpToolItem.setToolTipText(UIStringManager.THIS.getButtonQuickHelp());
        quickHelpToolItem.setImage(UIImageManager.THIS.getQuickHelp());
        ProgressPanel progressPanel = new ProgressPanel(bottomPanel, SWT.NONE);
        GridData progressPanel_data = new GridData();
        progressPanel_data.horizontalAlignment = GridData.FILL;
        progressPanel_data.verticalAlignment = GridData.FILL;
        progressPanel_data.grabExcessHorizontalSpace = true;
        progressPanel_data.grabExcessVerticalSpace = true;
        progressPanel.setLayoutData(progressPanel_data);
        Composite buttonPanel = new Composite(bottomPanel, SWT.NONE);
        GridData buttonPanel_data = new GridData();
        buttonPanel_data.horizontalAlignment = GridData.FILL;
        buttonPanel_data.verticalAlignment = GridData.FILL;
        buttonPanel_data.grabExcessVerticalSpace = true;
        buttonPanel.setLayoutData(buttonPanel_data);
        GridLayout buttonPanel_layout = new GridLayout();
        buttonPanel_layout.verticalSpacing = 0;
        buttonPanel_layout.marginTop = 0;
        buttonPanel_layout.marginBottom = 0;
        buttonPanel_layout.marginHeight = 0;
        buttonPanel.setLayout(buttonPanel_layout);
        createContents(middlePanel);
        createButtons(buttonPanel);
        dialogShell.pack();
        ToolKit.center(dialogShell);
        dialogShell.open();
        while (!dialogShell.isDisposed()) {
            if (!dialogShell.getDisplay().readAndDispatch()) {
                dialogShell.getDisplay().sleep();
            }
        }
        return null;
    }

    protected abstract void createContents(Composite container);

    protected abstract void createButtons(Composite buttonPanel);

    protected abstract void validate() throws Exception;

    public static void main(String[] args) {
        Display display = Display.getDefault();
        final Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        Button cmd = new Button(shell, SWT.PUSH);
        cmd.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
            }
        });
        shell.setSize(640, 480);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}
