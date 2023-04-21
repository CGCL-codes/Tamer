package jscicalc.button;

import jscicalc.CalculatorApplet;
import jscicalc.pobject.*;
import jscicalc.complex.Complex;
import jscicalc.OObject;

/**
 * Evaluates current value and adds it to statistical memory. Probably this
 * could be derived from EqualsButton.
 *
 * @author J.&nbsp;D.&nbsp;Lamb
 * @version $Revision: 14 $
 */
public class SplusButton extends CalculatorButton {

    /**
     * Constructor. Sets PObject to SigmaPlus.
     * @param applet The <em>controller</em> object.
     */
    public SplusButton(CalculatorApplet applet) {
        this.applet = applet;
        this.pobject = new SigmaPlus();
        setText();
        setTextSize();
        tooltip = "adds current expression or most recent<br>" + "result as a number in statistics memory";
        shortcut = 'M';
        setToolTipText();
        addActionListener(this);
    }

    /**
     * Evaluates current expression and asks CalculatorApplet to add it to
     * statistical memory
     * EntryPanel also gets updated.
     * @param actionEvent The event that generated this method call: usually a button
     * press or called when CalculatorApplet responded to the key associated with
     * this button
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        synchronized (applet) {
            if (getApplet().getMode() != 0) {
                getApplet().setMode(pobject);
                getApplet().requestFocusInWindow();
                return;
            }
            getApplet().pushHistory();
            OObject o = getApplet().getValue();
            if (!getApplet().getParser().isEmpty()) {
                PObject p = getApplet().getParser().getLast();
                if (o instanceof Complex && (p instanceof RFunction || p instanceof DFunction || p instanceof MFunction || p instanceof AFunction)) {
                    Ans ans = new Ans();
                    ans.setValue((Complex) o);
                    getApplet().insert(ans);
                    getApplet().updateDisplay(true, true);
                }
            } else {
                Ans ans = new Ans();
                ans.setValue((Complex) o);
                getApplet().insert(ans);
                getApplet().updateDisplay(true, true);
            }
            o = getApplet().getParser().evaluate(getApplet().getAngleType());
            if (o instanceof Complex) {
                Complex d = (Complex) o;
                getApplet().setValue(getApplet().statAdd(d));
                getApplet().updateDisplay(false, true);
            }
            getApplet().setShift(false);
            getApplet().newExpression();
            getApplet().requestFocusInWindow();
        }
    }

    private static final long serialVersionUID = 1L;
}
