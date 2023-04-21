package gnu.testlet.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the result of running all the tests for a particular package.
 */
public class PackageResult implements Comparable, Result {

    /** The name of the package. */
    private String name;

    /** A list containing results for each class in the package. */
    private List classResults;

    private boolean sorted = true;

    /**
     * Creates a new result, initially empty.
     *
     * @param name  the class name.
     */
    PackageResult(String name) {
        this.name = name;
        classResults = new ArrayList();
    }

    /**
     * Returns the package name.
     *
     * @return The package name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the package name.
     *
     * @param name  the name.
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a class result.
     *
     * @param result  the test result.
     */
    public void add(ClassResult result) {
        classResults.add(result);
        sorted = false;
    }

    /**
     * Returns an iterator that provides access to the class results.
     *
     * @return An iterator.
     */
    public Iterator getClassIterator() {
        sortClasses();
        return classResults.iterator();
    }

    /**
     * Returns the class result for the named class.
     *
     * @param name  the class name.
     *
     * @return A class result.
     */
    public ClassResult getClassResult(String name) {
        sortClasses();
        for (int i = 0; i < classResults.size(); i++) {
            ClassResult cr = (ClassResult) classResults.get(i);
            if (cr.getName().equals(name)) return cr;
        }
        return null;
    }

    /**
     * Returns the total number of checks performed for this package.
     *
     * @return The check count.
     */
    public int getCheckCount() {
        int result = 0;
        Iterator iterator = getClassIterator();
        while (iterator.hasNext()) {
            ClassResult cr = (ClassResult) iterator.next();
            result = result + cr.getCheckCount();
        }
        return result;
    }

    /**
     * Returns the number of checks with the specified status.
     *
     * @param passed  the check status.
     *
     * @return The number of checks passed or failed.
     */
    public int getCheckCount(boolean passed) {
        int result = 0;
        Iterator iterator = getClassIterator();
        while (iterator.hasNext()) {
            ClassResult cr = (ClassResult) iterator.next();
            result = result + cr.getCheckCount(passed);
        }
        return result;
    }

    /**
     * Returns the index of the specified result.
     *
     * @param result  the class result.
     *
     * @return The index.
     */
    public int indexOf(ClassResult result) {
        sortClasses();
        return classResults.indexOf(result);
    }

    public int compareTo(Object obj) {
        PackageResult that = (PackageResult) obj;
        return getName().compareTo(that.getName());
    }

    /**
     * Sorts the class results.
     */
    private void sortClasses() {
        if (sorted) return;
        Collections.sort(classResults);
        sorted = true;
    }
}
