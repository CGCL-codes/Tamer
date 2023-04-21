package org.apache.nutch.protocol.http.api;

import org.apache.nutch.protocol.http.api.RobotRulesParser.RobotRuleSet;
import junit.framework.TestCase;

public class TestRobotRulesParser extends TestCase {

    private static final String LF = "\n";

    private static final String CR = "\r";

    private static final String CRLF = "\r\n";

    private static final boolean[] ACCEPT_ALL = { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true };

    private static final String[] ROBOTS_STRINGS = new String[] { "User-Agent: Agent1 #foo" + CR + "Disallow: /a" + CR + "Disallow: /b/a" + CR + "#Disallow: /c" + CR + "" + CR + "" + CR + "User-Agent: Agent2 Agent3#foo" + CR + "User-Agent: Agent4" + CR + "Disallow: /d" + CR + "Disallow: /e/d/" + CR + "" + CR + "User-Agent: *" + CR + "Disallow: /foo/bar/" + CR, null };

    private static final String[] AGENT_STRINGS = new String[] { "Agent1", "Agent2", "Agent3", "Agent4", "Agent5" };

    private static final boolean[][] NOT_IN_ROBOTS_STRING = new boolean[][] { { false, false, false, false, true }, { false, false, false, false, true } };

    private static final String[] TEST_PATHS = new String[] { "/a", "/a/", "/a/bloh/foo.html", "/b", "/b/a", "/b/a/index.html", "/b/b/foo.html", "/c", "/c/a", "/c/a/index.html", "/c/b/foo.html", "/d", "/d/a", "/e/a/index.html", "/e/d", "/e/d/foo.html", "/e/doh.html", "/f/index.html", "/foo/bar/baz.html", "/f/" };

    private static final boolean[][][] ALLOWED = new boolean[][][] { { { false, false, false, true, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true }, { true, true, true, true, true, true, true, true, true, true, true, false, false, true, true, false, true, true, true, true }, { true, true, true, true, true, true, true, true, true, true, true, false, false, true, true, false, true, true, true, true }, { true, true, true, true, true, true, true, true, true, true, true, false, false, true, true, false, true, true, true, true }, { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true } }, { ACCEPT_ALL, ACCEPT_ALL, ACCEPT_ALL, ACCEPT_ALL, ACCEPT_ALL } };

    public TestRobotRulesParser(String name) {
        super(name);
    }

    public void testRobotsOneAgent() {
        for (int i = 0; i < ROBOTS_STRINGS.length; i++) {
            for (int j = 0; j < AGENT_STRINGS.length; j++) {
                testRobots(i, new String[] { AGENT_STRINGS[j] }, TEST_PATHS, ALLOWED[i][j]);
            }
        }
    }

    public void testRobotsTwoAgents() {
        for (int i = 0; i < ROBOTS_STRINGS.length; i++) {
            for (int j = 0; j < AGENT_STRINGS.length; j++) {
                for (int k = 0; k < AGENT_STRINGS.length; k++) {
                    int key = j;
                    if (NOT_IN_ROBOTS_STRING[i][j]) key = k;
                    testRobots(i, new String[] { AGENT_STRINGS[j], AGENT_STRINGS[k] }, TEST_PATHS, ALLOWED[i][key]);
                }
            }
        }
    }

    public void testRobots(int robotsString, String[] agents, String[] paths, boolean[] allowed) {
        String agentsString = agents[0];
        for (int i = 1; i < agents.length; i++) agentsString = agentsString + "," + agents[i];
        RobotRulesParser p = new RobotRulesParser(agents);
        RobotRuleSet rules = p.parseRules(ROBOTS_STRINGS[robotsString] != null ? ROBOTS_STRINGS[robotsString].getBytes() : null);
        for (int i = 0; i < paths.length; i++) {
            assertTrue("testing robots file " + robotsString + ", on agents (" + agentsString + "), and path " + TEST_PATHS[i] + "; got " + rules.isAllowed(TEST_PATHS[i]) + ", rules are: " + LF + rules, rules.isAllowed(TEST_PATHS[i]) == allowed[i]);
        }
    }
}
