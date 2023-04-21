package org.dctmutils.initutils.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dctmutils.common.FileHelper;
import org.dctmutils.common.test.DctmUtilsTestCase;
import org.dctmutils.initutils.xml.Group;
import org.dctmutils.initutils.xml.Groups;
import org.dctmutils.initutils.xml.InitutilsDocument;
import org.dctmutils.initutils.xml.Members;
import org.dctmutils.initutils.xml.User;
import org.dctmutils.initutils.xml.Users;

/**
 * Unit tests for Init Utils XmlBeans.
 * 
 * @author <a href="mailto:luther@dctmutils.org">Luther E. Birdzell</a>
 * @version 1.0
 */
public class XmlBeansTest extends DctmUtilsTestCase {

    private static Log log = LogFactory.getLog(XmlBeansTest.class);

    /**
     * 
     */
    public static final String REFERENCE_XML_FILE = "init-utils-minimal.xml";

    /**
     * Set up the TestSuite.
     * 
     * @return a <code>Test</code> value
     */
    public static Test suite() {
        return new TestSuite(XmlBeansTest.class);
    }

    /**
     * Test parse document.
     * 
     */
    public void testParseDocument() {
        try {
            InitutilsDocument document = InitutilsDocument.Factory.parse(FileHelper.getFileAsStringFromClassPath(REFERENCE_XML_FILE));
            assertNotNull(document);
            log.debug("document = " + document);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    /**
     * Test groups.
     */
    public void testGroups() {
        try {
            InitutilsDocument document = InitutilsDocument.Factory.parse(FileHelper.getFileAsStringFromClassPath(REFERENCE_XML_FILE));
            assertNotNull(document);
            Groups groupsObject = document.getInitutils().getGroups();
            assertNotNull(groupsObject);
            Group[] groups = groupsObject.getGroupArray();
            assertNotNull(groups);
            assertTrue(groups.length >= 1);
            for (int i = 0; i < groups.length; i++) {
                Group group = groups[i];
                String groupName = group.getName();
                assertTrue(StringUtils.isNotBlank(groupName));
                String groupEmail = group.getEmail();
                log.debug("groupEmail = " + groupEmail);
                Members members = group.getMembers();
                assertNotNull(members);
                Users usersObject = members.getUsers();
                Groups nestedGroupsObject = members.getGroups();
                User[] users = usersObject.getUserArray();
                for (int j = 0; j < users.length; j++) {
                    User user = users[j];
                    assertNotNull(user);
                    String userName = user.getUserName();
                    assertTrue(StringUtils.isNotBlank(userName));
                }
                Group[] nestedGroups = nestedGroupsObject.getGroupArray();
                for (int k = 0; k < nestedGroups.length; k++) {
                    Group nestedGroup = nestedGroups[k];
                    assertNotNull(nestedGroup);
                    String nestedGroupName = nestedGroup.getName();
                    assertTrue(StringUtils.isNotBlank(nestedGroupName));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }
}
