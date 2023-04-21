package org.apache.myfaces.custom.skin;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SkinViewHandlerImpl extends ViewHandler {

    private static final Log _LOG = LogFactory.getLog(SkinViewHandlerImpl.class);

    public static final String ALTERNATE_VIEW_HANDLER = "org.apache.myfaces.custom.skin.ALTERNATE_VIEW_HANDLER";

    private ViewHandler _delegate;

    private boolean _inited;

    public SkinViewHandlerImpl(ViewHandler delegate) {
        _delegate = delegate;
    }

    @Override
    public Locale calculateLocale(FacesContext context) {
        return _delegate.calculateLocale(context);
    }

    @Override
    public String calculateRenderKitId(FacesContext context) {
        return _delegate.calculateRenderKitId(context);
    }

    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
        _initIfNeeded(context);
        return _delegate.createView(context, viewId);
    }

    @Override
    public String getActionURL(FacesContext context, String viewId) {
        return _delegate.getActionURL(context, viewId);
    }

    @Override
    public String getResourceURL(FacesContext context, String path) {
        return _delegate.getResourceURL(context, path);
    }

    private synchronized void _initIfNeeded(FacesContext context) {
        if (!_inited) {
            _inited = true;
            String alternateViewHandler = context.getExternalContext().getInitParameter(ALTERNATE_VIEW_HANDLER);
            if (alternateViewHandler != null) {
                ViewHandler viewHandlerInstance = null;
                try {
                    ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    Class<?> c = loader.loadClass(alternateViewHandler);
                    try {
                        Constructor<?> constructor = c.getConstructor(new Class[] { ViewHandler.class });
                        viewHandlerInstance = (ViewHandler) constructor.newInstance(new Object[] { _delegate });
                    } catch (NoSuchMethodException nsme) {
                        viewHandlerInstance = (ViewHandler) c.newInstance();
                    }
                } catch (Exception e) {
                    _LOG.warn("CANNOT_LOAD_VIEWHANDLER " + alternateViewHandler);
                    _LOG.warn(e);
                }
                if (viewHandlerInstance != null) _delegate = viewHandlerInstance;
            }
        }
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
        _initIfNeeded(context);
        _delegate.renderView(context, viewToRender);
    }

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return _delegate.restoreView(context, viewId);
    }

    @Override
    public void writeState(FacesContext context) throws IOException {
        _delegate.writeState(context);
    }
}
