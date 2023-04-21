package org.xaware.server.engine.instruction.bizcomps.multiformat;

import java.util.Collection;
import java.util.Iterator;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * @author jweaver
 * 
 */
public class FieldsFormatDefinitionMatch implements FormatDefinitionMatchIF {

    private static final XAwareLogger lf = XAwareLogger.getXAwareLogger(FieldsFormatDefinitionMatch.class.getName());

    private final String className = "FieldsFormatDefinitionMatch";

    protected Collection fieldDefinitions = null;

    public FieldsFormatDefinitionMatch(final Collection poFieldDefinitions) {
        if (poFieldDefinitions == null) {
            throw new IllegalArgumentException("Collection of FieldDefinition Objects can not be null.");
        }
        fieldDefinitions = poFieldDefinitions;
    }

    public int getRecordEndLength() {
        int maxFragmentLength = 0;
        final Iterator fieldsIterator = fieldDefinitions.iterator();
        while (fieldsIterator.hasNext()) {
            final MultiFormatFieldDefinition fd = (MultiFormatFieldDefinition) fieldsIterator.next();
            if (fd.requiresAMatch()) {
                final int aLength = fd.getMatchEndingIndex();
                if (aLength > maxFragmentLength) {
                    maxFragmentLength = aLength;
                }
            }
        }
        return maxFragmentLength;
    }

    public boolean isMatch(final String psRecordFragment) {
        final String currentMethod = "isMatch";
        lf.entering(className, currentMethod);
        boolean isAMatch = true;
        final Iterator fieldsIterator = fieldDefinitions.iterator();
        while (fieldsIterator.hasNext() && isAMatch) {
            final MultiFormatFieldDefinition fd = (MultiFormatFieldDefinition) fieldsIterator.next();
            if (fd.requiresAMatch()) {
                isAMatch &= fd.isAMatch(psRecordFragment);
            }
        }
        lf.fine("Field match result is: " + isAMatch, className, currentMethod);
        lf.exiting(className, currentMethod);
        return isAMatch;
    }
}
