package generate.java.forms;

import semantic.CDataEntity;
import semantic.expression.CBaseEntityCondition;
import semantic.forms.CEntityIsFieldHighlight;

/**
 * @author sly
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CJavaIsFieldHighlight extends CEntityIsFieldHighlight {

    public CJavaIsFieldHighlight(CDataEntity ref) {
        super(ref);
    }

    public int GetPriorityLevel() {
        return 7;
    }

    public CBaseEntityCondition GetOppositeCondition() {
        CJavaIsFieldHighlight not = new CJavaIsFieldHighlight(m_Reference);
        not.m_bIsBlink = m_bIsBlink;
        not.m_bIsReverse = m_bIsReverse;
        not.m_bIsUnderlined = m_bIsUnderlined;
        not.m_bOpposite = !m_bOpposite;
        m_Reference.RegisterVarTesting(not);
        return not;
    }

    public String Export() {
        String cs = "is";
        if (m_bOpposite) {
            cs += "Not";
        }
        if (m_bIsUnderlined) {
            cs += "FieldUnderlined(";
        } else if (m_bIsBlink) {
            cs += "FieldBlink(";
        } else if (m_bIsReverse) {
            cs += "FieldReverse(";
        } else {
            cs += "FieldHighlightNormal(";
        }
        cs += m_Reference.ExportReference(getLine()) + ")";
        return cs;
    }
}
