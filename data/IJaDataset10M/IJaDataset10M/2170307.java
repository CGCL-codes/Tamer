package gnu.testlet.java2.util.concurrent.CopyOnWriteArrayList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;

/**
 * @author Mario Torre <neugens@limasoftware.net>
 */
public class RemoveTest implements Testlet {

    public void test(TestHarness harness) {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<Integer>();
        List<Integer> data = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) data.add(i);
        list.addAll(data);
        harness.check(list.size() == 10);
        Integer el = list.remove(5);
        harness.check(el.intValue() == 5);
        harness.check(list.size() == 9);
        harness.check(list.add(el));
        harness.check(list.size() == 10);
        harness.check(list.remove(el));
        harness.check(list.size() == 9);
        int[] expected = { 0, 1, 2, 3, 4, 6, 7, 8, 9 };
        int i = 0;
        for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext(); ) harness.check(iterator.next().intValue() == expected[i++]);
    }
}
