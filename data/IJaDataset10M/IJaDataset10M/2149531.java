package org.xmlvm.refcount.optimizations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.xmlvm.refcount.CodePath;
import org.xmlvm.refcount.InstructionActions;
import org.xmlvm.refcount.ReferenceCountingException;

/**
 * A growing interface used to add optimizations to the reference counting
 * module.
 */
public interface RefCountOptimization {

    /**
     * Used to add instructions to the start of a method
     */
    public class ReturnValue {

        public List<Element> functionInit = new ArrayList<Element>();
    }

    /**
     * This interface allows an implementor to make changes to nulling releases
     * and retains as it desires, based on all the processing that as already
     * occurred.
     */
    public ReturnValue Process(List<CodePath> allCodePaths, Map<Element, InstructionActions> beenTo, Element codeElement) throws ReferenceCountingException, DataConversionException;
}
