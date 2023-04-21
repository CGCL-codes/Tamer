package net.nothinginteresting.xlib.options;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import net.nothinginteresting.xlib.constants.XOption;
import org.eclipse.swt.SWT;

/**
 * @author Dmitri Gorbenko
 * 
 */
public class XOptions {

    private final Set<XOption> options;

    public XOptions() {
        this.options = new TreeSet<XOption>();
    }

    public XOptions(Set<XOption> options) {
        this.options = options;
    }

    public Set<XOption> getOptions() {
        return Collections.unmodifiableSet(options);
    }

    protected boolean getOption(XOption option) {
        return options.contains(option);
    }

    protected void setOption(XOption option, boolean set) {
        if (set) options.add(option); else options.remove(option);
    }

    public int getStyle() {
        int result = SWT.NONE;
        for (XOption option : options) {
            result = result | option.getValue();
        }
        return result;
    }
}
