package net.sourceforge.eclipsetrader.trading;

import java.util.HashMap;
import java.util.Map;
import net.sourceforge.eclipsetrader.core.db.Security;
import net.sourceforge.eclipsetrader.trading.internal.wizards.IPluginParametersPage;

public abstract class TradingSystemPluginPreferencePage implements IPluginParametersPage {

    private Security security;

    private Map parameters = new HashMap();

    public TradingSystemPluginPreferencePage() {
    }

    public void init(Security security, Map params) {
        this.security = security;
        this.parameters = new HashMap(params);
    }

    public Security getSecurity() {
        return security;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map params) {
        this.parameters = new HashMap(params);
    }

    /**
     * Subclasses must implement this method to perform
     * any special finish processing for their page.
     */
    public void performOk() {
    }
}
