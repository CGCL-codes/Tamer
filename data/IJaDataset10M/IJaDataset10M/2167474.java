package eu.popeye.middleware.pluginmanagement.plugin.file.xschema;

import java.util.ArrayList;
import java.util.Vector;
import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
import eu.popeye.middleware.pluginmanagement.plugin.file.SerializableCharmyFile;

/**
 * 
 * @author Ezio
 *
 */
public class SchemaForest {

    private Vector forest = null;

    /**
	 * identificatore dell'XML che gli XSchema vogliono estendere;
	 */
    private String idXMLModel = null;

    /**
	 * nome del file XML che gli XSchema vogliono estendere;
	 */
    private String nameXMLModel = null;

    /**
	 * schema entry radice ottenuto dal merge di tutti gli XSchema tree.
	 */
    private SchemaEntry entryRootMerged = null;

    public SchemaForest() {
        this.forest = new Vector();
    }

    public SchemaEntry insert(SerializableCharmyFile plugin, XSElementDeclaration xsElement, SchemaEntry schemaEntryParent) {
        if ((plugin == null) || (xsElement == null)) return null;
        SchemaEntry schemaEntry = new SchemaEntry(xsElement);
        if (schemaEntryParent != null) schemaEntryParent.appendChild(schemaEntry);
        schemaEntry.setParent(schemaEntryParent);
        schemaEntry.setPlugin(plugin);
        this.insert(schemaEntry, plugin);
        return schemaEntry;
    }

    public SchemaEntry[] getRoot(SerializableCharmyFile plugin) {
        if (plugin == null) return null;
        Vector tree = this.getTree(plugin);
        if (tree == null) return null;
        ArrayList result = new ArrayList();
        for (int j = 0; j < tree.size(); j++) {
            if (((SchemaEntry) tree.get(j)).getParent() == null) result.add(tree.get(j));
        }
        return (SchemaEntry[]) result.toArray(new SchemaEntry[result.size()]);
    }

    public Vector getTree(SerializableCharmyFile plugin) {
        for (int j = 0; j < this.forest.size(); j++) {
            if (((SerializableCharmyFile) ((Vector) forest.get(j)).get(0)).equals(plugin)) return (Vector) ((Vector) forest.get(j)).get(1);
        }
        return null;
    }

    private void insert(SchemaEntry schemaEntry, SerializableCharmyFile plugin) {
        if ((schemaEntry == null) || (plugin == null)) return;
        boolean bo = false;
        for (int j = 0; j < this.forest.size(); j++) {
            if (((SerializableCharmyFile) ((Vector) forest.get(j)).get(0)).equals(plugin)) {
                ((Vector) ((Vector) forest.get(j)).get(1)).add(schemaEntry);
                bo = true;
            }
        }
        if (!bo) {
            Vector data = new Vector(2);
            data.add(0, plugin);
            Vector tree = new Vector();
            tree.add(schemaEntry);
            data.add(1, tree);
            this.forest.add(data);
        }
    }

    public SchemaEntry getSchemaEntry(XSElementDeclaration xsElement) {
        if (xsElement == null) return null;
        for (int i = 0; i < this.forest.size(); i++) {
            Vector tree = (Vector) ((Vector) forest.get(i)).get(1);
            for (int j = 0; j < tree.size(); j++) {
                SchemaEntry currentEntry = (SchemaEntry) tree.get(j);
                if (currentEntry.getElementSource().equals(xsElement)) return currentEntry;
            }
        }
        return null;
    }

    public Vector getForest() {
        return forest;
    }

    public SerializableCharmyFile[] getPlugins() {
        ArrayList result = new ArrayList();
        for (int j = 0; j < this.forest.size(); j++) {
            result.add((SerializableCharmyFile) ((Vector) forest.get(j)).get(0));
        }
        return (SerializableCharmyFile[]) result.toArray(new SerializableCharmyFile[result.size()]);
    }

    public SchemaEntry getEntryRootMerged() {
        return entryRootMerged;
    }

    public void setEntryRootMerged(SchemaEntry entryRootMerged) {
        this.entryRootMerged = entryRootMerged;
    }

    public String getIdXMLModel() {
        return idXMLModel;
    }

    public void setIdXMLModel(String idXMLModel) {
        this.idXMLModel = idXMLModel;
    }

    public String getNameXMLModel() {
        return nameXMLModel;
    }

    public void setNameXMLModel(String nameXMLModel) {
        this.nameXMLModel = nameXMLModel;
    }
}
