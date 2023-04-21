package org.dicom4j.apps.commons.management.tree;

import javax.swing.tree.DefaultTreeModel;
import org.dicom4j.toolkit.management.Devices;
import org.dicom4j.toolkit.management.tree.DevicesTreeAdapter;

public class DevicesTreeTableModel extends DefaultTreeModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public DevicesTreeTableModel() {
        super(null);
    }

    public void setDevices(Devices devices) {
        root = new DevicesTreeAdapter(devices);
        assert (root != null);
        setRoot(root);
        assert (getRoot() != null);
        reload();
    }
}
