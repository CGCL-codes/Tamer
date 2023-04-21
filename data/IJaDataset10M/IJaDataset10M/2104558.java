package agentgui.core.ontologies;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Reads the public fields of an ontology base class, which extends 'jade.content.onto.Ontology'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyClassVocabulary extends Hashtable<String, String> implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 866517459374218362L;

    /**
	 * Initialise the class, which holds the complete vocabulary
	 * of the current ontology inside of a Hashtable.
	 *
	 * @param clazz the clazz
	 */
    public OntologyClassVocabulary(Class<?> clazz) {
        if (clazz == null) return;
        Hashtable<String, String> unsortedHash = new Hashtable<String, String>();
        Field[] publicFields = clazz.getFields();
        for (int i = 0; i < publicFields.length; i++) {
            String fieldName = publicFields[i].getName();
            String fieldValue = null;
            try {
                fieldValue = publicFields[i].get(clazz).toString();
                unsortedHash.put(fieldName, fieldValue);
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            }
        }
        this.putAll(unsortedHash);
        this.addAID_Vocabulary();
    }

    /**
	 * Because there is no class for the AID inside a single Ontology, some
	 * values related to this must be added manually.
	 */
    private void addAID_Vocabulary() {
        String key = "";
        String value = "";
        key = "AID";
        value = "aid";
        this.put(key, value);
        key = "AID_ADDRESSES";
        value = "addresses";
        this.put(key, value);
        key = "AID_RESOLVERS";
        value = "resolvers";
        this.put(key, value);
        key = "AID_NAME";
        value = "name";
        this.put(key, value);
    }

    /**
	 * Filter the Vocabulary for a special Ontology-Class, to get
	 * the "slots" / internal variables of the class.
	 *
	 * @param selectedNode the selected node
	 * @return The slots of the selected node / OntologyClassTreeObject
	 */
    public Hashtable<String, String> getSlots(DefaultMutableTreeNode selectedNode) {
        Object uObject = selectedNode.getUserObject();
        OntologyClassTreeObject octo = (OntologyClassTreeObject) uObject;
        return getSlots(octo.getOntologySubClass());
    }

    /**
	 * Filter the Vocabulary for a special Ontology-Class, to get
	 * the "slots" / internal variables of the class.
	 *
	 * @param ontologyClass the ontology class
	 * @return The slots of the selected node / OntologyClassTreeObject
	 */
    public Hashtable<String, String> getSlots(Class<?> ontologyClass) {
        String reference = ontologyClass.getName();
        String filter4Class = reference.substring(reference.lastIndexOf(".") + 1);
        return getSlots(filter4Class);
    }

    /**
	 * Filter the Vocabulary for a special Ontology-Class, to get
	 * the "slots" / internal variables of the class.
	 *
	 * @param filter4Class the filter4 class
	 * @return The slots of the selected node / OntologyClassTreeObject
	 */
    public Hashtable<String, String> getSlots(String filter4Class) {
        Set<String> set = this.keySet();
        Iterator<String> itr = set.iterator();
        Hashtable<String, String> resultHT = new Hashtable<String, String>();
        String Prefix = filter4Class.toUpperCase();
        while (itr.hasNext()) {
            String key = itr.next();
            if (key.startsWith(Prefix)) {
                boolean add2Result = false;
                if (key.length() == Prefix.length()) {
                    add2Result = false;
                } else {
                    if (key.startsWith(Prefix + "_")) {
                        add2Result = true;
                    }
                }
                if (add2Result == true) {
                    resultHT.put(key, this.get(key));
                }
            }
        }
        return resultHT;
    }
}
