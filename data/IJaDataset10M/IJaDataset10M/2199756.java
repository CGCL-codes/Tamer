package org.gguth;

/**
 * Created by IntelliJ IDEA.
 * User: jbunting
 * Date: Nov 16, 2008
 * Time: 11:45:54 AM
 * To change this template use File | Settings | File Templates.
 */
public interface RuleExpectation {

    void assertValid(Object ruleOutput);
}
