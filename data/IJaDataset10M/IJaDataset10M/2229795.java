package remote.gui.util;

import java.util.Observable;
import java.util.Vector;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observer;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import remote.motecontrol.client.LedsInputStream;
import remote.motecontrol.client.LedsListener;
import remote.motecontrol.client.Mote;
import remote.motecontrol.client.MoteList;
import remote.motecontrol.client.MoteListListener;
import remote.motecontrol.client.SimpleMote;
import remote.motecontrol.client.SimpleMoteStatus;
import remote.motecontrol.client.SimpleMoteStatusListener;
import remote.protocol.motecontrol.MsgCommand;
import remote.protocol.motecontrol.MsgResult;
import remote.service.motedata.client.MoteDataWrapper;
import remote.service.motedata.client.Table;
import remote.service.motedata.client.TableHeader;
import remote.service.motedata.client.TableListener;
import remote.service.motedata.client.TableRow;

public class MoteControlTable implements MoteListListener, SimpleMoteStatusListener, Table {

    private static final long serialVersionUID = -8759189345776880210L;

    protected Header header = new Header(this);

    protected Hashtable moteControlMap;

    protected Vector moteControlVector;

    protected HashSet listeners;

    protected class LedsListenerWrapper implements LedsListener {

        MoteControlTable table;

        SimpleMote mote;

        public LedsListenerWrapper(MoteControlTable table, SimpleMote mote) {
            super();
            this.table = table;
            this.mote = mote;
        }

        public void ledsChanged(LedsInputStream leds) {
            this.table.ledsChange(mote);
        }
    }

    protected class Header implements TableHeader {

        MoteControlTable table;

        public Header(MoteControlTable table) {
            this.table = table;
        }

        public Table getTable() {
            return table;
        }

        public int indexOf(String columnName) throws Exception {
            if (columnName.equals("mote_id")) return MoteControlRow.COL_MOTE_ID;
            if (columnName.equals("site")) return MoteControlRow.COL_SITE;
            if (columnName.equals("netaddress")) return MoteControlRow.COL_NETADDRESS;
            if (columnName.equals("macaddress")) return MoteControlRow.COL_MACADDRESS;
            if (columnName.equals("status")) return MoteControlRow.COL_STATUS;
            if (columnName.equals("lastrequest")) return MoteControlRow.COL_LAST_REQUEST;
            if (columnName.equals("leds")) return MoteControlRow.COL_LEDS;
            if (columnName.equals("logfile")) return MoteControlRow.COL_LOGFILE;
            throw new Exception("Invalid column name");
        }

        public String getTitle(String columnName) throws Exception {
            return this.getTitle(this.indexOf(columnName));
        }

        public String getTitle(int columnIndex) {
            switch(columnIndex) {
                case MoteControlRow.COL_MOTE_ID:
                    return "mote_id";
                case MoteControlRow.COL_SITE:
                    return "Site";
                case MoteControlRow.COL_NETADDRESS:
                    return "NET";
                case MoteControlRow.COL_MACADDRESS:
                    return "MAC";
                case MoteControlRow.COL_STATUS:
                    return "Status";
                case MoteControlRow.COL_LAST_REQUEST:
                    return "Last request";
                case MoteControlRow.COL_LEDS:
                    return "Leds";
                case MoteControlRow.COL_LOGFILE:
                    return "Local log";
            }
            return null;
        }

        public String getName(int columnIndex) {
            switch(columnIndex) {
                case MoteControlRow.COL_MOTE_ID:
                    return "mote_id";
                case MoteControlRow.COL_SITE:
                    return "site";
                case MoteControlRow.COL_NETADDRESS:
                    return "netaddress";
                case MoteControlRow.COL_MACADDRESS:
                    return "macaddress";
                case MoteControlRow.COL_STATUS:
                    return "status";
                case MoteControlRow.COL_LAST_REQUEST:
                    return "lastrequest";
                case MoteControlRow.COL_LEDS:
                    return "leds";
                case MoteControlRow.COL_LOGFILE:
                    return "logfile";
            }
            System.out.println("Invalid columnIndex " + columnIndex);
            return null;
        }

        public Class getClass(int columnIndex) throws ClassNotFoundException {
            switch(columnIndex) {
                case MoteControlRow.COL_MOTE_ID:
                    return Long.class;
                case MoteControlRow.COL_SITE:
                    return String.class;
                case MoteControlRow.COL_NETADDRESS:
                    return Integer.class;
                case MoteControlRow.COL_MACADDRESS:
                    return String.class;
                case MoteControlRow.COL_STATUS:
                    return String.class;
                case MoteControlRow.COL_LAST_REQUEST:
                    return String.class;
                case MoteControlRow.COL_LEDS:
                    return LedsInputStream.class;
                case MoteControlRow.COL_LOGFILE:
                    return String.class;
            }
            return null;
        }

        public Class getClass(String columnName) throws Exception {
            return this.getClass(this.indexOf(columnName));
        }

        public boolean isVisible(String columnName) throws Exception {
            if (columnName.equals("mote_id") || columnName.equals("leds")) {
                return false;
            } else {
                return true;
            }
        }

        public boolean isVisible(int columnIndex) {
            try {
                return this.isVisible(this.getName(columnIndex));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public int countVisible() {
            return 6;
        }
    }

    public MoteControlTable(MoteList moteList) {
        moteControlMap = new Hashtable();
        Iterator i = moteList.getCollectionView().iterator();
        this.moteControlVector = new Vector();
        this.listeners = new HashSet();
        while (i.hasNext()) {
            this.addMote((SimpleMote) i.next());
        }
        moteList.addListener(this);
    }

    protected void refreshTable() {
        this.moteControlVector = new Vector(this.moteControlMap.values());
    }

    protected void addMote(SimpleMote mote) {
        try {
            Table moteInfoTable = MoteDataWrapper.getMoteTable();
            int colIndex = moteInfoTable.getHeader().indexOf("mote_id");
            TableRow moteInfoRow = moteInfoTable.lookup(colIndex, new Long(mote.getId()));
            MoteControlRow moteControl = new MoteControlRow(mote, true, moteInfoRow, this);
            this.moteControlMap.put(mote, moteControl);
            mote.getStatus().addChangeListener(this);
            this.refreshTable();
            TableListener l;
            Iterator it = listeners.iterator();
            while (it.hasNext()) {
                l = (TableListener) it.next();
                l.addedRow(this, moteControl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void removeMote(SimpleMote mote) {
        mote.getStatus().removeChangeListener(this);
        MoteControlRow row = (MoteControlRow) this.moteControlMap.remove(mote);
        this.refreshTable();
        TableListener l;
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
            l = (TableListener) it.next();
            l.deletedRow(this, row);
        }
    }

    public void addedMote(MoteList moteList, Mote mote) {
        this.addMote((SimpleMote) mote);
    }

    public void removedMote(MoteList moteList, Mote mote) {
        this.removeMote((SimpleMote) mote);
    }

    public void cleared(MoteList moteList) {
        moteControlMap.clear();
        this.refreshTable();
    }

    public void simpleMoteStatusChange(SimpleMote mote) {
        MoteControlRow row = (MoteControlRow) this.moteControlMap.get(mote);
        TableListener l;
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
            l = (TableListener) it.next();
            l.updatedCell(this, row, MoteControlRow.COL_STATUS);
        }
    }

    public MoteControl getMoteControl(int index) {
        return (MoteControl) this.moteControlVector.get(index);
    }

    public void ledsChange(SimpleMote mote) {
        int index = this.moteControlVector.indexOf(this.moteControlMap.get(mote));
    }

    protected String decodeLastCommandResult(SimpleMoteStatus s) {
        String prefix, suffix;
        switch(s.getLastCommand()) {
            case MsgCommand.CANCELPROGRAMMING:
                prefix = "Cancel programming ";
                break;
            case MsgCommand.PROGRAM:
                prefix = "Programming ";
                break;
            case MsgCommand.RESET:
                prefix = "Reset ";
                break;
            case MsgCommand.START:
                prefix = "Start ";
                break;
            case MsgCommand.STATUS:
                prefix = "Status request ";
                break;
            case MsgCommand.STOP:
                prefix = "Stop ";
                break;
            default:
                prefix = "Unknown command ";
        }
        switch(s.getLastResult()) {
            case MsgResult.FAILURE:
                suffix = "failed";
                break;
            case MsgResult.NOT_SUPPORTED:
                suffix = "is not supported";
                break;
            case MsgResult.SUCCESS:
                suffix = "succesful";
                break;
            default:
                suffix = "returned unkown result";
        }
        return prefix + suffix;
    }

    public TableHeader getHeader() {
        return this.header;
    }

    public TableRow getRow(int rowIndex) {
        return (TableRow) this.moteControlVector.get(rowIndex);
    }

    public int rows() {
        return this.moteControlVector.size();
    }

    public int columns() {
        return 8;
    }

    public TableRow lookup(int columnIndex, Object value) {
        return null;
    }

    public void addListener(TableListener l) {
        listeners.add(l);
    }

    public void removeListener(TableListener l) {
        listeners.remove(l);
    }
}
