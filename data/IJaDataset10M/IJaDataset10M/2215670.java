package cn.saker.test.org.apache.commons.chain;

import junit.framework.TestCase;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;

public class ChainTest extends TestCase {

    public void testProfileCheckNeed() {
        Context context = new ContextBase();
        Command command = new ProfileCheck();
        try {
            command.execute(context);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        Profile profile = (Profile) context.get(Profile.PROFILE_KEY);
        assertNotNull("Missing Profile", profile);
    }
}
