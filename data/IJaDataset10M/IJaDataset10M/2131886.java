package org.norecess.nolatte.ast;

import org.norecess.nolatte.ast.visitors.StatementVisitor;

public class WhileLoop implements IWhileLoop {

    private static final long serialVersionUID = -2673694655056029813L;

    private final Statement myTest;

    private final IGroupOfStatements myBody;

    public WhileLoop(Statement test, IGroupOfStatements body) {
        myTest = test;
        myBody = body;
    }

    @Override
    public boolean equals(Object other) {
        return (other != null) && getClass().equals(other.getClass()) && equals((WhileLoop) other);
    }

    public boolean equals(WhileLoop other) {
        return myTest.equals(other.myTest) && myBody.equals(other.myBody);
    }

    @Override
    public int hashCode() {
        return myTest.hashCode() + myBody.hashCode() * 32;
    }

    public IGroupOfStatements getBody() {
        return myBody;
    }

    public Statement getTest() {
        return myTest;
    }

    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitWhileLoop(this);
    }

    public boolean isTrue() {
        return true;
    }

    public int getLine() {
        return 0;
    }

    @Override
    public String toString() {
        return "while: " + getTest() + " " + getBody();
    }
}
