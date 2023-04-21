package com.hs.domain;

import java.io.Serializable;

/**
 * @author <a href="mailto:guangzong@gmail.com">Guangzong Syu</a>
 *
 */
public class SalaryType implements Serializable {

    private static final long serialVersionUID = -930273362928936754L;

    private Long id;

    private String type;

    /**
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(String type) {
        this.type = type;
    }
}
