package ghm.follow.test;

import static org.junit.Assert.assertEquals;
import ghm.follow.FileFollower;
import ghm.follow.io.OutputDestination;
import ghm.follow.io.PrintStreamDestination;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Before;
import org.junit.Test;

public class PrintStreamDestinationT extends BaseTestCase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testPrintCalled() throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PrintStreamDestination dest = new PrintStreamDestination(new PrintStream(byteStream));
        follower = new FileFollower(followedFile, new OutputDestination[] { dest });
        follower.start();
        String control = "control";
        writeToFollowedFileAndWait(control);
        assertEquals(control, new String(byteStream.toByteArray()));
    }
}
