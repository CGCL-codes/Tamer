package nl.dgl.rgb.treemodel;

import java.util.List;
import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * Presents all statements of a property.
 * @author David G. Lindeijer.
 */
public class PropertiesTreeModel extends OsgsTreeModel {

    public PropertiesTreeModel(Model jenaModel) {
        super("properties");
        createChildren(jenaModel);
    }

    public void createChildren(Model jenaModel) {
        List<Property> properties = getAllProperties(jenaModel);
        for (Property property : properties) {
            PropertyTreeModel propertyTreeModel = PropertyTreeModel.getInstance(property, jenaModel);
            this.osgsChildren.add(propertyTreeModel);
        }
    }

    private static List<Property> getAllProperties(Model jenaModel) {
        StmtIterator allStatements_iter = jenaModel.listStatements();
        List<Property> allProperties = new Vector<Property>();
        while (allStatements_iter.hasNext()) {
            Property property = allStatements_iter.nextStatement().getPredicate();
            if (allProperties.contains(property)) {
                continue;
            }
            allProperties.add(property);
        }
        return allProperties;
    }
}
