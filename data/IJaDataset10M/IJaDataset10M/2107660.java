package net.sourceforge.solexatools.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import net.sourceforge.solexatools.model.*;

public class LibrarySelection implements Serializable, Comparable<LibrarySelection> {

    private static final long serialVersionUID = 3611345318915990568L;

    private Integer librarySelectionId;

    private String name;

    private String description;

    public LibrarySelection() {
        super();
    }

    public int compareTo(LibrarySelection that) {
        if (that == null) return -1;
        if (that.getName() == this.getName()) return 0;
        if (that.getName() == null) return -1;
        return (that.getName().compareTo(this.getName()));
    }

    public String toString() {
        return new ToStringBuilder(this).append("librarySelectionId", getLibrarySelectionId()).toString();
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if (!(other instanceof LibrarySelection)) return false;
        LibrarySelection castOther = (LibrarySelection) other;
        return new EqualsBuilder().append(this.getName(), castOther.getName()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getName()).toHashCode();
    }

    public Integer getLibrarySelectionId() {
        return librarySelectionId;
    }

    public void setLibrarySelectionId(Integer librarySelectionId) {
        this.librarySelectionId = librarySelectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
