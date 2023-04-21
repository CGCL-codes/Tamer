package uk.org.ogsadai.dbcreation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import uk.org.ogsadai.dbcreation.CreateExtDatabaseException;

/**
 * This program will create a new eXist database containing a new
 * collection and populate it with M documents consisting of an ID
 * number, name (First, Last), Address, Telephone Number, Date of
 * Birth, Creation Timestamp, Time of Birth, Biography, Piture,
 * Religious boolean, Height, Long value, Double value.
 * <p>
 * This program will attempt to connect to the database specified and
 * to try to create the new table. 
 * </p>
 * <p>
 * The Nth document in each possible database will be identical across
 * each one which has at least N documents (assuming
 * <code>Random</code> is implemented the same on all Java JDKs).
 * </p>
 * <p>
 * It makes use of a base file which contains references to other
 * files. Together this provides the test data. See 
 * <code>ogsa-dai-cvs/test-framework/databases/TestDBSource/filelist.txt</code> * for an example of this file format and the formats of the dependant
 * files that are also read. 
 * </p>
 * <p>
 * <pre>
 * Usage:
 * java uk.org.ogsadai.client.dbcreate.CreateExtTypeExistDB 
 *         [-driverclass DriverClass]
 *         [-host HostName] [-database DatabaseName]
 *         [-username UserName] [-password Password]
 *         [-collectionname NameOfCollectionToCreate] 
 *         [-documents NumberOfDocumentsToCreate]
 *         -file baseFileName
 * Default Settings:
 *     DriverClass:               org.exist.xmldb.DatabaseImpl
 *     HostName:                  localhost
 *     PortNumber:                8080
 *     DatabaseName:              db
 *     UserName:                  guest
 *     Password:                  guest
 *     NameOfCollectionToCreate:  extblackbook
 *     NumberOfDocumentsToCreate: 2000
 * </pre>
 * <p>
 * For example:
 * </p>
 * <pre>
 * cd ogsa-dai-cvs/test-framework/databases/TestDBSource/
 * java -Duser.dir=/home/michaelj/ogsa-dai-cvs/test-framework/databases/TestDBSource/ uk.org.ogsadai.dbcreation.CreateExtTypeExistDB -database daitest -username daitester -password 0D-T35t\!acc -file filelist.txt 
 * </pre>
 * 
 * @author The OGSA-DAI Project Team
 */
public class CreateExtTypeExistDB extends CreateExtTypeDB {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008.";

    /** Name of the collection to hold the test data. */
    private String mNewCollectionName;

    /**
     * Parse the arguments and populates the test database. Displays
     * an error message if the arguments are invalid or an error
     * arises while creating the test database.
     * 
     * @param args 
     *     Arguments passed to the client's main method.
     * @throws IllegalArgumentException
     *     If there are any argument problems.
     * @throws CreateExtDatabaseException
     *     If there is a problem loading the driver, extracting the
     *     file data or using the database.
     */
    public CreateExtTypeExistDB(String[] args) throws CreateExtDatabaseException, IllegalArgumentException {
        super(args);
    }

    /**
     * Populates the test collection.
     *
     * {@inheritDoc}
     */
    public void populateDatabaseTable() throws Exception {
        Collection collection = null;
        Collection child = null;
        try {
            final Class c = Class.forName(mDriver);
            final Database db = (Database) c.newInstance();
            DatabaseManager.registerDatabase(db);
            final String existURL = "xmldb:exist://" + mHost + ":" + mPort + "/exist/xmlrpc/" + mDatabase + "/";
            System.out.println("Opening base collection: " + existURL);
            collection = DatabaseManager.getCollection(existURL);
            final CollectionManagementService service = (CollectionManagementService) collection.getService("CollectionManagementService", "1.0");
            child = collection.getChildCollection(mNewCollectionName);
            if (child != null) {
                service.removeCollection(mNewCollectionName);
            }
            System.out.println("Creating new collection named '" + mNewCollectionName + "'.");
            child = service.createCollection(mNewCollectionName);
            System.out.println("Adding " + mNumberOfEntries + " documents to '" + mNewCollectionName + "'...");
            String documentString;
            String resourceID;
            XMLResource document;
            BinaryResource imgFile;
            for (int i = 1; i <= mNumberOfEntries; i++) {
                resourceID = String.valueOf(i);
                documentString = makeDocument(i);
                document = (XMLResource) child.createResource(resourceID, "XMLResource");
                imgFile = (BinaryResource) child.createResource(resourceID + "image", "BinaryResource");
                File iFile = new File(mDataCreator.getImageFileName(i));
                byte[] bytes = new byte[(int) iFile.length()];
                FileInputStream fis = new FileInputStream(iFile);
                fis.read(bytes);
                imgFile.setContent(bytes);
                child.storeResource(imgFile);
                document.setContent(documentString);
                child.storeResource(document);
            }
        } finally {
            closeCollection(child);
            closeCollection(collection);
        }
    }

    /**
     * Closes the specified collection.
     * 
     * @param child
     *     Name of the collection to close.
     */
    private void closeCollection(Collection child) {
        if (child != null) {
            try {
                child.close();
            } catch (XMLDBException e) {
            }
        }
    }

    /**
     * Replace all <code>&</code> with <code>&amp;</code> in a string
     * for XML storage.
     *
     * @param str
     *     String to be modified.
     * @return string with replacementa made.
     */
    private String replaceAmp(String str) {
        return str.replaceAll("&", "&amp;");
    }

    /**
     * Load a text file and return as a string buffer.
     *
     * @param fileName
     *     File name.
     * @return string buffer.
     *
     */
    private StringBuffer textFileReader(String fileName) {
        File bFile = new File(fileName);
        FileReader isBiog;
        StringBuffer sbClob = new StringBuffer();
        try {
            isBiog = new FileReader(bFile);
            BufferedReader bReader = new BufferedReader(isBiog);
            String line = null;
            while ((line = bReader.readLine()) != null) {
                sbClob.append(replaceAmp(line));
                sbClob.append(System.getProperty("line.separator"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            sbClob.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
            sbClob.append("\n");
        }
        return sbClob;
    }

    /**
     * Make an XML document.
     *
     * @param i
     *     Document index.
     * @return XML document.
     */
    private String makeDocument(final int i) {
        final StringBuffer doc = new StringBuffer();
        doc.append("<entry id='" + i + "'>\n");
        doc.append("  <name>");
        doc.append(replaceAmp(mDataCreator.getName(i)));
        doc.append("</name>\n");
        doc.append("  <address>");
        doc.append(replaceAmp(mDataCreator.getAddress(i)));
        doc.append("</address>\n");
        doc.append("  <phone>");
        doc.append(mDataCreator.getPhone(i));
        doc.append("</phone>\n");
        doc.append("  <DOB>");
        doc.append(mDataCreator.getDOB());
        doc.append("</DOB>\n");
        doc.append("  <creationTimestamp>");
        doc.append(mDataCreator.getCreationTimeStamp());
        doc.append("</creationTimestamp>\n");
        doc.append("  <DOBTime>");
        doc.append(mDataCreator.getDOBTime());
        doc.append("</DOBTime>\n");
        doc.append("  <Religious>");
        doc.append(mDataCreator.getReligious());
        doc.append("</Religious>\n");
        doc.append("  <height>");
        doc.append(mDataCreator.getHeight());
        doc.append("</height>\n");
        doc.append("  <verylongnumber>");
        doc.append(mDataCreator.getJavaLong());
        doc.append("</verylongnumber>\n");
        doc.append("  <double>");
        doc.append(mDataCreator.getJavaDouble());
        doc.append("</double>\n");
        doc.append("  <bio>");
        doc.append(textFileReader(mDataCreator.getBioFileName(i)));
        doc.append("</bio>\n");
        doc.append("  <imageResourceID>");
        doc.append(i + "image");
        doc.append("</imageResourceID>\n");
        doc.append("</entry>");
        return doc.toString();
    }

    /**
     * {@inheritDoc}
     */
    protected void setupDBCreationArguments(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].toLowerCase().equals("-driverclass")) {
                mDriver = args[i + 1];
            } else if (args[i].toLowerCase().equals("-host")) {
                mHost = args[i + 1];
            } else if (args[i].toLowerCase().equals("-port")) {
                mPort = args[i + 1];
            } else if (args[i].toLowerCase().equals("-database")) {
                mDatabase = args[i + 1];
            } else if (args[i].toLowerCase().equals("-username")) {
                mUsername = args[i + 1];
            } else if (args[i].toLowerCase().equals("-password")) {
                mPassword = args[i + 1];
            } else if (args[i].toLowerCase().equals("-collectionname")) {
                mNewCollectionName = args[i + 1];
            } else if (args[i].toLowerCase().equals("-documents")) {
                mNumberOfEntries = Integer.parseInt(args[i + 1]);
            } else if (args[i].toLowerCase().equals("-file")) {
                mFileMaster = args[i + 1];
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected String printUsage() {
        return ("Usage:\n\n" + "\tjava uk.org.ogsadai.client.dbcreate.CreateExtType" + mDBMS + "DB " + "[-driverclass " + mDBMS + "DriverClass] \n\t\t" + "[-host " + mDBMS + "HostName] " + "[-port " + mDBMS + "PortNumber] " + "[-database " + mDBMS + "DatabaseName] \n\t\t" + "[-username " + mDBMS + "UserName] " + "[-password " + mDBMS + "Password] \n\t\t" + "[-collectionname NameOfCollectionToCreate] " + "[-documents NumberOfDocumentsToCreate]\n" + "-file FileName");
    }

    /**
     * {@inheritDoc}
     */
    public String returnVariables() {
        return (" Settings:" + "\n\t" + mDBMS + "DriverClass:    \t" + mDriver + "\n\t" + mDBMS + "HostName:       \t" + mHost + "\n\t" + mDBMS + "PortNumber:     \t" + mPort + "\n\t" + mDBMS + "DatabaseName:   \t" + mDatabase + "\n\t" + mDBMS + "UserName:       \t" + mUsername + "\n\t" + mDBMS + "Password:       \t" + mPassword + "\n\tNameOfCollectionToCreate: \t" + mNewCollectionName + "\n\tNumberOfDocumentsToCreate:\t" + mNumberOfEntries + "\n\tFile Master List File:       \t" + mFileMaster);
    }

    /**
     * {@inheritDoc}
     *
     * No-op for obvious reasons.
     */
    protected void dropTableIfExists(Connection connection) throws SQLException {
    }

    /**
     * {@inheritDoc}
     */
    protected String getConnectionURL() {
        return "xmldb:exist://" + mHost + ":" + mPort + "/exist/xmlrpc/" + mDatabase + "/";
    }

    /**
     * {@inheritDoc}
     */
    protected void setDefaultSettings() {
        mDBMS = "eXist";
        mDriver = "org.exist.xmldb.DatabaseImpl";
        mHost = "localhost";
        mPort = "8080";
        mDatabase = "db";
        mUsername = "guest";
        mPassword = "guest";
        mNewCollectionName = "extblackbook";
    }

    /**
     * Command-line program.
     *
     * @param args
     *     Arguments as described in the header.
     * @throws Exception
     *     If any problems arise
     */
    public static void main(String[] args) throws Exception {
        CreateExtTypeExistDB mCTMExt = new CreateExtTypeExistDB(args);
        System.out.println(mCTMExt.returnVariables());
        mCTMExt.populateDatabaseTable();
    }
}
