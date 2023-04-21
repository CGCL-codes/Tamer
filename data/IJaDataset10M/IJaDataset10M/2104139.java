package j2se.typestate.generalized.cmp;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

class TestKernelBenchmark1Small {

    Random r = new Random();

    int anumber = r.nextInt();

    public void test1() {
        Vector v1 = new Vector();
        v1.add("aha");
        Iterator it1 = v1.iterator();
        it1.next();
        if (anumber == 3) {
            Vector v2 = new Vector();
            Iterator it2 = v2.iterator();
            v2.add("aha");
            it2.next();
        }
        Vector v3 = new Vector();
        ;
        while (this.anumber == 17) {
            v3.add("aha");
            Iterator it3 = v3.iterator();
            while (it3.hasNext()) it3.next();
        }
    }

    public static void main(String[] args) {
        TestKernelBenchmark1Small k = new TestKernelBenchmark1Small();
        k.test1();
    }
}
