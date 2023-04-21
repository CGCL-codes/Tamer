package generate.java.CICS;

import generate.CBaseLanguageExporter;
import semantic.CICS.CEntityCICSAbend;
import utils.CObjectCatalog;

/**
 * @author U930CV
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CJavaCICSAbend extends CEntityCICSAbend {

    /**
	 * @param line
	 * @param cat
	 * @param out
	 */
    public CJavaCICSAbend(int line, CObjectCatalog cat, CBaseLanguageExporter out) {
        super(line, cat, out);
    }

    protected void DoExport() {
        String cs = "CESM.abend(";
        if (m_ABCode != null) {
            cs += m_ABCode.ExportReference(getLine());
        }
        WriteLine(cs + ") ;");
    }
}
