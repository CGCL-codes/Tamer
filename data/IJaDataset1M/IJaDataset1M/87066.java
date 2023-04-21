package pcgen.base.term;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import pcgen.core.PlayerCharacter;
import pcgen.core.Equipment;
import pcgen.cdom.base.Constants;

public class PCCountContainersTermEvaluator extends BasePCTermEvaluator implements TermEvaluator {

    public PCCountContainersTermEvaluator(String originalText) {
        this.originalText = originalText;
    }

    public Float resolve(PlayerCharacter pc) {
        final int merge = Constants.MERGE_ALL;
        final Collection<Equipment> aList = new ArrayList<Equipment>();
        final List<Equipment> eList = pc.getEquipmentListInOutputOrder(merge);
        for (Equipment eq : eList) {
            if (eq.acceptsChildren()) {
                aList.add(eq);
            }
        }
        return (float) aList.size();
    }

    public boolean isSourceDependant() {
        return false;
    }

    public boolean isStatic() {
        return false;
    }
}
