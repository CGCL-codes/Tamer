package com.sptci.cms.admin.jcr;

import com.sptci.cms.NodeTypes;
import com.sptci.cms.Nodes;
import com.sptci.cms.SectionNodes;
import static com.sptci.cms.admin.jcr.AllTests.session;
import static com.sptci.cms.admin.jcr.SectionRepository.FOLDER_TYPE;
import static com.sptci.cms.admin.jcr.SectionRepository.TOOL_TIP;
import static com.sptci.cms.admin.jcr.SectionRepository.sectionRepository;
import com.sptci.cms.site.model.NodeBean;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.jcr.Node;
import java.io.FileOutputStream;
import static java.lang.String.format;
import java.util.Collection;

/**
 * Unit test suite for the {@link SectionRepository}
 * class that is used to manage section nodes in the repository.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-12-04
 * @version $Id$
 */
@SuppressWarnings({ "StaticNonFinalField" })
public class SectionRepositoryTestCase extends BaseTestCase {

    static final String TEST_NAME = "unitTestSection";

    static final String FOLDER_NAME = "unitTestSectionFolder";

    static Node test;

    static Node folder;

    @BeforeClass
    public static void createTest() throws Exception {
        test = sectionRepository.create(TEST_NAME, session);
        folder = sectionRepository.createFolder(FOLDER_NAME, session);
    }

    @Test
    public void root() throws Exception {
        final Node root = sectionRepository.getRootNode(session);
        assertTrue("Ensure page root node has tooltip", root.hasProperty(TOOL_TIP));
        assertTrue("Ensure page root node is folder type", root.getProperty(FOLDER_TYPE).getBoolean());
    }

    @SuppressWarnings({ "ObjectAllocationInLoop" })
    @Test
    public void standard() throws Exception {
        for (final SectionNodes sn : SectionNodes.values()) {
            final Node node = sectionRepository.getFolder(sn, session);
            checkNodeBean(new NodeBean(node), node);
            checkFolder(node, format("%s section node", sn), sectionRepository, NodeTypes.section);
        }
    }

    @Test
    public void site() throws Exception {
        final Node node = sectionRepository.getFolder(SectionNodes.site, session);
        assertNotNull("Ensure site section found", node);
        assertTrue("Ensure site section has tooltip", node.hasProperty(TOOL_TIP));
    }

    @Test
    public void shared() throws Exception {
        final Node node = sectionRepository.getFolder(SectionNodes.shared, session);
        assertNotNull("Ensure shared section found", node);
        assertTrue("Ensure shared section has tooltip", node.hasProperty(TOOL_TIP));
    }

    @Test
    public void checkTest() throws Exception {
        assertNotNull("Ensure test section node created", test);
        checkNode(test, "test section", sectionRepository, NodeTypes.section);
    }

    @Test
    public void checkFolder() throws Exception {
        assertNotNull("Ensure test section folder created", folder);
        checkFolder(folder, "folder section", sectionRepository, NodeTypes.section);
    }

    @Test
    public void getTest() throws Exception {
        final Collection<Node> nodes = sectionRepository.getSection(TEST_NAME, session);
        assertFalse("Ensure test section node found", nodes.isEmpty());
    }

    @Test
    public void getFolder() throws Exception {
        final Collection<Node> nodes = sectionRepository.getFolders(FOLDER_NAME, session);
        assertFalse("Ensure folder section node found", nodes.isEmpty());
    }

    @AfterClass
    public static void deleteTest() throws Exception {
        sectionRepository.delete(test);
        assertTrue("Ensure test section node deleted", sectionRepository.getSection(TEST_NAME, session).isEmpty());
        export();
    }

    @SuppressWarnings({ "IOResourceOpenedButNotSafelyClosed" })
    private static void export() throws Exception {
        session.exportSystemView(format("/%s", Nodes.sections), new FileOutputStream(format("/tmp/%s.xml", Nodes.sections)), false, false);
    }
}
