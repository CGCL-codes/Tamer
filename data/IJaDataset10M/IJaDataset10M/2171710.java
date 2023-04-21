package com.rcreations.hibernate.appstate;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Table shared by all applications, stores simple name/value pairs global to entire application.
 */
@Entity
@Table(name = AppStateDb.TABLE_NAME)
public class AppStateDb implements Serializable {

    static final long serialVersionUID = 1L;

    static final String TABLE_NAME = "APP_STATE";

    @Id
    @Column(name = "NAME", length = 100)
    public String name;

    @Column(name = "VALUE", nullable = true, length = 4000)
    public String value;
}
