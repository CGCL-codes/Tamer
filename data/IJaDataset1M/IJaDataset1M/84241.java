package com.agentfactory.afapl2.compiler;

import com.agentfactory.afapl2.compiler.parser.node.EnumNode;
import com.agentfactory.afapl2.compiler.parser.node.FunctionNode;
import com.agentfactory.afapl2.compiler.parser.node.OntologyNode;
import com.agentfactory.afapl2.compiler.parser.node.PredicateNode;
import com.agentfactory.afapl2.compiler.parser.node.TypeDefNode;
import com.agentfactory.logic.lang.FOS;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author  Administrator
 */
public class Ontology {

    private List<Predicate> predicates;

    private List<Function> functions;

    private List<Enumeration> enumerations;

    private List<TypeDefinition> types;

    private String name;

    public Ontology(String name) {
        this.name = name;
        predicates = new LinkedList<Predicate>();
        functions = new LinkedList<Function>();
        enumerations = new LinkedList<Enumeration>();
        types = new LinkedList<TypeDefinition>();
    }

    public Ontology(OntologyNode node) {
        this(node.getIdentifier());
        for (EnumNode eNode : node.getEnumerations()) {
            enumerations.add(new Enumeration(eNode));
        }
        for (TypeDefNode tNode : node.getTypes()) {
            types.add(new TypeDefinition(tNode));
        }
        for (FunctionNode fNode : node.getFunctions()) {
            functions.add(new Function(fNode));
        }
        for (PredicateNode tNode : node.getPredicates()) {
            predicates.add(new Predicate(tNode));
        }
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public boolean hasFOS(FOS fos) {
        boolean found = false;
        Predicate term = null;
        int i = 0;
        while (i < predicates.size() && !found) {
            term = predicates.get(i++);
        }
        return found;
    }

    public String getName() {
        return name;
    }

    public Predicate getPredicate(int index) {
        return predicates.get(index);
    }
}
