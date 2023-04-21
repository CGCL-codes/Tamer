package org.singularityoss.devicemgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import com.wavechain.interrogatorIO.IInterrogatorIO;
import com.wavechain.interrogatorIO.InterrogatorFactory;
import com.wavechain.system.ECSpecProfile;
import com.wavechain.system.Event;
import com.wavechain.system.Reader;
import com.wavechain.system.ReaderComponent;
import com.wavechain.system.Sensor;
import com.wavechain.util.JMSUtil;
import com.wavechain.util.XMLUtil;

/**
 * 
 * @author TomRose
 * 
 */
public abstract class DeviceManagerAbstractImpl implements DeviceManager {

    Logger log = Logger.getLogger(this.getClass());

    private enum rcAttributes {

        readerComponentName, interrogatorIPAddress, interrogatorPort, interrogatorDriver, Reader, Sensor
    }

    ;

    private List<IInterrogatorIO> interrogators = Collections.synchronizedList(new ArrayList<IInterrogatorIO>());

    private List<Reader> readers = Collections.synchronizedList(new ArrayList<Reader>());

    private Hashtable ecSpecHash = new Hashtable();

    public void ping() throws Exception {
    }

    protected DeviceManagerAbstractImpl() {
    }

    /**
	 * Get the configuration from the configuration service. DeviceManagerRemote
	 * instance must know what ReaderComponent(s) it will be servicing and
	 * lookup the required information. ReaderComponents will be identified by
	 * EPC (SGTIN). Note: currently just in a properties file
	 * 
	 * 
	 * <li> Create a list ReaderComponents serviced by this device manager
	 * Create a list of Readers defined for all the sensors defined for the
	 * Reader Components.
	 * 
	 * </li>
	 * 
	 * Note: A Reader has ReaderComponents, each or
	 * 
	 * @return
	 * @throws Exception
	 */
    protected void getConfiguration() throws Exception {
        URL url = this.getClass().getResource("/physical_readers.properties");
        URLConnection site = url.openConnection();
        InputStream is = site.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bufferReader;
        bufferReader = new BufferedReader(isr);
        HashMap<String, String[]> physicalReaderHash = new HashMap<String, String[]>();
        String line = null;
        try {
            while ((line = bufferReader.readLine()) != null) {
                if (line.startsWith("#")) continue;
                log.debug("ALE physicalReader attributes = " + line);
                StringTokenizer sTokenizer = new StringTokenizer(line);
                int i = 0;
                String readerComponentId = "";
                String[] pReaderAttributes = new String[rcAttributes.values().length];
                while (sTokenizer.hasMoreTokens()) {
                    String token = sTokenizer.nextToken();
                    if (i == 0) readerComponentId = token; else pReaderAttributes[i - 1] = token;
                    i++;
                }
                physicalReaderHash.put(readerComponentId, pReaderAttributes);
                ReaderComponent readerComponent = new ReaderComponent(readerComponentId);
                readerComponent.setName(pReaderAttributes[rcAttributes.readerComponentName.ordinal()]);
                readerComponent.setIpAddress(pReaderAttributes[rcAttributes.interrogatorIPAddress.ordinal()]);
                readerComponent.setPort(pReaderAttributes[rcAttributes.interrogatorPort.ordinal()]);
                readerComponent.setInterrogatorClass(Class.forName(pReaderAttributes[rcAttributes.interrogatorDriver.ordinal()]));
                ArrayList<Sensor> sensors = new ArrayList<Sensor>();
                sensors.add(new Sensor(pReaderAttributes[rcAttributes.Sensor.ordinal()]));
                readerComponent.setSensors(sensors);
                IInterrogatorIO interrogator = InterrogatorFactory.create(readerComponent);
                Reader reader = new Reader(pReaderAttributes[rcAttributes.Reader.ordinal()]);
                reader.setSensors(sensors);
                readers.add(reader);
                interrogators.add(interrogator);
                interrogator.setDeviceManager(this);
            }
        } catch (IOException e) {
            log.error("unable to read properties stream", e);
            throw new Exception("unable to read properties stream");
        }
    }

    /**
	 * Initialize the DeviceManager
	 * 
	 * 1. Create ReaderComponents, and Assign Readers 2. Create Interrogator,
	 * and assign a ReaderComponet. 3. DeviceManagerRemote will get SensorEvents
	 * and
	 * 
	 * @throws Exception
	 * 
	 * @throws Exception
	 * 
	 */
    void init() throws Exception {
        getConfiguration();
        log.info("Device Manager Started...");
    }

    public void sendEvent(Event event) throws Exception {
        String sensorId = event.getSensor().getId();
        Enumeration ecSpecEnum = ecSpecHash.keys();
        while (ecSpecEnum.hasMoreElements()) {
            String ecSpecName = (String) ecSpecEnum.nextElement();
            ECSpecProfile specProfile = (ECSpecProfile) ecSpecHash.get(ecSpecName);
            if (System.currentTimeMillis() < specProfile.getEndTime()) {
                Document doc = XMLUtil.generateXMLFromListOfIntArrays(sensorId, event.getSensor().getTagIds());
                JMSUtil.deliverMessageToQueue(specProfile.getQueueName(), doc);
                System.out.println("Message Sent on -> " + specProfile.getQueueName());
            } else {
                unRegisterECSpecProfile(specProfile.getSpecName());
            }
        }
    }

    public String sayHello() throws RemoteException, Exception {
        return "Hello there";
    }

    public void registerECSpecProfile(ECSpecProfile specProfile) throws RemoteException, Exception {
        ecSpecHash.put(specProfile.getSpecName(), specProfile);
        try {
            Iterator<IInterrogatorIO> iterator = interrogators.iterator();
            while (iterator.hasNext()) {
                iterator.next().on();
            }
        } catch (Exception e) {
            String message = "unable to start interrogators";
            log.error(message, e);
            throw new Exception(message, e);
        }
    }

    public void unRegisterECSpecProfile(String specProfileName) throws RemoteException, Exception {
        ecSpecHash.remove(specProfileName);
        if (ecSpecHash.size() <= 0) {
            try {
                Iterator<IInterrogatorIO> iterator = interrogators.iterator();
                while (iterator.hasNext()) {
                    iterator.next().off();
                }
            } catch (Exception e) {
                String message = "unable to start interrogators";
                log.error(message, e);
                throw new Exception(message, e);
            }
        }
    }

    /**
	 * @return Returns the readers.
	 */
    public List<Reader> getReaders() {
        return readers;
    }

    /**
	 * @param readers The readers to set.
	 */
    public void setReaders(List<Reader> readers) {
        this.readers = readers;
    }

    /**
	 * @return Returns the interrogators.
	 */
    public List<IInterrogatorIO> getInterrogators() {
        return interrogators;
    }

    /**
	 * @param interrogators The interrogators to set.
	 */
    public void setInterrogators(List<IInterrogatorIO> interrogators) {
        this.interrogators = interrogators;
    }
}
