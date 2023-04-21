package org.cantaloop.jiomask.factory.javacode.layout;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.cantaloop.cgimlet.lang.ClassTemplate;
import org.cantaloop.cgimlet.lang.MethodTemplate;

/**
 * AbstractFormControl.java
 *
 *
 * @created 2002-08-06 07:49:52 CEST
 *
 * @author <a href="mailto:stefan@cantaloop.org">Stefan Heimann</a>
 *
 * @version @version@ ($Revision: 1.5 $)
 */
public abstract class AbstractFormControl extends AbstractDescriptor {

    private Map m_jiostyleFacets = new HashMap();

    public AbstractFormControl() {
    }

    public void addFacet(String name, Object value) {
        m_jiostyleFacets.put(name, value);
    }

    public Map getFacets() {
        return Collections.unmodifiableMap(m_jiostyleFacets);
    }

    public abstract void accept(CellContentVisitor visitor, ClassTemplate ct, MethodTemplate method);
}
