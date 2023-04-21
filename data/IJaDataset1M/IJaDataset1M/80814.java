package com.volantis.mcs.protocols.wml.menu;

import com.volantis.mcs.protocols.menu.shared.renderer.AbstractMenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultImageMenuItemRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultStyledPlainTextMenuItemRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedImageOutput;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedOutputLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedSpanOutput;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemComponentRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.NumericShortcutEmulationRenderer;
import com.volantis.mcs.protocols.renderer.RendererContext;

/**
 * A menu item renderer factory for Openwave menus.
 */
public class OpenwaveMenuItemRendererFactory extends AbstractMenuItemRendererFactory {

    /**
     * A means of rendering an image utilizing protocol specific information.
     */
    private final DeprecatedImageOutput imageOutput;

    /**
     * Renders the option tag around a menu item.
     */
    private MenuItemBracketingRenderer optionRenderer;

    /**
     * Renders "span" like markup around other markup, potentially used to
     * add stylistic values
     */
    private final DeprecatedSpanOutput spanOutput;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param context       Contains contextual information.
     * @param outputLocator Contains references to markup generators.
     */
    public OpenwaveMenuItemRendererFactory(RendererContext context, DeprecatedOutputLocator outputLocator) {
        super(context.getOutputBufferFactory());
        this.imageOutput = outputLocator.getImageOutput();
        this.spanOutput = outputLocator.getSpanOutput();
    }

    protected MenuItemComponentRenderer createPlainImageRendererImpl(boolean provideAltText) {
        return new DefaultImageMenuItemRenderer(imageOutput, provideAltText);
    }

    public MenuItemBracketingRenderer createInnerLinkRenderer(NumericShortcutEmulationRenderer emulation) {
        return MenuItemBracketingRenderer.NULL;
    }

    public MenuItemBracketingRenderer createOuterLinkRenderer(NumericShortcutEmulationRenderer emulation) {
        return createOptionRenderer();
    }

    public MenuItemBracketingRenderer createOuterRenderer() {
        return createOptionRenderer();
    }

    public NumericShortcutEmulationRenderer createNumericShortcutEmulationRenderer() {
        return null;
    }

    public MenuItemComponentRenderer createPlainTextRenderer() {
        return new DefaultStyledPlainTextMenuItemRenderer(spanOutput);
    }

    /**
     * Create a renderer for the option tag.
     *
     * @return the created renderer.
     */
    private MenuItemBracketingRenderer createOptionRenderer() {
        if (optionRenderer == null) {
            optionRenderer = new OpenwaveOptionMenuItemRenderer();
        }
        return optionRenderer;
    }
}
