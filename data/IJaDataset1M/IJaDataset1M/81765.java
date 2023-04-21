package groovy.lang.webobjects.categories;

import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;
import er.extensions.*;
import java.util.*;
import groovy.lang.*;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class NSArrayAdditions {

    public static ArrayList asType(NSArray array, Class clazz) {
        return array.arrayList();
    }

    public static NSArray arrayByAddingObjectsFromArrayWithoutDuplicates(NSArray self, NSArray other) {
        return ERXArrayUtilities.arrayByAddingObjectsFromArrayWithoutDuplicates(self, other);
    }

    public static NSDictionary dictionaryGroupedByKeyPath(NSArray self, String keyPath) {
        return ERXArrayUtilities.arrayGroupedByKeyPath(self, keyPath);
    }

    public static NSDictionary dictionaryGroupedByKeyPath(NSArray self, String keyPath, boolean includeNulls, String extraKeyPathForValues) {
        return ERXArrayUtilities.arrayGroupedByKeyPath(self, keyPath, includeNulls, extraKeyPathForValues);
    }

    public static NSDictionary dictionaryGroupedByToManyKeyPath(NSArray self, String keyPath, boolean includeNulls) {
        return ERXArrayUtilities.arrayGroupedByToManyKeyPath(self, keyPath, includeNulls);
    }

    public static NSDictionary dictionaryOfObjectsIndexedByKeyPath(NSArray self, String keyPath) {
        return ERXArrayUtilities.dictionaryOfObjectsIndexedByKeyPath(self, keyPath);
    }

    public static NSArray arrayWithoutDuplicateKeyValue(NSArray self, String key) {
        return ERXArrayUtilities.arrayWithoutDuplicateKeyValue(self, key);
    }

    public static NSArray arrayWithoutDuplicates(NSArray self) {
        return ERXArrayUtilities.arrayWithoutDuplicates(self);
    }

    public static String friendlyDisplayForKeyPath(NSArray self, String attribute, String nullArrayDisplay, String separator, String finalSeparator) {
        return ERXArrayUtilities.friendlyDisplayForKeyPath(self, attribute, nullArrayDisplay, separator, finalSeparator);
    }

    public static NSArray intersectingElements(NSArray self, NSArray other) {
        return ERXArrayUtilities.intersectingElements(self, other);
    }

    public static List list(NSArray self) {
        return new ArrayList(self.vector());
    }

    public static EOArrayDataSource dataSource(NSArray self) {
        return ERXUtilities.dataSourceForArray(self);
    }

    public static NSSet set(NSArray self) {
        return ERXArrayUtilities.setFromArray(self);
    }

    public static boolean containsAnyObjectFromArray(NSArray self, NSArray objects) {
        return ERXArrayUtilities.arrayContainsAnyObjectFromArray(self, objects);
    }

    public static boolean containsArray(NSArray self, NSArray objects) {
        return ERXArrayUtilities.arrayContainsArray(self, objects);
    }

    public static boolean areIdenticalSets(NSArray self, NSArray other) {
        return ERXArrayUtilities.arraysAreIdenticalSets(self, other);
    }

    public static Object computeAvgForKey(NSArray self, String key) {
        return self.valueForKeyPath("@" + NSArray.AverageOperatorName + "." + key);
    }

    public static Object computeCountForKey(NSArray self, String key) {
        return self.valueForKeyPath("@" + NSArray.CountOperatorName + "." + key);
    }

    public static Object computeMaxForKey(NSArray self, String key) {
        return self.valueForKeyPath("@" + NSArray.MaximumOperatorName + "." + key);
    }

    public static Object computeMinForKey(NSArray self, String key) {
        return self.valueForKeyPath("@" + NSArray.MinimumOperatorName + "." + key);
    }

    public static Object computeSumForKey(NSArray self, String key) {
        return self.valueForKeyPath("@" + NSArray.SumOperatorName + "." + key);
    }

    public static Object getAt(NSArray self, int index) {
        return self.objectAtIndex(index);
    }

    public static Object getAt(NSArray self, String key) {
        return self.valueForKey(key);
    }

    public static Object getAt(NSArray self, NSArray paths) {
        return ERXArrayUtilities.valuesForKeyPaths(self, paths);
    }

    public static Object getAt(NSArray self, Range range) {
        return getAt(self, range);
    }

    public static Object getAt(NSArray self, ObjectRange range) {
        return getAt(self, range);
    }

    public static List getAt(NSArray self, Collection indices) {
        NSArray answer = new NSMutableArray(indices.size());
        for (Iterator iter = indices.iterator(); iter.hasNext(); ) {
            Object value = iter.next();
            if (value instanceof Range) {
                answer.addAll((Collection) getAt(self, (Range) value));
            } else if (value instanceof List) {
                answer.addAll(getAt(self, (List) value));
            } else {
                int idx = DefaultTypeTransformation.intUnbox(value);
                answer.add(getAt(self, idx));
            }
        }
        return answer;
    }

    public static Object getAt(NSArray self, IntRange range) {
        _RangeInfo info = subListBorders(self.size(), range);
        List answer = self.subarrayWithRange(new NSRange(info.from, (info.to - info.from)));
        if (info.reverse) {
            answer = DefaultGroovyMethods.reverse(answer);
        }
        return answer;
    }

    protected static _RangeInfo subListBorders(int size, IntRange range) {
        int from = normaliseIndex(DefaultTypeTransformation.intUnbox(range.getFrom()), size);
        int to = normaliseIndex(DefaultTypeTransformation.intUnbox(range.getTo()), size);
        boolean reverse = range.isReverse();
        if (from > to) {
            int tmp = to;
            to = from;
            from = tmp;
            reverse = !reverse;
        }
        return new _RangeInfo(from, to + 1, reverse);
    }

    protected static int normaliseIndex(int i, int size) {
        int temp = i;
        if (i < 0) {
            i += size;
        }
        if (i < 0) {
            throw new ArrayIndexOutOfBoundsException("Negative array index [" + temp + "] too large for array size " + size);
        }
        return i;
    }

    public static int count(NSArray self, Object object) {
        int count = 0;
        for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
            if (e.nextElement().equals(object)) count++;
        }
        return count;
    }

    public static NSArray valuesForKeyPaths(NSArray self, NSArray paths) {
        return ERXArrayUtilities.valuesForKeyPaths(self, paths);
    }

    public static NSArray flatten(NSArray self) {
        return ERXArrayUtilities.flatten(self);
    }

    public static NSArray filteredArrayWithQualifier(NSArray self, EOQualifier qualifier) {
        return EOQualifier.filteredArrayWithQualifier(self, qualifier);
    }

    public static NSArray filteredArrayWithEntityFetchSpecification(NSArray self, String fetchSpec, String entity) {
        return ERXArrayUtilities.filteredArrayWithEntityFetchSpecification(self, fetchSpec, entity);
    }

    public static NSArray filteredArrayWithEntityFetchSpecification(NSArray self, String entity, String fetchSpec, NSDictionary bindings) {
        return ERXArrayUtilities.filteredArrayWithEntityFetchSpecification(self, entity, fetchSpec, bindings);
    }

    public static NSArray filteredArrayWithQualifierEvaluation(NSArray self, EOQualifierEvaluation qualifier) {
        return ERXArrayUtilities.filteredArrayWithQualifierEvaluation(self, qualifier);
    }

    public static NSArray sortedArrayUsingKeyOrderArray(NSArray self, NSArray sortOrderings) {
        return EOSortOrdering.sortedArrayUsingKeyOrderArray(self, sortOrderings);
    }

    public static NSArray sortedArraySortedWithKey(NSArray self, String key, NSSelector selector) {
        return ERXArrayUtilities.sortedArraySortedWithKey(self, key, selector);
    }

    public static NSMutableArray sortedMutableArraySortedWithKey(NSArray self, String key) {
        return ERXArrayUtilities.sortedMutableArraySortedWithKey(self, key);
    }

    public static NSArray sort(NSArray self) {
        ArrayList list = self.arrayList();
        Collections.sort(list);
        NSArray answer = new NSArray(list, true);
        return answer;
    }

    public static NSArray sort(NSArray self, String key) {
        return ERXArrayUtilities.sortedArraySortedWithKey(self, key);
    }

    public static NSArray sort(NSArray self, String key, NSSelector selector) {
        return ERXArrayUtilities.sortedArraySortedWithKey(self, key, selector);
    }

    public static NSArray sortAsc(NSArray self, String key) {
        return ERXArrayUtilities.sortedArraySortedWithKey(self, key);
    }

    public static NSArray sortDesc(NSArray self, String key) {
        return ERXArrayUtilities.sortedArraySortedWithKey(self, key, EOSortOrdering.CompareDescending);
    }

    public static NSArray sortInsensitiveAsc(NSArray self, String key) {
        return ERXArrayUtilities.sortedArraySortedWithKey(self, key, EOSortOrdering.CompareCaseInsensitiveAscending);
    }

    public static Object sortInsensitiveDesc(NSArray self, String key) {
        return ERXArrayUtilities.sortedArraySortedWithKey(self, key, EOSortOrdering.CompareCaseInsensitiveDescending);
    }

    public static NSArray plus(NSArray left, NSArray right) {
        return left.arrayByAddingObjectsFromArray(right);
    }

    public static NSArray plus(NSArray left, Object right) {
        return left.arrayByAddingObject(right);
    }

    public static NSArray minus(NSArray left, NSArray right) {
        return ERXArrayUtilities.arrayMinusArray(left, right);
    }

    public static NSArray minus(NSArray left, Object right) {
        return ERXArrayUtilities.arrayMinusObject(left, right);
    }

    public static NSArray reverse(NSArray self) {
        return ERXArrayUtilities.reverse(self);
    }

    public static void each(NSArray self, Closure closure) {
        for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
            closure.call(e.nextElement());
        }
    }

    public static void eachWithIndex(NSArray self, Closure closure) {
        int counter = 0;
        for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
            Object[] objects = new Object[2];
            objects[0] = e.nextElement();
            objects[1] = new Integer(counter++);
            closure.call(objects);
        }
    }

    public static void reverseEach(NSArray self, Closure closure) {
        for (Enumeration e = self.reverseObjectEnumerator(); e.hasMoreElements(); ) {
            closure.call(e.nextElement());
        }
    }

    public static NSArray collect(NSArray self, Closure closure) {
        NSMutableArray answer = new NSMutableArray();
        for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
            answer.addObject(closure.call(e.nextElement()));
            if (closure.getDirective() == Closure.DONE) {
                break;
            }
        }
        return answer;
    }

    public static Object find(NSArray self, Closure closure) {
        for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
            Object value = e.nextElement();
            if (DefaultTypeTransformation.castToBoolean(closure.call(value))) {
                return value;
            }
        }
        return null;
    }

    public static NSArray findAll(NSArray self, Closure closure) {
        NSMutableArray answer = new NSMutableArray(self);
        for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
            Object value = e.nextElement();
            if (DefaultTypeTransformation.castToBoolean(closure.call(value))) {
                answer.addObject(value);
            }
        }
        return answer;
    }

    public static Object inject(NSArray self, Object value, Closure closure) {
        Object[] params = new Object[2];
        for (Enumeration e = self.objectEnumerator(); e.hasMoreElements(); ) {
            Object item = e.nextElement();
            params[0] = value;
            params[1] = item;
            value = closure.call(params);
        }
        return value;
    }

    public static NSDictionary dictionaryOfFormValues(NSArray self, String separator, boolean encrypt) {
        return ERXEOEncodingUtilities.dictionaryOfFormValuesForEnterpriseObjects(self, separator, encrypt);
    }

    public static String stringByEncodingPrimaryKeys(NSArray self, String separator, boolean encrypt) {
        return ERXEOEncodingUtilities.encodeEnterpriseObjectsPrimaryKeyForUrl(self, separator, encrypt);
    }

    protected static class _RangeInfo {

        protected int from, to;

        protected boolean reverse;

        public _RangeInfo(int from, int to, boolean reverse) {
            this.from = from;
            this.to = to;
            this.reverse = reverse;
        }

        public int getFrom() {
            return this.from;
        }

        public int getTo() {
            return this.to;
        }
    }
}
