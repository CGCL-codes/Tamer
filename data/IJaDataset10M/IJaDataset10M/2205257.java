package net.sf.gham.core.entity.player.cellrenderer;

import net.sf.gham.core.util.Skill;

/**
 *
 * @author  Fabio Collini
 */
public class SkillCellRender extends SkillFloatCellRender {

    private static SkillCellRender singleton;

    public static SkillCellRender singleton() {
        if (singleton == null) {
            singleton = new SkillCellRender(true);
        }
        return singleton;
    }

    private static SkillCellRender singletonNoColor;

    public static SkillCellRender singletonNoColor() {
        if (singletonNoColor == null) {
            singletonNoColor = new SkillCellRender(false);
        }
        return singletonNoColor;
    }

    private SkillCellRender(boolean showColor) {
        super(showColor);
    }

    protected void setText(Skill s) {
        if (s == null) {
            setText("");
        } else {
            setText(Integer.toString((int) s.getValue()));
        }
    }
}
