package edu.toronto.cs.telos;

import jtelos.Attribute;
import jtelos.PropositionOrPrimitive;

/**
 * @author Xiao Xue Deng
 */
public class TelosAttribute extends TelosParserIndividual implements Attribute {

    public String attributeTelosName;

    public String[] categories;

    public String label;

    public String value;

    /**
	 * 
	 * @uml.property name="telosKB"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    public TelosParserKB telosKB;

    public TelosAttribute(String name, String[] cate, String lab, String val, TelosParserKB kb) {
        super(null, null, null, null, null);
        attributeTelosName = name;
        categories = cate;
        label = lab;
        value = val;
        telosKB = kb;
    }

    public String label() {
        return label;
    }

    public String[] categories() {
        return categories;
    }

    /** 
     * Get the value of the attribute.
     * 
     * @return PropositionOrPrimitive
     *               the value of the attribute. It could be a primitive, such as a string, an integer or a real
     *               number. It could also be a proposition.
     */
    public PropositionOrPrimitive to() {
        String v = value;
        if (v.startsWith("Element") || v.startsWith("Link") || v.startsWith("SerializedViewObject")) {
            TelosParserKB kb = this.telosKB;
            TelosParserIndividual ind = (TelosParserIndividual) kb.individual(v);
            return ind;
        }
        TelosParserKB kb = this.telosKB;
        TelosParserIndividual ind = (TelosParserIndividual) kb.individual(v);
        if (ind != null) {
            return ind;
        }
        int size = v.length();
        try {
            Integer.parseInt(v);
        } catch (NumberFormatException nfe) {
            try {
                Double.parseDouble(v);
            } catch (NumberFormatException nfe2) {
                if (size > 1 && (v.substring(0, 1)).equals("\"")) return (new TelosString(v.substring(1, size - 1))); else return (new TelosString(v.substring(0, size)));
            }
            return (new TelosReal(v));
        }
        return (new TelosInteger(v));
    }
}
