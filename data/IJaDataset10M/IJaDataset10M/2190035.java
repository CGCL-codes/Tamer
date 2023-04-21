package com.codebitches.spruce.module.bb.test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.codebitches.spruce.module.bb.dao.ICategoryDAO;
import com.codebitches.spruce.module.bb.dao.ITopicDAO;
import com.codebitches.spruce.module.bb.dao.IUserDAO;
import com.codebitches.spruce.module.bb.domain.hibernate.SprucebbCategory;
import com.codebitches.spruce.module.bb.domain.hibernate.SprucebbTopic;
import com.codebitches.spruce.module.bb.domain.hibernate.SprucebbUser;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Stuart Eccles
 */
public class SprucebbDAOTest extends AbstractHibernateTestCase {

    private String contextConfigLocation = "com/codebitches/spruce/module/bb/test/dataaccessContext-hibernate.xml";

    private ApplicationContext ctx;

    public void setUp() throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(getContextConfigLocation());
        this.ctx = ctx;
        super.setUp();
    }

    public void testCategoryDAO() throws Exception {
        ICategoryDAO dao = (ICategoryDAO) ctx.getBean("categoryDAO");
        SprucebbCategory cat = new SprucebbCategory();
        cat.setCatTitle("junittesttitle");
        cat.setCatOrder(2);
        dao.createOrUpdateCategory(cat);
        Long newCatId = cat.getCatId();
        SprucebbCategory newcat = dao.getCategoryById(newCatId.longValue());
        Assert.assertEquals("junittesttitle", newcat.getCatTitle());
        Collection col = dao.getAllCategories();
        Assert.assertNotNull(col);
        Assert.assertTrue(col.size() > 0);
        dao.permanentlyDeleteCategory(newcat);
    }

    public void testForumDAO() throws Exception {
    }

    public void testGroupDAO() throws Exception {
    }

    public void testPostDAO() throws Exception {
    }

    public void testTopicDAO() throws Exception {
        ITopicDAO dao = (ITopicDAO) ctx.getBean("topicDAO");
        SprucebbTopic topic = dao.getTopicById(7);
        SprucebbTopic prevTopic = dao.findPreviousTopicInForum(topic.getTopicLastPost().getPostId().longValue(), topic.getSprucebbForum().getForumId().longValue());
        System.out.println(prevTopic.getTopicId());
    }

    public void testUserDAO() throws Exception {
        IUserDAO dao = (IUserDAO) ctx.getBean("userDAO");
        SprucebbUser user = new SprucebbUser();
        user.setUsername("testjunit");
        user.setUserPassword("testpassword");
        user.setUserTimezone(new BigDecimal(0));
        user.setUserDateformat("ddMMyyy");
        user.setUserLastvisit(new Date());
        user.setUserRegdate(new Date());
        user.setUserActive(true);
        user.setUserActkey("");
        user.setUserAim("dfsdf");
        user.setUserAllowavatar(true);
        user.setUserAllowbbcode(true);
        user.setUserAllowhtml(true);
        user.setUserAllowPm(true);
        user.setUserAllowsmile(true);
        user.setUserAllowViewonline(true);
        user.setUserAttachsig(true);
        user.setUserAvatar("sdf");
        user.setUserAvatarType(3);
        user.setUserEmail("test@junit.com");
        user.setUserEmailtime(new Date());
        user.setUserFrom("dfsfd");
        user.setUserIcq("fdsf");
        user.setUserInterests("sdfsdf");
        user.setUserLang("EN");
        user.setUserLevel(new Integer(1));
        user.setUserMsnm("sfsdf");
        user.setUserPosts(0);
        dao.createOrUpdateUser(user);
    }

    /**
	 * @return Returns the contextConfigLocation.
	 */
    public String getContextConfigLocation() {
        return contextConfigLocation;
    }

    /**
	 * @param contextConfigLocation The contextConfigLocation to set.
	 */
    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    /**
	 * @return Returns the ctx.
	 */
    public ApplicationContext getCtx() {
        return ctx;
    }

    /**
	 * @param ctx The ctx to set.
	 */
    public void setCtx(ApplicationContext ctx) {
        this.ctx = ctx;
    }
}
