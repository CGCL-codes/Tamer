package org.torweg.pulse.accesscontrol.attributes;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.jdom.Element;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.service.request.Command;

/**
 * represents a string based value.
 * 
 * @author Thomas Weber, Daniel Dietz
 * @version $Revision: 1378 $
 * 
 */
@Entity
@XmlRootElement(name = "string-value")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.FIELD)
public class StringValue extends AbstractValue<String> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5893574296828225190L;

    /**
	 * the value.
	 */
    @Basic
    @XmlElement(name = "value")
    private String value;

    /**
	 * the attribute.
	 */
    @ManyToOne
    @XmlElement(name = "attribute")
    private StringAttribute attribute;

    /**
	 * used by Hibernate<sup>TM</sup>.
	 */
    @Deprecated
    protected StringValue() {
        super();
    }

    /**
	 * creates a {@code StringValue} form a given string value.
	 * 
	 * @param valueString
	 *            the value
	 */
    public StringValue(final String valueString) {
        super();
        this.setValue(valueString);
    }

    /**
	 * returns the value.
	 * 
	 * @return the value
	 * @see org.torweg.pulse.accesscontrol.attributes.AbstractValue#getValue()
	 */
    @Override
    public final String getValue() {
        return this.value;
    }

    /**
	 * sets the value.
	 * 
	 * @param v
	 *            the value to set
	 * @see org.torweg.pulse.accesscontrol.attributes.AbstractValue#setValue(java.lang.Object)
	 */
    @Override
    public final void setValue(final String v) {
        this.value = v;
    }

    /**
	 * returns the attribute the value is associated with.
	 * 
	 * @return the attribute the value is associated with
	 */
    @Override
    public final AbstractAttribute<String> getAttribute() {
        return this.attribute;
    }

    /**
	 * sets the attribute the value is associated with.
	 * 
	 * @param a
	 *            the attribute to associate the value with
	 */
    @Override
    public final void setAttribute(final AbstractAttribute<String> a) {
        this.attribute = (StringAttribute) a;
    }

    /**
	 * updates the values from a given {@code Command}.
	 * 
	 * @param c
	 *            the {@code Command}
	 */
    @Override
    public void updateFromCommand(final Command c) {
        if (getAttribute() == null) {
            throw new PulseException("Cannot update value, since this value is not yet assigned to a Attribute");
        }
        if (c != null) {
            setValue(c.getParameter(((StringAttribute) getAttribute()).getParameterName()).getFirstValue());
        }
    }

    /**
	 * returns an Element representing the value.
	 * 
	 * @return an Element representing the value
	 */
    @Override
    public Element deserializeToJDOM() {
        Element valueEl = new Element("Value").setAttribute("class", getClass().getCanonicalName());
        if (getId() != null) {
            valueEl.setAttribute("id", getId().toString());
        }
        if (getAttribute() != null) {
            valueEl.addContent(new Element("values").addContent(new Element("value").setAttribute("parameterName", ((StringAttribute) getAttribute()).getParameterName()).setText(getValue())));
        }
        return valueEl;
    }

    /**
	 * @return the string stored in the value
	 * @see org.torweg.pulse.accesscontrol.attributes.AbstractValue#toString()
	 */
    @Override
    public String toString() {
        if (this.value == null) {
            return "null";
        }
        return this.value;
    }
}
