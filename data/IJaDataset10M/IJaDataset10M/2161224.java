package csel.model.skills;

import csel.model.entity.Character;

public abstract class RangedActiveSkill extends ActiveSkill {

    private static final long serialVersionUID = 5L;

    public RangedActiveSkill(Character owner) {
        super(owner);
    }
}
