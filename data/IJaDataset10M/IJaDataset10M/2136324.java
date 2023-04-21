package com.beanstalktech.common.script;

/**
 * Represents a return command.
 */
public class ReturnScriptToken extends CommandScriptToken {

    public ReturnScriptToken(String properties) {
        m_tokenType = RETURN_TOKEN;
        m_tokenArgs.addElement("return");
        m_tokenArgs.addElement(properties);
    }
}
