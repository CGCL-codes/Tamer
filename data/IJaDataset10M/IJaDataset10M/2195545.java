package net.mlw.fball.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Matthew L. Wilson
 * @version $Revision: 1.5 $ $Date: 2004/03/17 14:20:21 $
 */
public class Coach {

    private String id;

    private String providerCoachId;

    private String name;

    private Map players;

    private List starters;

    public Coach(String providerCoachId) {
        this.providerCoachId = providerCoachId;
    }

    /**
    * @return Returns the providerCoachId.
    */
    public String getProviderCoachId() {
        return providerCoachId;
    }

    /**
    * @param providerCoachId The providerCoachId to set.
    */
    public void setProviderCoachId(String providerCoachId) {
        this.providerCoachId = providerCoachId;
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
    public void setId(String id) {
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

    private Coach() {
    }

    public Collection getPlayersCollection() {
        return getPlayers().values();
    }

    /**
    * @return Returns the players.
    */
    public Map getPlayers() {
        if (players == null) {
            players = new HashMap();
        }
        return players;
    }

    /**
    * @param players The players to set.
    */
    private void setPlayers(Map players) {
        this.players = players;
    }

    /**
    * @return Returns the starters.
    */
    public List getStarters() {
        if (starters == null) {
            starters = new ArrayList();
        }
        return starters;
    }
}
