package com.novocode.naf.resource.js;

import org.mozilla.javascript.ScriptableObject;

/**
 * The NAF Scriptable.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 7, 2008
 * @version $Id: NAFObject.java 417 2008-05-06 22:27:03Z szeiger $
 */
public class NAFObject extends ScriptableObject {

    private static final long serialVersionUID = 5517702007340899751L;

    private JSResourceLoader loader;

    public void jsConstructor(Object loader) {
        this.loader = (JSResourceLoader) loader;
    }

    @Override
    public String getClassName() {
        return "NAF";
    }

    public void jsFunction_define(Object o) {
        loader.nafDefine(o);
    }

    public void jsFunction_runMainWindow(Object o) {
        loader.nafRunMainWindow(o);
    }
}
