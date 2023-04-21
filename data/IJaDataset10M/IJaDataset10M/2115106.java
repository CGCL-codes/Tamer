package net.sf.jsfcomp.hibernatetrace.domain;

import java.io.Serializable;

/**
 * @author Mert Caliskan
 * 
 */
public class BasePersistentObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
