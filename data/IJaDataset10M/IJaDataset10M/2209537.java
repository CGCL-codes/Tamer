package modules.zones;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import ui.PluggedGUI;
import utils.PManager;

/**
 * GUI class for the ZonesModule.
 * 
 * @author Creative
 */
public class ZonesModuleGUI extends PluggedGUI<ZonesModule> {

    public ZonesModuleGUI(final ZonesModule owner) {
        super(owner);
    }

    /**
     * Handles the "Zone editor" menu item click action.
     */
    public void mnutmEditOpenZoneEditorAction() {
        PManager.getDefault().drw_zns.show(true);
    }

    @Override
    public void initialize(final Shell shell, final ExpandBar expandBar, final Menu menuBar, final CoolBar coolBar, final Group grpGraphs) {
        Menu mnu_edit = null;
        for (final MenuItem miOut : menuBar.getItems()) if (miOut.getText().equals("Edit")) mnu_edit = miOut.getMenu();
        final MenuItem mnutm_edit_openzoneeditor = new MenuItem(mnu_edit, SWT.PUSH);
        mnutm_edit_openzoneeditor.setText("Zone Editor ..");
        mnutm_edit_openzoneeditor.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            @Override
            public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
            }

            @Override
            public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
                mnutmEditOpenZoneEditorAction();
            }
        });
    }

    @Override
    public void inIdleState() {
    }

    @Override
    public void inStreamingState() {
    }

    @Override
    public void inTrackingState() {
    }
}
