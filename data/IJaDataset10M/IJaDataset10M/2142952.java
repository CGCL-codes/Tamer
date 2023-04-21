package castadiva.classpath;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class classpathModifier {

    private static final String METODO_ADD_URL = "addURL";

    private static final Class[] PARAMETRO_METODO = new Class[] { URL.class };

    private final URLClassLoader loader;

    private final Method metodoAdd;

    public classpathModifier() throws NoSuchMethodException {
        loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        metodoAdd = URLClassLoader.class.getDeclaredMethod(METODO_ADD_URL, PARAMETRO_METODO);
        metodoAdd.setAccessible(true);
    }

    public URL[] getURLs() {
        return loader.getURLs();
    }

    public void addURL(URL url) {
        if (url != null) {
            try {
                metodoAdd.invoke(loader, new Object[] { url });
            } catch (Exception ex) {
                System.err.println("Excepcion al guardar URL: " + ex.getLocalizedMessage());
            }
        }
    }

    public void addURLs(URL[] urls) {
        if (urls != null) {
            for (URL url : urls) {
                addURL(url);
            }
        }
    }

    public void addFile(File archivo) throws MalformedURLException {
        if (archivo != null) {
            addURL(archivo.toURI().toURL());
        }
    }

    public void addFile(String nombreArchivo) throws MalformedURLException {
        addFile(new File(nombreArchivo));
    }
}
