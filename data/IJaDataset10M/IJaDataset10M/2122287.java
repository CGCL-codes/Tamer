package parser.condition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import parser.expression.*;
import semantic.CBaseEntityFactory;
import semantic.CDataEntity;
import semantic.expression.CBaseEntityCondition;
import semantic.expression.CBaseEntityExpression;
import semantic.expression.CEntityCondCompare;

/**
 * @author U930CV
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CCondGreaterStatement extends CExpression {

    public CCondGreaterStatement(int line, CExpression term1, CExpression term2) {
        super(line);
        m_term1 = term1;
        m_term2 = term2;
    }

    public CCondGreaterStatement(int line, CExpression term1, CExpression term2, boolean bOrEquals) {
        super(line);
        m_term1 = term1;
        m_term2 = term2;
        m_bOrEquals = bOrEquals;
    }

    protected boolean CheckMembersBeforeExport() {
        boolean b = CheckMemberNotNull(m_term1);
        b &= CheckMemberNotNull(m_term2);
        return b;
    }

    protected boolean m_bOrEquals = false;

    protected CExpression m_term1 = null;

    protected CExpression m_term2 = null;

    public Element DoExport(Document root) {
        Element e;
        if (m_bOrEquals) {
            e = root.createElement("GreaterThanOrEqual");
        } else {
            e = root.createElement("GreaterThan");
        }
        Element e1 = m_term1.Export(root);
        if (e1 == null) {
            int n = 0;
        }
        e.appendChild(e1);
        if (m_term2 != null) {
            Element e2 = m_term2.Export(root);
            if (e2 == null) {
                int n = 0;
            }
            e.appendChild(e2);
        }
        return e;
    }

    public boolean IsOrEquals() {
        return m_bOrEquals;
    }

    public int GetPriorityLevel() {
        return 3;
    }

    public CExpression GetOppositeCondition() {
        return new CCondLessStatement(getLine(), m_term1, m_term2, !m_bOrEquals);
    }

    public CBaseEntityExpression AnalyseExpression(CBaseEntityFactory factory) {
        return null;
    }

    public CBaseEntityCondition AnalyseCondition(CBaseEntityFactory factory, CDefaultConditionManager masterCond) {
        masterCond.SetMasterCondition(this);
        String value = m_term2.GetConstantValue();
        if (!value.equals("") && m_term1.IsReference()) {
            CDataEntity ref = m_term1.GetReference(factory);
            if (ref == null) {
                return null;
            }
            CBaseEntityCondition.EConditionType type = CBaseEntityCondition.EConditionType.IS_GREATER_THAN;
            if (m_bOrEquals) {
                type = CBaseEntityCondition.EConditionType.IS_GREATER_THAN_OR_EQUAL;
            }
            CBaseEntityCondition eCond = ref.GetSpecialCondition(getLine(), value, type, factory);
            if (eCond != null) {
                return eCond;
            }
        }
        value = m_term1.GetConstantValue();
        if (!value.equals("") && m_term2.IsReference()) {
            CDataEntity ref = m_term2.GetReference(factory);
            CBaseEntityCondition.EConditionType type = CBaseEntityCondition.EConditionType.IS_LESS_THAN_OR_EQUAL;
            if (m_bOrEquals) {
                type = CBaseEntityCondition.EConditionType.IS_LESS_THAN;
            }
            CBaseEntityCondition eCond = ref.GetSpecialCondition(getLine(), value, type, factory);
            if (eCond != null) {
                return eCond;
            }
        }
        CBaseEntityExpression op1 = m_term1.AnalyseExpression(factory);
        CBaseEntityExpression op2 = m_term2.AnalyseExpression(factory);
        if (op2 == null) {
            masterCond.SetMasterCondition(this);
            CBaseEntityCondition eCond = m_term2.AnalyseCondition(factory, masterCond);
            ASSERT(eCond, m_term2);
            return eCond;
        }
        CEntityCondCompare eCond = factory.NewEntityCondCompare();
        if (m_bOrEquals) {
            eCond.SetGreaterOrEqualsThan(op1, op2);
        } else {
            eCond.SetGreaterThan(op1, op2);
        }
        if (op1.GetSingleOperator() != null) {
            op1.GetSingleOperator().RegisterVarTesting(eCond);
        }
        if (op2.GetSingleOperator() != null) {
            op2.GetSingleOperator().RegisterValueAccess(eCond);
        }
        return eCond;
    }

    public CExpression GetFirstConditionOperand() {
        return m_term1;
    }

    public CExpression GetSimilarExpression(CExpression operand) {
        CCondGreaterStatement gt = new CCondGreaterStatement(getLine(), m_term1, operand);
        gt.m_bOrEquals = m_bOrEquals;
        return gt;
    }

    public boolean IsBinaryCondition() {
        return true;
    }

    public String toString() {
        if (m_bOrEquals) {
            return "GREATER_OR_EQUAL(" + m_term1.toString() + ", " + m_term2.toString() + ")";
        } else {
            return "GREATER(" + m_term1.toString() + ", " + m_term2.toString() + ")";
        }
    }

    public CExpression getMasterBinaryCondition() {
        return this;
    }

    @Override
    public CExpression GetFirstCalculOperand() {
        return m_term1.GetFirstCalculOperand();
    }
}
