package buildtools.java.packagebrowser;

import org.gjt.sp.jedit.MiscUtilities;

/**
 * A comparator to compare two <code>JavaClass</code> objects.
 *
 * @see org.gjt.sp.jedit.MiscUtilities.Compare
 * @see buildtools.java.packagebrowser.JavaClass
 * @author Andre Kaplan
 * @version $Id: JavaClassComparator.java 9278 2001-04-08 21:25:59Z dmoebius $
 */
public class JavaClassComparator implements MiscUtilities.Compare {

    public int compare(Object obj1, Object obj2) {
        JavaClass one = (JavaClass) obj1;
        JavaClass two = (JavaClass) obj2;
        return one.getName().compareTo(two.getName());
    }
}
