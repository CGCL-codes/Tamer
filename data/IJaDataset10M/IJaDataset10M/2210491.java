package org.jcvi.glk;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cascade;

/**
 * A Hibernate composite key for {@link LibraryStat}.
 * 
 * @author jsitz
 * @author dkatzel
 */
@Embeddable
public class LibraryStatId implements Serializable {

    /** The Serial Version UID */
    private static final long serialVersionUID = 6168624520675723073L;

    private LibraryStatGroup group;

    private LibraryStatType type;

    /**
     * Creates a new <code>LibraryStatId</code>.
     */
    public LibraryStatId() {
        super();
    }

    /**
     * Creates a new <code>LibraryStatId</code>. 
     *
     * @param group The {@link LibraryStatGroup} which contains this stat.
     * @param type The {@link LibraryStatType} of this stat.
     */
    public LibraryStatId(LibraryStatGroup group, LibraryStatType type) {
        this();
        this.group = group;
        this.type = type;
    }

    /**
     * Retrieves the {@link LibraryStatGroup} which contains this stat.
     *
     * @return The associated {@link LibraryStatGroup}.
     */
    @ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinColumn(name = "Library_Experiment_id", unique = false, nullable = false, insertable = true, updatable = true)
    public LibraryStatGroup getGroup() {
        return this.group;
    }

    /**
     * Sets the {@link LibraryStatGroup} which contains this stat.
     * 
     * @param group An {@link LibraryStatGroup}.
     */
    public void setGroup(LibraryStatGroup group) {
        this.group = group;
    }

    /**
     * Retrieves the type of this stat.
     *
     * @return The associated {@link LibraryStatType}.
     */
    @ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinColumn(name = "Library_Stats_Template_id", unique = false, nullable = false, insertable = true, updatable = true)
    public LibraryStatType getType() {
        return this.type;
    }

    /**
     * Sets the type of this stat.
     * 
     * @param type A {@link LibraryStatType}.
     */
    public void setType(LibraryStatType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getGroup() == null) ? 0 : this.getGroup().hashCode());
        result = prime * result + ((this.getType() == null) ? 0 : this.getType().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof LibraryStatId)) return false;
        final LibraryStatId other = (LibraryStatId) obj;
        if (this.getGroup() == null) {
            if (other.getGroup() != null) return false;
        } else if (!this.getGroup().equals(other.getGroup())) return false;
        if (this.getType() == null) {
            if (other.getType() != null) return false;
        } else if (!this.getType().equals(other.getType())) return false;
        return true;
    }
}
