package semantic.forms;

import semantic.CBaseEntityFactory;
import semantic.CDataEntity;
import semantic.expression.CBaseEntityCondition;
import semantic.expression.CUnitaryEntityCondition;

/**
 * @author U930CV
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class CEntityIsFieldModified extends CUnitaryEntityCondition {

    public void SetIsModified(CDataEntity eData) {
        m_Reference = eData;
    }

    protected CDataEntity m_Reference = null;

    public void Clear() {
        super.Clear();
        m_Reference = null;
    }

    public boolean ignore() {
        return m_Reference.ignore();
    }

    public CBaseEntityCondition GetSpecialConditionReplacing(String val, CBaseEntityFactory fact, CDataEntity replace) {
        return m_Reference.GetSpecialCondition(getLine(), val, EConditionType.IS_EQUAL, fact);
    }

    public boolean isBinaryCondition() {
        return true;
    }
}
