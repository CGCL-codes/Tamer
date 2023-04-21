package com.pcmsolutions.device.EMU.E4.gui.multimode;

import com.pcmsolutions.device.EMU.E4.DeviceContext;
import com.pcmsolutions.device.EMU.E4.gui.device.DefaultDeviceEnclosurePanel;
import com.pcmsolutions.gui.UserMessaging;
import com.pcmsolutions.system.ZDisposable;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class MultiModeEnclosurePanel extends DefaultDeviceEnclosurePanel implements ZDisposable {

    protected DeviceContext device;

    private MultiModeEditorPanel mmep;

    public void init(final DeviceContext device) throws Exception {
        this.device = device;
        mmep = new MultiModeEditorPanel().init(device, false, new AbstractAction("All sounds off (via sample defrag)") {

            public void actionPerformed(ActionEvent e) {
                try {
                    device.sampleMemoryDefrag(true).post();
                } catch (Exception e1) {
                    UserMessaging.flashWarning(MultiModeEnclosurePanel.this, e1.getMessage());
                }
            }
        });
        super.init(device, mmep);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public void zDispose() {
        super.zDispose();
        device = null;
        mmep = null;
    }
}
