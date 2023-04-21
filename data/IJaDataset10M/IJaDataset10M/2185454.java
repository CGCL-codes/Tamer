package info.walnutstreet.vs.ps03.client.view.listener;

import info.walnutstreet.vs.ps03.client.view.dialogs.EditGoodInMyListDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Christoph Gostner
 * @author Romedius Weiss
 * @version 0.1
 *
 */
public class EditGoodSelectionListener implements SelectionListener {

    private Shell shell;

    /**
	 * Constructor
	 * 
	 * @param shell
	 */
    public EditGoodSelectionListener(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent event) {
        this.widgetSelected(event);
    }

    @Override
    public void widgetSelected(SelectionEvent event) {
        EditGoodInMyListDialog dialog = new EditGoodInMyListDialog(this.shell);
        dialog.open();
    }
}
