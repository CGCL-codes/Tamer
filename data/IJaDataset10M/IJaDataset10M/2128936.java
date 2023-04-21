package net.sourceforge.jcoupling2.dao;

import java.util.Iterator;
import java.util.TreeSet;
import net.sourceforge.jcoupling.JCouplingException;
import net.sourceforge.jcoupling2.dao.obsolete.Attribute;
import net.sourceforge.jcoupling2.dao.obsolete.TransferException;
import org.apache.log4j.Logger;

public class Property implements Comparable<Property> {

    private TreeSet<Attribute> propertyAttributes = new TreeSet<Attribute>();

    private static Logger logger = Logger.getLogger(Property.class);

    /**
	 * 
	 * A <code>property </code> object defines a function over a message to extract values from it. Properties are no
	 * independent objects but need to be associated with a channel.
	 * 
	 * @param propertyName
	 *          a unique name
	 */
    public Property() {
    }

    public TreeSet<Attribute> getPropertyAttributes() {
        if (this.propertyAttributes == null) {
            this.propertyAttributes = new TreeSet<Attribute>();
        }
        return this.propertyAttributes;
    }

    public void setPropertyAttributes(TreeSet<Attribute> attributes) {
        this.propertyAttributes = attributes;
    }

    public int compareTo(Property property) {
        Iterator<Attribute> attributeList = null;
        Attribute attribute = null;
        String thisId = null;
        String id = null;
        if (this.propertyAttributes != null && property.propertyAttributes != null) {
            attributeList = this.propertyAttributes.iterator();
            while (attributeList.hasNext()) {
                attribute = attributeList.next();
                if (attribute.isPrimary) {
                    thisId = attribute.value;
                }
            }
            attributeList = property.propertyAttributes.iterator();
            while (attributeList.hasNext()) {
                attribute = attributeList.next();
                if (attribute.isPrimary) {
                    id = attribute.value;
                }
            }
            if (thisId != null && id != null) {
                return thisId.compareTo(id);
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public String toString() {
        Iterator<Attribute> attributeList = this.propertyAttributes.iterator();
        Attribute attribute = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getClass().getSimpleName());
        buffer.append("[");
        while (attributeList.hasNext()) {
            attribute = attributeList.next();
            buffer.append(attribute.name);
            buffer.append("=");
            buffer.append(attribute.value);
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("]");
        return buffer.toString();
    }

    /**
	 * This method returns an executable object capable of receiving a message and returning a property value.
	 * 
	 * @return a property capable of producing a value.
	 * @throws JCouplingException
	 * @throws JCouplingException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
    public net.sourceforge.jcoupling.peer.mql.Property rebuildExecutablePropertyInstance() throws JCouplingException {
        Object obj = null;
        try {
            String classname = getClassnameForExecutableObject();
            Class classd = Class.forName(classname);
            obj = classd.newInstance();
        } catch (InstantiationException e) {
            throw new TransferException("Unable to instantiate the property.", e);
        } catch (IllegalAccessException e) {
            throw new TransferException("Target property has no default constructor.", e);
        } catch (ClassNotFoundException e) {
            throw new TransferException("Target property not found.", e);
        }
        if (obj instanceof net.sourceforge.jcoupling.peer.mql.Property) {
            net.sourceforge.jcoupling.peer.mql.Property prop = (net.sourceforge.jcoupling.peer.mql.Property) obj;
            prop.initInstance(getPropertyAttributes());
            return prop;
        } else return null;
    }

    private String getClassnameForExecutableObject() throws JCouplingException {
        for (Attribute attr : getPropertyAttributes()) {
            if (attr.name.equals("classname")) {
                return attr.value;
            }
        }
        throw new JCouplingException("Could not create Class for executable Object!");
    }
}
