package uk.ac.rothamsted.ovtk.Petri;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * @author steuerna
 *
 */
public class XMLEntity {

    Element Entity = new Element("entity");

    Namespace ns = Namespace.getNamespace("csml", "http://www.csml.org/csml/version1");

    public XMLEntity(Element e) {
        Entity = e;
        ns = e.getNamespace();
    }

    public XMLEntity(String label, String type, String variable) {
        Entity.setNamespace(ns);
        Entity.setAttribute("label", label);
        Entity.setAttribute("name", label);
        Entity.setAttribute("type", type);
        Element parameter = new Element("parameter");
        parameter.setAttribute("label", variable);
        parameter.setAttribute("initialValue", "0");
        parameter.setAttribute("minimumValue", "0");
        parameter.setAttribute("maximumValue", "infinite");
        Entity.addContent(parameter);
        Element graphics = new Element("graphics", ns);
        Element figure = new Element("figure", ns);
        if (type.equalsIgnoreCase("continuous")) {
            Element conEnt = new Element("continuousEntity", ns);
            conEnt.setAttribute(new Attribute("location", "0.0 0.0"));
            figure.addContent(conEnt);
        } else {
            if (type.equalsIgnoreCase("discrete")) {
                Element disEnt = new Element("discreteEntity", ns);
                disEnt.setAttribute(new Attribute("location", "0.0 0.0"));
                figure.addContent(disEnt);
            }
        }
        graphics.addContent(figure);
        Entity.addContent(graphics);
    }

    /**
	 * 
	 * @return returns the XML Element 
	 */
    public Element getElement() {
        return Entity;
    }

    /**
	 * 
	 * @return The label of the Entity. This is useful to find all Connectors concerning the Entity.
	 */
    public String getLabel() {
        String name = (Entity.getAttribute("label")).getValue();
        return name;
    }

    /**
	 * returns the name of the entity
	 * @return name of the entity
	 */
    public String getName() {
        String name = (Entity.getAttributeValue("name"));
        return name;
    }

    /**
	 * sets the Name of the Entity to the given String
	 * @param n The Name of the Entity
	 */
    public void setName(String n) {
        if (n != null) {
            Entity.setAttribute("name", n);
        } else {
            Entity.setAttribute("name", "name not known");
        }
    }

    /**
	 * Sets the location attribute. (generic Entities are not designated!)
	 * @param loc the location of the entity. A String like "100.0 100.0"
	 */
    public void setLocation(String loc) {
        if ((Entity.getAttributeValue("type")).equalsIgnoreCase("continuous")) {
            (Entity.getChild("graphics", ns).getChild("figure", ns).getChild("continuousEntity", ns)).setAttribute("location", loc);
        } else {
            if ((Entity.getAttributeValue("type")).equalsIgnoreCase("discrete")) {
                (Entity.getChild("graphics", ns).getChild("figure", ns).getChild("discreteEntity", ns)).setAttribute("location", loc);
            }
        }
    }

    /**
	 * 
	 * @return returns the initial Value of the entity
	 */
    public String getInitialValue() {
        String iniVal = ((Entity.getChild("parameter", ns)).getAttribute("initialValue")).getValue();
        return iniVal;
    }

    /**
	 * change the InitialValue of a Entity
	 * @param iniVal is the new Value
	 */
    public void setInitaialValue(String iniVal) {
        ((Entity.getChild("parameter", ns)).getAttribute("initialValue")).setValue(iniVal);
    }

    /**
	 * 
	 * @return the fillColor of the Entity
	 */
    public String getColor() {
        String color = new String();
        Attribute fillColor;
        if (Entity.getChild("graphics", ns).getChild("figure", ns).getChild("continuousEntity", ns) != null) {
            fillColor = Entity.getChild("graphics", ns).getChild("figure", ns).getChild("continuousEntity", ns).getAttribute("fillColor");
        } else {
            fillColor = Entity.getChild("graphics", ns).getChild("figure", ns).getChild("discreteEntity", ns).getAttribute("fillColor");
        }
        if (fillColor != null) {
            color = fillColor.getValue();
        }
        return color;
    }

    public void setColor(String color) {
        final String red = new String("255 0 0 255");
        final String blue = new String("0 0 255 255");
        final String green = new String("0 255 0 255");
        final String gray = new String("192 192 192 255");
        final String pink = new String("255 175 175 255");
        final String orange = new String("255 200 0 255");
        String fillcolor = new String(pink);
        if (color.equalsIgnoreCase("red")) {
            fillcolor = red;
        }
        if (color.equalsIgnoreCase("blue")) {
            fillcolor = blue;
        }
        if (color.equalsIgnoreCase("green")) {
            fillcolor = green;
        }
        if (color.equalsIgnoreCase("gray")) {
            fillcolor = gray;
        }
        if (color.equalsIgnoreCase("pink")) {
            fillcolor = pink;
        }
        if (color.equalsIgnoreCase("orange")) {
            fillcolor = orange;
        }
        if (Entity.getChild("graphics", ns).getChild("figure", ns).getChild("continuousEntity", ns) != null) {
            Entity.getChild("graphics", ns).getChild("figure", ns).getChild("continuousEntity", ns).setAttribute("fillColor", fillcolor);
        } else {
            Entity.getChild("graphics", ns).getChild("figure", ns).getChild("discreteEntity", ns).setAttribute("fillColor", fillcolor);
        }
    }

    /**
	 * 
	 * @return returns the comment of the entity
	 */
    public String getComment() {
        String c = new String();
        if (Entity.getChild("comment", ns) != null) {
            Element comment = Entity.getChild("comment", ns);
            c = comment.getText();
        }
        return c;
    }

    public void setComment(String c) {
        CDATA mycdata = new CDATA(c);
        if (Entity.getChild("comment", ns) != null) {
            Entity.removeChild("comment", ns);
        }
        Element comment = new Element("comment", ns);
        comment.addContent(mycdata);
        Entity.addContent(comment);
    }

    public String getGeneForEnzyme() {
        String gene = new String();
        if ((getColor().equalsIgnoreCase("255 200 0 255")) || (getColor().equalsIgnoreCase("0 0 255 255"))) {
            String comment = getComment();
            if ((comment.trim()).equalsIgnoreCase("unknown".trim()) != true) {
                try {
                    String[] c1 = comment.split("\n");
                    String c2 = c1[2];
                    String[] c3 = c2.split(" ");
                    gene = c3[1].trim();
                } catch (ArrayIndexOutOfBoundsException e1) {
                    System.out.println("Gene " + getName() + " of Enzyme is unknown");
                }
            } else {
                System.out.println("Gene " + getName() + " of Enzyme is unknown");
            }
        } else {
        }
        return gene;
    }
}
