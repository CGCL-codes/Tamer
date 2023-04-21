package generate.java.forms;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import generate.CBaseLanguageExporter;
import semantic.forms.CEntityResourceField;
import semantic.forms.CEntityResourceFieldArray;
import semantic.forms.CResourceStrings;
import utils.CObjectCatalog;

/**
 * @author U930CV
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CJavaFieldArray extends CEntityResourceFieldArray {

    /**
	 * @param l
	 * @param name
	 * @param cat
	 * @param lexp
	 */
    public CJavaFieldArray(int l, String name, CObjectCatalog cat, CBaseLanguageExporter lexp) {
        super(l, name, cat, lexp);
    }

    public String GetTypeDecl() {
        return "";
    }

    public String ExportReference(int nLine) {
        return "";
    }

    public String ExportWriteAccessorTo(String value) {
        return "";
    }

    public boolean isValNeeded() {
        return false;
    }

    protected void DoExport() {
        ExportChildren();
    }

    public Element DoXMLExport(Document doc, CResourceStrings res) {
        Element eArray = doc.createElement("array");
        eArray.setAttribute("nbCol", String.valueOf(m_NbColumns));
        eArray.setAttribute("nbItems", String.valueOf(m_NbItems));
        eArray.setAttribute("vert", String.valueOf(m_bVerticalFilling));
        eArray.setAttribute("line", String.valueOf(m_nPosLine));
        eArray.setAttribute("col", String.valueOf(m_nPosCol));
        Element eItem = doc.createElement("item");
        eArray.appendChild(eItem);
        ListIterator iter = m_lstChildren.listIterator();
        try {
            CEntityResourceField field = (CEntityResourceField) iter.next();
            while (field != null) {
                Element e = field.DoXMLExport(doc, res);
                if (e != null) {
                    eItem.appendChild(e);
                }
                field = (CEntityResourceField) iter.next();
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return eArray;
    }
}
