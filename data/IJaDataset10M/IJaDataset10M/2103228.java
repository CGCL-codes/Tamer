package com.bostone.regx;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * TODO Add description
 * 
 * @author t_rstone
 */
public class MainFrame {

    private org.eclipse.swt.widgets.Shell sShell = null;

    private Label label = null;

    private StyledText regXArea = null;

    private Label label1 = null;

    private StyledText textArea = null;

    private RegXRunner runner = new RegXRunner();

    private Label statusLabel = null;

    private Composite composite = null;

    private Button button = null;

    private Button button1 = null;

    private Button button2 = null;

    private Button button3 = null;

    private Composite composite1 = null;

    private Composite composite2 = null;

    private Label label2 = null;

    private Button radioButton = null;

    private Button radioButton1 = null;

    private Button radioButton2 = null;

    private Button radioButton3 = null;

    private Button radioButton4 = null;

    private Button radioButton5 = null;

    private Button radioButton6 = null;

    private Label label3 = null;

    private Button radioButton7 = null;

    private Button radioButton8 = null;

    private Button radioButton9 = null;

    private Button radioButton10 = null;

    private Button radioButton11 = null;

    private Button radioButton12 = null;

    private Button radioButton13 = null;

    private Button radioButton14 = null;

    private int mode;

    private ModifyListener modifyListener = new ModifyListener() {

        public void modifyText(ModifyEvent e) {
            runner.sync(regXArea, textArea, statusLabel, 0, 0, mode);
        }
    };

    /**
     * This method initializes sShell
     */
    private void createSShell() {
        sShell = new Shell(SWT.BORDER | SWT.SHELL_TRIM);
        GridData regexGrid = new GridData();
        GridData textGrid = new GridData();
        GridData gridData2 = new GridData();
        label = new Label(sShell, SWT.NONE);
        regXArea = new StyledText(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
        createComposite1();
        createComposite2();
        label1 = new Label(sShell, SWT.NONE);
        textArea = new StyledText(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
        createComposite();
        statusLabel = new Label(sShell, SWT.NONE);
        sShell.setText("RegX");
        sShell.setLayout(new GridLayout());
        label.setText("Regular expression:");
        label1.setText("Target text:");
        regexGrid.grabExcessVerticalSpace = true;
        regexGrid.grabExcessHorizontalSpace = true;
        regexGrid.widthHint = -1;
        regexGrid.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        regexGrid.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        textGrid.grabExcessHorizontalSpace = true;
        textGrid.grabExcessVerticalSpace = true;
        textGrid.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        textGrid.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        textArea.setLayoutData(textGrid);
        textArea.setBackground(new org.eclipse.swt.graphics.Color(org.eclipse.swt.widgets.Display.getDefault(), 255, 255, 255));
        textArea.addModifyListener(modifyListener);
        regXArea.addModifyListener(modifyListener);
        regXArea.setLayoutData(regexGrid);
        regXArea.setBackground(new org.eclipse.swt.graphics.Color(org.eclipse.swt.widgets.Display.getDefault(), 255, 255, 255));
        statusLabel.setText("");
        statusLabel.setLayoutData(gridData2);
        statusLabel.setForeground(new org.eclipse.swt.graphics.Color(org.eclipse.swt.widgets.Display.getDefault(), 255, 0, 0));
        statusLabel.setFont(new org.eclipse.swt.graphics.Font(org.eclipse.swt.widgets.Display.getDefault(), "Tahoma", 8, org.eclipse.swt.SWT.BOLD));
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData2.heightHint = 20;
        sShell.setSize(new org.eclipse.swt.graphics.Point(737, 422));
    }

    public void display() {
        Display display = Display.getDefault();
        createSShell();
        sShell.open();
        while (!sShell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    /**
     * This method initializes composite
     *  
     */
    private void createComposite() {
        GridData gridData3 = new GridData();
        composite = new Composite(sShell, SWT.NONE);
        button = new Button(composite, SWT.NONE);
        button1 = new Button(composite, SWT.NONE);
        button2 = new Button(composite, SWT.NONE);
        button3 = new Button(composite, SWT.NONE);
        gridData3.grabExcessHorizontalSpace = true;
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        composite.setLayoutData(gridData3);
        composite.setLayout(new FillLayout());
        button.setText("<<");
        button.setToolTipText("First Match");
        button1.setText("<");
        button1.setToolTipText("Previous Match");
        button2.setText(">");
        button2.setToolTipText("Next Match");
        button3.setText(">>");
        button3.setToolTipText("Last Match");
    }

    /**
     * This method initializes composite1
     *  
     */
    private void createComposite1() {
        GridData gridData4 = new GridData();
        composite1 = new Composite(sShell, SWT.NONE);
        label2 = new Label(composite1, SWT.NONE);
        radioButton = new Button(composite1, SWT.RADIO);
        radioButton1 = new Button(composite1, SWT.RADIO);
        radioButton2 = new Button(composite1, SWT.RADIO);
        radioButton3 = new Button(composite1, SWT.RADIO);
        radioButton4 = new Button(composite1, SWT.RADIO);
        radioButton5 = new Button(composite1, SWT.RADIO);
        radioButton6 = new Button(composite1, SWT.RADIO);
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData4.heightHint = 20;
        composite1.setLayoutData(gridData4);
        composite1.setLayout(new FillLayout());
        label2.setText("Groups:");
        radioButton.setText("0");
        radioButton1.setText("1");
        radioButton2.setText("2");
        radioButton3.setText("3");
        radioButton4.setText("4");
        radioButton5.setText("5");
        radioButton6.setText("6");
    }

    /**
     * This method initializes composite2
     *  
     */
    private void createComposite2() {
        RowData rowData1 = new org.eclipse.swt.layout.RowData();
        GridData gridData5 = new GridData();
        composite2 = new Composite(sShell, SWT.NONE);
        label3 = new Label(composite2, SWT.NONE);
        radioButton14 = new Button(composite2, SWT.RADIO);
        radioButton7 = new Button(composite2, SWT.CHECK);
        radioButton8 = new Button(composite2, SWT.CHECK);
        radioButton9 = new Button(composite2, SWT.CHECK);
        radioButton10 = new Button(composite2, SWT.CHECK);
        radioButton11 = new Button(composite2, SWT.CHECK);
        radioButton12 = new Button(composite2, SWT.CHECK);
        radioButton13 = new Button(composite2, SWT.CHECK);
        gridData5.grabExcessHorizontalSpace = true;
        gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData5.heightHint = 20;
        composite2.setLayoutData(gridData5);
        composite2.setLayout(new RowLayout());
        label3.setText("Flags:");
        label3.setLayoutData(rowData1);
        radioButton7.setText("CANON_EQ");
        radioButton8.setText("CASE_INSENSITIVE");
        radioButton9.setText("COMMENTS");
        radioButton10.setText("DOTALL");
        radioButton11.setText("MULTILINE");
        radioButton12.setText("UNICODE_CASE");
        radioButton13.setText("UNIX_LINES");
        rowData1.width = 80;
        radioButton14.setText("NONE");
        radioButton14.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                uncheckFlags();
            }
        });
    }

    private void uncheckFlags() {
        radioButton7.setSelection(false);
        radioButton8.setSelection(false);
        radioButton9.setSelection(false);
        radioButton10.setSelection(false);
        radioButton11.setSelection(false);
        radioButton12.setSelection(false);
        radioButton13.setSelection(false);
        runner.sync(regXArea, textArea, statusLabel, 0, 0, mode);
    }
}
