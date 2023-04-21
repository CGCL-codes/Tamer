package pcgen.core.term;

import java.util.List;
import java.util.Arrays;
import pcgen.core.PlayerCharacter;

public class PCCountMiscMagicTermEvaluator extends BasePCTermEvaluator implements TermEvaluator {

    public PCCountMiscMagicTermEvaluator(String originalText) {
        this.originalText = originalText;
    }

    @Override
    public Float resolve(PlayerCharacter pc) {
        String magicString = pc.getMiscList().get(2);
        List<String> magicList = Arrays.asList(magicString.split("\r?\n"));
        return (float) magicList.size();
    }

    public boolean isSourceDependant() {
        return false;
    }

    public boolean isStatic() {
        return false;
    }
}
