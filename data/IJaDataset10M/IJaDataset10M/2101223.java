package tree;

import descriptor.AbstractDescriptor;
import symboltable.Symboltable;

public class StatementSequenceNode extends AbstractNode {

    private final AbstractNode statementSequence;

    private final AbstractNode statement;

    public StatementSequenceNode(AbstractNode statementSequence, AbstractNode statement) {
        this.statementSequence = statementSequence;
        this.statement = statement;
    }

    @Override
    public void print(int level) {
        System.out.println(toString(level));
        if (statementSequence != null) statementSequence.print(level + 1);
        if (statement != null) statement.print(level + 1);
    }

    @Override
    public AbstractDescriptor compile(Symboltable st) {
        statementSequence.compile(st);
        statement.compile(st);
        return null;
    }
}
