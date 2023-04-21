package uk.ac.ebi.intact.synchron;

import org.apache.ojb.broker.*;
import org.apache.ojb.broker.util.logging.*;
import org.apache.ojb.broker.accesslayer.*;
import org.apache.ojb.broker.query.*;
import org.apache.ojb.broker.util.*;
import org.apache.ojb.odmg.OJB;
import org.odmg.Implementation;
import org.odmg.Database;
import org.odmg.ODMGException;
import java.util.*;
import java.util.zip.CRC32;
import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import com.enterprisedt.net.ftp.*;
import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.util.Utilities;

/**
 * Purpose: collects XML files
 * from others IntAct nodes
 */
public class Collector {

    /**
     * OJB persistenceBroker, which defines the interface
     * between Java Objects and persistent objects
     */
    private PersistenceBroker broker;

    /**
     * provides all necessary information for the mapping between
     * persistent objects and java objects;
     * is needed by the broker
     */
    private String repositoryFile;

    /**
     * provides an XmlLoader object to load an XML file
     */
    private XmlLoader xmlLoader;

    /**
     * provides an ftp connection to another Intact node
     */
    private FTPClient ftpClient;

    private Properties properties = null;

    /**
     * Create a PersistenceBroker object and initializes
     * so the Broker filed; allocates a XmlLoader object
     * and initializes so the XmlLoader field.
     */
    private void init() throws Exception {
        try {
            properties = Utilities.getProperties("config");
        } catch (IOException e) {
            System.out.println("[ERROR] Was not possible to get the poperties");
            throw e;
        }
        try {
            broker = PersistenceBrokerFactory.createPersistenceBroker("repository.xml");
        } catch (MalformedURLException e) {
            System.out.println("[ERROR] Was not possible to get the repository file");
            throw e;
        }
        try {
            xmlLoader = new XmlLoader(broker);
        } catch (Exception e) {
            System.out.println("[ERROR] Was not possible to create the loader");
            throw e;
        }
    }

    /**
     * accesses to the database to get all existing local
     * intact nodes, which are not rejected; returns a
     * Iterator that contains this ImtactNode object
     */
    private Iterator getIntactNodes() throws Exception {
        Criteria c = new Criteria();
        c.addEqualTo("rejected", new Integer(0));
        c.addNotLike("ownerPrefix", (properties.getProperty("ownerPrefix", "")));
        return (broker.getCollectionByQuery(new QueryByCriteria(IntactNode.class, c))).iterator();
    }

    /**
     * Open a ftp connection to the specified intact node
     * get a file list of available files produced by this
     * intact node
     * choose the requiered files and download them
     * for each file calls checkFile and XmlLoader.loadFile
     */
    private void getXmlFiles(IntactNode node) throws Exception {
        System.out.println("[Collector get XML files for node : " + node.getOwnerPrefix() + "]");
        int lastId = node.getLastCheckId();
        int firstId = lastId + 1;
        try {
            System.out.println(node.getFtpLogin() + " " + node.getFtpPassword() + " " + node.getFtpAddress() + " " + node.getFtpDirectory());
            ftpClient = new FTPClient(node.getFtpAddress());
            ftpClient.user(node.getFtpLogin());
            ftpClient.password(node.getFtpPassword());
            ftpClient.chdir(node.getFtpDirectory());
        } catch (FTPException ftpe) {
            System.out.println("[ERROR] unable to connect to ftp server: " + node.getFtpAddress());
            System.out.println("        maybe wrong address, login, password");
            return;
        } catch (IOException e) {
            System.out.println("[ERROR] unable to connect to ftp server: ");
            return;
        }
        try {
            String files[];
            String releaseFiles[];
            try {
                files = ftpClient.dir("*.xml");
            } catch (FTPException ftpe) {
                System.out.println("no files found for node " + node.getOwnerPrefix());
                return;
            }
            try {
                releaseFiles = ftpClient.dir("*-R-*.xml");
                for (int i = 0; i < Array.getLength(releaseFiles); i++) {
                    System.out.println("[release file :" + releaseFiles[i] + "]");
                    int id = Integer.parseInt(releaseFiles[i].substring(0, files[i].indexOf('-')));
                    if (id > firstId) firstId = id;
                }
            } catch (FTPException ftpe) {
                System.out.println("no release file found for node " + node.getOwnerPrefix());
            }
            HashMap files2download = new HashMap();
            for (int i = 0; i < Array.getLength(files); i++) {
                System.out.println("[found file :" + files[i] + "]");
                int id = Integer.parseInt(files[i].substring(0, files[i].indexOf('-')));
                if (id > lastId) lastId = id;
                files2download.put(new Integer(id), files[i]);
            }
            if (lastId == node.getLastCheckId()) return;
            for (int i = firstId; i <= lastId; i++) {
                String fileName = (String) files2download.get(new Integer(i));
                System.out.println("[Download file: " + fileName + "]");
                ftpClient.get(properties.getProperty("Collector.directory") + "/" + fileName, fileName);
            }
            for (int i = firstId; i <= lastId; i++) {
                String fileName = (String) (String) files2download.get(new Integer(i));
                System.out.println("[load file:" + fileName + "]");
                try {
                    broker.clearCache();
                    xmlLoader.loadFile(properties.getProperty("Collector.directory") + "/" + fileName);
                } catch (Exception e) {
                    System.out.println("[ERROR] unable to load the file " + fileName + " from node " + node.getOwner().getFullName());
                    return;
                }
            }
            node.setLastCheckId(lastId);
            broker.store(node);
        } catch (FTPException ftpe) {
            System.out.println("[ERROR] no xml file found on ftp server: " + node.getFtpAddress());
            ftpe.printStackTrace(System.out);
            return;
        } catch (IOException e) {
            System.out.println("[ERROR] was not possible to get the files from " + node.getFtpAddress());
            e.printStackTrace(System.out);
            return;
        }
    }

    /**
     * check if the file was not damaged
     * during the transfert by computing the
     * checksum of the received XML file and compare
     * to the checksum in the crc32 file.`
     */
    private boolean isNotCorrupted(String fileName) throws Exception {
        FileReader xmlFile;
        FileReader checksumFile;
        CRC32 check = new CRC32();
        int c;
        String compareCheck = "";
        check.reset();
        try {
            xmlFile = new FileReader(fileName);
            while ((c = xmlFile.read()) != -1) {
                check.update((char) c);
            }
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] was not possible to open xml file: " + fileName);
            throw e;
        } catch (IOException e) {
            System.out.println("[ERROR] was not possible to read xml file: " + fileName);
            throw e;
        }
        try {
            checksumFile = new FileReader(fileName.substring(0, fileName.length() - 4) + ".crc32");
            while ((c = checksumFile.read()) != -1) {
                compareCheck += (char) c;
            }
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] was not possible to open checksum file: " + fileName.substring(0, fileName.length() - 4) + ".crc32");
            throw e;
        } catch (IOException e) {
            System.out.println("[ERROR] was not possible to read checksum file: " + fileName.substring(0, fileName.length() - 4) + ".crc32");
            throw e;
        }
        return compareCheck.trim().equalsIgnoreCase(String.valueOf(check.getValue()));
    }

    /**
     * Calls init() to initialise some fields
     * calls getIntactNodes and retrieves an ObjectSet of IntactNode objects
     * For each IntactNode object it calls getXmlFiles().
     */
    public static void main(String[] args) throws Exception {
        Collector c = new Collector();
        try {
            c.init();
        } catch (Exception e) {
            System.out.println("[ERROR] Was not possible to initialize the collector");
            e.printStackTrace(System.out);
            System.exit(0);
        }
        Iterator intactNodes = c.getIntactNodes();
        while (intactNodes.hasNext()) {
            IntactNode in = (IntactNode) intactNodes.next();
            System.out.println("[node: " + in.getOwnerPrefix() + "]");
            c.getXmlFiles(in);
        }
    }
}
