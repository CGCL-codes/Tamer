package at.tw.findme.bluetooth;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.midlet.MIDlet;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import at.tw.findme.gui.BTMenu;

/**
 * Klasse f�r die Suche nach Bluetooth Ger�ten und Services.
 * <p>
 * Klasse beinhaltet Methoden f�r das Bluetooth-Inquiry (= Suchen):
 * <ul>
 * <li>Suchen von BT-Devices und deren serielle Dienste mittels full inquiry
 * <li>Suchen von preknown und cached devices und deren serielle Dienste
 * <li>erm�glicht Service-URL zu finden (notwendig f�r die Verbindung)
 * <li>Suchanfragen k�nnen auch wieder abgebrochen werden
 * </ul>
 * Dabei sollte die Suche in einem eigenen Thread ablaufen. Es wird eine Instanz
 * des DiscoveryAgents aufgerufen. Das DiscoveryListener Interface wird
 * implementiert um vollst�ndige Suchanfragen durchzuf�hren. Werden Ger�te oder
 * Dienste gefunen, so werden die entsprechenden Call-Back-Funktionen aufgerufen
 * (=> Discovery-Listener-Interface). Ger�te und Dienste werden stets in
 * Vector-Listen gespeichert. Bei Fehlern wird eine Info-Message f�r den Userer
 * ausgegeben.
 * 
 * @author Petrina
 * @see javax.bluetooth.DiscoveryListener
 * @see java.lang.Runnable
 */
public class BTInquiry implements DiscoveryListener, Runnable {

    /**
	 * Konstruktor mit Midlet�bergabe und Art der Suche (einfach/vollst�ndig).
	 * <p>
	 * Im Konstruktor werden Listen inizialisert, das Midlet gesetzt und den
	 * Parametern f�r den gefunden Dienst defaultwerte zugewiesen. F�r die Suche
	 * wird der DiscoveryAgent ebenfalls inizialisiert.
	 * 
	 * @param m
	 *            Midlet
	 * @param isInquiry
	 *            true wenn vollst�ndig gesucht werden soll andersfalls false
	 */
    public BTInquiry(MIDlet m, boolean isInquiry) {
        this.currentNr = 0;
        this.devicesFound = new Vector();
        this.servicesFound = new Vector();
        this.m = m;
        this.isInquiry = isInquiry;
        this.selectedDeviceName = "";
        this.selectedServiceName = "";
        this.serviceUrl = "";
        try {
            agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
        } catch (BluetoothStateException e) {
            System.out.println("BluetoothStateException in searchDevices: " + e.getMessage());
        }
    }

    /**
	 * Konstruktur mit MIDlet-�bergabe.
	 * <p>
	 * �bergibt das Midlet an den Konstruktor
	 * {@link #BTInquiry(MIDlet, boolean)} und schaft die Voraussetzungen f�r
	 * eine vollst�ndige Suche.
	 * 
	 * @param m
	 *            Midlet
	 */
    public BTInquiry(MIDlet m) {
        this(m, true);
    }

    /**
	 * Konstruktor mit �bergabe der Art der Suche.
	 * <p>
	 * Ruft den Konstruktor {@link #BTInquiry(MIDlet, boolean)} auf und
	 * entscheidet �ber die Art der Suche.
	 * 
	 * @param isInquiry
	 *            true wenn vollst�ndig gesucht wird andersfalls false (=
	 *            einfache Suche)
	 */
    public BTInquiry(boolean isInquiry) {
        this(null, isInquiry);
    }

    /**
	 * Defaultkonstruktor.
	 * <p>
	 * Ruft den Konstruktor {@link #BTInquiry(MIDlet, boolean)} mit
	 * Defaultwerten auf.
	 */
    public BTInquiry() {
        this(null, true);
    }

    /**
	 * Gefunden Ger�te in einem Vektor.
	 * <p>
	 * Ein Vector ist eine Listen-Datenstruktur mit Indexzugriff und variabler
	 * Gr��e um beliebige Objekte zu speichern. In diesem Fall sind
	 * RemoteDevice-Objekte enthalten.
	 * 
	 * @see java.util.Vector
	 * @see javax.bluetooth.RemoteDevice
	 */
    private Vector devicesFound;

    /**
	 * Aktueller Ger�te-Index.
	 * <p>
	 * Speichert den aktuelles Index im deviceFound-Vektor zur Service-Suche.
	 * Somit ist bekannt von welchen Ger�t in der Liste gerade Services gesucht
	 * werden.
	 */
    private int currentNr;

    /**
	 * Aktuelle Nummer der ServiceSuche.
	 * <p>
	 * Es handelt sich um eine Art Such-ID. Damit ist es m�glich eine bestimmte
	 * Service-Suche mithilfe der entsprechenden Discovery-Agent-Insanz
	 * abzustoppen (das wird dann verwendet, wenn der User auf abbrechen
	 * klickt).
	 */
    private int transID;

    /**
	 * Erm�glicht das Suchen nach Ger�ten + Services
	 */
    private DiscoveryAgent agent;

    /**
	 * Speichert gefundene Dienste (ServiceRecord) in einer Liste
	 * 
	 * @see javax.bluetooth.ServiceRecord
	 */
    private Vector servicesFound;

    /**
	 * MIDlet f�r Ausgaben
	 */
    public MIDlet m;

    /**
	 * True wenn eine vollst�ndige Suche durchgef�hrt wird, sonst False
	 */
    public boolean isInquiry;

    /**
	 * Gefundene services und devices als string liste f�r gui.
	 * <p>
	 * Diese Liste wird mit den Inhalten aus dem servicesFound-Vektor erstellt.
	 * Format "deviceX: serviceY"
	 */
    private String[] records;

    /**
	 * Friendly Name des gew�hlten BT-Ger�ts
	 */
    private String selectedDeviceName;

    /**
	 * Name des service, den der User gew�hlt hat
	 */
    private String selectedServiceName;

    /**
	 * UUID des gew�hlten service als String
	 */
    private String serviceUUID;

    /**
	 * URL des Service, der zum Verbinden notwendig wird
	 */
    private String serviceUrl;

    /**
	 * Macht eine einfache Ger�te-Suche.
	 * <p>
	 * Es wird eine Suche nach preknown und cached Ger�te durchgef�hrt und
	 * anschlie�endende eine Suche nach zugeh�rigen Diensten eingeleitet.
	 * 
	 */
    public void searchKnownDevices() {
        RemoteDevice[] list_pre = agent.retrieveDevices(DiscoveryAgent.PREKNOWN);
        RemoteDevice[] list_cach = agent.retrieveDevices(DiscoveryAgent.CACHED);
        this.devicesFound.removeAllElements();
        this.servicesFound.removeAllElements();
        if (list_pre != null) {
            for (int i = 0; i < list_pre.length; i++) {
                this.devicesFound.addElement(list_pre[i]);
            }
        }
        if (list_cach != null) {
            for (int i = 0; i < list_cach.length; i++) {
                if (!this.containElement(list_cach[i])) {
                    this.devicesFound.addElement(list_cach[i]);
                }
            }
        }
        BTMenu.waitAndSearch(this.m, this, "einfache Ger�tesuche abgeschlossen");
        this.currentNr = 0;
        if (this.devicesFound.size() > 0) {
            this.searchServices((RemoteDevice) this.devicesFound.elementAt(0));
        } else {
            System.out.print("einfache Suche funktioniert nicht");
            this.createRecords();
            BTMenu.showSearchResult(this.m, this);
        }
    }

    /**
	 * Checkt, ob das RemoteDevice bereits in der Ger�teliste enthalten ist.
	 * <p>
	 * Um zu pr�fen, ob das Ger�t schon im {@link #devicesFound} enthalten ist,
	 * wird die BT-Adress zur eindeutigen Ger�teidentifikation benutzt.
	 * Zur�ckgegeben wird demnach true falls das Ger�t in der Liste enthatlen
	 * ist, andernfalls false.
	 * 
	 * @param device
	 *            zu pr�fendes Ger�t
	 */
    private boolean containElement(RemoteDevice device) {
        boolean isElement = false;
        for (int i = 0; i < this.devicesFound.size(); i++) {
            if (device.getBluetoothAddress().equals(((RemoteDevice) this.devicesFound.elementAt(i)).getBluetoothAddress())) {
                isElement = true;
                break;
            }
        }
        return isElement;
    }

    /**
	 * Leitet die vollst�ndige Suche nach Ger�ten und Diensten ein
	 */
    public void makeFullInquiry() {
        this.currentNr = 0;
        try {
            if (!agent.startInquiry(DiscoveryAgent.GIAC, this)) {
                System.out.print("The inquiry was not started.");
                BTMenu.BTError(this.m, "Such-Fehler - die Suche wurde nicht gestartet!");
            }
        } catch (BluetoothStateException e) {
            System.out.println("BluetoothStateException in searchDevices: " + e.getMessage());
            BTMenu.BTError(this.m, "Such-Fehler bei der Ger�tesuche!");
        }
    }

    /**
	 * Leitet die Suche nach Services f�r ein Ger�t ein
	 * 
	 * @param device
	 *            Ger�t f�r das Services gesucht werden sollen
	 */
    private void searchServices(RemoteDevice device) {
        BTMenu.waitAndSearch(this.m, this, "Dienstsuche gestartet");
        int[] attributes = { 0x100 };
        UUID[] uuids = new UUID[1];
        uuids[0] = new UUID(0x0003);
        try {
            this.transID = agent.searchServices(attributes, uuids, device, this);
        } catch (BluetoothStateException e) {
            System.out.print("Bluetooth-State-Exception: " + e.getMessage());
        }
    }

    /**
	 * Callback-Funktion f�r das Finden eines Ger�ts.
	 * <p>
	 * Event-Handler-Methode die aufgerufen wird, wenn ein Ger�t gefunden wurde.
	 * Sie speichert das Ger�t der Klasse DeviceClass im Vektor devicesFound (es
	 * wird ein Element angeh�ngt).
	 * 
	 * @param remoteDevice
	 *            gefundenes Ger�t
	 * @param deviceClass
	 *            Klasse des gefunden Ger�ts
	 * @see javax.bluetooth.DiscoveryListener#deviceDiscovered(RemoteDevice,
	 *      DeviceClass)
	 */
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        this.devicesFound.addElement(remoteDevice);
        BTMenu.waitAndSearch(this.m, this, String.valueOf(this.devicesFound.size()) + "Ger�te gefunden ");
    }

    /**
	 * Callback-Funktion f�r das Beeenden der Ger�tesuche.
	 * <p>
	 * Event-Handler-Methode, die aufgerufen wird, wenn die Suche nach Ger�ten
	 * abgeschlossen ist. Diese Methode ruft die Service-Suche bei erfolgreichen
	 * Abschluss der Ger�tesuchen auf.
	 * 
	 * @param completed
	 *            completed gibt an, wie die Suche beendet wurde (erfolgreich =
	 *            normal, abgebrochen, fehler)
	 * @see javax.bluetooth.DiscoveryListener#inquiryCompleted(int)
	 */
    public void inquiryCompleted(int completed) {
        BTMenu.waitAndSearch(this.m, this, "Ger�te-Suche beendet");
        switch(completed) {
            case DiscoveryListener.INQUIRY_COMPLETED:
                if (this.devicesFound.size() > 0) {
                    this.searchServices((RemoteDevice) this.devicesFound.elementAt(0));
                } else {
                    BTMenu.waitAndSearch(this.m, this, "Suche abgeschossen");
                    this.createRecords();
                    BTMenu.showSearchResult(this.m, this);
                }
                break;
            case DiscoveryListener.INQUIRY_ERROR:
                System.out.println("inguiry error");
                BTMenu.BTError(this.m, "inguiry error - ist die BT-Funktion eingeschaltet?");
                break;
            case DiscoveryListener.INQUIRY_TERMINATED:
                System.out.println("inguiry terminated");
                BTMenu.waitAndSearch(this.m, this, "Ger�te-Suche abgeborechen");
                break;
            default:
                System.out.println("An Error occured at the inquiry completed Methode - we are in default of the switch case!");
                BTMenu.BTError(this.m, "Undefined inguiry error!!");
                break;
        }
    }

    /**
	 * Callback-Funktion f�r das Finden von Diensten.
	 * <p>
	 * Event-Handler-Methode, die aufgerufen wird wenn Dienste f�r ein Ger�t
	 * gefunden wurde. Sie speichert gefundene Dienste (Klasse: serviceRecord)
	 * im Vektor servicesFound.
	 * 
	 * @param transID
	 *            �bertragungs-ID
	 * @param serviceRecord
	 *            Array mit den gefunden Services (serviceRecord)
	 * @see javax.bluetooth.DiscoveryListener#servicesDiscovered(int,
	 *      ServiceRecord[])
	 */
    public void servicesDiscovered(int transID, ServiceRecord[] serviceRecord) {
        for (int i = 0; i < serviceRecord.length; i++) {
            this.servicesFound.addElement(serviceRecord[i]);
            BTMenu.waitAndSearch(this.m, this, String.valueOf(i + 1) + " Dienste vom " + String.valueOf(this.currentNr) + ". Ger�t gefunden");
        }
        this.createRecords();
    }

    /**
	 * Callback-Funktion f�r das Ende einer Service-Suche.
	 * <p>
	 * Event-Handler-Methode, die aufgerufen wird, wenn die Suche nach Services
	 * f�r ein bestimmtes Ger�t abgeschlossen wurde. Sie ruft die ServiceSuche
	 * f�r das n�chste Ger�t auf bis f�r alle Ger�te die gew�nschten Services
	 * gesucht wurden - in diesem Fall wird {@link #createRecords()} aufgerufen.
	 * 
	 * @param transID
	 *            �bertragungsID
	 * @param respCode
	 *            wie wurde die Suche abgeschlossen
	 * @see javax.bluetooth.DiscoveryListener#serviceSearchCompleted(int, int)
	 */
    public void serviceSearchCompleted(int transID, int respCode) {
        this.currentNr++;
        if (this.currentNr < this.devicesFound.size()) {
            this.searchServices((RemoteDevice) this.devicesFound.elementAt(this.currentNr));
        } else {
            this.currentNr = 0;
            BTMenu.waitAndSearch(this.m, this, "Suche abgeschlossen");
            this.createRecords();
            BTMenu.showSearchResult(this.m, this);
        }
    }

    /**
	 * Startpunkt f�r den Thread.
	 * <p>
	 * Diese Methode stellt die Implementation des runable interface dar und
	 * beginnt somit die Suche nach Ger�ten und Diensten. Nach dieser Suche wird
	 * der Thread wieder beendet
	 * 
	 * @see java.lang.Runnable#run()
	 */
    public void run() {
        if (this.isInquiry) {
            this.makeFullInquiry();
        } else {
            this.searchKnownDevices();
        }
    }

    /**
	 * Laufende Suche anhalten.
	 * <p>
	 * Im Fall, dass gerade gesucht wird kann die Suche abgebrochen werden
	 * (z.B.: weil auf abbrechen geklickt wird) - sonst passiert nichts
	 * 
	 */
    public void stopSearching() {
        try {
            agent.cancelInquiry(this);
        } catch (Exception e) {
        }
        try {
            agent.cancelServiceSearch(this.transID);
        } catch (Exception e) {
        }
    }

    /**
	 * Erstellen einer Dienst-Liste mit Ger�ten und Diensten.
	 * <p>
	 * Erstellt ein String-Array aus der Liste der gefunden Ger�te und Services
	 * (--> notwendig f�r die Anzeige in der GUI). Dabei werden die Felder im
	 * folgenden Format bef�llt: "Ger�t: Dienst". Sollte der Name des Ger�t
	 * nicht identifizierbar sein, so wird stattdessen nur "device" geschrieben.
	 * 
	 * 
	 * @return {@link #records} String-Array mit allen Diensten und Ger�tenamen
	 */
    private String[] createRecords() {
        String[] rec = new String[this.servicesFound.size()];
        for (int i = 0; i < this.servicesFound.size(); i++) {
            try {
                rec[i] = ((ServiceRecord) this.servicesFound.elementAt(i)).getHostDevice().getFriendlyName(true) + ": ";
            } catch (IOException e) {
                rec[i] = "device: ";
            }
            if (rec[i] == null) {
                rec[i] = "";
            }
            DataElement de = ((ServiceRecord) this.servicesFound.elementAt(i)).getAttributeValue(0x100);
            if (de.getValue() != null && de.getDataType() == DataElement.STRING) {
                rec[i] += de.getValue();
            }
        }
        this.records = rec;
        return rec;
    }

    /**
	 * Eigenschaften des gew�hlten Services auslesen.
	 * <p>
	 * Es werden die Eigenschaften des Dienstes am entprechenden Indexwert in
	 * der Liste gelesen. Dabei wird der Service und Ger�tname sowie die UUID
	 * und URL gespeichert.
	 * 
	 * @param index
	 *            Index des gew�hlten Services in der Liste (mithilfe der
	 *            Anzeigeliste)
	 */
    public void selectService(int index) {
        if (this.servicesFound.size() > index) {
            try {
                this.selectedDeviceName = ((ServiceRecord) this.servicesFound.elementAt(index)).getHostDevice().getFriendlyName(true);
            } catch (IOException e) {
            }
            DataElement de = ((ServiceRecord) this.servicesFound.elementAt(index)).getAttributeValue(0x100);
            if (de.getValue() != null && de.getDataType() == DataElement.STRING) {
                this.selectedServiceName = de.getValue().toString();
            }
            de = (DataElement) ((ServiceRecord) this.servicesFound.elementAt(index)).getAttributeValue(0x0001);
            if (de != null && de.getDataType() == DataElement.DATSEQ) {
                Enumeration e = (Enumeration) de.getValue();
                if (e.hasMoreElements()) {
                    DataElement el = (DataElement) e.nextElement();
                    if (el.getDataType() == DataElement.UUID) {
                        this.serviceUUID = ((UUID) el.getValue()).toString();
                    }
                }
            }
            this.serviceUrl = ((ServiceRecord) this.servicesFound.elementAt(index)).getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            BTMenu.showSelectedService(this.m, this);
        } else {
            BTMenu.BTError(this.m, "es wurde ein falscher Index gew�hlt");
        }
    }

    /**
	 * Getter f�r den friendly name des Ger�ts
	 * 
	 * @return {@link #selectedDeviceName}
	 */
    public String getSelectedDeviceName() {
        return this.selectedDeviceName;
    }

    /**
	 * Getter f�r den Namen des ausgew�hlten Dienstes
	 * 
	 * @return {@link #selectedServiceName}
	 */
    public String getSelectedServiceName() {
        return this.selectedServiceName;
    }

    /**
	 * Getter f�r die speziellste UUID aus der ServiceClassIDList des gew�hlten
	 * Services
	 * 
	 * @return {@link #serviceUUID}
	 */
    public String getServiceUUID() {
        return this.serviceUUID;
    }

    /**
	 * Getter f�r die Verbindungs-URL
	 * 
	 * @return {@link #serviceUrl}
	 */
    public String getServiceUrl() {
        return this.serviceUrl;
    }

    /**
	 * Gibt die String-Liste mit Ger�ten und zugeh�rigen Service zur�ck
	 * 
	 * @return {@link #records}
	 */
    public String[] getRecords() {
        return this.records;
    }
}
