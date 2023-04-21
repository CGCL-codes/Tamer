package edu.ufpa.ppgcc.visualpseudo.syntactic.tests;

import junit.awtui.TestRunner;
import junit.framework.TestCase;
import edu.ufpa.ppgcc.visualpseudo.lexicon.TokenTable;
import edu.ufpa.ppgcc.visualpseudo.syntactic.constructions.AssigmentSyntax;

public class AssigmentTest extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(AssigmentTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        System.out.println("\nTeste de Atribui��o");
    }

    public final void testNormalArithAttrib() {
        String test = "a<-3%2+(1+1.2/2)=1";
        AssigmentSyntax attr = new AssigmentSyntax(new TokenTable(test));
        assertTrue(attr.execute());
        System.out.println(attr.toString());
    }

    public final void testNormalLogicAttrib() {
        String test = "teste <- (teste=1 e verdadeiro) ou falso";
        AssigmentSyntax attr = new AssigmentSyntax(new TokenTable(test));
        assertTrue(attr.execute());
        System.out.println(attr.toString());
    }

    public final void testVectorArithAttrib() {
        String test = "t[1,s]<-6*(4+a)";
        AssigmentSyntax attr = new AssigmentSyntax(new TokenTable(test));
        assertTrue(attr.execute());
        System.out.println(attr.toString());
    }

    public final void testVectorLogicAttrib() {
        String test = "s[a]<-teste e 4=b ou (g=ds e verdadeiro)";
        AssigmentSyntax attr = new AssigmentSyntax(new TokenTable(test));
        assertTrue(attr.execute());
        System.out.println(attr.toString());
    }

    public final void testMatrix1Attrib() {
        String test = "a[1,22,s,d2]<-3%2+\"mais\"+(1-1.2/2)=1 ou falso";
        AssigmentSyntax attr = new AssigmentSyntax(new TokenTable(test));
        assertTrue(attr.execute());
        System.out.println(attr.toString());
    }

    public final void testMatrix2Attrib() {
        String test = "vet[1] <- vet[1] + (a * 3)";
        AssigmentSyntax attr = new AssigmentSyntax(new TokenTable(test));
        assertTrue(attr.execute());
        System.out.println(attr.toString());
    }
}
