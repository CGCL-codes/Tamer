package jp.seraph.jsade.action;

import jp.seraph.jsade.effector.Effector;
import jp.seraph.jsade.util.NaoEffectorCreator;

/**
 * Beamエフェクタを生成するAction実装
 */
public class NaoBeamAgentAction implements AgentAction {

    public NaoBeamAgentAction(double x, double y, double z) {
        mEffector = new NaoEffectorCreator().createBeamEffector(x, y, z);
    }

    private Effector mEffector;

    /**
     * 
     * @see jp.seraph.jsade.action.AgentAction#getActionEffector()
     */
    public Effector getActionEffector() {
        return mEffector;
    }
}
