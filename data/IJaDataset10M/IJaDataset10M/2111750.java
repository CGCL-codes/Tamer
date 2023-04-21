package com.thesett.aima.logic.fol.l2;

import java.io.PrintStream;
import com.thesett.aima.logic.fol.Clause;
import com.thesett.aima.logic.fol.LogicCompiler;
import com.thesett.aima.logic.fol.Parser;
import com.thesett.aima.logic.fol.interpreter.ResolutionEngine;
import com.thesett.aima.logic.fol.interpreter.ResolutionInterpreter;
import com.thesett.aima.logic.fol.isoprologparser.SentenceParser;
import com.thesett.aima.logic.fol.isoprologparser.Token;
import com.thesett.aima.logic.fol.isoprologparser.TokenSource;
import com.thesett.common.util.doublemaps.SymbolTableImpl;

/**
 * PrologInterpreter builds an interactive resolving interpreter using the interpreted resolution engine
 * {@link L2ResolvingNativeMachine}.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Create an interpreter for L2.
 *     <td> {@link SentenceParser}, {@link L2Compiler}, {@link L2ResolvingNativeMachine}
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class L2NativeInterpreter {

    /**
     * Creates the interpreter and launches its top-level run loop.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            final L2ResolvingMachine machine = new L2ResolvingNativeMachine();
            Parser<Clause, Token> parser = new SentenceParser(machine);
            parser.setTokenSource(TokenSource.getTokenSourceForInputStream(System.in));
            LogicCompiler<Clause, L2CompiledClause, L2CompiledClause> compiler = new L2Compiler(new SymbolTableImpl<Integer, String, Object>(), machine);
            ResolutionEngine<Clause, L2CompiledClause, L2CompiledClause> engine = new ResolutionEngine<Clause, L2CompiledClause, L2CompiledClause>(parser, machine, compiler, machine) {

                public void reset() {
                    machine.reset();
                }
            };
            ResolutionInterpreter<Clause, L2CompiledClause, L2CompiledClause> interpreter = new ResolutionInterpreter<Clause, L2CompiledClause, L2CompiledClause>(engine);
            interpreter.interpreterLoop();
        } catch (Exception e) {
            e.printStackTrace(new PrintStream(System.err));
            System.exit(-1);
        }
    }
}
