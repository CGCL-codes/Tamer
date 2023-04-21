package org.argouml.notation.providers.uml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.argouml.model.Model;
import org.argouml.notation.providers.NodeInstanceNotation;

/**
 * Notation for a NodeInstance.
 * 
 * @author Michiel
 */
public class NodeInstanceNotationUml extends NodeInstanceNotation {

    /**
     * The constructor.
     *
     * @param nodeInstance the UML nodeInstance
     */
    public NodeInstanceNotationUml(Object nodeInstance) {
        super(nodeInstance);
    }

    /**
     * Parse a line of the form: "name : base-node". 
     * <p>
     * The base-node part is a comma separated list of Nodes. <p>
     * 
     * Note that stereotypes are not supported.
     *
     * {@inheritDoc}
     */
    public void parse(Object modelElement, String text) {
        String s = text.trim();
        if (s.length() == 0) {
            return;
        }
        if (s.charAt(s.length() - 1) == ';') {
            s = s.substring(0, s.length() - 2);
        }
        String name = "";
        String bases = "";
        StringTokenizer tokenizer = null;
        if (s.indexOf(":", 0) > -1) {
            name = s.substring(0, s.indexOf(":")).trim();
            bases = s.substring(s.indexOf(":") + 1).trim();
        } else {
            name = s;
        }
        tokenizer = new StringTokenizer(bases, ",");
        List<Object> classifiers = new ArrayList<Object>();
        Object ns = Model.getFacade().getNamespace(modelElement);
        if (ns != null) {
            while (tokenizer.hasMoreElements()) {
                String newBase = tokenizer.nextToken();
                Object cls = Model.getFacade().lookupIn(ns, newBase.trim());
                if (cls != null) {
                    classifiers.add(cls);
                }
            }
        }
        Model.getCommonBehaviorHelper().setClassifiers(modelElement, classifiers);
        Model.getCoreHelper().setName(modelElement, name);
    }

    public String getParsingHelp() {
        return "parsing.help.fig-nodeinstance";
    }

    public String toString(Object modelElement, Map args) {
        String nameStr = "";
        if (Model.getFacade().getName(modelElement) != null) {
            nameStr = Model.getFacade().getName(modelElement).trim();
        }
        StringBuilder baseStr = new StringBuilder();
        Collection col = Model.getFacade().getClassifiers(modelElement);
        if (col != null && col.size() > 0) {
            Iterator it = col.iterator();
            baseStr.append(Model.getFacade().getName(it.next()));
            while (it.hasNext()) {
                baseStr.append(", " + Model.getFacade().getName(it.next()));
            }
        }
        if ((nameStr.length() == 0) && (baseStr.length() == 0)) {
            return "";
        }
        String base = baseStr.toString().trim();
        if (base.length() < 1) {
            return nameStr.trim();
        }
        return nameStr.trim() + " : " + base;
    }
}
