package stabilizer.runner;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

/**
 * @version $Revision: 1.25 $
 */
public class Selector {

    private boolean myDefault = false;

    private StringBuffer mySelectPattern = null;

    private RE mySelectRE = null;

    private StringBuffer myUnselectPattern = null;

    private RE myUnselectRE = null;

    public synchronized void clear() {
        myDefault = false;
        mySelectPattern = null;
        mySelectRE = null;
        myUnselectPattern = null;
        myUnselectRE = null;
    }

    public synchronized void appendSelect(String pattern) throws RESyntaxException {
        String newPattern = pattern;
        if (mySelectPattern != null) {
            newPattern = mySelectPattern.toString() + "|" + pattern;
        }
        setSelect(newPattern);
    }

    public synchronized void appendUnselect(String pattern) throws RESyntaxException {
        String newPattern = pattern;
        if (myUnselectPattern != null) {
            newPattern = myUnselectPattern.toString() + "|" + pattern;
        }
        setUnselect(newPattern);
    }

    public boolean getDefault() {
        return myDefault;
    }

    public synchronized String getSelect() {
        return (mySelectPattern == null) ? null : mySelectPattern.toString();
    }

    public synchronized String getUnselect() {
        return (myUnselectPattern == null) ? null : myUnselectPattern.toString();
    }

    public synchronized boolean isSelected(String str) {
        boolean result = myDefault;
        if (result) {
            if (myUnselectRE != null) {
                result = !myUnselectRE.match(str);
            }
        } else {
            if (mySelectRE != null) {
                result = mySelectRE.match(str);
            }
        }
        return result;
    }

    public synchronized void setDefault(boolean defaultValue) {
        myDefault = defaultValue;
    }

    public synchronized void setSelect(String pattern) throws RESyntaxException {
        if (pattern == null) {
            mySelectRE = null;
            mySelectPattern = null;
        } else {
            try {
                mySelectRE = new RE(pattern);
                mySelectPattern = new StringBuffer(pattern);
            } catch (RESyntaxException excpt) {
                if (mySelectPattern != null) {
                    mySelectRE = new RE(mySelectPattern.toString());
                }
                throw excpt;
            }
        }
    }

    public synchronized void setUnselect(String pattern) throws RESyntaxException {
        if (pattern == null) {
            myUnselectRE = null;
            myUnselectPattern = null;
        } else {
            try {
                myUnselectRE = new RE(pattern);
                myUnselectPattern = new StringBuffer(pattern);
            } catch (RESyntaxException excpt) {
                if (myUnselectPattern != null) {
                    myUnselectRE = new RE(myUnselectPattern.toString());
                }
                throw excpt;
            }
        }
    }
}
