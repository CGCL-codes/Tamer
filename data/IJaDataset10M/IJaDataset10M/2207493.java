package net.sourceforge.rconx.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 * @author RconX Project
 * 
 */
public class ServerListModel extends DefaultTableModel {

    /**
	 * 
	 */
    private List<ServerModel> data = new ArrayList<ServerModel>();

    /**
	 * 
	 */
    public ServerListModel() {
        super();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        final ServerModel server = this.data.get(row);
        switch(column) {
            case 0:
                return server.getServerID();
            case 1:
                return server.getServerInfo().getName();
            case 2:
                return server.getServerInfo().getGame();
            case 3:
                return server.getServerInfo().getMap();
            case 4:
                return server.getServerInfo().getPlayerCount() + " / " + server.getServerInfo().getMaxPlayers();
            case 5:
                return "!!! PING !!!";
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        final ServerModel server = this.data.get(row);
        switch(column) {
            case 0:
                server.setServerID((Integer) value);
            case 1:
                server.getServerInfo().setName((String) value);
            case 2:
                server.getServerInfo().setGame((String) value);
            case 3:
                server.getServerInfo().setMap((String) value);
            case 4:
                server.getServerInfo().setPlayerCount((String) value);
            case 5:
                System.out.println("!! Pas de ping dans ServerInfo !!");
        }
        this.fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return null != this.data ? this.data.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return "ID";
            case 1:
                return "Name";
            case 2:
                return "Game";
            case 3:
                return "Map";
            case 4:
                return "Players";
            case 5:
                return "Ping";
            default:
                return null;
        }
    }

    /**
	 * @param server
	 */
    public void addRow(ServerModel server) {
        this.data.add(server);
        this.fireTableStructureChanged();
    }

    @Override
    public void removeRow(int row) {
        this.removeRow(row);
        this.fireTableStructureChanged();
    }

    /**
	 * 
	 */
    public void removeAll() {
        this.data.clear();
        this.fireTableStructureChanged();
    }
}
