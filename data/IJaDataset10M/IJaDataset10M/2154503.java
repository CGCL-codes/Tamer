package serene.validation.handlers.content.impl;

import java.util.List;
import org.xml.sax.Locator;
import serene.validation.handlers.content.ElementEventHandler;
import serene.validation.handlers.content.util.InputStackDescriptor;

class UnknownElementHandler extends ErrorEEH {

    UnknownElementHandler() {
        super();
    }

    public void recycle() {
        pool.recycle(this);
    }

    void validateInContext() {
        parent.unknownElement(inputStackDescriptor.getCurrentItemInputRecordIndex());
    }

    public String toString() {
        return "UnknownElementHandler ";
    }

    boolean functionalEquivalent(ComparableEEH other) {
        return other.functionalEquivalent(this);
    }

    boolean functionalEquivalent(ElementValidationHandler other) {
        return false;
    }

    boolean functionalEquivalent(UnexpectedElementHandler other) {
        return false;
    }

    boolean functionalEquivalent(UnexpectedAmbiguousElementHandler other) {
        return false;
    }

    boolean functionalEquivalent(UnknownElementHandler other) {
        return true;
    }

    boolean functionalEquivalent(ElementDefaultHandler other) {
        return false;
    }

    boolean functionalEquivalent(ElementConcurrentHandler other) {
        return false;
    }

    boolean functionalEquivalent(ElementParallelHandler other) {
        return false;
    }

    boolean functionalEquivalent(ElementCommonHandler other) {
        return false;
    }
}
