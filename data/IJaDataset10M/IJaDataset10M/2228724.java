package edu.clemson.cs.r2jt.typereasoning;

import edu.clemson.cs.r2jt.absyn.Exp;
import edu.clemson.cs.r2jt.mathtype.*;
import java.util.*;

/**
 * Represents a directed graph of types, where edges between types
 * indicate a possible coercion that the type checker can perform.
 */
public class TypeGraph {

    public final MTType ENTITY = new MTProper(this, "Entity");

    public final MTProper MTYPE = new MTProper(this, null, true, "MType");

    public final MTType BOOLEAN = new MTProper(this, "B");

    public final MTType ATOM = new MTProper(this, "Atom");

    public final MTFunction POWERTYPE = new MTFunction(this, MTYPE, MTYPE, true);

    public final MTFunction UNION = new MTFunction(this, new MTCartesian(this, MTYPE, MTYPE), MTYPE);

    public final MTFunction INTERSECT = new MTFunction(this, new MTCartesian(this, MTYPE, MTYPE), MTYPE);

    public final MTFunction FUNCTION = new MTFunction(this, new MTCartesian(this, MTYPE, MTYPE), MTYPE);

    public final MTFunction AND = new MTFunction(this, new MTCartesian(this, BOOLEAN, BOOLEAN), BOOLEAN);

    public final MTFunction NOT = new MTFunction(this, BOOLEAN, BOOLEAN);

    HashMap<MTType, TypeNode> myTypeNodes;

    private static BindingCollection myCurrentBindings;

    public TypeGraph() {
        this.myTypeNodes = new HashMap<MTType, TypeNode>();
    }

    public boolean canCoerce(MTType sourceType, MTType targetType, Exp bindingExp) {
        boolean result = false;
        myCurrentBindings = new BindingCollection();
        List<TypeNode> sourceNodes = getTypeNodes(sourceType, false), destinationNodes = getTypeNodes(targetType, false);
        for (int i = 0; !result && i < sourceNodes.size(); ++i) {
            TypeNode sourceNode = sourceNodes.get(i);
            for (int j = 0; !result && j < destinationNodes.size(); ++j) {
                TypeNode destinationNode = destinationNodes.get(j);
                result = sourceNode.hasPathTo(destinationNode, bindingExp);
            }
        }
        myCurrentBindings = null;
        return result;
    }

    public List<TypeNode> getTypeNodes(MTType type, boolean createIfNotExist) {
        List<TypeNode> nodes = new ArrayList<TypeNode>();
        TypeNode node = myTypeNodes.get(type);
        if (node != null) {
            nodes.add(node);
        }
        Set<MTType> keys = myTypeNodes.keySet();
        Iterator<MTType> iter = keys.iterator();
        while (iter.hasNext()) {
            MTType compType = iter.next();
            if (type.equals(compType)) {
                node = myTypeNodes.get(compType);
                if (!nodes.contains(node)) {
                    nodes.add(node);
                }
            }
        }
        if (createIfNotExist && nodes.isEmpty()) {
            node = new TypeNode(type);
            myTypeNodes.put(type, node);
            nodes.add(node);
        }
        return nodes;
    }

    /**
	 * Add a relationship (edge) to the graph. The edge goes from
	 * the type of the binding expression to the type that is declared
	 * in the type theorem.
	 * @param relationship The data indicating the nature of the type relationship
	 */
    public void addRelationship(TypeRelationship relationship) {
        MTType sourceType = relationship.getSourceType();
        Iterator<TypeNode> nodes = this.getTypeNodes(sourceType, true).iterator();
        while (nodes.hasNext()) {
            nodes.next().addRelationship(relationship);
        }
    }

    public static void addBinding(String var1, String var2) {
        if (myCurrentBindings != null) {
            myCurrentBindings.addBinding(var1, var2);
        }
    }

    public static String getBinding(String var) {
        if (myCurrentBindings != null) {
            return myCurrentBindings.getBinding(var);
        }
        return var;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        Set<MTType> keys = myTypeNodes.keySet();
        Iterator<MTType> iter = keys.iterator();
        while (iter.hasNext()) {
            str.append(myTypeNodes.get(iter.next()).toString());
        }
        return str.toString();
    }
}
