package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRenderer;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * A decorating menu item renderer which renders shard link conditional markup
 * around a standard menu item renderer.
 * <p>
 * This is used to render menu items for use in shard link menus.
 *
 * @see ShardLinkMenuModelBuilder
 */
public final class ShardLinkMenuItemRenderer implements MenuItemBracketingRenderer {

    /**
     * The copyright statement.
     */
    private static final String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The item renderer which we delegate to to render the standard markup.
     */
    private final MenuItemBracketingRenderer delegate;

    /**
     * Construct an instance of this class.
     *
     * @param delegate the renderer which will render the standard markup for
     *      the menu item.
     */
    public ShardLinkMenuItemRenderer(MenuItemBracketingRenderer delegate) {
        this.delegate = delegate;
    }

    public boolean open(OutputBuffer buffer, MenuItem item) throws RendererException {
        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;
        ShardLinkMenuItem shardLinkMenuItem = (ShardLinkMenuItem) item;
        Element element;
        element = dom.openElement(DissectionConstants.SHARD_LINK_ELEMENT);
        element.setAnnotation(shardLinkMenuItem.getShardLinkAttributes());
        return delegate.open(buffer, item);
    }

    public void close(OutputBuffer buffer, MenuItem item) throws RendererException {
        delegate.close(buffer, item);
        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;
        dom.closeElement(DissectionConstants.SHARD_LINK_ELEMENT);
    }
}
