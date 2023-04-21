package diuf.diva.hephaistk.xml.smuiml;

import java.util.Vector;
import diuf.diva.hephaistk.Constants;
import diuf.diva.hephaistk.xml.Attribute;
import diuf.diva.hephaistk.xml.HephaisTKParsingException;
import diuf.diva.hephaistk.xml.XmlGeneric;

public class Par_And extends FusionElement {

    /**
	 *
	 */
    private static final long serialVersionUID = -4758891215658766680L;

    public static final String NAME = "par_and";

    private Attribute attEvent = null;

    private Vector<Trigger> triggers = null;

    private Vector<FusionElement> fusionElements = null;

    public Par_And() {
        super();
        setName(NAME);
        triggers = new Vector<Trigger>();
        fusionElements = new Vector<FusionElement>();
    }

    public Vector<FusionElement> getFusionElements() {
        return fusionElements;
    }

    public FusionElement getFusionElement(int index) {
        return fusionElements.get(index);
    }

    public int fusionElementsSize() {
        return fusionElements.size();
    }

    public void addFusionElement(FusionElement fe) {
        fusionElements.add(fe);
    }

    /**
	 * @return the attAction
	 */
    public Attribute getAttEvent() {
        return attEvent;
    }

    /**
	 * @param attAction the attAction to set
	 */
    public void setAttEvent(String event) {
        this.attEvent = new Attribute("event", event);
    }

    public Vector<Trigger> getTriggers() {
        return triggers;
    }

    public Trigger getTrigger(int index) {
        return triggers.get(index);
    }

    public int triggersSize() {
        return triggers.size();
    }

    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    public void addElement(XmlGeneric element) throws HephaisTKParsingException {
        if (element instanceof Trigger) {
            addTrigger((Trigger) element);
        } else if (element instanceof FusionElement) {
            addFusionElement((FusionElement) element);
        } else {
            throw new HephaisTKParsingException(getNamespace() + "." + NAME + " -- unexpected element: " + element.getName());
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
        if (name.equalsIgnoreCase("event")) {
            setAttEvent(value);
        } else if (name.equalsIgnoreCase("name")) {
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
        ret.append("<" + NAME + (attEvent != null ? " event=\"" + attEvent.getValue() + "\"" : "") + ">\n");
        for (int i = 0; i < triggers.size(); i++) {
            ret.append(triggers.get(i).toString(indent + Constants.INDENT_PREFIX));
        }
        for (int i = 0; i < fusionElements.size(); i++) {
            ret.append(fusionElements.get(i).toString(indent + Constants.INDENT_PREFIX));
        }
        for (int i = 0; i < indent; i++) {
            ret.append(' ');
        }
        ret.append("</" + NAME + ">\n");
        return ret.toString();
    }
}
