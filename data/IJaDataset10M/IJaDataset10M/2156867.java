package com.volantis.mcs.protocols.renderer.layouts.spatial.nested;

import com.volantis.mcs.layouts.spatial.CoordinateConverter;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.spatial.AbstractSpatialRenderer;
import com.volantis.styling.Styles;

public class NestedRenderer extends AbstractSpatialRenderer {

    protected void render(FormatRendererContext context, FormatInstance instance, Styles formatStyles, CoordinateConverter converter) throws RendererException {
        NestedSpatialFormatIteratorHandler handler = new NestedSpatialFormatIteratorHandler(context, instance, formatStyles, converter);
        handler.render();
    }
}
