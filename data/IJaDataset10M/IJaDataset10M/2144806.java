package ch.ethz.mxquery.test.pattern;

import ch.ethz.mxquery.exceptions.StaticException;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.testsuite.XQueryTestBase;

public class PatternExample_SlidingAllMatch extends XQueryTestBase {

    private static final String myPrefix = "PatternTests/Queries/Pattern/";

    public void test_selectPattern1() throws Exception {
        String query = "pattern sliding allmatch $a in ('A','B','B','A','A') with $p $q using" + "$p as item()* pcur $t when $t eq \"A\" " + "$q as item()+ pcur $u when $u eq \"B\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A B</pattern><pattern>A B B</pattern><pattern>B</pattern><pattern>B B</pattern><pattern>B</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern2() throws Exception {
        String query = "pattern sliding allmatch $a in ('A','B','C','D','B','B','C') with $p $q $r using" + "$p as xs:string* pcur $t when $t eq \"A\" " + "$q as xs:string+ pcur $u when $u eq \"B\" " + "$r as xs:string* pcur $v when $v eq \"C\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A B</pattern><pattern>A B C</pattern><pattern>B</pattern><pattern>B C</pattern><pattern>B</pattern><pattern>B B</pattern><pattern>B B C</pattern><pattern>B</pattern><pattern>B C</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern3() throws Exception {
        String query = "pattern sliding allmatch  $a in ('A','B','C','C','B','A','C') with $p $q $r using" + "$p as xs:string* pcur $t when $t eq \"A\" " + "$q as xs:string* pcur $u when $u eq \"B\" " + "$r as xs:string* pcur $v when $v eq \"C\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A</pattern><pattern>A B</pattern><pattern>A B C</pattern><pattern>A B C C</pattern><pattern>B</pattern><pattern>B C</pattern><pattern>B C C</pattern><pattern>C</pattern><pattern>C C</pattern><pattern>C</pattern><pattern>B</pattern><pattern>A</pattern><pattern>A C</pattern><pattern>C</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern4() throws Exception {
        String query = "pattern sliding allmatch  $a in ('A','B','C','D','C','F','C','B','B','C','A','D','B','C') with $p $q $r using" + "$p as xs:string* pcur $t when $t eq \"A\" " + "$q as xs:string+ pcur $u when $u eq \"B\" " + "$r as xs:string+ pcur $v when $v eq \"C\" " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>A B C</pattern><pattern>B C</pattern><pattern>B B C</pattern><pattern>B C</pattern><pattern>B C</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern5() throws Exception {
        String query = "pattern sliding allmatch  $a in (1,2,3,4,4,4,4,5,6,7,8) with $p using" + "$p as xs:integer+ pcur $t, pprev $u when $u < $t " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>2</pattern><pattern>2 3</pattern><pattern>2 3 4</pattern><pattern>3</pattern><pattern>3 4</pattern><pattern>4</pattern><pattern>5</pattern><pattern>5 6</pattern><pattern>5 6 7</pattern><pattern>5 6 7 8</pattern><pattern>6</pattern><pattern>6 7</pattern><pattern>6 7 8</pattern><pattern>7</pattern><pattern>7 8</pattern><pattern>8</pattern>", resultBuffer.toString());
    }

    public void test_selectPattern6() throws Exception {
        String query = "pattern sliding allmatch $a in (1,2,3,4) with $p using" + "$p as xs:integer+ pcur $t, pprev $u when $u < $t " + "return <pattern>{$a}</pattern>";
        boolean result = false;
        try {
            PreparedStatement expr = prepareQuery(query, false, false, false, true, false);
            doQuery(expr);
        } catch (StaticException err) {
            err.printStackTrace();
        }
        assertEquals("<pattern>2</pattern><pattern>2 3</pattern><pattern>2 3 4</pattern><pattern>3</pattern><pattern>3 4</pattern><pattern>4</pattern>", resultBuffer.toString());
    }

    public static void main(String args[]) {
        PatternExample_SlidingAllMatch pex = new PatternExample_SlidingAllMatch();
        try {
            pex.test_selectPattern6();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
