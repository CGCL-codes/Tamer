package com.pcmsolutions.device.EMU.E4.gui.master;

import com.pcmsolutions.device.EMU.E4.DeviceContext;
import com.pcmsolutions.device.EMU.E4.gui.device.DefaultDeviceEnclosurePanel;
import com.pcmsolutions.device.EMU.E4.parameter.IllegalParameterIdException;
import com.pcmsolutions.gui.desktop.SessionableComponent;
import com.pcmsolutions.system.ZDisposable;
import com.pcmsolutions.system.ZUtilities;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 27-May-2003
 * Time: 09:10:02
 * To change this template use Options | File Templates.
 */
public class MasterEnclosurePanel extends DefaultDeviceEnclosurePanel implements ZDisposable {

    private MasterPanel mp;

    public void init(DeviceContext device) throws Exception, IllegalParameterIdException {
        mp = new MasterPanel().init(device);
        super.init(device, mp);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
}
