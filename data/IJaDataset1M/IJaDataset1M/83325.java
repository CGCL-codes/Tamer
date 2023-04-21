package org.jalgo.module.am0c0.model.c0.ast;

/**
 * AST part that represents the syntactic variable {@code Block}. A block
 * contains a {@code Declaration} and, optionally, a {@code StatementSequence}.
 * 
 * @author Martin Morgenstern
 */
public class Block extends C0AST {

    private final Declaration declaration;

    private final StatementSequence statementSequence;

    private final String codeText;

    /**
	 * Construct a new {@code Block} with given a given {@code Declaration} and
	 * an optional {@code StatementSequence}.
	 * 
	 * @param declaration
	 *            the declaration
	 * @param statementSequence
	 *            the statement sequence or {@code null}
	 * @throws NullPointerException
	 *             if declaration is {@code null}
	 */
    public Block(final Declaration declaration, final StatementSequence statementSequence) {
        if (null == declaration) {
            throw new NullPointerException();
        }
        this.declaration = declaration;
        this.statementSequence = statementSequence;
        codeText = initCodeText();
    }

    /**
	 * @return the declaration part of the block
	 */
    public Declaration getDeclaration() {
        return declaration;
    }

    /**
	 * @return the statement sequence or {@code null} if the block does not
	 *         contain a statement sequence
	 */
    public StatementSequence getStatementSequence() {
        return statementSequence;
    }

    @Override
    protected String getCodeTextInternal() {
        return codeText;
    }

    private String initCodeText() {
        assert declaration != null;
        final StringBuilder result = new StringBuilder();
        result.append("{\n");
        result.append(declaration.getCodeText());
        if (null != statementSequence) {
            result.append(statementSequence.getCodeText());
        }
        result.append("return 0;\n}");
        return result.toString();
    }
}
