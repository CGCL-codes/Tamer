package net.sf.rcpforms.widgetwrapper.gen;

import net.sf.rcpforms.widgetwrapper.wrapper.RCPLabeledControl;
import net.sf.rcpforms.widgets2.IRCPProgressBar;
import java.lang.Object;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPProgressBar;
import java.lang.String;

/**
 * <h3 style='color: #D22'><span style='font-size: 18pt'>NON-API</span>, not intended to user or subclassed!</h3> 
 * <p>
 * <ul><li><i>This class is generated, do not alter it manually!!!</i></li></ul>
 * <p>
 */
public class RCPLabeledProgressBar extends RCPLabeledControl<RCPProgressBar> implements IRCPProgressBar {

    public RCPLabeledProgressBar(String arg0, RCPProgressBar arg1) {
        super(arg0, arg1);
    }

    public RCPLabeledProgressBar(String arg0, RCPProgressBar arg1, Object arg2) {
        super(arg0, arg1, arg2);
    }

    public int getMaximum() {
        return getRCPControl().getMaximum();
    }

    public int getMinimum() {
        return getRCPControl().getMinimum();
    }

    public void setSelection(int arg0) {
        getRCPControl().setSelection(arg0);
    }

    public int getSelection() {
        return getRCPControl().getSelection();
    }

    public void setMinimum(int arg0) {
        getRCPControl().setMinimum(arg0);
    }

    public void setMaximum(int arg0) {
        getRCPControl().setMaximum(arg0);
    }
}
