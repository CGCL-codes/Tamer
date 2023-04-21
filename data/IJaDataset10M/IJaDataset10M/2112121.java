package net.nothinginteresting.xlib.options;

import java.util.Set;
import net.nothinginteresting.xlib.constants.XOption;

/**
 * Options for XButton constructor.
 * 
 * @author Dmitri Gorbenko
 * 
 */
public class XButtonOptions extends XControlOptions {

    public XButtonOptions() {
        super();
    }

    public XButtonOptions(Set<XOption> options) {
        super(options);
    }

    public boolean getFlat() {
        return getOption(XOption.FLAT);
    }

    public void setFlat(boolean value) {
        setOption(XOption.FLAT, value);
    }
}
