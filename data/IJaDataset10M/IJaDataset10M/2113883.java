package com.mindquarry.persistence.jcr;

import static com.mindquarry.common.lang.StringUtil.concat;
import java.util.LinkedList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import org.apache.avalon.framework.service.ServiceException;
import com.mindquarry.persistence.api.JavaConfiguration;
import com.mindquarry.persistence.api.Session;
import com.mindquarry.persistence.api.SessionFactory;
import com.mindquarry.persistence.jcr.query.QueryException;

public class JcrPersistenceTest extends JcrPersistenceTestBase {

    private static final String TEST_USER_ID = "testUser";

    private static final String TEST_TEAM_ID = "jcr-persistence";

    private SessionFactory sessionFactory_;

    protected void setUp() throws Exception {
        super.setUp();
        JavaConfiguration configuration = new JavaConfiguration();
        configuration.addClass(User.class);
        configuration.addClass(Group.class);
        configuration.addClass(Team.class);
        Persistence persistence = (Persistence) lookup(Persistence.ROLE);
        persistence.addConfiguration(configuration);
        sessionFactory_ = persistence;
    }

    protected void tearDown() throws Exception {
        javax.jcr.Session jcrSession = getJcrSession();
        Node rootNode = jcrSession.getRootNode();
        String[] entityFolders = new String[] { User.PARENT_FOLDER, Team.PARENT_FOLDER, Group.PARENT_FOLDER };
        for (String entityFolder : entityFolders) {
            if (rootNode.hasNode(entityFolder)) rootNode.getNode(entityFolder).remove();
        }
        jcrSession.save();
        super.tearDown();
    }

    private Session currentSession() {
        return sessionFactory_.currentSession();
    }

    private User makeTestUser() {
        User user = new User();
        user.setLogin(TEST_USER_ID);
        user.setPwd("pwd");
        user.firstname = "test";
        user.lastname = "test";
        return user;
    }

    private String testUserContentNodePath() {
        return concat(User.PARENT_FOLDER, "/", TEST_USER_ID, "/", "jcr:content");
    }

    private Team makeTestTeam() {
        Team team = new Team();
        team.name = TEST_TEAM_ID;
        team.title = "Jcr Persistence title";
        team.description = "bla blub";
        return team;
    }

    private String testTeamContentNodePath() {
        return concat(Team.PARENT_FOLDER, "/", TEST_TEAM_ID, "/", TEST_TEAM_ID, ".xml", "/", "jcr:content");
    }

    private String textValueOfChild(Node contentNode, String childName) throws Exception {
        return textValue(contentNode.getNode(childName));
    }

    private String textValue(Node node) throws Exception {
        Node textNode = node.getNode("text");
        return textNode.getProperty("xt:characters").getString();
    }

    public void testPersistUser() throws Exception {
        User user = makeTestUser();
        currentSession().persist(user);
        Node rootNode = getJcrSession().getRootNode();
        assertTrue(rootNode.hasNode(testUserContentNodePath()));
        Node contentNode = rootNode.getNode(testUserContentNodePath());
        assertEquals(user.getPwd(), textValueOfChild(contentNode, "pwd"));
        assertEquals(user.firstname, textValueOfChild(contentNode, "firstname"));
        assertEquals(user.lastname, textValueOfChild(contentNode, "lastname"));
    }

    public void testPersistCompositeTeamEntity() throws Exception {
        Team team = makeTestTeam();
        currentSession().persist(team);
        Node rootNode = getJcrSession().getRootNode();
        assertTrue(rootNode.hasNode(testTeamContentNodePath()));
        Node contentNode = rootNode.getNode(testTeamContentNodePath());
        assertEquals(team.title, textValueOfChild(contentNode, "title"));
        assertEquals(team.description, textValueOfChild(contentNode, "description"));
    }

    public void testPersistCollectionProperties() throws Exception {
        User user = makeTestUser();
        List<String> skills = new LinkedList<String>();
        skills.add("foo");
        skills.add("bar");
        user.setSkills(skills);
        user.setSkillsArray(skills.toArray(new String[skills.size()]));
        currentSession().persist(user);
        Node rootNode = getJcrSession().getRootNode();
        Node contentNode = rootNode.getNode(testUserContentNodePath());
        String[] collectionProperties = new String[] { "skills", "skillsArray" };
        for (String collectionProperty : collectionProperties) {
            Node collectionNode = contentNode.getNode(collectionProperty);
            assertEquals(2, collectionNode.getNodes().getSize());
            NodeIterator itemIt = collectionNode.getNodes();
            assertEquals("foo", textValue(itemIt.nextNode()));
            assertEquals("bar", textValue(itemIt.nextNode()));
        }
    }

    public void testUpdateUser() throws Exception {
        User user = makeTestUser();
        currentSession().persist(user);
        user.lastname = "updatedLastName";
        currentSession().update(user);
        Node rootNode = getJcrSession().getRootNode();
        Node contentNode = rootNode.getNode(testUserContentNodePath());
        assertEquals(user.lastname, textValueOfChild(contentNode, "lastname"));
    }

    public void testQueryUsersByLogin() throws Exception {
        currentSession().persist(makeTestUser());
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query("userByLogin", TEST_USER_ID);
        assertEquals(1, queryResult.size());
        User user = (User) queryResult.get(0);
        assertEquals(TEST_USER_ID, user.getLogin());
    }

    public void testPersistUserReferencingTeams() {
        Team team = makeTestTeam();
        currentSession().persist(team);
        List<Team> teams = new LinkedList<Team>();
        teams.add(team);
        User user = makeTestUser();
        user.setTeams(teams);
        currentSession().persist(user);
        List<Object> queryResult = currentSession().query("userByLogin", TEST_USER_ID);
        User queriedUser = (User) queryResult.get(0);
        assertEquals(1, queriedUser.getTeams().size());
        assertEquals(TEST_TEAM_ID, queriedUser.getTeams().get(0).name);
    }

    public void testQueryUsersForTeam() throws ServiceException {
        Team team = makeTestTeam();
        currentSession().persist(team);
        List<Team> teams = new LinkedList<Team>();
        teams.add(team);
        User user = makeTestUser();
        user.setTeams(teams);
        currentSession().persist(user);
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query("getUsersForTeam", TEST_TEAM_ID);
        assertEquals(1, queryResult.size());
    }

    public void testMapProperties() throws ServiceException {
        Team team = makeTestTeam();
        List<String> aMapValue = new LinkedList<String>();
        aMapValue.add("blub");
        team.fooMap.put("foo", aMapValue);
        currentSession().persist(team);
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query("allTeams");
        assertEquals(1, queryResult.size());
        Team queriedTeam = (Team) queryResult.get(0);
        assertEquals("blub", queriedTeam.fooMap.get("foo").get(0));
    }

    public void testInvalidQuery() throws ServiceException {
        Session session = sessionFactory_.currentSession();
        try {
            session.query("userByLogin");
        } catch (QueryException e) {
            return;
        }
        fail();
    }

    public void testQueryAllUsers() throws ServiceException {
        currentSession().persist(makeTestUser());
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query("allUsers");
        assertEquals(1, queryResult.size());
    }

    public void testDynamicTypedCollection() throws ServiceException {
        User user = makeTestUser();
        currentSession().persist(user);
        Group group = new Group();
        group.id = "foogroup";
        group.members.add(user);
        currentSession().persist(group);
    }

    public void testDeleteUser() throws Exception {
        currentSession().persist(makeTestUser());
        Session session = sessionFactory_.currentSession();
        List<Object> queryResult = session.query("userByLogin", TEST_USER_ID);
        User user = (User) queryResult.get(0);
        session.delete(user);
        Node rootNode = getJcrSession().getRootNode();
        Node userParentNode = rootNode.getNode(User.PARENT_FOLDER);
        assertFalse(userParentNode.hasNode(TEST_USER_ID));
    }
}
