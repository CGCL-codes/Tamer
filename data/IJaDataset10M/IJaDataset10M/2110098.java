package com.agentfactory.afapl2.compiler.parser.token;

/**
 *
 * @author Administrator
 */
public class LessThanToken extends Token {

    /** Creates a new instance of StringToken */
    public LessThanToken() {
    }

    @Override
    public String getNodeType() {
        return "com.agentfactory.afapl2.compiler.parser.node.LessThanNode";
    }
}
