package com.go.tea.parsetree;

import com.go.tea.compiler.SourceInfo;
import com.go.tea.compiler.Type;

/******************************************************************************
 * Template is the main container node for Tea templates.
 *
 * @author Brian S O'Neill
 * @version
 * <!--$$Revision: 6 $-->, <!--$$JustDate:-->  9/07/00 <!-- $-->
 */
public class Template extends Node {

    private Name mName;

    private Variable[] mParams;

    private boolean mSubParam;

    private Statement mStatement;

    private Type mType;

    public Template(SourceInfo info, Name name, Variable[] params, boolean subParam, Statement statement) {
        super(info);
        mName = name;
        mParams = params;
        mSubParam = subParam;
        mStatement = statement;
    }

    public Object accept(NodeVisitor visitor) {
        return visitor.visit(this);
    }

    public Object clone() {
        Template t = (Template) super.clone();
        int length = mParams.length;
        Variable[] newParams = new Variable[length];
        for (int i = 0; i < length; i++) {
            newParams[i] = (Variable) mParams[i].clone();
        }
        t.mParams = newParams;
        t.mStatement = (Statement) mStatement.clone();
        return t;
    }

    public Name getName() {
        return mName;
    }

    public Variable[] getParams() {
        return mParams;
    }

    public boolean hasSubstitutionParam() {
        return mSubParam;
    }

    /**
     * Will likely return a StatementList or Block in order to hold many
     * statements.
     *
     * @see StatementList
     * @see Block
     */
    public Statement getStatement() {
        return mStatement;
    }

    public void setStatement(Statement stmt) {
        mStatement = stmt;
    }

    /**
     * The return type is set by a type checker. Returns null if this template
     * returns void, which is the default value.
     */
    public Type getReturnType() {
        return mType;
    }

    public void setReturnType(Type type) {
        mType = type;
    }
}
