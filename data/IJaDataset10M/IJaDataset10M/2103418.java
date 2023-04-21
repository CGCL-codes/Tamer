package net.mlw.fball.bo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew L. Wilson
 * @version $Revision: 1.6 $ $Date: 2004/03/17 14:20:21 $
 */
public class Player {

    private Double DEFAULT_FANTASY_VALUE = new Double(0);

    private String id;

    private String name;

    private String position;

    private Team currentTeam;

    private String firstName, lastName;

    private Double fantasyValue = DEFAULT_FANTASY_VALUE;

    private Map providers = new HashMap();

    public Player() {
    }

    /**
    * @return Returns the id.
    */
    public String getId() {
        return id;
    }

    /**
    * @param id The id to set.
    */
    private void setId(String id) {
        this.id = id;
    }

    /**
    * @return Returns the name.
    */
    public String getName() {
        return name;
    }

    /**
    * @param name The name to set.
    */
    public void setName(String name) {
        this.name = name;
    }

    public void addProvider(String providerId, String playerProviderId) {
        providers.put(providerId, playerProviderId);
    }

    public String getProvider(String providerId) {
        return (String) providers.get(providerId);
    }

    /**
    * @return Returns the providerIds.
    */
    private Map getProviders() {
        return providers;
    }

    /**
    * @param providerIds The providerIds to set.
    */
    private void setProviders(Map providers) {
        this.providers = providers;
    }

    /**
    * @return Returns the currentTeam.
    */
    public Team getCurrentTeam() {
        return currentTeam;
    }

    /**
    * @param currentTeam The currentTeam to set.
    */
    public void setCurrentTeam(Team currentTeam) {
        this.currentTeam = currentTeam;
    }

    /**
    * @return Returns the firstName.
    */
    public String getFirstName() {
        return firstName;
    }

    /**
    * @param firstName The firstName to set.
    */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
    * @return Returns the lastName.
    */
    public String getLastName() {
        return lastName;
    }

    /**
    * @param lastName The lastName to set.
    */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
    * @return Returns the position.
    */
    public String getPosition() {
        return position;
    }

    /**
    * @param position The position to set.
    */
    public void setPosition(String position) {
        this.position = position;
    }

    /** @see java.lang.Object#toString()
    */
    public String toString() {
        return name + " [" + position + "]";
    }

    /**
    * @return Returns the fantasyValue.
    */
    public Double getFantasyValue() {
        return fantasyValue;
    }

    /**
    * @param fantasyValue The fantasyValue to set.
    */
    public void setFantasyValue(Double fantasyValue) {
        this.fantasyValue = fantasyValue;
    }
}
