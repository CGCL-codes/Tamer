package bufferings.ktr.wjr.shared.model;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The store of test case and method models.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrStore {

    /**
   * The root summary item of the store.
   * 
   * @author bufferings[at]gmail.com
   */
    protected static class Root extends WjrSummaryItem {

        /**
     * {@inheritDoc}
     */
        @Override
        protected List<? extends WjrStoreItem> fetchChildren(WjrStore store) {
            return store.getClassItems();
        }
    }

    /**
   * The root summary item of the store.
   */
    protected Root root = new Root();

    /**
   * The class items sorted by the className.
   */
    protected TreeMap<String, WjrClassItem> classItems = new TreeMap<String, WjrClassItem>();

    /**
   * The method items sorted by the classAndMethodName.
   */
    protected TreeMap<String, WjrMethodItem> methodItems = new TreeMap<String, WjrMethodItem>();

    /**
   * Adds the class item to the store.
   * 
   * The class name of the classItem must not already be stored.
   * 
   * @param classItem
   *          The class item, cannot be null.
   * @throws NullPointerException
   *           When the classItem parameter is null.
   * @throws IllegalStateException
   *           When the className has already existed.
   */
    public void addClassItem(WjrClassItem classItem) {
        checkNotNull(classItem, "The classItem parameter is null.");
        String className = classItem.getClassName();
        checkState(!classItems.containsKey(className), "The %s has already existed.", className);
        classItems.put(className, classItem);
    }

    /**
   * Adds the method item to the store.
   * 
   * The classItem which is the parent of the methodItem must stored in this
   * store, and the methodItem must not already stored.
   * 
   * @param methodItem
   *          The method item, cannot be null.
   * @throws NullPointerException
   *           When the methodItem parameter is null.
   * @throws IllegalStateException
   *           When the className is not found.
   * @throws IllegalStateException
   *           When the classAndMethodName has already existed.
   */
    public void addMethodItem(WjrMethodItem methodItem) {
        checkNotNull(methodItem, "The methodItem parameter is null.");
        String className = methodItem.getClassName();
        checkState(classItems.containsKey(className), "The %s is not found.", className);
        String classAndMethodName = methodItem.getClassAndMethodName();
        checkState(!methodItems.containsKey(classAndMethodName), "The %s has already existed.", classAndMethodName);
        methodItems.put(classAndMethodName, methodItem);
    }

    /**
   * Gets the class item from the store. If not found, the exception occurs.
   * 
   * @param className
   *          The className.
   * @return The class item.
   * @throws NullPointerException
   *           When the className parameter is null.
   * @throws IllegalStateException
   *           When the className is not found.
   */
    public WjrClassItem getClassItem(String className) {
        checkNotNull(className, "The className parameter is null.");
        checkState(classItems.containsKey(className), "The %s is not found.", className);
        return classItems.get(className);
    }

    /**
   * Gets the method item from the store. If not found, the exception occurs.
   * 
   * @param classAndMethodName
   *          The classAndMethodName.
   * @return The method item.
   * @throws NullPointerException
   *           When the methodItemName parameter is null.
   * @throws IllegalStateException
   *           When the classAndMethodName is not found.
   */
    public WjrMethodItem getMethodItem(String classAndMethodName) {
        checkNotNull(classAndMethodName, "The classAndMethodName parameter is null.");
        checkState(methodItems.containsKey(classAndMethodName), "The %s is not found.", classAndMethodName);
        return methodItems.get(classAndMethodName);
    }

    /**
   * Gets the class items.
   * 
   * @return The list of the class items.
   */
    public List<WjrClassItem> getClassItems() {
        return new ArrayList<WjrClassItem>(classItems.values());
    }

    /**
   * Gets the method items.
   * 
   * @return The list of the method items.
   */
    public List<WjrMethodItem> getMethodItems() {
        return new ArrayList<WjrMethodItem>(methodItems.values());
    }

    /**
   * Gets the method items belong to the className.
   * 
   * @param className
   *          The className.
   * @return The list of the method items.
   * @throws NullPointerException
   *           When the className parameter is null.
   * @throws IllegalStateException
   *           The classItem of the className is not found.
   */
    public List<WjrMethodItem> getMethodItems(String className) {
        checkNotNull(className, "The className parameter is null.");
        checkState(classItems.containsKey(className), "The %s is not found.", className);
        List<WjrMethodItem> items = new ArrayList<WjrMethodItem>();
        SortedMap<String, WjrMethodItem> tailMap = methodItems.tailMap(className);
        for (WjrMethodItem item : tailMap.values()) {
            if (item.getClassName().equals(className)) {
                items.add(item);
            } else {
                break;
            }
        }
        return items;
    }

    /**
   * Gets the total count.
   * 
   * @return The total count.
   */
    public int getTotalCount() {
        return root.getTotalCount();
    }

    /**
   * Gets the success count.
   * 
   * @return The success count.
   */
    public int getSuccessCount() {
        return root.getSuccessCount();
    }

    /**
   * Gets the failure count.
   * 
   * @return The failure count.
   */
    public int getFailureCount() {
        return root.getFailureCount();
    }

    /**
   * Gets the error count.
   * 
   * @return The error count.
   */
    public int getErrorCount() {
        return root.getErrorCount();
    }

    /**
   * Gets the not yet count.
   * 
   * @return The not yet count.
   */
    public int getNotYetCount() {
        return root.getNotYetCount();
    }

    /**
   * Gets the running count.
   * 
   * @return The running count.
   */
    public int getRunningCount() {
        return root.getRunningCount();
    }

    /**
   * Gets the retry waiting count.
   * 
   * @return The retry waiting count.
   */
    public int getRetryWaitingCount() {
        return root.getRetryWaitingCount();
    }

    /**
   * Updates the summary of the class items. This method does not update the
   * summaries in the class items. If you want to update all summaries, you can
   * use {@link WjrStore#updateAllSummaries()} method.
   */
    public void updateSummary() {
        root.updateSummary(this);
    }

    /**
   * Updates all the summary. First this method updates the summaries in the
   * class items, then updates the summary of the class items.
   */
    public void updateAllSummaries() {
        for (WjrClassItem classItem : classItems.values()) {
            classItem.updateSummary(this);
        }
        root.updateSummary(this);
    }

    /**
   * Clears the results in methodItems and the summaries in classItems and the
   * summary of classItems.
   */
    public void clearAllResultsAndSummaries() {
        for (WjrMethodItem methodItem : methodItems.values()) {
            methodItem.clearResult();
        }
        for (WjrClassItem classItem : classItems.values()) {
            classItem.clearSummary();
        }
        root.clearSummary();
    }
}
