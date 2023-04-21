package net.sf.ipm.sluchacze;

import net.sf.ipm.narzedzia.NarzGUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class WyjdzListener implements Listener {

    private Shell parent;

    public WyjdzListener(Shell parent) {
        this.parent = parent;
    }

    @Override
    public void handleEvent(Event event) {
        if (event.doit = NarzGUI.getMessageBox(parent, SWT.ICON_INFORMATION | SWT.YES | SWT.NO, "Zamknąć aplikację?", "Informacja") == SWT.YES) parent.dispose();
    }
}
