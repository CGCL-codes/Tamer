package uk.ac.cam.caret.minibix.metadata.impl;

import java.util.*;
import uk.ac.cam.caret.minibix.metadata.api.*;

public class MetadataStoreImpl implements MetadataStore {

    private Map<String, MetadataKey> data = new HashMap<String, MetadataKey>();

    private ParserFactory pf = new ParserFactoryImpl();

    private Defaults def = pf.createEmptyDefaults();

    private TypeRegistry reg = new TypeRegistryImpl();

    public MetadataKey createKey(String key, String type) {
        MetadataKey out = new MetadataKeyImpl(this, key, type);
        data.put(key, out);
        return out;
    }

    Parser getParser() {
        return pf.createParser(def);
    }

    public MetadataKey getKey(String key) {
        return data.get(key);
    }

    public void setDefaults(Map<String, String> in) {
        def = pf.createEmptyDefaults();
        for (Map.Entry<String, String> e : in.entrySet()) def.setDefault(e.getKey(), e.getValue());
    }

    public void addDefault(String k, String v) {
        def.setDefault(k, v);
    }

    public String[] getKeys() {
        return data.keySet().toArray(new String[0]);
    }

    public MetadataKey getOrCreateKey(String key, String type) {
        MetadataKey out = getKey(key);
        if (out != null) return out;
        return createKey(key, type);
    }

    public MetadataKey createKey(String key) {
        return createKey(key, getRegistry().getType(key));
    }

    public MetadataKey getOrCreateKey(String key) {
        return getOrCreateKey(key, getRegistry().getType(key));
    }

    public String serialiseDefaults() {
        return def.serialise();
    }

    public void setDefaultsFromSerialisation(String in) throws BadFormatException {
        def = pf.createDefaultsFromSerialisation(in);
    }

    public TypeRegistry getRegistry() {
        return reg;
    }

    public void setRegistry(TypeRegistry in) {
        reg = in;
    }

    public void addRegistry(TypeRegistry in) {
        reg.mergeRegistry(in);
    }
}
