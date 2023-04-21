package net.liveseeds.base.direction;

import net.liveseeds.base.Coordinate;
import net.liveseeds.base.world.World;
import java.io.Serializable;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public interface Direction extends Serializable {

    long serialVersionUID = 161723809325394365L;

    int getXDelta();

    int getYDelta();

    String getDescription();

    int getCode();

    Coordinate getCellPoint(final World world, final Coordinate cellPoint);
}
