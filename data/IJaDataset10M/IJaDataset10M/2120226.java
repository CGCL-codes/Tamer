package org.xaware.server.engine.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.xaware.server.engine.IResourceManager;
import org.xaware.server.engine.ISessionStateRegistry;
import org.xaware.server.engine.instruction.XABizCompRef;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;

/**
 * This context represents the accumulated state of the execution of a BizComponent.
 * It provides access and storage of the response elements created by the BizComp
 * instructions (these are stored in accumulatedResults). The scriptRoot gets reset
 * with each execution of the BizComponent so this context gets pushed onto the
 * stack as a new context with each execution of a BizComponent instruction that
 * produces a new element for the scriptRoot node. After execution of those scriptNodes
 * in the script root the mutated intermediate result is placed into a ScriptNode
 * created by the parent bizDocContext after being added as the child or sibling of
 * the bizCompRefInst's element.
 * 
 * @author jweaver
 */
public class BizCompContext extends BizViewContext {

    private final String bizCompType;

    private final BizDocContext parentBizDocContext;

    private final XABizCompRef parentBizCompRef;

    private final List<Content> accumulatedResults = new ArrayList<Content>(50);

    private int referenceIterationCount = 0;

    /**
     * Constructor.
     * 
     * @param bizCompName
     *            the name of the XBC file.
     * @param params
     *            any BizParams to the BizComp.
     * @param inputXML
     *            the root element of the inputXML.
     * @param immediateParentContext
     *            the immediate parent context which spawned this context, which may
     *            be a BizDocContext or a ExecutionSubContext.
     * @param registry
     *            the registry containing execution state of this BizView.
     * @param parentCompRef
     *            the parent BizComp reference instruction.
     * @throws XAwareException
     */
    public BizCompContext(final String bizCompName, final Map<String, Object> params, final Element inputXML, final BizDocContext immediateParentContext, final ISessionStateRegistry registry, final XABizCompRef parentCompRef) throws XAwareException {
        super(bizCompName, registry, inputXML);
        ip = getResourceMgr().getInstructionParser(IResourceManager.BIZ_COMP_INST_PARSER);
        this.parentBizDocContext = (immediateParentContext instanceof ExecutionSubContext) ? ((ExecutionSubContext) immediateParentContext).getBizDocContext() : immediateParentContext;
        parentBizCompRef = parentCompRef;
        setupJdom(bizCompName);
        if (params != null) {
            suppliedBizParams.putAll(params);
        }
        bizCompType = getScriptRoot().getAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_TYPE, XAwareConstants.xaNamespace);
        checkForUnsupportedAttributes();
    }

    /**
     * This method is intended to be a lifecycle method called after the construction and before the root element is
     * utilized for anything other than configuring the initial error handler. It is during the execution of this method
     * that the configuration information provided in the script is consumed. In this case the work done here is to
     * setup the streaming info.
     * 
     * @throws XAwareException
     *             if there is a problem utilizing the configuration information.
     */
    public void configure() throws XAwareException {
        processXaInput(getScriptRoot());
        configured = true;
    }

    /**
     * Checks for the presence of unsupported attributes on the BizComp root element, and
     * throws an exception if any are present.
     * 
     * @throws XAwareException if any unsupported attribute exists on the BizComp root element.
     */
    private void checkForUnsupportedAttributes() throws XAwareException {
        checkForUnsupportedAttribute(XAwareConstants.BIZDOCUMENT_ATTR_TRANSACTION);
        checkForUnsupportedAttribute(XAwareConstants.BIZDOCUMENT_ATTR_ON_ERROR);
        checkForUnsupportedAttribute(XAwareConstants.BIZDOCUMENT_ATTR_ON_ERR);
        checkForUnsupportedAttribute(XAwareConstants.BIZDOCUMENT_ATTR_ON_BIZDOC_ERR);
        checkForUnsupportedAttribute(XAwareConstants.BIZDOCUMENT_ATTR_ON_PARSE_ERROR);
        checkForUnsupportedAttribute(XAwareConstants.BIZDOCUMENT_ATTR_ON_PARSE_ERR);
        checkForUnsupportedAttribute(XAwareConstants.BIZDOCUMENT_ATTR_ON_VALIDATION_ERROR);
        checkForUnsupportedAttribute(XAwareConstants.BIZDOCUMENT_ATTR_ON_VALID_ERR);
    }

    /**
     * Checks for the presence of a single unsupported attribute on the BizComp root element,
     * and throws an exception if it is present.
     * 
     * @param attrName
     *            the unqualified name of the target attribute. It is assumed to be
     *            in the xa: namespace.
     * @throws XAwareException
     *             if any unsupported attribute exists on the BizComp root element.
     */
    private void checkForUnsupportedAttribute(String attrName) throws XAwareException {
        Attribute attr = scriptRoot.getAttribute(attrName, XAwareConstants.xaNamespace);
        if (attr != null) {
            throw new XAwareException("The xa:" + attrName + " attribute is not supported in BizComponents.");
        }
    }

    /**
     * Uses the provided bizCompName to request a JDOM Document from the BizCompFactory. The root element of that JDOM
     * is added to the internal XML structure as a child of the configRoot element. The provided input Xml document has
     * the root element detached and added as a child element to the internal inputRoot element.
     * 
     * @param p_bizCompName
     * @throws XAwareException
     */
    private void setupJdom(final String bizCompName) throws XAwareException {
        try {
            final Document jdom = getResourceMgr().getBizComponentFactory().createInstance(bizCompName);
            scriptRoot = jdom.getRootElement();
            scriptDocument = jdom;
            operState = new OperationState(scriptRoot, lf);
        } catch (final XAwareException e) {
            final String errMsg = "Failed to load BizComponent.  " + e.getMessage();
            lf.severe(errMsg, "BizCompContext", "setupJdom");
            throw new XAwareException(errMsg, e);
        }
    }

    /**
     * Returns the result of executing this BizComp.
     * 
     * @see org.xaware.server.engine.IBizViewContext#getResult()
     * @return a JDOM Document containing the results of BizComp execution.
     */
    public Document getResult() {
        final Document resultDoc = new Document();
        final List<Content> accumulatedRslts = getAccumulatedResults();
        for (int i = 0; i < accumulatedRslts.size(); i++) {
            final Content c = accumulatedRslts.get(i);
            resultDoc.addContent((Content) c.clone());
        }
        return resultDoc;
    }

    /**
     * Replaces all of the contents of an Element in the JDOM
     * with a clone of its error handler Element.
     * 
     * @param elemToReplaceContentOf
     *            the Element whose contents are to be replaced,
     *            i.e., the Element declaring the error handler.
     * @param errorHandlerElement
     *            The error handler Element to be cloned
     */
    @Override
    public void replaceContentsWithErrorHandler(final Element elemToReplaceContentOf, final Element errorHandlerElement) {
        throw new IllegalStateException("BizCompContext does not do error handling");
    }

    /**
     * Builds a String to be used as the prefix of error messages showing the BizView type and name.
     * 
     * @return a String to be used as the prefix of error messages.
     */
    public String getErrorMessagePrefix() {
        return "Error in executing BizComponent [" + getBizViewName() + "]:";
    }

    /**
     * Sets up a second pass through a set of nodes if needed. This is done for processing xa:visible instructions after
     * all processing for a BizView has been completed.
     * 
     * @param registry
     *            An ISessionStateRegistry reference to the registry in which the IScriptNodes for the second pass
     *            should be enqueued.
     * @throws XAwareException
     *             if any error occurs setting up the second pass.
     */
    public void setupSecondPass(final ISessionStateRegistry registry) throws XAwareException {
    }

    /**
     * Gets the value of the xa:bizcomptype attribute specified in the root element
     * of the BizComp JDOM Document.
     * 
     * @return the BizComp type String.
     */
    public String getBizCompType() {
        return bizCompType;
    }

    /**
     * Adds each of the result elements in the List provided into accumulated results, and notifies the parentBizCompRef
     * instruction that a new results are available (so that they can be streamed out, if applicable).
     * 
     * @param results
     *            a List of results Elements
     * @throws XAwareException
     *             If there is a problem with the output streaming.
     */
    public void addResults(final List<Content> results) throws XAwareException {
        if (results == null) {
            return;
        }
        accumulatedResults.addAll(results);
        if (parentBizCompRef != null) {
            parentBizCompRef.resultAddedByBizComp();
        }
    }

    /**
     * Gets a read-only version of the accumulated results List.
     * 
     * @return an unmodifiable version of the accumulated results List.
     */
    public List<Content> getAccumulatedResults() {
        return Collections.unmodifiableList(accumulatedResults);
    }

    /**
     * Resets the accumulated results List.
     */
    public void clearAccumulatedResults() {
        accumulatedResults.clear();
    }

    /**
     * Creates a new ExecutionSubContext with the supplied Element as its
     * scriptRoot.
     * 
     * @param aScriptRoot
     *            the Element to use as the scriptRoot of the new
     *            ExecutionSubContext.
     * @return the newly created ExecutionSubContext.
     * @see org.xaware.server.engine.IBizViewContext#createSubContext(org.jdom.Element)
     */
    public ExecutionSubContext createSubContext(final Element aScriptRoot) {
        return new ExecutionSubContext(this, aScriptRoot);
    }

    /**
     * Gets the parent BizDocContext of this BizCompContext.
     * 
     * @return the parent BizDocContext.
     */
    public BizDocContext getParentBizDocContext() {
        return this.parentBizDocContext;
    }

    /**
     * This method should do any cleanup of configuration settings for the context.
     * One specific example is dynamic log level settings can be cleaned up here.
     * 
     * @throws XAwareException on any failure.
     */
    public void endPassProcessing() throws XAwareException {
    }

    /**
     * @return
     */
    public int getReferenceIterationCount() {
        return referenceIterationCount;
    }

    /**
     * This stores the count of times the reference instruction has created BizCompContexts.
     * @param count
     */
    public void setReferenceIterationCount(int count) {
        referenceIterationCount = count;
    }
}
