package jp.seraph.same.view.cui;

import jp.seraph.cui.AbstractModeType;
import jp.seraph.cui.ModeType;
import jp.seraph.cui.SystemNodeType;

/**
 *
 *
 */
public class MotionSessionDataType extends AbstractModeType {

    /**
     * @param aName
     */
    private MotionSessionDataType() {
        super("MotionSessionData");
    }

    private static MotionSessionDataType INSTANCE = new MotionSessionDataType();

    public static MotionSessionDataType getInstance() {
        return INSTANCE;
    }

    /**
     *
     * @see jp.seraph.cui.AbstractNodeType#getSuperType()
     */
    @Override
    protected SystemNodeType getSuperType() {
        return ModeType.getInstance();
    }
}
