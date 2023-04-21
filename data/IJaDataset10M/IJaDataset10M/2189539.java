package net.sf.joafip.entity;

import net.sf.joafip.NotStorableClass;

@NotStorableClass
public class FilePersistencePropertyEntry implements Comparable<FilePersistencePropertyEntry> {

    private final String[] strings;

    public FilePersistencePropertyEntry(final String[] strings) {
        super();
        if (strings.length < 1) {
            throw new IllegalArgumentException("must have at least 1 entry");
        }
        this.strings = strings;
    }

    public String[] getStrings() {
        return strings;
    }

    @Override
    public int compareTo(final FilePersistencePropertyEntry oEntry) {
        return strings[0].compareTo(oEntry.strings[0]);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + strings[0].hashCode();
        return result;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FilePersistencePropertyEntry other = (FilePersistencePropertyEntry) obj;
        if (!strings[0].equals(other.strings[0])) return false;
        return true;
    }
}
