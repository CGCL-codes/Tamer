package org.opennms.reporting.datablock;

/**
 * This class gives a name to the object.
 *
 * @author <A HREF="mailto:jacinta@oculan.com">Jacinta Remedios </A>
 * @author <A HREF="http://www.oculan.com">oculan.com </A>
 * @author <A HREF="mailto:jacinta@oculan.com">Jacinta Remedios </A>
 * @author <A HREF="http://www.oculan.com">oculan.com </A>
 * @version $Id: $
 */
public class StandardNamedObject extends Object {

    /**
     * The name of the object
     */
    private String m_name;

    /**
     * Default Constructor.
     */
    public StandardNamedObject() {
        m_name = new String();
    }

    /**
     * Constructor.
     *
     * @param name a {@link java.lang.String} object.
     */
    public StandardNamedObject(String name) {
        m_name = new String(name);
    }

    /**
     * Set the name
     *
     * @param name
     *            The name to be set.
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * Return the name
     *
     * @return the name.
     */
    public String getName() {
        return m_name;
    }
}
