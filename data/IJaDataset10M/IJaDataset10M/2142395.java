package transformations;

import l2.universe.gameserver.datatables.SkillTable;
import l2.universe.gameserver.instancemanager.TransformationManager;
import l2.universe.gameserver.model.L2Transformation;

public class TinGolem extends L2Transformation {

    private static final int[] SKILLS = { 940, 941, 5437, 619 };

    public TinGolem() {
        super(116, 13, 18.5);
    }

    @Override
    public void onTransform() {
        if (getPlayer().getTransformationId() != 116 || getPlayer().isCursedWeaponEquipped()) return;
        transformedSkills();
    }

    public void transformedSkills() {
        getPlayer().addSkill(SkillTable.getInstance().getInfo(940, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(941, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(5437, 2), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(SKILLS);
    }

    @Override
    public void onUntransform() {
        removeSkills();
    }

    public void removeSkills() {
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(940, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(941, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(5437, 2), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
    }

    public static void main(String[] args) {
        TransformationManager.getInstance().registerTransformation(new TinGolem());
    }
}
