package codesounding.plain;

public class AddSimpleWriter extends AddFileWriter {

    protected String getVarDeclaration() {
        return "/* VAR DECLARATION */";
    }

    protected String getStartBlock() {
        return "/* OPEN BLOCK */";
    }

    protected String getEndBlock() {
        return "/* CLOSE BLOCK */";
    }

    protected String getIfStatement() {
        return "/* NEAR IF*/";
    }

    protected String getForStatement() {
        return "/* NEAR FOR*/";
    }

    protected String getDoStatement() {
        return "/* NEAR DO*/";
    }

    protected String getWhileStatement() {
        return "/* NEAR WHILE*/";
    }

    protected String getReturnStatement() {
        return "/* NEAR RETURN */";
    }

    protected String getBreakStatement() {
        return "/* NEAR BREAK */";
    }

    protected String getContinueStatement() {
        return "/* NEAR CONTINUE */";
    }

    protected String getThrowStatement() {
        return "/* NEAR THROW */";
    }
}
