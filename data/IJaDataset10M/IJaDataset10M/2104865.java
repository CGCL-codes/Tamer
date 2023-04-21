package org.jpox.samples.compoundidentity;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Abstract class with compound identity.
 * Used to test inheritance and compound relationships.
 *
 * @version $Revision: 1.1 $
 */
public abstract class CompoundAbstractBase implements Serializable {

    private CompoundRelated related;

    private String name;

    public CompoundAbstractBase(CompoundRelated related, String name) {
        this.related = related;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompoundRelated getRelated() {
        return related;
    }

    public void setRelated(CompoundRelated rel) {
        this.related = rel;
    }

    public abstract String getValue();

    public static class Id implements Serializable {

        public CompoundRelated.Id related;

        public String name;

        public Id() {
        }

        public Id(String id) {
            StringTokenizer st = new StringTokenizer(id, "::");
            this.related = new CompoundRelated.Id(st.nextToken());
            this.name = st.nextToken();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (o.getClass() != getClass()) {
                return false;
            }
            Id other = (Id) o;
            return (this.name.equals(other.name) && this.related.equals(other.related));
        }

        public int hashCode() {
            return (name.hashCode() ^ related.hashCode());
        }

        public String toString() {
            return (this.related.toString() + "::" + this.name);
        }
    }
}
