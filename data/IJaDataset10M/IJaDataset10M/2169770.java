package game;

import org.apache.log4j.Logger;
import singleton.RandomUtils;
import base.SingletonRegistry;
import base.enums.E_LOCTYPE;
import config.CONSTANTS;

public class Road extends GameLocation {

    private static final long serialVersionUID = -745832325087741501L;

    private static Logger LOGGER = Logger.getLogger(Road.class);

    public Road(Town one_, Town two_) {
        super(one_.getName() + CONSTANTS.ROAD_NAME_SEPARATOR + two_.getName(), E_LOCTYPE.LINE, SingletonRegistry.get(RandomUtils.class).nextIntInc(CONSTANTS.ROAD_MAXLENGTH));
        init(one_, two_);
        LOGGER.debug("Created Road " + getName() + " of length " + getLength());
    }

    public Road(Town one_, Town two_, int length_) {
        super(one_.getName() + CONSTANTS.ROAD_NAME_SEPARATOR + two_.getName(), E_LOCTYPE.LINE, length_);
        init(one_, two_);
        LOGGER.debug("Created Road " + getName() + " of length " + getLength());
    }

    private void init(Town one_, Town two_) {
        one_.addLink(this);
        two_.addLink(this);
        addLink(one_);
        addLink(two_);
    }
}
