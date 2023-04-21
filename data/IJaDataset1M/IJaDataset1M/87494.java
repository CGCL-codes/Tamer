package nl.dgl.rgb.treemodel;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class ResourceTypesTreeModel extends OsgsTreeModel {

    public ResourceTypesTreeModel(Object nodeMode, Model jenaModel) {
        super(nodeMode);
        this.createChildrenList(jenaModel);
    }

    public String toString() {
        return RDF.type.toString();
    }

    private void createChildrenList(Model jenaModel) {
        Resource resource = (Resource) nodeModel;
        StmtIterator statements = jenaModel.listStatements(resource, RDF.type, (RDFNode) null);
        while (statements.hasNext()) {
            Statement typeStatement = statements.nextStatement();
            Resource typeResource = (Resource) typeStatement.getObject();
            if (resource.equals(typeResource)) {
                continue;
            }
            if (typeResource.equals(RDFS.Resource)) {
                continue;
            }
            if (typeResource.equals(RDFS.Class)) {
                if (jenaModel.contains(resource, RDF.type, OWL.Class)) {
                    continue;
                }
            }
            if (typeResource.equals(RDF.Property)) {
                if (jenaModel.contains(resource, RDF.type, OWL.ObjectProperty)) {
                    continue;
                }
            }
            this.osgsChildren.add(ResourceTreeModel.getInstance(typeResource, jenaModel));
        }
    }
}
