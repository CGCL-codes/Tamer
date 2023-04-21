package nl.dgl.rgb.treemodel;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

public class ResourceSuperClassesTreeModel extends OsgsTreeModel {

    public ResourceSuperClassesTreeModel(Resource resource, Model jenaModel) {
        super(resource);
        this.createChildrenList(jenaModel);
    }

    public String toString() {
        return RDFS.subClassOf.toString();
    }

    private void createChildrenList(Model jenaModel) {
        Resource resource = (Resource) nodeModel;
        StmtIterator statements = jenaModel.listStatements(resource, RDFS.subClassOf, (RDFNode) null);
        while (statements.hasNext()) {
            Statement subClassOfStatement = statements.nextStatement();
            Resource superClass = (Resource) subClassOfStatement.getObject();
            if (superClass.equals(resource)) {
                continue;
            }
            if (superClass.equals(RDFS.Resource)) {
                continue;
            }
            this.osgsChildren.add(ResourceTreeModel.getInstance(superClass, jenaModel));
        }
    }
}
