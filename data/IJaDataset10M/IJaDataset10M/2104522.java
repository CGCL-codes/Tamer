package com.jme.util.export;

import java.io.IOException;
import java.util.Map;

/**
 * A Savable String-to-Int map.
 */
public class StringIntMap extends AbstractStringKeyMap<Integer> {

    static final long serialVersionUID = -6443631854556869718L;

    public StringIntMap() {
        super();
    }

    public StringIntMap(int initialCapacity) {
        super(initialCapacity);
    }

    public StringIntMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public StringIntMap(Map<? extends String, ? extends Integer> m) {
        super(m);
    }

    /**
     * Subclasses must super.write(e)!
     */
    public void write(JMEExporter e) throws IOException {
        super.write(e);
        int[] nativeArray = new int[size()];
        int i = -1;
        for (int integ : values()) nativeArray[++i] = integ;
        e.getCapsule(this).write(nativeArray, "vals", null);
    }

    public void read(JMEImporter e) throws IOException {
        super.read(e);
        int[] vals = e.getCapsule(this).readIntArray("vals", null);
        if (unsavedKeys == null) throw new IOException("Keys not stored");
        if (vals == null) throw new IOException("Vals not stored");
        if (unsavedKeys.length != vals.length) throw new IOException("Key/Val size mismatch: " + unsavedKeys.length + " vs. " + vals.length);
        for (int i = 0; i < unsavedKeys.length; i++) put(unsavedKeys[i], vals[i]);
    }
}
