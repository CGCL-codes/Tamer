package com.liferay.portlet.polls.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.service.persistence.BasePersistenceTestCase;
import com.liferay.portlet.polls.NoSuchChoiceException;
import com.liferay.portlet.polls.model.PollsChoice;

/**
 * <a href="PollsChoicePersistenceTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PollsChoicePersistenceTest extends BasePersistenceTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        _persistence = (PollsChoicePersistence) PortalBeanLocatorUtil.locate(PollsChoicePersistence.class.getName() + ".impl");
    }

    public void testCreate() throws Exception {
        long pk = nextLong();
        PollsChoice pollsChoice = _persistence.create(pk);
        assertNotNull(pollsChoice);
        assertEquals(pollsChoice.getPrimaryKey(), pk);
    }

    public void testRemove() throws Exception {
        PollsChoice newPollsChoice = addPollsChoice();
        _persistence.remove(newPollsChoice);
        PollsChoice existingPollsChoice = _persistence.fetchByPrimaryKey(newPollsChoice.getPrimaryKey());
        assertNull(existingPollsChoice);
    }

    public void testUpdateNew() throws Exception {
        addPollsChoice();
    }

    public void testUpdateExisting() throws Exception {
        long pk = nextLong();
        PollsChoice newPollsChoice = _persistence.create(pk);
        newPollsChoice.setUuid(randomString());
        newPollsChoice.setQuestionId(nextLong());
        newPollsChoice.setName(randomString());
        newPollsChoice.setDescription(randomString());
        _persistence.update(newPollsChoice, false);
        PollsChoice existingPollsChoice = _persistence.findByPrimaryKey(newPollsChoice.getPrimaryKey());
        assertEquals(existingPollsChoice.getUuid(), newPollsChoice.getUuid());
        assertEquals(existingPollsChoice.getChoiceId(), newPollsChoice.getChoiceId());
        assertEquals(existingPollsChoice.getQuestionId(), newPollsChoice.getQuestionId());
        assertEquals(existingPollsChoice.getName(), newPollsChoice.getName());
        assertEquals(existingPollsChoice.getDescription(), newPollsChoice.getDescription());
    }

    public void testFindByPrimaryKeyExisting() throws Exception {
        PollsChoice newPollsChoice = addPollsChoice();
        PollsChoice existingPollsChoice = _persistence.findByPrimaryKey(newPollsChoice.getPrimaryKey());
        assertEquals(existingPollsChoice, newPollsChoice);
    }

    public void testFindByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        try {
            _persistence.findByPrimaryKey(pk);
            fail("Missing entity did not throw NoSuchChoiceException");
        } catch (NoSuchChoiceException nsee) {
        }
    }

    public void testFetchByPrimaryKeyExisting() throws Exception {
        PollsChoice newPollsChoice = addPollsChoice();
        PollsChoice existingPollsChoice = _persistence.fetchByPrimaryKey(newPollsChoice.getPrimaryKey());
        assertEquals(existingPollsChoice, newPollsChoice);
    }

    public void testFetchByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        PollsChoice missingPollsChoice = _persistence.fetchByPrimaryKey(pk);
        assertNull(missingPollsChoice);
    }

    protected PollsChoice addPollsChoice() throws Exception {
        long pk = nextLong();
        PollsChoice pollsChoice = _persistence.create(pk);
        pollsChoice.setUuid(randomString());
        pollsChoice.setQuestionId(nextLong());
        pollsChoice.setName(randomString());
        pollsChoice.setDescription(randomString());
        _persistence.update(pollsChoice, false);
        return pollsChoice;
    }

    private PollsChoicePersistence _persistence;
}
