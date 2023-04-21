package xml.parser;

import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.validation.ValidatorHandler;
import java.util.*;
import java.util.regex.*;
import java.net.*;
import org.gjt.sp.util.Log;
import org.gjt.sp.jedit.io.VFSManager;
import org.gjt.sp.jedit.io.VFS;
import org.gjt.sp.jedit.io.VFSFile;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.View;
import javax.swing.SwingUtilities;
import static xml.Debug.*;
import xml.PathUtilities;

/**
 * keeps rules to map a schema to a given instance document.
 * The schema mapping can be serialized in a "schemas.xml" file,
 * compatible with the nXML emacs mode written by James Clark.
 * TODO: some features are still missing (apply following rule)
 * TODO: with XSD, one has to specify one schema per namespace,
 * it is not supported by this implementation.
 *
 * @author Eric Le Lay
 * @version $Id: SchemaMapping.java 21187 2012-02-23 09:28:19Z kerik-sf $
 */
public final class SchemaMapping {

    /** namespace to use for these rules in schemas.xml */
    private static final String LOCATING_RULES_NS = "http://thaiopensource.com/ns/locating-rules/1.0";

    /** default name of the file containing schema mapping rules */
    public static final String SCHEMAS_FILE = "schemas.xml";

    /** type IDs : typeId -> schema URL or typeId -> typeId */
    private final List<TypeIdMapping> typeIds;

    /** mapping rules */
    private final List<Mapping> rules;

    /** this schema mapping file's URI */
    private URI baseURI;

    /**
	 * empty mapping
	 */
    public SchemaMapping() {
        this(null);
    }

    /**
	 * empty mapping
	 * @param	baseURI		url to resolve relative URLs in this document
	 */
    public SchemaMapping(URI baseURI) {
        typeIds = new ArrayList<TypeIdMapping>();
        rules = new ArrayList<Mapping>();
        this.baseURI = baseURI;
    }

    /**
	 * @return this schema mapping's URL or null if it's totally in memory
	 */
    public URI getBaseURI() {
        return baseURI;
    }

    public void insertRuleAt(int pos, Mapping m) {
        m.parent = this;
        if (m.getBaseURI() == null) {
            m.setBaseURI(getBaseURI());
        }
        rules.add(pos, m);
    }

    public void removeRule(Mapping m) {
        if (rules.contains(m)) {
            rules.remove(m);
            m.parent = null;
        }
    }

    public void removeRuleAt(int pos) {
        if (pos >= 0 && pos < rules.size()) {
            rules.get(pos).parent = null;
            rules.remove(pos);
        }
    }

    public void updateMapping(URIResourceRule newRule) {
        if (newRule == null) throw new IllegalArgumentException("newRule==null");
        newRule.parent = this;
        for (int i = 0; i < rules.size(); i++) {
            if ((rules.get(i) instanceof URIResourceRule) && ((URIResourceRule) rules.get(i)).resource.equals(newRule.resource)) {
                if (DEBUG_SCHEMA_MAPPING) Log.log(Log.DEBUG, SchemaMapping.class, "replacing " + rules.get(i) + " by " + newRule);
                rules.set(i, newRule);
                return;
            }
        }
        rules.add(0, newRule);
    }

    /**
	 * @return was there ?
	 */
    public boolean ensureIncluded(SchemaMapping mapping) {
        for (Mapping m : rules) {
            if (m instanceof IncludeMapping) {
                SchemaMapping candidate = ((IncludeMapping) m).mapping;
                if (m.getBaseURI().equals(candidate.getBaseURI())) {
                    return true;
                } else {
                    if (candidate.ensureIncluded(mapping)) return true;
                }
            }
        }
        addRule(new IncludeMapping(null, mapping));
        return false;
    }

    /**
	 * @return typeId for resource or null
	 */
    public String getTypeIdForDocument(String resourceURL) {
        Result r = getSchemaForDocument(null, resourceURL, null, null, null, false);
        if (r == null) return null; else return r.target;
    }

    /**
	 * @param	tid	the type identifier to look up (try "RNG")
	 * @return	URL (eventually, in case of chaining of typeIds)
	 *          associated to this typeId or null if it can't be found.
	 */
    public Result resolveTypeId(String tid) {
        if (DEBUG_SCHEMA_MAPPING) Log.log(Log.DEBUG, SchemaMapping.class, "resolveTypeId(" + tid + ")");
        for (TypeIdMapping m : typeIds) {
            if (m.tid.equals(tid)) {
                if (m.targetIsTypeId) {
                    return resolveTypeId(m.target);
                } else {
                    Result r = new Result(m.getBaseURI(), m.target);
                    if (DEBUG_SCHEMA_MAPPING) Log.log(Log.DEBUG, SchemaMapping.class, " resolvedTypeId : " + r);
                    return r;
                }
            }
        }
        for (Mapping m : rules) {
            if (m instanceof IncludeMapping) {
                Result r = ((IncludeMapping) m).mapping.resolveTypeId(tid);
                if (r != null) return r;
            }
        }
        Log.log(Log.WARNING, SchemaMapping.class, "couldn't find typeId '" + tid + "'");
        return null;
    }

    /**
	 * manually add a rule
	 * @param	r	rule to add
	 */
    public void addRule(Mapping r) {
        insertRuleAt(rules.size(), r);
    }

    /**
	 * manually add a typeId mapping.
	 * If the typeId mapping exists already, it is overriden
	 *
	 * @param	tid		typeId
	 * @param	target	target url or typeId
	 * @param	targetIsTypeId	is the target itself a type id ?
	 */
    public void addTypeId(String tid, String target, boolean targetIsTypeId) {
        TypeIdMapping tidm = new TypeIdMapping(getBaseURI(), tid, target, targetIsTypeId);
        typeIds.add(tidm);
    }

    public List<TypeIdMapping> getTypeIds() {
        return Collections.unmodifiableList(typeIds);
    }

    public List<Mapping> getRules() {
        return Collections.unmodifiableList(rules);
    }

    /**
	 * iterate over the mappings and return the first hit.
	 * all the parameters are given the same priority : it's really the ordering
	 * of rules which defines a priority order.
	 * Any of the paremeters can be null.
	 * @param	publicId	public ID of the parsed document
	 * @param	systemId	system ID of the parsed document
	 * @param	namespace	namespace of the root element of the parsed document
	 * @param	prefix		prefix of the root element of the parsed document
	 * @param	localName	localName of the root element of the parsed document
	 * @param	followTypeId	if the schema referenced from the typeId should be returned
	 * @return	schema URL for given document or null if not found
	 */
    public Result getSchemaForDocument(String publicId, String systemId, String namespace, String prefix, String localName, boolean followTypeId) {
        if (DEBUG_SCHEMA_MAPPING) Log.log(Log.DEBUG, SchemaMapping.class, "getSchemaForDocumentElement(" + publicId + "," + systemId + "," + namespace + "," + prefix + "," + localName + ")");
        Result res = null;
        for (Mapping r : rules) {
            res = r.getSchemaForDocument(publicId, systemId, namespace, prefix, localName, followTypeId);
            if (res != null) break;
        }
        if (res != null) {
            if (DEBUG_SCHEMA_MAPPING) Log.log(Log.DEBUG, SchemaMapping.class, "found: " + res);
        }
        return res;
    }

    /**
	 * iterate over the mappings and return the first hit.
	 * all the parameters are given the same priority : it's really the ordering
	 * of rules which defines a priority order.
	 * Any of the paremeters can be null.
	 * @param	publicId	public ID of the parsed document
	 * @param	systemId	system ID of the parsed document
	 * @param	namespace	namespace of the root element of the parsed document
	 * @param	prefix		prefix of the root element of the parsed document
	 * @param	localName	localName of the root element of the parsed document
	 * @return	matching mapping
	 */
    public Mapping getMappingForDocument(String publicId, String systemId, String namespace, String prefix, String localName) {
        if (DEBUG_SCHEMA_MAPPING) Log.log(Log.DEBUG, SchemaMapping.class, "getMappingForDocument(" + publicId + "," + systemId + "," + namespace + "," + prefix + "," + localName + ")");
        Mapping res = null;
        for (Mapping r : rules) {
            res = r.getMappingForDocument(publicId, systemId, namespace, prefix, localName);
            if (res != null) break;
        }
        return res;
    }

    /**
	 * prefix + localName -> typeId or URL
	 */
    public static class DocumentElementRule extends Rule {

        /** matched prefix (can be null) */
        private String prefix;

        /** matched local name (can be null) */
        private String localName;

        /**
		 * @param	prefix	matched prefix (can be null)
		 * @param	localName	matched local name (can be null)
		 * @param	target	typeID or URL
		 * @param	targetIsTypeId	typeID / URL ?
		 */
        public DocumentElementRule(URI baseURI, String prefix, String localName, String target, boolean targetIsTypeId) {
            super(baseURI, target, targetIsTypeId);
            if ((prefix == null || "".equals(prefix)) && (localName == null || "".equals(localName))) throw new IllegalArgumentException("prefix and localName can't both be null");
            if ("".equals(localName)) localName = null;
            this.prefix = prefix;
            this.localName = localName;
        }

        @Override
        boolean matchDocumentElement(String prefix, String localName) {
            return (this.prefix == null || this.prefix.equals(prefix)) && (this.localName == null || this.localName.equals(localName));
        }

        /** @return xml serialization */
        public String toString() {
            return "<documentElement " + (prefix != null ? ("prefix=\"" + prefix + "\" ") : "") + (localName != null ? ("localName=\"" + localName + "\" ") : "") + (base == null ? "" : "xml:base=\"" + base + "\" ") + (targetIsTypeId ? "typeId" : "uri") + "=\"" + target + "\"/>";
        }
    }

    /** namespace -> typeId or URL */
    public static class NamespaceRule extends Rule {

        /** matched namespace */
        private String namespace;

        /**
		 * @param	ns	matched namespace
		 * @param	target	typeID or URL
		 * @param	targetIsTypeId	typeID / URL ?
		 * @throws	IllegalArgumentException	if ns is null
		 */
        public NamespaceRule(URI baseURI, String ns, String target, boolean targetIsTypeId) {
            super(baseURI, target, targetIsTypeId);
            if (ns == null) throw new IllegalArgumentException("namespace can't be null");
            namespace = ns;
        }

        @Override
        boolean matchNamespace(String namespace) {
            return this.namespace.equals(namespace);
        }

        /** @return xml serialization */
        public String toString() {
            return "<namespace ns=\"" + namespace + "\" " + (base == null ? "" : "xml:base=\"" + base + "\" ") + (targetIsTypeId ? "typeId" : "uri") + "=\"" + target + "\"/>";
        }
    }

    /** URI pattern -> typeID or URL.
	 * The pattern is in glob syntax :
	 *  - star matches any sequence of character except slashes
	 *  - dot really means a dot.
	 * e.g. :
	 *  *.rng -> relax NG schema
	 *  *.xsd -> Schema for Schemas
	 */
    public static class URIPatternRule extends Rule {

        /** matched pattern for toString() */
        private String pattern;

        /** compiled whole pattern */
        private Pattern compPattern;

        /**
		 * @param	pattern	matched pattern
		 * @param	target	typeID or URL
		 * @param	targetIsTypeId	typeID / URL ?
		 * @throws	IllegalArgumentException if pattern is null
		 */
        public URIPatternRule(URI baseURI, String pattern, String target, boolean targetIsTypeId) {
            super(baseURI, target, targetIsTypeId);
            if (pattern == null) throw new IllegalArgumentException("pattern can't be null");
            this.pattern = pattern;
            this.compPattern = Pattern.compile("^.*?" + pattern.replace(".", "\\.").replace("*", "[^/]*"));
        }

        /**
		 * TODO: what if pattern is absolute and url is relative ?
		 * TODO: patterns with .. won't be correctly resolved
		 */
        @Override
        boolean matchURL(String url) {
            try {
                URI u = new URI(url).normalize();
                if (u.isOpaque()) {
                    return compPattern.matcher(url).matches();
                } else {
                    return compPattern.matcher(url).matches();
                }
            } catch (URISyntaxException use) {
                Log.log(Log.WARNING, SchemaMapping.class, "error looking for matching schema for " + url + ", invalid url: " + use.getMessage());
                return compPattern.matcher(url).matches();
            }
        }

        /**@return xml serialization */
        @Override
        public String toString() {
            return "<uri pattern=\"" + pattern + "\" " + (base == null ? "" : "xml:base=\"" + base + "\" ") + (targetIsTypeId ? "typeId" : "uri") + "=\"" + target + "\"/>";
        }
    }

    /** URI pattern -> related URL.
	 * The pattern is in glob syntax :
	 *  - star matches any sequence of character except slashes
	 *  - dot really means a dot.
	 * Any text matched by * is inserted in place of the corresponding *
	 * in the destination pattern
	 * e.g. :
	 *  *.xml -> *.xsd
	 *  *.xml -> *.rng
	 */
    public static class TransformURI extends Mapping {

        /** matched pattern for toString() */
        private String fromPattern;

        /** compiled whole pattern */
        private Pattern compPattern;

        private String toPattern;

        /** compiled toPattern */
        private String[] splitToPattern;

        private int starCount;

        /**
		 * @param	baseURI	baseURI to use if resource is relative
		 * @param	fromPattern	matched pattern
		 * @param	toPattern	result pattern
		 * @throws	IllegalArgumentException if pattern is null
		 */
        public TransformURI(URI baseURI, String fromPattern, String toPattern) {
            super(baseURI);
            if (fromPattern == null) throw new IllegalArgumentException("fromPattern can't be null");
            if (toPattern == null) throw new IllegalArgumentException("toPattern can't be null");
            this.fromPattern = fromPattern;
            this.toPattern = toPattern;
            for (int i = fromPattern.indexOf('*', 0); i != -1; i = fromPattern.indexOf('*', i + 1)) {
                starCount++;
            }
            compPattern = Pattern.compile("(^.*?)" + fromPattern.replace(".", "\\.").replace("*", "([^/]*)"));
            splitToPattern = toPattern.split("\\*", -1);
            if (splitToPattern.length > starCount + 1) {
                throw new IllegalArgumentException("there may not be more stars in toPattern (='" + toPattern + ") than in fromPattern (=" + fromPattern + ") " + (splitToPattern.length - 1) + ">" + starCount);
            }
        }

        /**
		 * TODO: what if pattern is absolute and url is relative ?
		 * TODO: patterns with .. won't be correctly resolved
		 */
        @Override
        public Result getSchemaForDocument(String ignoredPublicId, String url, String ignoredNamespace, String ignoredPrefix, String ignoredLocalName, boolean ignored) {
            try {
                Matcher m = compPattern.matcher(url);
                if (m.matches()) {
                    String result = m.group(1);
                    StringBuilder resultSb = new StringBuilder(result);
                    if (splitToPattern.length > 0) {
                        resultSb.append(splitToPattern[0]);
                        for (int i = 0; i < starCount; i++) {
                            resultSb.append(m.group(i + 2));
                            if (i + 1 < splitToPattern.length) resultSb.append(splitToPattern[i + 1]);
                        }
                    }
                    result = resultSb.toString();
                    URI resultURI;
                    if (getBaseURI() == null) {
                        resultURI = new URI(result);
                    } else {
                        resultURI = getBaseURI().resolve(result);
                    }
                    if (resourceExists(resultURI)) {
                        return new Result(getBaseURI(), result);
                    } else {
                        if (DEBUG_SCHEMA_MAPPING) Log.log(Log.DEBUG, SchemaMapping.class, "resource '" + result + "' not found for '" + url + "'");
                    }
                }
            } catch (URISyntaxException use) {
                Log.log(Log.ERROR, SchemaMapping.class, "Malformed:" + url);
            }
            return null;
        }

        @Override
        public Mapping getMappingForDocument(String ignoredPublicId, String url, String ignoredNamespace, String ignoredPrefix, String ignoredLocalName) {
            if (getSchemaForDocument(ignoredPublicId, url, ignoredNamespace, ignoredPrefix, ignoredLocalName, false) == null) {
                return null;
            } else {
                return this;
            }
        }

        /**@return xml serialization */
        @Override
        public String toString() {
            return "<transformURI " + (base == null ? "" : "xml:base=\"" + base + "\" ") + "fromPattern=\"" + fromPattern + "\" toPattern=\"" + toPattern + "\"/>";
        }
    }

    /**
	 * URI -> typeId or URL.
	 * This form is useful to map one schema to one particular file.
	 * e.g. : file:///tmp/myfile.xml  -> mycustomschema.xsd
	 */
    public static class URIResourceRule extends Rule {

        /** matched URI */
        private final String resource;

        private final URI resURL;

        /**
		 * @param	base	baseURI to use if resource is relative
		 * @param	resource	matched resource
		 * @param	target	typeID or URL
		 * @param	targetIsTypeId	typeID / URL ?
		 * @throws	IllegalArgumentException	if resource is null
		 */
        public URIResourceRule(URI base, String resource, String target, boolean targetIsTypeId) {
            super(base, target, targetIsTypeId);
            if (resource == null) throw new IllegalArgumentException("resource can't be null");
            try {
                URI u = new URI(resource).normalize();
                if (u.isAbsolute()) {
                    resURL = u;
                } else {
                    if (base == null) throw new IllegalArgumentException("base can't be null when resource is relative :" + resource);
                    resURL = base.resolve(resource);
                }
            } catch (URISyntaxException use) {
                throw new IllegalArgumentException("resource '" + resource + "' must be a valid url", use);
            }
            this.resource = resource;
        }

        /**
		 * relative url is resolved against the current baseURI...
		 */
        @Override
        boolean matchURL(String url) {
            if (resURL != null) {
                try {
                    URI matchedURI = new URI(url).normalize();
                    URI matchedURL;
                    if (matchedURI.isAbsolute()) {
                        matchedURL = matchedURI;
                    } else {
                        matchedURL = resURL.resolve(matchedURI);
                    }
                    return resURL.equals(matchedURL);
                } catch (URISyntaxException use) {
                    Log.log(Log.WARNING, SchemaMapping.class, "error looking for matching schema for " + url + ", invalid URL: " + use.getMessage());
                }
            }
            return resource.equals(url);
        }

        /** xml serialization */
        @Override
        public String toString() {
            return "<uri resource=\"" + resource + "\" " + (base == null ? "" : "xml:base=\"" + base + "\" ") + (targetIsTypeId ? "typeId" : "uri") + "=\"" + target + "\"/>";
        }
    }

    /**
	 * always matches : not really useful for us, is it ?
	 */
    public static class DefaultRule extends Rule {

        public DefaultRule(URI baseURI, String target, boolean targetIsTypeId) {
            super(baseURI, target, targetIsTypeId);
        }

        @Override
        boolean matchNamespace(String namespace) {
            return true;
        }

        @Override
        boolean matchDocumentElement(String prefix, String localName) {
            return true;
        }

        @Override
        boolean matchURL(String url) {
            return true;
        }

        @Override
        boolean matchDoctype(String dt) {
            return true;
        }

        @Override
        public String toString() {
            return "<default " + (base == null ? "" : "xml:base=\"" + base + "\" ") + (targetIsTypeId ? "typeId" : "uri") + "=\"" + target + "\"/>";
        }
    }

    /**
	 * doctype -> typeID or URL.
	 * not used yet.
	 */
    public static class DoctypeRule extends Rule {

        /** matched doctype */
        private String doctype;

        /**
		 * @param	doctype	matched doctype
		 * @param	target	typeID or URL
		 * @param	targetIsTypeId	typeID / URL ?
		 * @throws	IllegalArgumentException	if doctype is null
		 */
        public DoctypeRule(URI baseURI, String doctype, String target, boolean targetIsTypeId) {
            super(baseURI, target, targetIsTypeId);
            if (doctype == null) throw new IllegalArgumentException("doctype can't be null");
            if (doctype.length() == 0) throw new IllegalArgumentException("doctype can't be \"\"");
            this.doctype = doctype;
        }

        @Override
        boolean matchDoctype(String dt) {
            return doctype.equals(dt);
        }

        /**@return xml serialization */
        @Override
        public String toString() {
            return "<doctypePublicId publicId=\"" + doctype + "\" " + (base == null ? "" : "xml:base=\"" + base + "\" ") + (targetIsTypeId ? "typeId" : "uri") + "=\"" + target + "\"/>";
        }
    }

    /**
	 * base class for all mapping rules.
	 * this allows to keep a list of the rules and try each of them,
	 * regardless of their type.
	 */
    abstract static class Rule extends Mapping {

        /** typeId or URL of the schema to use if this rule matches */
        protected final String target;

        /** for serialisation : output typeId="..." or url="..." */
        protected final boolean targetIsTypeId;

        /**
		 * @param	target	typeID or URL
		 * @param	targetIsTypeId	typeID / URL ?
		 * @throws	IllegalArgumentException	if target is null
		 */
        Rule(URI baseURI, String target, boolean targetIsTypeId) {
            super(baseURI);
            if (target == null) throw new IllegalArgumentException("target can't be null");
            if ("".equals(target)) throw new IllegalArgumentException("target can't be \"\"");
            this.target = target;
            this.targetIsTypeId = targetIsTypeId;
        }

        /**
		 * does the rule match for this namespace of the root element ?
		 * @return false
		 */
        boolean matchNamespace(String namespace) {
            return false;
        }

        /**
		 * does the rule match for this prefix and local name of the root element ?
		 * @return false
		 */
        boolean matchDocumentElement(String prefix, String localName) {
            return false;
        }

        /**
		 * does the rule match for this url of the document ?
		 * @return false
		 */
        boolean matchURL(String url) {
            return false;
        }

        /**
		 * does the rule match for this doctype of the document ?
		 * @return false
		 */
        boolean matchDoctype(String dt) {
            return false;
        }

        public final Result getSchemaForDocument(String publicId, String systemId, String namespace, String prefix, String localName, boolean followTypeId) {
            if (matchURL(systemId) || matchNamespace(namespace) || matchDocumentElement(prefix, localName)) {
                if (DEBUG_SCHEMA_MAPPING) Log.log(Log.DEBUG, SchemaMapping.class, "matching rule : " + this);
                if (targetIsTypeId && followTypeId) {
                    return parent.resolveTypeId(target);
                } else {
                    return new Result(getBaseURI(), target);
                }
            } else {
                return null;
            }
        }

        public final Mapping getMappingForDocument(String publicId, String systemId, String namespace, String prefix, String localName) {
            if (matchURL(systemId) || matchNamespace(namespace) || matchDocumentElement(prefix, localName)) {
                return this;
            } else {
                return null;
            }
        }
    }

    /** deserialize an xml document describing the mapping rules (schemas.xml)
	 * @param	url	url of the document to read
	 * @return	new SchemaMapping taken from the document at url, or null
	 * @throws	IllegalArgumentException	if the url is null
	 */
    public static SchemaMapping fromDocument(String url) {
        if (DEBUG_SCHEMA_MAPPING) Log.log(Log.DEBUG, SchemaMapping.class, "fromDocument(" + url + ")");
        if (url == null) throw new IllegalArgumentException("url can't be null");
        final SchemaMapping mapping = new SchemaMapping();
        try {
            mapping.baseURI = new URI(url);
        } catch (URISyntaxException mfue) {
            throw new IllegalArgumentException("malformed URL: " + url, mfue);
        }
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setEntityResolver(xml.Resolver.instance());
            DefaultHandler handler = new MyHandler(mapping);
            ValidatorHandler verifierFilter = SchemaLoader.instance().loadJaxpGrammar(null, "jeditresource:XML.jar!/xml/dtds/locate.rng", handler, null);
            reader.setContentHandler(verifierFilter);
            verifierFilter.setContentHandler(handler);
            verifierFilter.setErrorHandler(handler);
            InputSource input = xml.Resolver.instance().resolveEntity(null, url);
            reader.parse(input);
        } catch (SAXException se) {
            String msg = "error loading schema mapping '" + url + "'";
            Throwable t = se.getException();
            if (msg != null) {
                msg += ": " + se.getMessage();
            }
            if (t != null) {
                msg += " caused by " + t;
            }
            Log.log(Log.WARNING, SchemaMapping.class, msg);
        } catch (IOException e) {
            Log.log(Log.ERROR, SchemaMapping.class, "I/O error loading schema mapping '" + url + "': " + e.getClass() + ": " + e.getMessage());
        }
        return mapping;
    }

    /**
	 * serialize to an XML document
	 * @param	output	url to the output file
	 * @throws	IOException	if there is an error during serialization
	 * FIXME: serialized document could be invalid. Is it worth using proper XML serialization ?
	 */
    public void toDocument(final String output) throws IOException {
        if (output == null) throw new IllegalArgumentException("url can't be null");
        final URI resource;
        try {
            resource = new URI(output);
        } catch (URISyntaxException ue) {
            throw new IllegalArgumentException("malformed URL: " + output, ue);
        }
        String scheme = resource.getScheme();
        if (scheme == null) scheme = "file";
        final VFS vfs = VFSManager.getVFSForProtocol(scheme);
        final Object[] sessionArray = new Object[1];
        final View view = jEdit.getActiveView();
        Runnable run = new Runnable() {

            public void run() {
                sessionArray[0] = vfs.createVFSSession(output, view);
                Object session = sessionArray[0];
                if (session != null) {
                    try {
                        OutputStream vfsos = vfs._createOutputStream(session, PathUtilities.urlToPath(resource.toString()), view);
                        Writer out = new OutputStreamWriter(vfsos, "UTF-8");
                        out.write("<?xml version=\"1.0\" ?>\n");
                        out.write("<locatingRules xmlns=\"http://thaiopensource.com/ns/locating-rules/1.0\">\n");
                        for (Mapping r : rules) {
                            out.write(r.toString());
                        }
                        for (TypeIdMapping tid : typeIds) {
                            out.write(tid.toString());
                        }
                        out.write("</locatingRules>");
                        out.close();
                    } catch (IOException ioe) {
                        Log.log(Log.ERROR, SchemaMapping.class, "unable to write document: " + ioe.getMessage());
                    } finally {
                        try {
                            vfs._endVFSSession(session, view);
                        } catch (IOException ioe) {
                            Log.log(Log.ERROR, SchemaMapping.class, "ending VFS session in toDocument(" + output + ")", ioe);
                        }
                    }
                }
            }
        };
        SwingUtilities.invokeLater(run);
    }

    private static class MyHandler extends DefaultHandler {

        private SchemaMapping mapping;

        private Stack<URI> baseURIs;

        MyHandler(SchemaMapping mapping) {
            this.mapping = mapping;
            baseURIs = new Stack<URI>();
            baseURIs.push(mapping.baseURI);
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (!LOCATING_RULES_NS.equals(uri)) return;
            String base = null;
            if (attributes.getIndex("xml:base") != -1) {
                base = attributes.getValue("xml:base");
                try {
                    URI buri = new URI(base);
                    URI burl;
                    if (buri.isAbsolute()) {
                        burl = buri;
                    } else {
                        burl = baseURIs.peek().resolve(buri);
                    }
                    baseURIs.push(burl);
                } catch (URISyntaxException use) {
                    throw new SAXException("invalid xml:base " + base, use);
                }
            } else {
                baseURIs.push(baseURIs.peek());
            }
            String target = null;
            boolean targetIsTypeId = false;
            if (attributes.getIndex("", "typeId") != -1) {
                target = attributes.getValue("", "typeId");
                targetIsTypeId = true;
            } else if (attributes.getIndex("", "uri") != -1) {
                target = attributes.getValue("", "uri");
                targetIsTypeId = false;
            }
            Mapping newRule = null;
            if ("transformURI".equals(localName)) {
                if (attributes.getIndex("", "fromPattern") != -1 && attributes.getIndex("", "toPattern") != -1) {
                    String from = attributes.getValue("", "fromPattern");
                    String to = attributes.getValue("", "toPattern");
                    newRule = new TransformURI(baseURIs.peek(), from, to);
                }
            } else if ("uri".equals(localName)) {
                if (attributes.getIndex("", "pattern") != -1) {
                    newRule = new URIPatternRule(baseURIs.peek(), attributes.getValue("", "pattern"), target, targetIsTypeId);
                } else if (attributes.getIndex("", "resource") != -1) {
                    newRule = new URIResourceRule(baseURIs.peek(), attributes.getValue("", "resource"), target, targetIsTypeId);
                }
            } else if ("namespace".equals(localName)) {
                if (attributes.getIndex("", "ns") != -1) {
                    newRule = new NamespaceRule(baseURIs.peek(), attributes.getValue("", "ns"), target, targetIsTypeId);
                }
            } else if ("documentElement".equals(localName)) {
                String prefix = attributes.getValue("", "prefix");
                String name = attributes.getValue("", "localName");
                newRule = new DocumentElementRule(baseURIs.peek(), prefix, name, target, targetIsTypeId);
            } else if ("doctypePublicId".equals(localName)) {
                newRule = new DoctypeRule(baseURIs.peek(), attributes.getValue("", "publicId"), target, targetIsTypeId);
            } else if ("include".equals(localName)) {
                if (attributes.getIndex("", "rules") != -1) {
                    newRule = new IncludeMapping(baseURIs.peek(), attributes.getValue("", "rules"));
                }
            } else if ("default".equals(localName)) {
                newRule = new DefaultRule(baseURIs.peek(), target, targetIsTypeId);
            } else if ("typeId".equals(localName) && attributes.getIndex("", "id") != -1) {
                String id = attributes.getValue("", "id");
                TypeIdMapping tid = new TypeIdMapping(baseURIs.peek(), id, target, targetIsTypeId);
                mapping.typeIds.add(tid);
            }
            if (newRule != null) {
                if (base != null) newRule.setExplicitBase(base);
                newRule.parent = mapping;
                mapping.rules.add(newRule);
            }
        }

        public void endElement(String uri, String localName, String qName) {
            baseURIs.pop();
        }
    }

    public abstract static class Mapping {

        /** parent of this Mapping */
        protected SchemaMapping parent;

        /** this schema mapping file's URI */
        protected URI baseURI;

        /** explicit xml:base, if any */
        protected String base;

        Mapping(URI baseURI) {
            this.baseURI = baseURI;
            this.parent = null;
        }

        public SchemaMapping getParent() {
            return parent;
        }

        /**
		 * @return this schema mapping's URL or null if it's totally in memory
		 */
        public URI getBaseURI() {
            return baseURI;
        }

        /**
		 * for containing SchemaMapping 
		 */
        void setBaseURI(URI baseURI) {
            this.baseURI = baseURI;
        }

        void setExplicitBase(String base) {
            this.base = base;
        }

        /**
		 * iterate over the mappings and return the first hit.
		 * all the parameters are given the same priority : it's really the ordering
		 * of rules which defines a priority order.
		 * Any of the paremeters can be null.
		 * @param	publicId	public ID of the parsed document
		 * @param	systemId	system ID of the parsed document
		 * @param	namespace	namespace of the root element of the parsed document
		 * @param	prefix		prefix of the root element of the parsed document
		 * @param	localName	localName of the root element of the parsed document
		 * @param	followTypeId	if the schema referenced from a typeId must be returned
		 * @return	schema URL for given document (and baseURI) or null if not found
		 */
        public abstract Result getSchemaForDocument(String publicId, String systemId, String namespace, String prefix, String localName, boolean followTypeId);

        /**
		 * iterate over the mappings and return the first hit.
		 * all the parameters are given the same priority : it's really the ordering
		 * of rules which defines a priority order.
		 * Any of the paremeters can be null.
		 * @param	publicId	public ID of the parsed document
		 * @param	systemId	system ID of the parsed document
		 * @param	namespace	namespace of the root element of the parsed document
		 * @param	prefix		prefix of the root element of the parsed document
		 * @param	localName	localName of the root element of the parsed document
		 * @return	schema URL for given document (and baseURI) or null if not found
		 */
        public abstract Mapping getMappingForDocument(String publicId, String systemId, String namespace, String prefix, String localName);
    }

    public static class IncludeMapping extends Mapping {

        private final String rules;

        private final SchemaMapping mapping;

        /**
		 * @param	baseURI	base URL to resolve rules (may be null if rules is absolute)
		 * @param	rules	URL where to find the rules
		 * @throws	IllegalArgumentException	if the url is malformed
		 */
        public IncludeMapping(URI baseURI, String rules) {
            super(baseURI);
            if (rules == null) throw new IllegalArgumentException("rules can't be null");
            try {
                URI u = new URI(rules);
                URI url;
                if (u.isAbsolute()) {
                    url = u;
                } else {
                    if (baseURI == null) throw new IllegalArgumentException("relative rules '" + rules + "' with null baseURI"); else url = baseURI.resolve(rules);
                }
                mapping = SchemaMapping.fromDocument(url.toURL().toExternalForm());
            } catch (MalformedURLException mfue) {
                throw new IllegalArgumentException("rules '" + rules + "' must be an URL", mfue);
            } catch (URISyntaxException use) {
                throw new IllegalArgumentException("rules '" + rules + "' must be an URL", use);
            }
            this.rules = rules;
        }

        /**
		 * @param	baseURI	base URL to resolve rules (may be null if rules is absolute)
		 * @param	mapping	mapping to link to
		 * @throws	IllegalArgumentException	if mapping is null
		 */
        public IncludeMapping(URI baseURI, SchemaMapping mapping) {
            super(baseURI);
            if (mapping == null) throw new IllegalArgumentException("mapping can't be null");
            this.mapping = mapping;
            this.rules = mapping.getBaseURI().toString();
        }

        @Override
        public Result getSchemaForDocument(String publicId, String systemId, String namespace, String prefix, String localName, boolean followTypeId) {
            return mapping.getSchemaForDocument(publicId, systemId, namespace, prefix, localName, followTypeId);
        }

        @Override
        public Mapping getMappingForDocument(String publicId, String systemId, String namespace, String prefix, String localName) {
            return mapping.getMappingForDocument(publicId, systemId, namespace, prefix, localName);
        }

        /**@return xml serialization */
        @Override
        public String toString() {
            return "<include rules=\"" + rules + "\" " + (base == null ? "" : "xml:base=\"" + base + "\"") + "/>";
        }
    }

    public static final class Result {

        public final URI baseURI;

        public final String target;

        public Result(URI baseURI, String target) {
            this.baseURI = baseURI;
            this.target = target;
        }

        public String toString() {
            return "(base=" + baseURI + ") " + target;
        }

        public int hashCode() {
            return 1253 + (baseURI == null ? 0 : baseURI.hashCode()) + (target == null ? 0 : target.hashCode());
        }

        public boolean equals(Object other) {
            if (other == this) return true;
            if (other instanceof Result) {
                Result o = (Result) other;
                return ((baseURI == null && o.baseURI == null) || baseURI.equals(o.baseURI)) && ((target == null && o.target == null) || target.equals(o.target));
            } else return false;
        }
    }

    /**
	 * models the typeId element in schemas.xml
	 */
    public static final class TypeIdMapping {

        final String tid;

        final String target;

        final boolean targetIsTypeId;

        final URI baseURI;

        TypeIdMapping(URI baseURI, String tid, String target, boolean targetIsTypeId) {
            this.baseURI = baseURI;
            this.tid = tid;
            this.target = target;
            this.targetIsTypeId = targetIsTypeId;
        }

        public URI getBaseURI() {
            return baseURI;
        }

        public String getId() {
            return tid;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("<typeId id=\"").append(tid).append("\" ");
            if (targetIsTypeId) {
                sb.append("typeId");
            } else {
                sb.append("uri");
            }
            sb.append("=\"").append(target).append("\"/>\n");
            return sb.toString();
        }
    }

    /**
	 * try to use the VFS to detect if a resource exists
	 */
    public static boolean resourceExists(final URI resource) {
        if (DEBUG_SCHEMA_MAPPING) Log.log(Log.DEBUG, SchemaMapping.class, "resourceExists(" + resource + ")");
        VFSFile f = null;
        String scheme = resource.getScheme();
        if (scheme == null) scheme = "file";
        final VFS vfs = VFSManager.getVFSForProtocol(scheme);
        final Object[] sessionArray = new Object[1];
        final View view = jEdit.getActiveView();
        sessionArray[0] = vfs.createVFSSession(resource.toString(), view);
        Object session = sessionArray[0];
        if (session != null) {
            try {
                f = vfs._getFile(session, PathUtilities.urlToPath(resource.toString()), view);
            } catch (IOException ioe) {
                Log.log(Log.ERROR, SchemaMapping.class, "error in resourceExists(" + resource + ")", ioe);
            }
            try {
                vfs._endVFSSession(session, view);
            } catch (IOException ioe) {
                Log.log(Log.ERROR, SchemaMapping.class, "ending VFS session in resourceExists(" + resource + ")", ioe);
            }
        }
        return f != null;
    }
}
