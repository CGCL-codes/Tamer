package edu.yale.csgp.vitapad.gui.components;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractListModel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import edu.yale.csgp.vitapad.VPUtilities;
import edu.yale.csgp.vitapad.VitaPad;
import edu.yale.csgp.vitapad.util.logging.ILogger;
import edu.yale.csgp.vitapad.util.logging.LoggerController;

/**
 * Maintains a list of strings for history of such things as searches.
 * The histories are stored in a very basic xml file called
 * history.xml. This is essentially copied from jEdit except he uses a
 * flat file instead of the xml.
 * 
 * 
 * @author Matt Holford
 * 
 */
public class HistoryModel extends AbstractListModel {

    private static ILogger _log = LoggerController.createLogger(HistoryModel.class);

    private static File history;

    private static Hashtable models;

    private String name;

    private static int max;

    private Vector data;

    private static boolean modified;

    private static long historyModTime;

    /**
     * Create a new history model. Not called explicitly, but
     * instantiated statically through HistoryModel.getModel()
     * 
     * @param name
     *            The name of the model.
     */
    public HistoryModel(String name) {
        this.name = name;
        data = new Vector(max);
    }

    public int getSize() {
        return data.size();
    }

    public Object getElementAt(int index) {
        return getItem(index);
    }

    String getItem(int index) {
        return (String) data.elementAt(index);
    }

    /**
     * Add an item to the end of the history list and trim extras if
     * necessary.
     * 
     * @param text
     *            The String to add
     */
    public void addItem(String text) {
        if (text == null || text.length() == 0) return;
        modified = true;
        int index = data.indexOf(text);
        if (index != -1) data.removeElementAt(index);
        data.insertElementAt(text, 0);
        while (getSize() > max) data.removeElementAt(data.size() - 1);
    }

    /**
     * Remove all entries from the model.
     */
    public void clear() {
        modified = true;
        data.removeAllElements();
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the given model. If it doesn't exist creates a new one
     * with that name
     * 
     * @param name
     *            The name of the model
     * 
     */
    public static HistoryModel getModel(String name) {
        if (models == null) models = new Hashtable();
        HistoryModel model = (HistoryModel) models.get(name);
        if (model == null) {
            model = new HistoryModel(name);
            models.put(name, model);
        }
        return model;
    }

    /**
     * Load whatever history is out there in the history.xml file.
     * This is called at startup time. We open this as a file rather
     * than as a resource because it doesn't exist when you first run
     * the program. We use dom4j to parse the xml.
     * 
     */
    public static void loadHistory() {
        String settingsDir = VitaPad.getSettingsDir();
        if (settingsDir == null) return;
        history = new File(VPUtilities.constructPath(settingsDir, "history.xml"));
        if (!history.exists()) return;
        historyModTime = history.lastModified();
        _log.info("Loading history");
        if (models == null) models = new Hashtable();
        SAXReader sr = new SAXReader();
        Document doc = null;
        try {
            doc = sr.read(history);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = doc.getRootElement();
        List histories = root.elements("History");
        Iterator iter = histories.iterator();
        while (iter.hasNext()) {
            Element child = (Element) iter.next();
            HistoryModel current = new HistoryModel(child.attributeValue("Name"));
            List entries = child.elements("Entry");
            Iterator iter2 = entries.iterator();
            while (iter2.hasNext()) {
                Element entry = (Element) iter2.next();
                current.data.addElement(entry.getText());
            }
            if (current != null) models.put(current.getName(), current);
        }
    }

    /**
     * The only thing here that is effected by a property is the max
     * number of entries. We set a default of 25.
     */
    public static void propertiesChanged() {
        max = VitaPad.getIntegerProperty("history.max", 25);
    }

    /**
     * Save whatever histories are in local memory to the
     * "history.xml" file. We use dom4j. This gets called at shutdown.
     * 
     */
    public static void saveHistory() {
        if (!modified) return;
        _log.info("Saving histories");
        history = new File(VPUtilities.constructPath(VitaPad.getSettingsDir(), "history.xml"));
        if (history.exists()) VitaPad.backupSettingsFile(history); else {
            try {
                history.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("Histories");
        if (models != null) {
            Enumeration modelEnum = models.elements();
            while (modelEnum.hasMoreElements()) {
                HistoryModel model = (HistoryModel) modelEnum.nextElement();
                if (model.getSize() == 0) continue;
                Element history = root.addElement("History");
                history.addAttribute("Name", model.getName());
                for (int i = 0; i < model.getSize(); i++) {
                    Element entry = history.addElement("Entry");
                    entry.setText(model.getItem(i));
                }
            }
        }
        try {
            XMLWriter output = new XMLWriter(new FileWriter(history), new OutputFormat("   ", true));
            output.write(document);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
