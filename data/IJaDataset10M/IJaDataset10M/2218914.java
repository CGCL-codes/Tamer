package org.genxdm.bridgekit.filters;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.genxdm.bridgekit.names.DefaultNamespaceBinding;
import org.genxdm.bridgekit.xs.BuiltInSchema;
import org.genxdm.exceptions.GenXDMException;
import org.genxdm.exceptions.PreCondition;
import org.genxdm.io.DtdAttributeKind;
import org.genxdm.names.NamespaceBinding;
import org.genxdm.typed.io.SequenceFilter;
import org.genxdm.typed.io.SequenceHandler;
import org.genxdm.typed.types.AtomBridge;
import org.genxdm.typed.types.TypesBridge;
import org.genxdm.xs.enums.DerivationMethod;
import org.genxdm.xs.types.Type;

public class NamespaceFixupSequenceFilter<A> implements SequenceFilter<A> {

    public NamespaceFixupSequenceFilter() {
        methods.add(DerivationMethod.Extension);
        methods.add(DerivationMethod.Restriction);
        methods.add(DerivationMethod.List);
    }

    @Override
    public void attribute(String namespaceURI, String localName, String prefix, List<? extends A> data, QName type) throws GenXDMException {
        PreCondition.assertNotNull(output);
        if (localName.equalsIgnoreCase("xmlns") || ((prefix != null) && prefix.equalsIgnoreCase("xmlns"))) {
            if (atoms != null) namespace(localName.equalsIgnoreCase("xmlns") ? "" : localName, atoms.getC14NString(data));
            return;
        }
        if (localName.toLowerCase().startsWith("xml")) throw new GenXDMException("Invalid attribute name: " + localName);
        String ns = namespaceURI == null ? "" : namespaceURI;
        String p = prefix == null ? "" : prefix;
        if (ns.trim().length() > 0) {
            if (p.trim().length() == 0) {
                Set<String> prefixes = getPrefixesForURI(ns);
                if (prefixes != null) p = prefixes.iterator().next(); else p = randomPrefix(ns);
            }
            required.add(new DefaultNamespaceBinding(p, ns));
        }
        Type t = (type == null) ? BuiltInSchema.SINGLETON.UNTYPED_ATOMIC : types.getComponentProvider().getTypeDefinition(type);
        if ((type != null) && t.derivedFromType(BuiltInSchema.SINGLETON.QNAME, methods)) {
        }
        attributes.add(new Attr(ns, localName, p, data, type));
    }

    @Override
    public void startElement(String namespaceURI, String localName, String prefix, QName type) throws GenXDMException {
        PreCondition.assertNotNull(output);
        reconcile();
        output.startElement(namespaceURI, localName, prefix, type);
        newScope();
        required.add(new DefaultNamespaceBinding(prefix, namespaceURI));
    }

    @Override
    public void text(List<? extends A> data) throws GenXDMException {
        PreCondition.assertNotNull(output);
        reconcile();
        output.text(data);
    }

    @Override
    public void attribute(String namespaceURI, String localName, String prefix, String value, DtdAttributeKind type) throws GenXDMException {
        PreCondition.assertNotNull(output);
        if (localName.equalsIgnoreCase("xmlns") || ((prefix != null) && prefix.equalsIgnoreCase("xmlns"))) {
            namespace(localName.equalsIgnoreCase("xmlns") ? "" : localName, value);
            return;
        }
        if (localName.toLowerCase().startsWith("xml")) throw new GenXDMException("Invalid attribute name: " + localName);
        String ns = namespaceURI == null ? "" : namespaceURI;
        String p = prefix == null ? "" : prefix;
        if (ns.trim().length() > 0) {
            if (p.trim().length() == 0) {
                Set<String> prefixes = getPrefixesForURI(ns);
                if (prefixes != null) p = prefixes.iterator().next(); else p = randomPrefix(ns);
            }
            required.add(new DefaultNamespaceBinding(p, ns));
        }
        attributes.add(new Attr(ns, localName, p, value, type));
    }

    @Override
    public void comment(String value) throws GenXDMException {
        PreCondition.assertNotNull(output);
        reconcile();
        output.comment(value);
    }

    @Override
    public void endDocument() throws GenXDMException {
        PreCondition.assertNotNull(output);
        output.endDocument();
    }

    @Override
    public void endElement() throws GenXDMException {
        PreCondition.assertNotNull(output);
        reconcile();
        output.endElement();
        endScope();
    }

    @Override
    public void namespace(String prefix, String namespaceURI) throws GenXDMException {
        PreCondition.assertNotNull(output);
        if (prefix == null) prefix = XMLConstants.DEFAULT_NS_PREFIX;
        if (namespaceURI == null) namespaceURI = XMLConstants.NULL_NS_URI;
        if (prefix.equals(XMLConstants.XML_NS_PREFIX) && !namespaceURI.equals(XMLConstants.XML_NS_URI)) return;
        if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE) && !namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) return;
        if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) return;
        String boundTo = getDeclaredURI(prefix);
        if ((boundTo != null) && !boundTo.equals(namespaceURI)) {
            throw new GenXDMException("The prefix '" + prefix + "' is already bound to " + boundTo + "and cannot also be bound to " + namespaceURI + ".");
        }
        namespaces.add(new DefaultNamespaceBinding(prefix, namespaceURI));
        scopes.get(depth).put(prefix, namespaceURI);
    }

    @Override
    public void processingInstruction(String target, String data) throws GenXDMException {
        PreCondition.assertNotNull(output);
        reconcile();
        output.processingInstruction(target, data);
    }

    @Override
    public void startDocument(URI documentURI, String docTypeDecl) throws GenXDMException {
        PreCondition.assertNotNull(output);
        output.startDocument(documentURI, docTypeDecl);
    }

    @Override
    public void startElement(String namespaceURI, String localName, String prefix) throws GenXDMException {
        PreCondition.assertNotNull(output);
        reconcile();
        output.startElement(namespaceURI, localName, prefix);
        newScope();
        required.add(new DefaultNamespaceBinding(prefix, namespaceURI));
    }

    @Override
    public void text(String data) throws GenXDMException {
        PreCondition.assertNotNull(output);
        reconcile();
        output.text(data);
    }

    @Override
    public void close() throws IOException {
        PreCondition.assertNotNull(output);
        output.close();
    }

    @Override
    public void flush() throws IOException {
        PreCondition.assertNotNull(output);
        output.flush();
    }

    @Override
    public void setOutputSequenceHandler(SequenceHandler<A> output) {
        this.output = PreCondition.assertNotNull(output);
    }

    @Override
    public void setTypesBridge(TypesBridge bridge) {
        this.types = PreCondition.assertNotNull(bridge);
    }

    @Override
    public void setAtomBridge(AtomBridge<A> bridge) {
        this.atoms = PreCondition.assertNotNull(bridge);
    }

    private void newScope() {
        depth++;
        Map<String, String> scope = new HashMap<String, String>();
        if (depth == 0) {
            scope.put(XMLConstants.DEFAULT_NS_PREFIX, XMLConstants.NULL_NS_URI);
            scope.put(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
            scope.put(XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
        } else {
            scope.putAll(scopes.get(depth - 1));
        }
        scopes.add(scope);
    }

    private void endScope() {
        scopes.remove(depth);
        depth--;
    }

    private void reconcile() {
        for (NamespaceBinding want : required) {
            if (!inScope(want.getPrefix(), want.getNamespaceURI())) {
                namespace(want.getPrefix(), want.getNamespaceURI());
            }
        }
        required.clear();
        for (NamespaceBinding namespace : namespaces) {
            output.namespace(namespace.getPrefix(), namespace.getNamespaceURI());
        }
        namespaces.clear();
        for (Attr a : attributes) {
            if (a.data != null) output.attribute(a.namespace, a.name, a.prefix, a.data, a.typeName); else output.attribute(a.namespace, a.name, a.prefix, a.value, a.type);
        }
        attributes.clear();
    }

    private boolean inScope(String prefix, String uri) {
        Map<String, String> scope = scopes.get(depth);
        String bound = scope.get(prefix);
        if (bound != null) {
            if (uri.equals(bound)) return true;
        }
        return false;
    }

    private String getDeclaredURI(String prefix) {
        for (NamespaceBinding namespace : namespaces) {
            if (namespace.getPrefix().equals(prefix)) return namespace.getNamespaceURI();
        }
        return null;
    }

    private Set<String> getPrefixesForURI(String ns) {
        Set<String> results = new HashSet<String>();
        Map<String, String> scope = scopes.get(depth);
        boolean found = false;
        for (Map.Entry<String, String> binding : scope.entrySet()) {
            if (binding.getValue().equals(ns)) {
                results.add(binding.getKey());
                found = true;
            }
        }
        if (found) return results;
        return null;
    }

    private String randomPrefix(String uri) {
        return "ns" + counter++;
    }

    private class Attr {

        Attr(String namespaceURI, String localName, String prefix, String value, DtdAttributeKind type) {
            this.namespace = namespaceURI;
            this.name = localName;
            this.prefix = prefix;
            this.value = value;
            this.type = type;
        }

        Attr(String namespaceURI, String localName, String prefix, List<? extends A> data, QName type) {
            this.namespace = namespaceURI;
            this.name = localName;
            this.prefix = prefix;
            this.data = data;
            this.typeName = type;
        }

        String namespace;

        String name;

        String prefix;

        String value;

        DtdAttributeKind type;

        List<? extends A> data;

        QName typeName;

        @Override
        public int hashCode() {
            return ("{" + namespace + "}" + name).hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof NamespaceFixupSequenceFilter.Attr) return hashCode() == other.hashCode();
            return false;
        }
    }

    private Set<NamespaceBinding> namespaces = new HashSet<NamespaceBinding>();

    private Set<NamespaceBinding> required = new HashSet<NamespaceBinding>();

    private Set<Attr> attributes = new HashSet<Attr>();

    private List<Map<String, String>> scopes = new ArrayList<Map<String, String>>();

    private int depth = -1;

    private int counter = 0;

    private SequenceHandler<A> output;

    private TypesBridge types;

    private AtomBridge<A> atoms;

    private Set<DerivationMethod> methods = new HashSet<DerivationMethod>(3);
}
