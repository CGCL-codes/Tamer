package de.searchworkorange.lib.statemachine;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public interface IMsgGenerator {

    public StringBuffer generateMsgStringBuffer(StringBuffer contentBuffer);

    public StringBuffer generateMsgStringBuffer(String content);

    public String generateMsg(StringBuffer contentBuffer);

    public String generateMsg(String content);
}
