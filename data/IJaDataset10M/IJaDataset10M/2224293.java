package generate.java.forms;

import generate.CBaseLanguageExporter;
import semantic.forms.CEntityGetKeyPressed;
import utils.CObjectCatalog;

/**
 * @author U930CV
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CJavaGetKeyPressed extends CEntityGetKeyPressed {

    /**
	 * @param l
	 * @param name
	 * @param cat
	 * @param out
	 */
    public CJavaGetKeyPressed(String name, CObjectCatalog cat, CBaseLanguageExporter out) {
        super(name, cat, out);
    }

    public String ExportReference(int nLine) {
        return "getKeyPressed()";
    }

    public String ExportWriteAccessorTo(String value) {
        return "setKeyPressed(" + value + ") ;";
    }

    public boolean isValNeeded() {
        return false;
    }
}
