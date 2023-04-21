package pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.common;

import pt.igeo.snig.mig.editor.list.FixedList;
import pt.igeo.snig.mig.editor.list.ListValueManager;
import fi.mmm.yhteinen.swing.core.tools.YFormatter;

/**
 * Formatter for Fixed List values
 * 
 * @author Ant�nio Silva
 * @version $Revision: 9175 $
 * @since 1.0
 */
public class FixedListFormatter extends YFormatter {

    /** only instance available */
    private static FixedListFormatter instance = null;

    /**
	 * Constructor is made private
	 */
    private FixedListFormatter() {
    }

    /**
	 * public interface to access instance - singleton design pattern
	 * 
	 * @return the only instance
	 */
    public static FixedListFormatter getInstance() {
        if (instance == null) {
            instance = new FixedListFormatter();
        }
        return instance;
    }

    /**
	 * Formats a fixed List item
	 * @param itemModel 
	 * @return the formatted string
	 */
    public String format(Object itemModel) {
        FixedList l = (FixedList) itemModel;
        return l.getText();
    }

    /**
	 * Asks list manager for the text from the given item. When not found, the text from the given item is returned
	 * @param fl
	 * @return the item's text from fixed list list
	 */
    public String getSafeText(FixedList fl) {
        FixedList safeFl = ListValueManager.getInstance().getFixedListItem(fl.getCodeList(), fl.getCodeValue());
        if (safeFl != null) {
            return safeFl.getText();
        } else {
            return fl.getText();
        }
    }
}
