package ui.table.model;

import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.AbstractTableModel;
import ui.Messages;
import base.network.NetworkManager;
import base.network.Workstation;

@SuppressWarnings("serial")
public class ServerWorkstationTableModel extends AbstractTableModel implements Observer {

    private String[] columnNames = { Messages.getString("common.id"), Messages.getString("common.name"), Messages.getString("common.location"), Messages.getString("common.description"), Messages.getString("common.type"), Messages.getString("common.address"), Messages.getString("common.port"), Messages.getString("common.inuse"), Messages.getString("common.role") };

    public static final int WORKSTATION_ID_COLUMN = 0;

    public static final int WORKSTATION_NAME_COLUMN = 1;

    public static final int WORKSTATION_LOCATION_COLUMN = 2;

    public static final int WORKSTATION_DESCRIPTION_COLUMN = 3;

    public static final int WORKSTATION_TYPE_COLUMN = 4;

    public static final int WORKSTATION_ADDRESS_COLUMN = 5;

    public static final int WORKSTATION_PORT_COLUMN = 6;

    public static final int WORKSTATION_IN_USE_COLUMN = 7;

    public static final int WORKSTATION_ROLE_COLUMN = 8;

    public ServerWorkstationTableModel() {
        super();
        NetworkManager.getInstance().addObserver(this);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return NetworkManager.getInstance().getNetwork().getServer().length;
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Object getValueAt(int row, int column) {
        if (row < 0 || column < 0) return null;
        Workstation[] server = NetworkManager.getInstance().getNetwork().getServer();
        Object result = null;
        switch(column) {
            case WORKSTATION_ID_COLUMN:
                result = server[row].getId();
                break;
            case WORKSTATION_NAME_COLUMN:
                result = server[row].getName();
                break;
            case WORKSTATION_LOCATION_COLUMN:
                result = server[row].getLocation();
                break;
            case WORKSTATION_DESCRIPTION_COLUMN:
                result = server[row].getDescription();
                break;
            case WORKSTATION_TYPE_COLUMN:
                result = server[row].getType();
                break;
            case WORKSTATION_ADDRESS_COLUMN:
                result = server[row].getAddress().getHostAddress();
                break;
            case WORKSTATION_PORT_COLUMN:
                result = server[row].getPort();
                break;
            case WORKSTATION_IN_USE_COLUMN:
                result = server[row].isInUse();
                break;
            case WORKSTATION_ROLE_COLUMN:
                break;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Class getColumnClass(int column) {
        Class result = null;
        switch(column) {
            case WORKSTATION_ID_COLUMN:
                result = Integer.class;
                break;
            case WORKSTATION_NAME_COLUMN:
                result = String.class;
                break;
            case WORKSTATION_LOCATION_COLUMN:
                result = String.class;
                break;
            case WORKSTATION_DESCRIPTION_COLUMN:
                result = String.class;
                break;
            case WORKSTATION_TYPE_COLUMN:
                result = String.class;
                break;
            case WORKSTATION_ADDRESS_COLUMN:
                result = InetAddress.class;
                break;
            case WORKSTATION_PORT_COLUMN:
                result = Integer.class;
                break;
            case WORKSTATION_IN_USE_COLUMN:
                result = Boolean.class;
                break;
            case WORKSTATION_ROLE_COLUMN:
                result = String.class;
                break;
        }
        return result;
    }

    public boolean isCellEditable(int row, int column) {
        if (row < 0 || column < 0) return false;
        boolean result = false;
        switch(column) {
            case WORKSTATION_ID_COLUMN:
                result = false;
                break;
            case WORKSTATION_NAME_COLUMN:
                result = true;
                break;
            case WORKSTATION_LOCATION_COLUMN:
                result = true;
                break;
            case WORKSTATION_DESCRIPTION_COLUMN:
                result = true;
                break;
            case WORKSTATION_TYPE_COLUMN:
                result = true;
                break;
            case WORKSTATION_ADDRESS_COLUMN:
                result = false;
                break;
            case WORKSTATION_PORT_COLUMN:
                result = true;
                break;
            case WORKSTATION_IN_USE_COLUMN:
                result = false;
                break;
            case WORKSTATION_ROLE_COLUMN:
                result = true;
                break;
        }
        return result;
    }

    public void setValueAt(Object value, int row, int column) {
        if (row < 0 || column < 0) return;
        Workstation[] server = NetworkManager.getInstance().getNetwork().getServer();
        switch(column) {
            case WORKSTATION_ID_COLUMN:
                break;
            case WORKSTATION_NAME_COLUMN:
                server[row].setName(value.toString());
                break;
            case WORKSTATION_LOCATION_COLUMN:
                server[row].setLocation(value.toString());
                break;
            case WORKSTATION_DESCRIPTION_COLUMN:
                server[row].setDescription(value.toString());
                break;
            case WORKSTATION_TYPE_COLUMN:
                server[row].setType(value.toString());
                break;
            case WORKSTATION_ADDRESS_COLUMN:
                break;
            case WORKSTATION_PORT_COLUMN:
                server[row].setPort(Integer.parseInt(value.toString()));
                break;
            case WORKSTATION_IN_USE_COLUMN:
                break;
            case WORKSTATION_ROLE_COLUMN:
                break;
        }
        fireTableCellUpdated(row, column);
    }

    public void update(Observable observable, Object changedObject) {
        if (changedObject instanceof Workstation) this.fireTableDataChanged();
    }
}
