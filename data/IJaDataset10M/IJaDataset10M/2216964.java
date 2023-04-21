package pcgen.base.term;

import pcgen.core.PlayerCharacter;

public class PCHeightTermEvaluator extends BasePCTermEvaluator implements TermEvaluator {

    public PCHeightTermEvaluator(String originalText) {
        this.originalText = originalText;
    }

    public Float resolve(PlayerCharacter pc) {
        return (float) pc.getHeight();
    }

    public boolean isSourceDependant() {
        return false;
    }

    public boolean isStatic() {
        return false;
    }
}
