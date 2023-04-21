package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBufferFactory;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.protocols.assets.implementation.LiteralImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.css.DefaultStylePropertyResolver;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.builder.ConcreteMenuModelBuilder;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import java.io.IOException;

/**
 * Tests {@link DefaultMenuItemRendererSelector}.
 */
public class DefaultMenuItemRendererSelectorTestCase extends TestCaseAbstract {

    /**
     * Provides an output buffer that can be used in the testing.
     *
     * @return an output buffer for the testing
     */
    protected OutputBuffer createOutputBuffer() {
        return new TestDOMOutputBuffer();
    }

    /**
     * Creates a menu buffer that can be used in the testing. Calls
     * {@link #createOutputBuffer}.
     *
     * @return a menu buffer for the testing
     */
    protected MenuBuffer createMenuBuffer() {
        OutputBuffer outputBuffer = createOutputBuffer();
        return new ConcreteMenuBuffer(outputBuffer, SeparatorRenderer.NULL);
    }

    /**
     * Creates a menu item renderer factory that can be used in the testing.
     *
     * @return a menu item renderer factory for the testing
     */
    protected MenuItemRendererFactory createFactory() {
        TestDOMOutputBufferFactory factory = new TestDOMOutputBufferFactory();
        return new TestMenuItemRendererFactory(factory);
    }

    /**
     * Creates a menu item renderer selector that can be used in the testing.
     * Calls {@link #createFactory} and {@link #createSeparatorSelector}.
     *
     * @return a menu item renderer selector for the testing
     */
    protected MenuItemRendererSelector createSelector() {
        return new DefaultMenuItemRendererSelector(createFactory(), createSeparatorSelector());
    }

    /**
     * Creates a menu separator renderer selector that can be used in the
     * testing. Calls {@link #createSeparatorFactory} and {@link
     * #createAssetResolver}.
     *
     * @return a menu separator renderer selector for the testing
     */
    protected MenuSeparatorRendererSelector createSeparatorSelector() {
        return new DefaultMenuSeparatorRendererSelector(createSeparatorFactory(), createAssetResolver(), new DefaultStylePropertyResolver(null, null));
    }

    /**
     * Creates an asset resolver that can be used in the testing.
     *
     * @return an asset resolver for the testing
     */
    protected AssetResolver createAssetResolver() {
        return new AssetResolverMock("assetResolverMock", expectations);
    }

    /**
     * Creates a menu separator renderer factory that can be used in the
     * testing.
     *
     * @return a menu separator renderer factory for the testing
     */
    protected MenuSeparatorRendererFactory createSeparatorFactory() {
        return new TestMenuSeparatorRendererFactory();
    }

    /**
     * Creates a menu buffer locator that can be used in the testing.
     *
     * @param buffer a menu buffer instance to be returned by the locator
     * @return a menu buffer locator for the testing
     */
    protected MenuBufferLocator createMenuBufferLocator(final MenuBuffer buffer) {
        return new MenuBufferLocator() {

            public MenuBuffer getMenuBuffer(MenuEntry entry) {
                return buffer;
            }
        };
    }

    /**
     * A utility method to return a string of the markup in the given menu
     * buffer's associated output buffer.
     *
     * @param buffer the buffer for which the markup string is required
     * @return a string of the markup from the given buffer's output buffer
     */
    protected String toString(MenuBuffer buffer) throws IOException {
        TestDOMOutputBuffer outputBuffer = (TestDOMOutputBuffer) buffer.getOutputBuffer();
        return DOMUtilities.toString(outputBuffer.getRoot());
    }

    /**
     * A utility method to create a valid menu where the menu has the specified
     * style property values.
     *
     * @param menuStyles the styles for the menu
     * @return a valid menu with the specified style property values
     */
    protected Menu createMenu(Styles menuStyles) throws Exception {
        MenuModelBuilder builder = new ConcreteMenuModelBuilder();
        builder.startMenu();
        builder.setElementDetails("menu", "menu1", menuStyles);
        builder.startMenuItem();
        builder.setElementDetails("menuitem", "menuitem1", StylesBuilder.getInitialValueStyles());
        builder.startLabel();
        builder.startIcon();
        builder.setNormalImageURL(new LiteralImageAssetReference("normal.gif"));
        builder.setOverImageURL(new LiteralImageAssetReference("over.gif"));
        builder.endIcon();
        builder.startText();
        OutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.writeText("text");
        builder.setText(buffer);
        builder.endText();
        builder.endLabel();
        builder.setHref(new LiteralLinkAssetReference("href.xml"));
        builder.endMenuItem();
        builder.endMenu();
        return builder.getCompletedMenuModel();
    }

    /**
     * Tests that the right menu item renderer for the specified configuration.
     */
    public void testPlainImage() throws Exception {
        MenuItemRendererSelector selector = createSelector();
        Menu menu = createMenu(StylesBuilder.getCompleteStyles("mcs-menu-image-style: plain; " + "mcs-menu-text-style: none"));
        MenuBuffer buffer = createMenuBuffer();
        MenuBufferLocator locator = createMenuBufferLocator(buffer);
        MenuItemRenderer renderer = selector.selectMenuItemRenderer(menu);
        assertNotNull("No renderer selected", renderer);
        renderer.render(locator, (MenuItem) menu.get(0));
        String string = toString(buffer);
        assertEquals("Output does not match", "<link" + " href=\"href.xml\">" + "<plain-image src=\"normal.gif\"/>" + "</link>", string);
    }

    /**
     * Tests that the right menu item renderer for the specified configuration.
     */
    public void testRolloverImage() throws Exception {
        MenuItemRendererSelector selector = createSelector();
        Menu menu = createMenu(StylesBuilder.getCompleteStyles("mcs-menu-image-style: rollover; " + "mcs-menu-text-style: none"));
        MenuBuffer buffer = createMenuBuffer();
        MenuBufferLocator locator = createMenuBufferLocator(buffer);
        MenuItemRenderer renderer = selector.selectMenuItemRenderer(menu);
        assertNotNull("No renderer selected", renderer);
        renderer.render(locator, (MenuItem) menu.get(0));
        String string = toString(buffer);
        assertEquals("Output does not match", "<link" + " href=\"href.xml\">" + "<rollover-image normal=\"normal.gif\" over=\"over.gif\"/>" + "</link>", string);
    }

    /**
     * Tests that the right menu item renderer for the specified configuration.
     */
    public void testPlainText() throws Exception {
        MenuItemRendererSelector selector = createSelector();
        Menu menu = createMenu(StylesBuilder.getCompleteStyles("mcs-menu-image-style: none; " + "mcs-menu-text-style: plain"));
        MenuBuffer buffer = createMenuBuffer();
        MenuBufferLocator locator = createMenuBufferLocator(buffer);
        MenuItemRenderer renderer = selector.selectMenuItemRenderer(menu);
        assertNotNull("No renderer selected", renderer);
        renderer.render(locator, (MenuItem) menu.get(0));
        String string = toString(buffer);
        assertEquals("Output does not match", "<link " + "href=\"href.xml\">" + "<plain-text>text</plain-text>" + "</link>", string);
    }
}
