package com.volantis.mcs.ibm.websphere.mcsi;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.runtime.configuration.project.GeneratedResourcesConfiguration;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;

/**
 * The generated-resources IAPI element.
 * <p>
 * This collects the base-dir attribute of the generated-resources element,
 * creates a GeneratedResourcesConfiguration object to store it in and stores
 * that into the parent element for later use. It follows a similar pattern to
 * the other related elements.
 */
public class GeneratedResourcesElement extends AbstractPortletContextChildElement {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The name of the element in an array for use in error messages.
     */
    private static final Object[] elementName = { "generated-resources" };

    /**
     * The "fake" configuration object which we create from the incoming XDIME.
     */
    private GeneratedResourcesConfiguration generatedResourcesConfiguration;

    /**
     * The parent element, where we store the data we create from the incoming
     * XDIME.
     */
    private PortletContextElement parent;

    public int elementStart(MarinerRequestContext context, PAPIAttributes mcsiAttributes) throws PAPIException {
        GeneratedResourcesAttributes attrs = (GeneratedResourcesAttributes) mcsiAttributes;
        MarinerPageContext pageContext = ContextInternals.getMarinerPageContext(context);
        generatedResourcesConfiguration = new GeneratedResourcesConfiguration();
        generatedResourcesConfiguration.setBaseDir(attrs.getBaseDir());
        parent = findParent(pageContext, elementName);
        pageContext.pushMCSIElement(this);
        return PROCESS_ELEMENT_BODY;
    }

    public int elementEnd(MarinerRequestContext context, PAPIAttributes mcsiAttributes) throws PAPIException {
        if (parent != null) {
            parent.setGeneratedResourcesConfiguration(generatedResourcesConfiguration);
            MarinerPageContext pageContext = ContextInternals.getMarinerPageContext(context);
            pageContext.popMCSIElement();
        }
        return CONTINUE_PROCESSING;
    }

    public void elementReset(MarinerRequestContext context) {
        parent = null;
        generatedResourcesConfiguration = null;
    }
}
