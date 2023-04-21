package org.jtools.tmpl.macro.buildin;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jtools.tmpl.compiler.api.CompilerException;
import org.jtools.tmpl.compiler.api.Function;
import org.jtools.tmpl.compiler.api.Mandatory;
import org.jtools.tmpl.compiler.api.SimpleMacro;
import org.jtools.util.StringUtils;

@org.jtools.tmpl.compiler.api.Definition(ns = "buildin")
@Function({ "name", "classname" })
public class Macros extends SimpleMacro {

    private static final Logger LOG = Logger.getLogger(Macros.class.getName());

    private String name;

    private String classname;

    public void setName(String name) {
        this.name = name;
    }

    @Mandatory
    public void setClassname(String classname) {
        this.classname = classname;
    }

    @SuppressWarnings("unchecked")
    public void execute() {
        try {
            Object macroSet = Class.forName(classname);
            getTemplate().addDefinition(macroSet, null, name);
            LOG.logp(Level.FINE, Macros.class.getName(), "execute", "definition '{0}' as group {1} macros added.", new Object[] { classname, StringUtils.literal(name) });
        } catch (Exception ex) {
            CompilerException cex = createException("adding definition with parameters name='" + name + "' classname='" + classname + "' failed.");
            cex.initCause(ex);
            throw cex;
        }
    }
}
