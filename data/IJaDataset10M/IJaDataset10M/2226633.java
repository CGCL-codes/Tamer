package transformations;

import l2.universe.gameserver.datatables.SkillTable;
import l2.universe.gameserver.instancemanager.TransformationManager;
import l2.universe.gameserver.model.L2Transformation;

public class AurabirdOwl extends L2Transformation {

    private static final int[] SKILLS = new int[] { 884, 885, 887, 889, 892, 893, 895, 911, 932, 619 };

    public AurabirdOwl() {
        super(9, 40, 18.57);
    }

    @Override
    public void onTransform() {
        if (getPlayer().getTransformationId() != 9 || getPlayer().isCursedWeaponEquipped()) return;
        getPlayer().setIsFlyingMounted(true);
        transformedSkills();
    }

    public void transformedSkills() {
        if (getPlayer().getLevel() >= 75) getPlayer().addSkill(SkillTable.getInstance().getInfo(885, 1), false);
        if (getPlayer().getLevel() >= 83) getPlayer().addSkill(SkillTable.getInstance().getInfo(895, 1), false);
        int lvl = getPlayer().getLevel() - 74;
        if (lvl > 0) {
            getPlayer().addSkill(SkillTable.getInstance().getInfo(884, lvl), false);
            getPlayer().addSkill(SkillTable.getInstance().getInfo(887, lvl), false);
            getPlayer().addSkill(SkillTable.getInstance().getInfo(889, lvl), false);
            getPlayer().addSkill(SkillTable.getInstance().getInfo(892, lvl), false);
            getPlayer().addSkill(SkillTable.getInstance().getInfo(893, lvl), false);
            getPlayer().addSkill(SkillTable.getInstance().getInfo(911, lvl), false);
        }
        getPlayer().addSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(SKILLS);
    }

    @Override
    public void onUntransform() {
        getPlayer().setIsFlyingMounted(false);
        removeSkills();
    }

    public void removeSkills() {
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(885, 1), false);
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(895, 1), false);
        int lvl = getPlayer().getLevel() - 74;
        if (lvl > 0) {
            getPlayer().removeSkill(SkillTable.getInstance().getInfo(884, lvl), false);
            getPlayer().removeSkill(SkillTable.getInstance().getInfo(887, lvl), false);
            getPlayer().removeSkill(SkillTable.getInstance().getInfo(889, lvl), false);
            getPlayer().removeSkill(SkillTable.getInstance().getInfo(892, lvl), false);
            getPlayer().removeSkill(SkillTable.getInstance().getInfo(893, lvl), false);
            getPlayer().removeSkill(SkillTable.getInstance().getInfo(911, lvl), false);
        }
        getPlayer().removeSkill(SkillTable.getInstance().getInfo(619, 1), false);
        getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
    }

    public static void main(String[] args) {
        TransformationManager.getInstance().registerTransformation(new AurabirdOwl());
    }
}
