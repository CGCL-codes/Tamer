package com.inetmon.jn.addressBook.ui;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author   Nicolas Parcollet  Action used to change between cumulative/real time
 */
public class ChangeCumulativeItem extends ControlContribution {

    /**
	 * The view that is associated with this listener
	 */
    private AddressBookView view;

    /**
	 * Construct a distribution view listener that is associated with view
	 * 
	 * @param view
	 *            The associated view
	 *  
	 */
    public ChangeCumulativeItem(AddressBookView view) {
        super("Cumulative");
        this.view = view;
    }

    protected Control createControl(Composite parent) {
        Button but = new Button(parent, SWT.CHECK);
        but.setToolTipText("Turn On/Off Address Book Alerting");
        but.setText("Alerting Mode");
        but.setSelection(view.alertFlag);
        but.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                boolean checked = ((Button) e.widget).getSelection();
                view.alertFlag = checked;
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        Point pt = but.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        but.setSize(pt);
        return but;
    }
}
