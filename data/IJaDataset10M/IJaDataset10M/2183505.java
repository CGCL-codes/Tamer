package lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.Stopwatch;

public class EachPractice {

    static void EachArray() {
        int[] a = new int[5000];
        for (int i = 0; i < a.length; i++) a[i] = i;
        Stopwatch sw = Stopwatch.begin();
        for (int i : a) {
            i = i * 2 - 1 + 4 / 3;
            if (i == 2000) break;
        }
        sw.stop();
        System.out.println(sw.getDuration());
        Stopwatch sw2 = Stopwatch.begin();
        Lang.each(a, new Each<Integer>() {

            @Override
            public void invoke(int i, Integer ele, int length) throws ExitLoop, LoopException {
                i = i * 2 - 1 + 4 / 3;
                if (i == 2000) throw new ExitLoop();
            }
        });
        sw2.stop();
        System.out.println(sw2.getDuration());
    }

    static void EachMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("1", "hello");
        map.put("2", "world!");
        Lang.each(map, new Each<String>() {

            @Override
            public void invoke(int i, String ele, int length) throws ExitLoop, LoopException {
                System.out.println(i + ":" + ele + "\t" + length);
            }
        });
    }

    static void EachCollection() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("hello");
        arrayList.add("world!");
        Lang.each(arrayList, new Each<String>() {

            @Override
            public void invoke(int i, String ele, int length) throws ExitLoop, LoopException {
                System.out.println(i + ":" + ele + "\t" + length);
            }
        });
    }

    public static void main(String[] args) {
        EachArray();
        System.out.println("-------------------");
        EachMap();
        System.out.println("-------------------");
        EachCollection();
        System.out.println(Lang.parseBoolean("off"));
    }
}
