package com.gorillalogic.dal.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.test.*;

public class EqTest extends CommonTest {

    private EqTest(Tester tester) throws AccessException {
        super(tester);
        triangularModel();
    }

    private void testExprEq(Table table, String s1, String s2) throws AccessException {
        Expr e1 = table.precompile(s1);
        Expr e2 = table.precompile(s2);
        testExprEq(table, e1, e2);
    }

    private void testExprEq(Table table, Expr e1, Expr e2) throws AccessException {
        tester.testEq("Expr Eq: ", new ExprTestWrapper(e1), new ExprTestWrapper(e2));
    }

    private void testResultsEq(Table table, String s1, String s2) throws AccessException {
        Expr e1 = table.precompile(s1);
        Expr e2 = table.precompile(s2);
        testResultsEq(table, e1, e2);
    }

    private void testResultsEq(Table table, Expr e1, Expr e2) throws AccessException {
        Table t1 = e1 == null ? null : e1.computeTable(table);
        Table t2 = e2 == null ? null : e2.computeTable(table);
        String path = t1 == null ? "null" : t1.path();
        tester.testEq("Table Eq: (" + path + ")", t1, t2);
    }

    private void testExprNeqResultsEq(Table table, String s1, String s2) throws AccessException {
        Expr e1 = table.precompile(s1);
        Expr e2 = table.precompile(s2);
        testExprNeq(table, e1, e2);
        testResultsEq(table, e1, e2);
    }

    private void testExprNeq(Table table, String s1, String s2) throws AccessException {
        Expr e1 = table.precompile(s1);
        Expr e2 = table.precompile(s2);
        tester.testNeq("Expr NOT Eq", e1, e2);
    }

    private void testExprNeq(Table table, Expr e1, Expr e2) throws AccessException {
        tester.testNeq("Expr NOT Eq", e1, e2);
    }

    private void testEq(Table table, String s) throws AccessException {
        String s2 = s == null ? null : new String(s);
        testEq(table, s, s2);
    }

    private void testEq(Table table, String s1, String s2) throws AccessException {
        Expr e1 = table.precompile(s1);
        Expr e2 = table.precompile(s2);
        testExprEq(table, e1, e2);
        testResultsEq(table, e1, e2);
    }

    private void testExpr(Table table, String s1, String s2, String s3) throws AccessException {
        testEq(table, s1);
        testEq(table, s2);
        testEq(table, s3);
        testExprNeq(table, s1, s2);
        testExprNeq(table, s1, s3);
        testExprNeq(table, s2, s3);
    }

    private void testExpr() throws AccessException {
        Table north = getExtent("North");
        Table east = getExtent("East");
        Table west = getExtent("West");
        Table south = getExtent("South");
        testExpr(north, "1", "2", "3");
        testExpr(north, "6.3", "6.0", "0.3");
        testExpr(north, "true", "false", "0");
        testExpr(north, "\"blue\"", null, "\"red\"");
        testExpr(north, "ni", "nf", "nb");
        testExpr(north, "ni+1", "nf", "ni-1");
        testExpr(north, "ni+ni", "ni-ni", "\"ni+ni\"");
        testExpr(north, "west", "east", "0");
        testExpr(north, "west.east", "east.west", "east");
        testExpr(north, "west.east.north", "ni", "east.west.north");
        testExpr(north, "west.east.north.west", "0", "east.west");
        testExpr(north, "west.wi+1", "east.ei+1", "1+1");
        testExpr(north, "west.wi+west.wi", "west.wi+west.wi+1", "west.wi+west.wf");
        testExpr(north, "west.wi+east.ei", "west.wf+east.ei", "0");
        testExpr(north, "west.wf+east.ef-1", "west.wf-1", "east.ef+east.ef-1");
        testExpr(north, "west.east.ei-1+4+east.ef", "west.east.ei-1+400+east.ef", "west.east.eb");
        testExpr(north, "west.east.west.wb", "west.east.west.east.eb", "west.wb");
        testExpr(north, "west.east.west.wb or true", "true or true", "0");
        testExpr(north, "west.east.ei = nf", "west.east.ei > nf", "west.east.ei < nf");
        testExpr(north, "west.east.ei < nf", "west.east.ei <= nf", "not west.east.ei < nf");
        testExpr(north, "west.east.ei < nf/22", "west.east.ei <= nf/22", "west.east.ei <= nf/100");
        testExpr(north, "west.east.ei > nf*22 or east.eb", "west.east.ei > nf+22 or east.eb", "west.east.ei > nf*22 and east.eb");
        testExpr(north, "west.east.ei > nf*22/ni and east.ei > east.west.wi", "west.east.ei > nf*22/ni and east.ef > east.west.wi", "west.east.ei = nf*22/ni and east.ei > east.west.wi");
        testExpr(north, "not nb and east.eb or 999 < east.ef", "not nb and east.eb and 999 < east.ef", "not nb and east.eb or 999 = east.ef");
        testExpr(north, "/North", "/East", "/South");
        Txn.mgr.begin();
        Table.Row s1 = south.extend(true).addRow();
        s1.setString("name", "s1");
        Txn.mgr.commit();
        testExprNeqResultsEq(north, "/South@s1", "/North@s1");
        testExprNeqResultsEq(north, "/South[0]", "/North[0]");
        testExprNeqResultsEq(north, "/North[0]:South", "/North[0]");
    }

    public static void testExpr(Tester tester) throws AccessException {
        EqTest theTest = new EqTest(tester);
        theTest.testExpr();
    }

    public static void main(String[] argv) {
        Tester.run(EqTest.class);
    }
}
