package eu.more.jotau.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;
import java.util.List;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.protocol.PipeAdvertisement;
import org.apache.log4j.Logger;
import eu.more.jotau.objects.GetConfigurationFileByteArrayMessageElement;
import eu.more.jotau.objects.GetConfigurationFilePort;
import eu.more.jotau.objects.GetStatusPort;
import eu.more.jotau.tools.Constants;

/**
 * @author seraphim
 *
 */
public class ClientMain {

    private NetworkManager manager;

    private boolean startup;

    private PeerGroup netPeerGroup;

    private PeerGroupAdvertisement groupAdvertisement;

    private DiscoveryService discovery;

    private PipeService pipeService;

    private Logger logger = Logger.getLogger(getClass());

    /**
   *
   */
    public ClientMain() {
        File f = new File("recieved");
        if (!f.exists()) f.mkdir();
    }

    private void startClient() {
        try {
            logger.info("Setup client");
            manager = new NetworkManager(NetworkManager.ConfigMode.EDGE, "ConfigurationClient", new File(new File(".cache"), "ConfigurationClient").toURI());
            NetworkConfigurator conf = manager.getConfigurator();
            conf.addSeedRendezvous(new URI("tcp://" + Constants.RDV_RELAY_SERVER + ":9701/"));
            conf.addSeedRelay(new URI("tcp://" + Constants.RDV_RELAY_SERVER + ":9701/"));
            conf.addSeedRendezvous(new URI("http://" + Constants.RDV_RELAY_SERVER + ":9700/"));
            conf.addSeedRelay(new URI("http://" + Constants.RDV_RELAY_SERVER + ":9700/"));
            conf.setUseOnlyRendezvousSeeds(true);
            conf.setUseOnlyRelaySeeds(true);
            manager.startNetwork();
            startup = true;
            logger.info("Client set up!");
        } catch (Exception e) {
            logger.error(e);
            System.exit(-1);
        }
        netPeerGroup = manager.getNetPeerGroup();
        groupAdvertisement = netPeerGroup.getPeerGroupAdvertisement();
        logger.info("Getting DiscoveryService");
        discovery = netPeerGroup.getDiscoveryService();
        logger.info("Getting PipeService");
        pipeService = netPeerGroup.getPipeService();
    }

    private Enumeration searchAdvertisment(String name, String modSpecName, ModuleSpecAdvertisement madv, PipeAdvertisement pipeadv) {
        if (!startup) startClient();
        logger.info("searching for the advertisement responding to " + modSpecName);
        Enumeration en;
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
        }
        long time = System.currentTimeMillis();
        while (true) {
            try {
                en = discovery.getLocalAdvertisements(DiscoveryService.ADV, name, modSpecName);
                if ((en != null) && en.hasMoreElements()) {
                    break;
                }
                discovery.getRemoteAdvertisements(null, DiscoveryService.ADV, name, modSpecName, 1, null);
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                }
            } catch (IOException e) {
                logger.warn(e);
            }
        }
        logger.info("Searching-duration: " + (System.currentTimeMillis() - time));
        logger.info("we found the service advertisement");
        return en;
    }

    public boolean isUpToDate(String filename) throws FileNotFoundException, IOException, InterruptedException {
        logger.info("Retrieving status information of " + filename);
        ModuleSpecAdvertisement mdsadv = null;
        PipeAdvertisement pipeadv = null;
        boolean success = false;
        Enumeration en = searchAdvertisment("Name", GetStatusPort.MODULE_SPEC_NAME, mdsadv, pipeadv);
        mdsadv = (ModuleSpecAdvertisement) en.nextElement();
        pipeadv = mdsadv.getPipeAdvertisement();
        OutputPipe outputPipe = pipeService.createOutputPipe(pipeadv, 10000);
        PipeID pipeID = null;
        pipeID = IDFactory.newPipeID(netPeerGroup.getPeerGroupID());
        PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        advertisement.setPipeID(pipeID);
        advertisement.setType(PipeService.UnicastType);
        advertisement.setName("Pipe tutorial");
        InputPipe inputPipe = pipeService.createInputPipe(advertisement);
        discovery.publish(advertisement);
        Message msg = new Message();
        logger.info("PIPE-ID: " + pipeID.toString());
        StringMessageElement sme = new StringMessageElement("returnPipeID", pipeID.toString(), null);
        StringMessageElement filenameME = new StringMessageElement("filename", filename, null);
        msg.addMessageElement(filenameME);
        msg.addMessageElement(sme);
        outputPipe.send(msg);
        logger.info("message sent to the ServiceServer - waiting for response");
        msg = inputPipe.poll(10000);
        if (msg == null) {
            logger.warn("NO ANSWER");
            success = false;
        } else {
            ByteArrayMessageElement responseElement = (ByteArrayMessageElement) msg.getMessageElement("upToDate");
            String response = null;
            if (responseElement != null) response = responseElement.toString();
            if (response == null) throw new FileNotFoundException();
            int responseValue = Integer.parseInt(response);
            if (responseValue == -1) throw new FileNotFoundException();
            return responseValue == 1;
        }
        inputPipe.close();
        return success;
    }

    public boolean getConfigurationFile(String filename) throws FileNotFoundException, IOException, InterruptedException {
        ModuleSpecAdvertisement mdsadv = null;
        PipeAdvertisement pipeadv = null;
        boolean success = false;
        Enumeration en = searchAdvertisment("Name", GetConfigurationFilePort.MODULE_SPEC_NAME, mdsadv, pipeadv);
        mdsadv = (ModuleSpecAdvertisement) en.nextElement();
        pipeadv = mdsadv.getPipeAdvertisement();
        OutputPipe outputPipe = pipeService.createOutputPipe(pipeadv, 10000);
        PipeID pipeID = null;
        pipeID = IDFactory.newPipeID(netPeerGroup.getPeerGroupID());
        PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        advertisement.setPipeID(pipeID);
        advertisement.setType(PipeService.UnicastType);
        advertisement.setName("Pipe tutorial");
        InputPipe inputPipe = pipeService.createInputPipe(advertisement);
        discovery.publish(advertisement);
        Message msg = new Message();
        logger.info("PIPE-ID: " + pipeID.toString());
        StringMessageElement sme = new StringMessageElement("returnPipeID", pipeID.toString(), null);
        StringMessageElement filenameME = new StringMessageElement("filename", filename, null);
        msg.addMessageElement(filenameME);
        msg.addMessageElement(sme);
        outputPipe.send(msg);
        logger.info("message sent to the ServiceServer - waiting for response");
        msg = inputPipe.poll(10000);
        if (msg == null) {
            logger.error("NO ANSWER ");
            success = false;
        } else {
            ByteArrayMessageElement responseElement = (ByteArrayMessageElement) msg.getMessageElement("file");
            GetConfigurationFileByteArrayMessageElement fileElement = new GetConfigurationFileByteArrayMessageElement(responseElement);
            List<Character> getCharList = fileElement.GetCharList();
            if (getCharList.size() == 0) throw new FileNotFoundException();
            File r = new File("./recieved/" + filename);
            FileWriter writer = new FileWriter(r);
            StringBuffer buffer = new StringBuffer();
            for (Character character : getCharList) {
                buffer.append(character);
            }
            writer.write(buffer.toString());
            writer.close();
            success = true;
        }
        inputPipe.close();
        return success;
    }

    public static void main(String[] args) {
        ClientMain main = new ClientMain();
        main.startClient();
        try {
            if (main.isUpToDate("test.txt")) System.out.println("File is up to date"); else System.out.println("File is out of date");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if (main.getConfigurationFile("test.txt")) System.out.println("File is successfully written"); else System.out.println("Writing file failed!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
