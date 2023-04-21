package net.sourceforge.nattable.typeconfig.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map;
import net.sourceforge.nattable.typeconfig.ConfigRegistry;
import net.sourceforge.nattable.typeconfig.TypedCellOverrider;
import net.sourceforge.nattable.typeconfig.TypedRowOverrider;
import net.sourceforge.nattable.typeconfig.style.IStyleConfig;

public class ConfigRegistryStylePersistor<T> extends AbstractPersistor {

    private static final long serialVersionUID = 1L;

    private ConfigRegistry styleRegistry;

    private TypedCellOverrider<T> cellOverrider;

    private TypedRowOverrider<T> rowOverrider;

    private StyleMapStorer styleMapRestorer;

    public ConfigRegistryStylePersistor(ConfigRegistry registry, TypedCellOverrider<T> cellOverrider, TypedRowOverrider<T> rowOverrider) {
        styleRegistry = registry;
        this.cellOverrider = cellOverrider;
        this.rowOverrider = rowOverrider;
    }

    @SuppressWarnings("unchecked")
    public void load(InputStream source) {
        try {
            ConfigRegistryStylePersistor<T> restorer = (ConfigRegistryStylePersistor<T>) restore(source);
            styleRegistry.getStyleConfigMap().putAll(restorer.styleMapRestorer.restoredTypedStyleConfigMap);
            styleRegistry.getDefaultStyleConfigMap().putAll(restorer.styleMapRestorer.restoredStyleConfigMap);
            cellOverrider.addOverrides(restorer.styleMapRestorer.restoredCellOverride);
            rowOverrider.addOverrides(restorer.styleMapRestorer.restoredRowOverride);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void save(OutputStream store) {
        try {
            store(store);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        StyleMapStorer storer = new StyleMapStorer();
        storer.restoredTypedStyleConfigMap = styleRegistry.getStyleConfigMap();
        storer.restoredStyleConfigMap = styleRegistry.getDefaultStyleConfigMap();
        storer.restoredCellOverride = cellOverrider.getOverrides();
        storer.restoredRowOverride = rowOverrider.getOverrides();
        stream.writeObject(storer);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        styleMapRestorer = (StyleMapStorer) stream.readObject();
    }

    @SuppressWarnings("unused")
    private void readObjectNoData() throws ObjectStreamException {
    }

    class StyleMapStorer implements Serializable {

        private static final long serialVersionUID = 1L;

        Map<String, Map<String, IStyleConfig>> restoredTypedStyleConfigMap;

        Map<String, IStyleConfig> restoredStyleConfigMap;

        Map<Serializable, String> restoredCellOverride;

        Map<Serializable, String> restoredRowOverride;
    }
}
