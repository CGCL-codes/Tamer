package rails.game.correct;

import rails.common.LocalText;
import rails.game.action.PossibleAction;
import rails.util.*;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Action class to request specific correction actions
 * @author freystef
 *
 */
public class CorrectionModeAction extends CorrectionAction {

    public static final long serialVersionUID = 1L;

    protected boolean active;

    /** 
     * Initializes with all possible correction types
     */
    public CorrectionModeAction(CorrectionType correction, boolean active) {
        this.correctionType = correction;
        correctionName = correction.name();
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public String getInfo() {
        return (LocalText.getText(correctionName));
    }

    @Override
    public boolean equalsAsOption(PossibleAction action) {
        if (!(action instanceof CorrectionModeAction)) return false;
        CorrectionModeAction a = (CorrectionModeAction) action;
        return (a.correctionType.equals(this.correctionType) && a.isActive() == this.isActive());
    }

    @Override
    public boolean equalsAsAction(PossibleAction action) {
        return action.equalsAsOption(this);
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer("CorrectionModeAction");
        if (!acted) {
            b.append(" (not acted)");
            if (correctionType != null) b.append(", correctionType=" + correctionType);
            b.append(", current state=" + active);
        } else {
            b.append(" (acted)");
            if (correctionType != null) b.append(", correctionType=" + correctionType);
            b.append(", previous state=" + active);
        }
        return b.toString();
    }

    /** Deserialize */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (Util.hasValue(correctionName)) correctionType = CorrectionType.valueOf(correctionName);
    }
}
