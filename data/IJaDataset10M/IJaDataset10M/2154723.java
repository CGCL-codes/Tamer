package net.solarnetwork.central.dras.biz.dao.test.ibatis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.solarnetwork.central.dras.biz.dao.DaoEventBiz;
import net.solarnetwork.central.dras.dao.ParticipantGroupDao;
import net.solarnetwork.central.dras.dao.ProgramDao;
import net.solarnetwork.central.dras.domain.EffectiveCollection;
import net.solarnetwork.central.dras.domain.Event;
import net.solarnetwork.central.dras.domain.Member;
import net.solarnetwork.central.dras.domain.Participant;
import net.solarnetwork.central.dras.domain.ParticipantGroup;
import net.solarnetwork.central.dras.support.MembershipCommand;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Test case for {@link DaoEventBiz}.
 * 
 * @author matt
 * @version $Revision: 1533 $
 */
public class DaoEventBizTest extends AbstractTestSupport {

    @Autowired
    private ParticipantGroupDao participantGroupDao;

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private DaoEventBiz eventBiz;

    @Before
    public void setupSecurity() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(TEST_USERNAME, "unittest"));
    }

    @Test
    public void assignMembers() {
        setupTestLocation();
        setupTestProgram(TEST_PROGRAM_ID, TEST_PROGRAM_NAME);
        setupTestParticipant();
        setupTestParticipantGroup();
        setupTestEvent(TEST_EVENT_ID, TEST_PROGRAM_ID);
        Set<Long> memberIds = new HashSet<Long>(1);
        memberIds.add(TEST_PARTICIPANT_ID);
        programDao.assignParticipantMembers(TEST_PROGRAM_ID, memberIds, TEST_EFFECTIVE_ID);
        MembershipCommand participants = new MembershipCommand();
        participants.getGroup().addAll(memberIds);
        participantGroupDao.assignParticipantMembers(TEST_PARTICIPANT_GROUP_ID, memberIds, TEST_EFFECTIVE_ID);
        memberIds.clear();
        memberIds.add(TEST_PARTICIPANT_GROUP_ID);
        MembershipCommand groups = new MembershipCommand();
        groups.getGroup().addAll(memberIds);
        EffectiveCollection<Event, Member> result = eventBiz.assignMembers(TEST_EVENT_ID, participants, groups);
        assertNotNull(result);
        assertNotNull(result.getEffective());
        assertNotNull(result.getObject());
        assertEquals(TEST_EVENT_ID, result.getObject().getId());
        assertNotNull(result.getCollection());
        assertEquals(2, result.getCollection().size());
        Map<String, Collection<Long>> mapping = result.getMemberMap();
        assertEquals(2, mapping.size());
        Collection<Long> foundParticipants = mapping.get(Participant.class.getSimpleName());
        assertNotNull(foundParticipants);
        assertEquals(1, foundParticipants.size());
        Collection<Long> foundParticipantGroups = mapping.get(ParticipantGroup.class.getSimpleName());
        assertNotNull(foundParticipantGroups);
        assertEquals(1, foundParticipantGroups.size());
        assertTrue(foundParticipants.contains(TEST_PARTICIPANT_ID));
        assertTrue(foundParticipantGroups.contains(TEST_PARTICIPANT_GROUP_ID));
    }
}
