package fr.lig.sigma.astral.interpreter.builder;

import fr.lig.sigma.astral.common.InstanceCreationException;
import fr.lig.sigma.astral.common.structure.Entity;
import fr.lig.sigma.astral.common.structure.Relation;
import fr.lig.sigma.astral.handler.Handler;
import fr.lig.sigma.astral.interpreter.common.XMLUtils;
import fr.lig.sigma.astral.interpreter.common.BottomUpGuide;
import fr.lig.sigma.astral.interpreter.common.QueryVisitor;
import fr.lig.sigma.astral.query.AstralCore;
import fr.lig.sigma.astral.query.QueryNode;
import fr.lig.sigma.astral.query.QueryRuntime;
import fr.lig.sigma.astral.source.*;
import org.w3c.dom.*;
import java.util.*;

/**
 *
 */
public class QueryBuilder {

    private AstralCore core;

    private static final Handler[] NULL_HANDLERS = new Handler[0];

    public QueryBuilder(AstralCore core) {
        this.core = core;
    }

    public void induceSource(Document doc) throws SourceAlreadyExistsException, InstanceCreationException, UnknownSourceException {
        Node sourceNode = XMLUtils.seek("//sources[1]", doc);
        if (sourceNode == null) sourceNode = appendSourceNode(doc);
        NodeList nodes = XMLUtils.list("source", sourceNode);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            declare((Element) n);
        }
        nodes = XMLUtils.list("//query//source", doc);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element n = (Element) nodes.item(i);
            String id = n.getAttribute("id");
            Element sourceElement = (Element) XMLUtils.seek("source[@id='" + id + "']", sourceNode);
            if (sourceElement == null) {
                fetchDeclaredSource(doc, sourceNode, id, n);
            } else {
                n.setAttribute("type", sourceElement.getAttribute("type"));
                String attributes = sourceElement.getAttribute("attributes");
                n.setAttribute("sourceAttributes", attributes);
                n.setAttribute("attributes", attributes);
            }
        }
    }

    private Node appendSourceNode(Document doc) {
        Node n = doc.createElement("sources");
        doc.getFirstChild().appendChild(n);
        return n;
    }

    private void fetchDeclaredSource(Document doc, Node sourceNode, String id, Element queryElement) throws UnknownSourceException {
        Element newSource = doc.createElement("source");
        Source e = core.getSm().getSource(id);
        Map<String, Object> prop = core.getSf().getProperties(e);
        for (Map.Entry<String, Object> entry : prop.entrySet()) {
            if ("entityname".equals(entry.getKey()) || "schema".equals(entry.getKey())) continue;
            Object value = entry.getValue();
            if (value != null) newSource.setAttribute(entry.getKey(), value.toString());
        }
        newSource.setAttribute("impl", e.getClass().getSimpleName());
        newSource.setAttribute("id", id);
        String type = toTypeString(e);
        newSource.setAttribute("type", type);
        queryElement.setAttribute("type", type);
        String attributes = toAttributesString(e.getAttributes());
        newSource.setAttribute("attributes", attributes);
        queryElement.setAttribute("attributes", attributes);
        queryElement.setAttribute("sourceAttributes", attributes);
        sourceNode.appendChild(newSource);
    }

    private String toAttributesString(Set<String> attributes) {
        String s = attributes.toString();
        return s.substring(1, s.length() - 1).replaceAll(" ", "");
    }

    private String toTypeString(Entity e) {
        return e instanceof Relation ? "Relation" : "Stream";
    }

    private void declare(Element e) throws InstanceCreationException, SourceAlreadyExistsException {
        Properties p = extractProperties(e, true);
        String impl = (String) p.get("stype");
        if (impl == null) impl = (String) p.get("impl");
        Source s = core.getSf().createSource(impl, p);
        e.setAttribute("type", toTypeString(s));
        e.setAttribute("attributes", toAttributesString(s.getAttributes()));
    }

    private Properties extractProperties(Element e, boolean changeId) {
        Properties p = new Properties();
        NodeList children = e.getChildNodes();
        Dictionary<Object, Object> filter = new Properties();
        p.put("requires.filters", filter);
        if (children.getLength() > 0) {
            Dictionary<Object, Object> arguments = new Properties();
            for (int i = 0; i < children.getLength(); i++) {
                if (!(children.item(i) instanceof Element)) continue;
                Element elt = (Element) children.item(i);
                if ("filter".equals(elt.getNodeName())) filter.put(elt.getAttribute("name"), elt.getAttribute("value"));
                if ("argument".equals(elt.getNodeName())) arguments.put(elt.getAttribute("name"), elt.getAttribute("value"));
            }
            if (!arguments.isEmpty()) p.put("arguments", arguments);
        }
        for (int i = 0; i < e.getAttributes().getLength(); i++) {
            Attr t = (Attr) e.getAttributes().item(i);
            if (changeId && "id".equals(t.getName())) p.put("entityname", t.getValue()); else p.put(t.getName(), t.getValue());
        }
        return p;
    }

    public void buildHandlers(Document doc, QueryRuntime qr, AstralCore core, QueryNode child) throws Exception {
        Node handlerList = XMLUtils.seek("//handlers[1]", doc);
        Handler res;
        if (handlerList == null) return;
        NodeList nodes = XMLUtils.list("handler", handlerList);
        Properties p = extractProperties((Element) nodes.item(0), false);
        Object type = p.get("type");
        if (type == null) type = p.get("impl");
        res = core.getHf().createHandler((String) type, p, core);
        core.getHf().prepareHandler(res, qr);
        qr.setQueryNode(new QueryNode("handler", convert(p), Arrays.asList(child), String.valueOf(core.getEngine().getServiceId(res))));
    }

    private Map<String, Object> convert(Properties p) {
        Map<String, Object> res = new HashMap<String, Object>();
        for (Map.Entry<Object, Object> entry : p.entrySet()) {
            res.put((String) entry.getKey(), entry.getValue());
        }
        return res;
    }
}
