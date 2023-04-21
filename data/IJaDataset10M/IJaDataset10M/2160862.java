package com.aptana.ide.editor.js.runtime;

import com.aptana.ide.lexer.IRange;

/**
 * @author Robin Debreuil
 * @author Kevin Lindsey
 */
public class JSBoolean extends ObjectBase {

    /**
	 * @see com.aptana.ide.editor.js.runtime.ObjectBase#getClassName()
	 */
    public String getClassName() {
        return "Boolean";
    }

    /**
	 * Create a new instance of JSBoolean
	 */
    public JSBoolean() {
        this(null);
    }

    /**
	 * Create a new instance of JSBoolean
	 * 
	 * @param sourceRegion
	 *            The region within the source that defines this object instance
	 */
    public JSBoolean(IRange sourceRegion) {
        super(sourceRegion);
    }
}
