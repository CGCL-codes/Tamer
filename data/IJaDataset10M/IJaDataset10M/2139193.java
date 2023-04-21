package org.marcont.portal.ontologyeditor.tree.factory.ont;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import org.marcont.portal.ontologyeditor.ontology.Ontology;
import org.marcont.portal.ontologyeditor.tree.TreeException;
import org.marcont.portal.ontologyeditor.tree.factory.TreeNodeOntResource;

/**
 *
 * @author micha�
 */
public class TreeNodeOntClass extends TreeNodeOntResource {

    /** Creates a new instance of TreeNodeOntClass */
    public TreeNodeOntClass(Ontology ont, OntClass data) throws TreeException {
        super(ont, data);
    }

    public void checkData(Resource data) {
        if (data == null) {
            this.nameSpace = OWL.Thing.getNameSpace();
            this.name = OWL.Thing.getLocalName();
            return;
        }
        super.checkData(data);
    }

    public OntClass getData() {
        if (data == null) {
            return ont.getModel().getOntClass(OWL.Thing.getURI());
        }
        return (OntClass) super.getData().as(OntClass.class);
    }

    public void createOrGetChildren() throws TreeException {
        OntClass data = (OntClass) this.getData();
        if (data != null) {
            for (ExtendedIterator it = data.listSubClasses(true); it.hasNext(); ) {
                OntClass sub = (OntClass) it.next();
                this.addChildAlph(createOrGet(ont, sub));
            }
        }
    }

    public String getLink() {
        return super.getLink() + "&activeTab=0";
    }
}
