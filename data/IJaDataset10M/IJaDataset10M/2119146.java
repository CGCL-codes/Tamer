package semantic.forms;

import lexer.Cobol.CCobolConstantList;
import parser.expression.CTerminal;
import generate.CBaseLanguageExporter;
import semantic.CBaseActionEntity;
import semantic.CBaseEntityFactory;
import semantic.CDataEntity;
import semantic.expression.CBaseEntityCondition;
import utils.CObjectCatalog;
import semantic.expression.CBaseEntityCondition.EConditionType;

/**
 * @author U930CV
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class CEntityGetKeyPressed extends CDataEntity {

    /**
	 * @param l
	 * @param name
	 * @param cat
	 * @param out
	 */
    public CEntityGetKeyPressed(String name, CObjectCatalog cat, CBaseLanguageExporter out) {
        super(0, name, cat, out);
    }

    public CDataEntityType GetDataType() {
        return null;
    }

    public boolean HasAccessors() {
        return true;
    }

    protected void DoExport() {
    }

    public CBaseEntityCondition GetSpecialCondition(int nLine, CDataEntity eData2, EConditionType type, CBaseEntityFactory factory) {
        if (eData2.GetDataType() == CDataEntityType.CONSOLE_KEY) {
            if (type == CBaseEntityCondition.EConditionType.IS_DIFFERENT) {
                CEntityIsKeyPressed is = factory.NewEntityIsKeyPressed();
                eData2.RegisterValueAccess(is);
                is.isNotKeyPressed(eData2);
                return is;
            } else if (type == CBaseEntityCondition.EConditionType.IS_EQUAL) {
                CEntityIsKeyPressed is = factory.NewEntityIsKeyPressed();
                eData2.RegisterValueAccess(is);
                is.isKeyPressed(eData2);
                return is;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public CBaseEntityCondition GetSpecialCondition(int nLine, String value, EConditionType type, CBaseEntityFactory factory) {
        return null;
    }

    public CBaseActionEntity GetSpecialAssignment(CTerminal term, CBaseEntityFactory factory, int l) {
        String val = term.GetValue();
        if (val.equals(CCobolConstantList.SPACE.m_Name)) {
            CEntityResetKeyPressed e = factory.NewEntityResetKeyPressed(l);
            return e;
        } else {
            return null;
        }
    }

    public CBaseActionEntity GetSpecialAssignment(CDataEntity term, CBaseEntityFactory factory, int l) {
        return null;
    }

    public boolean ignore() {
        return false;
    }

    public String GetConstantValue() {
        return "";
    }
}
