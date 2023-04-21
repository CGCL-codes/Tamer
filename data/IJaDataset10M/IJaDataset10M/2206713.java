package com.unsins.core.basedomain;

import com.unsins.core.BaseObject;
import javax.persistence.MappedSuperclass;

/**
 * Created by IntelliJ IDEA.
 * User: odpsoft
 * Date: 2008-11-26
 * Time: 14:32:53
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public abstract class BaseDomain extends BaseObject {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
