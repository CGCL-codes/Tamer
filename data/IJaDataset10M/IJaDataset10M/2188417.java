package resources.digesters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import resources.PropertiesLoader;
import utils.IoUtil;

/**
 * Esta clase representa la ubicaci�n de un plug-in.
 */
public class PluginLocation {

    private final URL context;

    private final URL manifest;

    public PluginLocation(final URL aContext, final URL aManifest) {
        if (aContext == null) {
            throw new NullPointerException("context");
        }
        if (aManifest == null) {
            throw new NullPointerException("manifest");
        }
        context = aContext;
        manifest = aManifest;
    }

    /**
	 * Crea la ubicaci�n del plug-in para el archivo o directorio pasado por
	 * par�metro, y verifica que todos los recursos requeridos est�n disponibles.
	 * Antes de crear la instancia <code>PluginLocation</code>, este m�todo
	 * examina el directorio en busca del archivo <code>plugin.properties</code>. 
     * @param file archivo (JAR o ZIP) o directorio conteniendo el plug-in.
     * @return 	ubicaci�n del plug-in, o <code>null</code> si el arcchivo o
     * 			directorio pasado por par�metro no contiene un plug-in.
     * @throws MalformedURLException si la URL del plug-in no pudo crearse.
     */
    public static PluginLocation create(final File file) throws MalformedURLException {
        if (file.isDirectory()) {
            URL manifestUrl = getManifestUrl(file);
            return (manifestUrl == null) ? null : new PluginLocation(IoUtil.file2url(file), manifestUrl);
        }
        String fileName = file.getName().toLowerCase(Locale.getDefault());
        if (!fileName.endsWith(".jar") && !fileName.endsWith(".zip")) {
            return null;
        }
        URL manifestUrl = getManifestUrl(file);
        return (manifestUrl == null) ? null : new PluginLocation(new URL("jar:" + IoUtil.file2url(file).toExternalForm() + "!/"), manifestUrl);
    }

    private static URL getManifestUrl(final File file) throws MalformedURLException {
        if (file.isDirectory()) {
            File result = new File(file, PropertiesLoader.getInstance().getProperty("PluginFileNamePrefix") + ".properties");
            if (result.isFile()) {
                return IoUtil.file2url(result);
            }
        }
        if (!file.isFile()) {
            return null;
        }
        URL url = new URL("jar:" + IoUtil.file2url(file).toExternalForm() + "!/" + PropertiesLoader.getInstance().getProperty("PluginFileNamePrefix") + ".properties");
        if (IoUtil.isResourceExists(url)) {
            return url;
        }
        return null;
    }

    public URL getManifestLocation() {
        return manifest;
    }

    public URL getContextLocation() {
        return context;
    }
}
