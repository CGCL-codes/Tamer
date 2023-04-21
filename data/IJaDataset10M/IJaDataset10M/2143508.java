package jscicalc.button;

import jscicalc.CalculatorApplet;
import jscicalc.pobject.*;
import jscicalc.OObject;

/**
 * Button to add to memory. This probably should be derived from EqualsButton.
 *
 * @author J.&nbsp;D.&nbsp;Lamb
 * @version $Revision: 14 $
 */
public class MplusButton extends CalculatorButton {

    /**
     * Constructor. Sets PObject to Mplus.
     * @param applet The <em>controller</em> object.
     */
    public MplusButton(CalculatorApplet applet) {
        this.applet = applet;
        this.pobject = new Mplus();
        setText();
        setTextSize();
        tooltip = "adds current expression or most<br>recent " + "result to memory value";
        shortcut = 'M';
        setToolTipText();
        addActionListener(this);
    }

    /**
     * Evaluates current expression and adds result to CalculatorApplet memory.
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
            OObject m = getApplet().getMemory();
            getApplet().pushHistory();
            OObject o = getApplet().getValue();
            if (!getApplet().getParser().isEmpty()) {
                PObject p = getApplet().getParser().getLast();
                if (!(o instanceof jscicalc.Error) && (p instanceof RFunction || p instanceof DFunction || p instanceof MFunction || p instanceof AFunction)) {
                    Ans ans = new Ans();
                    ans.setValue(o);
                    getApplet().insert(ans);
                    getApplet().updateDisplay(true, true);
                }
            } else {
                Ans ans = new Ans();
                ans.setValue(o);
                getApplet().insert(ans);
                getApplet().updateDisplay(true, true);
            }
            o = getApplet().getParser().evaluate(getApplet().getAngleType());
            if (!(o instanceof jscicalc.Error)) {
                OObject q = m.add(o);
                if (!(q instanceof jscicalc.Error)) getApplet().setMemory(q);
                getApplet().updateDisplay(false, true);
            }
            getApplet().setShift(false);
            getApplet().newExpression();
            getApplet().requestFocusInWindow();
        }
    }

    private static final long serialVersionUID = 1L;
}
