package net.sourceforge.srr.rule.persistence.digester;

import java.util.HashMap;
import java.util.Map;
import net.sourceforge.srr.domainproperty.value.representation.ValueRepresentationConstantsI;
import net.sourceforge.srr.domainproperty.value.representation.ValueRepresentationManager;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ObjectCreationFactory;
import org.xml.sax.Attributes;

/**
 * The factory for digester to use when creating ValueRepresentations
 * from XML.
 */
public class ValueRepresentationFactory implements ObjectCreationFactory {

    static final String ATTRIBUTE_LOCAL_NAME_ID = "type";

    static final String ATTRIBUTE_LOCAL_NAME_PATTERN = "pattern";

    public Object createObject(Attributes saxAttribs) throws Exception {
        String dpvrType = saxAttribs.getValue(ATTRIBUTE_LOCAL_NAME_ID);
        String pattern = saxAttribs.getValue(ATTRIBUTE_LOCAL_NAME_PATTERN);
        Map details = new HashMap();
        if (pattern == null) {
        } else {
            details.put(ValueRepresentationConstantsI.DPVR_DETAIL_PATTERN, pattern);
        }
        return ValueRepresentationManager.getInstance().getValueRepresentation(dpvrType, details);
    }

    public Digester getDigester() {
        return null;
    }

    public void setDigester(Digester arg0) {
    }
}
