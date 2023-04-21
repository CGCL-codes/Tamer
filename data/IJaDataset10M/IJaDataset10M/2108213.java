package edu.kds.circuit.test;

import junit.framework.TestCase;
import edu.kds.circuit.misc.Range;
import edu.kds.circuit.misc.SeqRange;

public class SeqRangeTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SeqRangeTest.class);
    }

    public void testGetSize() {
        SeqRange r = new SeqRange("2,3,5,7");
        super.assertEquals(r.getSize(), 4);
        r.addClause("5");
        r.addClause("0");
        super.assertEquals(r.getSize(), 6);
        r = new SeqRange("0..9");
        super.assertEquals(r.getSize(), 10);
        r.addClause("15..11");
        super.assertEquals(r.getSize(), 15);
        r = new SeqRange("0..9/2");
        super.assertEquals(r.getSize(), 5);
        r.addClause("15..11/3");
        super.assertEquals(r.getSize(), 7);
        r = new SeqRange("0..9/2*4");
        super.assertEquals(r.getSize(), 5 * 4);
        r.addClause("15..11*2");
        super.assertEquals(r.getSize(), 5 * 4 + 2 * 5);
        r.addClause("0*32");
        super.assertEquals(r.getSize(), 5 * 4 + 2 * 5 + 32);
        r = new SeqRange("0..9/2, 0..9, 2,3,5,7");
        super.assertEquals(r.getSize(), 5 + 10 + 4);
    }

    public void testOverlapsRange() {
        Range r1 = new SeqRange("0,1,2");
        Range r2 = new SeqRange("3,4,5");
        super.assertFalse(r2.overlaps(r1));
        super.assertFalse(r1.overlaps(r2));
        r1 = new SeqRange("1,3,5");
        r2 = new SeqRange("2,4");
        super.assertFalse(r2.overlaps(r1));
        super.assertFalse(r1.overlaps(r2));
        r1 = new SeqRange("1,3,5");
        r2 = new SeqRange("2,5");
        super.assertTrue(r2.overlaps(r1));
        super.assertTrue(r1.overlaps(r2));
        r1 = new SeqRange("1,3,5");
        r2 = new SeqRange("1,3,5");
        super.assertTrue(r1.overlaps(r1));
        super.assertTrue(r2.overlaps(r2));
        r1 = new SeqRange("2..10");
        r2 = new SeqRange("11..20");
        super.assertFalse(r2.overlaps(r1));
        super.assertFalse(r1.overlaps(r2));
        r1 = new SeqRange("9..5");
        r2 = new SeqRange("15..10");
        super.assertFalse(r2.overlaps(r1));
        super.assertFalse(r1.overlaps(r2));
        r1 = new SeqRange("10..15");
        r2 = new SeqRange("20..15");
        super.assertTrue(r2.overlaps(r1));
        super.assertTrue(r1.overlaps(r2));
        r1 = new SeqRange("0..20");
        r2 = new SeqRange("2..5");
        super.assertTrue(r1.overlaps(r1));
        super.assertTrue(r2.overlaps(r2));
        r1 = new SeqRange("0..31/2");
        r2 = new SeqRange("1..31/2");
        super.assertFalse(r2.overlaps(r1));
        super.assertFalse(r1.overlaps(r2));
        r1 = new SeqRange("5..20/3");
        r2 = new SeqRange("30..7/3");
        super.assertFalse(r2.overlaps(r1));
        super.assertFalse(r1.overlaps(r2));
        r1 = new SeqRange("5..15/2");
        r2 = new SeqRange("12..20/3");
        super.assertTrue(r2.overlaps(r1));
        super.assertTrue(r1.overlaps(r2));
        r1 = new SeqRange("20..10/2");
        r2 = new SeqRange("30..4/2");
        super.assertTrue(r1.overlaps(r1));
        super.assertTrue(r2.overlaps(r2));
        r1 = new SeqRange("0..31/2*2");
        r2 = new SeqRange("1..31/2*2");
        super.assertFalse(r2.overlaps(r1));
        super.assertFalse(r1.overlaps(r2));
        r1 = new SeqRange("1*54,3*5,5");
        r2 = new SeqRange("2,5*2");
        super.assertTrue(r2.overlaps(r1));
        super.assertTrue(r1.overlaps(r2));
        r1 = new SeqRange("9..5*4");
        r2 = new SeqRange("15..10");
        super.assertFalse(r2.overlaps(r1));
        super.assertFalse(r1.overlaps(r2));
        r1 = new SeqRange("20..10/2");
        r2 = new SeqRange("30..4/2");
        super.assertTrue(r1.overlaps(r1));
        super.assertTrue(r2.overlaps(r2));
    }

    public void testOverlapsInt() {
    }

    public void testTrim() {
        Range r = new SeqRange("1,2,4,6,8,9");
        r.trim(5);
        super.assertEquals(r.getSize(), 5);
        r.trim(2);
        super.assertEquals(r.getSize(), 2);
        super.assertTrue(r.overlaps(1));
        super.assertTrue(r.overlaps(2));
        super.assertFalse(r.overlaps(4));
        super.assertFalse(r.overlaps(6));
        super.assertFalse(r.overlaps(8));
        super.assertFalse(r.overlaps(9));
        super.assertFalse(r.overlaps(3));
        r = new SeqRange("0,2, 4..6");
        r.trim(4);
        super.assertEquals(r.getSize(), 4);
        r.trim(1);
        super.assertEquals(r.getSize(), 1);
        super.assertTrue(r.overlaps(0));
        super.assertFalse(r.overlaps(2));
        super.assertFalse(r.overlaps(3));
        super.assertFalse(r.overlaps(4));
        super.assertFalse(r.overlaps(5));
        super.assertFalse(r.overlaps(6));
        super.assertFalse(r.overlaps(7));
        r = new SeqRange("0,2, 10..4/2");
        r.trim(4);
        super.assertEquals(r.getSize(), 4);
        super.assertTrue(r.overlaps(0));
        super.assertTrue(r.overlaps(2));
        super.assertTrue(r.overlaps(10));
        super.assertTrue(r.overlaps(8));
        super.assertFalse(r.overlaps(6));
        super.assertFalse(r.overlaps(4));
        super.assertFalse(r.overlaps(5));
        super.assertFalse(r.overlaps(9));
        r.trim(1);
        super.assertEquals(r.getSize(), 1);
        r = new SeqRange("0..4*3");
        r.trim(7);
        super.assertEquals(r.getSize(), 5);
        super.assertTrue(r.overlaps(0));
        super.assertTrue(r.overlaps(1));
        super.assertTrue(r.overlaps(2));
        super.assertTrue(r.overlaps(3));
        super.assertTrue(r.overlaps(4));
        super.assertFalse(r.overlaps(5));
        super.assertFalse(r.overlaps(6));
        r.trim(3);
        super.assertEquals(r.getSize(), 3);
        super.assertTrue(r.overlaps(0));
        super.assertTrue(r.overlaps(1));
        super.assertTrue(r.overlaps(2));
        super.assertFalse(r.overlaps(3));
        super.assertFalse(r.overlaps(4));
        super.assertFalse(r.overlaps(5));
    }

    public void testTruncate() {
        Range r = new SeqRange("14,2,4,10,5,6");
        super.assertEquals(r.getSize(), 6);
        r.truncate(5);
        super.assertEquals(r.getSize(), 3);
        super.assertTrue(r.overlaps(2));
        super.assertTrue(r.overlaps(4));
        super.assertTrue(r.overlaps(5));
        super.assertFalse(r.overlaps(10));
        super.assertFalse(r.overlaps(14));
        super.assertFalse(r.overlaps(3));
        super.assertFalse(r.overlaps(6));
        r = new SeqRange("2..13, 15..9, 20..22");
        super.assertEquals(r.getSize(), 12 + 7 + 3);
        r.truncate(10);
        super.assertEquals(r.getSize(), 9 + 2);
        super.assertTrue(r.overlaps(2));
        super.assertTrue(r.overlaps(10));
        super.assertTrue(r.overlaps(9));
        super.assertFalse(r.overlaps(1));
        super.assertFalse(r.overlaps(11));
        r = new SeqRange("2..13*2, 15..9*4, 20..22*1");
        super.assertEquals(r.getSize(), 12 * 2 + 7 * 4 + 3);
        r.truncate(10);
        super.assertEquals(r.getSize(), 9 * 2 + 2 * 4);
        super.assertTrue(r.overlaps(2));
        super.assertTrue(r.overlaps(10));
        super.assertTrue(r.overlaps(9));
        super.assertFalse(r.overlaps(1));
        super.assertFalse(r.overlaps(11));
    }

    public void testAddIndex() {
    }

    public void testRemIndex() {
    }
}
