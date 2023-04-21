package com.objectwave.xjr.codegen;

import com.objectwave.xjr.*;
import java.text.*;

/**
 * @author  trever
 * @version  $Id: IntegerPropertySpec.java,v 1.1.1.1 2002/05/15 16:50:06 trever Exp $
 */
public class IntegerPropertySpec extends PropertySpec {

    /**
	 * @param  as
	 */
    public IntegerPropertySpec(AttributeSpec as) {
        super(as);
        aSpec.setDefaultValue(XJRDictionary.stripQuotes(aSpec.getDefaultValue()));
    }

    /**
	 * @return  always returns "integer"
	 */
    public String getPropertyType() {
        return "integer";
    }
}
