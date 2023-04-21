package org.eclipse.rap.demo.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class ZOrderTab extends ExampleTab {

    private Label label;

    public ZOrderTab(final CTabFolder topFolder) {
        super(topFolder, "Z-Order");
    }

    protected void createStyleControls(final Composite parent) {
    }

    protected void createExampleControls(final Composite top) {
        top.setLayout(new FormLayout());
        final Composite comp = new Composite(top, SWT.NONE);
        comp.setLayout(new FormLayout());
        FormData fdComp = new FormData();
        fdComp.top = new FormAttachment(0, 0);
        fdComp.left = new FormAttachment(0, 0);
        fdComp.right = new FormAttachment(100, 0);
        fdComp.bottom = new FormAttachment(70, 0);
        comp.setLayoutData(fdComp);
        final Label labelA = createLabel(comp, 15, 45);
        labelA.setText("A");
        labelA.setBackground(BG_COLOR_BLUE);
        final Label labelB = createLabel(comp, 35, 65);
        labelB.setText("B");
        labelB.setBackground(BG_COLOR_GREEN);
        final Label labelC = createLabel(comp, 55, 85);
        labelC.setText("C");
        labelC.setBackground(BG_COLOR_BROWN);
        label = new Label(comp, SWT.CENTER);
        FormData fdData = fdComp;
        fdData.top = new FormAttachment(labelC, labelC.getSize().y + 10);
        fdData.left = new FormAttachment(0, 0);
        fdData.right = new FormAttachment(100, 0);
        label.setLayoutData(fdData);
        label.setText("trallala");
        Button aboveA = createButton(top, 80, 10, 36);
        aboveA.setText("B above A");
        aboveA.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                labelB.moveAbove(labelA);
                printChildren(comp);
            }
        });
        Button belowA = createButton(top, aboveA, 10, 36);
        belowA.setText("B below A");
        belowA.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                labelB.moveBelow(labelA);
                printChildren(comp);
            }
        });
        Button aboveAll = createButton(top, 80, 37, 63);
        aboveAll.setText("B above all");
        aboveAll.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                labelB.moveAbove(null);
                printChildren(comp);
            }
        });
        Button belowAll = createButton(top, aboveAll, 37, 63);
        belowAll.setText("B below all");
        belowAll.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                labelB.moveBelow(null);
                printChildren(comp);
            }
        });
        Button aboveC = createButton(top, 80, 64, 90);
        aboveC.setText("B above C");
        aboveC.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                labelB.moveAbove(labelC);
                printChildren(comp);
            }
        });
        Button belowC = createButton(top, aboveC, 64, 90);
        belowC.setText("B below C");
        belowC.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                labelB.moveBelow(labelC);
                printChildren(comp);
            }
        });
        top.setTabList(new Control[] { aboveA, aboveAll, aboveC, belowA, belowAll, belowC });
    }

    private Button createButton(final Composite composite, final int top, final int left, final int right) {
        Button result = new Button(composite, SWT.PUSH);
        FormData fdButton = new FormData();
        fdButton.top = new FormAttachment(top, 0);
        fdButton.left = new FormAttachment(left, 0);
        fdButton.right = new FormAttachment(right, 0);
        result.setLayoutData(fdButton);
        return result;
    }

    private Button createButton(final Composite composite, final Control top, final int left, final int right) {
        Button result = new Button(composite, SWT.PUSH);
        FormData fdButton = new FormData();
        fdButton.top = new FormAttachment(top, 1);
        fdButton.left = new FormAttachment(left, 0);
        fdButton.right = new FormAttachment(right, 0);
        result.setLayoutData(fdButton);
        return result;
    }

    private Label createLabel(final Composite top, final int topAndLeft, final int rightAndBottom) {
        final Label result = new Label(top, SWT.BORDER | SWT.CENTER);
        FormData fdLabel = new FormData();
        fdLabel.top = new FormAttachment(topAndLeft, 0);
        fdLabel.left = new FormAttachment(topAndLeft, 0);
        fdLabel.right = new FormAttachment(rightAndBottom, 0);
        fdLabel.bottom = new FormAttachment(rightAndBottom, 0);
        result.setLayoutData(fdLabel);
        return result;
    }

    private void printChildren(final Composite comp) {
        Control[] children = comp.getChildren();
        StringBuffer sb = new StringBuffer("Z-Order: ");
        for (int i = 0; i < children.length; i++) {
            sb.append(((Label) children[i]).getText() + " ");
        }
        label.setText(sb.toString());
    }
}
