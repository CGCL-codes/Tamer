package org.sgmiller.quickstem.tests;

import org.sgmiller.quickstem.IsolationLevel;
import org.sgmiller.quickstem.Stem;
import org.sgmiller.quickstem.Transaction;

public class SubtransactionTest {

    /**
	 * @param args
	 * @throws InterruptedException 
	 */
    public static void main(String[] args) throws InterruptedException {
        final Stem<Integer> v = new Stem<Integer>(0);
        Thread t1 = new Thread() {

            public void run() {
                Transaction.begin();
                v.set(5);
                debug("i:" + v.get());
                Transaction.commit();
            }
        };
        Transaction.begin(IsolationLevel.SERIALIZABLE);
        t1.start();
        v.set(3);
        System.out.println(v.versions);
        debug("o:" + v.get());
        Transaction.beginSubTransaction();
        v.set(4);
        debug("o:" + v.get());
        Transaction.rollback();
        debug("o:" + v.get());
        System.out.flush();
        debug("o:" + v.get());
        System.out.println(v.versions);
        v.set(6);
        Transaction.rollback();
        System.out.println(v.versions);
        v.set(0);
        System.out.println(v.versions);
    }

    private static synchronized void debug(String string) {
        System.out.println(string);
        System.out.flush();
    }
}
