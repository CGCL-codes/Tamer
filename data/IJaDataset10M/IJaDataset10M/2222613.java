package artofillusion;

import artofillusion.math.*;
import artofillusion.object.*;
import artofillusion.ui.*;
import java.io.*;
import java.util.*;

/** This class keeps track of program-wide user preferences. */
public class ApplicationPreferences {

    private Properties properties;

    private int defaultDisplayMode, undoLevels;

    private double interactiveTol;

    private Renderer objectPreviewRenderer, texturePreviewRenderer, defaultRenderer;

    public ApplicationPreferences() {
        loadPreferences();
    }

    /** Load the preferences from disk. */
    public void loadPreferences() {
        properties = new Properties();
        initDefaultPreferences();
        File f = new File(System.getProperty("user.home"), ".aoiprefs");
        if (!f.exists()) {
            Translate.setLocale(Locale.getDefault());
            return;
        }
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(f));
            properties.load(in);
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        parsePreferences();
    }

    /** Save any changed preferences to disk. */
    public void savePreferences() {
        File f = new File(System.getProperty("user.home"), ".aoiprefs");
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
            properties.store(out, "Art of Illusion Preferences File");
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Initialize internal variables to reasonable defaults. */
    private void initDefaultPreferences() {
        Renderer renderers[] = ModellingApp.getRenderers();
        if (renderers.length > 0) objectPreviewRenderer = texturePreviewRenderer = defaultRenderer = renderers[renderers.length - 1];
        defaultDisplayMode = ViewerCanvas.RENDER_SMOOTH;
        interactiveTol = 0.05;
        undoLevels = 6;
    }

    /** Parse the properties loaded from the preferences file. */
    private void parsePreferences() {
        objectPreviewRenderer = getNamedRenderer(properties.getProperty("objectPreviewRenderer"));
        texturePreviewRenderer = getNamedRenderer(properties.getProperty("texturePreviewRenderer"));
        defaultRenderer = getNamedRenderer(properties.getProperty("defaultRenderer"));
        defaultDisplayMode = parseIntProperty("defaultDisplayMode", defaultDisplayMode);
        interactiveTol = parseDoubleProperty("interactiveSurfaceError", interactiveTol);
        undoLevels = parseIntProperty("undoLevels", undoLevels);
        Translate.setLocale(parseLocaleProperty("language"));
    }

    /** Parse an integer valued property. */
    private int parseIntProperty(String name, int defaultVal) {
        try {
            return Integer.parseInt(properties.getProperty(name));
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    /** Parse a double valued property. */
    private double parseDoubleProperty(String name, double defaultVal) {
        try {
            return new Double(properties.getProperty(name)).doubleValue();
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    /** Parse a property specifying a locale. */
    private Locale parseLocaleProperty(String name) {
        try {
            String desc = properties.getProperty(name);
            String language = desc.substring(0, 2);
            String country = desc.substring(3);
            return new Locale(language, country);
        } catch (Exception ex) {
            return Locale.getDefault();
        }
    }

    /** Look up a renderer by name. */
    private Renderer getNamedRenderer(String name) {
        Renderer renderers[] = ModellingApp.getRenderers();
        if (renderers.length == 0) return null;
        for (int i = 0; i < renderers.length; i++) if (renderers[i].getName().equals(name)) return renderers[i];
        return renderers[renderers.length - 1];
    }

    /** Get the default renderer. */
    public final Renderer getDefaultRenderer() {
        return defaultRenderer;
    }

    /** Set the default renderer. */
    public final void setDefaultRenderer(Renderer rend) {
        defaultRenderer = rend;
        properties.put("defaultRenderer", rend.getName());
    }

    /** Get the object preview renderer. */
    public final Renderer getObjectPreviewRenderer() {
        return objectPreviewRenderer;
    }

    /** Set the object preview renderer. */
    public final void setObjectPreviewRenderer(Renderer rend) {
        objectPreviewRenderer = rend;
        properties.put("objectPreviewRenderer", rend.getName());
    }

    /** Get the texture preview renderer. */
    public final Renderer getTexturePreviewRenderer() {
        return texturePreviewRenderer;
    }

    /** Set the texture preview renderer. */
    public final void setTexturePreviewRenderer(Renderer rend) {
        texturePreviewRenderer = rend;
        properties.put("texturePreviewRenderer", rend.getName());
    }

    /** Get the default display mode. */
    public final int getDefaultDisplayMode() {
        return defaultDisplayMode;
    }

    /** Set the default display mode. */
    public final void setDefaultDisplayMode(int mode) {
        defaultDisplayMode = mode;
        properties.put("defaultDisplayMode", Integer.toString(mode));
    }

    /** Get the interactive surface error. */
    public final double getInteractiveSurfaceError() {
        return interactiveTol;
    }

    /** Set the interactive surface error. */
    public final void setInteractiveSurfaceError(double tol) {
        boolean changed = (interactiveTol != tol);
        interactiveTol = tol;
        properties.put("interactiveSurfaceError", Double.toString(tol));
        if (changed) {
            EditingWindow windows[] = ModellingApp.getWindows();
            for (int i = 0; i < windows.length; i++) {
                Scene sc = windows[i].getScene();
                if (sc == null) continue;
                for (int j = 0; j < sc.getNumObjects(); j++) {
                    ObjectInfo info = sc.getObject(j);
                    Vec3 size = info.getBounds().getSize();
                    info.object.setSize(size.x, size.y, size.z);
                    info.clearCachedMeshes();
                }
                windows[i].updateImage();
            }
        }
    }

    /** Get the locale for displaying text. */
    public final Locale getLocale() {
        return Translate.getLocale();
    }

    /** Set the locale for displaying text. */
    public final void setLocale(Locale locale) {
        Translate.setLocale(locale);
        properties.put("language", locale.getLanguage() + '_' + locale.getCountry());
    }

    /** Get the number of levels of Undo to support. */
    public final int getUndoLevels() {
        return undoLevels;
    }

    /** Set the number of levels of Undo to support. */
    public final void setUndoLevels(int levels) {
        undoLevels = levels;
        properties.put("undoLevels", Integer.toString(levels));
    }
}
