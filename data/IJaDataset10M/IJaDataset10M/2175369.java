package net.planetrenner.picit.sdcc.preferences;

import net.planetrenner.picit.sdcc.SdccPlugin;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;

public class SdccProperties {

    public static final String QUALIFIER = SdccPlugin.PLUGIN_ID;

    public static final String PREF_SEARCH_PATH = "searchPath";

    public static final String PREF_PORT = "port";

    public static final String PREF_PROCESSOR = "processor";

    public static final Port DEFAULT_PORT = Port.PIC16;

    public static final String DEFAULT_PROCESSOR = "18f4550";

    private final IEclipsePreferences preferences;

    private Port port;

    private String processor;

    private boolean dirty = false;

    SdccProperties(IScopeContext context) {
        this.preferences = context.getNode(QUALIFIER);
        load();
    }

    SdccProperties(SdccProperties source, IScopeContext context) {
        this.preferences = context.getNode(QUALIFIER);
        copyAttributes(source);
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        if (!this.port.equals(port)) {
            this.port = port;
            dirty = true;
        }
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        if (!this.processor.equals(processor)) {
            this.processor = processor;
            dirty = true;
        }
    }

    protected void load() {
        port = Port.valueOf(preferences.get(PREF_PORT, DEFAULT_PORT.toString()));
        processor = preferences.get(PREF_PROCESSOR, DEFAULT_PROCESSOR);
    }

    protected void save() throws BackingStoreException {
        if (dirty) {
            preferences.put(PREF_PORT, port.toString());
            preferences.put(PREF_PROCESSOR, processor);
            preferences.flush();
            dirty = false;
        }
    }

    protected void copyFrom(SdccProperties sdccProperties) {
        copyAttributes(sdccProperties);
    }

    private void copyAttributes(SdccProperties source) {
        this.port = source.port;
        this.processor = new String(source.processor);
        this.dirty = source.dirty;
    }
}
