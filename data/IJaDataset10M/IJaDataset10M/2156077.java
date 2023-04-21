package net.sf.isolation.util.spi;

import net.sf.isolation.core.IsoInformation;
import net.sf.isolation.util.IsoLongStack;

/**
 * <p>
 * </p>
 * 
 * <dl>
 * <dt><b>Thread Safe:</b></dt>
 * <dd>See implementation</dd>
 * </dl>
 * 
 * @author <a href="https://sourceforge.net/users/mc_new">mc_new</a>
 * @since 1.0
 */
@IsoInformation(lastChangedBy = "$LastChangedBy: mc_new $", revision = "$LastChangedRevision: 250 $", source = "$URL: http://isolation.svn.sourceforge.net/svnroot/isolation/Current/Source/Branches/dev_20100401/Isolation/src/main/java/net/sf/isolation/util/spi/IsoLongStackSpi.java $", lastChangedDate = "$LastChangedDate: 2010-08-27 00:08:22 -0400 (Fri, 27 Aug 2010) $")
public class IsoLongStackSpi extends IsoAbstractStackSpi implements IsoLongStack {

    private long[] values;

    public IsoLongStackSpi() {
        this(16);
    }

    public IsoLongStackSpi(int size) {
        values = new long[size];
    }

    public void push(long value) {
        int next = curr + 1;
        if (next == values.length) {
            resize();
        }
        values[curr = next] = value;
    }

    public long pop() {
        checkStatus();
        return values[curr--];
    }

    public long top() {
        checkStatus();
        return values[curr];
    }

    private void resize() {
        int l = values.length;
        long[] tmp = new long[l * 2];
        System.arraycopy(values, 0, tmp, 0, l);
        values = tmp;
    }
}
