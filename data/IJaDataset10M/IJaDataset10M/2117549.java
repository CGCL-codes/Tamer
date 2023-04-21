package generate.java;

import generate.*;
import semantic.*;
import utils.*;

/**
 * @author sly
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CJavaAttribute extends CEntityAttribute {

    public boolean ignore() {
        boolean b = super.ignore();
        if (b) {
            Transcoder.logInfo(getLine(), "Unused EntityAttribute " + GetName() + "; it won't be generated");
            super.ignore();
        }
        return b;
    }

    /**
	 * @param name
	 * @param cat
	 */
    public CJavaAttribute(int l, String name, CObjectCatalog cat, CBaseLanguageExporter out) {
        super(l, name, cat, out);
    }

    protected void DoExport() {
        if (m_bBlankWhenZero && m_Type.equals("pic9")) {
            m_Type = "pic";
            m_Format = "";
            for (int i = 0; i < m_Length; i++) m_Format += "9";
            if (m_Decimals > 0) {
                m_Format += ".";
                for (int i = 0; i < m_Decimals; i++) m_Format += "9";
            }
        }
        String line = "Var " + FormatIdentifier(GetName()) + " = declare.level(77)";
        line += "." + m_Type + "(";
        if (m_Format.equals("")) {
            if (m_Length > 0 || m_Decimals > 0) {
                line += m_Length;
                if (m_Decimals > 0) {
                    line += "," + m_Decimals;
                }
            }
        } else {
            line += "\"" + m_Format + "\"";
        }
        line += ")";
        if (!m_Comp.equals("")) {
            if (m_Comp.equalsIgnoreCase("Comp3")) {
                line += ".comp3()";
            } else if (m_Comp.equalsIgnoreCase("Comp4")) {
                line += ".comp()";
            } else if (m_Comp.equalsIgnoreCase("Comp")) {
                line += ".comp()";
            }
        }
        WriteWord(line);
        if (m_bSync) {
            WriteWord(".sync()");
        }
        if (m_Value != null) {
            String cs = "";
            if (m_bFillWithValue) {
                cs = ".valueAll(";
            } else {
                cs = ".value(";
            }
            cs += m_Value.ExportReference(getLine());
            WriteWord(cs + ")");
        } else if (m_bInitialValueIsSpaces) {
            WriteWord(".valueSpaces()");
        } else if (m_bInitialValueIsZeros) {
            WriteWord(".valueZero()");
        } else if (m_bInitialValueIsLowValue) {
            WriteWord(".valueLowValue()");
        } else if (m_bInitialValueIsHighValue) {
            WriteWord(".valueHighValue()");
        }
        if (m_bJustifiedRight) {
            WriteWord(".justifyRight()");
        }
        if (m_bBlankWhenZero) {
            WriteWord(".blankWhenZero()");
        }
        WriteWord(".var() ;");
        WriteEOL();
        StartOutputBloc();
        ExportChildren();
        EndOutputBloc();
    }

    public String ExportReference(int nLine) {
        String cs = "";
        if (m_Of != null) {
            cs = m_Of.ExportReference(getLine()) + ".";
        }
        cs += FormatIdentifier(GetName());
        return cs;
    }

    public boolean HasAccessors() {
        return false;
    }

    public String ExportWriteAccessorTo(String value) {
        return "";
    }

    public boolean isValNeeded() {
        return true;
    }

    public CDataEntityType GetDataType() {
        if (m_Type.equals("picS9") || m_Type.equals("pic9")) {
            return CDataEntityType.NUMERIC_VAR;
        } else {
            return CDataEntityType.VAR;
        }
    }
}
