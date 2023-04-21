package edu.ufpa.ppgcc.visualpseudo.syntactic.tests;

import junit.awtui.TestRunner;
import junit.framework.TestCase;
import edu.ufpa.ppgcc.visualpseudo.lexicon.TokenTable;
import edu.ufpa.ppgcc.visualpseudo.syntactic.constructions.ExpressionSyntax;

public class ExpressionTest extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(ExpressionTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        System.out.println("\nTeste de Express�o");
    }

    public final void test1Expression() {
        String test = "-1+4*3";
        ExpressionSyntax s = new ExpressionSyntax(new TokenTable(test)) {
        };
        assertTrue(s.execute());
        System.out.println(s.toString());
    }

    public final void test2Expression() {
        String test = "1+4^2*3";
        ExpressionSyntax s = new ExpressionSyntax(new TokenTable(test)) {
        };
        assertTrue(s.execute());
        System.out.println(s.toString());
    }

    public final void test3Expression() {
        String test = "1+teste*3";
        ExpressionSyntax s = new ExpressionSyntax(new TokenTable(test)) {
        };
        assertTrue(s.execute());
        System.out.println(s.toString());
    }

    public final void test4Expression() {
        String test = "3*(4-teste)";
        ExpressionSyntax s = new ExpressionSyntax(new TokenTable(test)) {
        };
        assertTrue(s.execute());
        System.out.println(s.toString());
    }

    public final void test5Expression() {
        String test = "3*a()-b[1]/c[1,2]";
        ExpressionSyntax s = new ExpressionSyntax(new TokenTable(test)) {
        };
        assertTrue(s.execute());
        System.out.println(s.toString());
    }

    public final void test6Expression() {
        String test = "a=3*2 ou 5*(4-1)>t e (1<=4 xou 1<>verdadeiro)";
        ExpressionSyntax s = new ExpressionSyntax(new TokenTable(test)) {
        };
        assertTrue(s.execute());
        System.out.println(s.toString());
    }

    public final void test7Expression() {
        String test = "false<>true";
        ExpressionSyntax s = new ExpressionSyntax(new TokenTable(test)) {
        };
        assertTrue(s.execute());
        System.out.println(s.toString());
    }

    public final void test8Expression() {
        String test = "";
        ExpressionSyntax s = new ExpressionSyntax(new TokenTable(test)) {
        };
        assertFalse(s.execute());
        System.out.println(s.toString());
    }
}
