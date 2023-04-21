package pcgen.base.term;

import java.util.List;
import pcgen.core.AbilityCategory;
import pcgen.core.PlayerCharacter;
import pcgen.core.Ability;

public class PCCountAbilitiesNatureVirtualTermEvaluator extends BasePCCountAbilitiesNatureTermEvaluator implements TermEvaluator {

    public PCCountAbilitiesNatureVirtualTermEvaluator(String originalText, AbilityCategory abCat, boolean visible, boolean hidden) {
        this.originalText = originalText;
        this.abCat = abCat;
        this.visible = visible;
        this.hidden = hidden;
    }

    List<Ability> getAbilities(PlayerCharacter pc) {
        return pc.getVirtualAbilityList(abCat);
    }

    public boolean isSourceDependant() {
        return false;
    }

    public boolean isStatic() {
        return false;
    }
}
