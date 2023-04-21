package com.gae.spring3.newgalaxy.model.user;

import com.gae.spring3.newgalaxy.model.BaseBean;
import javax.persistence.*;

/**
 * User: Sergey
 * Date: 11.04.2010 20:48:41
 */
@Entity
@Table(name = "users")
public class User extends BaseBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idKey")
    private Long idKey;

    @Column(name = "name")
    private String name;

    @Column(name = "id")
    private Integer age;

    public User() {
    }

    public Long getIdKey() {
        return idKey;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void setIdKey(Long idKey) {
        this.idKey = idKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
