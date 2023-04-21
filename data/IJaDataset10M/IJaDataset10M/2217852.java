package twilio.client;

public class GetConferencesTest extends AbstractTwilioTest {

    public void testGetConferences() throws Exception {
        TwilioClient c = getClient();
        Conferences cs = c.getConferences();
        System.out.println("cs.size() = " + cs.size());
        assertNotNull(cs);
        for (Conference conf : cs) {
            assertValid(conf);
            System.out.println(conf);
        }
    }
}
