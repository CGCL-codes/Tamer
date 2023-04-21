package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.protocols.layouts.DissectingPaneInstance;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactory;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import java.io.IOException;

/**
 * A format renderer that is used to render dissecting panes.
 */
public class DissectingPaneRenderer extends PaneRenderer {

    /**
     * The factory to use to create attributes classes.
     */
    private final LayoutAttributesFactory factory;

    /**
     * Initialise.
     *
     * @param factory The factory to use to construct any attributes classes
     * used internally.
     */
    public DissectingPaneRenderer(LayoutAttributesFactory factory) {
        this.factory = factory;
    }

    protected void renderPaneInstance(FormatRendererContext context, AbstractPaneInstance abstractPaneInstance) throws IOException, ProtocolException {
        DissectingPane pane = (DissectingPane) abstractPaneInstance.getFormat();
        String inclusionPath = context.getInclusionPath();
        DissectingPaneInstance paneInstance = (DissectingPaneInstance) abstractPaneInstance;
        DissectingPaneAttributes attributes = factory.createDissectingPaneAttributes();
        PaneAttributes oldattr = paneInstance.getAttributes();
        attributes.setStyles(oldattr.getStyles());
        attributes.setInclusionPath(inclusionPath);
        attributes.setDissectingPane(pane);
        attributes.setIsNextLinkFirst(pane.isNextLinkFirst());
        attributes.setLinkText(paneInstance.getLinkToText());
        attributes.setBackLinkText(paneInstance.getLinkFromText());
        LayoutModule module = context.getLayoutModule();
        module.writeOpenDissectingPane(attributes);
        super.renderPaneInstance(context, abstractPaneInstance);
        module.writeCloseDissectingPane(attributes);
    }
}
