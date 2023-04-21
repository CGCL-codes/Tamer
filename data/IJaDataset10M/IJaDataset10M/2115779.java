package net.sourceforge.agiltestlist.core;

import java.io.Serializable;
import javax.persistence.Version;

public abstract class DomainObject implements Serializable {

    @Version
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
