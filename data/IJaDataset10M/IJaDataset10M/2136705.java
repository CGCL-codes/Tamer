package org.eclipse.wst.xml.search.editor.searchers.statics;

import org.eclipse.wst.xml.search.core.statics.IStaticValue;
import org.eclipse.wst.xml.search.core.statics.IStaticValueCollector;
import org.eclipse.wst.xml.search.editor.validation.AbstractValidationResult;

public class ValidationResultForStatics extends AbstractValidationResult implements IStaticValueCollector {

    public ValidationResultForStatics(String value, int startIndex, int endIndex) {
        super(value, startIndex, endIndex);
    }

    public boolean add(IStaticValue value) {
        nbElements++;
        return true;
    }
}
