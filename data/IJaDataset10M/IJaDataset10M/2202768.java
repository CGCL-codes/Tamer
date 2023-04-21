package diuf.diva.hephaistk.xml.smuiml;

import diuf.diva.hephaistk.xml.Attribute;
import diuf.diva.hephaistk.xml.HephaisTKParsingException;
import diuf.diva.hephaistk.xml.XmlGeneric;

public class Source extends Smuiml_Generic {

    /**
	 *
	 */
    private static final long serialVersionUID = -113410600086957839L;

    public static final String NAME = "source";

    private Attribute attValue = null;

    private Attribute attModality = null;

    private Attribute attVariable = null;

    private Attribute attCondition = null;

    public Source() {
        super();
        setName(NAME);
    }

    /**
	 * @return the attModality
	 */
    public Attribute getAttModality() {
        return attModality;
    }

    /**
	 * @param attModality the attModality to set
	 */
    public void setAttModality(String modality) {
        this.attModality = new Attribute("modality", modality);
    }

    /**
	 * @return the attVariable
	 */
    public Attribute getAttVariable() {
        return attVariable;
    }

    /**
	 * @param attVariable the attVariable to set
	 */
    public void setAttVariable(String variable) {
        this.attVariable = new Attribute("variable", variable);
    }

    /**
	 * @return the attValue
	 */
    public Attribute getAttValue() {
        return attValue;
    }

    /**
	 * @param attValue the attValue to set
	 */
    public void setAttValue(String value) {
        this.attValue = new Attribute("value", value);
    }

    /**
	 * @return the attCondition
	 */
    public Attribute getAttCondition() {
        return attCondition;
    }

    /**
	 * @param attCondition the attCondition to set
	 */
    public void setAttCondition(String condition) {
        this.attCondition = new Attribute("condition", condition);
    }

    public void addElement(XmlGeneric element) throws HephaisTKParsingException {
        throw new HephaisTKParsingException(getNamespace() + "." + NAME + " -- unexpected element: " + element.getName());
    }

    @Override
    public void addAttribute(Attribute attribute) throws HephaisTKParsingException {
        super.addAttribute(attribute);
        String attName = attribute.getName();
        if (attName.indexOf(':') != -1) {
            attName = attName.substring(attName.indexOf(':') + 1);
        }
        addAttribute(attName, attribute.getValue());
    }

    public void addAttribute(String name, String value) throws HephaisTKParsingException {
        if (name.equalsIgnoreCase("value")) {
            setAttValue(value);
        } else if (name.equalsIgnoreCase("modality")) {
            setAttModality(value);
        } else if (name.equalsIgnoreCase("condition")) {
            setAttCondition(value);
        } else if (name.equalsIgnoreCase("variable")) {
            setAttVariable(value);
        } else {
            throw new HephaisTKParsingException(getNamespace() + "." + NAME + " -- unexpected attribute: " + name);
        }
    }

    public String toString(int indent) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            ret.append(' ');
        }
        ret.append("<" + NAME + (attModality != null ? " modality=\"" + attModality.getValue() + "\"" : "") + (attVariable != null ? " variable=\"" + attVariable.getValue() + "\"" : "") + (attValue != null ? " value=\"" + attValue.getValue() + "\"" : "") + (attCondition != null ? " condition=\"" + attCondition.getValue() + "\"" : "") + "/>\n");
        return ret.toString();
    }
}
