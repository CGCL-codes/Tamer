package org.t2framework.samples.ao.entity;

import net.java.ao.Entity;

/**
 * 
 * {@.en }
 * 
 * <br />
 * 
 * {@.ja Personエンティティです.}
 * 
 * @author yone098
 * 
 */
public interface Person extends Entity {

    public Integer getId();

    public void setId(Integer id);

    public String getName();

    public void setName(String name);

    public Integer getAge();

    public void setAge(Integer age);
}
