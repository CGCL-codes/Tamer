package pcgen.core.term;

import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;

public class PCACcheckTermEvaluator extends BasePCTermEvaluator implements TermEvaluator {

    PCACcheckTermEvaluator(final String originalText) {
        this.originalText = originalText;
    }

    @Override
    public Float resolve(PlayerCharacter pc) {
        int maxCheck = 0;
        for (Equipment eq : pc.getEquipmentOfType("Armor", 1)) {
            maxCheck += eq.acCheck(pc);
        }
        for (Equipment eq : pc.getEquipmentOfType("Shield", 1)) {
            maxCheck += eq.acCheck(pc);
        }
        return (float) maxCheck;
    }

    public boolean isSourceDependant() {
        return false;
    }

    public boolean isStatic() {
        return false;
    }
}
