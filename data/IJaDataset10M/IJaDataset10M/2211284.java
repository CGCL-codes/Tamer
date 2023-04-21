package org.zoolib;

import org.zoolib.compat.java.util.*;

public final class ZTextCollation {

    public static final class Key {

        Key(int iStrength, String iString) {
            fStrength = iStrength;
            fString = iString;
        }

        public int compareTo(Key ck) {
            return fString.compareTo(ck.fString);
        }

        public int compareTo(Object obj) {
            return this.compareTo((Key) obj);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) return false;
            return fString.equals(((Key) obj).fString);
        }

        public String getSourceString() {
            return fString;
        }

        public int hashCode() {
            return fString.hashCode();
        }

        private final String fString;

        private final int fStrength;
    }

    public static final Key sMakeKey(int iStrength, String iString) {
        return new Key(iStrength, iString);
    }
}
