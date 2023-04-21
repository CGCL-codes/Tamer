package pcgen.core.term;

import pcgen.core.PlayerCharacter;

public class PCHeightTermEvaluator extends BasePCTermEvaluator implements TermEvaluator {

    public PCHeightTermEvaluator(String originalText) {
        this.originalText = originalText;
    }

    @Override
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
