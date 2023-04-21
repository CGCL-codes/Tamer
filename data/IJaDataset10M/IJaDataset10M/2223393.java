package oxygen.forum.data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class ForumEntity implements Serializable, Comparable {

    @Version
    @Column(name = "f_version", nullable = false)
    private Long version;

    public abstract Long getId();

    public abstract void setId(Long id);

    public int compareTo(Object o) {
        return getId().compareTo(((ForumEntity) o).getId());
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
