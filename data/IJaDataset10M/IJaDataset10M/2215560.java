package org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.jsLibs;

import java.io.IOException;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import javax.faces.context.FacesContext;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.XhtmlUtils;
import org.apache.myfaces.trinidadinternal.resource.CoreRenderKitResourceLoader;

/**
 *
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 */
public class LibraryScriptlet extends Scriptlet {

    public LibraryScriptlet(String libraryName, String[] functions) {
        this(libraryName, functions, null);
    }

    public LibraryScriptlet(String libraryName, Object[] functions, Object[] dependencies) {
        _libraryName = libraryName;
        _functions = (functions == null) ? null : functions.clone();
        _dependencies = (dependencies == null) ? null : dependencies.clone();
    }

    @Override
    public void registerSelf() {
        super.registerSelf();
        if (_functions != null) {
            for (int i = 0; i < _functions.length; i++) registerSelfWithKey(_functions[i]);
        }
    }

    @Override
    public Object getScriptletKey() {
        return _libraryName;
    }

    /**
   * given a libraryName, return the versioned-name
   * if versioning if on.
   * @param context
   * @param libraryName
   * @return
   */
    public static String getLibraryNameWithVersion(FacesContext context, String libraryName) {
        if (_useLibraryVersions()) return libraryName + _LIBRARY_VERSION;
        return libraryName;
    }

    @Override
    protected void outputScriptletImpl(FacesContext context, RenderingContext arc) throws IOException {
        if (_dependencies != null) {
            for (int i = 0; i < _dependencies.length; i++) outputDependency(context, arc, _dependencies[i]);
        }
        XhtmlUtils.writeLibImport(context, arc, getLibraryURL(context, arc));
    }

    @Override
    protected void outputScriptletContent(FacesContext context, RenderingContext arc) throws IOException {
    }

    @Override
    protected void embedInScriptTagImpl(FacesContext context, RenderingContext arc) throws IOException {
        throw new IllegalStateException();
    }

    protected String getLibraryName(FacesContext context, RenderingContext arc) {
        String libraryName = _libraryName;
        if (_isDebug(context)) libraryName = "Debug" + libraryName;
        return libraryName;
    }

    protected String getExtraParameters(FacesContext context, RenderingContext arc) {
        return null;
    }

    protected String getLibraryURL(FacesContext context, RenderingContext arc) {
        String libraryName = getLibraryName(context, arc);
        StringBuffer libURL = new StringBuffer(80);
        libURL.append(context.getExternalContext().getRequestContextPath());
        libURL.append(getBaseLibURL());
        libURL.append(libraryName);
        if (_useLibraryVersions()) libURL.append(_LIBRARY_VERSION);
        libURL.append(".js");
        String extraParams = getExtraParameters(context, arc);
        if (extraParams != null) libURL.append(extraParams);
        return libURL.toString();
    }

    public static String getBaseLibURL() {
        return _JSLIBS_DIRECTORY;
    }

    /**
   * @todo Re-enable disabling versioning??
   */
    private static boolean _useLibraryVersions() {
        if (_LIBRARY_VERSION == null) return false;
        return true;
    }

    private static boolean _isDebug(FacesContext context) {
        if (_debugJavascript == null) {
            String debugJavascript = context.getExternalContext().getInitParameter(_DEBUG_JAVASCRIPT);
            if ((debugJavascript != null) && debugJavascript.equalsIgnoreCase("true")) {
                _debugJavascript = Boolean.TRUE;
                _LOG.info("RUNNING_DEBUG_JAVASCRIPT");
            } else {
                _debugJavascript = Boolean.FALSE;
            }
        }
        return _debugJavascript.booleanValue();
    }

    private final String _libraryName;

    private final Object[] _functions;

    private final Object[] _dependencies;

    private static Boolean _debugJavascript;

    private static final String _DEBUG_JAVASCRIPT = "org.apache.myfaces.trinidad.DEBUG_JAVASCRIPT";

    private static String _LIBRARY_VERSION = null;

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(LibraryScriptlet.class);

    private static final String _JSLIBS_DIRECTORY = "/adf/jsLibs/";

    static {
        _LIBRARY_VERSION = CoreRenderKitResourceLoader.__getVersion();
    }
}
