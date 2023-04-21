package org.dllearner.kb.sparql.datastructure;

import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.dllearner.kb.sparql.Manipulator;
import org.dllearner.kb.sparql.TypedSparqlQueryInterface;
import org.dllearner.utilities.StringTuple;

/**
 * A node in the graph that is an instance.
 * 
 * @author Sebastian Hellmann
 * 
 */
public class InstanceNode extends Node {

    Set<ClassNode> classes = new HashSet<ClassNode>();

    Set<StringTuple> datatypes = new HashSet<StringTuple>();

    Set<PropertyNode> properties = new HashSet<PropertyNode>();

    public InstanceNode(URI u) {
        super(u);
    }

    @Override
    public Vector<Node> expand(TypedSparqlQueryInterface tsq, Manipulator m) {
        Set<StringTuple> s = tsq.getTupelForResource(uri);
        m.check(s, this);
        Vector<Node> Nodes = new Vector<Node>();
        Iterator<StringTuple> it = s.iterator();
        while (it.hasNext()) {
            StringTuple t = (StringTuple) it.next();
            try {
                if (t.a.equals(m.type)) {
                    ClassNode tmp = new ClassNode(new URI(t.b));
                    classes.add(tmp);
                    Nodes.add(tmp);
                } else {
                    InstanceNode tmp = new InstanceNode(new URI(t.b));
                    properties.add(new PropertyNode(new URI(t.a), this, tmp));
                    Nodes.add(tmp);
                }
            } catch (Exception e) {
                System.out.println("Problem with: " + t);
                e.printStackTrace();
            }
        }
        expanded = true;
        return Nodes;
    }

    @Override
    public void expandProperties(TypedSparqlQueryInterface tsq, Manipulator m) {
        for (PropertyNode one : properties) {
            one.expandProperties(tsq, m);
        }
    }

    @Override
    public Set<String> toNTriple() {
        Set<String> s = new HashSet<String>();
        s.add("<" + uri + "><" + rdftype + "><" + thing + ">.");
        for (ClassNode one : classes) {
            s.add("<" + uri + "><" + rdftype + "><" + one.getURI() + ">.");
            s.addAll(one.toNTriple());
        }
        for (PropertyNode one : properties) {
            s.add("<" + uri + "><" + one.getURI() + "><" + one.getB().getURI() + ">.");
            s.addAll(one.toNTriple());
            s.addAll(one.getB().toNTriple());
        }
        return s;
    }

    @Override
    public int compareTo(Node n) {
        return super.compareTo(n);
    }
}
