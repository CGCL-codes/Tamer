package org.openymsg.test;

import junitx.extensions.EqualsHashCodeTestCase;
import org.openymsg.network.YahooUser;

/**
 * Basic Equality and HashCode contract checks.
 * 
 * @author G. der Kinderen, Nimbuzz B.V. guus@nimbuzz.com
 */
public class YahooUserTest extends EqualsHashCodeTestCase {

    public YahooUserTest() {
        super(YahooUserTest.class.getName());
    }

    @Override
    protected Object createInstance() throws Exception {
        return new YahooUser("same");
    }

    @Override
    protected Object createNotEqualInstance() throws Exception {
        return new YahooUser("different");
    }
}
