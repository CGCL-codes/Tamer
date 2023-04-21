package test.granite.ejb3.entity;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * @author Franck WOLFF
 */
@MappedSuperclass
public abstract class Versioned implements Serializable {

    private static final long serialVersionUID = 1L;

    @Version
    private Integer version;

    public Integer getVersion() {
        return version;
    }
}
