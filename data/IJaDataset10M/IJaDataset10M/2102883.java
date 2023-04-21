package pcgen.core.term;

import pcgen.core.PlayerCharacter;
import pcgen.cdom.enumeration.FormulaKey;

public class PCRaceSizeTermEvaluator extends BasePCTermEvaluator implements TermEvaluator {

    public PCRaceSizeTermEvaluator(String originalText) {
        this.originalText = originalText;
    }

    @Override
    public Float resolve(PlayerCharacter pc) {
        return pc.getRace().getSafe(FormulaKey.SIZE).resolve(pc, "").floatValue();
    }

    public boolean isSourceDependant() {
        return false;
    }

    public boolean isStatic() {
        return false;
    }
}
