package jp.seraph.same.view.cui;

import jp.seraph.cui.AbstractCommandType;
import jp.seraph.cui.CommandType;
import jp.seraph.cui.SystemNodeType;

/**
 *
 *
 */
public class CreateMotionSessionCommandType extends AbstractCommandType {

    /**
     * @param aName
     */
    private CreateMotionSessionCommandType() {
        super("CreateSession");
    }

    private static CreateMotionSessionCommandType INSTANCE = new CreateMotionSessionCommandType();

    public static CreateMotionSessionCommandType getInstance() {
        return INSTANCE;
    }

    /**
     *
     * @see jp.seraph.cui.AbstractNodeType#getSuperType()
     */
    @Override
    protected SystemNodeType getSuperType() {
        return CommandType.getInstance();
    }
}
