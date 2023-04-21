package pcgen.core.kit;

import pcgen.core.Kit;
import pcgen.core.PlayerCharacter;
import java.io.Serializable;
import java.util.List;

/**
 * <code>KitSelect</code>.
 *
 * @author Aaron Divinsky <boomer70@yahoo.com>
 * @version $Revision: 1.2 $
 */
public final class KitSelect extends BaseKit implements Serializable, Cloneable {

    private static final long serialVersionUID = 1;

    private String theFormula = "";

    public KitSelect(final String formula) {
        theFormula = formula;
    }

    public String getFormla() {
        return theFormula;
    }

    public void setFormula(final String aFormula) {
        theFormula = aFormula;
    }

    public String toString() {
        return theFormula;
    }

    public boolean testApply(Kit aKit, PlayerCharacter aPC, List warnings) {
        aKit.setSelectValue(aPC.getVariableValue(theFormula, "").intValue());
        return true;
    }

    public void apply(PlayerCharacter aPC) {
    }

    public Object clone() {
        KitSelect aClone = (KitSelect) super.clone();
        aClone.theFormula = theFormula;
        return aClone;
    }

    public String getObjectName() {
        return "Select";
    }
}
