package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;

/**
 * This class provides a means of rendering the (potentially) styled text part
 * of menu item. It does not handle all of the niceties that are necessary
 * around such information to make it into a functional menu item.  Higher
 * level menu renderers should be used for this purpose, and they should
 * delegate the lower level rendering (of potentially styled text items) to
 * this class.
 */
public class DefaultStyledPlainTextMenuItemRenderer extends AbstractStyledTextMenuItemRenderer {

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param spanOutput the outputter for span markup required when style is
     *                   applied to the text
     */
    public DefaultStyledPlainTextMenuItemRenderer(DeprecatedSpanOutput spanOutput) {
        super(spanOutput);
    }

    protected void writeMenuTextToBuffer(OutputBuffer output, MenuItem item) {
        OutputBuffer text = item.getLabel().getText().getText();
        output.transferContentsFrom(text);
    }
}
