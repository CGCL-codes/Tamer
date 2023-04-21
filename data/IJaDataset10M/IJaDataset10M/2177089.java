package org.apache.jasper.tagplugins.jstl.core;

import org.apache.jasper.compiler.tagplugin.*;

public final class Choose implements TagPlugin {

    public void doTag(TagPluginContext ctxt) {
        ctxt.generateBody();
        ctxt.generateJavaSource("}");
    }
}
