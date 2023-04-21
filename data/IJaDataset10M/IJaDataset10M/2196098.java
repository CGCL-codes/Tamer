package com.peterhi.client.actions;

import com.peterhi.client.ui.DrawPad;
import com.peterhi.client.ui.Util;
import com.peterhi.client.ui.constants.Strings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;

/**
 *
 * @author YUN TAO
 */
public class EraseAllAction extends SelectionAdapter {

    @Override
    public void widgetSelected(SelectionEvent e) {
        Control control = (Control) e.widget;
        int result = Util.yesno(control.getShell(), Strings.action_erase_mb_title, Strings.action_erase_mb_text);
        if (result == SWT.YES) {
            DrawPad.get().removeAll();
            DrawPad.get().setSelection(null);
        }
    }
}
