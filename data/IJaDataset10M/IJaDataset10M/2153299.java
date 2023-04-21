package org.marcont.portal.ontologyeditor.ontology;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;
import com.hp.hpl.jena.vocabulary.OWL;
import java.util.ArrayList;
import org.marcont.portal.ontologyeditor.tree.TreeModel;
import org.marcont.portal.ontologyeditor.tree.TreeException;
import org.marcont.portal.ontologyeditor.tree.TreeNodeFactory;
import org.marcont.portal.ontologyeditor.tree.factory.TreeNodeResource;
import org.marcont.portal.ontologyeditor.tree.factory.ont.TreeNodeIndividual;
import org.marcont.portal.ontologyeditor.tree.factory.ont.TreeNodeOntClass;
import org.marcont.portal.ontologyeditor.tree.factory.ont.TreeNodeOntProperty;

/**
 *
 * @author Michal Wozniak
 */
public class Ontology {

    public IdentityManager im;

    public OntModel model;

    private Filter anonFilter = new AnonFilter();

    private TreeNodeOntClass classRoot = null;

    private TreeNodeOntProperty propertyRoot = null;

    private TreeModel propertyTm;

    private TreeModel classTm;

    public TreeNodeIndividual activeIndividual;

    private Integer selectedTab = new Integer(0);

    public static final OntProperty propertyRootResource = null;

    public static final OntClass classRootResource = null;

    private NameSpaces nsc = null;

    public Ontology() {
    }

    public void loadModel(OntModel model) {
        this.setModel(model);
        if (model != null) {
            this.resetTree();
        } else {
            this.classRoot = null;
            this.propertyRoot = null;
            this.nsc = null;
        }
    }

    public void resetTree() {
        im = new IdentityManager();
        this.classRoot = null;
        this.propertyRoot = null;
        this.nsc = null;
        lazyLoad();
    }

    private void lazyLoadNamespaces() {
        nsc = NameSpaces.getInstance(this);
    }

    private void lazyLoad() {
        getClasses();
        getProperties();
        removeRedundandHierarchyRoot(this.getClassRoot());
        removeRedundandHierarchyRoot(this.getPropertyRoot());
    }

    public OntModel getModel() {
        return model;
    }

    public class AnonFilter extends Filter {

        /**
         * Returns true if given object is a resource that is not anonymous
         *
         * @param obj
         *            object to check if is anonymous
         * @return true if given object is not anonymous
         */
        public boolean accept(Object obj) {
            if (obj instanceof Resource) {
                Resource tmp = (Resource) obj;
                return !tmp.isAnon();
            } else {
                return false;
            }
        }
    }

    public void getProperties() {
        try {
            propertyRoot = new TreeNodeOntProperty(this, null);
            im.putUri("Property#Thing", propertyRoot);
            propertyTm = new TreeModel(propertyRoot);
            propertyTm.setNode(propertyRoot);
            if (nsc == null) {
                lazyLoadNamespaces();
            }
            for (ExtendedIterator it = model.listOntProperties(); it.hasNext(); ) {
                makePropertiesTree(propertyRoot, (RDFNode) it.next());
            }
            for (ExtendedIterator it = model.listObjectProperties(); it.hasNext(); ) {
                makePropertiesTree(propertyRoot, (RDFNode) it.next());
            }
            for (ExtendedIterator it = model.listDatatypeProperties(); it.hasNext(); ) {
                makePropertiesTree(propertyRoot, (RDFNode) it.next());
            }
        } catch (TreeException t) {
        }
    }

    private void makePropertiesTree(TreeNodeFactory parent, RDFNode r) throws TreeException {
        if (nsc == null) {
            lazyLoadNamespaces();
        }
        nsc.addNameSpace(((Resource) r).getNameSpace());
        String type = "abstract";
        if (r.canAs(OntResource.class) && ((OntResource) r.as(OntResource.class)).isOntLanguageTerm()) {
            return;
        }
        TreeNodeFactory tnf = TreeNodeFactory.createOrGet(this, r);
        parent.addChildAlph(tnf);
        if (r.canAs(OntProperty.class)) {
            for (ExtendedIterator it = ((OntProperty) r.as(OntProperty.class)).listSubProperties(true); it.hasNext(); ) {
                RDFNode cr = (RDFNode) it.next();
                makePropertiesTree(tnf, cr);
            }
        }
    }

    public void getClasses() {
        if (nsc == null) {
            lazyLoadNamespaces();
        }
        try {
            classRoot = new TreeNodeOntClass(this, classRootResource);
            im.putUri(OWL.Thing.getURI(), classRoot);
            classTm = new TreeModel(classRoot);
            classTm.setNode(classRoot);
            for (ExtendedIterator it = model.listHierarchyRootClasses().filterKeep(anonFilter); it.hasNext(); ) {
                makeClassesTree(classRoot, (RDFNode) it.next());
            }
            for (ExtendedIterator it = model.listNamedClasses(); it.hasNext(); ) {
                makeClassesTree(classRoot, (RDFNode) it.next());
            }
        } catch (TreeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void makeClassesTree(TreeNodeFactory parent, RDFNode r) throws TreeException {
        if (nsc == null) {
            lazyLoadNamespaces();
        }
        nsc.addNameSpace(((Resource) r).getNameSpace());
        TreeNodeFactory tnf = TreeNodeFactory.createOrGet(this, r);
        if (!tnf.getName().equals("Thing")) {
            parent.addChildAlph(tnf);
            if (r.canAs(OntClass.class)) {
                for (ExtendedIterator it = ((OntClass) r.as(OntClass.class)).listSubClasses(true); it.hasNext(); ) {
                    RDFNode cr = (RDFNode) it.next();
                    makeClassesTree(tnf, cr);
                }
            }
        }
    }

    private void removeRedundandHierarchyRoot(TreeNodeResource root) {
        ArrayList<TreeNodeFactory> toBeRemoved = new ArrayList<TreeNodeFactory>();
        for (TreeNodeFactory hierarchyRoot : root.getChildren()) {
            if (hierarchyRoot.getParentCount() > 1) {
                toBeRemoved.add(hierarchyRoot);
            }
        }
        for (TreeNodeFactory hierarchyRoot : toBeRemoved) {
            root.removeChild(hierarchyRoot);
        }
    }

    public void setModel(OntModel model) {
        this.model = model;
    }

    public TreeNodeOntClass getClassRoot() {
        if (classRoot == null) {
            lazyLoad();
        }
        return classRoot;
    }

    public void setClassRoot(TreeNodeOntClass classRoot) {
        this.classRoot = classRoot;
    }

    public TreeNodeOntProperty getPropertyRoot() {
        if (propertyRoot == null) {
            lazyLoad();
        }
        return propertyRoot;
    }

    public void setPropertyRoot(TreeNodeOntProperty propertyRoot) {
        this.propertyRoot = propertyRoot;
    }

    public TreeModel getPropertyTm() {
        return propertyTm;
    }

    public TreeNodeOntProperty getPropertyNode() {
        return (TreeNodeOntProperty) propertyTm.getNode();
    }

    public TreeNodeOntClass getClassNode() {
        return (TreeNodeOntClass) classTm.getNode();
    }

    public void setPropertyTm(TreeModel propertyTm) {
        this.propertyTm = propertyTm;
    }

    public TreeModel getClassTm() {
        return classTm;
    }

    public void setClassTm(TreeModel classTm) {
        this.classTm = classTm;
    }

    public Integer getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(Integer selectedTab) {
        this.selectedTab = selectedTab;
    }
}
