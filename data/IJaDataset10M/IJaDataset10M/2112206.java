package net.sf.metarbe.rsxml.handlers;

import net.sf.metarbe.rsxml.MalformedElementException;
import net.sf.metarbe.scripting.ScriptRuleSetManager;

public class GlobalHandler extends DomElementAbstractHandler {

    public GlobalHandler(String tagName) {
        super(tagName);
    }

    @Override
    public void configureScriptingRules(ScriptRuleSetManager aScriptingFacade) throws MalformedElementException {
        aScriptingFacade.setGlobalScript(getScript());
    }
}
