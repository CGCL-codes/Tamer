package pcgen.base.term;

import pcgen.core.PlayerCharacter;
import pcgen.core.PCClass;

public class PCMaxCastableSpellTypeTermEvaluator extends BasePCTermEvaluator implements TermEvaluator {

    private final String typeKey;

    public PCMaxCastableSpellTypeTermEvaluator(String originalText, String typeKey) {
        this.originalText = originalText;
        this.typeKey = typeKey;
    }

    public Float resolve(PlayerCharacter pc) {
        Float max = 0f;
        for (PCClass spClass : pc.getClassList()) {
            if (typeKey.equalsIgnoreCase(spClass.getSpellType())) {
                int cutoff = spClass.getHighestLevelSpell();
                if (spClass.hasCastList()) {
                    for (int i = 0; i < cutoff; i++) {
                        if (spClass.getCastForLevel(i, pc) != 0) {
                            max = Math.max(max, i);
                        }
                    }
                } else {
                    for (int i = 0; i < cutoff; i++) {
                        if (spClass.getKnownForLevel(i, pc) != 0) {
                            max = Math.max(max, i);
                        }
                    }
                }
            }
        }
        return max;
    }

    public boolean isSourceDependant() {
        return true;
    }

    public boolean isStatic() {
        return false;
    }
}
