package com.privilege.entity.tournament;

import java.util.HashSet;
import java.util.Set;
import org.directwebremoting.util.Logger;
import com.privilege.entity.Entity;

public class PoolDescription implements Entity {

    private long id = 0;

    private long tournamentId;

    private int nbOpponents = 0;

    private int nbGroups = 0;

    private boolean eliminationDirect = false;

    private boolean isAllerRetour = false;

    private int nbQualified = 0;

    private Set<Opponent> opponents = new HashSet<Opponent>();

    private Set<Group> groups = new HashSet<Group>();

    private static final Logger logger = Logger.getLogger(PoolDescription.class);

    public long getId() {
        logger.debug("Method: getId(" + ")");
        return this.id;
    }

    private void setId(long id) {
        logger.debug("Method: setId(" + id + ")");
        this.id = id;
    }

    public long getTournamentId() {
        logger.debug("Method: getTournamentId(" + ")");
        return this.tournamentId;
    }

    public void setTournamentId(long id) {
        logger.debug("Method: setTournamentId(" + tournamentId + ")");
        this.tournamentId = tournamentId;
    }

    public int getNbOpponents() {
        logger.debug("Method: getNbOpponents(" + ")");
        return this.nbOpponents;
    }

    public void setNbOpponents(int nbOpponents) {
        logger.debug("Method: setNbOpponents(" + nbOpponents + ")");
        this.nbOpponents = nbOpponents;
    }

    public int getNbGroups() {
        logger.debug("Method: getNbGroups(" + ")");
        return this.nbGroups;
    }

    public void setNbGroups(int nbGroups) {
        logger.debug("Method: setNbGroups(" + nbGroups + ")");
        this.nbGroups = nbGroups;
    }

    public int getNbQualified() {
        logger.debug("Method: getNbQualified(" + ")");
        return this.nbQualified;
    }

    public void setNbQualified(int nbQualified) {
        logger.debug("Method: setNbQualified(" + nbQualified + ")");
        this.nbQualified = nbQualified;
    }

    public boolean getIsAllerRetour() {
        logger.debug("Method: isAllerRetour(" + ")");
        return this.isAllerRetour;
    }

    public void setIsAllerRetour(boolean isAllerRetour) {
        logger.debug("Method: setIsAllerRetour(" + isAllerRetour + ")");
        this.isAllerRetour = isAllerRetour;
    }

    public boolean getEliminationDirect() {
        logger.debug("Method: getEliminationDirect(" + ")");
        return this.eliminationDirect;
    }

    public void setEliminationDirect(boolean eliminationDirect) {
        logger.debug("Method: setEliminationDirect(" + eliminationDirect + ")");
        this.eliminationDirect = eliminationDirect;
    }

    public Set getOpponents() {
        logger.debug("Method: getOpponents(" + ")");
        return this.opponents;
    }

    public void setOpponents(Set opponents) {
        logger.debug("Method: setOpponents(" + opponents + ")");
        this.opponents = opponents;
    }

    public Set getGroups() {
        logger.debug("Method: getGroups(" + ")");
        return this.groups;
    }

    public void setGroups(Set opponents) {
        logger.debug("Method: setGroups(" + groups + ")");
        this.groups = groups;
    }

    public void associate(Entity entity) {
        logger.debug("Method: associate(" + entity + ")");
        if (entity instanceof Opponent) {
            if (this.opponents.size() < this.nbOpponents) this.opponents.add((Opponent) entity); else {
            }
        } else if (entity instanceof Group) {
            if (this.groups.size() < this.nbGroups) this.groups.add((Group) entity); else {
            }
        }
    }

    public void dissociate(Entity entity) {
        logger.debug("Method: dissociate(" + entity + ")");
        if (entity instanceof Opponent) {
            this.opponents.remove((Opponent) entity);
        } else if (entity instanceof Group) {
            this.groups.remove((Group) entity);
        }
    }
}
