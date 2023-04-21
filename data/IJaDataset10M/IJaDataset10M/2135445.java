package ast.stm;

import semantica.Semantica;

public class CompoundStatement extends Statement {

    private Statement statement;

    private CompoundStatement compoundStatement;

    public CompoundStatement(Statement statement, CompoundStatement compoundStatement) {
        super();
        this.statement = statement;
        this.compoundStatement = compoundStatement;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public CompoundStatement getCompoundStatement() {
        return compoundStatement;
    }

    public void setCompoundStatement(CompoundStatement compoundStatement) {
        this.compoundStatement = compoundStatement;
    }

    @Override
    public String toString() {
        return "CompoundStatement [compoundStatement=" + compoundStatement + ", statement=" + statement + "]";
    }

    public void semantica(Semantica s) {
        s.visit(this);
    }
}
