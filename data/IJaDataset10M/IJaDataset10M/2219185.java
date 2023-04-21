package com.sptci.cms.admin.jcr;

import com.sptci.cms.NodeTypes;
import com.sptci.cms.Nodes;
import static com.sptci.cms.admin.jcr.AllTests.session;
import static com.sptci.cms.admin.jcr.TemplateRepository.FOLDER_TYPE;
import static com.sptci.cms.admin.jcr.TemplateRepository.TOOL_TIP;
import static com.sptci.cms.admin.jcr.TemplateRepository.templateRepository;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.jcr.Node;
import java.io.FileOutputStream;
import static java.lang.String.format;
import java.util.Collection;

/**
 * Unit test suite for the {@link TemplateRepository}
 * class that is used to manage template nodes in the repository.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-12-04
 * @version $Id$
 */
@SuppressWarnings({ "StaticNonFinalField" })
public class TemplateRepositoryTestCase extends BaseTestCase {

    static final String TEST_NAME = "unitTestTemplate";

    static Node test;

    @BeforeClass
    public static void createTest() throws Exception {
        test = templateRepository.create(TEST_NAME, session);
    }

    @Test
    public void root() throws Exception {
        final Node root = templateRepository.getRootNode(session);
        assertTrue("Ensure page root node has tooltip", root.hasProperty(TOOL_TIP));
        assertTrue("Ensure page root node is folder type", root.getProperty(FOLDER_TYPE).getBoolean());
    }

    @Test
    public void checkTest() throws Exception {
        checkNode(test, "test template", templateRepository, NodeTypes.template);
    }

    @Test
    public void getTest() throws Exception {
        final Collection<Node> nodes = templateRepository.getTemplate(TEST_NAME, session);
        assertFalse("Ensure test template node found", nodes.isEmpty());
    }

    @AfterClass
    public static void deleteTest() throws Exception {
        templateRepository.delete(test);
        assertTrue("Ensure test template node deleted", templateRepository.getTemplate(TEST_NAME, session).isEmpty());
        export();
    }

    @SuppressWarnings({ "IOResourceOpenedButNotSafelyClosed" })
    private static void export() throws Exception {
        session.exportSystemView(format("/%s", Nodes.templates), new FileOutputStream(format("/tmp/%s.xml", Nodes.templates)), false, false);
    }
}
