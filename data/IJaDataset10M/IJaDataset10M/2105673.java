package com.volantis.mcs.protocols.html.menu;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.renderer.DelegatingMenuItemBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRenderer;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * A menu item renderer which decorates a child menu item renderer and adds
 * the ability to render a shortcut which is external to the markup created
 * for the menu item.
 * <p>
 * This is useful for HTMLLiberate where we render the shortcut as a bunch of 
 * javascript in the page head. 
 */
class HTMLLiberateShortcutMenuItemRenderer extends DelegatingMenuItemBracketingRenderer {

    /**
     * Object which can handle rendering shortcuts external to a menu item.
     */
    private final DeprecatedExternalShortcutRenderer shortcutRenderer;

    /**
     * Construct an instance of this class.
     * 
     * @param delegate renders entire menu items (apart from the shortcut).
     * @param shortcutRenderer renders shortcuts external to the menu item.
     */
    public HTMLLiberateShortcutMenuItemRenderer(MenuItemBracketingRenderer delegate, DeprecatedExternalShortcutRenderer shortcutRenderer) {
        super(delegate);
        this.shortcutRenderer = shortcutRenderer;
    }

    /**
     * Add a shortcut for the menu item.
     */
    public void close(OutputBuffer buffer, MenuItem item) throws RendererException {
        super.close(buffer, item);
        shortcutRenderer.renderShortcut(item);
    }
}
