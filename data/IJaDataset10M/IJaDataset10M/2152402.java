package org.primordion.xholon.script;

import org.primordion.xholon.base.IXholon;
import org.primordion.xholon.service.IXholonService;

/**
 * Cascading Style Sheet (CSS) Stylist.
 * Examples:
<pre>
&lt;CssStylist>
JButton {
  Background: PINK;
  Foreground: BLUE;
  MinimumSize: "Dimension,100,100";
  PreferredSize: "Dimension,100,100";
  SetFont: "Monospaced,7,14";
}
JTextField {Background: ORANGE;}
JLabel {Foreground: GREEN;}
&lt;/CssStylist>
</pre>
<pre>
&lt;CssStylist>
Glucose {val: "200000.0";}
Glucose_6_Phosphate {val: "200000.0";}
Fructose_6_Phosphate {val: "200000.0";}
&lt;/CssStylist>
</pre>
<p>In the following example, SmallMolecule is the Xholon superclass of Glucose, etc. from the previous example.</p>
<pre>
&lt;CssStylist>
SmallMolecule {val: "200000.0";}
&lt;/CssStylist>
</pre>
<pre>
&lt;CssStylist>
JButton.Add {Background:MAGENTA}
JButton.Edit {Background:BLUE }
JButton.Delete {Background:GREEN }
JButton.Refresh {Background:YELLOW }
JButton.Save {Background:ORANGE }
JButton.Cancel {Background:RED }
JButton.Close {Background:PINK }
&lt;/CssStylist>
</pre>
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8.1 (Created on May 9, 2010)
 */
public class CssStylist extends XholonScript {

    /**
	 * A set of stylesheet rules, formatted as a CSS String.
	 */
    private String cssString = null;

    public void setVal(String val) {
        cssString = val;
    }

    public String getVal_String() {
        return cssString;
    }

    public void postConfigure() {
        IXholon cssService = getService(IXholonService.XHSRV_CSS);
        if ((cssService != null) && (cssString != null)) {
            cssService.sendSyncMessage(IXholonService.SIG_PROCESS_REQUEST, cssString, getParentNode());
        }
        removeChild();
    }
}
