package lo.local.dreamrec.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Counter {

    private static Log LOG = LogFactory.getLog(Counter.class);

    int n;

    int nMax;

    public Counter(int nMax) {
        this.nMax = nMax;
    }

    /**
	 * returns true after every nMax-1 invocations;
	 */
    public boolean check() {
        boolean result = n == 0;
        n++;
        if (n == nMax) {
            n = 0;
        }
        if (n > nMax) {
            String msg = "Illegal counter state: n = " + n + "; nMax = " + nMax;
            LOG.error(msg);
            throw new IllegalStateException(msg);
        }
        return result;
    }

    public void reset() {
        n = 0;
    }
}
