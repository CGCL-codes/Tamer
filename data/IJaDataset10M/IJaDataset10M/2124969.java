package sevs.network;

import sevs.msg.*;

/**
 * Tests the dummy client module.
 * 
 * @author  Kohsuke Kawaguchi
 */
public class ClientTest {

    public static void main(String[] args) throws Exception {
        NetworkClient nc = NetworkFactory.createClient("test", 6789);
        testVote(nc);
    }

    /** Tests the vote casting potocol. */
    private static void testVote(NetworkClient nc) throws Exception {
        Status s = (Status) nc.onMessage(new VoteCast("abc", "def", "user", "pin"));
        System.out.println(s.isOK);
        System.out.println(s.message);
    }

    /** Tests the candidates query protocol. */
    private static void testCandidates(NetworkClient nc) throws Exception {
        CandidatesList cl = (CandidatesList) nc.onMessage(new CandidatesQuery("dummy"));
        System.out.println(cl.isOK);
        System.out.println(cl.message);
        if (cl.getCandidates() != null) {
            for (int i = 0; i < cl.getCandidates().length; i++) System.out.println(cl.getCandidates()[i]);
        } else {
            System.out.println("null candidates");
        }
    }
}
