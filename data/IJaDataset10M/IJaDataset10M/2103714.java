package pcgen.core.term;

import java.util.List;
import java.util.ArrayList;
import pcgen.core.PlayerCharacter;
import pcgen.core.Skill;
import pcgen.cdom.base.CDOMObject;

public class PCSkillTypeTermEvaluator extends BasePCTermEvaluator implements TermEvaluator {

    private final String type;

    public PCSkillTypeTermEvaluator(String originalText, String type) {
        this.originalText = originalText;
        this.type = type;
    }

    @Override
    public Float resolve(PlayerCharacter pc) {
        final ArrayList<Skill> skills = new ArrayList<Skill>(pc.getAllSkillList(true));
        final List<Skill> skillList = pc.getSkillListInOutputOrder(skills);
        Float typeCount = 0f;
        for (CDOMObject skill : skillList) {
            if (skill.isType(type)) {
                typeCount++;
            }
        }
        return typeCount;
    }

    public boolean isSourceDependant() {
        return false;
    }

    public boolean isStatic() {
        return false;
    }
}
