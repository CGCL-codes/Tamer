package de.mindcrimeilab.xsanalyzer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import de.mindcrimeilab.xsanalyzer.util.XSModelHelper;

/**
 * @author Michael Engelhardt<me@mindcrime-ilab.de>
 * @author $Author: agony $
 * @version $Revision: 165 $
 * 
 */
public class TypeUsageWorker extends AbstractXsComponentWorker implements XsComponentWorker {

    /** XRef Map of types and respective users */
    private final Map<XSObject, XSObjectListImpl> usedBy;

    public TypeUsageWorker() {
        super();
        usedBy = new HashMap<XSObject, XSObjectListImpl>();
    }

    /**
     * Returns the xref map of types and users
     * 
     * @return the usedBy
     */
    public Map<XSObject, ? extends XSObjectList> getUsedBy() {
        return Collections.unmodifiableMap(usedBy);
    }

    @Override
    public void execute(XSObject object, XSObject parent) {
        final XSTypeDefinition type = XSModelHelper.getBaseType(object);
        if (null == type) {
            return;
        }
        addUsedByType(object, type);
    }

    @Override
    public boolean isSupported(XSObject object) {
        return true;
    }

    /**
     * Add xref to usedBy map.
     * 
     * @param element
     *            element which is used by <code>type<code>
     * @param type
     *            type definition
     */
    private void addUsedByType(XSObject element, XSTypeDefinition type) {
        if (null == element) {
            return;
        }
        if (XSModelHelper.isSameTypeDefinition(element, type)) {
            return;
        }
        if (usedBy.containsKey(type)) {
            final XSObjectListImpl objectList = usedBy.get(type);
            for (int i = 0; i < objectList.getLength(); ++i) {
                XSObject object = objectList.item(i);
                if (XSModelHelper.isSameTypeDefinition(object, element)) {
                    return;
                }
            }
            objectList.add(element);
        } else {
            final XSObjectListImpl xsoList = new XSObjectListImpl();
            xsoList.add(element);
            usedBy.put(type, xsoList);
        }
    }
}
