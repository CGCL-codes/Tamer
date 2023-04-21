package org.jcr_blog.persistence.dao;

import org.jcr_blog.commons.cdi_test.DeploymentFactory;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Rule;
import javax.inject.Inject;
import javax.jcr.Session;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jcr_blog.commons.logging.LoggerService;
import org.jcr_blog.domain.Blog;
import org.jcr_blog.domain.Category;
import org.jcr_blog.domain.Page;
import org.jcr_blog.domain.Post;
import org.jcr_blog.persistence.jcr.ObjectContentManagerService;
import org.jcr_blog.persistence.jcr.SessionService;
import org.jcr_blog.persistence.qualifiers.JcrSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author Sebastian Prehn <sebastian.prehn@planetswebdesign.de>
 */
@RunWith(Arquillian.class)
public class IntegrationTest {

    @Inject
    private BlogDao blogDao;

    @Inject
    private PageDao pageDao;

    @Inject
    private PostDao postDao;

    @Inject
    private CategoryDao categoryDao;

    @Inject
    @JcrSession
    private Session session;

    private Blog blog;

    private Page page;

    private Post post;

    private Category category;

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationTest.class);

    @Rule
    public MethodRule watchman = new TestWatchman() {

        @Override
        public void starting(FrameworkMethod method) {
            LOGGER.info("running: {}", method.getName());
        }
    };

    public IntegrationTest() {
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return DeploymentFactory.createDeployment(CommentDaoImpl.class, PageDaoImpl.class, BlogDaoImpl.class, PostDaoImpl.class, CategoryDaoImpl.class, ObjectContentManagerService.class, RepositoryServiceMock.class, SessionService.class, LoggerService.class);
    }

    private void setUp() {
        blog = EntityHelper.createBlog(blogDao, "testBlog");
        blogDao.save(blog);
        page = EntityHelper.createPage(pageDao, "testPage");
        pageDao.save(blog, page);
        post = EntityHelper.createPost(postDao, "testPost");
        postDao.save(blog, post);
        category = EntityHelper.createCategory(categoryDao, "testCategory");
        categoryDao.save(blog, category);
    }

    private void tearDown() {
        blogDao.delete(blog);
    }

    /**
     * Test of findByChild method, of class BlogDaoImpl.
     */
    @Test
    public void testFindByChild_Category() {
        setUp();
        Blog b = blogDao.findByChild(category);
        assertNotNull(b);
        assertEquals(blog, b);
        tearDown();
        session.logout();
    }

    /**
     * Test of findByChild method, of class BlogDaoImpl.
     */
    @Test
    public void testFindByChild_Page() {
        setUp();
        Blog b = blogDao.findByChild(page);
        assertNotNull(b);
        assertEquals(blog, b);
        tearDown();
        session.logout();
    }

    /**
     * Test of findByChild method, of class BlogDaoImpl.
     */
    @Test
    public void testFindByChild_Post() {
        setUp();
        Blog b = blogDao.findByChild(post);
        assertNotNull(b);
        assertEquals(blog, b);
        tearDown();
        session.logout();
    }
}
