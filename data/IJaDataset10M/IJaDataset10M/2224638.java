package org.tagbox.engine.procedure.module;

import org.tagbox.engine.TagBoxException;
import org.tagbox.engine.TagBoxConfigurationException;
import org.tagbox.engine.procedure.Procedure;
import org.tagbox.engine.procedure.ProcedureFactory;
import org.tagbox.engine.descender.Descender;
import org.tagbox.engine.descender.NamespaceDescender;
import org.tagbox.xml.NodeFinder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;
import org.tagbox.util.Log;

public class ModuleFactory extends ProcedureFactory {

    public Module buildModule(Element module) throws TagBoxException {
        String name = module.getAttribute("name");
        if (name == null) throw new TagBoxConfigurationException("module missing 'name' attribute");
        String ns = module.getAttribute("namespace");
        if (ns.equals("")) throw new TagBoxConfigurationException("module missing 'namespace' attribute");
        Descender descender = new NamespaceDescender(ns);
        Module mod = new Module(name, descender);
        NodeFinder finder = descender.getNodeFinder();
        NodeIterator it = finder.getElements(module, "procedure");
        for (Node node = it.nextNode(); node != null; node = it.nextNode()) {
            Procedure procedure = buildProcedure((Element) node, descender);
            mod.addProcedure(procedure);
            Log.trace("module >" + name + "< procedure >" + procedure.getName() + "<");
        }
        Log.trace("ModuleFactory >" + name + "<");
        return mod;
    }
}
