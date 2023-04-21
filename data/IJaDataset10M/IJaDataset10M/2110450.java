package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.menu.MenuModuleTestAbstract;
import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.protocols.menu.MenuModuleCustomisation;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedOutputLocator;
import com.volantis.mcs.protocols.renderer.RendererContext;

/**
 * Test the default menu module.
 */
public class DefaultMenuModuleTestCase extends MenuModuleTestAbstract {

    protected MenuModule createMenuModule(RendererContext rendererContext, MenuModuleRendererFactory rendererFactory) {
        return new DefaultMenuModule(rendererContext, rendererFactory);
    }
}
