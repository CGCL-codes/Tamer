package com.android.ide.eclipse.ddms.views;

import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmuilib.SelectionDependentPanel;
import com.android.ide.eclipse.ddms.DdmsPlugin;
import com.android.ide.eclipse.ddms.DdmsPlugin.ISelectionListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.ui.part.ViewPart;

/**
 * A Workbench {@link ViewPart} that requires {@link Device}/{@link Client} selection notifications
 * from {@link DdmsPlugin} through the {@link ISelectionListener} interface.
 */
public abstract class SelectionDependentViewPart extends ViewPart implements ISelectionListener {

    private SelectionDependentPanel mPanel;

    protected final void setSelectionDependentPanel(SelectionDependentPanel panel) {
        mPanel = panel;
        DdmsPlugin.getDefault().addSelectionListener(this);
    }

    @Override
    public void dispose() {
        DdmsPlugin.getDefault().removeSelectionListener(this);
        super.dispose();
    }

    /**
     * Sent when a new {@link Client} is selected.
     * @param selectedClient The selected client.
     *
     * @see ISelectionListener
     */
    public final void selectionChanged(Client selectedClient) {
        mPanel.clientSelected(selectedClient);
    }

    /**
     * Sent when a new {@link Device} is selected.
     * @param selectedDevice the selected device.
     *
     * @see ISelectionListener
     */
    public final void selectionChanged(IDevice selectedDevice) {
        mPanel.deviceSelected(selectedDevice);
    }
}
