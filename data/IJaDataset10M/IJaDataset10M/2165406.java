package team.strategy;

import controller.BehaviourController;
import transport.message.HearMessage;

/**
 *
 * @author rem
 */
public interface IStrategy {

    public void setController(BehaviourController controller);

    public String evaluate(HearMessage message);
}
