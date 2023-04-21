package net.sf.mytoolbox.test;

/**
 * Helper class for EasyMock. <br/>
 * @author ggrussenmeyer
 */
public class EasyMock extends org.easymock.EasyMock {

    /**
     * Verify and then reset the given mocks. <br/>
     * @param objects
     */
    public static void verifyAndReset(Object... objects) {
        verify(objects);
        reset(objects);
    }
}
