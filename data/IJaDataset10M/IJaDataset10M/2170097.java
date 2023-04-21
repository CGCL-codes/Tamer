package com.volantis.mcs.papi.impl;

import com.volantis.mcs.papi.DissectingPane;
import com.volantis.mcs.protocols.layouts.DissectingPaneInstance;

/**
 * This class allows the overriding of link text used within dissecting panes
 * at runtime. The class can only be obtained via the MarinerRequestContext
 * class and for this reason it does not have a publicly accessible constructor.
 *
 * @see com.volantis.mcs.context.MarinerRequestContext#getDissectingPane
 */
public class DissectingPaneImpl implements DissectingPane {

    /**
     * The context for the dissecting pane
     */
    private DissectingPaneInstance dissectingPaneInstance;

    /**
     * Do not alter the access level of this constructor
     * It is package scope so that it is not availaible via the
     * public API.
     *
     * Creates a DissectingPane facade from a DissectingPaneInstance
     *
     * @param instance A DissectingPaneInstance object that is used
     *                 to access/modify the pane attributes.
     */
    DissectingPaneImpl(DissectingPaneInstance instance) {
        this.dissectingPaneInstance = instance;
    }

    public String getName() {
        return dissectingPaneInstance.getDissectingPaneName();
    }

    public String getLinkToText() {
        return dissectingPaneInstance.getLinkToText();
    }

    public String getLinkFromText() {
        return dissectingPaneInstance.getLinkFromText();
    }

    public void overrideLinkToText(String linkToText) {
        dissectingPaneInstance.setLinkToText(linkToText);
    }

    public void overrideLinkFromText(String linkFromText) {
        dissectingPaneInstance.setLinkFromText(linkFromText);
    }
}
