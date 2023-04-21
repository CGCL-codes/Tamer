package ua.orion.cpu.core.eduprocplanning.entities;

import javax.persistence.*;
import ua.orion.core.annotations.UserPresentable;
import ua.orion.core.persistence.*;

/**
 * Справочник циклов дисциплин, поступающих из освтньо-професійних програм
 * @author kgp
 */
@Entity
@ReferenceBook
@AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(unique = true)), @AttributeOverride(name = "shortName", column = @Column(unique = true)) })
@UserPresentable("name")
@Cacheable
public class EPPCycle extends AbstractReferenceEntity<EPPCycle> {

    private static final long serialVersionUID = 1L;

    public EPPCycle() {
    }

    public EPPCycle(String name, String shortName) {
        this.setName(name);
        this.setShortName(shortName);
    }

    @Override
    public String toString() {
        return getName();
    }
}
