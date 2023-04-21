package ch.ethz.mxquery.test;

import ch.ethz.mxquery.exceptions.TypeException;
import ch.ethz.mxquery.testsuite.XQueryTestBase;

public class TestFuncDef extends XQueryTestBase {

    public void test_parse_func_noargs_noreturn() throws Exception {
        String query = "declare namespace f=\"www.ethz.ch\"; declare function f:test1() {<b/>}; <a/>";
        prepareQuery(query, false, false, false, false, false);
    }

    ;

    public void test_parse_func_singlearg_notype_noreturn() throws Exception {
        String query = "declare function local:test2($a) {<b/>}; <a/>";
        prepareQuery(query, false, false, false, false, false);
    }

    ;

    public void test_parse_func_singlearg_simpletype_noreturn() throws Exception {
        String query = "declare function local:test3($a as xs:integer) {<b/>}; <a/>";
        prepareQuery(query, false, false, false, false, false);
    }

    ;

    public void test_parse_func_multiarg_simpletype_noreturn() throws Exception {
        String query = "declare function local:test4($a as xs:integer, $b as xs:boolean) {<b/>}; <a/>";
        prepareQuery(query, false, false, false, false, false);
    }

    ;

    public void test_parse_func_multiarg_exptype_noreturn() throws Exception {
        String query = "declare function local:test5($a as xs:integer, $b) {<b/>}; <a/>";
        prepareQuery(query, false, false, false, false, false);
    }

    ;

    public void test_parse_func_simplereturn() throws Exception {
        String query = "declare function local:test6($a as xs:integer) as xs:string{<b/>}; <a/>";
        prepareQuery(query, false, false, false, false, false);
    }

    ;

    public void test_parse_func_multiarg_exptype_expreturn() throws Exception {
        String query = "declare function local:test7($a as xs:integer, $b) as element()*{<b/>}; <a/>";
        prepareQuery(query, false, false, false, false, false);
    }

    ;

    public void test_parse_func_complexbody() throws Exception {
        String query = "declare function local:test8($a as xs:integer, $seq, $level) as xs:string {forseq $w in $seq tumbling window " + "start position $x when $seq[$x]/@level eq $level " + "end when newstart " + "return <bla/>}; <a/>";
        prepareQuery(query, false, false, false, true, false);
    }

    ;

    public void test_parse_func_1() throws Exception {
        String query = "declare function local:test5($a as xs:integer) {$a}; local:test5(12)";
        doQuery(prepareQuery(query, false, false, false, false, false));
        assertEquals("12", resultBuffer.toString().trim());
    }

    ;

    public void test_parse_func_2() throws Exception {
        String query = "declare function local:test5($a as xs:string) {$a}; local:test5(12)";
        String res = "no exception";
        try {
            doQuery(prepareQuery(query, false, false, false, false, false));
        } catch (TypeException e) {
            res = "exception";
        }
        assertEquals("exception", res);
    }

    ;

    public void test_parse_func_3() throws Exception {
        String query = "declare function local:test5($a as xs:integer) as xs:integer {$a}; local:test5(12)";
        doQuery(prepareQuery(query, false, false, false, false, false));
        assertEquals("12", resultBuffer.toString().trim());
    }

    ;

    public void test_parse_func_4() throws Exception {
        String query = "declare function local:test5($a as xs:integer) as xs:dayTimeDuration {$a}; local:test5(12)";
        String res = "no exception";
        try {
            doQuery(prepareQuery(query, false, false, false, false, false));
        } catch (TypeException e) {
            res = "exception";
        }
        assertEquals("exception", res);
    }

    ;
}
