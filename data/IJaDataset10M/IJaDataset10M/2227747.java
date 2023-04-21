package tests.it.crs4.seal.recab;

import it.crs4.seal.recab.MdOp;
import it.crs4.seal.common.FormatException;
import java.util.List;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

public class TestMdOp {

    @Test(expected = FormatException.class)
    public void testEmpty() {
        MdOp.scanMdTag("");
    }

    @Test
    public void testSimple() {
        List<MdOp> result = MdOp.scanMdTag("81");
        assertEquals(1, result.size());
        assertEquals(new MdOp(MdOp.Type.Match, 81), result.get(0));
    }

    @Test
    public void testMismatch() {
        List<MdOp> result = MdOp.scanMdTag("10A5");
        assertEquals(3, result.size());
        ArrayList<MdOp> answer = new ArrayList<MdOp>(3);
        answer.add(new MdOp(MdOp.Type.Match, 10));
        answer.add(new MdOp(MdOp.Type.Mismatch, 1));
        answer.add(new MdOp(MdOp.Type.Match, 5));
        assertEquals(answer, result);
    }

    @Test
    public void testDeletion() {
        List<MdOp> result = MdOp.scanMdTag("10^A5");
        assertEquals(3, result.size());
        ArrayList<MdOp> answer = new ArrayList<MdOp>(3);
        answer.add(new MdOp(MdOp.Type.Match, 10));
        answer.add(new MdOp(MdOp.Type.Delete, 1));
        answer.add(new MdOp(MdOp.Type.Match, 5));
        assertEquals(answer, result);
    }

    @Test
    public void testComplex() {
        List<MdOp> result = MdOp.scanMdTag("10A5^AC6");
        assertEquals(5, result.size());
        ArrayList<MdOp> answer = new ArrayList<MdOp>(5);
        answer.add(new MdOp(MdOp.Type.Match, 10));
        answer.add(new MdOp(MdOp.Type.Mismatch, 1));
        answer.add(new MdOp(MdOp.Type.Match, 5));
        answer.add(new MdOp(MdOp.Type.Delete, 2));
        answer.add(new MdOp(MdOp.Type.Match, 6));
        assertEquals(answer, result);
    }

    @Test
    public void testStartWith0() {
        List<MdOp> result = MdOp.scanMdTag("0A5");
        assertEquals(2, result.size());
        ArrayList<MdOp> answer = new ArrayList<MdOp>(2);
        answer.add(new MdOp(MdOp.Type.Mismatch, 1));
        answer.add(new MdOp(MdOp.Type.Match, 5));
        assertEquals(answer, result);
    }

    @Test
    public void testEndWith0() {
        List<MdOp> result = MdOp.scanMdTag("5A0");
        assertEquals(2, result.size());
        ArrayList<MdOp> answer = new ArrayList<MdOp>(2);
        answer.add(new MdOp(MdOp.Type.Match, 5));
        answer.add(new MdOp(MdOp.Type.Mismatch, 1));
        assertEquals(answer, result);
    }

    @Test(expected = FormatException.class)
    public void testErrorStartWithMismatch() {
        MdOp.scanMdTag("A5");
    }

    @Test(expected = FormatException.class)
    public void testErrorStartWithDelete() {
        MdOp.scanMdTag("^A5");
    }

    @Test(expected = FormatException.class)
    public void testErrorFinishWithMismatch() {
        MdOp.scanMdTag("5A");
    }
}
