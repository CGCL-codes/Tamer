package client.gameplay;

import java.awt.Point;
import java.util.List;
import network.protocol.PlayerPackage;

/**
 * Interface for the buildings.
 * @author Torbj�rn
 *
 */
public interface Buildings {

    float growth(float curRes);

    boolean isUpgradeable(float res);

    float defend(int attackMorale, int ownMorale, int attackArmory);

    int upgradeCost();

    void addLevel();

    Buildings clone();

    Buildings clone(int level);

    void InRange(Point pos, List<Unit> units, PlayerPackage owner);

    int getLevel();

    void reduceLevel();
}
