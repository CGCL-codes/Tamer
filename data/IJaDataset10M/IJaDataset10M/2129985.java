package owl2prefuse.tree.rdf;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import owl2prefuse.Constants;
import owl2prefuse.tree.Tree;
import prefuse.data.Node;

/**
 * This class converts the given RDF Model to a Prefuse tree datastructure.
 * <p/>
 * Project OWL2Prefuse <br/>
 * RDFTreeConverter.java created 14 june 2007, 14:07
 * <p/>
 * Copyright &copy 2007 Jethro Borsje
 * 
 * @author <a href="mailto:info@jborsje.nl">Jethro Borsje</a>
 * @version $$Revision:$$, $$Date:$$
 */
public class RDFTreeConverter {

    /**
     * The Jena Model which needs to be converted to a Prefuse tree.
     */
    private Model m_model;

    /**
     * The Prefuse tree.
     */
    private Tree m_tree;

    /**
     * Creates a new instance of RDF.
     * @param p_model The Jena model that needs to be converted.
     */
    public RDFTreeConverter(Model p_model) {
        m_model = p_model;
        createTree();
    }

    /**
     * Return the created Prefuse tree.
     * @return The created Prefuse tree.
     */
    public Tree getTree() {
        return m_tree;
    }

    /**
     * Create the Prefuse tree. This method creates an empty tree and adds the 
     * appropriate columns to it. After that it gets the root class (o2p:dummyRoot) 
     * of the RDF data set and recursively starts building the tree from there.
     * This method is automatically called from the constructors of this converter.
     */
    private void createTree() {
        m_tree = new Tree(Constants.TREE_TYPE_RDF);
        m_tree.addColumn("URI", String.class);
        m_tree.addColumn("name", String.class);
        m_tree.addColumn("type", String.class);
        Resource rootResource = m_model.getResource(Constants.DUMMY_ROOT_URI);
        buildTree(null, rootResource);
    }

    /**
     * Build the Prefuse tree, this method is called recursively.
     * @param p_parent The parent node of the resource that is being added to the graph.
     * @param p_currentClass The resource which is being added to the graph.
     */
    private void buildTree(Node p_parent, RDFNode p_currentRDFNode) {
        Node currNode = null;
        if (p_parent == null) currNode = m_tree.addRoot(); else currNode = m_tree.addChild(p_parent);
        if (p_currentRDFNode.isResource()) {
            Resource resource = (Resource) p_currentRDFNode;
            if (p_currentRDFNode.isURIResource()) {
                currNode.setString("URI", resource.getURI());
                currNode.setString("name", resource.getURI());
                currNode.setString("type", "resource");
            } else if (p_currentRDFNode.isAnon()) {
                currNode.setString("URI", resource.getId().getLabelString());
                currNode.setString("name", resource.getId().getLabelString());
                currNode.setString("type", "resource");
            }
            StmtIterator itProperties = resource.listProperties();
            while (itProperties.hasNext()) {
                Statement prop = itProperties.nextStatement();
                buildTree(currNode, prop.getObject());
            }
        } else if (p_currentRDFNode.isLiteral()) {
            Literal literal = (Literal) p_currentRDFNode;
            currNode.setString("URI", literal.getString());
            currNode.setString("name", literal.getLexicalForm());
            currNode.setString("type", "literal");
        }
    }
}
