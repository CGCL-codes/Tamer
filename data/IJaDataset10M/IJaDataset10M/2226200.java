package org.o14x.migale.script.impl;

import groovy.lang.GroovyShell;
import org.o14x.migale.Factory;
import org.o14x.migale.renderkit.RenderKit;
import org.o14x.migale.script.Binding;
import org.o14x.migale.script.ScriptEngine;
import org.o14x.utilz.exception.TechException;

/**
 * Groovy based implementation of ScriptEngine.
 * 
 * @author Olivier Dangr�aux
 */
public class GroovyScriptEngine implements ScriptEngine {

    Binding binding;

    /**
	 * Instantiates a new GroovyScriptEngine.
	 * 
	 * @param binding The Binding to use. 
	 */
    public GroovyScriptEngine(Binding binding) {
        this.binding = binding;
    }

    /**
	 * Instantiates a new GroovyScriptEngine.
	 * 
	 * @param renderKit The render kit to use.
	 * 
	 * @throws TechException 
	 */
    public GroovyScriptEngine(RenderKit renderKit) throws TechException {
        binding = Factory.getNewScriptBinding(renderKit);
    }

    public Object execute(String scriptSource) {
        groovy.lang.Binding groovyBinding = new groovy.lang.Binding(binding.getView());
        GroovyShell shell = new GroovyShell(groovyBinding);
        Object result = shell.evaluate(scriptSource);
        return result;
    }

    public Binding getBinding() {
        return binding;
    }

    public void setBinding(Binding binding) {
        this.binding = binding;
    }
}
