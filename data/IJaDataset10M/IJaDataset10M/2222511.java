package com.sun.syndication.io.impl;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.feed.module.SyModule;
import com.sun.syndication.feed.module.SyModuleImpl;
import com.sun.syndication.io.ModuleParser;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 */
public class SyModuleParser implements ModuleParser {

    public String getNamespaceUri() {
        return SyModule.URI;
    }

    private Namespace getDCNamespace() {
        return Namespace.getNamespace(SyModule.URI);
    }

    public Module parse(Element syndRoot) {
        boolean foundSomething = false;
        SyModule sm = new SyModuleImpl();
        Element e = syndRoot.getChild("updatePeriod", getDCNamespace());
        if (e != null) {
            foundSomething = true;
            sm.setUpdatePeriod(e.getText());
        }
        e = syndRoot.getChild("updateFrequency", getDCNamespace());
        if (e != null) {
            foundSomething = true;
            sm.setUpdateFrequency(Integer.parseInt(e.getText().trim()));
        }
        e = syndRoot.getChild("updateBase", getDCNamespace());
        if (e != null) {
            foundSomething = true;
            sm.setUpdateBase(DateParser.parseDate(e.getText()));
        }
        return (foundSomething) ? sm : null;
    }
}
