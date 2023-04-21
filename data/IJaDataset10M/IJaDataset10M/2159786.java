package com.volantis.devrep.repository.impl.devices.policy.types;

import com.volantis.mcs.devices.policy.types.SelectionPolicyType;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.type.immutable.ImmutableMetaDataType;
import com.volantis.shared.metadata.type.mutable.MutableStringType;
import com.volantis.shared.metadata.value.mutable.MutableStringValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of {@link SelectionPolicyType}.
 */
public class DefaultSelectionPolicyType extends DefaultPolicyType implements SelectionPolicyType {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Flag to indicate if initialisation is complete.
     */
    private boolean complete;

    /**
     * The list of keywords for this selection. 
     * <p>
     * This will be a normal list before {@link #complete} has been called, 
     * and an unmodifiable list afterwards.
     */
    private List keywords = new ArrayList();

    /**
     * Add a keyword to the selection.
     * <p>
     * Note: This method may only be called before {@link #complete}.
     * 
     * @param keyword the keyword to add.
     */
    public void addKeyword(String keyword) {
        ensureIncomplete();
        keywords.add(keyword);
    }

    /**
     * Mark the selection as having had it's initialisation completed.
     * <p>
     * Note: This method must be called after {@link #addKeyword} and before 
     * {@link #getKeywords}. 
     */
    public void complete() {
        ensureIncomplete();
        keywords = Collections.unmodifiableList(keywords);
        complete = true;
    }

    /**
     * Note: this method must be called after {@link #complete}.
     */
    public List getKeywords() {
        ensureComplete();
        return keywords;
    }

    /**
     * Throws a runtime exception if this type's initialisation has not been
     * completed.
     */
    private void ensureComplete() {
        if (!complete) {
            throw new IllegalStateException();
        }
    }

    /**
     * Throws a runtime exception if this type's initialisation has been
     * completed.
     */
    private void ensureIncomplete() {
        if (complete) {
            throw new IllegalStateException();
        }
    }

    public ImmutableMetaDataType createMetaDataType() {
        ensureComplete();
        MutableStringType stringType = TYPE_FACTORY.createStringType();
        MutableEnumeratedConstraint enumeratedConstraint = CONSTRAINT_FACTORY.createEnumeratedConstraint();
        List allowableValues = enumeratedConstraint.getMutableEnumeratedValues();
        for (Iterator i = keywords.iterator(); i.hasNext(); ) {
            String keyword = (String) i.next();
            MutableStringValue value = VALUE_FACTORY.createStringValue();
            value.setValue(keyword);
            allowableValues.add(value);
        }
        stringType.setEnumeratedConstraint(enumeratedConstraint);
        return (ImmutableMetaDataType) stringType.createImmutable();
    }

    public String toString() {
        return "[DefaultSelectionPolicyType: " + "keywords=" + keywords + "]";
    }
}
