package org.apache.roller.webservices.atomprotocol;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jdom.Element;
import org.jdom.Namespace;
import com.sun.syndication.feed.module.Module;
import com.sun.syndication.io.ModuleGenerator;

public class PubControlModuleGenerator implements ModuleGenerator {

    private static final Namespace PUBCONTROL_NS = Namespace.getNamespace("app", PubControlModule.URI);

    public String getNamespaceUri() {
        return PubControlModule.URI;
    }

    private static final Set NAMESPACES;

    static {
        Set nss = new HashSet();
        nss.add(PUBCONTROL_NS);
        NAMESPACES = Collections.unmodifiableSet(nss);
    }

    public Set getNamespaces() {
        return NAMESPACES;
    }

    public void generate(Module module, Element element) {
        PubControlModule m = (PubControlModule) module;
        String draft = m.getDraft() ? "yes" : "no";
        Element control = new Element("control", PUBCONTROL_NS);
        control.addContent(generateSimpleElement("draft", draft));
        element.addContent(control);
    }

    protected Element generateSimpleElement(String name, String value) {
        Element element = new Element(name, PUBCONTROL_NS);
        element.addContent(value);
        return element;
    }
}
