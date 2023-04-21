package org.mandarax.xpath.treeModelExtensions;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.IteratorUtils;
import org.mandarax.kernel.ClauseSet;
import org.mandarax.kernel.ClauseSetException;
import org.mandarax.kernel.Fact;
import org.mandarax.kernel.KnowledgeBase;
import org.mandarax.kernel.Predicate;
import org.mandarax.kernel.Prerequisite;
import org.mandarax.kernel.Rule;
import org.mandarax.kernel.SimplePredicate;
import org.mandarax.kernel.Term;
import org.mandarax.lib.AbstractPredicate;
import org.mandarax.xpath.MandaraxTreeModel;
import org.mandarax.xpath.MandaraxTreeModelExtension;
import org.mandarax.xpath.Path;
import org.mandarax.xpath.PathImpl;
import org.mandarax.xpath.XPathState;

/**
 * This class contains the rules for interpreting the basic objects within the
 * Mandarax tree model. Further extensions to this should decide whether they should
 * go into this class or become their own extension by implementing MandaraxTreeModelExtension.
 * 
 * @author Jonathan Giles
 * @version 0.2
 */
public class BaseTreeModelExtension implements MandaraxTreeModelExtension {

    public List<Path> getChildren(Path p) {
        if (p == null) return null;
        Object obj = p.getLast();
        ArrayList<Path> children = new ArrayList<Path>();
        if (obj instanceof Rule) {
            Rule rule = (Rule) obj;
            children.add(new PathImpl(p, rule.getHead()));
            children.add(new PathImpl(p, rule.getBody()));
        } else if (obj instanceof KnowledgeBase) {
            KnowledgeBase kb = (KnowledgeBase) obj;
            for (ClauseSet cs : kb.getClauseSets()) children.add(new PathImpl(kb, cs));
        } else if (obj instanceof Fact) {
            Fact fact = (Fact) obj;
            children.add(new PathImpl(p, fact.getPredicate()));
            for (int i = 0; i < fact.getTerms().length; ++i) children.add(new PathImpl(p, fact.getTerms()[i]));
        } else if (obj instanceof Predicate) {
            for (String s : ((Predicate) obj).getSlotNames()) children.add(new PathImpl(p, s));
        } else if (obj instanceof Term) {
            for (Term t : ((Term) obj).getAllSubterms()) children.add(new PathImpl(p, t));
        } else if (obj instanceof List) {
            List list = (List) obj;
            for (Object child : list) children.add(new PathImpl(p, child));
        }
        return children;
    }

    public String getElementName(Path p) {
        Object obj = p.getLast();
        Object parent = p.getParent().getLast();
        if (obj instanceof Rule) {
            return "rule";
        } else if (obj instanceof Predicate) {
            return "predicate";
        } else if (obj instanceof Prerequisite) {
            return "prerequisite";
        } else if (obj instanceof Fact) {
            if (parent instanceof Rule) {
                return "head";
            }
            return "fact";
        } else if (obj instanceof Term) {
            return "term";
        } else if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof KnowledgeBase) {
            return "";
        } else if (obj instanceof List) {
            if (parent instanceof Rule) {
                return "body";
            }
        }
        return null;
    }

    public Iterator getAttributeAxisIterator(Path p) {
        Object obj = p.getLast();
        HashMap<Object, String> attrNameMap = MandaraxTreeModel.getInstance().getAttributeNameMap();
        attrNameMap.put(obj.getClass(), "impltype");
        attrNameMap.put(obj.getClass().getInterfaces(), "type");
        if (obj instanceof Prerequisite) {
            Prerequisite prerequisite = (Prerequisite) obj;
            attrNameMap.put(prerequisite.getId(), "id");
            attrNameMap.put(new Attribute("naf", prerequisite.isNegatedAF()), "naf");
        } else if (obj instanceof Fact) {
            Fact fact = (Fact) obj;
            attrNameMap.put(fact.getId(), "id");
        } else if (obj instanceof Rule) {
            Rule rule = (Rule) obj;
            attrNameMap.put(rule.getId(), "id");
            attrNameMap.put(new Attribute("or", rule.isBodyOrConnected()), "or");
            List<Prerequisite> prerequisites = rule.getBody();
            attrNameMap.put(new Attribute("size", prerequisites.size()), "size");
            boolean hasNegatedPrerequisites = false;
            for (Prerequisite prerequisite : prerequisites) {
                if (prerequisite.isNegatedAF()) {
                    hasNegatedPrerequisites = true;
                    break;
                }
            }
            attrNameMap.put(new Attribute("hasNegatedPrerequisites", hasNegatedPrerequisites), "hasNegatedPrerequisites");
        } else if (obj instanceof ClauseSet) {
            ClauseSet cs = (ClauseSet) obj;
            attrNameMap.put(cs.getId(), "id");
            String propertyName;
            Enumeration propertyNames = cs.getProperties().propertyNames();
            while (propertyNames.hasMoreElements()) {
                propertyName = (String) propertyNames.nextElement();
                attrNameMap.put(new Attribute(propertyName, cs.getProperty(propertyName)), propertyName);
            }
        }
        return attrNameMap.keySet().iterator();
    }
}
