package net.sf.jawp.api.service;

import java.util.Collection;
import net.sf.jawp.api.domain.BattleReportVO;
import net.sf.jawp.api.domain.Fleet;
import net.sf.jawp.api.domain.GameSpeed;
import net.sf.jawp.api.domain.Planet;
import net.sf.jawp.api.domain.Realm;

/**
 * Game specific methods. 
 * all methods are called for current player
 * @author jarek
 * @version $Revision: 1.12 $
 *
 */
public interface JAWPGameService {

    /**
	 * return player realm
	 */
    Realm getRealm();

    /**
	 * returns all visible planets 
	 */
    Collection<Planet> getPlanets();

    /**
	 * sends order to move fleet between given planets
	 * @throws IllegalArgumentException when from planet not owned by player
	 */
    void moveFleet(final long fromPlanetKey, final long toPlanetKey, final int size) throws IllegalArgumentException;

    /**
	 * returns other player realm
	 */
    Realm getOtherRealm(final long realmKey);

    Collection<Fleet> getOwnFleets();

    GameSpeed getGameSpeed();

    Collection<BattleReportVO> getBattleReports();
}
