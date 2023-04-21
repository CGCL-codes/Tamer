package jezuch.utils.observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import jezuch.utils.Pair;
import org.testng.annotations.Test;

/**
 * @author ksobolewski
 */
@SuppressWarnings({ "unchecked" })
@Test(groups = { "jezuch.utils" })
public class ObservableTest {

    private static <L, R> Pair<L, R> p(L left, R right) {
        return Pair.make(left, right);
    }

    private static <L, R> EntryPair<L, R> e(L left, R right) {
        return new EntryPair<L, R>(left, right);
    }

    private static <T> Set<T> set(T... ts) {
        return set(Arrays.asList(ts));
    }

    private static <T> Set<T> set(Collection<T> ts) {
        return new HashSet<T>(ts);
    }

    private static <T> List<T> list(T... ts) {
        return Arrays.asList(ts);
    }

    public void observableCollectionBasic() {
        Collection<String> src = new ArrayList<String>();
        ObservableCollection<String> col = new ObservableCollection<String>(src);
        CollectionAccum<String> accum = new CollectionAccum<String>();
        col.addCollectionListener(accum);
        assert accum.getAll().equals(list());
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.add("blah");
        assert src.equals(list("blah"));
        assert col.containsAll(list("blah"));
        assert accum.getAll().equals(list(list("blah")));
        assert accum.getAdds().equals(list(list("blah")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.addAll(list("ple", "ple", "ple"));
        assert src.equals(list("blah", "ple", "ple", "ple"));
        assert col.containsAll(list("blah", "ple", "ple", "ple"));
        assert accum.getAll().equals(list(list("ple", "ple", "ple")));
        assert accum.getAdds().equals(list(list("ple", "ple", "ple")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.remove("blah");
        assert src.equals(list("ple", "ple", "ple"));
        assert col.containsAll(list("ple", "ple", "ple"));
        assert accum.getAll().equals(list(list("blah")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list("blah")));
        accum.reset();
        col.addAll(list("sru", "tutu"));
        assert src.equals(list("ple", "ple", "ple", "sru", "tutu"));
        assert col.containsAll(list("ple", "ple", "ple", "sru", "tutu"));
        assert accum.getAll().equals(list(list("sru", "tutu")));
        assert accum.getAdds().equals(list(list("sru", "tutu")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.removeAll(list("ple"));
        assert src.equals(list("sru", "tutu"));
        assert col.containsAll(list("sru", "tutu"));
        assert accum.getAll().equals(list(list("ple", "ple", "ple")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list("ple", "ple", "ple")));
        accum.reset();
        col.retainAll(list("tutu"));
        assert src.equals(list("tutu"));
        assert col.containsAll(list("tutu"));
        assert accum.getAll().equals(list(list("sru")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list("sru")));
        accum.reset();
        col.clear();
        assert src.equals(list());
        assert col.containsAll(list());
        assert accum.getAll().equals(list(list("tutu")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list("tutu")));
    }

    public void observableSetBasic() {
        Set<String> src = new HashSet<String>();
        ObservableSet<String> col = new ObservableSet<String>(src);
        SetAccum<String> accum = new SetAccum<String>();
        col.addCollectionListener(accum);
        assert accum.getAll().equals(list());
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.add("blah");
        assert src.equals(set("blah"));
        assert col.equals(set("blah"));
        assert accum.getAll().equals(list(set("blah")));
        assert accum.getAdds().equals(list(set("blah")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.addAll(list("ple", "blah", "ple", "ple"));
        assert src.equals(set("blah", "ple"));
        assert col.equals(set("blah", "ple"));
        assert accum.getAll().equals(list(set("ple")));
        assert accum.getAdds().equals(list(set("ple")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.remove("blah");
        assert src.equals(set("ple"));
        assert col.equals(set("ple"));
        assert accum.getAll().equals(list(set("blah")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set("blah")));
        accum.reset();
        col.addAll(list("sru", "tutu"));
        assert src.equals(set("ple", "sru", "tutu"));
        assert col.equals(set("ple", "sru", "tutu"));
        assert accum.getAll().equals(list(set("sru", "tutu")));
        assert accum.getAdds().equals(list(set("sru", "tutu")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.removeAll(list("ple"));
        assert src.equals(set("sru", "tutu"));
        assert col.equals(set("sru", "tutu"));
        assert accum.getAll().equals(list(set("ple")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set("ple")));
        accum.reset();
        col.retainAll(list("tutu"));
        assert src.equals(set("tutu"));
        assert col.equals(set("tutu"));
        assert accum.getAll().equals(list(set("sru")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set("sru")));
        accum.reset();
        col.clear();
        assert src.equals(set(new String[0]));
        assert col.equals(set(new String[0]));
        assert accum.getAll().equals(list(set("tutu")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set("tutu")));
    }

    public void observableSortedSetBasic() {
        SortedSet<String> src = new TreeSet<String>();
        ObservableSortedSet<String> col = new ObservableSortedSet<String>(src);
        SortedSetAccum<String> accum = new SortedSetAccum<String>();
        col.addCollectionListener(accum);
        assert accum.getAll().equals(list());
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.add("blah");
        assert src.equals(set("blah"));
        assert col.equals(set("blah"));
        assert accum.getAll().equals(list(set("blah")));
        assert accum.getAdds().equals(list(set("blah")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.addAll(list("ple", "blah", "ple", "ple"));
        assert src.equals(set("blah", "ple"));
        assert col.equals(set("blah", "ple"));
        assert accum.getAll().equals(list(set("ple")));
        assert accum.getAdds().equals(list(set("ple")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.remove("blah");
        assert src.equals(set("ple"));
        assert col.equals(set("ple"));
        assert accum.getAll().equals(list(set("blah")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set("blah")));
        accum.reset();
        col.addAll(list("sru", "tutu"));
        assert src.equals(set("ple", "sru", "tutu"));
        assert col.equals(set("ple", "sru", "tutu"));
        assert accum.getAll().equals(list(set("sru", "tutu")));
        assert accum.getAdds().equals(list(set("sru", "tutu")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        col.removeAll(list("ple"));
        assert src.equals(set("sru", "tutu"));
        assert col.equals(set("sru", "tutu"));
        assert accum.getAll().equals(list(set("ple")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set("ple")));
        accum.reset();
        col.retainAll(list("tutu"));
        assert src.equals(set("tutu"));
        assert col.equals(set("tutu"));
        assert accum.getAll().equals(list(set("sru")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set("sru")));
        accum.reset();
        col.clear();
        assert src.equals(set(new String[0]));
        assert col.equals(set(new String[0]));
        assert accum.getAll().equals(list(set("tutu")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set("tutu")));
    }

    public void observableSortedSet() {
        SortedSet<String> src = new TreeSet<String>();
        ObservableSortedSet<String> col = new ObservableSortedSet<String>(src);
        SortedSetAccum<String> accum = new SortedSetAccum<String>();
        col.addCollectionListener(accum);
        src.addAll(list("1", "2", "3", "4", "5", "6"));
        SortedSet<String> head = col.headSet("4");
        SortedSet<String> tail = col.tailSet("4");
        SortedSet<String> sub = col.subSet("2", "6");
        accum.reset();
        head.add("0");
        assert col.equals(set("0", "1", "2", "3", "4", "5", "6"));
        assert accum.getAll().equals(list(set("0")));
        assert accum.getAdds().equals(list(set("0")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        head.remove("1");
        assert col.equals(set("0", "2", "3", "4", "5", "6"));
        assert accum.getAll().equals(list(set("1")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set("1")));
        accum.reset();
        tail.add("7");
        assert col.equals(set("0", "2", "3", "4", "5", "6", "7"));
        assert accum.getAll().equals(list(set("7")));
        assert accum.getAdds().equals(list(set("7")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        tail.remove("6");
        assert col.equals(set("0", "2", "3", "4", "5", "7"));
        assert accum.getAll().equals(list(set("6")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set("6")));
        accum.reset();
        sub.add("3a");
        assert col.equals(set("0", "2", "3", "3a", "4", "5", "7"));
        assert accum.getAll().equals(list(set("3a")));
        assert accum.getAdds().equals(list(set("3a")));
        assert accum.getRemoves().equals(list());
        accum.reset();
        sub.remove("3");
        assert col.equals(set("0", "2", "3a", "4", "5", "7"));
        assert accum.getAll().equals(list(set("3")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set("3")));
    }

    public void observableListBasic() {
        List<String> src = new ArrayList<String>();
        ObservableList<String> col = new ObservableList<String>(src);
        ListAccum<String> accum = new ListAccum<String>();
        col.addCollectionListener(accum);
        assert accum.getAll().equals(list());
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        col.add("blah");
        assert src.equals(list("blah"));
        assert col.equals(list("blah"));
        assert accum.getAll().equals(list(list(p("blah", 0))));
        assert accum.getAdds().equals(list(list(p("blah", 0))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        col.addAll(list("ple", "ple", "ple"));
        assert src.equals(list("blah", "ple", "ple", "ple"));
        assert col.equals(list("blah", "ple", "ple", "ple"));
        assert accum.getAll().equals(list(list(p("ple", 1), p("ple", 2), p("ple", 3))));
        assert accum.getAdds().equals(list(list(p("ple", 1), p("ple", 2), p("ple", 3))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        col.remove("blah");
        assert src.equals(list("ple", "ple", "ple"));
        assert col.equals(list("ple", "ple", "ple"));
        assert accum.getAll().equals(list(list(p("blah", 0))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list(p("blah", 0))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.addAll(list("sru", "tutu"));
        assert src.equals(list("ple", "ple", "ple", "sru", "tutu"));
        assert col.equals(list("ple", "ple", "ple", "sru", "tutu"));
        assert accum.getAll().equals(list(list(p("sru", 3), p("tutu", 4))));
        assert accum.getAdds().equals(list(list(p("sru", 3), p("tutu", 4))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        col.removeAll(list("ple"));
        assert src.equals(list("sru", "tutu"));
        assert col.equals(list("sru", "tutu"));
        assert accum.getAll().equals(list(list(p("ple", 0), p("ple", 1), p("ple", 2))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list(p("ple", 0), p("ple", 1), p("ple", 2))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.retainAll(list("sru"));
        assert src.equals(list("sru"));
        assert col.equals(list("sru"));
        assert accum.getAll().equals(list(list(p("tutu", 1))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list(p("tutu", 1))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.clear();
        assert src.equals(list());
        assert col.equals(list());
        assert accum.getAll().equals(list(list(p("sru", 0))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list(p("sru", 0))));
        assert accum.getModifs().equals(list());
    }

    public void observableCollectionIterator() {
        List<String> src = new ArrayList<String>();
        ObservableCollection<String> col = new ObservableCollection<String>(src);
        CollectionAccum<String> accum = new CollectionAccum<String>();
        col.addCollectionListener(accum);
        src.addAll(list("1", "2", "3", "4", "5", "6"));
        Iterator<String> it = col.iterator();
        it.next();
        it.next();
        it.remove();
        it.next();
        it.next();
        it.remove();
        it.next();
        it.next();
        it.remove();
        assert src.equals(list("1", "3", "5"));
        assert accum.getAll().equals(list(list("2"), list("4"), list("6")));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list("2"), list("4"), list("6")));
    }

    public void observableList() {
        List<String> src = new ArrayList<String>();
        ObservableList<String> col = new ObservableList<String>(src);
        ListAccum<String> accum = new ListAccum<String>();
        col.addCollectionListener(accum);
        accum.reset();
        col.addAll(list("1", "2", "3"));
        assert src.equals(list("1", "2", "3"));
        assert accum.getAll().equals(list(list(p("1", 0), p("2", 1), p("3", 2))));
        assert accum.getAdds().equals(list(list(p("1", 0), p("2", 1), p("3", 2))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        col.add(1, "x");
        assert src.equals(list("1", "x", "2", "3"));
        assert accum.getAll().equals(list(list(p("x", 1))));
        assert accum.getAdds().equals(list(list(p("x", 1))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        col.remove(2);
        assert src.equals(list("1", "x", "3"));
        assert accum.getAll().equals(list(list(p("2", 2))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list(p("2", 2))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.addAll(2, list("q", "w"));
        assert src.equals(list("1", "x", "q", "w", "3"));
        assert accum.getAll().equals(list(list(p("q", 2), p("w", 3))));
        assert accum.getAdds().equals(list(list(p("q", 2), p("w", 3))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        col.set(3, "~");
        assert src.equals(list("1", "x", "q", "~", "3"));
        assert accum.getAll().equals(list(list(p("w", 3))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list(list(p("w", 3))));
        accum.reset();
        col.clear();
        assert src.equals(list());
        assert accum.getAll().equals(list(list(p("1", 0), p("x", 1), p("q", 2), p("~", 3), p("3", 4))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list(p("1", 0), p("x", 1), p("q", 2), p("~", 3), p("3", 4))));
        assert accum.getModifs().equals(list());
    }

    public void observableListIterator() {
        List<String> src = new ArrayList<String>();
        ObservableList<String> col = new ObservableList<String>(src);
        ListAccum<String> accum = new ListAccum<String>();
        col.addCollectionListener(accum);
        src.addAll(list("1", "2", "3", "4", "5"));
        accum.reset();
        ListIterator<String> it = col.listIterator();
        it.next();
        it.next();
        it.remove();
        assert src.equals(list("1", "3", "4", "5"));
        assert accum.getAll().equals(list(list(p("2", 1))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list(p("2", 1))));
        assert accum.getModifs().equals(list());
        accum.reset();
        it.next();
        it.set("q");
        assert src.equals(list("1", "q", "4", "5"));
        assert accum.getAll().equals(list(list(p("3", 1))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list(list(p("3", 1))));
        accum.reset();
        it.next();
        it.next();
        it.previous();
        it.add("~");
        assert src.equals(list("1", "q", "4", "~", "5"));
        assert accum.getAll().equals(list(list(p("~", 3))));
        assert accum.getAdds().equals(list(list(p("~", 3))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
    }

    public void observableListSublist() {
        List<String> src = new ArrayList<String>();
        ObservableList<String> col = new ObservableList<String>(src);
        ListAccum<String> accum = new ListAccum<String>();
        col.addCollectionListener(accum);
        src.addAll(list("1", "2", "3", "4", "5", "6"));
        List<String> sub = col.subList(1, 5);
        accum.reset();
        sub.add("q");
        assert src.equals(list("1", "2", "3", "4", "5", "q", "6"));
        assert accum.getAll().equals(list(list(p("q", 5))));
        assert accum.getAdds().equals(list(list(p("q", 5))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        sub.set(1, "w");
        assert src.equals(list("1", "2", "w", "4", "5", "q", "6"));
        assert accum.getAll().equals(list(list(p("3", 2))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list(list(p("3", 2))));
        accum.reset();
        sub.remove(4);
        assert src.equals(list("1", "2", "w", "4", "5", "6"));
        assert accum.getAll().equals(list(list(p("q", 5))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list(p("q", 5))));
        assert accum.getModifs().equals(list());
        accum.reset();
        Iterator<String> sit = sub.iterator();
        sit.next();
        sit.next();
        sit.remove();
        assert src.equals(list("1", "2", "4", "5", "6"));
        assert accum.getAll().equals(list(list(p("w", 2))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list(p("w", 2))));
        assert accum.getModifs().equals(list());
        accum.reset();
        sub.clear();
        assert src.equals(list("1", "6"));
        assert accum.getAll().equals(list(list(p("2", 1), p("4", 2), p("5", 3))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(list(p("2", 1), p("4", 2), p("5", 3))));
        assert accum.getModifs().equals(list());
    }

    public void observableMapBasic() {
        Map<String, Integer> src = new HashMap<String, Integer>();
        ObservableMap<String, Integer> col = new ObservableMap<String, Integer>(src);
        MapAccum<String, Integer> accum = new MapAccum<String, Integer>();
        col.addCollectionListener(accum);
        Map<String, Integer> test = new LinkedHashMap<String, Integer>();
        test.put("1", 2);
        test.put("2", 4);
        test.put("3", 6);
        accum.reset();
        col.put("1", 1);
        assert col.entrySet().equals(set(e("1", 1)));
        assert accum.getAll().equals(list(set(p("1", 1))));
        assert accum.getAdds().equals(list(set(p("1", 1))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        col.put("1", 2);
        assert col.entrySet().equals(set(e("1", 2)));
        assert accum.getAll().equals(list(set(p("1", 1))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list(set(p("1", 1))));
        col.put("0", 0);
        col.put("2", 3);
        accum.reset();
        col.putAll(test);
        assert col.entrySet().equals(set(e("0", 0), e("1", 2), e("2", 4), e("3", 6)));
        assert accum.getAll().equals(list(set(p("1", 2), p("2", 3)), set(p("3", 6))));
        assert accum.getAdds().equals(list(set(p("3", 6))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list(set(p("1", 2), p("2", 3))));
        accum.reset();
        col.remove("2");
        assert col.entrySet().equals(set(e("0", 0), e("1", 2), e("3", 6)));
        assert accum.getAll().equals(list(set(p("2", 4))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("2", 4))));
        assert accum.getModifs().equals(list());
    }

    public void observableMapEntrySet() {
        Map<String, Integer> src = new HashMap<String, Integer>();
        ObservableMap<String, Integer> col = new ObservableMap<String, Integer>(src);
        MapAccum<String, Integer> accum = new MapAccum<String, Integer>();
        col.addCollectionListener(accum);
        col.put("1", 1);
        col.put("2", 2);
        col.put("3", 3);
        col.put("4", 4);
        col.put("5", 5);
        accum.reset();
        col.entrySet().remove(e("2", 2));
        assert col.entrySet().equals(set(e("1", 1), e("3", 3), e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("2", 2))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("2", 2))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.entrySet().removeAll(set(e("2", 2), e("3", 3)));
        assert col.entrySet().equals(set(e("1", 1), e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("3", 3))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("3", 3))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.entrySet().retainAll(set(e("2", 2), e("4", 4), e("5", 5)));
        assert col.entrySet().equals(set(e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("1", 1))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("1", 1))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.entrySet().clear();
        assert col.entrySet().equals(set());
        assert accum.getAll().equals(list(set(p("4", 4), p("5", 5))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("4", 4), p("5", 5))));
        assert accum.getModifs().equals(list());
    }

    public void observableMapEntrySetIterator() {
        Map<String, Integer> src = new LinkedHashMap<String, Integer>();
        ObservableMap<String, Integer> col = new ObservableMap<String, Integer>(src);
        MapAccum<String, Integer> accum = new MapAccum<String, Integer>();
        col.addCollectionListener(accum);
        col.put("1", 1);
        col.put("2", 2);
        col.put("3", 3);
        col.put("4", 4);
        col.put("5", 5);
        accum.reset();
        Iterator<Entry<String, Integer>> it = col.entrySet().iterator();
        assert it.next().equals(e("1", 1));
        assert it.next().equals(e("2", 2));
        assert it.next().equals(e("3", 3));
        it.remove();
        assert col.entrySet().equals(set(e("1", 1), e("2", 2), e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("3", 3))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("3", 3))));
        assert accum.getModifs().equals(list());
    }

    public void observableMapKeySet() {
        Map<String, Integer> src = new HashMap<String, Integer>();
        ObservableMap<String, Integer> col = new ObservableMap<String, Integer>(src);
        MapAccum<String, Integer> accum = new MapAccum<String, Integer>();
        col.addCollectionListener(accum);
        col.put("1", 1);
        col.put("2", 2);
        col.put("3", 3);
        col.put("4", 4);
        col.put("5", 5);
        accum.reset();
        col.keySet().remove("2");
        assert col.entrySet().equals(set(e("1", 1), e("3", 3), e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("2", 2))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("2", 2))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.keySet().removeAll(set("2", "3"));
        assert col.entrySet().equals(set(e("1", 1), e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("3", 3))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("3", 3))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.keySet().retainAll(set("2", "4", "5"));
        assert col.entrySet().equals(set(e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("1", 1))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("1", 1))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.keySet().clear();
        assert col.entrySet().equals(set());
        assert accum.getAll().equals(list(set(p("4", 4), p("5", 5))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("4", 4), p("5", 5))));
        assert accum.getModifs().equals(list());
    }

    public void observableMapKeySetIterator() {
        Map<String, Integer> src = new LinkedHashMap<String, Integer>();
        ObservableMap<String, Integer> col = new ObservableMap<String, Integer>(src);
        MapAccum<String, Integer> accum = new MapAccum<String, Integer>();
        col.addCollectionListener(accum);
        col.put("1", 1);
        col.put("2", 2);
        col.put("3", 3);
        col.put("4", 4);
        col.put("5", 5);
        accum.reset();
        Iterator<String> it = col.keySet().iterator();
        assert it.next().equals("1");
        assert it.next().equals("2");
        assert it.next().equals("3");
        it.remove();
        assert col.entrySet().equals(set(e("1", 1), e("2", 2), e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("3", 3))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("3", 3))));
        assert accum.getModifs().equals(list());
    }

    public void observableMapValueColl() {
        Map<String, Integer> src = new HashMap<String, Integer>();
        ObservableMap<String, Integer> col = new ObservableMap<String, Integer>(src);
        MapAccum<String, Integer> accum = new MapAccum<String, Integer>();
        col.addCollectionListener(accum);
        col.put("1", 1);
        col.put("2", 2);
        col.put("3", 3);
        col.put("4", 4);
        col.put("5", 5);
        accum.reset();
        col.values().remove(2);
        assert col.entrySet().equals(set(e("1", 1), e("3", 3), e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("2", 2))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("2", 2))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.values().removeAll(set(2, 3));
        assert col.entrySet().equals(set(e("1", 1), e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("3", 3))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("3", 3))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.values().retainAll(set(2, 4, 5));
        assert col.entrySet().equals(set(e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("1", 1))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("1", 1))));
        assert accum.getModifs().equals(list());
        accum.reset();
        col.keySet().clear();
        assert col.entrySet().equals(set());
        assert accum.getAll().equals(list(set(p("4", 4), p("5", 5))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("4", 4), p("5", 5))));
        assert accum.getModifs().equals(list());
    }

    public void observableMapValueCollIterator() {
        Map<String, Integer> src = new LinkedHashMap<String, Integer>();
        ObservableMap<String, Integer> col = new ObservableMap<String, Integer>(src);
        MapAccum<String, Integer> accum = new MapAccum<String, Integer>();
        col.addCollectionListener(accum);
        col.put("1", 1);
        col.put("2", 2);
        col.put("3", 3);
        col.put("4", 4);
        col.put("5", 5);
        accum.reset();
        Iterator<Integer> it = col.values().iterator();
        assert it.next() == 1;
        assert it.next() == 2;
        assert it.next() == 3;
        it.remove();
        assert col.entrySet().equals(set(e("1", 1), e("2", 2), e("4", 4), e("5", 5)));
        assert accum.getAll().equals(list(set(p("3", 3))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("3", 3))));
        assert accum.getModifs().equals(list());
    }

    public void observableSortedMapBasic() {
        SortedMap<String, Integer> src = new TreeMap<String, Integer>();
        ObservableSortedMap<String, Integer> col = new ObservableSortedMap<String, Integer>(src);
        SortedMapAccum<String, Integer> accum = new SortedMapAccum<String, Integer>();
        col.addCollectionListener(accum);
        Map<String, Integer> test = new LinkedHashMap<String, Integer>();
        test.put("1", 2);
        test.put("2", 4);
        test.put("3", 6);
        accum.reset();
        col.put("1", 1);
        assert col.entrySet().equals(set(e("1", 1)));
        assert accum.getAll().equals(list(set(p("1", 1))));
        assert accum.getAdds().equals(list(set(p("1", 1))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        col.put("1", 2);
        assert col.entrySet().equals(set(e("1", 2)));
        assert accum.getAll().equals(list(set(p("1", 1))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list(set(p("1", 1))));
        col.put("0", 0);
        col.put("2", 3);
        accum.reset();
        col.putAll(test);
        assert col.entrySet().equals(set(e("0", 0), e("1", 2), e("2", 4), e("3", 6)));
        assert accum.getAll().equals(list(set(p("1", 2), p("2", 3)), set(p("3", 6))));
        assert accum.getAdds().equals(list(set(p("3", 6))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list(set(p("1", 2), p("2", 3))));
        accum.reset();
        col.remove("2");
        assert col.entrySet().equals(set(e("0", 0), e("1", 2), e("3", 6)));
        assert accum.getAll().equals(list(set(p("2", 4))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("2", 4))));
        assert accum.getModifs().equals(list());
    }

    public void observableSortedMapHeadMap() {
        SortedMap<String, Integer> src = new TreeMap<String, Integer>();
        ObservableSortedMap<String, Integer> col = new ObservableSortedMap<String, Integer>(src);
        SortedMapAccum<String, Integer> accum = new SortedMapAccum<String, Integer>();
        col.addCollectionListener(accum);
        col.put("1", 1);
        col.put("2", 2);
        col.put("3", 3);
        col.put("4", 4);
        col.put("5", 5);
        col.put("6", 6);
        col.put("7", 7);
        SortedMap<String, Integer> head = col.headMap("6");
        accum.reset();
        head.put("0", 0);
        assert col.entrySet().equals(set(e("0", 0), e("1", 1), e("2", 2), e("3", 3), e("4", 4), e("5", 5), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("0", 0))));
        assert accum.getAdds().equals(list(set(p("0", 0))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        head.remove("1");
        assert col.entrySet().equals(set(e("0", 0), e("2", 2), e("3", 3), e("4", 4), e("5", 5), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("1", 1))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("1", 1))));
        assert accum.getModifs().equals(list());
        accum.reset();
        head.entrySet().removeAll(set(e("1", 1), e("2", 2)));
        assert col.entrySet().equals(set(e("0", 0), e("3", 3), e("4", 4), e("5", 5), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("2", 2))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("2", 2))));
        assert accum.getModifs().equals(list());
        accum.reset();
        head.keySet().removeAll(set("2", "3"));
        assert col.entrySet().equals(set(e("0", 0), e("4", 4), e("5", 5), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("3", 3))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("3", 3))));
        assert accum.getModifs().equals(list());
        accum.reset();
        head.values().removeAll(set(3, 4));
        assert col.entrySet().equals(set(e("0", 0), e("5", 5), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("4", 4))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("4", 4))));
        assert accum.getModifs().equals(list());
        accum.reset();
        Iterator<String> it = head.keySet().iterator();
        assert it.next().equals("0");
        assert it.next().equals("5");
        it.remove();
        assert col.entrySet().equals(set(e("0", 0), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("5", 5))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("5", 5))));
        assert accum.getModifs().equals(list());
    }

    public void observableSortedMapTailMap() {
        SortedMap<String, Integer> src = new TreeMap<String, Integer>();
        ObservableSortedMap<String, Integer> col = new ObservableSortedMap<String, Integer>(src);
        SortedMapAccum<String, Integer> accum = new SortedMapAccum<String, Integer>();
        col.addCollectionListener(accum);
        col.put("1", 1);
        col.put("2", 2);
        col.put("3", 3);
        col.put("4", 4);
        col.put("5", 5);
        col.put("6", 6);
        col.put("7", 7);
        SortedMap<String, Integer> tail = col.tailMap("3");
        accum.reset();
        tail.put("8", 8);
        assert col.entrySet().equals(set(e("8", 8), e("1", 1), e("2", 2), e("3", 3), e("4", 4), e("5", 5), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("8", 8))));
        assert accum.getAdds().equals(list(set(p("8", 8))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        tail.remove("4");
        assert col.entrySet().equals(set(e("8", 8), e("2", 2), e("3", 3), e("1", 1), e("5", 5), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("4", 4))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("4", 4))));
        assert accum.getModifs().equals(list());
        accum.reset();
        tail.entrySet().removeAll(set(e("4", 4), e("5", 5)));
        assert col.entrySet().equals(set(e("8", 8), e("3", 3), e("1", 1), e("2", 2), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("5", 5))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("5", 5))));
        assert accum.getModifs().equals(list());
        accum.reset();
        tail.keySet().removeAll(set("5", "6"));
        assert col.entrySet().equals(set(e("8", 8), e("1", 1), e("2", 2), e("3", 3), e("7", 7)));
        assert accum.getAll().equals(list(set(p("6", 6))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("6", 6))));
        assert accum.getModifs().equals(list());
        accum.reset();
        tail.values().removeAll(set(6, 7));
        assert col.entrySet().equals(set(e("8", 8), e("2", 2), e("3", 3), e("1", 1)));
        assert accum.getAll().equals(list(set(p("7", 7))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("7", 7))));
        assert accum.getModifs().equals(list());
        accum.reset();
        Iterator<String> it = tail.keySet().iterator();
        assert it.next().equals("3");
        assert it.next().equals("8");
        it.remove();
        assert col.entrySet().equals(set(e("3", 3), e("2", 2), e("1", 1)));
        assert accum.getAll().equals(list(set(p("8", 8))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("8", 8))));
        assert accum.getModifs().equals(list());
    }

    public void observableSortedMapSubMap() {
        SortedMap<String, Integer> src = new TreeMap<String, Integer>();
        ObservableSortedMap<String, Integer> col = new ObservableSortedMap<String, Integer>(src);
        SortedMapAccum<String, Integer> accum = new SortedMapAccum<String, Integer>();
        col.addCollectionListener(accum);
        col.put("1", 1);
        col.put("2", 2);
        col.put("3", 3);
        col.put("4", 4);
        col.put("5", 5);
        col.put("6", 6);
        col.put("7", 7);
        SortedMap<String, Integer> sub = col.subMap("3", "9");
        accum.reset();
        sub.put("8", 8);
        assert col.entrySet().equals(set(e("8", 8), e("1", 1), e("2", 2), e("3", 3), e("4", 4), e("5", 5), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("8", 8))));
        assert accum.getAdds().equals(list(set(p("8", 8))));
        assert accum.getRemoves().equals(list());
        assert accum.getModifs().equals(list());
        accum.reset();
        sub.remove("4");
        assert col.entrySet().equals(set(e("8", 8), e("2", 2), e("3", 3), e("1", 1), e("5", 5), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("4", 4))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("4", 4))));
        assert accum.getModifs().equals(list());
        accum.reset();
        sub.entrySet().removeAll(set(e("4", 4), e("5", 5)));
        assert col.entrySet().equals(set(e("8", 8), e("3", 3), e("1", 1), e("2", 2), e("6", 6), e("7", 7)));
        assert accum.getAll().equals(list(set(p("5", 5))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("5", 5))));
        assert accum.getModifs().equals(list());
        accum.reset();
        sub.keySet().removeAll(set("5", "6"));
        assert col.entrySet().equals(set(e("8", 8), e("1", 1), e("2", 2), e("3", 3), e("7", 7)));
        assert accum.getAll().equals(list(set(p("6", 6))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("6", 6))));
        assert accum.getModifs().equals(list());
        accum.reset();
        sub.values().removeAll(set(6, 7));
        assert col.entrySet().equals(set(e("8", 8), e("2", 2), e("3", 3), e("1", 1)));
        assert accum.getAll().equals(list(set(p("7", 7))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("7", 7))));
        assert accum.getModifs().equals(list());
        accum.reset();
        Iterator<String> it = sub.keySet().iterator();
        assert it.next().equals("3");
        assert it.next().equals("8");
        it.remove();
        assert col.entrySet().equals(set(e("3", 3), e("2", 2), e("1", 1)));
        assert accum.getAll().equals(list(set(p("8", 8))));
        assert accum.getAdds().equals(list());
        assert accum.getRemoves().equals(list(set(p("8", 8))));
        assert accum.getModifs().equals(list());
    }

    private abstract static class CollectionModAccum<T, M, E extends AbstractObservableCollection.CollectionEvent<T, M, C, V>, C extends Collection<T>, V extends Collection<M>> {

        private final List<V> all = new ArrayList<V>();

        private final List<V> adds = new ArrayList<V>();

        private final List<V> removes = new ArrayList<V>();

        private final List<V> modifs = new ArrayList<V>();

        CollectionModAccum() {
        }

        protected abstract V copyEventCollection(V c);

        protected void elementsAddedReal(E ev) {
            assert ev.getId() == ObservableCollection.CollectionEvent.ID.ELEMENTS_ADDED;
            V mod = copyEventCollection(ev.getElements());
            all.add(mod);
            adds.add(mod);
        }

        protected void elementsRemovedReal(E ev) {
            assert ev.getId() == ObservableCollection.CollectionEvent.ID.ELEMENTS_REMOVED;
            V mod = copyEventCollection(ev.getElements());
            all.add(mod);
            removes.add(mod);
        }

        protected void elementsModifiedReal(E ev) {
            assert ev.getId() == ObservableCollection.CollectionEvent.ID.ELEMENTS_MODIFIED;
            V mod = copyEventCollection(ev.getElements());
            all.add(mod);
            modifs.add(mod);
        }

        /**
		 * NOTE: "All" set is used usually to test the order of events.
		 */
        public List<V> getAll() {
            return all;
        }

        public List<V> getAdds() {
            return adds;
        }

        public List<V> getRemoves() {
            return removes;
        }

        public List<V> getModifs() {
            return modifs;
        }

        public void reset() {
            all.clear();
            adds.clear();
            removes.clear();
            modifs.clear();
        }
    }

    private static class CollectionAccum<T> extends CollectionModAccum<T, T, ObservableCollection.CollectionEvent<T>, Collection<T>, Collection<T>> implements ObservableCollection.CollectionListener<T> {

        CollectionAccum() {
        }

        @Override
        protected Collection<T> copyEventCollection(Collection<T> c) {
            return new ArrayList<T>(c);
        }

        public void elementsAdded(ObservableCollection.CollectionEvent<T> ev) {
            elementsAddedReal(ev);
        }

        public void elementsRemoved(ObservableCollection.CollectionEvent<T> ev) {
            elementsRemovedReal(ev);
        }

        public void elementsModified(ObservableCollection.CollectionEvent<T> ev) {
            elementsModifiedReal(ev);
        }
    }

    private static class SetAccum<T> extends CollectionModAccum<T, T, ObservableSet.SetEvent<T>, Set<T>, Set<T>> implements ObservableSet.SetListener<T> {

        SetAccum() {
        }

        @Override
        protected Set<T> copyEventCollection(Set<T> c) {
            return new LinkedHashSet<T>(c);
        }

        public void elementsAdded(ObservableSet.SetEvent<T> ev) {
            elementsAddedReal(ev);
        }

        public void elementsRemoved(ObservableSet.SetEvent<T> ev) {
            elementsRemovedReal(ev);
        }

        public void elementsModified(ObservableSet.SetEvent<T> ev) {
            elementsModifiedReal(ev);
        }
    }

    private static class SortedSetAccum<T> extends CollectionModAccum<T, T, ObservableSortedSet.SortedSetEvent<T>, SortedSet<T>, SortedSet<T>> implements ObservableSortedSet.SortedSetListener<T> {

        SortedSetAccum() {
        }

        @Override
        protected SortedSet<T> copyEventCollection(SortedSet<T> c) {
            return new TreeSet<T>(c);
        }

        public void elementsAdded(ObservableSortedSet.SortedSetEvent<T> ev) {
            elementsAddedReal(ev);
        }

        public void elementsRemoved(ObservableSortedSet.SortedSetEvent<T> ev) {
            elementsRemovedReal(ev);
        }

        public void elementsModified(ObservableSortedSet.SortedSetEvent<T> ev) {
            elementsModifiedReal(ev);
        }
    }

    private static class ListAccum<T> extends CollectionModAccum<T, Pair<T, Integer>, ObservableList.ListEvent<T>, List<T>, List<Pair<T, Integer>>> implements ObservableList.ListListener<T> {

        ListAccum() {
        }

        @Override
        protected List<Pair<T, Integer>> copyEventCollection(List<Pair<T, Integer>> c) {
            return new ArrayList<Pair<T, Integer>>(c);
        }

        public void elementsAdded(ObservableList.ListEvent<T> ev) {
            elementsAddedReal(ev);
        }

        public void elementsRemoved(ObservableList.ListEvent<T> ev) {
            elementsRemovedReal(ev);
        }

        public void elementsModified(ObservableList.ListEvent<T> ev) {
            elementsModifiedReal(ev);
        }
    }

    private static class MapModAccum<K, V, E extends AbstractObservableMap.MapEvent<K, V, M>, M extends Map<K, V>> {

        private final List<Set<Pair<K, V>>> all = new ArrayList<Set<Pair<K, V>>>();

        private final List<Set<Pair<K, V>>> adds = new ArrayList<Set<Pair<K, V>>>();

        private final List<Set<Pair<K, V>>> removes = new ArrayList<Set<Pair<K, V>>>();

        private final List<Set<Pair<K, V>>> modifs = new ArrayList<Set<Pair<K, V>>>();

        MapModAccum() {
        }

        protected void elementsAddedReal(E ev) {
            assert ev.getId() == ObservableMap.MapEvent.ID.KEYS_ADDED;
            Set<Pair<K, V>> mod = new HashSet<Pair<K, V>>(ev.getElements());
            all.add(mod);
            adds.add(mod);
        }

        protected void elementsRemovedReal(E ev) {
            assert ev.getId() == ObservableMap.MapEvent.ID.KEYS_REMOVED;
            Set<Pair<K, V>> mod = new HashSet<Pair<K, V>>(ev.getElements());
            all.add(mod);
            removes.add(mod);
        }

        protected void elementsModifiedReal(E ev) {
            assert ev.getId() == ObservableMap.MapEvent.ID.KEYS_MODIFIED;
            Set<Pair<K, V>> mod = new HashSet<Pair<K, V>>(ev.getElements());
            all.add(mod);
            modifs.add(mod);
        }

        public List<Set<Pair<K, V>>> getAll() {
            return all;
        }

        public List<Set<Pair<K, V>>> getAdds() {
            return adds;
        }

        public List<Set<Pair<K, V>>> getRemoves() {
            return removes;
        }

        public List<Set<Pair<K, V>>> getModifs() {
            return modifs;
        }

        public void reset() {
            all.clear();
            adds.clear();
            removes.clear();
            modifs.clear();
        }
    }

    private static class MapAccum<K, V> extends MapModAccum<K, V, ObservableMap.MapEvent<K, V>, Map<K, V>> implements ObservableMap.MapListener<K, V> {

        MapAccum() {
        }

        public void elementsAdded(ObservableMap.MapEvent<K, V> ev) {
            elementsAddedReal(ev);
        }

        public void elementsRemoved(ObservableMap.MapEvent<K, V> ev) {
            elementsRemovedReal(ev);
        }

        public void elementsModified(ObservableMap.MapEvent<K, V> ev) {
            elementsModifiedReal(ev);
        }
    }

    private static class SortedMapAccum<K, V> extends MapModAccum<K, V, ObservableSortedMap.SortedMapEvent<K, V>, SortedMap<K, V>> implements ObservableSortedMap.SortedMapListener<K, V> {

        SortedMapAccum() {
        }

        public void elementsAdded(ObservableSortedMap.SortedMapEvent<K, V> ev) {
            elementsAddedReal(ev);
        }

        public void elementsRemoved(ObservableSortedMap.SortedMapEvent<K, V> ev) {
            elementsRemovedReal(ev);
        }

        public void elementsModified(ObservableSortedMap.SortedMapEvent<K, V> ev) {
            elementsModifiedReal(ev);
        }
    }

    /**
	 * An extension of {@link Pair} that is a {@link Map.Entry} too. This has to
	 * be a separate class because {@link Map.Entry} specifies special contract
	 * for {@link Map.Entry#hashCode()} which is incompatible with
	 * {@link Pair#hashCode() Pair.hashCode()}. So {@link Pair} (and
	 * {@link ObservableTest#p(Object, Object)}) is used in contexts where
	 * {@link Pair} is expected and {@link EntryPair} (and
	 * {@link ObservableTest#e(Object, Object)}) where {@link Map.Entry} is
	 * expected.
	 * 
	 * @author ksobolewski
	 */
    private static class EntryPair<L, R> extends Pair<L, R> implements Map.Entry<L, R> {

        public EntryPair(L left, R right) {
            super(left, right);
        }

        public L getKey() {
            return left;
        }

        public R getValue() {
            return right;
        }

        public R setValue(R value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (super.equals(o)) return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                return equals(Pair.make(e.getKey(), e.getValue()));
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (left == null ? 0 : left.hashCode()) ^ (right == null ? 0 : right.hashCode());
        }
    }
}
