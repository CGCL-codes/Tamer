package middlegen.plugins.entitybean;

import middlegen.MiddlegenException;

/**
 * Subtask specifying JBoss specific features to the entity bean plugin.
 *
 * @author Aslak Helles�y
 * @created 11. juli 2002
 */
public class JBoss extends Server {

    /**
    * Field denoted whether dynamicQL method should be generated.
    */
    private boolean _dynamicQL = false;

    /**
    * @todo-javadoc Describe the field
    */
    private static org.apache.log4j.Category _log = org.apache.log4j.Category.getInstance(JBoss.class.getName());

    /**
    * Sets the DynamicQL attribute of the JBoss object
    *
    * @param dynamicQL The new DynamicQL value
    */
    public void setDynamicQL(boolean dynamicQL) {
        _dynamicQL = dynamicQL;
    }

    /**
    * Sets the Pkgen attribute of the JBoss object
    *
    * @param pkgen The new Pkgen value
    * @exception MiddlegenException Describe the exception
    * @todo-javadoc Write javadocs for exception
    * @todo-javadoc Write javadocs for exception
    */
    public void setPkgen(boolean pkgen) throws MiddlegenException {
        if (pkgen) {
            throw new MiddlegenException("pkgen not supported for JBoss yet");
        }
    }

    /**
    * Gets the DynamicQL attribute of the JBoss object
    *
    * @return The DynamicQL value
    */
    public boolean isDynamicQL() {
        return _dynamicQL;
    }

    /**
    * Gets the ServerName attribute of the JBoss object
    *
    * @return The ServerName value
    */
    public String getServerName() {
        return "JBoss";
    }

    /**
    * Describe what the method does
    *
    * @exception MiddlegenException Describe the exception
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for exception
    */
    public void validate() throws MiddlegenException {
        super.validate();
        warnIfFkCmp();
    }

    /**
    * Logs a warning if fkcmp="true"
    */
    private void warnIfFkCmp() {
        if (getPlugin() instanceof CMP20Plugin) {
            CMP20Plugin cmp20Plugin = (CMP20Plugin) getPlugin();
            if (cmp20Plugin.isFkcmp()) {
                _log.warn("WARNING (" + cmp20Plugin.getName() + "): JBoss 3.0 doesn't support fields that are part of CMR to be CMP at the same time. " + "You might want to set fkcmp=\"false\" in the " + getPlugin().getName() + " plugin.");
            }
        }
    }
}
