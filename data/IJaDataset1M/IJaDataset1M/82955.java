package org.boblight4j.server.config;

import java.util.Vector;

public class ConfigGroup {

    public Vector<ConfigLine> lines = new Vector<ConfigLine>();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lines == null) ? 0 : lines.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ConfigGroup other = (ConfigGroup) obj;
        if (lines == null) {
            if (other.lines != null) return false;
        } else if (!lines.equals(other.lines)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ConfigGroup [lines=" + this.lines + "]";
    }
}
