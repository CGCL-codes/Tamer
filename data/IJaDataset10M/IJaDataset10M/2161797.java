package controler;

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import model.ApplicationSignature;
import model.Challenge;
import model.GenericChallenge;
import model.ChallengeContainedLinks;
import model.InvalidChallengeComparison;
import model.utils.Pair;
import view.ErrorMsgBox;
import view.WarssInputDialog;
import java.net.MalformedURLException;
import java.net.URL;
import model.ChallengeFaviconMd5;

/**
 * Signatures database manager.
 * Based on the singleton pattern design.
 * This class has more or less all its methods synchronized
 * because it's strongly solicited by scanning threads
 * to compare or store new signatures.
 */
public class SigManager {

    private static SigManager INSTANCE;

    private Element racine;

    private Document document;

    private final String filename = "SigBase.xml";

    private final String UNRECOGNIZED = "Not Recognized";

    private final double minProp = 0.4;

    /**
     * challenge performed and succeeded
     */
    public static final String SUCCEEDED = "challenge succeeded";

    /**
     * Constructor
     */
    private SigManager() {
        setRacine(new Element("signatures"));
        setDocument(new Document(getRacine()));
        this.readFile();
    }

    /**
     * Getter
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Getter
     * @return the current XML document root
     */
    public synchronized Element getRacine() {
        return racine;
    }

    /**
     * Setter
     * set the document's root
     * @param rac the new document's roots
     */
    public synchronized void setRacine(Element rac) {
        racine = rac;
    }

    /**
     * Getter
     * @return the current document
     */
    public synchronized Document getDocument() {
        return document;
    }

    /**
     * Setter
     * @param doc the new document
     */
    public synchronized void setDocument(Document doc) {
        document = doc;
    }

    /**
     * Ensure that only one instance of sigmanager
     * is running
     * @return INSTANCE of SigManager
     */
    public static synchronized SigManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SigManager();
        }
        return INSTANCE;
    }

    /**
     * Add the signature to the XML file
     * @param as Signature to save
     * @param name name of the signature
     * @param version version of the signature
     */
    public synchronized void addSig(ApplicationSignature as, String name, String version) {
        Element sigName = new Element("sig");
        boolean save = true;
        if (name != null && !name.equals("")) {
            sigName.setAttribute("name", name);
        } else {
            new ErrorMsgBox("Caution, Application name is null, signature will not be saved !");
            save = false;
        }
        if (version != null) {
            sigName.setAttribute("version", version);
        } else {
            sigName.setAttribute("version", "0");
        }
        if (save) {
            for (Pair<Challenge, Integer> challPair : as.getTests()) {
                Challenge chall = challPair.getA();
                Element eChall = new Element("challenge");
                eChall.setAttribute("name", chall.getName());
                eChall.setAttribute("state", chall.getState());
                eChall.setText(chall.getTestResults());
                sigName.addContent(eChall);
            }
            getRacine().addContent(sigName);
        }
    }

    /**
     *  Take an application Signature, and it compares with the database if it exists, 
     *  and give it name.
     *  
     * @param as Signature to find
     * @return Name of the Application
     */
    public synchronized Pair<String, Double> getAppBySig(ApplicationSignature as) {
        String lastAppSigClosest = "";
        double lastAppSigScore = 0;
        List allSigs = getRacine().getChildren();
        Iterator i = allSigs.iterator();
        while (i.hasNext()) {
            Element currentSig = (Element) i.next();
            List allChall = currentSig.getChildren();
            Iterator i2 = allChall.iterator();
            while (i2.hasNext()) {
                Element currentChall = (Element) i2.next();
                for (Pair<Challenge, Integer> challPair : as.getTests()) {
                    Challenge chall = challPair.getA();
                    if (chall.getName().equals(currentChall.getAttributeValue("name"))) {
                        if (chall.getTestResults() != null && currentChall.getText() != null && currentChall.getAttributeValue("state").equals(SUCCEEDED)) {
                            Challenge challTest = challFactory(currentChall.getAttributeValue("name"), null, currentChall.getAttributeValue("state"), currentChall.getText());
                            try {
                                if (chall.compareTo(challTest) > minProp) {
                                    lastAppSigClosest = currentSig.getAttributeValue("name");
                                    lastAppSigScore = chall.compareTo(challTest);
                                }
                            } catch (InvalidChallengeComparison e) {
                                new ErrorMsgBox("Internal error : \n" + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        if (lastAppSigScore > minProp) {
            return new Pair<String, Double>(lastAppSigClosest, new Double(lastAppSigScore));
        } else {
            return new Pair<String, Double>(UNRECOGNIZED, new Double(0));
        }
    }

    /**
     * Take the name of an application, and it searches it and if the application exists,
     * it constructs the ApplicationSignature corresponding
     * 
     * @param name : The name of the researched Signature
     * @return asRet : Application Signature of the recognized application, if it is
     */
    public synchronized Pair<ApplicationSignature, String> getSigByApp(String name) {
        ApplicationSignature asRet = new ApplicationSignature();
        String versionRet = new String();
        List allSigs = getRacine().getChildren();
        Iterator i = allSigs.iterator();
        while (i.hasNext()) {
            Element currentSig = (Element) i.next();
            if (currentSig.getAttributeValue("name").equals(name)) {
                System.out.println("[+] DEBUG : Trouve :" + currentSig.getAttributeValue("name"));
                versionRet = currentSig.getAttributeValue("version");
                List ChallVect = currentSig.getChildren();
                Iterator i2 = ChallVect.iterator();
                while (i2.hasNext()) {
                    Element currentChall = (Element) i2.next();
                    GenericChallenge genericChallenge = new GenericChallenge(currentChall.getAttributeValue("name"));
                    genericChallenge.setState(currentChall.getAttributeValue("state"));
                    genericChallenge.setTestResults(currentChall.getText());
                    asRet.addChallenge(genericChallenge, 0);
                }
            }
        }
        return new Pair<ApplicationSignature, String>(asRet, versionRet);
    }

    /**
     * Load the file in XML structure
     */
    public synchronized void readFile() {
        try {
            File sigFile = new File(this.filename);
            if (!sigFile.exists()) {
                sigFile.createNewFile();
                saveFile(filename);
            }
            SAXBuilder sxb = new SAXBuilder();
            setDocument(sxb.build(new File(this.filename)));
            setRacine(getDocument().getRootElement());
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (JDOMException je) {
            System.out.println("JDOMException");
        }
    }

    /**
     * Save the XML structure to the file
     * 
     * @param fileName the name of the signatures database
     */
    public synchronized void saveFile(String fileName) {
        try {
            XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            sortie.output(getDocument(), new FileOutputStream(fileName));
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }

    /**
     * Print the XML structure 
     */
    public String toString() {
        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        return (sortie.outputString(getDocument()));
    }

    /**
     *  Get the closest signature of an ApplicationSignature in the XML structure
     * 
     * @param appSig the reference application signature
     * @return Pair<String, String> : sigName, sigVersion
     */
    public synchronized Pair<String, String> getClosestSignature(ApplicationSignature appSig) {
        Pair<String, Double> retPair = getAppBySig(appSig);
        if (retPair.getB() > minProp) {
            System.out.println("[SigManager] :" + retPair.getA());
            Pair<ApplicationSignature, String> sigAndVersion = getSigByApp(retPair.getA());
            return new Pair<String, String>(retPair.getA(), sigAndVersion.getB());
        } else {
            return null;
        }
    }

    /**
     * Constructs the correct Challenge class with the challenge test 
     * 
     * @param name chall name
     * @param url challenge url
     * @param state challenge state
     * @param result challenge results
     * @return a new instance of the requested challenge or null
     * if the challenge was not recognized
     */
    public synchronized Challenge challFactory(String name, URL url, String state, String result) {
        if (name.equals("FaviconMd5")) {
            ChallengeFaviconMd5 chall = new ChallengeFaviconMd5(url);
            chall.setState(state);
            chall.setTestResults(result);
            return chall;
        } else if (name.equals("ChallengeContainedLinks")) {
            ChallengeContainedLinks chall = new ChallengeContainedLinks(url);
            chall.setState(state);
            chall.setTestResults(result);
            return chall;
        } else {
            return null;
        }
    }
}
