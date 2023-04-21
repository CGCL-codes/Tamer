package org.marcont.portal.ontologyeditor.ontology;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.marcont.portal.ontologyeditor.tree.*;

/**
 *
 * @author katar
 */
public class Utils {

    public static ArrayList<TreeNodeFactory> getArrayResourcesFromIterator(Ontology ont, ExtendedIterator iter) throws TreeException {
        ArrayList<TreeNodeFactory> Array = new ArrayList<TreeNodeFactory>();
        if (iter == null) {
            return Array;
        }
        for (ExtendedIterator it = iter; it.hasNext(); ) {
            Array.add(TreeNodeFactory.createOrGet(ont, (RDFNode) it.next()));
        }
        return Array;
    }

    public static String implode(Collection c, String separator) {
        String output = "";
        for (Iterator it = c.iterator(); it.hasNext(); ) {
            Object o = it.next();
            output += o.toString();
            if (it.hasNext()) {
                output += separator;
            }
        }
        return output;
    }

    public static ArrayList<String> getValuesFromResources(ArrayList<TreeNodeFactory> resources) {
        ArrayList<String> output = new ArrayList<String>();
        for (TreeNodeFactory tnf : resources) {
            output.add(tnf.getValue());
        }
        return output;
    }

    public static ArrayList<NodeStatement> getArrayStatementsFromIterator(Ontology ont, StmtIterator iter) throws TreeException {
        ArrayList<NodeStatement> Array = new ArrayList<NodeStatement>();
        if (iter == null) {
            return Array;
        }
        for (StmtIterator it = iter; it.hasNext(); ) {
            Statement st = it.nextStatement();
            Array.add(new NodeStatement(TreeNodeFactory.createOrGetTreeNodeResource(ont, st.getSubject()), TreeNodeFactory.createOrGetTreeNodeResource(ont, st.getPredicate()), TreeNodeFactory.createOrGet(ont, st.getObject())));
        }
        return Array;
    }
}
