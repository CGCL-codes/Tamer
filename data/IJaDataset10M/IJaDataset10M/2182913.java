package com.jivespaces.domain;

import java.io.Serializable;

/**
 * @author <a href="mailto:rory.cn@gmail.com">somebody</a>
 * @since 2006-8-21 下午09:54:02
 * @version $Id BaseDomain.java$
 */
public abstract class BaseDomain implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
