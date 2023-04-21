package oxygen.tool.classcompatibilityinspector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates all the problems which can be found
 * when inspecting a class, listing the deprecatedApis,
 * internal Apis and unavailable Apis referenced in the class.
 */
public class CCIResults {

    private Set deprecatedApis = new HashSet();

    private Set internalApis = new HashSet();

    private Set unavailableApis = new HashSet();

    /**
   * add a deprecated API to this result set
   */
    public void addDeprecatedApi(String s) {
        deprecatedApis.add(s);
    }

    /**
   * add an internal API to this result set
   */
    public void addInternalApi(String s) {
        internalApis.add(s);
    }

    /**
   * add an unavailable API to this result set
   */
    public void addUnavailableApi(String s) {
        unavailableApis.add(s);
    }

    /**
   * Returns an array of deprecated APIs used in this class
   */
    public String[] getDeprecatedApis() {
        String[] rtnval = (String[]) deprecatedApis.toArray(new String[0]);
        Arrays.sort(rtnval);
        return rtnval;
    }

    /**
   * Returns an array of internal APIs used in this class
   */
    public String[] getInternalApis() {
        String[] rtnval = (String[]) internalApis.toArray(new String[0]);
        Arrays.sort(rtnval);
        return rtnval;
    }

    /**
   * Returns an array of unavailable APIs used in this class
   */
    public String[] getUnavailableApis() {
        String[] rtnval = (String[]) unavailableApis.toArray(new String[0]);
        Arrays.sort(rtnval);
        return rtnval;
    }

    /**
   * Return true if there is nothing in this result set
   */
    public boolean isEmpty() {
        return (deprecatedApis.size() == 0 && internalApis.size() == 0 && unavailableApis.size() == 0);
    }

    /**
   * merge 2 results, to give a new one
   */
    public static CCIResults merge(CCIResults cir0, CCIResults cir1) {
        CCIResults cir2 = new CCIResults();
        cir2.deprecatedApis.addAll(cir0.deprecatedApis);
        cir2.deprecatedApis.addAll(cir1.deprecatedApis);
        cir2.internalApis.addAll(cir0.internalApis);
        cir2.internalApis.addAll(cir1.internalApis);
        cir2.unavailableApis.addAll(cir0.unavailableApis);
        cir2.unavailableApis.addAll(cir1.unavailableApis);
        return cir2;
    }

    public String toString() {
        String str = " Referenced: " + " Internal APIs(" + internalApis.size() + "): " + Arrays.asList(getInternalApis()) + " Deprecated APIs(" + deprecatedApis.size() + "): " + Arrays.asList(getDeprecatedApis()) + " Unavailable APIs(" + unavailableApis.size() + "): " + Arrays.asList(getUnavailableApis());
        return str;
    }
}
