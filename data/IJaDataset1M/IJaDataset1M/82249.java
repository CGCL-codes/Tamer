package ch.isbiel.oois.infospectator.service;

import java.rmi.RemoteException;
import java.util.List;
import ch.isbiel.oois.common.service.ServiceLocator;
import ch.isbiel.oois.common.service.ServiceLocatorFactory;
import ch.isbiel.oois.infocore.dto.AthleteDO;
import ch.isbiel.oois.infocore.dto.RaceMetaDO;
import ch.isbiel.oois.infocore.ejb.IDNotFoundException;
import ch.isbiel.oois.infocore.ejb.InfoManager;
import ch.isbiel.oois.infocore.ejb.InfoManagerHome;

/**
 * Business Delegate that supports the InfoManagerService interface using the
 * InfoManager session bean.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class InfoManagerEJBDelegate implements InfoManagerService {

    /**
   * Reference to the InfoManager session bean
   */
    private InfoManager _im;

    /**
   * Create a new delegate
   * 
   * @param providerURL URL of the JNDI provider
   * 
   * @throws InfoManagerServiceException if the initialization of
   *    the InfoManager failed
   */
    public InfoManagerEJBDelegate(String providerURL) throws InfoManagerServiceException {
        init(providerURL);
    }

    /**
   * Retrieves a InfoManager stub
   * 
   * @param providerURL URL of the JNDI provider
   * 
   * @throws InfoManagerServiceException if the lookup of the InfoManager failed
   */
    private void init(String providerURL) throws InfoManagerServiceException {
        try {
            ServiceLocator sl = ServiceLocatorFactory.getInstance(providerURL);
            InfoManagerHome im_home = (InfoManagerHome) sl.getRemoteHome(InfoManagerHome.JNDI_NAME, InfoManagerHome.class);
            _im = im_home.create();
        } catch (Exception e) {
            throw new InfoManagerServiceException(e);
        }
    }

    /**
   * Return a List of DTOs of all Athletes for a specific race that
   * match the specified category.
   * @param raceId Id of the Race we want to query for Athletes
   * @param category The category that returned Athletes have to match. null and
   *    AthleteRaceDO.ALL_CATEGORIES matches Athletes of all categories
   * @return A List of AthleteRaceDOs. An empty List is returned if the Race
   *    has no matching Athletes
   * @throws InfoManagerServiceException If the operation failed
   */
    public List getAthletes(Integer raceId, String category) throws InfoManagerServiceException {
        try {
            return _im.getAthletes(raceId, category);
        } catch (RemoteException rex) {
            throw new InfoManagerServiceException(rex);
        } catch (IDNotFoundException idex) {
            throw new InfoManagerServiceException(idex);
        }
    }

    /**
   * Return a List of DTOs of all Competitions for a specific event
   * @param eventId Id of the Event we want to query for Competitions
   * @return A List of CompetitionRaceMetaDOs. An empy list is returned if
   *     the event has no Competitions
   * @throws InfoManagerServiceException If the operation failed
   */
    public List getCompetitions(Integer eventId) throws InfoManagerServiceException {
        try {
            return _im.getCompetitions(eventId);
        } catch (RemoteException rex) {
            throw new InfoManagerServiceException(rex);
        } catch (IDNotFoundException idex) {
            throw new InfoManagerServiceException(idex);
        }
    }

    /**
   * Return a List of DTOs for all Events in OOIS.
   * @return A List of EventDOs
   * @throws InfoManagerServiceException If the operation failed
   */
    public List getEvents() throws InfoManagerServiceException {
        try {
            return _im.getEvents();
        } catch (RemoteException rex) {
            throw new InfoManagerServiceException(rex);
        }
    }

    /**
   * Return race meta information for a specific race.
   * @param raceId Id of the race
   * @return A RaceMetaDOs
   * @throws InfoManagerServiceException If the operation failed
   */
    public RaceMetaDO getRaceMetaInfo(Integer raceId) throws InfoManagerServiceException {
        try {
            return _im.getRaceMetaInfo(raceId);
        } catch (RemoteException rex) {
            throw new InfoManagerServiceException(rex);
        } catch (IDNotFoundException idex) {
            throw new InfoManagerServiceException(idex);
        }
    }

    /**
   * Return a List of DTOs of all Races for a specific competition
   * @param compId Id of the Competition we want to query for Races
   * @return A List of RaceMetaDOs. An empty List is returned if the
   *    Competition has no Races
   * @throws InfoManagerServiceException If the operation failed
   */
    public List getRaces(Integer compId) throws InfoManagerServiceException {
        try {
            return _im.getRaces(compId);
        } catch (RemoteException rex) {
            throw new InfoManagerServiceException(rex);
        } catch (IDNotFoundException idex) {
            throw new InfoManagerServiceException(idex);
        }
    }

    /**
   * Return a List of DTOs of all Teams for a specific relay race that
   * match the specified category.
   * @param raceId Id of the Race we want to query for Teams
   * @param category The category that returned Athletes have to match. null and
   *    TeamRaceDO.ALL_CATEGORIES matches Teams of all categories
   * @return A List of TeamRaceDOs. An empty List is returned if the Race
   *    has no matching Teams
   * @throws InfoManagerServiceException If the operation failed
   */
    public List getTeams(Integer raceId, String category) throws InfoManagerServiceException {
        try {
            return _im.getTeams(raceId, category);
        } catch (RemoteException rex) {
            throw new InfoManagerServiceException(rex);
        } catch (IDNotFoundException idex) {
            throw new InfoManagerServiceException(idex);
        }
    }

    /**
   * Return information for a specific athlete.
   *
   * @param iofId Id of the athlete
   * @return An AthleteDO
   * 
   * @throws InfoManagerServiceException If the operation failed
   */
    public AthleteDO getAthleteInfo(String iofId) throws InfoManagerServiceException {
        try {
            return _im.getAthleteInfo(iofId);
        } catch (RemoteException rex) {
            throw new InfoManagerServiceException(rex);
        } catch (IDNotFoundException idex) {
            throw new InfoManagerServiceException(idex);
        }
    }
}
