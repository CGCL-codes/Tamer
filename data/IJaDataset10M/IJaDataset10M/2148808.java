package jp.seraph.same.view.cui;

import jp.seraph.cui.AbstractCommandType;
import jp.seraph.cui.CommandType;
import jp.seraph.cui.SystemNodeType;

/**
 *
 */
public class RunMotionCommandType extends AbstractCommandType {

    public RunMotionCommandType() {
        super("RunMotionCommand");
    }

    private static RunMotionCommandType INSTANCE = new RunMotionCommandType();

    public static RunMotionCommandType getInstance() {
        return INSTANCE;
    }

    /**
     * @see jp.seraph.cui.AbstractNodeType#getSuperType()
     */
    @Override
    protected SystemNodeType getSuperType() {
        return CommandType.getInstance();
    }
}
