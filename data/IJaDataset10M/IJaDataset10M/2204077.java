package generate.java.expressions;

import semantic.CBaseEntityFactory;
import semantic.expression.CBaseEntityCondition;
import semantic.expression.CEntityConstant;
import semantic.expression.CEntityTally;
import semantic.expression.CEntityConstant.Value;
import utils.modificationsReporter.Reporter;

public class CJavaTally extends CEntityTally {

    /**
	 * @param val
	 */
    public CJavaTally() {
        super();
    }

    /**
	 * @see semantic.CDataEntity#ExportReference(getLine())
	 */
    @Override
    public String ExportReference(int nLine) {
        Reporter.Add("Modif_PJ", "Tally");
        return "tally";
    }

    /**
	 * @see semantic.CBaseLanguageEntity#DoExport()
	 */
    @Override
    protected void DoExport() {
    }

    public CBaseEntityCondition GetSpecialCondition(int nLine, String value, CBaseEntityCondition.EConditionType type, CBaseEntityFactory factory) {
        return null;
    }
}
