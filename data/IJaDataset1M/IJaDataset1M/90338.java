package panda.query.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import panda.query.scan.Scanner;
import panda.query.scan.TableScanner;
import panda.query.scan.UpdatableScanner;
import panda.query.struct.Attribute;
import panda.query.tree.QueryTreeNode;
import panda.record.Schema;
import panda.record.TableToken;
import panda.transaction.Transaction;

/**
 * A component that sorts a table using merge sort algorihm
 * @author Tian Yuan
 */
public class MergeSorter {

    static int threadCnt = 3;

    /**
	 * 
	 * @param from
	 * @param to
	 * @param sch
	 * @return
	 */
    private static boolean moveTo(Scanner from, UpdatableScanner to, Schema sch) {
        Collection<Attribute> attrs = sch.getAllAttributes();
        to.insert();
        for (Attribute i : attrs) to.setValue(i, from.getValue(i));
        return from.next();
    }

    /**
	 * 
	 * @param t
	 * @param attrs
	 */
    public static Scanner sortTable(QueryTreeNode t, List<Attribute> attrs, Comparator cp, Transaction tx) {
        Scanner src = t.open();
        int fin = 3;
        Schema s = t.getSchema();
        ArrayList<UpdatableScanner> threads = new ArrayList<UpdatableScanner>(threadCnt);
        while (fin > 1) {
            if (!src.next()) return src;
            threads.clear();
            for (int i = 0; i < threadCnt; i++) {
                TempTable tt = new TempTable(s, tx);
                threads.add(tt.open());
            }
            int cnt = 0, c = 0;
            int i = 0;
            while (moveTo(src, threads.get(i), s)) {
                if (cp.compare(src, threads.get(i)) < 0) {
                    i = (i + 1) % threads.size();
                    cnt++;
                }
            }
            if (cnt == 0) {
                for (i = 1; i < threadCnt; i++) threads.get(i).close();
                threads.get(0).init();
                return threads.get(0);
            }
            src.close();
            src = new TempTable(s, tx).open();
            UpdatableScanner us = (UpdatableScanner) src;
            ArrayList<UpdatableScanner> unsorted = new ArrayList<UpdatableScanner>();
            ArrayList<UpdatableScanner> current = new ArrayList<UpdatableScanner>();
            fin = 0;
            for (i = 0; i < threadCnt; i++) {
                threads.get(i).init();
                if (threads.get(i).next()) unsorted.add(threads.get(i));
            }
            while (unsorted.size() > 0) {
                if (current.size() == 0) {
                    current.addAll(unsorted);
                    fin++;
                }
                UpdatableScanner least = current.get(0);
                for (i = 1; i < current.size(); i++) if (cp.compare(current.get(i), least) < 0) least = current.get(i);
                if (!moveTo(least, us, s)) {
                    current.remove(least);
                    unsorted.remove(least);
                } else if (cp.compare(least, us) < 0) current.remove(least);
            }
            for (i = 0; i < threadCnt; i++) threads.get(i).close();
            src.init();
        }
        return src;
    }
}
