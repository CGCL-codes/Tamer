package net.oesterholt.jndbm.spikes;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;
import net.oesterholt.jndbm.NDbm;

public class dbmspike {

    public static void main(String[] args) {
        try {
            NDbm.setGlobalUnsafe();
            File f = new File("test");
            int testnr = 1;
            System.out.println("test" + testnr++);
            NDbm db = new NDbm(f, 10, false);
            db.putStr("test", "Hallo allemaal");
            System.out.println("test=" + db.getStr("test"));
            db.putInt("int", 100);
            System.out.println("int=" + db.getInt("int"));
            Vector<String> v = new Vector<String>();
            v.add("Hi");
            v.add("Well");
            db.putVectorOfString("vector", v);
            System.out.println("vector=" + db.getVectorOfString("vector"));
            db.close();
            System.out.println("test" + testnr++);
            db = new NDbm(f, true);
            System.out.println("test=" + db.getStr("test"));
            System.out.println("int=" + db.getInt("int"));
            System.out.println("vector=" + db.getVectorOfString("vector"));
            db.close();
            System.out.println("test" + testnr++);
            db = new NDbm(f, false);
            System.out.println("int=" + db.getInt("int"));
            db.putStr("test", "Nou ja zeg, wat nu dan");
            System.out.println("int=" + db.getInt("int"));
            db.close();
            System.out.println("test" + testnr++);
            db = new NDbm(f, false);
            System.out.println("int=" + db.getInt("int"));
            db.putStr("test", "Nou ja zeg, wat nu dan");
            System.out.println("int=" + db.getInt("int"));
            db.close();
            System.out.println("test" + testnr++);
            db = new NDbm(f, false);
            System.out.println("int=" + db.getInt("int"));
            db.putStr("test", "Klein");
            db.putInt("new", 80822);
            db.putInt("min", -232232);
            System.out.println("int=" + db.getInt("int"));
            db.close();
            System.out.println("test" + testnr++);
            db = new NDbm(f, true);
            System.out.println("test=" + db.getStr("test"));
            System.out.println("new=" + db.getInt("new"));
            System.out.println("min=" + db.getInt("min"));
            System.out.println("vector=" + db.getVectorOfString("vector"));
            System.out.println("int=" + db.getInt("int"));
            db.close();
            System.out.println("test" + testnr++);
            db = new NDbm(f, true);
            Iterator<String> it = db.iterator();
            while (it.hasNext()) {
                System.out.println("key: " + it.next());
            }
            db.close();
            System.out.println("test" + testnr++);
            db = new NDbm(f, false);
            Vector<String> k = db.keys();
            db.remove("new");
            Vector<String> k1 = db.keys();
            System.out.println("k  contains 'new'? :" + k.contains("new"));
            System.out.println("k1 contains 'new'? :" + k1.contains("new"));
            System.out.println("result of 'new'?   :" + db.getStr("new"));
            db.close();
            System.out.println("test" + testnr++);
            db = new NDbm(f, true);
            it = db.iterator();
            while (it.hasNext()) {
                String key = it.next();
                System.out.println(key + "=" + db.getObject(key));
            }
            System.out.println("test" + testnr++);
            db = new NDbm(f, false);
            db.putObject("boolean", true);
            db.putObject("tryit", 98213);
            it = db.iterator();
            while (it.hasNext()) {
                String key = it.next();
                System.out.println(key + "=" + db.getObject(key));
            }
            Runtime.getRuntime().gc();
            System.out.println("test" + testnr++);
            db = new NDbm(f, false);
            int i;
            for (i = 0; i < 100; i++) {
                System.out.print(i + " ");
                db.putInt("key:" + i, i * 4);
            }
            it = db.iterator();
            while (it.hasNext()) {
                System.out.print(it.next() + " ");
            }
            System.out.println();
            db.close();
            db = new NDbm(f, false);
            int factor = 2;
            for (i = 0; i < 1000; i++) {
                System.out.print(i + ":" + i * factor + " ");
                if (i % 10 == 0) {
                    System.out.println();
                }
                db.putInt("key:" + i, i * factor);
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.exit(0);
    }
}
