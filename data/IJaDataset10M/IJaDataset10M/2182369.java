package test;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import fw4ex_client.authentication.AuthenticationCredentials;
import fw4ex_client.authentication.IServerAuthentication;
import fw4ex_client.authentication.ServerAuthentication;
import fw4ex_client.data.interfaces.IExercisePathItem;
import fw4ex_client.exercise.retrieve.RetrieveExerciseList;

public class RetrieveExceriseListTest {

    RetrieveExerciseList retrieve;

    IServerAuthentication auth;

    @Before
    public void setUp() throws Exception {
        retrieve = new RetrieveExerciseList();
        auth = ServerAuthentication.getInstance();
        auth.setCredentials(new AuthenticationCredentials("conf/mxdata.inc"));
        auth.authenticate();
    }

    @Test
    public void testGetList() {
        retrieve.setKeyword("li362");
        retrieve.fetchExoList();
        IExercisePathItem res = retrieve.getTree();
        assertTrue(res.size() > 0);
        retrieve.setKeyword("");
        retrieve.fetchExoList();
        res = retrieve.getTree();
        assertTrue(res.size() == 0);
        retrieve.setKeyword("whatever");
        retrieve.fetchExoList();
        res = retrieve.getTree();
        assertTrue(res.size() == 0);
        auth.logout();
        retrieve.setKeyword("li362");
        retrieve.fetchExoList();
        res = retrieve.getTree();
        assertTrue(res.size() == 0);
    }
}
