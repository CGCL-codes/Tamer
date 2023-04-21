package com.mangobop.impl.functions;

import com.mangobop.functions.Function;
import com.mangobop.functions.Reference;

/**
 * @author Stefan Meyer
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SimpleReference implements Reference {

    private Function function;

    /**
	 * 
	 */
    public SimpleReference(Function function) {
        this.function = function;
    }

    /**
	 * @return
	 */
    public Function getFunction() {
        return function;
    }

    /**
	 * @param function
	 */
    public void setFunction(Function function) {
        this.function = function;
    }
}
