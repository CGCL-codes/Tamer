package emulator.shell.GUI.IECwindow;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import emulator.hardware.io.peripherals.iec.DeviceInfo;
import emulator.hardware.io.peripherals.iec.IecSimDeviceRegistry;

public class DeviceTableModel extends AbstractTableModel {

    String[] column_names = { "En", "#", "Type", "File" };

    int[] column_widths = { 20, 20, 70, 100 };

    Vector<DeviceInfo> devices = new Vector<DeviceInfo>();

    public DeviceTableModel() {
        int[] device_numbers = IecSimDeviceRegistry.instance().enumDevices();
        for (int i = 0; i < device_numbers.length; i++) {
            DeviceInfo device = IecSimDeviceRegistry.instance().getDevice(device_numbers[i]);
            if (device != null) devices.add(device);
        }
    }

    @Override
    public int getColumnCount() {
        return column_names.length;
    }

    @Override
    public String getColumnName(int column) {
        return column_names[column];
    }

    public int getColumnWidth(int col) {
        return column_widths[col];
    }

    @Override
    public int getRowCount() {
        return devices.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        switch(column) {
            case 0:
                return getDeviceForRow(row).isEnabled();
            case 1:
                return getDeviceForRow(row).getAddress();
            case 2:
                return getDeviceForRow(row).getType();
            case 3:
                return getDeviceForRow(row).getFile();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 1;
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        switch(column) {
            case 0:
                getDeviceForRow(row).setEnabled((Boolean) value);
                break;
            case 2:
                getDeviceForRow(row).setType((String) value);
                break;
            case 3:
                getDeviceForRow(row).setFile((String) value);
                break;
        }
    }

    private static final long serialVersionUID = 4776849022812402994L;

    public DeviceInfo getDeviceForRow(int row) {
        return devices.get(row);
    }

    public void addDevice(DeviceInfo device) {
        int pos;
        for (pos = 0; pos < devices.size(); pos++) {
            if (devices.get(pos).getAddress() > device.getAddress()) break;
        }
        devices.insertElementAt(device, pos);
        fireTableRowsInserted(pos, pos);
    }

    public void removeDevice(DeviceInfo device) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getAddress() == device.getAddress()) {
                devices.remove(i);
                fireTableRowsDeleted(i, i);
                break;
            }
        }
    }
}
