package org.itsnat.feashow.features.core;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;

public class PrettyURLLoadListener implements ItsNatServletRequestListener {

    public PrettyURLLoadListener() {
    }

    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response) {
        ItsNatDocument itsNatDoc = request.getItsNatDocument();
        new PrettyURLListener(itsNatDoc);
    }
}
