package net.etherstorm.jopenrpg.swing;

import net.etherstorm.jopenrpg.swing.text.*;
import javax.swing.*;
import javax.swing.text.Document;

/** 
 * 
 * 
 * @author Ted Berg (<a href="mailto:tedberg@users.sourceforge.net">tedberg@users.sourceforge.net</a>)
 * @version $Revision: 1.1 $
 */
class JDoubleField extends JTextField {

    /** 
	 * 
	 */
    public JDoubleField() {
        super();
    }

    /** 
	 * 
	 * 
	 * @param cols 
	 */
    public JDoubleField(int cols) {
        super(cols);
    }

    /** 
	 * 
	 * 
	 * @param value 
	 */
    public JDoubleField(String value) {
        super(value);
    }

    /** 
	 * 
	 * 
	 * @return 
	 */
    protected Document createDefaultModel() {
        return new DoubleDocument();
    }
}
