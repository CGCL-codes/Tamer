package jmri.jmrit.symbolicprog;

import jmri.jmrit.XmlFile;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import org.jdom.Element;

/**
 * Represents a set of standard names and aliases in memory.
 *<P>
 * This class doesn't provide tools for defining the names & aliases; that's done manually, or
 * at least not done here, to create the file.
 *<P>
 * Initially, we only need one of these, so we use an "instance" method to
 * locate the one associated with the "xml/names.xml" file.
 *
 * @author			Bob Jacobsen   Copyright (C) 2001
 * @version			$Revision: 1.8 $
 */
public class NameFile extends XmlFile {

    public Set<String> names() {
        return _nameHash.keySet();
    }

    protected Hashtable<String, Element> _nameHash = new Hashtable<String, Element>();

    public Element elementFromName(String name) {
        return _nameHash.get(name);
    }

    static NameFile _instance = null;

    public static synchronized NameFile instance() {
        if (_instance == null) {
            if (log.isDebugEnabled()) log.debug("NameFile creating instance");
            _instance = new NameFile();
            try {
                _instance.readFile(defaultNameFilename());
            } catch (Exception e) {
                log.error("Exception during name file reading: " + e);
            }
        }
        if (log.isDebugEnabled()) log.debug("NameFile returns instance " + _instance);
        return _instance;
    }

    /**
	 * Check to see if a name is present in the file
	 */
    public boolean checkName(String name) {
        return (elementFromName(name) != null);
    }

    /**
	 * Read the contents of a NameFile XML file into this object. Note that this does not
	 * clear any existing entries.
	 */
    void readFile(String name) throws org.jdom.JDOMException, java.io.IOException {
        if (log.isDebugEnabled()) log.debug("readFile " + name);
        Element root = rootFromName(name);
        readNames(root);
    }

    @SuppressWarnings("unchecked")
    void readNames(Element root) {
        List<Element> l = root.getChildren("definition");
        if (log.isDebugEnabled()) log.debug("readNames sees " + l.size() + " direct children");
        for (int i = 0; i < l.size(); i++) {
            Element el = l.get(i);
            storeDefinition(el);
        }
        l = root.getChildren("definitiongroup");
        if (log.isDebugEnabled()) log.debug("readNames sees " + l.size() + " groups");
        for (int i = 0; i < l.size(); i++) {
            Element el = l.get(i);
            readNames(el);
        }
    }

    void storeDefinition(Element el) {
        String name = el.getAttribute("item").getValue();
        _nameHash.put(name, el);
    }

    /**
	* Return the filename String for the default file, including location.
	* This is here to allow easy override in tests.
	*/
    protected static String defaultNameFilename() {
        return fileLocation + nameFileName;
    }

    static String fileLocation = "";

    static String nameFileName = "names.xml";

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NameFile.class.getName());
}
