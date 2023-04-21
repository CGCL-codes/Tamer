package org.dmagiserver.stats;

import java.util.List;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.AgiScript;

public interface IScriptStats {

    public String beginScript(AgiRequest request, AgiScript handlingScript, Thread executionThread, String serverDomain);

    public void endScript(AgiRequest request, AgiScript handlingScript);

    public List<String> getRunningScriptsIDs();

    public IScriptStat getScriptStat(String scriptID);

    public int getAllScriptsCount();

    public List<String> getScripID(int beginIndex, int endIndex);

    public List<String> getRunningScriptsIDsForDomain(String domain);
}
