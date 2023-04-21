package net.sf.dbchanges.fs;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author olex
 */
public class Script implements Comparable<Script> {

    private String path;

    public Script(String relativePath) {
        this.path = relativePath;
    }

    public String getPath() {
        return path;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        Script script = (Script) obj;
        return new EqualsBuilder().append(path, script.path).isEquals();
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return path.hashCode();
    }

    /**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(Script script) {
        return path.compareTo(script.path);
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return path;
    }
}
