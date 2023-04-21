package transformations;

import l2.universe.gameserver.datatables.SkillTable;
import l2.universe.gameserver.instancemanager.TransformationManager;
import l2.universe.gameserver.model.L2Transformation;

public class MyoRace extends L2Transformation {

    private static final int[] SKILLS = { 896, 897, 898, 899, 900, 5491, 619 };

    public MyoRace() {
        super(219, 10, 23);
    }

    @Override
    public void onTransform() {
        if (getPlayer().getTransformationId() != 219 || getPlayer().isCursedWeaponEquipped()) return;
        if (getPlayer().getPet() != null) getPlayer().getPet().unSummon(getPlayer());
        transformedSkills();
    }

    public void transformedSkills() {
        getPlayer().addSkill(SkillTable.getInstance().getInfo(896, 4), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(897, 4), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(898, 4), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(899, 4), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(900, 4), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(5491, 1), false);
        getPlayer().addSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(SKILLS);
    }

    @Override
    public void onUntransform() {
        removeSkills();
    }

    public void removeSkills() {
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(896, 4), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(897, 4), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(898, 4), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(899, 4), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(900, 4), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(5491, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
    }

    public static void main(String[] args) {
        TransformationManager.getInstance().registerTransformation(new MyoRace());
    }
}
