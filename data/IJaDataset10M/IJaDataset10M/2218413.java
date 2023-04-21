package org.freenet.freekiwiki;

import java.util.ArrayList;
import java.util.Iterator;
import java.math.BigInteger;
import javax.xml.bind.*;
import org.freenet.freekiwiki.wiki.*;
import org.freenet.freekiwiki.xml.*;
import org.freenet.freekiwiki.config.*;
import org.freenet.freekiwiki.fcp.*;
import org.freenet.freekiwiki.ui.UserInteraction;
import org.freenet.freekiwiki.ui.ConfigIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public final class InitialInsert implements Action {

    private FCPConnection fcp;

    private Configuration configuration;

    boolean quiet;

    private int startFrom = 1;

    private int totalToInsert = -1;

    public InitialInsert(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    public void setTotalToInsert(int totalToInsert) {
        this.totalToInsert = totalToInsert;
    }

    public void printConfiguration(boolean showchangeable) {
        if (configuration == null) {
            System.out.println("Configuration has not been initiated");
            return;
        }
        String activelink = configuration.getFreekiwikiConfiguration().getActivelink();
        System.out.println("\n" + "\tCurrent variables you can alter:\n" + "1.  Database host ip                    " + configuration.getDatabaseConfiguration().getHost() + "\n" + "2.  Database port                       " + configuration.getDatabaseConfiguration().getPort().toString() + "\n" + "3.  Database name                       " + configuration.getDatabaseConfiguration().getName() + "\n" + "4.  Previx for tables                   " + configuration.getDatabaseConfiguration().getPrefix() + "\n" + "5.  Database login and password         " + configuration.getDatabaseConfiguration().getLogin() + "\n" + "6.  Database type                       " + configuration.getDatabaseConfiguration().getType().value() + "\n" + "7.  Type of database layout             " + configuration.getDatabaseConfiguration().getLayout().value() + "\n" + (showchangeable ? "8.  Enter range of articles to insert   " + "\n" : "") + "11. Freenet node ip                     " + configuration.getFreekiwikiConfiguration().getHost().toString() + "\n" + "12. Freenet node port                   " + configuration.getFreekiwikiConfiguration().getPort().toString() + "\n" + "13. Private key                         " + configuration.getFreekiwikiConfiguration().getPrivateKey() + "\n" + "14. Public key                          " + configuration.getFreekiwikiConfiguration().getPublicKey() + "\n" + (showchangeable ? "15. Regenerate keys                     " + "\n" : "") + "21. Freekiwiki name                     " + configuration.getFreekiwikiConfiguration().getName() + "\n" + "22. Activelink file                     " + (activelink == null ? "[no activelink]" : activelink.toString()) + "\n" + (showchangeable ? "0.  Beem me up.                         " : ""));
    }

    public void start() {
        JAXBContext jaxbContext = null;
        Marshaller marshaller = null;
        Unmarshaller unmarshaller = null;
        Validator validator = null;
        try {
            jaxbContext = JAXBContext.newInstance("org.freenet.freekiwiki.xml:org.freenet.freekiwiki.config");
            marshaller = jaxbContext.createMarshaller();
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            System.err.println("JAXBException:");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        String[] keyPair = null;
        File activelink = null;
        for (int choice = -1; !quiet; ) {
            printConfiguration(true);
            choice = UserInteraction.askInt("Please enter the number corresponding to your choice");
            switch(choice) {
                case 0:
                    quiet = true;
                    break;
                case 1:
                    configuration.getDatabaseConfiguration().setHost(UserInteraction.askString("Please enter the host of the database server"));
                    break;
                case 2:
                    configuration.getDatabaseConfiguration().setPort(Integer.valueOf(UserInteraction.askInt("Please enter the port number to access the database server")));
                    break;
                case 3:
                    configuration.getDatabaseConfiguration().setName(UserInteraction.askString("Please enter the name of the database"));
                    break;
                case 4:
                    configuration.getDatabaseConfiguration().setPrefix(UserInteraction.askString("Please enter the prefix used by the tables in the database"));
                    break;
                case 5:
                    configuration.getDatabaseConfiguration().setLogin(UserInteraction.askString("Please enter database login"));
                    configuration.getDatabaseConfiguration().setPassword(UserInteraction.askString("Please enter database password"));
                    break;
                case 6:
                    for (boolean keepgoing = true; keepgoing == true; ) {
                        try {
                            configuration.getDatabaseConfiguration().setType(DatabaseEngine.fromValue(UserInteraction.askString("Please enter the type of the database")));
                            keepgoing = false;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Value you have entered is not acceptable");
                            keepgoing = true;
                        }
                    }
                    break;
                case 7:
                    for (boolean keepgoing = true; keepgoing == true; ) {
                        try {
                            configuration.getDatabaseConfiguration().setLayout(DatabaseLayout.fromValue(UserInteraction.askString("Please enter the layout of the database")));
                            keepgoing = false;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Value you have entered is not acceptable");
                            keepgoing = true;
                        }
                    }
                    break;
                case 8:
                    startFrom = UserInteraction.askInt("Please enter the article number to start from");
                    totalToInsert = UserInteraction.askInt("Please enter the total number of articles to insert");
                    break;
                case 11:
                    configuration.getFreekiwikiConfiguration().setHost(UserInteraction.askString("Please enter the host of the Freenet node"));
                    break;
                case 12:
                    configuration.getDatabaseConfiguration().setPort(Integer.valueOf(UserInteraction.askInt("Please enter the port number to access the FCPv2")));
                    break;
                case 13:
                    configuration.getFreekiwikiConfiguration().setPrivateKey(UserInteraction.askString("Please enter the private key"));
                    break;
                case 14:
                    configuration.getFreekiwikiConfiguration().setPublicKey(UserInteraction.askString("Please enter the public key"));
                    break;
                case 15:
                    try {
                        if (fcp == null) fcp = (new FCPConnectionFactory(configuration.getFreekiwikiConfiguration())).getFCPConnectionInstance();
                        keyPair = fcp.getSSKPair();
                        configuration.getFreekiwikiConfiguration().setPrivateKey(keyPair[0].substring(4, keyPair[0].indexOf("/")));
                        configuration.getFreekiwikiConfiguration().setPublicKey(keyPair[1].substring(4, keyPair[1].indexOf("/")));
                    } catch (IOException e) {
                        System.err.println("Connection creation failed");
                    }
                    break;
                case 21:
                    configuration.getFreekiwikiConfiguration().setName(SafeName.encodeUTF8(UserInteraction.askString("Please enter the name of the freekiwiki")));
                    break;
                case 22:
                    activelink = new File(UserInteraction.askString("Please enter the activelink file"));
                    if (activelink.exists()) {
                        System.err.println(activelink.toString() + " does not exist");
                        activelink = null;
                    } else if (activelink.isDirectory()) {
                        System.err.println(activelink.toString() + " is a directory");
                        activelink = null;
                    } else if (activelink.canRead()) {
                        System.err.println(activelink.toString() + " is unreadable");
                        activelink = null;
                    } else if (activelink.length() == 0) {
                        System.err.println(activelink.toString() + " has 0-length");
                        activelink = null;
                    }
                    if (activelink != null) configuration.getFreekiwikiConfiguration().setActivelink(activelink.toString());
                    break;
                default:
                    System.out.println("Your selection is out of range.");
                    break;
            }
        }
        try {
            if (fcp == null) fcp = (new FCPConnectionFactory(configuration.getFreekiwikiConfiguration())).getFCPConnectionInstance();
            System.out.println("Connection to node successfully established");
        } catch (IOException e) {
            System.err.println("Connection creation failed");
            System.exit(1);
        }
        String activelinkId = null;
        if (activelink != null) {
            try {
                activelinkId = fcp.insertFileDirect(activelink);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        Database db = new Database(configuration.getDatabaseConfiguration());
        if (db == null) {
            System.err.println("Error opening the Database");
            return;
        }
        ArrayList<WikiArticle> wiki = db.readArticles(startFrom, totalToInsert);
        org.freenet.freekiwiki.xml.ObjectFactory freekiwikiFactory = new org.freenet.freekiwiki.xml.ObjectFactory();
        Freekiwiki freekiwiki = freekiwikiFactory.createFreekiwiki();
        freekiwiki.setName(configuration.getFreekiwikiConfiguration().getName());
        freekiwiki.setAnnounceport(BigInteger.valueOf(1));
        int count = 0;
        ArrayList<String> insertIds = new ArrayList<String>();
        ArrayList<Long> insertOldIds = new ArrayList<Long>();
        ArrayList<String> insertTitles = new ArrayList<String>();
        boolean redundant;
        for (WikiArticle current : wiki) {
            count++;
            redundant = false;
            for (String title : insertTitles) {
                if (current.getTitle().equals(title)) {
                    redundant = true;
                    break;
                }
            }
            if (redundant) {
                System.out.println("" + count + " Article " + current.getTitle() + " was already put into the queue, ignoring (please report this)");
                continue;
            }
            insertIds.add(fcp.insertArticleData(configuration.getFreekiwikiConfiguration().getName(), current.getTitle(), ((GenericWikiArticle) current).getWikiText(), 0));
            insertOldIds.add(Long.valueOf(current.getOldid()));
            insertTitles.add(current.getTitle());
            System.out.println("" + count + " Added article to the queue (wiki): " + current.getTitle());
            current = null;
        }
        String uri = null;
        Article currentArticle = null;
        for (int i = 0; i < insertIds.size(); i++) {
            uri = fcp.waitToComplete(insertIds.get(i));
            while (uri == null) {
                ++count;
                insertIds.add(fcp.insertArticleData(configuration.getFreekiwikiConfiguration().getName(), wiki.get(i).getTitle(), ((GenericWikiArticle) wiki.get(i)).getWikiText(), count));
                insertOldIds.add(Long.valueOf(wiki.get(i).getOldid()));
                insertTitles.add(wiki.get(i).getTitle());
                System.out.println("" + count + " Readded article to the queue (wiki): " + wiki.get(i).getTitle());
                uri = fcp.waitToComplete(insertIds.get(i));
            }
            System.out.println(uri + " has completed and was removed from the queue");
            currentArticle = freekiwikiFactory.createArticle();
            currentArticle.setOldid(insertOldIds.get(i));
            currentArticle.setTitle(insertTitles.get(i));
            currentArticle.setKey(Helper.standardiseKey(uri));
            freekiwiki.getArticle().add(currentArticle);
        }
        if (activelinkId != null) {
            activelinkId = fcp.waitToComplete(activelinkId);
            freekiwiki.setActivelink(activelinkId);
            configuration.getFreekiwikiConfiguration().setActivelink(activelinkId);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            marshaller.marshal(freekiwiki, os);
        } catch (JAXBException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        String fIX = fcp.insertFix(freekiwiki.getName(), configuration.getFreekiwikiConfiguration().getPrivateKey(), os.toByteArray(), activelinkId);
        fcp.waitToComplete(fIX);
        System.out.println("COMPLETE!");
        System.out.println("Please tell the users that the name is: " + SafeName.decodeUTF8(freekiwiki.getName()) + " and the key is:\n" + configuration.getFreekiwikiConfiguration().getPublicKey());
        ConfigIO.saveParam(configuration, marshaller, null);
    }
}
