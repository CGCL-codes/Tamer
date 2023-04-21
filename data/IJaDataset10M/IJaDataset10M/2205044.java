package com.sks.bean.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.compass.annotations.Searchable;

/**
 * Config entity. @author MyEclipse Persistence Tools
 */
@Entity(name = "config")
@Searchable(root = false)
@Table(name = "config", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Config implements java.io.Serializable {

    private Integer configId;

    private String name;

    /** default constructor */
    public Config() {
    }

    /** full constructor */
    public Config(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "configId", unique = true, nullable = false)
    public Integer getConfigId() {
        return this.configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    @Column(name = "name", unique = true, nullable = false, length = 40)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Config) {
            Config con = (Config) obj;
            return this.configId == con.getConfigId();
        }
        return false;
    }
}
