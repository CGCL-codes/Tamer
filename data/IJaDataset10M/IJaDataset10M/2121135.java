package com.google.gwt.dev.generator.ast;

import java.util.List;

/**
 * A kind of {@link Statements} that represents a <code>for</code> loop.
 */
public class ForLoop implements Statements {

    private final StatementsList body;

    private final String initializer;

    private String label;

    private final String step;

    private final String test;

    /**
   * Creates a {@link ForLoop#ForLoop(String,String,String,Statements)} with a
   * null body.
   */
    public ForLoop(String initializer, String test, String step) {
        this(initializer, test, step, null);
    }

    /**
   * Constructs a new <code>ForLoop</code> {@link Node}.
   * 
   * @param initializer The textual initializer {@link Expression}.
   * @param test The textual test {@link Expression}.
   * @param step The textual step {@link Expression}. May be <code>null</code>.
   * @param statements The {@link Statements} for the body of the loop. May be
   *            <code>null</code>.
   */
    public ForLoop(String initializer, String test, String step, Statements statements) {
        this.initializer = initializer;
        this.test = test;
        this.step = step;
        this.body = new StatementsList();
        if (statements != null) {
            body.getStatements().add(statements);
        }
    }

    public List<Statements> getStatements() {
        return body.getStatements();
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String toCode() {
        String loop = "for ( " + initializer + "; " + test + "; " + step + " ) {\n" + body.toCode() + "\n" + "}\n";
        return label != null ? label + ": " + loop : loop;
    }
}
