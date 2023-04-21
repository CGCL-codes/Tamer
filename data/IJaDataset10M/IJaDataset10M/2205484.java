package generate.java.expressions;

import semantic.expression.CBaseEntityCondition;
import semantic.expression.CEntityCondIsConstant;

/**
 * @author sly
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CJavaCondIsConstant extends CEntityCondIsConstant {

    public int GetPriorityLevel() {
        return 7;
    }

    public CBaseEntityCondition GetOppositeCondition() {
        CJavaCondIsConstant not = new CJavaCondIsConstant();
        not.m_bIsLowValue = m_bIsLowValue;
        not.m_bIsHighValue = m_bIsHighValue;
        not.m_bIsOpposite = !m_bIsOpposite;
        not.m_bIsSpace = m_bIsSpace;
        not.m_bIsZero = m_bIsZero;
        not.m_Reference = m_Reference;
        m_Reference.RegisterVarTesting(not);
        return not;
    }

    public String Export() {
        String cs = "is";
        if (m_bIsOpposite) {
            cs += "Not";
        }
        if (m_bIsZero) {
            cs += "Zero(";
        } else if (m_bIsSpace) {
            cs += "Space(";
        } else if (m_bIsLowValue) {
            cs += "LowValue(";
        } else if (m_bIsHighValue) {
            cs += "HighValue(";
        }
        cs += m_Reference.ExportReference(getLine()) + ")";
        return cs;
    }
}
