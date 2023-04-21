package net.sf.saxon.expr.instruct;

import net.sf.saxon.Configuration;
import net.sf.saxon.Controller;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.event.SequenceReceiver;
import net.sf.saxon.expr.*;
import net.sf.saxon.expr.sort.IntHashMap;
import net.sf.saxon.expr.sort.IntIterator;
import net.sf.saxon.functions.EscapeURI;
import net.sf.saxon.lib.*;
import net.sf.saxon.om.*;
import net.sf.saxon.pattern.EmptySequenceTest;
import net.sf.saxon.trace.ExpressionPresenter;
import net.sf.saxon.trans.Err;
import net.sf.saxon.trans.SaxonErrorCode;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.ItemType;
import net.sf.saxon.type.SchemaType;
import net.sf.saxon.type.Type;
import net.sf.saxon.type.TypeHierarchy;
import net.sf.saxon.value.Whitespace;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Stack;

/**
 * The compiled form of an xsl:result-document element in the stylesheet.
 * <p/>
 * The xsl:result-document element takes an attribute href="filename". The filename will
 * often contain parameters, e.g. {position()} to ensure that a different file is produced
 * for each element instance.
 * <p/>
 * There is a further attribute "format" which determines the format of the
 * output file, it identifies the name of an xsl:output element containing the output
 * format details. In addition, individual serialization properties may be specified as attributes.
 * These are attribute value templates, so they may need to be computed at run-time.
 */
public class ResultDocument extends Instruction implements DivisibleInstruction {

    private Expression href;

    private Expression formatExpression;

    private Expression content;

    private Properties globalProperties;

    private Properties localProperties;

    private String baseURI;

    private int validationAction;

    private SchemaType schemaType;

    private IntHashMap<Expression> serializationAttributes;

    private NamespaceResolver nsResolver;

    private Expression dynamicOutputElement;

    private boolean resolveAgainstStaticBase = false;

    /**
     * Create a result-document instruction
     * @param globalProperties        properties defined on static xsl:output
     * @param localProperties         non-AVT properties defined on result-document element
     * @param href                    href attribute of instruction
     * @param formatExpression        format attribute of instruction
     * @param baseURI                 base URI of the instruction
     * @param validationAction        for example {@link net.sf.saxon.lib.Validation#STRICT}
     * @param schemaType              schema type against which output is to be validated
     * @param serializationAttributes computed local properties
     * @param nsResolver              namespace resolver
     */
    public ResultDocument(Properties globalProperties, Properties localProperties, Expression href, Expression formatExpression, String baseURI, int validationAction, SchemaType schemaType, IntHashMap<Expression> serializationAttributes, NamespaceResolver nsResolver) {
        this.globalProperties = globalProperties;
        this.localProperties = localProperties;
        this.href = href;
        this.formatExpression = formatExpression;
        this.baseURI = baseURI;
        this.validationAction = validationAction;
        this.schemaType = schemaType;
        this.serializationAttributes = serializationAttributes;
        this.nsResolver = nsResolver;
        adoptChildExpression(href);
        for (Iterator it = serializationAttributes.valueIterator(); it.hasNext(); ) {
            adoptChildExpression((Expression) it.next());
        }
    }

    /**
     * Set the expression that constructs the content
     * @param content the expression defining the content of the result document
     */
    public void setContentExpression(Expression content) {
        this.content = content;
        adoptChildExpression(content);
    }

    /**
     * Get the expression that constructs the content
     * @return the content expression
     */
    public Expression getContentExpression() {
        return content;
    }

    /**
     * Set an expression that evaluates to a run-time xsl:output element, used in the saxon:result-document()
     * extension function designed for use in XQuery
     * @param exp the expression whose result should be an xsl:output element
     */
    public void setDynamicOutputElement(Expression exp) {
        dynamicOutputElement = exp;
    }

    /**
     * Set whether the the instruction should resolve the href relative URI against the static
     * base URI (rather than the dynamic base output URI)
     * @param staticBase set to true by fn:put(), to resolve against the static base URI of the query.
     *                   Default is false, which causes resolution against the base output URI obtained dynamically
     *                   from the Controller
     */
    public void setUseStaticBaseUri(boolean staticBase) {
        resolveAgainstStaticBase = staticBase;
    }

    /**
     * Simplify an expression. This performs any static optimization (by rewriting the expression
     * as a different expression). The default implementation does nothing.
     * @param visitor an expression visitor
     * @return the simplified expression
     * @throws net.sf.saxon.trans.XPathException
     *          if an error is discovered during expression rewriting
     */
    public Expression simplify(ExpressionVisitor visitor) throws XPathException {
        content = visitor.simplify(content);
        href = visitor.simplify(href);
        for (IntIterator it = serializationAttributes.keyIterator(); it.hasNext(); ) {
            int key = it.next();
            Expression value = serializationAttributes.get(key);
            if (!(value instanceof Literal)) {
                value = visitor.simplify(value);
                serializationAttributes.put(key, value);
            }
        }
        return this;
    }

    public Expression typeCheck(ExpressionVisitor visitor, ItemType contextItemType) throws XPathException {
        content = visitor.typeCheck(content, contextItemType);
        adoptChildExpression(content);
        if (href != null) {
            href = visitor.typeCheck(href, contextItemType);
            adoptChildExpression(href);
        }
        if (formatExpression != null) {
            formatExpression = visitor.typeCheck(formatExpression, contextItemType);
            adoptChildExpression(formatExpression);
        }
        for (IntIterator it = serializationAttributes.keyIterator(); it.hasNext(); ) {
            int key = it.next();
            Expression value = serializationAttributes.get(key);
            if (!(value instanceof Literal)) {
                value = visitor.typeCheck(value, contextItemType);
                adoptChildExpression(value);
                serializationAttributes.put(key, value);
            }
        }
        try {
            DocumentInstr.checkContentSequence(visitor.getStaticContext(), content, validationAction, schemaType);
        } catch (XPathException err) {
            err.maybeSetLocation(this);
            throw err;
        }
        return this;
    }

    public Expression optimize(ExpressionVisitor visitor, ItemType contextItemType) throws XPathException {
        content = visitor.optimize(content, contextItemType);
        adoptChildExpression(content);
        if (href != null) {
            href = visitor.optimize(href, contextItemType);
            adoptChildExpression(href);
        }
        if (formatExpression != null) {
            formatExpression = visitor.optimize(formatExpression, contextItemType);
            adoptChildExpression(formatExpression);
        }
        for (IntIterator it = serializationAttributes.keyIterator(); it.hasNext(); ) {
            int key = it.next();
            Expression value = serializationAttributes.get(key);
            if (!(value instanceof Literal)) {
                value = visitor.optimize(value, contextItemType);
                adoptChildExpression(value);
                serializationAttributes.put(key, value);
            }
        }
        return this;
    }

    public int getIntrinsicDependencies() {
        return StaticProperty.HAS_SIDE_EFFECTS;
    }

    /**
     * Copy an expression. This makes a deep copy.
     * @return the copy of the original expression
     */
    public Expression copy() {
        throw new UnsupportedOperationException("ResultDocument.copy()");
    }

    /**
     * Handle promotion offers, that is, non-local tree rewrites.
     * @param offer The type of rewrite being offered
     * @throws XPathException
     */
    protected void promoteInst(PromotionOffer offer) throws XPathException {
        content = doPromotion(content, offer);
        if (href != null) {
            href = doPromotion(href, offer);
        }
        for (IntIterator it = serializationAttributes.keyIterator(); it.hasNext(); ) {
            int key = it.next();
            Expression value = serializationAttributes.get(key);
            if (!(value instanceof Literal)) {
                value = doPromotion(value, offer);
                serializationAttributes.put(key, value);
            }
        }
    }

    /**
     * Get the name of this instruction for diagnostic and tracing purposes
     * (the string "xsl:result-document")
     */
    public int getInstructionNameCode() {
        return StandardNames.XSL_RESULT_DOCUMENT;
    }

    /**
     * Get the item type of the items returned by evaluating this instruction
     * @param th the type hierarchy cache
     * @return the static item type of the instruction. This is empty: the result-document instruction
     *         returns nothing.
     */
    public ItemType getItemType(TypeHierarchy th) {
        return EmptySequenceTest.getInstance();
    }

    /**
     * Get all the XPath expressions associated with this instruction
     * (in XSLT terms, the expression present on attributes of the instruction,
     * as distinct from the child instructions in a sequence construction)
     */
    public Iterator<Expression> iterateSubExpressions() {
        ArrayList list = new ArrayList(6);
        list.add(content);
        if (href != null) {
            list.add(href);
        }
        if (formatExpression != null) {
            list.add(formatExpression);
        }
        for (Iterator it = serializationAttributes.valueIterator(); it.hasNext(); ) {
            list.add(it.next());
        }
        if (dynamicOutputElement != null) {
            list.add(dynamicOutputElement);
        }
        return list.iterator();
    }

    /**
     * Replace one subexpression by a replacement subexpression
     * @param original    the original subexpression
     * @param replacement the replacement subexpression
     * @return true if the original subexpression is found
     */
    public boolean replaceSubExpression(Expression original, Expression replacement) {
        boolean found = false;
        if (content == original) {
            content = replacement;
            found = true;
        }
        if (href == original) {
            href = replacement;
            found = true;
        }
        for (IntIterator it = serializationAttributes.keyIterator(); it.hasNext(); ) {
            int k = it.next();
            if (serializationAttributes.get(k) == original) {
                serializationAttributes.put(k, replacement);
                found = true;
            }
        }
        if (dynamicOutputElement == original) {
            dynamicOutputElement = replacement;
            found = true;
        }
        return found;
    }

    public TailCall processLeavingTail(XPathContext context) throws XPathException {
        final Controller controller = context.getController();
        XPathContext c2 = context.newMinorContext();
        c2.setOrigin(this);
        Result result;
        OutputURIResolver resolver = (href == null ? null : controller.getOutputURIResolver());
        if (href == null) {
            String uri = controller.getBaseOutputURI();
            if (uri != null) {
                result = new StreamResult(uri);
            } else {
                result = controller.getPrincipalResult();
            }
        } else {
            try {
                String base;
                if (resolveAgainstStaticBase) {
                    base = baseURI;
                } else {
                    base = controller.getCookedBaseOutputURI();
                }
                String hrefValue = EscapeURI.iriToUri(href.evaluateAsString(context)).toString();
                try {
                    result = (resolver == null ? null : resolver.resolve(hrefValue, base));
                } catch (TransformerException err) {
                    throw XPathException.makeXPathException(err);
                } catch (Exception err) {
                    err.printStackTrace();
                    throw new XPathException("Exception thrown by OutputURIResolver", err);
                }
                if (result == null) {
                    resolver = StandardOutputResolver.getInstance();
                    result = resolver.resolve(hrefValue, base);
                }
            } catch (TransformerException e) {
                throw XPathException.makeXPathException(e);
            }
        }
        checkAcceptableUri(context, result);
        traceDestination(context, result);
        Properties computedLocalProps = gatherOutputProperties(context);
        String nextInChain = computedLocalProps.getProperty(SaxonOutputKeys.NEXT_IN_CHAIN);
        if (nextInChain != null && nextInChain.length() > 0) {
            try {
                result = controller.prepareNextStylesheet(nextInChain, baseURI, result);
            } catch (TransformerException e) {
                throw XPathException.makeXPathException(e);
            }
        }
        SerializerFactory sf = c2.getConfiguration().getSerializerFactory();
        PipelineConfiguration pipe = controller.makePipelineConfiguration();
        pipe.setHostLanguage(Configuration.XSLT);
        Receiver receiver = sf.getReceiver(result, pipe, computedLocalProps);
        c2.changeOutputDestination(receiver, true, validationAction, schemaType);
        SequenceReceiver out = c2.getReceiver();
        out.open();
        try {
            out.startDocument(0);
            content.process(c2);
            out.endDocument();
        } catch (XPathException err) {
            err.setXPathContext(context);
            err.maybeSetLocation(this);
            throw err;
        }
        out.close();
        if (resolver != null) {
            try {
                resolver.close(result);
            } catch (TransformerException e) {
                throw XPathException.makeXPathException(e);
            }
        }
        return null;
    }

    /**
     * In streaming mode, process the first half of the instruction (for example, to start a new document or element)
     * @param contextStack Stack of XPathContext objects. The instruction should use the one at the top of the stack.
     * Some instructions (such as xsl:result-document) create a new context object and add it to the stack, removing it
     * in the corresponding processRight() action.
     * @param state   a stack on which the instruction can save state information during the call on processLeft()
     */
    public void processLeft(Stack<XPathContext> contextStack, Stack state) throws XPathException {
        XPathContext context = contextStack.peek();
        final Controller controller = context.getController();
        XPathContext c2 = context.newMinorContext();
        c2.setOrigin(this);
        Result result;
        OutputURIResolver resolver = (href == null ? null : controller.getOutputURIResolver());
        if (href == null) {
            result = controller.getPrincipalResult();
        } else {
            try {
                String base;
                if (resolveAgainstStaticBase) {
                    base = baseURI;
                } else {
                    base = controller.getCookedBaseOutputURI();
                }
                String hrefValue = EscapeURI.iriToUri(href.evaluateAsString(context)).toString();
                try {
                    result = resolver.resolve(hrefValue, base);
                } catch (Exception err) {
                    throw new XPathException("Exception thrown by OutputURIResolver", err);
                }
                if (result == null) {
                    resolver = StandardOutputResolver.getInstance();
                    result = resolver.resolve(hrefValue, base);
                }
            } catch (TransformerException e) {
                throw XPathException.makeXPathException(e);
            }
        }
        checkAcceptableUri(context, result);
        traceDestination(context, result);
        Properties computedLocalProps = gatherOutputProperties(context);
        String nextInChain = computedLocalProps.getProperty(SaxonOutputKeys.NEXT_IN_CHAIN);
        if (nextInChain != null && nextInChain.length() > 0) {
            try {
                result = controller.prepareNextStylesheet(nextInChain, baseURI, result);
            } catch (TransformerException e) {
                throw XPathException.makeXPathException(e);
            }
        }
        SerializerFactory sf = c2.getConfiguration().getSerializerFactory();
        PipelineConfiguration pipe = controller.makePipelineConfiguration();
        pipe.setHostLanguage(Configuration.XSLT);
        Receiver receiver = sf.getReceiver(result, pipe, computedLocalProps);
        c2.changeOutputDestination(receiver, true, validationAction, schemaType);
        SequenceReceiver out = c2.getReceiver();
        out.open();
        state.push(out);
        state.push(resolver);
        state.push(result);
        contextStack.push(c2);
    }

    /**
     * In streaming mode, process the right half of the instruction (for example, to end a new document or element)
     * @param contextStack
     * @param state   a stack on which the instruction can save state information during the call on processLeft()
     */
    public void processRight(Stack<XPathContext> contextStack, Stack state) throws XPathException {
        XPathContext context = contextStack.pop();
        Result result = (Result) state.pop();
        OutputURIResolver resolver = (OutputURIResolver) state.pop();
        SequenceReceiver out = (SequenceReceiver) state.pop();
        try {
            out.endDocument();
        } catch (XPathException err) {
            err.setXPathContext(context);
            err.maybeSetLocation(this);
            throw err;
        }
        out.close();
        if (resolver != null) {
            try {
                resolver.close(result);
            } catch (TransformerException e) {
                throw XPathException.makeXPathException(e);
            }
        }
    }

    private void traceDestination(XPathContext context, Result result) {
        Configuration config = context.getConfiguration();
        boolean timing = config.isTiming();
        if (timing) {
            String dest = result.getSystemId();
            if (dest == null) {
                if (result instanceof StreamResult) {
                    dest = "anonymous output stream";
                } else if (result instanceof SAXResult) {
                    dest = "SAX2 ContentHandler";
                } else if (result instanceof DOMResult) {
                    dest = "DOM tree";
                } else {
                    dest = result.getClass().getName();
                }
            }
            config.getStandardErrorOutput().println("Writing to " + dest);
        }
    }

    private void checkAcceptableUri(XPathContext context, Result result) throws XPathException {
        Controller controller = context.getController();
        String uri = result.getSystemId();
        if (uri != null) {
            if (controller.getDocumentPool().find(uri) != null) {
                XPathException err = new XPathException("Cannot write to a URI that has already been read: " + result.getSystemId());
                err.setXPathContext(context);
                err.setLocator(this);
                err.setErrorCode("XTRE1500");
                throw err;
            }
            DocumentURI documentKey = new DocumentURI(uri);
            if (!controller.checkUniqueOutputDestination(documentKey)) {
                XPathException err = new XPathException("Cannot write more than one result document to the same URI: " + result.getSystemId());
                err.setXPathContext(context);
                err.setLocator(this);
                err.setErrorCode("XTDE1490");
                throw err;
            } else {
                controller.addUnavailableOutputDestination(documentKey);
            }
        }
        controller.setThereHasBeenAnExplicitResultDocument();
    }

    /**
     * Create a properties object that combines the serialization properties specified
     * on the xsl:result-document itself with those specified in the referenced xsl:output declaration
     * @param context The XPath evaluation context
     * @return the assembled properties
     * @throws XPathException if invalid properties are found
     */
    private Properties gatherOutputProperties(XPathContext context) throws XPathException {
        Controller controller = context.getController();
        Configuration config = context.getConfiguration();
        NamePool namePool = config.getNamePool();
        Properties computedGlobalProps = globalProperties;
        if (formatExpression != null) {
            CharSequence format = formatExpression.evaluateAsString(context);
            String[] parts;
            try {
                parts = controller.getConfiguration().getNameChecker().getQNameParts(format);
            } catch (QNameException e) {
                XPathException err = new XPathException("The requested output format " + Err.wrap(format) + " is not a valid QName");
                err.setErrorCode("XTDE1460");
                err.setXPathContext(context);
                throw err;
            }
            String uri = nsResolver.getURIForPrefix(parts[0], false);
            if (uri == null) {
                XPathException err = new XPathException("The namespace prefix in the format name " + format + " is undeclared");
                err.setErrorCode("XTDE1460");
                err.setXPathContext(context);
                throw err;
            }
            StructuredQName qName = new StructuredQName(parts[0], uri, parts[1]);
            computedGlobalProps = getExecutable().getOutputProperties(qName);
            if (computedGlobalProps == null) {
                XPathException err = new XPathException("There is no xsl:output format named " + format);
                err.setErrorCode("XTDE1460");
                err.setXPathContext(context);
                throw err;
            }
        }
        Properties computedLocalProps = new Properties(computedGlobalProps);
        for (Iterator citer = localProperties.keySet().iterator(); citer.hasNext(); ) {
            String key = (String) citer.next();
            String[] parts = NamePool.parseClarkName(key);
            try {
                setSerializationProperty(computedLocalProps, parts[0], parts[1], localProperties.getProperty(key), nsResolver, true, config);
            } catch (XPathException e) {
                e.setErrorCode("XTDE0030");
                e.maybeSetLocation(this);
                throw e;
            }
        }
        if (serializationAttributes.size() > 0) {
            for (IntIterator it = serializationAttributes.keyIterator(); it.hasNext(); ) {
                int key = it.next();
                Expression exp = serializationAttributes.get(key);
                String value = exp.evaluateAsString(context).toString();
                String lname = namePool.getLocalName(key);
                String uri = namePool.getURI(key);
                try {
                    setSerializationProperty(computedLocalProps, uri, lname, value, nsResolver, false, config);
                } catch (XPathException e) {
                    e.setErrorCode("XTDE0030");
                    e.maybeSetLocation(this);
                    e.maybeSetContext(context);
                    if (NamespaceConstant.SAXON.equals(e.getErrorCodeNamespace()) && "SXWN".equals(e.getErrorCodeLocalPart().substring(0, 4))) {
                        try {
                            context.getController().getErrorListener().warning(e);
                        } catch (TransformerException e2) {
                            throw XPathException.makeXPathException(e2);
                        }
                    } else {
                        throw e;
                    }
                }
            }
        }
        if (dynamicOutputElement != null) {
            Item outputArg = dynamicOutputElement.evaluateItem(context);
            if (!(outputArg instanceof NodeInfo && ((NodeInfo) outputArg).getNodeKind() == Type.ELEMENT && ((NodeInfo) outputArg).getFingerprint() == StandardNames.XSL_OUTPUT)) {
                XPathException err = new XPathException("The third argument of saxon:result-document must be an <xsl:output> element");
                err.setLocator(this);
                err.setXPathContext(context);
                throw err;
            }
            Properties dynamicProperties = new Properties();
            processXslOutputElement((NodeInfo) outputArg, dynamicProperties, context);
            for (Iterator it = dynamicProperties.keySet().iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                StructuredQName name = StructuredQName.fromClarkName(key);
                String value = dynamicProperties.getProperty(key);
                try {
                    setSerializationProperty(computedLocalProps, name.getNamespaceURI(), name.getLocalName(), value, nsResolver, false, config);
                } catch (XPathException e) {
                    e.setErrorCode("XTDE0030");
                    e.maybeSetLocation(this);
                    e.maybeSetContext(context);
                    throw e;
                }
            }
        }
        return computedLocalProps;
    }

    /**
     * Validate a serialization property and add its value to a Properties collection
     * @param details      the properties to be updated
     * @param uri          the uri of the property name
     * @param lname        the local part of the property name
     * @param value        the value of the serialization property. In the case of QName-valued values,
     *                     this will use lexical QNames if prevalidated is false and a NamespaceResolver is supplied;
     *                     otherwise they will use Clark-format names
     * @param nsResolver   resolver for lexical QNames; not needed if prevalidated, or if QNames are supplied in Clark
     *                     format
     * @param prevalidated true if values are already known to be valid and lexical QNames have been
     *                     expanded into Clark notation
     * @param config      the Saxon configuration
     * @throws XPathException if any serialization property has an invalid value
     */
    public static void setSerializationProperty(Properties details, String uri, String lname, String value, NamespaceResolver nsResolver, boolean prevalidated, Configuration config) throws XPathException {
        NameChecker checker = config.getNameChecker();
        if (uri.length() == 0 || NamespaceConstant.SAXON.equals(uri)) {
            if (lname.equals(StandardNames.METHOD)) {
                if (value.equals("xml") || value.equals("html") || value.equals("text") || value.equals("xhtml") || prevalidated || value.startsWith("{")) {
                    details.setProperty(OutputKeys.METHOD, value);
                } else {
                    String[] parts;
                    try {
                        parts = checker.getQNameParts(value);
                        String prefix = parts[0];
                        if (prefix.length() == 0) {
                            XPathException err = new XPathException("method must be xml, html, xhtml, or text, or a prefixed name");
                            err.setErrorCode("XTSE1570");
                            err.setIsStaticError(true);
                            throw err;
                        } else if (nsResolver != null) {
                            String muri = nsResolver.getURIForPrefix(prefix, false);
                            if (muri == null) {
                                XPathException err = new XPathException("Namespace prefix '" + prefix + "' has not been declared");
                                err.setErrorCode("XTSE1570");
                                err.setIsStaticError(true);
                                throw err;
                            }
                            details.setProperty(OutputKeys.METHOD, '{' + muri + '}' + parts[1]);
                        } else {
                            details.setProperty(OutputKeys.METHOD, value);
                        }
                    } catch (QNameException e) {
                        XPathException err = new XPathException("Invalid method name. " + e.getMessage());
                        err.setErrorCode("XTSE1570");
                        err.setIsStaticError(true);
                        throw err;
                    }
                }
            } else if (lname.equals(StandardNames.USE_CHARACTER_MAPS)) {
                String existing = details.getProperty(SaxonOutputKeys.USE_CHARACTER_MAPS);
                if (existing == null) {
                    existing = "";
                }
                details.setProperty(SaxonOutputKeys.USE_CHARACTER_MAPS, existing + value);
            } else if (lname.equals("cdata-section-elements")) {
                processListOfElementNames(details, OutputKeys.CDATA_SECTION_ELEMENTS, value, nsResolver, prevalidated, checker);
            } else if (lname.equals("suppress-indentation")) {
                processListOfElementNames(details, SaxonOutputKeys.SUPPRESS_INDENTATION, value, nsResolver, prevalidated, checker);
            } else if (lname.equals("double-space")) {
                processListOfElementNames(details, SaxonOutputKeys.DOUBLE_SPACE, value, nsResolver, prevalidated, checker);
            } else if (lname.equals("next-in-chain")) {
                XPathException e = new XPathException("saxon:next-in-chain value cannot be specified dynamically");
                e.setErrorCodeQName(new StructuredQName("saxon", NamespaceConstant.SAXON, SaxonErrorCode.SXWN9004));
                throw e;
            } else {
                if (lname.equals("output-version")) {
                    lname = "version";
                }
                String clarkName = lname;
                if (uri.length() != 0) {
                    clarkName = '{' + uri + '}' + lname;
                }
                if (!prevalidated) {
                    try {
                        SaxonOutputKeys.checkOutputProperty(clarkName, value, config);
                    } catch (XPathException err) {
                        err.maybeSetErrorCode("XTDE0030");
                        throw err;
                    }
                }
                details.setProperty(clarkName, value);
            }
        } else {
            details.setProperty('{' + uri + '}' + lname, value);
        }
    }

    private static void processListOfElementNames(Properties details, String key, String value, NamespaceResolver nsResolver, boolean prevalidated, NameChecker checker) throws XPathException {
        String existing = details.getProperty(key);
        if (existing == null) {
            existing = "";
        }
        String s = SaxonOutputKeys.parseListOfElementNames(value, nsResolver, prevalidated, checker, "XTDE0030");
        details.setProperty(key, existing + s);
    }

    /**
     * Diagnostic print of expression structure. The abstract expression tree
     * is written to the supplied output destination.
     */
    public void explain(ExpressionPresenter out) {
        out.startElement("resultDocument");
        content.explain(out);
        out.endElement();
    }

    /**
     * Construct a set of output properties from an xsl:output element supplied at run-time
     * @param element an xsl:output element
     * @param props Properties object to which will be added the values of those serialization properties
     * that were specified
     * @param c the XPath dynamic context
     */
    public static void processXslOutputElement(NodeInfo element, Properties props, XPathContext c) throws XPathException {
        SequenceIterator iter = element.iterateAxis(Axis.ATTRIBUTE);
        NamespaceResolver resolver = new InscopeNamespaceResolver(element);
        while (true) {
            NodeInfo att = (NodeInfo) iter.next();
            if (att == null) {
                break;
            }
            String uri = att.getURI();
            String local = att.getLocalPart();
            String val = Whitespace.trim(att.getStringValueCS());
            setSerializationProperty(props, uri, local, val, resolver, false, c.getConfiguration());
        }
    }
}
