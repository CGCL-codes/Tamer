package at.vartmp.jschnellen.core.util.confManager;

import java.rmi.RemoteException;
import org.apache.log4j.Level;
import org.dom4j.Attribute;
import org.dom4j.Element;
import at.vartmp.jschnellen.core.config.ServerConfManagerSlave;

/**
 * @author Lukas Tischler [tischler.lukas_AT_gmail.com]
 * 
 */
public class ConfManagerSlaveImpl implements ServerConfManagerSlave {

    private ConfigurationData configurationData;

    /**
	 * Instantiates a new conf manager slave impl.
	 * 
	 * @param configurationData
	 *            the configuration data
	 */
    ConfManagerSlaveImpl(ConfigurationData configurationData) {
        this.configurationData = configurationData;
    }

    public int getOwner() {
        return this.configurationData.owner;
    }

    public int getBlindFactor() throws RemoteException {
        Element e = this.configurationData.rootElement.element("blindfactor");
        String text = e.getText();
        return Integer.parseInt(text);
    }

    public int getPlayerCount(boolean aiPlayer) throws RemoteException {
        Attribute attr = null;
        if (aiPlayer) {
            attr = this.configurationData.rootElement.element("playerCount").attribute("aiPlayerCount");
        } else {
            attr = this.configurationData.rootElement.element("playerCount").attribute("count");
        }
        return Integer.parseInt(attr.getValue());
    }

    public String getPlayerName(int playerNumber) throws RemoteException {
        String toReturn = this.configurationData.getPlayerWithNumber(playerNumber).getText();
        return toReturn;
    }

    public int getWinnerPoint() throws RemoteException {
        String val = this.configurationData.rootElement.element("winnerPoints").getText();
        return Integer.parseInt(val);
    }

    public boolean isBlind() throws RemoteException {
        Attribute attr = this.configurationData.getRule("blindrule").attribute("enabled");
        return Boolean.parseBoolean(attr.getValue());
    }

    public boolean isNothingBlind() throws RemoteException {
        Attribute attr = this.configurationData.getRule("nothingblind").attribute("enabled");
        return Boolean.parseBoolean(attr.getValue());
    }

    public boolean isTacticalLoch() throws RemoteException {
        Attribute attr = this.configurationData.getRule("tacticalloch").attribute("enabled");
        return Boolean.parseBoolean(attr.getValue());
    }

    public boolean isLogToFile() {
        String isLogToFile = this.configurationData.rootElement.element("logger").attribute("logToFile").getValue();
        return Boolean.parseBoolean(isLogToFile);
    }

    public String getLogfilePath() throws RemoteException {
        return this.configurationData.rootElement.element("logger").attribute("logPath").getText();
    }

    public Level getLogLevel() {
        String logLevel = this.configurationData.rootElement.element("logger").attribute("logLevel").getValue();
        return Level.toLevel(logLevel.toUpperCase());
    }
}
