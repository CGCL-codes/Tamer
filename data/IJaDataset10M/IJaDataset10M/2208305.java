package de.innot.avreclipse.ui.views.avrdevice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import de.innot.avreclipse.core.util.AVRMCUidConverter;
import de.innot.avreclipse.devicedescription.IDeviceDescriptionProvider;

public class DeviceListContentProvider implements IStructuredContentProvider {

    private IDeviceDescriptionProvider fDMprovider = null;

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        fDMprovider = (IDeviceDescriptionProvider) newInput;
    }

    public Object[] getElements(Object inputElement) {
        Set<String> devicesset = null;
        try {
            devicesset = fDMprovider.getMCUList();
        } catch (IOException e) {
        }
        if (devicesset == null) {
            String[] empty = { "" };
            return empty;
        }
        List<String> devices = new ArrayList<String>(devicesset);
        Collections.sort(devices);
        List<String> nameslist = new ArrayList<String>();
        for (String deviceid : devices) {
            String devicename = AVRMCUidConverter.id2name(deviceid);
            if (devicename != null) {
                nameslist.add(devicename);
            }
        }
        return nameslist.toArray(new String[nameslist.size()]);
    }

    public void dispose() {
    }
}
