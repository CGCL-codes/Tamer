package vqwiki.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import vqwiki.WikiBase;
import vqwiki.WikiMember;
import vqwiki.utils.Utilities;

/**
 * Test for FileWikiMembers
 *
 * @author mteodori
 */
public class FileWikiMembersTest extends AbstractFileTest {

    private static final Logger logger = Logger.getLogger(FileWikiMembersTest.class.getName());

    private FileWikiMembers fileWikiMembers;

    public void testNewParser() throws Exception {
        String filename = getClass().getResource("/member-newformat.xml").getFile();
        List members = FileWikiMembers.parseXml(new File(filename));
        WikiMember member = (WikiMember) members.get(0);
        assertEquals("tizio", member.getUserName());
        assertEquals("tizio@example.com", member.getEmail());
        assertEquals("CFHWWGVQQTSWCEJPBCQI", member.getKey());
        member = (WikiMember) members.get(1);
        assertEquals("caio", member.getUserName());
        assertEquals("caio@example.com", member.getEmail());
        assertEquals("AJTMUKJRDXYGXGFLTMJU", member.getKey());
        member = (WikiMember) members.get(2);
        assertEquals("sempronio", member.getUserName());
        assertEquals("sempronio@example.com", member.getEmail());
        assertEquals("", member.getKey());
    }

    public void testNewSerializer() throws Exception {
        String filename = getClass().getResource("/member-newformat.xml").getFile();
        String changesXmlData = FileUtils.readFileToString(new File(filename), "UTF-8");
        List members = new ArrayList();
        members.add(new WikiMember("tizio", "tizio@example.com", "CFHWWGVQQTSWCEJPBCQI"));
        members.add(new WikiMember("caio", "caio@example.com", "AJTMUKJRDXYGXGFLTMJU"));
        members.add(new WikiMember("sempronio", "sempronio@example.com", ""));
        String serialized = FileWikiMembers.serializeXml(members);
        assertEquals(changesXmlData, serialized);
    }

    public void testSerializer() throws Exception {
        fileWikiMembers = new FileWikiMembers(WikiBase.DEFAULT_VWIKI);
        fileWikiMembers.requestMembership("username", "user@example.com", new MockHttpServletRequest());
        File serializedFile = FileHandler.getPathFor(WikiBase.DEFAULT_VWIKI, FileWikiMembers.MEMBER_FILE);
        String serialized = FileUtils.readFileToString(serializedFile, "UTF-8");
        logger.fine(serialized);
        assertTrue(serialized.indexOf("username") > -1);
        fileWikiMembers = new FileWikiMembers(WikiBase.DEFAULT_VWIKI);
        WikiMember wm = fileWikiMembers.findMemberByName("username");
        assertNotNull(wm);
        assertEquals("username", wm.getUserName());
        assertEquals("user@example.com", wm.getEmail());
        assertNull(wm.getPassword());
        assertNotNull(wm.getKey());
    }

    public void testParser() throws Exception {
        String filename = getClass().getResource("/" + FileWikiMembers.MEMBER_FILE).getFile();
        File file = new File(filename);
        assertTrue(Utilities.isJsxFile(file));
        Map map = FileWikiMembers.parseJsx(file);
        logger.info("loaded map: " + map);
    }
}
