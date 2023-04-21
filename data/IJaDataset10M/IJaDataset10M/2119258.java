package edu.toronto.cs.telos;

import java.util.ArrayList;
import java.util.Set;
import jtelos.Attribute;
import jtelos.Individual;
import jtelos.KB;
import jtelos.Proposition;
import jtelos.PropositionOrPrimitive;
import util.D;

/**
 * @author Yijun Yu
 */
public class TelosParserIndividual implements Individual {

    public String cls;

    public String id;

    public java.util.ArrayList types;

    public java.util.ArrayList parents;

    public java.util.HashMap attributes;

    /**
	 * 
	 * @uml.property name="telosKB"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    public TelosParserKB telosKB;

    public TelosParserIndividual(String c, String i, java.util.ArrayList t, java.util.ArrayList p, java.util.HashMap a) {
        cls = c;
        id = i;
        types = t;
        parents = p;
        attributes = a;
    }

    /**
	 * Return the individual's KB.
	 */
    public KB kb() {
        return telosKB;
    }

    public Proposition from() {
        return null;
    }

    /**
	 * Return the individual's name.
	 *  
	 */
    public String label() {
        return telosName();
    }

    public PropositionOrPrimitive to() {
        return null;
    }

    /**
	 * Return the individual's class level 
	 * 
	 * @return int 
	 *           the class level
	 */
    public int level() {
        if (this.cls.equals("Token")) return 0;
        if (this.cls.equals("SimpleClass")) return 1;
        if (this.cls.equals("MetaClass")) return 2;
        if (this.cls.equals("MetaMetaClass")) return 3;
        if (this.cls.equals("MetaMetaMetaClass")) return 4; else return 0;
    }

    /**
	 * Tell if an individual is built in 
	 * 
	 * @return boolean 
	 *             true is the individual is built in; otherwise,return false
	 * 
	 * @author Xiao Xue Deng
	 */
    public boolean isBuiltin() {
        if (this.telosName().startsWith("String") || this.telosName().startsWith("Real") || this.telosName().startsWith("Integer") || this.telosName().startsWith("M3Class") || this.telosName().startsWith("M2Class") || this.telosName().startsWith("M1Class") || this.telosName().startsWith("SClass") || this.telosName().startsWith("IndividualClass") || this.telosName().startsWith("Class") || this.telosName().startsWith("Individual") || this.telosName().startsWith("Proposition") || this.telosName().startsWith("TelosParserIndividual") || this.telosName().startsWith("OmegaClass")) return true;
        return false;
    }

    /**
	 * Direct instances of an individual. 
	 * 
	 * @return Proposition [] 
	 *              the direct instances of the individual
	 * 
	 * @author Xiao Xue Deng
	 */
    public Proposition[] directInstances() {
        Individual[] elements = telosKB.individuals();
        TelosParserIndividual[] telosIndivs = new TelosParserIndividual[elements.length];
        for (int i = 0; i < elements.length; i++) telosIndivs[i] = (TelosParserIndividual) elements[i];
        ArrayList instanceInstances = new ArrayList();
        for (int i = 0; i < telosIndivs.length; i++) {
            TelosParserIndividual instance = telosIndivs[i];
            if (instance == null) continue;
            if (instance.types == null) continue;
            for (int j = 0; j < instance.types.size(); j++) {
                String ancestor = (String) ((instance.types).get(j));
                if (ancestor.equals(this.telosName())) {
                    instanceInstances.add(instance);
                }
            }
        }
        return (TelosParserIndividual[]) instanceInstances.toArray(new TelosParserIndividual[instanceInstances.size()]);
    }

    /**
	 * Direct ancestors of an individual. 
	 * 
	 * @return Proposition [] 
	 *              the direct ancestors of the individual
	 * 
	 * @author Xiao Xue Deng
	 */
    public Proposition[] directAncestors() {
        ArrayList ancestorInstances = new ArrayList();
        if (types == null) return null;
        for (int i = 0; i < this.types.size(); i++) {
            String anc = (String) ((this.types).get(i));
            if (telosKB == null) {
                D.o("No KB");
                return null;
            }
            if (telosKB.individuals == null || telosKB.individuals.size() == 0) {
                return null;
            }
            Individual ancestor = telosKB.individual(anc);
            if (ancestor != null) {
                ancestorInstances.add(ancestor);
            }
        }
        return (TelosParserIndividual[]) ancestorInstances.toArray(new TelosParserIndividual[ancestorInstances.size()]);
    }

    /**
	 * Direct children of an individual.
	 * 
	 * @return Proposition [] 
	 *         the direct children of the individual
	 * 
	 * @author Xiao Xue Deng
	 */
    public Proposition[] directChildren() {
        Individual[] elements = telosKB.individuals();
        TelosParserIndividual[] telosIndivs = new TelosParserIndividual[elements.length];
        for (int i = 0; i < elements.length; i++) telosIndivs[i] = (TelosParserIndividual) elements[i];
        ArrayList childInstances = new ArrayList();
        for (int i = 0; i < telosIndivs.length; i++) {
            TelosParserIndividual instance = telosIndivs[i];
            if (instance == null) continue;
            if (instance.parents == null) continue;
            for (int j = 0; j < instance.parents.size(); j++) {
                String parent = (String) ((instance.parents).get(j));
                if (parent.equals(this.telosName())) {
                    childInstances.add(instance);
                }
            }
        }
        return (TelosParserIndividual[]) childInstances.toArray(new TelosParserIndividual[childInstances.size()]);
    }

    /**
	 * Direct parents of an individual.
	 * 
	 * @return Proposition [] 
	 *            the direct parents of the individual
	 * 
	 * @author Xiao Xue Deng
	 */
    public Proposition[] directParents() {
        ArrayList parentInstances = new ArrayList();
        if (parents == null) {
            return null;
        }
        for (int i = 0; i < this.parents.size(); i++) {
            String par = (String) ((this.parents).get(i));
            if (telosKB == null) {
                D.o("No KB");
                return null;
            }
            if (telosKB.individuals == null || telosKB.individuals.size() == 0) {
                return null;
            }
            Individual parent = telosKB.individual(par);
            if (parent != null) {
                parentInstances.add(parent);
            }
        }
        return (TelosParserIndividual[]) parentInstances.toArray(new TelosParserIndividual[parentInstances.size()]);
    }

    /**
	 * Direct and indirect Instances of an individual.
	 * 
	 * @return Proposition [] 
	 *              all instances of the individual, no matter direct or indirect ones
	 * 
	 * @author Xiao Xue Deng
	 */
    public Proposition[] allInstances() {
        ArrayList instances = new ArrayList();
        Individual[] individuals = telosKB.individuals();
        for (int i = 0; i < individuals.length; i++) {
            if (this.isAncestorOf(individuals[i])) {
                instances.add(individuals[i]);
            }
        }
        return (TelosParserIndividual[]) instances.toArray(new TelosParserIndividual[instances.size()]);
    }

    /**
	 * Direct and indirect ancestors of an individual.
	 * 
	 * @return Proposition [] 
	 *               all ancestors of the individual, no matter direct or indirect ones
	 * 
	 * @author Xiao Xue Deng
	 */
    public Proposition[] allAncestors() {
        ArrayList ancestors = new ArrayList();
        Individual[] individuals = telosKB.individuals();
        for (int i = 0; i < individuals.length; i++) {
            if (this.isInstanceOf(individuals[i])) {
                ancestors.add(individuals[i]);
            }
        }
        return (TelosParserIndividual[]) ancestors.toArray(new TelosParserIndividual[ancestors.size()]);
    }

    /**
	 * Direct and indirect children of an individual.
	 * 
	 * @return Proposition [] 
	 *             all children of the individual, no matter direct or indirect ones
	 * 
	 * @author Xiao Xue Deng
	 */
    public Proposition[] allChildren() {
        ArrayList children = new ArrayList();
        Individual[] individuals = telosKB.individuals();
        for (int i = 0; i < individuals.length; i++) {
            if (this.isParentOf(individuals[i])) {
                children.add(individuals[i]);
            }
        }
        return (TelosParserIndividual[]) children.toArray(new TelosParserIndividual[children.size()]);
    }

    /**
	 * Direct and indirect parents of an individual.
	 * 
	 * @return Proposition [] 
	 *             all parents of the individual, no matter direct or indirect ones
	 * 
	 * @author Xiao Xue Deng
	 */
    public Proposition[] allParents() {
        ArrayList parents = new ArrayList();
        Individual[] individuals = telosKB.individuals();
        for (int i = 0; i < individuals.length; i++) {
            if (this.isChildOf(individuals[i])) {
                parents.add(individuals[i]);
            }
        }
        return (TelosParserIndividual[]) parents.toArray(new TelosParserIndividual[parents.size()]);
    }

    public String[] categories() {
        return null;
    }

    /**
	 * Direct and indirect attributes of an individual.
	 * 
	 * @return Attribute [] 
	 *             all attributes of the individual, no matter direct or indirect ones
	 * 
	 * @author Xiao Xue Deng
	 */
    public Attribute[] attributes() {
        return attributes(null, null);
    }

    /**
	 * Direct and indirect attributes whose name matches "argo" and value matches
	 * "arg1" .
	 * 
	 * @return Attribute [] 
	 *             all attributes of the individual, no matter direct or indirect ones which meet the match
	 * 
	 *  @author Xiao Xue Deng
	 */
    public Attribute[] attributes(String[] arg0, String arg1) {
        ArrayList attrs = new ArrayList();
        if (parents == null) {
            return directAttributes(arg0, arg1);
        } else {
            Attribute[] childAttrs = directAttributes(arg0, arg1);
            if (childAttrs != null) {
                for (int h = 0; h < childAttrs.length; h++) {
                    TelosAttribute ca = (TelosAttribute) childAttrs[h];
                    attrs.add(ca);
                }
            }
            Proposition[] allParents = this.allParents();
            boolean overwrite = false;
            if (allParents != null) {
                for (int i = 0; i < allParents.length; i++) {
                    TelosParserIndividual parent = (TelosParserIndividual) allParents[i];
                    if (parent.attributes != null) {
                        Attribute[] parentAttrs = parent.directAttributes(arg0, arg1);
                        for (int j = 0; j < parentAttrs.length; j++) {
                            TelosAttribute pa = (TelosAttribute) parentAttrs[j];
                            if (childAttrs != null) {
                                for (int h = 0; h < childAttrs.length; h++) {
                                    TelosAttribute ca = (TelosAttribute) childAttrs[h];
                                    if ((pa.attributeTelosName).equals(ca.attributeTelosName) && (pa.label).equals(ca.label)) {
                                        overwrite = true;
                                    }
                                }
                            }
                            if (overwrite == false) attrs.add(pa);
                        }
                    }
                }
            }
            return (TelosAttribute[]) attrs.toArray(new TelosAttribute[attrs.size()]);
        }
    }

    /**
	 * Direct attributes of an individual.
	 * 
	 * @return Attribute []
	 *               all direct attributes of the individual
	 * 
	 * @author Xiao Xue Deng
	 */
    public Attribute[] directAttributes() {
        return directAttributes(null, null);
    }

    /**
	 * Direct attributes of an individual.
	 * 
	 * @return Attribute []
	 *               all direct attributes of the individual
	 * 
	 * @author Yijun Yu
	 */
    public Attribute[] directAttributes(String name) {
        String cat[] = new String[1];
        cat[0] = name;
        return directAttributes(cat, name);
    }

    /**
	 * Direct attributes whose name matches "argo" and value matches "arg1".
	 * 
	 * @return Attribute [] 
	 *                  all direct attributes of the individual meet the match
	 * 
	 * @author Xiao Xue Deng
	 */
    public Attribute[] directAttributes(String[] arg0, String arg1) {
        if (attributes == null) {
            return null;
        }
        String requiredType = null;
        String requiredLabel = null;
        if (arg0 == null) {
            requiredType = null;
        }
        if (arg1 == null) {
            requiredLabel = null;
        }
        if (arg0 != null) {
            requiredType = arg0[0];
        }
        if (arg1 != null) {
            requiredLabel = arg1;
        }
        int shift = 0;
        if (requiredType != null && requiredType.equals("attribute")) {
            requiredType = requiredLabel;
            requiredLabel = "";
            shift = 1;
        }
        ArrayList attrs = new ArrayList();
        Set keys = attributes.keySet();
        Object[] aKeys = keys.toArray();
        int size1 = attributes.size();
        for (int i = 0; i < size1; i++) {
            ArrayList key = (ArrayList) aKeys[i];
            while (((String) (key.get(key.size() - 1))).equals("")) {
                key.remove(key.size() - 1);
            }
            int h = 0;
            String currentType = null;
            String currentLabel = null;
            int namePos = 0;
            for (int j = 0; j < key.size(); j++) {
                String key1 = (String) key.get(j);
                if (!(key1.equals("attribute")) && h == 0) {
                    currentType = key1;
                    namePos = j;
                    h++;
                    continue;
                }
                if (!(key1.equals("attribute")) && h == 1) {
                    currentLabel = key1;
                    boolean name = currentLabel.equals("imagename") || currentLabel.equals("name");
                    if (currentLabel.equals(currentType) && (j - namePos) == 1 && name) {
                        currentLabel = "";
                    }
                    if (currentLabel.equals(currentType) && (j - namePos) != 1) {
                        currentLabel = "";
                    }
                }
            }
            int index = -1;
            if (currentType != null) index = currentType.indexOf("-"); else System.out.println("warning: currenttype is null" + key);
            if (index != -1) {
                currentType = currentType.substring(0, index);
            }
            if (currentLabel == null) {
                currentLabel = "";
            }
            boolean type = false;
            if (requiredType == null) {
                type = true;
            } else type = currentType.equals(requiredType);
            boolean label = false;
            if (requiredLabel == null || currentLabel.equals("")) label = true; else label = currentLabel.equals(requiredLabel);
            if (type && label) {
                Object t = attributes.get(key);
                String value = t.toString();
                ArrayList newKey = key;
                if (shift == 1) {
                    TelosAttribute attr = new TelosAttribute(currentType, arg0, arg1, value, telosKB);
                    attrs.add(attr);
                } else if (currentLabel.equals("")) {
                    Object[] cate = newKey.toArray();
                    String[] cateS = new String[cate.length];
                    for (int k = 0; k < cate.length; k++) {
                        cateS[k] = (String) cate[k];
                    }
                    TelosAttribute attr = new TelosAttribute(currentType, cateS, currentLabel, value, telosKB);
                    attrs.add(attr);
                } else {
                    Object[] cate = newKey.toArray();
                    String[] cateS = new String[cate.length - 1];
                    for (int k = 0; k < cate.length - 1; k++) {
                        cateS[k] = (String) cate[k];
                    }
                    TelosAttribute attr = new TelosAttribute(currentType, cateS, currentLabel, value, telosKB);
                    attrs.add(attr);
                }
            } else {
            }
        }
        return (TelosAttribute[]) attrs.toArray(new TelosAttribute[attrs.size()]);
    }

    public Attribute[] inheritedAttributes() {
        return null;
    }

    public Attribute[] inheritedAttributes(String[] arg0, String arg1) {
        return null;
    }

    public Attribute[] referencedBy() {
        return null;
    }

    /**
	 * Tell if an individual is an ancestor of arg0. 
	 * 
	 * @return boolean 
	 *           true is the individual is an ancestor; otherwise,return false
	 * 
	 * @author Xiao Xue Deng
	 */
    public boolean isAncestorOf(Proposition arg0) {
        return arg0.isInstanceOf(this);
    }

    /**
	 * Tell if an individual is a child of arg0.
	 * 
	 * @return boolean 
	 *             true is the individual is a child; otherwise,return false
	 * 
	 * @author Xiao Xue Deng
	 */
    public boolean isChildOf(Proposition arg0) {
        if (this.parents != null) {
            for (int i = 0; i < this.parents.size(); i++) {
                String parent = (String) ((this.parents).get(i));
                if (parent.equals(arg0.telosName())) {
                    return true;
                }
            }
            for (int i = 0; i < this.parents.size(); i++) {
                String parent = (String) ((this.parents).get(i));
                TelosParserIndividual indParent = (TelosParserIndividual) telosKB.individual(parent);
                indParent.telosKB = telosKB;
                if (indParent.isChildOf(arg0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Tell if an individual is an instance of arg0.
	 * 
	 * @return boolean 
	 *              true is the individual is an instance; otherwise,return false
	 * 
	 * @author Xiao Xue Deng
	 */
    public boolean isInstanceOf(Proposition arg0) {
        if (this.types != null) {
            for (int i = 0; i < this.types.size(); i++) {
                String ancestor = (String) ((this.types).get(i));
                if (ancestor.equals(arg0.telosName())) {
                    return true;
                }
            }
            for (int i = 0; i < this.types.size(); i++) {
                String ancestor = (String) ((this.types).get(i));
                TelosParserIndividual indAncestor = (TelosParserIndividual) telosKB.individual(ancestor);
                if (indAncestor == null) continue;
                indAncestor.telosKB = telosKB;
                if (indAncestor.isInstanceOf(arg0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Tell if an individual is a parent of arg0.
	 * 
	 * @return boolean 
	 *             true is the individual is a parent; otherwise,return false
	 * 
	 * @author Xiao Xue Deng
	 */
    public boolean isParentOf(Proposition arg0) {
        return arg0.isChildOf(this);
    }

    /**
	 * Add "arg0" to the individual's direct ancestors list.
	 */
    public void addDirectAncestors(Proposition[] arg0) {
        if (arg0 == null || arg0[0] == null) return;
        for (int i = 0; i < arg0.length; i++) {
            types.add(arg0[i].telosName());
        }
    }

    /**
	 * Add "arg0" to the individual's direct parents list.
	 */
    public void addDirectParents(Proposition[] arg0) {
        for (int i = 0; i < arg0.length; i++) {
            parents.add(arg0[i].telosName());
        }
    }

    /**
	 * Remove "arg0" from the individual's direct ancestors list.
	 */
    public void removeDirectAncestor(Proposition arg0) {
        types.remove(arg0.telosName());
    }

    /**
	 * Remove "arg0" from the individual's direct parents list.
	 */
    public void removeDirectParent(Proposition arg0) {
        parents.remove(arg0.telosName());
    }

    /**
	 * Remove "arg0" from the individual's direct attributes list.
	 */
    public void removeDirectAttr(Attribute arg0) {
        TelosAttribute attr = (TelosAttribute) arg0;
        ArrayList key = new ArrayList();
        String[] cate = attr.categories;
        if ((attr.label).equals("")) {
            for (int i = 0; i < cate.length; i++) {
                key.add(cate[i]);
            }
        } else {
            for (int i = 0; i < cate.length; i++) {
                key.add(cate[i]);
            }
            key.add(attr.label);
        }
        if (attributes.containsKey(key)) {
            attributes.remove(key);
        }
    }

    /**
	 * Return the individual's teloaName.
	 */
    public String telosName() {
        return id;
    }

    public String toString() {
        return id;
    }
}
