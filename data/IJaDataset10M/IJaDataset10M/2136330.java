package com.jme3.shader;

import com.jme3.export.*;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class DefineList implements Savable {

    private final SortedMap<String, String> defines = new TreeMap<String, String>();

    private String compiled = null;

    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        String[] keys = new String[defines.size()];
        String[] vals = new String[defines.size()];
        int i = 0;
        for (Map.Entry<String, String> define : defines.entrySet()) {
            keys[i] = define.getKey();
            vals[i] = define.getValue();
            i++;
        }
        oc.write(keys, "keys", null);
        oc.write(vals, "vals", null);
        oc.write(compiled, "compiled", null);
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        String[] keys = ic.readStringArray("keys", null);
        String[] vals = ic.readStringArray("vals", null);
        for (int i = 0; i < keys.length; i++) {
            defines.put(keys[i], vals[i]);
        }
        compiled = ic.readString("compiled", null);
    }

    public void clear() {
        defines.clear();
        compiled = "";
    }

    public String get(String key) {
        return defines.get(key);
    }

    public boolean set(String key, VarType type, Object val) {
        if (val == null) {
            defines.remove(key);
            compiled = null;
            return true;
        }
        switch(type) {
            case Boolean:
                if (((Boolean) val).booleanValue()) {
                    if (defines.put(key, "1") != "1") {
                        compiled = null;
                        return true;
                    }
                } else if (defines.containsKey(key)) {
                    defines.remove(key);
                    compiled = null;
                    return true;
                }
                break;
            case Float:
            case Int:
                String original = defines.put(key, val.toString());
                if (!val.equals(original)) {
                    compiled = null;
                    return true;
                }
                break;
            default:
                if (defines.put(key, "1") != "1") {
                    compiled = null;
                    return true;
                }
                break;
        }
        return false;
    }

    public boolean remove(String key) {
        if (defines.remove(key) != null) {
            compiled = null;
            return true;
        }
        return false;
    }

    public void addFrom(DefineList other) {
        if (other == null) return;
        compiled = null;
        defines.putAll(other.defines);
    }

    public String getCompiled() {
        if (compiled == null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : defines.entrySet()) {
                sb.append("#define ").append(entry.getKey()).append(" ");
                sb.append(entry.getValue()).append('\n');
            }
            compiled = sb.toString();
        }
        return compiled;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, String> entry : defines.entrySet()) {
            sb.append(entry.getKey());
            if (i != defines.size() - 1) sb.append(", ");
            i++;
        }
        return sb.toString();
    }
}
