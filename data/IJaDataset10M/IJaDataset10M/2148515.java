package codesounding;

import java.io.*;
import java.util.Stack;

public abstract class AbstractMarker implements IMarker {

    private void writeMark(String msg) {
        print("{ //inizio blocco");
        print("try{");
        mark(msg);
        print("}catch(Exception e" + System.currentTimeMillis() + "){");
        print("	    //nothing todo");
        print("}");
        print("} //fine blocco");
    }

    protected void print(String str) {
        System.out.println(str);
    }

    protected abstract String getVarDeclaration();

    protected abstract String getStartBlock();

    protected abstract String getEndBlock();

    protected abstract String getIfStatement();

    protected abstract String getForStatement();

    protected abstract String getDoStatement();

    protected abstract String getWhileStatement();

    protected abstract String getReturnStatement();

    protected abstract String getBreakStatement();

    protected abstract String getContinueStatement();

    protected abstract String getThrowStatement();

    protected abstract void mark(String msg);

    public void varDeclaration() {
        writeMark(getVarDeclaration());
    }

    public void startBlock() {
        writeMark(getStartBlock());
    }

    public void endBlock() {
        writeMark(getEndBlock());
    }

    public void ifStatement() {
        writeMark(getIfStatement());
    }

    public void forStatement() {
        writeMark(getForStatement());
    }

    public void doStatement() {
        writeMark(getDoStatement());
    }

    public void whileStatement() {
        writeMark(getWhileStatement());
    }

    public void returnStatement() {
        writeMark(getReturnStatement());
    }

    public void breakStatement() {
        writeMark(getBreakStatement());
    }

    public void continueStatement() {
        writeMark(getContinueStatement());
    }

    public void throwStatement() {
        writeMark(getThrowStatement());
    }
}
