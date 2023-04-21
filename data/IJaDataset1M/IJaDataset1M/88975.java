package com.thesett.aima.logic.fol.interpreter;

import java.util.Set;
import com.thesett.aima.logic.fol.Clause;
import com.thesett.aima.logic.fol.Sentence;
import com.thesett.aima.logic.fol.Variable;
import com.thesett.common.parsing.SourceCodeException;

/**
 * ResolutionInterpreter implements an interactive Prolog like interpreter, built on top of a {@link ResolutionEngine}.
 * It implements a top-level interpreter loop where queries or domain clauses may be entered. Queries are resolved
 * against the current domain using the resolver, after they have been compiled.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Parse text into first order logic clauses. <td> {@link com.thesett.aima.logic.fol.Parser}.
 * <tr><td> Compile clauses down to their compiled form. <td> {@link Compiler}.
 * <tr><td> Add facts to the current knowledge base. <td> {@link com.thesett.aima.logic.fol.Resolver}.
 * <tr><td> Resolve queries against the current knowledge base. <td> {@link com.thesett.aima.logic.fol.Resolver}.
 * <tr><td> Print the variable bindings resulting from resolution.
 *     <td> {@link com.thesett.aima.logic.fol.VariableAndFunctorInterner}.
 * </table></pre>
 *
 * @param  <S> The source clause type that the parser produces.
 * @param  <T> The compiled clause type that the compiler produces.
 *
 * @author Rupert Smith
 */
public class ResolutionInterpreter<S extends Clause, T, Q> {

    /** Holds the resolution engine that the interpreter loop runs on. */
    ResolutionEngine<S, T, Q> engine;

    /**
     * Builds an interactive logical resolution interpreter from a parser, interner, compiler and resolver, encapsulated
     * as a resolution engine.
     *
     * @param engine The resolution engine.
     */
    public ResolutionInterpreter(ResolutionEngine<S, T, Q> engine) {
        this.engine = engine;
    }

    /**
     * Implements the top-level interpreter loop. This will parse and evaluate sentences until it encounters an EOF at
     * which point the interpreter will terminate.
     *
     * @throws SourceCodeException If malformed code is encountered.
     */
    public void interpreterLoop() throws SourceCodeException {
        while (true) {
            Sentence<S> nextParsing = engine.parse();
            if (nextParsing == null) {
                break;
            }
            evaluate(nextParsing);
        }
    }

    /**
     * Evaluates a query against the resolver or adds a clause to the resolvers domain. In the case of queries, the
     * specified interner is used to recover textual names for the resulting variable bindings. The user is queried
     * through the parser to if more than one solution is required.
     *
     * @param  sentence The clausal sentence to run as a query or as a clause to add to the domain.
     *
     * @throws SourceCodeException If the query or domain clause fails to compile or link into the resolver.
     */
    private void evaluate(Sentence<S> sentence) throws SourceCodeException {
        engine.compile(sentence);
        if (sentence.getT().isQuery()) {
            boolean foundAtLeastOneSolution = false;
            for (Set<Variable> solution : engine) {
                foundAtLeastOneSolution = true;
                for (Variable nextVar : solution) {
                    String varName = engine.getVariableName(nextVar.getName());
                    System.out.println(varName + " = " + nextVar.getValue().toString(engine, true, false));
                }
                if (!engine.peekAndConsumeMore()) {
                    break;
                }
            }
            if (foundAtLeastOneSolution) {
                System.out.println("Yes.");
            } else {
                System.out.println("No.");
            }
        }
    }
}
