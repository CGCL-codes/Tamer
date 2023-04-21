package sk.fiit.mitandao.modules.inputs.dbreader;

/**
 * This is a parameter class. It only holds the infromation given from the first
 * screen and is used by the DBReaderController.
 * @author jelen
 *
 */
public class DBConnectionParameters {

    private String hostValue;

    private String portValue;

    private String userValue;

    private String passwordValue;

    private String dbNameValue;

    private String schemaValue;

    public String getHostValue() {
        return hostValue;
    }

    public void setHostValue(String hostValue) {
        this.hostValue = hostValue;
    }

    public String getPortValue() {
        return portValue;
    }

    public void setPortValue(String portValue) {
        this.portValue = portValue;
    }

    public String getUserValue() {
        return userValue;
    }

    public void setUserValue(String userValue) {
        this.userValue = userValue;
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    public void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
    }

    public String getDbNameValue() {
        return dbNameValue;
    }

    public void setDbNameValue(String dbNameValue) {
        this.dbNameValue = dbNameValue;
    }

    public String getSchemaValue() {
        return schemaValue;
    }

    public void setSchemaValue(String schemaValue) {
        this.schemaValue = schemaValue;
    }
}
