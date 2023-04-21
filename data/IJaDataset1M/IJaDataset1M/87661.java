package edu.ufpa.ppgcc.visualpseudo.semantic.tests;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import junit.awtui.TestRunner;
import junit.framework.TestCase;
import edu.ufpa.ppgcc.visualpseudo.base.PrimitiveGrammar;
import edu.ufpa.ppgcc.visualpseudo.exceptions.GrammarException;
import edu.ufpa.ppgcc.visualpseudo.exceptions.ReadException;
import edu.ufpa.ppgcc.visualpseudo.exceptions.SemanticException;
import edu.ufpa.ppgcc.visualpseudo.exceptions.SyntaticException;
import edu.ufpa.ppgcc.visualpseudo.lexicon.TokenTable;
import edu.ufpa.ppgcc.visualpseudo.semantic.MainProgram;
import edu.ufpa.ppgcc.visualpseudo.semantic.evaluate.ProgramEval;
import edu.ufpa.ppgcc.visualpseudo.syntactic.constructions.ProgramSyntax;

public class ProgramTest extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(ProgramTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        System.out.println("\nTeste de Program");
    }

    public final void testProgram() throws SemanticException, ReadException, GrammarException, SyntaticException {
        ProgramSyntax syntax = new ProgramSyntax(new TokenTable("algoritmo \"teste\"\nconst A=1\nconst B=10\nvar i:inteiro\nfuncao obtemValor(i: inteiro): inteiro\ninicio\nescreva(\"Entrei aqui...\")\nretorne(i+1)\nfimfuncao\ninicio\npara i de A ate B faca\nescreval(\"valor de i: \"+i)\nfimpara\nobtemValor(i)\nfimalgoritmo"));
        syntax.execute();
        ProgramEval programEval = new ProgramEval(syntax.getResult());
        programEval.setLevel(PrimitiveGrammar.ANALISIS);
        programEval.setWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        assertTrue(programEval.execute());
        MainProgram main = programEval.getProgram();
        System.out.println(main);
        programEval.setWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        try {
            main.execute();
        } catch (SemanticException e) {
            e.printStackTrace();
        }
    }
}
