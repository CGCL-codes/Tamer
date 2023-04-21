package com.potix.idom;

import com.potix.lang.Objects;
import com.potix.idom.impl.*;

/**
 * The iDOM entity reference.
 *
 * @author <a href="mailto:tomyeh@potix.com">Tom M. Yeh</a>
 */
public class EntityReference extends AbstractGroup implements org.w3c.dom.EntityReference {

    /** The name. */
    protected String _name;

    /** Constructor.
	 */
    public EntityReference(String name) {
        setName(name);
    }

    /** Constructor.
	 */
    protected EntityReference() {
    }

    public final String getName() {
        return _name;
    }

    public final void setName(String name) {
        checkWritable();
        if (!Objects.equals(_name, name)) {
            Verifier.checkXMLName(name, getLocator());
            _name = name;
            setModified();
        }
    }

    public final short getNodeType() {
        return ENTITY_REFERENCE_NODE;
    }

    public String toString() {
        return "[EntityReference: &" + _name + ";]";
    }
}
