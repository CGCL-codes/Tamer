package generate.java.verbs;

import generate.CBaseLanguageExporter;
import semantic.Verbs.CEntityReplace;
import utils.CObjectCatalog;

/**
 * @author U930CV
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CJavaReplace extends CEntityReplace {

    /**
	 * @param line
	 * @param cat
	 * @param out
	 */
    public CJavaReplace(int line, CObjectCatalog cat, CBaseLanguageExporter out) {
        super(line, cat, out);
    }

    protected void DoExport() {
        String begin = "inspectReplacing(" + m_Variable.ExportReference(getLine()) + ")";
        for (int i = 0; i < m_arrItemsToReplace.size(); i++) {
            WriteWord(begin);
            String cs = "";
            CReplaceItem item = m_arrItemsToReplace.get(i);
            if (item.m_Mode == CReplaceMode.ALL) {
                cs = ".all";
            } else if (item.m_Mode == CReplaceMode.FIRST) {
                cs = ".first";
            } else if (item.m_Mode == CReplaceMode.LEADING) {
                cs = ".leading";
            }
            if (item.m_ReplaceDataType == CReplaceType.SPACES) {
                cs += "Spaces(";
            } else if (item.m_ReplaceDataType == CReplaceType.ZEROS) {
                cs += "Zeros(";
            } else if (item.m_ReplaceDataType == CReplaceType.LOW_VALUES) {
                cs += "LowValues(";
            } else if (item.m_ReplaceDataType == CReplaceType.HIGH_VALUES) {
                cs += "HighValues(";
            } else if (item.m_ReplaceDataType == CReplaceType.CUSTOM) {
                cs += "(";
            }
            if (item.m_ReplaceData != null) {
                cs += item.m_ReplaceData.ExportReference(getLine());
            }
            WriteWord(cs + ")");
            cs = ".by";
            if (item.m_ByDataType == CReplaceType.SPACES) {
                cs += "Spaces(";
            } else if (item.m_ByDataType == CReplaceType.ZEROS) {
                cs += "Zero(";
            } else if (item.m_ByDataType == CReplaceType.LOW_VALUES) {
                cs += "LowValues(";
            } else if (item.m_ByDataType == CReplaceType.HIGH_VALUES) {
                cs += "HighValues(";
            } else if (item.m_ByDataType == CReplaceType.CUSTOM) {
                cs += "(";
            }
            if (item.m_ByData != null) {
                cs += item.m_ByData.ExportReference(getLine());
            }
            WriteWord(cs + ")");
            WriteWord(" ;");
            WriteEOL();
        }
    }
}
