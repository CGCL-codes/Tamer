package org.fao.geonet.kernel.contact;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import jeeves.utils.Xml;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.util.ZipUtil;
import org.jdom.Element;

public class ContactManager {

    private Hashtable<String, Contact> directoryTable = null;

    private File rootDirectory = null;

    private String INITIAL = "ResponsiblePartyCatalogue.xml";

    private String XSL = "contact/conversion/contact.xsl";

    private Transformer transformer = null;

    /**
	 * 
	 * @param appPath
	 * @param contactDirectory
	 * @throws Exception
	 */
    public ContactManager(String appPath, String rootDirectory) throws Exception {
        File localDir = new File(rootDirectory);
        if (!localDir.isAbsolute()) this.rootDirectory = new File(appPath + localDir); else this.rootDirectory = new File(localDir.getAbsolutePath());
        XSL = new File(appPath, Geonet.Path.STYLESHEETS + File.separator + XSL).getAbsolutePath();
        initContactDirectoryTable();
    }

    /**
	 * @return Transformer for ISO contact
	 */
    public synchronized Transformer getTransformer() {
        Transformer newTransformer = null;
        if (transformer == null) {
            TransformerFactory factory = TransformerFactory.newInstance();
            Source src = new StreamSource(this.XSL);
            try {
                newTransformer = factory.newTransformer(src);
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }
        } else {
            transformer.clearParameters();
            newTransformer = transformer;
            this.transformer = null;
        }
        return newTransformer;
    }

    /**
	 * 
	 * @return
	 */
    public synchronized void releaseTransformer(Transformer instance) {
        if (transformer == null) {
            transformer = instance;
        }
    }

    /**
	 * @param fname
	 * @param type
	 * @param dname
	 * @return
	 */
    public String buildContactFilePath(String fname, String type, String dname) {
        return new File(rootDirectory + File.separator + type + File.separator + Geonet.CodeList.CONTACT + File.separator + dname, fname).getAbsolutePath();
    }

    /**
	 * 
	 * 
	 */
    private void initContactDirectoryTable() {
        directoryTable = new Hashtable<String, Contact>();
        if (rootDirectory.isDirectory()) {
            File externalDirectory = new File(rootDirectory, Geonet.CodeList.EXTERNAL + File.separator + Geonet.CodeList.CONTACT);
            if (externalDirectory.isDirectory()) {
                loadRepositories(externalDirectory, Geonet.CodeList.EXTERNAL);
            }
            File localDirectory = new File(rootDirectory, Geonet.CodeList.LOCAL + File.separator + Geonet.CodeList.CONTACT);
            if (localDirectory.isDirectory()) {
                loadRepositories(localDirectory, Geonet.CodeList.LOCAL);
            }
        }
    }

    /**
	 * 
	 * @param directory
	 * @param root local / external
	 */
    private void loadRepositories(File directory, String root) {
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        };
        String[] dataFile = directory.list(filter);
        for (int i = 0; i < dataFile.length; i++) {
            Contact gs = new Contact(dataFile[i], root, directory.getName(), new File(directory, dataFile[i]), this);
            try {
                addElement(gs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * 
	 * @param gs
	 */
    public void addElement(Contact c) throws Exception {
        String name = c.getKey();
        if (exists(name)) {
            throw new Exception("A directory with code " + name + "ever exists.");
        }
        try {
            directoryTable.put(name, c);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void remove(String name) {
        directoryTable.remove(name);
    }

    public File getDirectory() {
        return rootDirectory;
    }

    public Hashtable<String, Contact> getDirectoryTable() {
        return directoryTable;
    }

    public Contact getDirectoryByName(String name) {
        return directoryTable.get(name);
    }

    public Contact getLocalDirectory() throws Exception {
        Contact result = null;
        Contact resultlocal = null;
        for (Iterator iter = directoryTable.values().iterator(); iter.hasNext(); ) {
            result = (Contact) iter.next();
            if (result.getType().equals("local")) {
                resultlocal = result;
                break;
            }
        }
        if (resultlocal == null) {
            File initial = new File(rootDirectory, INITIAL);
            File localDirectory = new File(rootDirectory, Geonet.CodeList.LOCAL + File.separator + Geonet.CodeList.CONTACT);
            File localFile = new File(localDirectory, INITIAL);
            try {
                ZipUtil.copyInputStream(new FileInputStream(initial), new BufferedOutputStream(new FileOutputStream(localFile)));
                resultlocal = new Contact(INITIAL, "local", "local", localFile, this);
                addElement(resultlocal);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultlocal;
    }

    /**
	 * @param name ID of contactDirectory File
	 * @return true if contactDirectory exists
	 */
    public boolean exists(String name) {
        return (directoryTable.get(name) != null);
    }

    /**
	 *  Get selected element in XML repository from IDS 
	 */
    public Element getSelection(Set<String> selection) {
        Element response = new Element("ResponsiblePartyList");
        for (Iterator iter = selection.iterator(); iter.hasNext(); ) {
            String ID = (String) iter.next();
            if (ID != null) {
                String id = Contact.getID(ID);
                String ref = Contact.getREF(ID);
                Contact cd = getDirectoryByName(ref);
                Element result = cd.listElements(null, null, id, null, null);
                response.addContent(result.cloneContent());
            }
        }
        return response;
    }

    /**
	 *  Get selected and remove them
	 */
    public void deleteSelection(Set<String> selection) {
        for (Iterator iter = selection.iterator(); iter.hasNext(); ) {
            String ID = (String) iter.next();
            if (ID != null) {
                String id = Contact.getID(ID);
                String ref = Contact.getREF(ID);
                Contact cd = getDirectoryByName(ref);
                cd.deleteElement(ID);
            }
        }
    }
}
