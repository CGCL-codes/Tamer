package org.likken.core.inspection;

import org.likken.core.Entry;
import org.likken.core.schema.AttributeType;
import org.likken.core.syntax.AttributeSyntax;
import org.likken.core.syntax.AttributeValue;
import org.likken.core.DistinguishedName;
import org.likken.core.syntax.Constants;

/**
 * @author Stephane Boisson
 * @version $Revision: 1.1 $ $Date: 2001/03/06 19:10:52 $
 */
public class MultilineInspector implements AttributeInspector {

    public String[] getHandledSyntaxes() {
        return new String[] { Constants.POSTAL_ADDRESS_OID };
    }

    public String[] getHandledTypes() {
        return new String[] { "description" };
    }

    public String getView(final DistinguishedName aDN, final AttributeType aType, final AttributeValue aValue, final int anIndex) {
        return (String) aValue.getValue();
    }
}
