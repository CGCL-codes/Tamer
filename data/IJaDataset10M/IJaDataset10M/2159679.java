package de.denkselbst.sentrick.eval;

import static org.junit.Assert.*;
import org.junit.Test;

public class ContingencyTableTest {

    @Test
    public void emptyTable() {
        ContingencyTable ct = new ContingencyTable();
        assertEquals("Wrong initial value.", 0, ct.getTruePositives());
        assertEquals("Wrong initial value.", 0, ct.getFalsePositives());
        assertEquals("Wrong initial value.", 0, ct.getFalseNegatives());
        assertEquals("Wrong initial value.", 0, ct.getTrueNegatives());
        assertEquals("Wrong value.", 0.0, ct.getPrecision(), 0.001);
        assertEquals("Wrong value.", 0.0, ct.getRecall(), 0.001);
        assertEquals("Wrong value.", 0.0, ct.getF1(), 0.001);
    }

    @Test
    public void count() {
        ContingencyTable ct = new ContingencyTable();
        for (int tp = 0; tp < 5; tp++) ct.countAP(true, true);
        for (int fp = 0; fp < 3; fp++) ct.countAP(false, true);
        for (int fn = 0; fn < 7; fn++) ct.countAP(true, false);
        for (int tn = 0; tn < 20; tn++) ct.countAP(false, false);
        assertEquals("Wrong value.", 5, ct.getTruePositives());
        assertEquals("Wrong value.", 3, ct.getFalsePositives());
        assertEquals("Wrong value.", 7, ct.getFalseNegatives());
        assertEquals("Wrong value.", 20, ct.getTrueNegatives());
        assertEquals("Wrong value.", 0.625, ct.getPrecision(), 0.001);
        assertEquals("Wrong value.", 0.416, ct.getRecall(), 0.001);
        assertEquals("Wrong value.", 0.5, ct.getF1(), 0.001);
        assertEquals("Wrong String representation.", "    |    a     |    �a    |\n" + "----+----------+----------+\n" + "  p |        5 |        3 |     precision: 0,6250\n" + "----+----------+----------+     recall:    0,4167\n" + " �p |        7 |       20 |     f1:        0,5000\n" + "----+----------+----------+\n", ct.toString());
    }

    @Test
    public void pad8() {
        ContingencyTable ct = new ContingencyTable();
        assertEquals("", "       7", ct.pad8(7));
        assertEquals("", " 1234567", ct.pad8(1234567));
        assertEquals("", "12345678", ct.pad8(12345678));
        assertEquals("", "!123456!", ct.pad8(1234567890));
    }

    @Test
    public void add() {
        ContingencyTable akk = new ContingencyTable();
        ContingencyTable ct1 = mkContingencyTable(5, 3, 7, 20);
        akk.add(ct1);
        assertEquals("Wrong value.", 5, akk.getTruePositives());
        assertEquals("Wrong value.", 3, akk.getFalsePositives());
        assertEquals("Wrong value.", 7, akk.getFalseNegatives());
        assertEquals("Wrong value.", 20, akk.getTrueNegatives());
        ContingencyTable ct2 = mkContingencyTable(5, 4, 3, 2);
        akk.add(ct2);
        assertEquals("Wrong value.", 10, akk.getTruePositives());
        assertEquals("Wrong value.", 7, akk.getFalsePositives());
        assertEquals("Wrong value.", 10, akk.getFalseNegatives());
        assertEquals("Wrong value.", 22, akk.getTrueNegatives());
    }

    private ContingencyTable mkContingencyTable(int tp, int fp, int fn, int tn) {
        ContingencyTable ct = new ContingencyTable();
        for (int i = 0; i < tp; i++) ct.countAP(true, true);
        for (int i = 0; i < fp; i++) ct.countAP(false, true);
        for (int i = 0; i < fn; i++) ct.countAP(true, false);
        for (int i = 0; i < tn; i++) ct.countAP(false, false);
        return ct;
    }
}
