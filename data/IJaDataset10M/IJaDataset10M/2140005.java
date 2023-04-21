package ch.ethz.mxquery.test.pattern;

import ch.ethz.mxquery.exceptions.StaticException;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.testsuite.XQueryTestBase;

public class PatternExample_TumblingInc extends XQueryTestBase {

    private static final String myPrefix = "PatternTests/Queries/Pattern/";

    public void test_selectPattern1() throws Exception {
        String query = "pattern tumbling incremental $a in ('A','A','B','B','C','A','A','A','B','B','B') with $p using" + "$p as xs:string+ pcur $t when $t eq \"A\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A</pattern><pattern>A A</pattern><pattern>A</pattern><pattern>A A</pattern><pattern>A A A</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern2() throws Exception {
        String query = "pattern tumbling incremental $a in ('A','A','B','C','C','D','A','B','C','A','B') with $p $q $r using" + "$p as item()* pcur $t when $t eq \"A\" " + "$q as item()* pcur $t when $t eq \"B\" " + "$r as item()* pcur $u when $u eq \"C\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A</pattern><pattern>A A</pattern><pattern>A A B</pattern><pattern>A A B C</pattern><pattern>A A B C C</pattern><pattern>A</pattern><pattern>A B</pattern><pattern>A B C</pattern><pattern>A</pattern><pattern>A B</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern3a() throws Exception {
        String query = "pattern tumbling incremental $a in ('A','B','C','C') with $p $q $r using" + "$p as item()+ pcur $t when $t eq \"A\" " + "$q as item() pcur $t when $t eq \"B\" " + "$r as item()+ pcur $u when $u eq \"C\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A B C</pattern><pattern>A B C C</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern3() throws Exception {
        String query = "pattern tumbling incremental $a in ('A','B','C','C','D','B','C','A','A','B','C','D') with $p $q $r using" + "$p as item()+ pcur $t when $t eq \"A\" " + "$q as item() pcur $t when $t eq \"B\" " + "$r as item()+ pcur $u when $u eq \"C\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A B C</pattern><pattern>A B C C</pattern><pattern>A A B C</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern4a() throws Exception {
        String query = "pattern tumbling incremental $a in ('A','C','C') with $p $q $r using" + "$p as item()* pcur $t when $t eq \"A\" " + "$q as item()+ pcur $t when $t eq \"B\" " + "$r as item()+ pcur $u when $u eq \"C\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("", resultBuffer.toString());
    }

    public void test_selectPattern4() throws Exception {
        String query = "pattern tumbling incremental $a in ('A','B','C','C','D','B','C','C','A','C','B','C','D') with $p $q $r using" + "$p as item()* pcur $t when $t eq \"A\" " + "$q as item()+ pcur $t when $t eq \"B\" " + "$r as item()+ pcur $u when $u eq \"C\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A B C</pattern><pattern>A B C C</pattern><pattern>B C</pattern><pattern>B C C</pattern><pattern>B C</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern5() throws Exception {
        String query = "pattern tumbling incremental $a in ('A','B','C','C','D','B','C','C','D','C','B','B','C','D') with $p $q $r using" + "$p as item()* pcur $t when $t eq \"A\" " + "$q as item()* pcur $t when $t eq \"B\" " + "$r as item()+ pcur $u when $u eq \"C\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A B C</pattern><pattern>A B C C</pattern><pattern>B C</pattern><pattern>B C C</pattern><pattern>C</pattern><pattern>B B C</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern6() throws Exception {
        String query = "pattern tumbling incremental $a in ('A','B','C','C','D','B','C','C','D','C','B','B','C','D') with $p $q $r using" + "$p as item()* pcur $t when $t eq \"A\" " + "$q as item()* pcur $t when $t eq \"B\" " + "$r as item()+ pcur $u when $u eq \"C\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A B C</pattern><pattern>A B C C</pattern><pattern>B C</pattern><pattern>B C C</pattern><pattern>C</pattern><pattern>B B C</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern7() throws Exception {
        String query = "pattern tumbling incremental $p in (1,2,3,4,5) with $a using" + "$a as item()+ pprev $b, pcur $c when $b < $c " + "return <pattern>{$p}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>2</pattern><pattern>2 3</pattern><pattern>2 3 4</pattern><pattern>2 3 4 5</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern8() throws Exception {
        String query = "pattern tumbling incremental $p in (1,2,3,4,5,4,1,10) with $a $d using" + "$a as item()+ pprev $b, pcur $c when $b < $c" + "$d as item()+ pprev $t, pcur $u when $t > $u " + "return <pattern>{$p}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>2 3 4 5 4</pattern><pattern>2 3 4 5 4 1</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern9() throws Exception {
        String query = "pattern tumbling incremental $p in (2,2,2,3,4,10,6,3,3,3) with $a $d using" + "$a as item()+ pprev $b, pcur $c when $b < $c" + "$d as item()+ pprev $t, pcur $u when $t > $u " + "return <pattern>{$p}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>3 4 10 6</pattern><pattern>3 4 10 6 3</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern10() throws Exception {
        String query = "pattern tumbling incremental $a in ('A','A','A','B','B','B','B','A','B','C') with $p $q using" + "$p as item()+ all $t when (every $y in $t satisfies $y eq \"A\") " + "$q as item()+ all $u when (every $f in $u satisfies $f eq \"B\") " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A A A B</pattern><pattern>A A A B B</pattern><pattern>A A A B B B</pattern><pattern>A A A B B B B</pattern><pattern>A B</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern11() throws Exception {
        String query = "pattern tumbling incremental $a in ('A','B','A') with $p $q using" + "$p as item()* all $t when (every $y in $t satisfies $y eq \"A\") " + "$q as item()* all $u when (every $f in $u satisfies $f eq \"B\") " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A</pattern><pattern>A B</pattern><pattern>A</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern12() throws Exception {
        String query = "pattern tumbling incremental $a in ('A','A','B','B','A','A') with $p $q using" + "$p as item()* all $t when (every $y in $t satisfies $y eq \"A\") " + "$q as item()* all $u when (every $f in $u satisfies $f eq \"B\") " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A</pattern><pattern>A A</pattern><pattern>A A B</pattern><pattern>A A B B</pattern><pattern>A</pattern><pattern>A A</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern13() throws Exception {
        String query = "pattern sliding allmatch $p in ('C','C','A') with $a using" + "$a as item()+ pcur $p1 when fn:true() " + "return <pattern>{$p}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>C</pattern><pattern>C C</pattern><pattern>C C A</pattern><pattern>C</pattern><pattern>C A</pattern><pattern>A</pattern>", resultBuffer.toString());
    }

    public static void main(String args[]) {
        PatternExample_TumblingInc pex = new PatternExample_TumblingInc();
        try {
            pex.test_selectPattern13();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
