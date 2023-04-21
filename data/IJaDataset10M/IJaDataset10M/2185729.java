package com.volantis.mcs.xdime.gallery;

import com.volantis.mcs.protocols.gallery.attributes.PagesCountAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;

public class PagesCountElement extends BaseGalleryElement {

    public PagesCountElement(XDIMEContextInternal context) {
        super(GalleryElements.PAGES_COUNT, context);
        protocolAttributes = new PagesCountAttributes();
    }
}
