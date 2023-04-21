package org.openymsg.test;

import junitx.extensions.EqualsHashCodeTestCase;
import org.openymsg.network.YahooIdentity;

/**
 * Basic Equality and HashCode contract checks.
 * 
 * @author G. der Kinderen, Nimbuzz B.V. guus@nimbuzz.com
 */
public class YahooIdentityTest extends EqualsHashCodeTestCase {

    public YahooIdentityTest() {
        super(YahooIdentityTest.class.getName());
    }

    @Override
    protected Object createInstance() throws Exception {
        return new YahooIdentity("same");
    }

    @Override
    protected Object createNotEqualInstance() throws Exception {
        return new YahooIdentity("different");
    }
}
