package com.ixora.rms.reactions;

import java.io.ObjectStreamException;
import com.ixora.common.Enum;

/**
 * @author Daniel Moraru
 */
public final class ReactionState extends Enum {

    private static final long serialVersionUID = 3141113311951800930L;

    public static final ReactionState ARMED = new ReactionState(0, "armed");

    public static final ReactionState FIRED = new ReactionState(1, "fired");

    public static final ReactionState FIRED_OK = new ReactionState(2, "fired ok");

    public static final ReactionState FIRED_ERROR = new ReactionState(3, "fired error");

    public static final ReactionState DISARMED = new ReactionState(4, "disarmed");

    ;

    /**
	 * @param key
	 * @param name
	 */
    protected ReactionState(int key, String name) {
        super(key, name);
    }

    /**
	 * @see com.ixora.common.Enum#readResolve()
	 */
    protected Object readResolve() throws ObjectStreamException {
        switch(key) {
            case 0:
                return ARMED;
            case 1:
                return FIRED;
            case 2:
                return FIRED_OK;
            case 3:
                return FIRED_ERROR;
            case 4:
                return DISARMED;
        }
        return null;
    }
}
