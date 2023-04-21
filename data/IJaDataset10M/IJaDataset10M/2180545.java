package net.sf.hattori.properties.monsters;

import net.sf.hattori.repository.AbstractPersistentObject;

/**
 * Monsters can be of different types. Each type defines a name and a base constitution value for all monsters of that
 * type.
 * 
 * @author jallik
 * 
 */
public class MonsterType extends AbstractPersistentObject {

    private String name;

    private Long baseConstitution;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBaseConstitution() {
        return baseConstitution;
    }

    public void setBaseConstitution(Long baseConstitution) {
        this.baseConstitution = baseConstitution;
    }
}
