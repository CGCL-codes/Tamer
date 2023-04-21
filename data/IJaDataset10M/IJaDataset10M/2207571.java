package semantic.forms;

import semantic.CBaseEntityFactory;
import semantic.CDataEntity;
import semantic.expression.CBaseEntityCondition;

/**
 * @author U930CV
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class CEntityIsKeyPressed extends CBaseEntityCondition {

    public void isKeyPressed(CDataEntity key) {
        m_KeyPressed = key;
        m_bIsNot = false;
    }

    public void isNotKeyPressed(CDataEntity key) {
        m_KeyPressed = key;
        m_bIsNot = true;
    }

    protected CDataEntity m_KeyPressed = null;

    protected boolean m_bIsNot = false;

    public void Clear() {
        super.Clear();
        m_KeyPressed = null;
    }

    public boolean ignore() {
        return false;
    }

    public CBaseEntityCondition GetSpecialConditionReplacing(String val, CBaseEntityFactory fact, CDataEntity replace) {
        return null;
    }

    public boolean isBinaryCondition() {
        return true;
    }

    /**
	 * @see semantic.expression.CBaseEntityCondition#GetConditionReference()
	 */
    @Override
    public CDataEntity GetConditionReference() {
        return null;
    }

    public void SetConditonReference(CDataEntity e) {
        ASSERT(null);
    }
}
