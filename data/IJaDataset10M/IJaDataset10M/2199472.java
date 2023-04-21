package org.apache.myfaces.renderkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

/**
 * RenderKitFactory implementation as defined in Spec. JSF.7.3
 * 
 * @author Manfred Geiler (latest modification by $Author: lu4242 $)
 * @version $Revision: 824859 $ $Date: 2009-10-13 12:42:36 -0500 (Tue, 13 Oct 2009) $
 */
public class RenderKitFactoryImpl extends RenderKitFactory {

    private static final Logger log = Logger.getLogger(RenderKitFactoryImpl.class.getName());

    private Map<String, RenderKit> _renderkits = new HashMap<String, RenderKit>();

    public RenderKitFactoryImpl() {
    }

    public void purgeRenderKit() {
        _renderkits.clear();
    }

    @Override
    public void addRenderKit(String renderKitId, RenderKit renderKit) {
        if (renderKitId == null) throw new NullPointerException("renderKitId");
        if (renderKit == null) throw new NullPointerException("renderKit");
        if (log.isLoggable(Level.INFO)) {
            if (_renderkits.containsKey(renderKitId)) {
                log.info("RenderKit with renderKitId '" + renderKitId + "' was replaced.");
            }
        }
        _renderkits.put(renderKitId, renderKit);
    }

    @Override
    public RenderKit getRenderKit(FacesContext context, String renderKitId) throws FacesException {
        if (renderKitId == null) throw new NullPointerException("renderKitId");
        RenderKit renderkit = _renderkits.get(renderKitId);
        if (renderkit == null) {
            log.warning("Unknown RenderKit '" + renderKitId + "'.");
        }
        return renderkit;
    }

    @Override
    public Iterator<String> getRenderKitIds() {
        return _renderkits.keySet().iterator();
    }
}
