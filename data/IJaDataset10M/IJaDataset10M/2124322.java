package com.volantis.mcs.protocols.vdxml.menu;

import com.volantis.mcs.protocols.menu.shared.renderer.AbstractMenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultSpanMenuItemRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedOutputLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedSpanOutput;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemComponentRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.NumericShortcutEmulationRenderer;
import com.volantis.mcs.protocols.renderer.RendererContext;

/**
 * A menu item renderer factory for VDXML menus.
 */
public class VDXMLMenuItemRendererFactory extends AbstractMenuItemRendererFactory {

    /**
     * Renders "span" like markup around other markup, potentially used to
     * add stylistic values.
     */
    private final DeprecatedSpanOutput spanOutput;

    /**
     * Renders the RACCOURCI markup.
     */
    private final DeprecatedExternalLinkOutput externalLinkOutput;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param context Contains contextual information.
     * @param outputLocator Contains references to markup generators.
     * @param externalLinkOutput Renderer for the RACCOURCI link.
     */
    public VDXMLMenuItemRendererFactory(RendererContext context, DeprecatedOutputLocator outputLocator, DeprecatedExternalLinkOutput externalLinkOutput) {
        super(context.getOutputBufferFactory());
        this.spanOutput = outputLocator.getSpanOutput();
        this.externalLinkOutput = externalLinkOutput;
    }

    protected MenuItemComponentRenderer createPlainImageRendererImpl(boolean provideAltText) {
        return null;
    }

    public MenuItemBracketingRenderer createInnerLinkRenderer(NumericShortcutEmulationRenderer emulation) {
        return new VDXMLExternalLinkMenuItemRenderer(externalLinkOutput, null);
    }

    public MenuItemBracketingRenderer createOuterLinkRenderer(NumericShortcutEmulationRenderer emulation) {
        return new VDXMLExternalLinkMenuItemRenderer(externalLinkOutput, new DefaultSpanMenuItemRenderer(spanOutput));
    }

    public MenuItemBracketingRenderer createOuterRenderer() {
        return new DefaultSpanMenuItemRenderer(spanOutput);
    }

    public NumericShortcutEmulationRenderer createNumericShortcutEmulationRenderer() {
        return null;
    }

    public MenuItemComponentRenderer createPlainTextRenderer() {
        return new VDXMLTextMenuItemRenderer(spanOutput);
    }
}
