package org.ofbiz.minilang.operation;

import java.util.*;
import org.w3c.dom.*;
import org.ofbiz.base.util.*;

/**
 * <p><b>Title:</b> A MakeInStringOperation that appends the specified constant string
 */
public class ConstantOper extends MakeInStringOperation {

    String constant;

    public ConstantOper(Element element) {
        super(element);
        constant = UtilXml.elementValue(element);
    }

    public String exec(Map inMap, List messages, Locale locale, ClassLoader loader) {
        return constant;
    }
}
