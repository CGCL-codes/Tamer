package ca.whu.taxman.engine;

import ca.whu.taxman.entity.Line;
import javax.ejb.Stateless;

/**
 *
 * @author Peter Wu <peterwu@hotmail.com>
 */
@Stateless
public class RuleEngineBean implements RuleEngineLocal {

    public void enforceRule(final Line line) {
    }
}
