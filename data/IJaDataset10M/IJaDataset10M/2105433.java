package generate.java.CICS;

import generate.CBaseLanguageExporter;
import semantic.CICS.CEntityCICSWrite;
import utils.CObjectCatalog;

/**
 * @author U930CV
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CJavaCICSWrite extends CEntityCICSWrite {

    /**
	 * @param line
	 * @param cat
	 * @param out
	 */
    public CJavaCICSWrite(int line, CObjectCatalog cat, CBaseLanguageExporter out) {
        super(line, cat, out);
    }

    protected void DoExport() {
        if (m_bWritetoDataSet && m_DataFrom.GetName().equals("CUM-COLL")) {
            if (m_DataFrom.m_Of != null) {
                String cs = "Pub2000Routines.writeStatistics(getProgramManager(), " + m_DataFrom.m_Of.ExportReference(getLine()) + ") ;";
                WriteLine(cs);
                return;
            } else {
                ASSERT(null);
            }
        } else {
            String title = "CESM.write";
            if (m_bWritetoDataSet) {
                title += "DataSet(";
            } else if (m_bWriteToFile) {
                title += "File(";
            }
            title += m_Name.ExportReference(getLine()) + ").from(" + m_DataFrom.ExportReference(getLine()) + ")";
            WriteWord(title);
            if (m_RecIDField != null) {
                WriteWord(".recIDField(" + m_RecIDField.ExportReference(getLine()) + ")");
            }
            WriteWord(" ;");
            WriteEOL();
        }
    }
}
