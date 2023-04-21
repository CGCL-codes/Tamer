package edu.upmc.opi.caBIG.caTIES.installer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import edu.upmc.opi.caBIG.caTIES.server.dispatcher.handshake.CaTIES_HandShaker;

public class CaTIES_CommandProcessorConfiguration {

    protected Class commandProcessorClass;

    protected Properties properties = new Properties();

    protected ArrayList orderedPropertyNames = new ArrayList();

    public static void main(String[] args) {
        CaTIES_CommandProcessorConfiguration caGen = new CaTIES_CommandProcessorConfiguration();
        caGen.setCommandProcessorClass(CaTIES_HandShaker.class);
        caGen.init();
    }

    public void init() {
    }

    public Class getCommandProcessorClass() {
        return commandProcessorClass;
    }

    public void setCommandProcessorClass(Class commandProcessorClass) {
        this.commandProcessorClass = commandProcessorClass;
    }

    public Properties getProperties() {
        return properties;
    }

    public void addProperty(String name, String value) {
        this.orderedPropertyNames.add(name);
        this.properties.put(name, value);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = this.orderedPropertyNames.iterator(); iterator.hasNext(); ) {
            String name = (String) iterator.next();
            String value = this.properties.getProperty(name);
            sb.append(name + " = " + value + "\n");
        }
        return sb.toString();
    }
}
