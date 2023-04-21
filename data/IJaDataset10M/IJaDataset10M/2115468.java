package com.triplea.rolap.plugins.acl.DefaultTests;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import org.apache.log4j.PropertyConfigurator;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.triplea.rolap.plugins.acl.Default;
import com.triplea.rolap.plugins.acl.Rule;
import junit.framework.JUnit4TestAdapter;

/**
 * @author kononyhin
 *
 */
public class DefaultTest {

    private static String _configFile = "plugins/PluginACLDefault.xml";

    static Mockery _context = new JUnit4Mockery();

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DefaultTest.class);
    }

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        PropertyConfigurator.configure("log4j.properties");
    }

    @Before
    public void setUp() throws Exception {
        String configFileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<rules>" + "	<database name=\"db1\">" + "		<cube name=\"cube1\">" + "			<readRules>" + "				<rule type=\"deny\">" + "					<objects>" + "						<user name=\"user1\"/>" + "						<group name=\"group1\"/>" + "					</objects>" + "					<subject>" + "						<dimension name=\"dim1\"> " + "							<element name=\"dim1_elem1\"/>" + "							<element name=\"dim1_elem2\"/>" + "						</dimension>" + "						<dimension name=\"dim2\"/>" + "					</subject>" + "				</rule>" + "				<rule type=\"deny\">" + "					<objects>" + "						<user name=\"user2\"/>" + "					</objects>" + "					<subject>" + "						<dimension name=\"dim2\"/> " + "						<dimension name=\"dim3\"/>" + "					</subject>" + "				</rule>" + "			</readRules>" + "			<writeRules>" + "				<rule type=\"deny\">" + "					<objects>" + "						<user name=\"user3\"/>" + "					</objects>" + "					<subject>" + "						<dimension name=\"dim1\"/> " + "						<dimension name=\"dim3\"/>" + "					</subject>" + "				</rule>" + "			</writeRules>" + "		</cube>" + "	</database>" + "</rules>";
        File configFile = new File(DefaultTest._configFile);
        configFile.delete();
        configFile.createNewFile();
        FileWriter writer = new FileWriter(configFile);
        writer.write(configFileContent);
        writer.flush();
        writer.close();
    }

    @After
    public void tearDown() throws Exception {
        File configFile = new File(DefaultTest._configFile);
        configFile.delete();
    }

    @Test
    public void checkReadRulesCount() {
        Default acl = new Default();
        ArrayList<Rule> rules = acl.getReadRules("db1", "cube1");
        assertEquals("Incorrect readRules count!", 2, rules.size());
    }

    @Test
    public void checkWriteRulesCount() {
        Default acl = new Default();
        ArrayList<Rule> rules = acl.getWriteRules("db1", "cube1");
        assertEquals("Incorrect writeRules count!", 1, rules.size());
    }

    @Test
    public void addReadRuleToUnknownDB() {
        String databaseName = "unknown_db";
        String cubeName = "unknown_cube";
        Default acl = new Default();
        Rule newRule = Rule.createAllowRule("newRule", "description of new rule");
        acl.addReadRule(databaseName, cubeName, newRule);
        ArrayList<Rule> readRules = acl.getReadRules(databaseName, cubeName);
        assertTrue("New rule hasn't been added", readRules.contains(newRule));
    }

    @Test
    public void addReadRuleToKnownDBAndUnknownCube() {
        String databaseName = "db1";
        String cubeName = "unknown_cube";
        Default acl = new Default();
        Rule newRule = Rule.createAllowRule("newRule", "description of new rule");
        acl.addReadRule(databaseName, cubeName, newRule);
        ArrayList<Rule> readRules = acl.getReadRules(databaseName, cubeName);
        assertTrue("New rule hasn't been added", readRules.contains(newRule));
    }

    @Test
    public void addReadRuleToKnownDBAndCube() {
        String databaseName = "db1";
        String cubeName = "cube1";
        Default acl = new Default();
        Rule newRule = Rule.createAllowRule("newRule", "description of new rule");
        acl.addReadRule(databaseName, cubeName, newRule);
        ArrayList<Rule> readRules = acl.getReadRules(databaseName, cubeName);
        assertTrue("New rule hasn't been added", readRules.contains(newRule));
    }

    @Test
    public void addWriteRuleToUnknownDB() {
        String databaseName = "unknown_db";
        String cubeName = "unknown_cube";
        Default acl = new Default();
        Rule newRule = Rule.createAllowRule("newRule", "description of new rule");
        acl.addWriteRule(databaseName, cubeName, newRule);
        ArrayList<Rule> writeRules = acl.getWriteRules(databaseName, cubeName);
        assertTrue("New rule hasn't been added", writeRules.contains(newRule));
    }

    @Test
    public void addWriteRuleToKnownDBAndUnknownCube() {
        String databaseName = "db1";
        String cubeName = "unknown_cube";
        Default acl = new Default();
        Rule newRule = Rule.createAllowRule("newRule", "description of new rule");
        acl.addWriteRule(databaseName, cubeName, newRule);
        ArrayList<Rule> writeRules = acl.getWriteRules(databaseName, cubeName);
        assertTrue("New rule hasn't been added", writeRules.contains(newRule));
    }

    @Test
    public void addWriteRuleToKnownDBAndCube() {
        String databaseName = "db1";
        String cubeName = "cube1";
        Default acl = new Default();
        Rule newRule = Rule.createAllowRule("newRule", "description of new rule");
        acl.addWriteRule(databaseName, cubeName, newRule);
        ArrayList<Rule> writeRules = acl.getWriteRules(databaseName, cubeName);
        assertTrue("New rule hasn't been added", writeRules.contains(newRule));
    }

    @Test
    public void deleteReadRule() {
        String databaseName = "db1";
        String cubeName = "cube1";
        Default acl = new Default();
        ArrayList<Rule> readRules = acl.getReadRules(databaseName, cubeName);
        Rule ruleToDelete = readRules.get(0);
        acl.deleteReadRule(databaseName, cubeName, ruleToDelete);
        readRules = acl.getReadRules(databaseName, cubeName);
        assertFalse("Rule hasn't been deleted", readRules.contains(ruleToDelete));
    }

    @Test
    public void deleteWriteRule() {
        String databaseName = "db1";
        String cubeName = "cube1";
        Default acl = new Default();
        ArrayList<Rule> writeRules = acl.getWriteRules(databaseName, cubeName);
        Rule ruleToDelete = writeRules.get(0);
        acl.deleteWriteRule(databaseName, cubeName, ruleToDelete);
        writeRules = acl.getWriteRules(databaseName, cubeName);
        assertFalse("Rule hasn't been deleted", writeRules.contains(ruleToDelete));
    }

    @Test
    public void saveParameters() {
        Default pluginBefore = new Default();
        ArrayList<Rule> readRules = pluginBefore.getReadRules("db1", "cube1");
        for (int i = 0; i < readRules.size(); i++) {
            readRules.get(i).setName("readRule" + i);
            readRules.get(i).setDescription("description of readRule" + i);
            ArrayList<String> users = readRules.get(i).getUsers();
            users.add("new_user");
            readRules.get(i).setUsers(users);
            ArrayList<String> groups = readRules.get(i).getGroups();
            users.add("new_group");
            readRules.get(i).setGroups(groups);
            if (readRules.get(i).isAllowType()) {
                readRules.get(i).setDenyType();
            } else {
                readRules.get(i).setAllowType();
            }
        }
        pluginBefore.saveParameters();
        Default pluginAfter = new Default();
        assertEquals("Amount of saved readRules for db1/cube1 differ!", pluginBefore.getReadRules("db1", "cube1").size(), pluginAfter.getReadRules("db1", "cube1").size());
        for (int i = 0; i < pluginAfter.getReadRules("db1", "cube1").size(); i++) {
            assertEquals("Different names", pluginBefore.getReadRules("db1", "cube1").get(i).getName(), pluginAfter.getReadRules("db1", "cube1").get(i).getName());
            assertEquals("Different descriptions", pluginBefore.getReadRules("db1", "cube1").get(i).getDescription(), pluginAfter.getReadRules("db1", "cube1").get(i).getDescription());
            assertArrayEquals("Different users", pluginBefore.getReadRules("db1", "cube1").get(i).getUsers().toArray(), pluginAfter.getReadRules("db1", "cube1").get(i).getUsers().toArray());
            assertArrayEquals("Different groups", pluginBefore.getReadRules("db1", "cube1").get(i).getGroups().toArray(), pluginAfter.getReadRules("db1", "cube1").get(i).getGroups().toArray());
            assertEquals("Different types", pluginBefore.getReadRules("db1", "cube1").get(i).isAllowType(), pluginAfter.getReadRules("db1", "cube1").get(i).isAllowType());
        }
    }
}
