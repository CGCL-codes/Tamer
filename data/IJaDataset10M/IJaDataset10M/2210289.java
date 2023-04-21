package fi.iki.asb.util.config;

import org.w3c.dom.*;

/**
 * An object representing an attribute.
 *
 * @author Antti S. Brax
 * @version 1.0
 */
class Attr extends Param {

    private String name = null;

    /**
     * @param param an attr or param element.
     */
    public Attr(XmlConfig conf, Node attr) throws Exception, XmlConfigException, ClassNotFoundException {
        super(conf, attr);
        name = conf.getAttributeValue(attr, "name");
        if (name == null) {
            throw new XmlConfigException("attr element must have a name " + "attribute");
        }
    }

    /**
     * Set the value of name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of name.
     */
    public String getName() {
        return name;
    }
}
