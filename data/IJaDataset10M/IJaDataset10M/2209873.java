package org.proclos.etlcore.config.export;

import org.jdom.Element;
import org.proclos.etlcore.config.BasicConfigurator;
import org.proclos.etlcore.config.ConfigurationException;
import org.proclos.etlcore.source.ISource;
import org.proclos.etlcore.connection.IConnection;
import org.proclos.etlcore.context.Context;
import org.proclos.etlcore.component.IManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.proclos.etlcore.project.ConfigManager;
import org.proclos.etlcore.export.IExport.Modes;

/**
 * 
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public class ExportConfigurator extends BasicConfigurator {

    private static final Log log = LogFactory.getLog(ExportConfigurator.class);

    public ExportConfigurator() {
    }

    public ISource getSource() {
        ISource viewSource = (ISource) ConfigManager.getInstance().getComponent(getLocator().clone().add(getName()).add(IManager.Sources).add("${0}"), getContextName());
        return viewSource;
    }

    public IConnection getConnection() {
        Element connection = getXML().getChild("connection");
        if (connection != null) {
            String connectionID = resolve(connection.getAttributeValue("nameref"));
            return (IConnection) ConfigManager.getInstance().getComponent(getLocator().getRootLocator().add(IManager.Connections).add(connectionID), getContextName());
        }
        return null;
    }

    protected void setDefaultName() {
        if (getName() == null) setName(getXML().getChild("source").getAttributeValue("nameref"));
    }

    public Context getContext() {
        Element context = getXML().getChild("context");
        String contextName = null;
        Context c = null;
        if (context != null) {
            contextName = context.getAttributeValue("nameref");
            c = (Context) ConfigManager.getInstance().getContext(getLocator().getRootName(), contextName);
        } else {
            c = (Context) super.getContext();
        }
        return c;
    }

    private String printModes() {
        StringBuffer output = new StringBuffer();
        for (Modes mode : Modes.values()) {
            output.append(mode.toString() + " ");
        }
        return output.toString();
    }

    protected String getDefaultMode() {
        return getParameter().getProperty("mode", "ADD");
    }

    public Modes getMode() {
        Modes mode;
        String modeString = resolve(getXML().getAttributeValue("mode", getDefaultMode()));
        try {
            mode = Modes.valueOf(modeString.toUpperCase());
        } catch (Exception e) {
            log.warn("Parameter mode has to be set to either " + printModes() + ". Falling back to mode ADD...");
            mode = Modes.ADD;
        }
        return mode;
    }

    public void configure() throws ConfigurationException {
        super.configure();
        setDefaultName();
    }
}
