package org.rubypeople.rdt.refactoring.ui.pages.movemethod;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.rubypeople.rdt.refactoring.core.movemethod.MoveMethodConfig;
import org.rubypeople.rdt.refactoring.nodewrapper.VisibilityNodeWrapper;

public class FirstMoveMethodPageComposite extends Composite {

    private MoveMethodConfig config;

    public FirstMoveMethodPageComposite(Composite parent, MoveMethodConfig config) {
        super(parent, SWT.NONE);
        this.config = config;
        initialize();
    }

    private void initialize() {
        GridLayout gridLayout = new GridLayout(2, false);
        setLayout(gridLayout);
        createSelectionGroup();
        createClassSelection();
        createLeaveDelegateMethodCheck();
    }

    private void createSelectionGroup() {
        String selectedMethodName = config.getMethodNode().getName();
        String selectedMethodVisibility = VisibilityNodeWrapper.getVisibilityName(config.getMethodVisibility());
        String selectedClassName = config.getSourceClassNode().getName();
        Group group = new Group(this, SWT.NONE);
        group.setLayout(new GridLayout());
        group.setText(Messages.FirstMoveMethodPageComposite_Selection);
        group.setLayoutData(getGridData(2, true));
        Label selectedMethodLabel = new Label(group, SWT.NONE);
        selectedMethodLabel.setText(Messages.FirstMoveMethodPageComposite_SelectedMethod + selectedMethodName);
        Label visibilityLabel = new Label(group, SWT.NONE);
        visibilityLabel.setText(Messages.FirstMoveMethodPageComposite_Visibility + selectedMethodVisibility);
        visibilityLabel.setLayoutData(getGridData(1, true));
        Label selcetecClassLabel = new Label(group, SWT.NONE);
        selcetecClassLabel.setText(Messages.FirstMoveMethodPageComposite_SelectedClass + selectedClassName);
        selcetecClassLabel.setLayoutData(getGridData(1, true));
    }

    private GridData getGridData(int span, boolean fill) {
        GridData gridData = new GridData();
        if (span > 1) {
            gridData.horizontalSpan = span;
        }
        if (fill) {
            gridData.horizontalAlignment = GridData.FILL;
            gridData.grabExcessHorizontalSpace = true;
        }
        return gridData;
    }

    private void createClassSelection() {
        Label moveToClassLabel = new Label(this, SWT.NONE);
        moveToClassLabel.setText(Messages.FirstMoveMethodPageComposite_MoveToClass);
        final Combo classSelectionCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        classSelectionCombo.setVisibleItemCount(10);
        for (String aktClassName : config.getTargetClassNames()) {
            if (config.getDestinationClassNode() == null) {
                config.setDestinationClassNode(aktClassName);
            }
            classSelectionCombo.add(aktClassName);
        }
        classSelectionCombo.select(0);
        classSelectionCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                config.setDestinationClassNode(classSelectionCombo.getText());
            }
        });
    }

    private void createLeaveDelegateMethodCheck() {
        final Button delegateCheck = new Button(this, SWT.CHECK);
        delegateCheck.setLayoutData(getGridData(2, true));
        delegateCheck.setText(Messages.FirstMoveMethodPageComposite_LeaveDelegate + config.getSourceClassNode().getName() + Messages.FirstMoveMethodPageComposite_DelegatesCalls + config.getMethodNode().getName() + "\".");
        delegateCheck.setSelection(config.leaveDelegateMethodInSource());
        if (config.canCreateDelegateMethod()) {
            delegateCheck.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                }

                public void widgetSelected(SelectionEvent e) {
                    config.setLeaveDelegateMethodInSource(delegateCheck.getSelection());
                }
            });
        } else {
            delegateCheck.setEnabled(false);
        }
    }
}
