package com.chrisgammage.wjt.lua_compiler.statements;

import com.chrisgammage.wjt.lua_compiler.LuaCode;
import com.sun.source.tree.ReturnTree;

public class LuaReturn extends LuaStatement {

    LuaExpression expression;

    public LuaReturn(LuaCode parent, ReturnTree returnTree) {
        super(parent);
        expression = LuaExpression.processExpression(parent, returnTree.getExpression());
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("return ");
        ret.append(expression.toString());
        return ret.toString();
    }
}
