package edu.unibi.agbi.biodwh.entity.kegg.medicus.environ;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class KeggEnvironCategoryId implements java.io.Serializable {

    private static final long serialVersionUID = -6779336509974465979L;

    private String entry;

    private String category;

    public KeggEnvironCategoryId() {
    }

    public KeggEnvironCategoryId(String entry, String category) {
        this.entry = entry;
        this.category = category;
    }

    @Column(name = "entry", nullable = false, length = 8)
    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    @Column(name = "category", nullable = false, length = 16)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((entry == null) ? 0 : entry.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        KeggEnvironCategoryId other = (KeggEnvironCategoryId) obj;
        if (category == null) {
            if (other.category != null) return false;
        } else if (!category.equals(other.category)) return false;
        if (entry == null) {
            if (other.entry != null) return false;
        } else if (!entry.equals(other.entry)) return false;
        return true;
    }
}
