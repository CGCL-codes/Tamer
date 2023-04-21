package edu.toronto.cs.telos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import jtelos.Attribute;
import jtelos.Individual;
import jtelos.KB;
import jtelos.Levels;
import jtelos.Proposition;
import jtelos.PropositionOrPrimitive;
import util.D;

public class TelosParserKB implements KB {

    /**
	 * 
	 * @uml.property name="parser"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    static TelosParser parser;

    ArrayList individuals = null;

    Hashtable name2individual = new Hashtable();

    private String telosKBName;

    public TelosParserKB(String kbManager, String temp) {
        telosKBName = kbManager;
    }

    /**
	 * Return the name of the KB.
	 * 
	 * @return String 
	 *              the KB's name
	 */
    public String name() {
        return telosKBName;
    }

    /**
	 * Return the size of the KB.
	 * 
	 * @return int 
	 *             the number of individuals in the KB
	 */
    public int size() {
        return individuals.size();
    }

    public boolean containsOnlyBuiltIns() {
        return false;
    }

    /**
	 * Tell if the KB is empty.
	 */
    public boolean isEmpty() {
        return (individuals == null || individuals.size() == 0);
    }

    public Attribute[] attributes() {
        return null;
    }

    /**
	 * All the individuals in the KB.
	 * 
	 * @return Individual[] 
	 *               an array of all individuals in the KB
	 */
    public Individual[] individuals() {
        ArrayList L = new ArrayList();
        if (individuals == null) return null;
        if (individuals.size() == 0) return null;
        for (int i = 0; i < individuals.size(); i++) {
            TelosParserIndividual t = (TelosParserIndividual) individuals.get(i);
            t.telosKB = this;
            L.add(individuals.get(i));
        }
        return (Individual[]) L.toArray(new Individual[L.size()]);
    }

    public Proposition[] builtIns() {
        return null;
    }

    public Attribute attribute(String arg0) {
        return null;
    }

    /**
	 * Return an individual whose id equals "id" .
	 * @param id
	 *        the name of the individual we want to find
	 * 
	 * @return Individual 
	 *         the indivdual whose id is "id"
	 * 
	 * @author Xiao Xue Deng, Last Updates January 13, 2004
	 */
    public Individual individual(String id) {
        if (individuals == null || individuals.size() == 0) {
            return null;
        }
        TelosParserIndividual ind = (TelosParserIndividual) name2individual.get(id);
        if (ind != null) {
            String ID = ind.id;
            ind.telosKB = this;
            return ind;
        }
        for (int i = 0; i < individuals.size(); i++) {
            TelosParserIndividual t = (TelosParserIndividual) individuals.get(i);
            String tname = t.telosName();
            if (tname.equals(id)) {
                t.telosKB = this;
                return t;
            }
        }
        return null;
    }

    /**
	 * Add a new attribute whose telosName is "arg1", lable is "arg2", and values is
	 * "is_proposition" to the individual "ind".
	 * 
	 * @param id
	 *            the proposition which will have a new attribute
	 * @param arg1
	 *            the telosName of the new attribute
	 * @param arg2
	 *            the lable of the new attribute
	 * @param is_proposition
	 *            the value of the new attribute
	 * @return Attribute 
	 *             the new attribute
	 * 
	 * @author Xiao Xue Deng
	 */
    public Attribute newAttribute(Proposition ind, String[] arg1, String arg2, PropositionOrPrimitive is_proposition) {
        String name = ((TelosParserIndividual) ind).id;
        if (arg1[0].equals("attribute")) {
            String[] cate;
            TelosAttribute newAtt;
            ArrayList key;
            if (arg1.length > 1) {
                cate = new String[2];
                cate[0] = "attribute";
                cate[1] = arg1[1];
                newAtt = new TelosAttribute(arg1[0], cate, arg1[1], is_proposition.telosName(), this);
                key = new ArrayList();
                key.add(arg1[0]);
                key.add(arg1[1]);
            } else {
                cate = new String[1];
                cate[0] = "attribute";
                newAtt = new TelosAttribute(arg1[0], cate, arg2, is_proposition.telosName(), this);
                key = new ArrayList();
                key.add(arg1[0]);
                key.add(arg2);
            }
            (((TelosParserIndividual) ind).attributes).put(key, is_proposition.telosName());
            name2individual.remove(name);
            name2individual.put(name, (TelosParserIndividual) ind);
            return newAtt;
        } else {
            String[] cate = new String[2];
            cate[0] = "attribute";
            cate[1] = arg1[0];
            if (arg1[0].equals("children")) {
                Individual in = individual(is_proposition.telosName());
                if (in == null) newIndividual(is_proposition.telosName(), Levels.TOKEN_LEVEL);
            }
            TelosAttribute newAtt = new TelosAttribute(arg1[0], cate, arg2, is_proposition.telosName(), this);
            ArrayList key = new ArrayList();
            if (arg2.equals("") && arg1[0].equals("links")) {
                return newAtt;
            }
            if (arg2.equals("")) {
                key.add("attribute");
                if (arg1[0].equals("children")) {
                    key.add(arg1[0] + "-" + is_proposition.telosName());
                } else key.add(arg1[0]);
            } else {
                key.add("attribute");
                key.add(arg1[0]);
                key.add(arg2);
            }
            (((TelosParserIndividual) ind).attributes).put(key, is_proposition.telosName());
            name2individual.remove(name);
            name2individual.put(name, (TelosParserIndividual) ind);
            return newAtt;
        }
    }

    /**
	 * Add a new individua whose id is "arg0" and class level is "arg1" to
	 * the KB.
	 * 
	 * @param arg0
	 *            the telosName of the new individual
	 * @param arg1
	 *            the class level of the new individual
	 * 
	 * @return Individual 
	 *            the new individual
	 * 
	 * @author Xiao Xue Deng
	 */
    public Individual newIndividual(String arg0, int arg1) {
        TelosParserIndividual ind = new TelosParserIndividual(null, null, null, null, null);
        ind.telosKB = this;
        Individual indiv = this.individual(arg0);
        if (arg0.startsWith("SerializedView_0") && indiv != null) {
            this.removeAndDeleteProposition(indiv);
        }
        if (arg1 == 0 && arg0.startsWith("SerializedView_0")) {
            ind.cls = "Token";
            ind.id = arg0;
            ArrayList types = new ArrayList();
            types.add("SerializedView");
            types.add("Token");
            types.add("Individual");
            ind.types = types;
            HashMap attributes = new HashMap();
            int duplicateKey = 0;
            for (int i = 0; i < this.individuals().length; i++) {
                Individual indi = (TelosParserIndividual) ((this.individuals())[i]);
                if (indi.telosName().startsWith("SerializedViewObject")) {
                    ArrayList key = new ArrayList();
                    key.add("attribute");
                    key.add("view_objects");
                    if (attributes.containsKey(key)) {
                        duplicateKey++;
                        key.remove("view_objects");
                        key.add("view_objects-" + duplicateKey);
                    }
                    String value = indi.telosName();
                    attributes.put(key, value);
                }
            }
            ind.attributes = attributes;
            int index = 0;
            for (int i = 0; i < individuals.size(); i++) {
                if (((TelosParserIndividual) (individuals.get(i))).cls.equals("Token")) {
                    index++;
                }
            }
            individuals.add(index, ind);
            name2individual.put(ind.id, ind);
        }
        Individual individual = this.individual(arg0);
        if (arg0.startsWith("SerialzedViewObject") && individual != null) {
            this.removeAndDeleteProposition(individual);
        }
        if (arg1 == 0 && arg0.startsWith("SerializedViewObject")) {
            ind.cls = "Token";
            ind.id = arg0;
            ArrayList types = new ArrayList();
            types.add("SerializedObject");
            types.add("Token");
            types.add("Individual");
            ind.types = types;
            HashMap attributes = new HashMap(64);
            ind.attributes = attributes;
            individuals.add(0, ind);
            name2individual.put(ind.id, ind);
        }
        if (arg1 == 0 && arg0.startsWith("Element")) {
            ind.cls = "Token";
            ind.id = arg0;
            ArrayList types = new ArrayList();
            types.add("OMEElement");
            types.add("OMEElementClass");
            ind.types = types;
            HashMap attributes = new HashMap();
            ind.attributes = attributes;
            individuals.add(ind);
            name2individual.put(ind.id, ind);
        }
        if (arg1 == 0 && arg0.startsWith("Link")) {
            ind.cls = "Token";
            ind.id = arg0;
            ArrayList types = new ArrayList();
            types.add("OMELink");
            types.add("OMELinkClass");
            ind.types = types;
            HashMap attributes = new HashMap();
            ind.attributes = attributes;
            individuals.add(ind);
            name2individual.put(ind.id, ind);
        }
        return ind;
    }

    /**
	 * Remove arg0 from the KB
	 * 
	 * @param arg0
	 *            the individual to remove
	 */
    public void removeAndDeleteProposition(Proposition arg0) {
        int index = individuals.indexOf(arg0);
        if (index >= individuals.size() || index == -1) return;
        individuals.remove(index);
        individuals.trimToSize();
        name2individual.remove(((TelosParserIndividual) arg0).id);
    }

    public void load(String arg0) {
        D.o("load");
    }

    public void save() {
        D.o("save");
    }

    public void closeKB() {
        D.o("closeKB");
    }

    public void wipe() {
        D.o("wipe");
    }

    public void RETELL(String arg0, String arg1) {
        D.o("REtell");
    }

    public void fileRETELL(String arg0, String arg1) {
        D.o("gileRETell");
    }

    public void TELL(String arg0) {
        D.o("Tell");
    }

    /**
	 * Load  the telos file "arg0" to the KB.
	 * 
	 * @param arg
	 *           the telos file name 
	 */
    public void fileTELL(String arg0) {
        try {
            if (parser == null) parser = new TelosParser(new java.io.FileInputStream(arg0)); else TelosParser.ReInit(new java.io.FileInputStream(arg0));
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Telos Parser Version 0.1:  File " + arg0 + " not found.");
            return;
        }
        try {
            if (parser == null) return; else {
                individuals = TelosParser.Tokens();
                for (int i = 0; i < individuals.size(); i++) {
                    TelosParserIndividual ind = (TelosParserIndividual) individuals.get(i);
                    name2individual.put(ind.id, ind);
                }
            }
            System.out.println("Telos Parser Version 0.1:  Telos program parsed successfully from \n" + arg0 + ".");
        } catch (ParseException e) {
            System.out.println("Telos Parser Version 0.1:  Encountered errors during parse.");
            e.printStackTrace();
        }
    }

    public void UNTELL(String arg0) {
        D.o("Untell");
    }

    public void fileUNTELL(String arg0) {
        D.o("fileUNTell");
    }
}
