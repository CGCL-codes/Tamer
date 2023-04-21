package com.ma_la.myRunning.domain;

import com.ma_la.myRunning.*;
import java.io.Serializable;

/**
 * JoJo fuer Streckenprofil
 *
 * @author <a href="mailto:mail@myrunning.de">Martin Lang</a>
 */
public class RouteProfile implements IdNameInterface, Serializable {

    private static final long serialVersionUID = 7414174634119691814L;

    private Long id;

    private String name;

    public RouteProfile() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
