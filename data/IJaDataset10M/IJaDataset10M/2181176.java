package diuf.diva.hephaistk.xml.smuiml;

import diuf.diva.hephaistk.Constants;
import diuf.diva.hephaistk.xml.Attribute;
import diuf.diva.hephaistk.xml.HephaisTKParsingException;
import diuf.diva.hephaistk.xml.XmlGeneric;

public class Trigger extends Smuiml_Generic {

    /**
	 *
	 */
    private static final long serialVersionUID = 5002043803933345634L;

    public static final String NAME = "trigger";

    private Attribute attName = null;

    private Source source = null;

    public Trigger() {
        super();
        setName(NAME);
    }

    /**
	 * @return the attName
	 */
    public Attribute getAttName() {
        return attName;
    }

    /**
	 * @param attName the attName to set
	 */
    public void setAttName(String attName) {
        this.attName = new Attribute("name", attName);
    }

    /**
	 * @return the source
	 */
    public Source getSource() {
        return source;
    }

    /**
	 * @param source the source to set
	 */
    public void setSource(Source source) {
        this.source = source;
    }

    public void addElement(XmlGeneric element) throws HephaisTKParsingException {
        if (element instanceof Source) {
            setSource((Source) element);
        } else {
            throw new HephaisTKParsingException(getNamespace() + "." + NAME + " -- unexpected element: " + element.getName() + "only <" + Source.NAME + "> allowed");
        }
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
        if (name.equalsIgnoreCase("name")) {
            setAttName(value);
        } else {
            throw new HephaisTKParsingException(getNamespace() + "." + NAME + " -- unexpected attribute: " + name);
        }
    }

    public String toString(int indent) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            ret.append(' ');
        }
        ret.append("<" + NAME + (attName != null ? " name=\"" + attName.getValue() + "\"" : "") + ">\n");
        if (source != null) {
            ret.append(source.toString(indent + Constants.INDENT_PREFIX));
        }
        for (int i = 0; i < indent; i++) {
            ret.append(' ');
        }
        ret.append("</" + NAME + ">\n");
        return ret.toString();
    }
}
