package com.dfruits.ui.widgets;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class DualViewer extends Composite {

    private Composite leftSideComposite;

    private Composite rightSideComposite;

    private Composite commandsComposite;

    private Button btnSelToRight;

    private Button btnSelToLeft;

    private Button btnAllToRight;

    private Button btnAllToLeft;

    private Viewer leftSideViewer;

    private Viewer rightSideViewer;

    private Label assignableUILabel;

    private Label assignedUILabel;

    private ListenerList swapListeners = new ListenerList();

    public DualViewer(Composite parent, int style) {
        super(parent, style);
        createSides(this);
    }

    private void createSides(Composite container) {
        container.setLayout(new GridLayout(1, false));
        GridData labelsData = new GridData();
        labelsData.grabExcessHorizontalSpace = true;
        labelsData.horizontalAlignment = SWT.FILL;
        Composite labelsContainer = new Composite(container, SWT.None);
        labelsContainer.setLayoutData(labelsData);
        labelsContainer.setLayout(new GridLayout(2, true));
        assignableUILabel = new Label(labelsContainer, SWT.None);
        assignableUILabel.setLayoutData(labelsData);
        assignableUILabel.setText("Assignable Items");
        assignedUILabel = new Label(labelsContainer, SWT.RIGHT);
        assignedUILabel.setLayoutData(labelsData);
        assignedUILabel.setText("Assigned Items");
        GridData dualData = new GridData();
        dualData.grabExcessHorizontalSpace = true;
        dualData.horizontalAlignment = SWT.FILL;
        dualData.grabExcessVerticalSpace = true;
        dualData.verticalAlignment = SWT.FILL;
        Composite dualContainer = new Composite(container, SWT.None);
        dualContainer.setLayoutData(dualData);
        dualContainer.setLayout(new FormLayout());
        FormData data;
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(45, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        leftSideComposite = new Composite(dualContainer, SWT.BORDER);
        leftSideComposite.setLayoutData(data);
        leftSideComposite.setLayout(new FillLayout());
        data = new FormData();
        data.left = new FormAttachment(leftSideComposite, 0);
        data.right = new FormAttachment(55, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        commandsComposite = new Composite(dualContainer, SWT.None);
        commandsComposite.setLayoutData(data);
        initCommands(commandsComposite);
        data = new FormData();
        data.left = new FormAttachment(commandsComposite, 0);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        rightSideComposite = new Composite(dualContainer, SWT.BORDER);
        rightSideComposite.setLayoutData(data);
        rightSideComposite.setLayout(new FillLayout());
    }

    public Composite getLeftSide() {
        return leftSideComposite;
    }

    public Composite getRightSide() {
        return rightSideComposite;
    }

    private void initCommands(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        parent.setLayout(layout);
        GridData data = new GridData();
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        btnSelToRight = new Button(parent, SWT.None);
        btnSelToRight.setLayoutData(data);
        btnSelToRight.setText(">");
        btnSelToRight.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                fromLeftToRight(getLeftSideViewer().getSelection());
            }
        });
        btnSelToLeft = new Button(parent, SWT.None);
        btnSelToLeft.setLayoutData(data);
        btnSelToLeft.setText("<");
        btnSelToLeft.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                fromRightToLeft(getRightSideViewer().getSelection());
            }
        });
        btnAllToRight = new Button(parent, SWT.None);
        btnAllToRight.setLayoutData(data);
        btnAllToRight.setText(">>");
        btnAllToRight.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                fromLeftToRight(getLeftSideViewer().getInput());
            }
        });
        btnAllToLeft = new Button(parent, SWT.None);
        btnAllToLeft.setLayoutData(data);
        btnAllToLeft.setText("<<");
        btnAllToLeft.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                fromRightToLeft(getRightSideViewer().getInput());
            }
        });
    }

    private Viewer getRightSideViewer() {
        if (rightSideViewer == null) {
            rightSideViewer = findViewer(rightSideComposite);
        }
        return rightSideViewer;
    }

    private Viewer getLeftSideViewer() {
        if (leftSideViewer == null) {
            leftSideViewer = findViewer(leftSideComposite);
        }
        return leftSideViewer;
    }

    private void fromLeftToRight(Object selection) {
        List swapped = swap(getRightSideViewer(), getLeftSideViewer(), selection);
        fireEvent(true, swapped);
    }

    private void fromRightToLeft(Object selection) {
        List swapped = swap(getLeftSideViewer(), getRightSideViewer(), selection);
        fireEvent(false, swapped);
    }

    private void fireEvent(boolean leftToRight, List swapped) {
        if (swapped.isEmpty()) {
            return;
        }
        SwappingEvent e = new SwappingEvent();
        e.leftToRight = leftToRight;
        e.elements = swapped;
        e.leftViewer = leftSideViewer;
        e.rightViewer = rightSideViewer;
        e.src = this;
        for (Object obj : swapListeners.getListeners()) {
            ISwapListener l = (ISwapListener) obj;
            l.swapped(e);
        }
    }

    private Viewer findViewer(Composite parent) {
        Viewer ret = null;
        Control[] c = parent.getChildren();
        if (c != null && c.length > 0) {
            Control vC = c[0];
            Object v = vC.getData("viewer");
            if (v instanceof Viewer) {
                ret = (Viewer) v;
            }
        }
        return ret;
    }

    private List swap(Viewer addTo, Viewer removeFrom, Object selection) {
        List swappedElements = new ArrayList();
        List selectionAsList = null;
        if (selection == null) {
            return swappedElements;
        }
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) selection;
            selectionAsList = ssel.toList();
        } else if (selection instanceof List) {
            selectionAsList = (List) selection;
        }
        if (selectionAsList == null || selectionAsList.isEmpty()) {
            return swappedElements;
        }
        List dataAddTo = new ArrayList((List) addTo.getInput());
        List dataRemoveFrom = new ArrayList((List) removeFrom.getInput());
        int added = 0;
        int removed = 0;
        for (Object obj : selectionAsList) {
            if (!dataAddTo.contains(obj)) {
                dataAddTo.add(obj);
                added++;
                swappedElements.add(obj);
            }
            dataRemoveFrom.remove(obj);
            removed++;
        }
        if (added > 0) {
            addTo.setInput(dataAddTo);
        }
        if (removed > 0) {
            removeFrom.setInput(dataRemoveFrom);
        }
        return swappedElements;
    }

    public String getLeftSideLabel() {
        return assignableUILabel.getText();
    }

    public void setLeftSideLabel(String leftSideLabel) {
        assignableUILabel.setText(leftSideLabel);
    }

    public String getRightSideLabel() {
        return assignedUILabel.getText();
    }

    public void setRightSideLabel(String rightSideLabel) {
        assignedUILabel.setText(rightSideLabel);
    }

    public void addSwapListener(ISwapListener listener) {
        swapListeners.add(listener);
    }

    public void removeSwapListener(ISwapListener listener) {
        swapListeners.remove(listener);
    }
}
