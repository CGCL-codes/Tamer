package com.gotloop.test.dao;

import static org.junit.Assert.assertTrue;
import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gotloop.dao.CommentDAO;
import com.gotloop.dao.LoopDAO;

/**
 * Test the Comment Data Access Object.
 * @author jibhaine
 *
 */
public class CommentDAOTest extends AbstractDAOTest {

    /** Logger for CommentDAOTest. */
    private static final Logger LOG = LoggerFactory.getLogger(CommentDAOTest.class);

    /**
	 * Comment Data Access Object.
	 */
    @Resource
    private CommentDAO commentDao;

    /**
	 * Loop Data Access Object.
	 */
    @Resource
    private LoopDAO loopDao;

    /**
	 * sets up data.
	 */
    @Before
    public void setUp() {
    }

    @Test
    @Override
    public void testSave() {
        assertTrue(false);
    }

    @Test
    @Override
    public void testUpdate() {
        assertTrue(false);
    }

    @Test
    @Override
    public void testDelete() {
        assertTrue(false);
    }

    @Test
    @Override
    public void testGetById() {
        assertTrue(false);
    }

    @Test
    @Override
    public void testGetAll() {
        assertTrue(false);
    }
}
