package it.unibo.deis.interaction.bth;

import it.unibo.deis.interaction.cm.LTSpace;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.BluetoothStateException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.lib.InvalidObjectIdException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * 
 * @author Simone
 *  
 */
public class DeviceManagerCtx implements DiscoveryListener {

    private boolean debug = false;

    private LTSpace tspace = null;

    private BthTransportReceiver currentReader = null;

    private UUID[] uuid = { new UUID(232323666) };

    private boolean discoveryCompleted = false;

    /**
	 * Vettore di dispositivi bluetooth nelle vicinanze del client
	 */
    private Vector devices;

    /**
	 * Vettore dei servizi trovati
	 */
    private Vector records;

    public DeviceManagerCtx() throws Exception {
        tspace = LTSpace.getSpace();
    }

    public void terminate() {
        System.out.println(" *** DeviceManager terminates " + currentReader);
        if (currentReader != null) currentReader.terminate();
    }

    public IBluetoothConnection searchConnection(String p_servicename) throws Exception {
        IBluetoothConnection conn = getConnection("Conn", p_servicename);
        if (conn == null) {
            System.out.println(" *** DeviceManager searching for a connection ... please wait ");
            Vector record = SearchServicesAndDevices(uuid);
            println(" *** DeviceManager searching the service n= " + record.size());
            for (int i = record.size() - 1; i >= 0; i--) {
                String service = ((ServiceRecord) record.get(i)).getAttributeValue(0x0100).toString();
                System.out.println(" *** DeviceManager searchConnection  has found  service=" + service + ":" + p_servicename);
                if (service.equals(p_servicename)) {
                    try {
                        String str = ((ServiceRecord) record.get(i)).getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                        println(" *** DeviceManager invoke ServiceRecord str= " + str);
                        StreamConnection c = (StreamConnection) Connector.open(str, Connector.READ_WRITE);
                        conn = new BluetoothConnection(service, c);
                        registerConnection(conn, conn.toString());
                        return conn;
                    } catch (Exception e) {
                        println(" *** DeviceManager searchConnection ERROR " + e);
                        return null;
                    }
                }
            }
            return null;
        } else {
            println(" *** DeviceManager " + p_servicename + " HAS ALREADY A CONNECTION ");
            return conn;
        }
    }

    public synchronized IBluetoothConnection getConnection(String ConnectionHandle, String ServiceName) throws Exception {
        IBluetoothConnection conn = null;
        SolveInfo sol;
        println(" *** DeviceManager  getConnection " + ServiceName);
        String queryS = "connectionBluetooth(" + ConnectionHandle + " , '" + ServiceName + "' , Obj)";
        println(" *** DeviceManager getConnection queryS=" + queryS);
        sol = tspace.solve(queryS);
        while (sol != null && sol.isSuccess()) {
            try {
                println(" *** DeviceManager getConnection queryS answer=" + sol.getVarValue("Obj"));
                conn = (IBluetoothConnection) tspace.getRegisteredObject("" + sol.getVarValue("Obj"));
                conn.getStreamConnection().openDataOutputStream();
                println(" ***  DeviceManager getConnection to " + ServiceName + " " + conn);
                return conn;
            } catch (InvalidObjectIdException e) {
                println(" ***  DeviceManager getConnection Errore CAST");
            } catch (IOException e1) {
                unregisterConnection(conn, conn.toString());
                conn.close();
                println(" *** DeviceManager  getConnection Connessione non pi� attiva!!!!");
            } catch (NoSolutionException e) {
                println(" *** DeviceManager No Solution");
            }
            sol = tspace.nextSolInfo();
        }
        println(" *** DeviceManager  getConnection to " + ServiceName + " NOT FOUND YET");
        return null;
    }

    /**
	 * Ricerca dispositivi e servizi nelle vicinanze
	 */
    public Vector SearchServicesAndDevices(UUID[] uuid) {
        this.uuid = uuid;
        devices = new Vector();
        Vector ret = new Vector();
        synchronized (this) {
            try {
                LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, this);
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (BluetoothStateException e) {
                e.printStackTrace();
            }
        }
        println(" *** DeviceManager  SearchServicesAndDevices n=" + devices.size());
        for (Enumeration enum_d = devices.elements(); enum_d.hasMoreElements(); ) {
            RemoteDevice d = (RemoteDevice) enum_d.nextElement();
            try {
                System.out.print(" *** DeviceManager Device name=" + d.getFriendlyName(false));
                System.out.println(" BluetoothAddress=" + d.getBluetoothAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
            records = new Vector();
            println(" *** DeviceManager  records num= " + records.size());
            synchronized (this) {
                try {
                    LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(new int[] { 0x0100 }, uuid, d, this);
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (BluetoothStateException e) {
                    e.printStackTrace();
                }
            }
            ret.removeAllElements();
            for (Enumeration enum_r = records.elements(); enum_r.hasMoreElements(); ) {
                ServiceRecord r = (ServiceRecord) enum_r.nextElement();
                ret.add(r);
            }
        }
        return ret;
    }

    /**
	 * Viene richiamato una volta che si trova un dispositivo
	 */
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        println(" *** DeviceManager DiscoveryListener discovers = " + cod);
        devices.addElement(btDevice);
    }

    /**
	 * Ricerca dispositivi conclusa
	 */
    public synchronized void inquiryCompleted(int discType) {
        println(" *** DeviceManager DiscoveryListener inquiry completed: discType = " + discType);
        notifyAll();
    }

    /**
	 * Viene richiamato quando si trovano dei servizi
	 */
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        for (int i = 0; i < servRecord.length; i++) records.addElement(servRecord[i]);
    }

    /**
	 * Ricerca servizi conclusa
	 */
    public synchronized void serviceSearchCompleted(int transID, int respCode) {
        println(" *** DeviceManager DiscoveryListener service search completed: respCode = " + respCode);
        discoveryCompleted = true;
        notifyAll();
    }

    private int indexConnection = 1;

    public synchronized void registerConnection(IBluetoothConnection conn, String id) throws Exception {
        if (conn.getIndex() == -1) {
            conn.setIndex(indexConnection);
            indexConnection++;
        }
        println(" *** DeviceManager registerConnection " + conn.getIndex());
        tspace.register("connObj" + conn.getIndex(), conn);
        String queryS = "asserta(connectionBluetooth('" + id + "','" + conn.getNameService() + "','connObj" + conn.getIndex() + "'))";
        println(" *** DeviceManager  " + queryS);
        tspace.query(queryS);
    }

    public synchronized void unregisterConnection(IBluetoothConnection conn, String id) throws Exception {
        int index = conn.getIndex();
        if (index == -1) return;
        println(" *** DeviceManagerCtx unregisterConnection " + conn.getIndex());
        tspace.unregister("connObj" + index);
        String queryS = "retract(connectionBluetooth('" + id + "','" + conn.getNameService() + "',connObj))";
        println(" *** DeviceManagerCtx queryS " + queryS);
        tspace.query(queryS);
    }

    protected void print(String msg) {
        if (debug) System.out.print(msg);
    }

    protected void println(String msg) {
        if (debug) System.out.println(msg);
    }
}
