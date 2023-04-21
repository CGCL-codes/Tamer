package spellcast.ui;

/**
 * Property change event for connecting to a server.
 *
 * @author Barrie Treloar
 */
public class ConnectToServerEvent {

    private String serverAddress;

    private String wizardName;

    private String gender;

    public ConnectToServerEvent(String serverAddress, String wizardName, String gender) {
        this.serverAddress = serverAddress;
        this.wizardName = wizardName;
        this.gender = gender;
    }

    /**
     * Get the value of serverAddress.
     *
     * @return value of serverAddress.
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Get the value of wizardName.
     *
     * @return value of wizardName.
     */
    public String getWizardName() {
        return wizardName;
    }

    /**
     * Get the value of gender.
     *
     * @return value of gender.
     */
    public String getGender() {
        return gender;
    }
}
