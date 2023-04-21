package com.judoscript.jamaica.parser;

import com.judoscript.jamaica.JavaClassCreator;
import java.util.ArrayList;

public abstract class ASTMacroBase extends ASTCodeWithText {

    protected ArrayList<Object> params = null;

    public ASTMacroBase(int id) {
        super(id);
    }

    public ASTMacroBase(JamaicaParser p, int id) {
        super(p, id);
    }

    public void addParam(Object value) {
        if (params == null) params = new ArrayList<Object>();
        params.add(value);
    }

    public ArrayList<JavaClassCreator.VarAccess> getAllVariables() {
        ArrayList<JavaClassCreator.VarAccess> ret = new ArrayList<JavaClassCreator.VarAccess>();
        if (params != null) {
            for (Object aParam : params) if (aParam instanceof JavaClassCreator.VarAccess) ret.add((JavaClassCreator.VarAccess) aParam);
        }
        return ret;
    }

    public Object[] getParams() {
        return params == null ? null : params.toArray();
    }

    public int getParamCount() {
        return params == null ? 0 : params.size();
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JamaicaParserVisitor visitor, Object data) throws Exception {
        return visitor.visit(this, data);
    }
}
