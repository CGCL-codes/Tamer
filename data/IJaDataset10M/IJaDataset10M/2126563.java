package com.gorillalogic.compile.xmidoc;

import com.gorillalogic.compile.CompileException;
import com.gorillalogic.compile.XMIImporter;

public class XMIElementException extends CompileException {

    public XMIElementException(XMIImporter imp) {
        super(imp);
    }

    /**
     * Constructs an instance of <code>CompileException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public XMIElementException(XMIImporter imp, String msg) {
        super(imp, msg);
    }

    public XMIElementException(XMIImporter imp, String msg, Throwable e) {
        super(imp, msg, e);
    }
}
