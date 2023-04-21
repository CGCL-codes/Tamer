package eu.ict.persist.RFID.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.HashMap;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.pm.prefmodel.api.pss3p.IAction;
import org.personalsmartspace.pm.prefmodel.api.pss3p.IActionConsumer;
import org.personalsmartspace.psm.rs.api.pss3p.IResourceSharingManager;
import org.personalsmartspace.psm.rs.api.pss3p.ISharingStrategyDescription;
import org.personalsmartspace.psm.rs.api.pss3p.StrategyDescriptionManager;
import org.personalsmartspace.spm.identity.api.platform.DigitalPersonalIdentifier;
import org.personalsmartspace.spm.identity.api.platform.MalformedDigitialPersonalIdentifierException;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;
import org.personalsmartspace.sre.slm.api.pss3p.callback.IService;
import org.personalsmartspace.onm.api.pss3p.ServiceMessage;
import org.personalsmartspace.onm.api.pss3p.XMLConverter;
import org.personalsmartspace.onm.api.pss3p.IMessageQueue;
import org.personalsmartspace.onm.api.pss3p.ONMException;
import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import eu.ict.persist.RFID.api.RFIDAPI;
import javax.swing.JOptionPane;

public class RFIDIMPL implements RFIDAPI, IService {

    private BundleContext bc;

    private final PSSLog logging = new PSSLog(this);

    private IServiceIdentifier myServiceId;

    private List<String> myServiceTypes = new ArrayList<String>();

    private IDigitalPersonalIdentifier controllerDPI = null;

    private IMessageQueue msgQ;

    private Hashtable<String, String> tagToPassword;

    private Hashtable<String, IDigitalPersonalIdentifier> tagtoDPI;

    private Hashtable<String, String> wUnitToSymloc;

    private ServerGUIFrame frame;

    public RFIDIMPL(BundleContext bc) {
        this.bc = bc;
        this.tagtoDPI = new Hashtable<String, IDigitalPersonalIdentifier>();
        this.tagToPassword = new Hashtable<String, String>();
        RFIDConfig rfidConfig = new RFIDConfig();
        this.wUnitToSymloc = rfidConfig.getUnitToSymloc();
        if (this.wUnitToSymloc == null) {
            this.wUnitToSymloc = new Hashtable<String, String>();
        }
        frame = new ServerGUIFrame(this);
    }

    private IMessageQueue getMessageQueue() {
        if (null != this.msgQ) {
            return this.msgQ;
        }
        ServiceTracker srtMessageQueue = new ServiceTracker(this.bc, IMessageQueue.class.getName(), null);
        srtMessageQueue.open();
        this.msgQ = (IMessageQueue) srtMessageQueue.getService();
        return this.msgQ;
    }

    public void sendAcknowledgeMessage(IDigitalPersonalIdentifier clientDPI, Integer rStatus) {
        String intAsXML = XMLConverter.objectToXml(rStatus);
        ServiceMessage msg = new ServiceMessage(this.myServiceId.toString(), "eu.ict.persist.RFID.api.RFIDClientAPI", clientDPI.toUriString(), true, "acknowledgeRegistration", false, new String[] { intAsXML }, new String[] { Integer.class.getName() });
        try {
            this.msgQ = this.getMessageQueue();
            this.msgQ.addServiceMessage(msg);
        } catch (ONMException e) {
            e.printStackTrace();
        }
    }

    public void sendUpdateMessage(IDigitalPersonalIdentifier clientDPI, String tagNumber, String symLoc) {
        this.msgQ = this.getMessageQueue();
        ServiceMessage msg = new ServiceMessage(this.myServiceId.toString(), "eu.ict.persist.RFID.api.RFIDClientAPI", clientDPI.toUriString(), true, "sendUpdate", true, new String[] { symLoc, tagNumber }, new String[] { String.class.getName() });
        try {
            this.msgQ.addServiceMessage(msg);
        } catch (ONMException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to send RFID update to :" + clientDPI.toUriString());
        }
    }

    @Override
    public IServiceIdentifier getID() {
        return this.myServiceId;
    }

    @Override
    public void setID(IServiceIdentifier serviceID) {
        logging.info("I got my serviceID: " + serviceID.toUriString());
        this.myServiceId = serviceID;
    }

    @Override
    public boolean startUserSession(String sessionId, IDigitalPersonalIdentifier consumerDPI, IDigitalPersonalIdentifier publicDPI) {
        logging.info("Session started with sessionId : " + sessionId + ", consumerDPI: " + consumerDPI.toString() + ", publicDPI: " + publicDPI.toString());
        System.out.println("****************************\n\n\n\n*******************\n\n\nIMPL service ID: " + myServiceId);
        return true;
    }

    @Override
    public boolean stopUserSession(String sessionId, IDigitalPersonalIdentifier consumerDPI, IDigitalPersonalIdentifier publicDPI) {
        logging.info("Session stopped with sessionId : " + sessionId + ", consumerDPI: " + consumerDPI.toString() + ", publicDPI: " + publicDPI.toString());
        return true;
    }

    @Override
    public void setControllerDPI(IDigitalPersonalIdentifier controllerDPI) {
        if (controllerDPI == null) {
            logging.info("controllerDPI is null - all dpis have control");
        } else {
            logging.info("Setting the controller DPI to: " + controllerDPI.toString());
        }
        this.controllerDPI = controllerDPI;
    }

    public String getClassName() {
        return RFIDIMPL.class.getName();
    }

    public void setClassName(String className) {
        return;
    }

    public void sendUpdate(String wUnit, String rfidTagNumber) {
        String symLoc = "other";
        if (this.wUnitToSymloc.containsKey(wUnit)) {
            symLoc = this.wUnitToSymloc.get(wUnit);
        } else {
            System.out.println("wUnit: " + wUnit + " not found, wUnit length: " + wUnit.length());
            Enumeration<String> e = this.wUnitToSymloc.keys();
            System.out.println("Existing wUnits: ");
            while (e.hasMoreElements()) {
                String u = e.nextElement();
                System.out.println(u + " size: " + u.length());
            }
        }
        if (this.tagtoDPI.containsKey(rfidTagNumber)) {
            IDigitalPersonalIdentifier dpi = this.tagtoDPI.get(rfidTagNumber);
            this.sendUpdateMessage(dpi, rfidTagNumber, symLoc);
            this.frame.addRow(dpi.toUriString(), rfidTagNumber, wUnit, symLoc);
        } else {
            System.out.println("Tag: " + rfidTagNumber + " in location " + symLoc + " not registered to a DPI");
            this.frame.addRow("Unregistered", rfidTagNumber, wUnit, symLoc);
        }
    }

    @Override
    public void registerRFIDTag(String tagNumber, String dpiAsString, String password) {
        System.out.println("Received request to register RFID tag: " + tagNumber + " from dpi: " + dpiAsString);
        IDigitalPersonalIdentifier dpi;
        try {
            dpi = DigitalPersonalIdentifier.fromString(dpiAsString);
            if (this.tagToPassword.containsKey(tagNumber)) {
                String myPass = this.tagToPassword.get(tagNumber);
                System.out.println("Tag exists");
                if (myPass.equalsIgnoreCase(password)) {
                    this.tagtoDPI.put(tagNumber, dpi);
                    this.sendAcknowledgeMessage(dpi, 0);
                    System.out.println("Registration successfull. Sent Acknowledgement 0");
                } else {
                    this.sendAcknowledgeMessage(dpi, 1);
                    System.out.println("Registration unsuccessfull. Sent Ack 1");
                }
            } else {
                this.sendAcknowledgeMessage(dpi, 2);
                System.out.println("Registration unsuccessfull. Sent Ack 2");
            }
        } catch (MalformedDigitialPersonalIdentifierException e) {
            e.printStackTrace();
            System.out.println("ALL HELL BROKE LOOSE!");
        }
    }

    public String getPassword() {
        int n = 4;
        char[] pw = new char[n];
        int c = 'A';
        int r1 = 0;
        for (int i = 0; i < n; i++) {
            r1 = (int) (Math.random() * 3);
            switch(r1) {
                case 0:
                    c = '0' + (int) (Math.random() * 10);
                    break;
                case 1:
                    c = 'a' + (int) (Math.random() * 26);
                    break;
                case 2:
                    c = 'A' + (int) (Math.random() * 26);
                    break;
            }
            pw[i] = (char) c;
        }
        return new String(pw);
    }

    public void storePassword(String tagNumber, String password) {
        this.tagToPassword.put(tagNumber, password);
        this.tagtoDPI.remove(tagNumber);
    }

    public static void main(String[] args) throws IOException {
        RFIDIMPL impl = new RFIDIMPL(null);
        System.out.println(impl.getPassword());
        RFIDConfig config = new RFIDConfig();
        impl.wUnitToSymloc = config.getUnitToSymloc();
    }
}
