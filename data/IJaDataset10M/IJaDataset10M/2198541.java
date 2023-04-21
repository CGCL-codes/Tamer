package jscicalc.button;

import jscicalc.CalculatorApplet;
import jscicalc.pobject.Dec;

/**
 * Button to set Base to DECIMAL.
 *
 * @author J.&nbsp;D.&nbsp;Lamb
 * @version $Revision: 14 $
 */
public class DecButton extends EqualsButton {

    /**
     * Constructor. Sets PObject to Dec.
     * @param applet The <em>controller</em> object.
     */
    public DecButton(CalculatorApplet applet) {
        this.applet = applet;
        this.pobject = new Dec();
        setText();
        tooltip = pobject.tooltip();
        shortcut = pobject.shortcut();
        setTextSize();
        setToolTipText();
        changeBase = ChangeBase.DECIMAL;
        addActionListener(this);
    }

    private static final long serialVersionUID = 1L;
}
