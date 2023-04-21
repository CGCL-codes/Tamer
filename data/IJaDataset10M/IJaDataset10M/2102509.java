package generate.java.verbs;

import generate.CBaseLanguageExporter;
import semantic.Verbs.CEntityConstantReturn;
import utils.CObjectCatalog;

public class CJavaConstantReturn extends CEntityConstantReturn {

    /**
	 * @param cat
	 * @param out
	 */
    public CJavaConstantReturn(int l, CObjectCatalog cat, CBaseLanguageExporter out, String cs) {
        super(l, cat, out, cs);
    }

    protected void DoExport() {
        WriteLine("return " + m_csConstant + " ;");
    }
}
