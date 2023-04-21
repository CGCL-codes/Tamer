package edu.gsbme.MMLParser2.CellML;

import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import edu.gsbme.MMLParser2.Factory.CMLFactory;

public class CellMLUnit {

    String[] insertBefore = { "component", "group", "connection" };

    String[] componentOrder = { "variable", "reaction", "math" };

    CMLFactory factory;

    /**
	 * Constructor
	 *
	 */
    public CellMLUnit(CMLFactory factory) {
        this.factory = factory;
    }

    /**
	 * Insert unit defined unit declaration
	 * @param factory.returnDoc()
	 * @param parent
	 * @param unitname
	 * @return Element
	 */
    public Element insertUserDefinedUnit(Element parent, String unitname) {
        Element unit = createUnitsTag(parent, unitname);
        unit.setAttribute("base_units", "yes");
        return unit;
    }

    /**
	 * Edit user defined unit declaration
	 * @param unit
	 * @param name
	 * @return Element
	 */
    public Element editUserDefinedUnit(Element unit, String name) {
        if ((unit.getTagName()).equals("units")) {
            unit.setAttribute("name", name);
            return unit;
        } else return null;
    }

    /**
	 * Create <units> element
	 * @param factory.returnDoc()
	 * @param parent
	 * @param name
	 * @return Element
	 */
    public Element createUnitsTag(Element parent, String name) {
        Element units = factory.returnDoc().createElement("units");
        units.setAttribute("name", name);
        if (parent.getTagName().equals("model")) positionModelUnit(units); else if (parent.getTagName().equals("component")) positionComponentUnit(parent, units);
        return units;
    }

    /**
	 * 
	 * @param factory.returnDoc()
	 * @param unit
	 * @return Element
	 */
    public Element positionModelUnit(Element unit) {
        NodeList modellist = factory.returnDoc().getElementsByTagName("model");
        if (modellist.getLength() == 0) return null;
        Element model = (Element) modellist.item(0);
        for (int i = 0; i < insertBefore.length; i++) {
            NodeList temp = factory.returnDoc().getElementsByTagName(insertBefore[i]);
            if (temp.getLength() > 0) {
                model.insertBefore((Node) unit, temp.item(0));
                return unit;
            }
        }
        model.appendChild(unit);
        return unit;
    }

    /**
	 * 
	 * @param factory.returnDoc()
	 * @param component
	 * @param unit
	 * @return Element
	 */
    public Element positionComponentUnit(Element component, Element unit) {
        for (int i = 0; i < componentOrder.length; i++) {
            NodeList temp = component.getElementsByTagName(componentOrder[i]);
            if (temp.getLength() > 0) {
                component.insertBefore((Node) unit, temp.item(0));
                return unit;
            }
        }
        component.appendChild(unit);
        return unit;
    }

    /**
	 * edit units tag
	 * @param units
	 * @param name
	 */
    public void editUnitsTag(Element units, String name) {
        units.setAttribute("name", name);
    }

    /**
	 * add unit element
	 * @param factory.returnDoc()
	 * @param parent
	 * @param offset
	 * @param prefix
	 * @param multiplier
	 * @param units
	 * @return Element
	 */
    public Element addSimpleUnitDef(Element parent, String offset, String prefix, String multiplier, String units) {
        Element unit = factory.returnDoc().createElement("unit");
        if (!offset.equals("")) unit.setAttribute("offset", offset);
        if (!prefix.equals("") && !prefix.equals("none")) unit.setAttribute("prefix", prefix);
        if (!multiplier.equals("")) unit.setAttribute("multiplier", multiplier);
        if (!units.equals("") && !units.equals("none")) unit.setAttribute("units", units);
        parent.appendChild(unit);
        return unit;
    }

    /**
	 * edit unit element
	 * @param units
	 * @param name
	 * @param offset
	 * @param prefix
	 * @param multiplier
	 * @param unitname
	 * @return Element
	 */
    public Element editSimpleUnitDef(Element units, String name, String offset, String prefix, String multiplier, String unitname) {
        editUnitsTag(units, name);
        NodeList children = units.getElementsByTagName("unit");
        Element unit = null;
        if (children.getLength() > 0) {
            unit = (Element) children.item(0);
            editSimpleUnitDef(unit, offset, prefix, multiplier, unitname);
            return units;
        }
        return null;
    }

    /**
	 * edit unit element
	 * @param unit
	 * @param offset
	 * @param prefix
	 * @param multiplier
	 * @param units
	 * @return Element
	 */
    public Element editSimpleUnitDef(Element unit, String offset, String prefix, String multiplier, String units) {
        unit.removeAttribute("offset");
        unit.removeAttribute("prefix");
        unit.removeAttribute("multiplier");
        unit.removeAttribute("units");
        if (!offset.equals("")) unit.setAttribute("offset", offset);
        if (!prefix.equals("") && !prefix.equals("none")) unit.setAttribute("prefix", prefix);
        if (!multiplier.equals("")) unit.setAttribute("multiplier", multiplier);
        if (!units.equals("") && !units.equals("none")) unit.setAttribute("units", units);
        return unit;
    }

    /**
	 * add unit element
	 * @param factory.returnDoc()
	 * @param parent
	 * @param offset
	 * @param prefix
	 * @param multiplier
	 * @param units
	 * @param exponent
	 * @return Element
	 */
    public Element addComplexUnitDef(Element parent, String offset, String prefix, String multiplier, String units, String exponent) {
        Element unit = factory.returnDoc().createElement("unit");
        if (!offset.equals("")) unit.setAttribute("offset", offset);
        if (!prefix.equals("") && !prefix.equals("none")) unit.setAttribute("prefix", prefix);
        if (!multiplier.equals("")) unit.setAttribute("multiplier", multiplier);
        if (!units.equals("") && !units.equals("none")) unit.setAttribute("units", units);
        if (!exponent.equals("")) unit.setAttribute("exponent", exponent);
        parent.appendChild(unit);
        return unit;
    }

    /**
	 * edit unit element
	 * @param unit
	 * @param offset
	 * @param prefix
	 * @param multiplier
	 * @param units
	 * @param exponent
	 * @return Element
	 */
    public Element editComplexUnitDef(Element unit, String offset, String prefix, String multiplier, String units, String exponent) {
        unit.removeAttribute("offset");
        unit.removeAttribute("prefix");
        unit.removeAttribute("multiplier");
        unit.removeAttribute("units");
        unit.removeAttribute("exponent");
        if (!offset.equals("")) unit.setAttribute("offset", offset);
        if (!prefix.equals("") && !prefix.equals("none")) unit.setAttribute("prefix", prefix);
        if (!multiplier.equals("")) unit.setAttribute("multiplier", multiplier);
        if (!units.equals("") && !units.equals("none")) unit.setAttribute("units", units);
        if (!exponent.equals("")) unit.setAttribute("exponent", exponent);
        return unit;
    }

    /**
	 * Return all units in array
	 * @param factory.returnDoc()
	 * @return String[]
	 */
    public String[] returnAllUnits() {
        if (factory.returnDoc() == null) return null;
        String[] result;
        NodeList u = factory.returnDoc().getElementsByTagName("units");
        int length = u.getLength();
        result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = ((Element) u.item(i)).getAttribute("name");
        }
        return result;
    }

    /**
	 * <units>
	 * @return
	 */
    public NodeList returnUnitsList() {
        NodeList u = factory.returnDoc().getElementsByTagName("units");
        return u;
    }

    /**
	 * <unit>
	 * @return
	 */
    public static NodeList returnUnitList(Element units) {
        NodeList u = units.getElementsByTagName("unit");
        return u;
    }

    /**
	 * Find units of a given name
	 * @param factory.returnDoc()
	 * @param name
	 * @return Node
	 */
    public Node returnUnits(String name) {
        NodeList children = factory.returnDoc().getElementsByTagName("units");
        Element temp;
        for (int x = 0; x < children.getLength(); x++) {
            temp = (Element) children.item(x);
            if ((temp.getAttribute("name")).equals(name)) return children.item(x);
        }
        return null;
    }

    /**
	 * return all units element of the parent element in a vector object
	 * @param parent
	 * @return Vector
	 */
    public Vector returnAllUnitsNodes(Element parent) {
        Vector result = new Vector();
        NodeList units = parent.getElementsByTagName("units");
        for (int i = 0; i < units.getLength(); i++) {
            result.add(units.item(i));
        }
        return result;
    }

    /**
	 * Return <unit> tag with the corrsponding name
	 * @param factory.returnDoc()
	 * @param units
	 * @param name
	 * @return Node
	 */
    public Node returnUnitsTag(Element units, String name) {
        NodeList children = units.getElementsByTagName("unit");
        Element temp;
        for (int x = 0; x < children.getLength(); x++) {
            temp = (Element) children.item(x);
            if ((temp.getAttribute("units")).equals(name)) return children.item(x);
        }
        return null;
    }

    /**
	 * Return the units contained in this parent, in an array
	 * @param parent
	 * @return String[]
	 */
    public String[] returnUnitsContained(Element parent) {
        NodeList children = parent.getChildNodes();
        Vector temp = returnUnitsNode(parent);
        String[] result = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            result[i] = ((Element) temp.get(i)).getAttribute("name");
        }
        return result;
    }

    /**
	 * 
	 * @param parent
	 * @return Vector
	 */
    public Vector returnUnitsNode(Element parent) {
        NodeList children = parent.getChildNodes();
        Vector temp = new Vector();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("units")) {
                temp.add((Element) n);
            }
        }
        return temp;
    }

    /**
	 * 
	 * @param units
	 * @return String
	 */
    public String returnUnitType(Element units) {
        NodeList unit = units.getElementsByTagName("unit");
        if (unit.getLength() == 0 && units.getAttribute("base_units").equalsIgnoreCase("yes")) return "User Defined";
        if (unit.getLength() == 1) {
            Element unit_element = (Element) unit.item(0);
            if (unit_element.hasAttribute("exponent")) {
                try {
                    if (Float.parseFloat(unit_element.getAttribute("exponent")) == 1) return "Simple Units"; else return "Complex Units";
                } catch (NumberFormatException e) {
                    return "Complex Units";
                }
            } else {
                return "Simple Units";
            }
        }
        return "Complex Units";
    }

    /**
	 * Return the parent <units> tag
	 * @param child <unit> tag
	 * @return Element
	 */
    public Element getUnitsParent(Node child) {
        Node result = null;
        if (!child.getNodeName().equals("unit")) return null;
        result = child.getParentNode();
        while (result != null) {
            if (result.getNodeName().equals("units")) {
                return (Element) result;
            }
            if (result.getNodeName().equals("component")) return null;
            if (result.getNodeName().equals("model")) return null;
            result = result.getParentNode();
        }
        return null;
    }

    /**
	 * Get the parent of this <units> , either model or component
	 * @param child
	 * @return Element
	 */
    public Element getParent(Node child) {
        Node result = null;
        result = child.getParentNode();
        while (result != null) {
            if (result.getNodeName().equals("model")) {
                return (Element) result;
            }
            if (result.getNodeName().equals("component")) {
                return (Element) result;
            }
            result = result.getParentNode();
        }
        return null;
    }

    public void dispose() {
        this.factory = null;
    }
}
