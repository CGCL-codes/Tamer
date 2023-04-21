package org.dicom4j.toolkit.management.tree;

import org.dicom4j.toolkit.management.DeviceManagementItem;
import org.dolmen.swing.tree.TreeAdapter;

public class DeviceTreeAdapter extends TreeAdapter {

    private DeviceManagementItem device;

    public DeviceTreeAdapter(DeviceManagementItem device) {
        super(device);
        this.device = device;
    }

    public DeviceManagementItem getDevice() {
        return device;
    }
}
