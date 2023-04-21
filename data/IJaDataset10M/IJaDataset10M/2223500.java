package org.apache.myfaces.trinidadinternal.skin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.faces.context.FacesContext;
import org.apache.myfaces.trinidad.util.ClassLoaderUtils;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidadinternal.share.io.DefaultNameResolver;
import org.apache.myfaces.trinidadinternal.share.io.FileInputStreamProvider;
import org.apache.myfaces.trinidadinternal.share.io.InputStreamProvider;
import org.apache.myfaces.trinidadinternal.share.io.NameResolver;
import org.apache.myfaces.trinidadinternal.share.io.URLInputStreamProvider;
import org.apache.myfaces.trinidadinternal.style.StyleContext;

/**
 * Package-private utility class used by StyleSheetEntry to
 * locate style sheet source files.  We look for style sheets
 * in both the local and the shared "styles" directory.
 *
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/skin/StyleSheetNameResolver.java#0 $) $Date: 10-nov-2005.18:59:02 $
 */
class StyleSheetNameResolver implements NameResolver {

    /**
   * Creates a NameResolver which can locate style sheets
   * for the specified StyleContext
   */
    public static NameResolver createResolver(StyleContext context) {
        File localStylesDir = _getStylesDir(context);
        if ((localStylesDir == null)) {
            _LOG.warning(_STYLES_DIR_ERROR);
            return null;
        }
        return new StyleSheetNameResolver(localStylesDir);
    }

    /**
   * Creates a StyleSheetNameResolver which looks in the specified
   * styles directories.  Note that the constructor is private since
   * StyleSheetEntry always calls createResolver().
   * @param localStylesDirectory The location of the local styles directory
   */
    private StyleSheetNameResolver(File localStylesDirectory) {
        assert ((localStylesDirectory != null));
        _localStylesDir = localStylesDirectory;
    }

    /**
   * Implementation of NameResolver.getProvider().
   * Given the name of the file, create an InputStreamProvider. I
   */
    public InputStreamProvider getProvider(String name) throws IOException {
        File file = _resolveLocalFile(name);
        if (file != null) return new FileInputStreamProvider(file);
        URL url = _resolveNonStaticURL(name);
        if (url != null) return new URLInputStreamProvider(url); else {
            url = _resolveClassLoaderURL(name);
            if (url != null) return new StaticURLInputStreamProvider(url);
        }
        throw new FileNotFoundException(_getFileNotFoundMessage(name));
    }

    /**
   * Implementation of NameResolver.getResolver()
   */
    public NameResolver getResolver(String name) {
        URL url = null;
        File file = _resolveLocalFile(name);
        if (file == null) {
            url = _resolveNonStaticURL(name);
            if (url == null) url = _resolveClassLoaderURL(name);
        }
        return new DefaultNameResolver(file, url);
    }

    private File _resolveLocalFile(String name) {
        File file = new File(_localStylesDir, name);
        if (file.exists()) return file;
        return null;
    }

    private URL _resolveClassLoaderURL(String name) {
        if (name == null) return null;
        return ClassLoaderUtils.getResource(name);
    }

    private URL _resolveNonStaticURL(String name) {
        if (name == null) return null;
        FacesContext fContext = FacesContext.getCurrentInstance();
        if (fContext != null) {
            try {
                if (name.startsWith("http:") || name.startsWith("https:") || name.startsWith("file:") || name.startsWith("ftp:") || name.startsWith("jar:")) {
                    URL url = new URL(name);
                    if (url != null) return url;
                } else {
                    String rootName = _getRootName(name);
                    URL url = fContext.getExternalContext().getResource(rootName);
                    if (url != null) return url;
                }
            } catch (MalformedURLException e) {
                ;
            }
        }
        return null;
    }

    private String _getFileNotFoundMessage(String name) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Unable to locate style sheet \"");
        buffer.append(name);
        buffer.append("\" in ");
        if (_localStylesDir != null) {
            buffer.append("local styles directory (");
            buffer.append(_localStylesDir.getPath());
            buffer.append("), ");
        }
        buffer.append("or on the class path.\n");
        buffer.append("Please be sure that this style sheet is installed.");
        return buffer.toString();
    }

    private static File _getStylesDir(StyleContext context) {
        String contextPath = context.getGeneratedFilesPath();
        if (contextPath == null) return null;
        String stylesPath = contextPath + "/adf/styles";
        File stylesDir = new File(stylesPath);
        if (stylesDir.exists()) return stylesDir;
        return null;
    }

    private static String _getRootName(String name) {
        return (name.startsWith("/")) ? name : ("/" + name);
    }

    private static class StaticURLInputStreamProvider extends URLInputStreamProvider {

        public StaticURLInputStreamProvider(URL url) {
            super(url);
        }

        @Override
        public boolean hasSourceChanged() {
            return false;
        }
    }

    private File _localStylesDir;

    private static final String _STYLES_DIR_ERROR = "Could not locate the Trinidad styles directory." + "Please be sure that the Trinidad installable resources are installed.";

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(StyleSheetNameResolver.class);
}
