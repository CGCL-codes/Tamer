package test;

import java.io.IOException;
import junit.textui.TestRunner;
import com.amazonaws.mturk.cmd.RejectQualificationRequests;

public class TestRejectQualificationRequests extends TestBase {

    private String rejectFile = "qualRequestsToReject.txt";

    public static void main(String[] args) {
        TestRunner.run(TestRejectQualificationRequests.class);
    }

    public TestRejectQualificationRequests(String arg0) {
        super(arg0);
    }

    public void testHappyCase() throws IOException {
        RejectQualificationRequests cmd = new RejectQualificationRequests();
        cmd.setForce(true);
        cmd.rejectQualRequestsInFile(rejectFile);
    }

    public void testNullRejectFile() throws IOException {
        RejectQualificationRequests cmd = new RejectQualificationRequests();
        try {
            cmd.rejectQualRequestsInFile(null);
            fail("Expected failure");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testInvalidQualficationRequestIds() throws IOException {
        RejectQualificationRequests cmd = new RejectQualificationRequests();
        cmd.setForce(true);
        try {
            cmd.rejectQualRequests("qualReq1,qualReq2");
            fail("Expected failure");
        } catch (Exception e) {
        }
    }
}
