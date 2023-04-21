package org.eledge.domain.participants;

import static org.eledge.Eledge.create;
import java.util.HashMap;
import org.apache.cayenne.CayenneDataObject;
import org.eledge.AbstractCDOTest;
import org.eledge.domain.BaseWebPage;
import org.eledge.domain.Course;
import org.eledge.domain.CourseSection;
import org.eledge.domain.User;
import org.eledge.domain.participants.CourseParticipant;
import org.eledge.domain.participants.GenericParticipant;
import org.eledge.domain.participants.GroupParticipant;
import org.eledge.domain.participants.ParticipantManagerConvertor;
import org.eledge.domain.participants.SectionParticipant;
import org.eledge.domain.participants.UserParticipant;
import org.eledge.domain.participants.WebPageCourseParticipant;
import org.eledge.domain.participants.WebPageSectionParticipant;
import org.eledge.domain.participants.WebPageUserParticipant;

/**
 * @author robertz
 * 
 */
public class ParticipantManagerConvertorTest extends AbstractCDOTest {

    BaseWebPage oldPage;

    BaseWebPage newPage;

    HashMap<CayenneDataObject, CayenneDataObject> params;

    User jack;

    User jill;

    Course course1;

    Course course2;

    CourseSection cs11;

    CourseSection cs12;

    CourseSection cs21;

    CourseSection cs22;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        course1 = create(Course.class);
        course2 = create(Course.class);
        cs11 = create(CourseSection.class);
        cs11.setSectionName("Section 1");
        cs11.setCourse(course1);
        cs21 = create(CourseSection.class);
        cs21.setSectionName("Section 1");
        cs21.setCourse(course2);
        cs12 = create(CourseSection.class);
        cs12.setSectionName("Section 2");
        cs12.setCourse(course1);
        cs22 = create(CourseSection.class);
        cs22.setSectionName("Section 2");
        cs22.setCourse(course2);
        jack = create(User.class);
        jill = create(User.class);
        jack.setUserId("jack");
        jill.setUserId("jill");
        oldPage = create(BaseWebPage.class);
        newPage = create(BaseWebPage.class);
        WebPageSectionParticipant p11 = create(WebPageSectionParticipant.class);
        p11.setCourseSection(cs11);
        p11.setWebPage(oldPage);
        WebPageSectionParticipant p12 = create(WebPageSectionParticipant.class);
        p12.setCourseSection(cs12);
        p12.setWebPage(oldPage);
        WebPageUserParticipant upjack = create(WebPageUserParticipant.class);
        upjack.setUser(jack);
        upjack.setWebPage(oldPage);
        WebPageCourseParticipant pc1 = create(WebPageCourseParticipant.class);
        pc1.setCourse(course1);
        pc1.setWebPage(oldPage);
        GenericParticipant gp = new GenericParticipant();
        gp.setWebPage(oldPage);
        params = new HashMap<CayenneDataObject, CayenneDataObject>();
        params.put(cs11, cs21);
        params.put(cs12, cs22);
        params.put(course1, course2);
        params.put(jack, jill);
    }

    public void testDuplicateParticipantsParticipantAssignableParticipantAssignableHashMaptrue() {
        ParticipantManagerConvertor.duplicateParticipants(oldPage, newPage, params, true);
        assertEquals(4, newPage.getParticipantList().size());
        boolean jillseen = false;
        boolean c2seen = false;
        boolean cs21seen = false;
        boolean cs22seen = false;
        boolean gpseen = false;
        for (GenericParticipant gp : newPage.getParticipantList()) {
            assertFalse(gp instanceof GroupParticipant);
            if (gp instanceof UserParticipant) {
                assertEquals(jill, gp.getParticipant());
                jillseen = true;
            } else if (gp instanceof CourseParticipant) {
                assertEquals(course2, gp.getParticipant());
                c2seen = true;
            } else if (gp instanceof SectionParticipant) {
                assertTrue(gp.getParticipant().equals(cs21) || gp.getParticipant().equals(cs22));
                if (gp.getParticipant().equals(cs21)) {
                    cs21seen = true;
                } else {
                    cs22seen = true;
                }
            } else {
                gpseen = true;
                assertNotNull(gp.getWebPage());
            }
        }
        assertFalse(jillseen);
        assertTrue(c2seen);
        assertTrue(cs21seen);
        assertTrue(cs22seen);
        assertTrue(gpseen);
    }

    public void testDuplicateParticipantsParticipantAssignableParticipantAssignableHashMapfalse() {
        assertEquals(5, oldPage.getParticipantList().size());
        ParticipantManagerConvertor.duplicateParticipants(oldPage, newPage, params, false);
        assertEquals(5, newPage.getParticipantList().size());
        boolean jillseen = false;
        boolean c2seen = false;
        boolean cs21seen = false;
        boolean cs22seen = false;
        for (GenericParticipant gp : newPage.getParticipantList()) {
            if (gp instanceof UserParticipant) {
                assertEquals(jill, gp.getParticipant());
                jillseen = true;
            } else if (gp instanceof CourseParticipant) {
                assertEquals(course2, gp.getParticipant());
                c2seen = true;
            } else if (gp instanceof SectionParticipant) {
                assertTrue(gp.getParticipant().equals(cs21) || gp.getParticipant().equals(cs22));
                if (gp.getParticipant().equals(cs21)) {
                    cs21seen = true;
                } else {
                    cs22seen = true;
                }
            }
        }
        assertTrue(jillseen);
        assertTrue(c2seen);
        assertTrue(cs21seen);
        assertTrue(cs22seen);
    }

    public void testDuplicateParticipantsParticipantAssignableParticipantAssignabletrue() {
        ParticipantManagerConvertor.duplicateParticipants(oldPage, newPage, true);
        assertEquals(4, newPage.getParticipantList().size());
        boolean jackseen = false;
        boolean c1seen = false;
        boolean cs11seen = false;
        boolean cs12seen = false;
        for (GenericParticipant gp : newPage.getParticipantList()) {
            if (gp instanceof UserParticipant) {
                assertEquals(jack, gp.getParticipant());
                jackseen = true;
            } else if (gp instanceof CourseParticipant) {
                assertEquals(course1, gp.getParticipant());
                c1seen = true;
            } else if (gp instanceof SectionParticipant) {
                assertTrue(gp.getParticipant().equals(cs11) || gp.getParticipant().equals(cs12));
                if (gp.getParticipant().equals(cs11)) {
                    cs11seen = true;
                } else {
                    cs12seen = true;
                }
            }
        }
        assertFalse(jackseen);
        assertTrue(c1seen);
        assertTrue(cs11seen);
        assertTrue(cs12seen);
    }

    public void testDuplicateParticipantsParticipantAssignableParticipantAssignablefalse() {
        ParticipantManagerConvertor.duplicateParticipants(oldPage, newPage, false);
        assertEquals(5, newPage.getParticipantList().size());
        boolean jackseen = false;
        boolean c1seen = false;
        boolean cs11seen = false;
        boolean cs12seen = false;
        for (GenericParticipant gp : newPage.getParticipantList()) {
            if (gp instanceof UserParticipant) {
                assertEquals(jack, gp.getParticipant());
                jackseen = true;
            } else if (gp instanceof CourseParticipant) {
                assertEquals(course1, gp.getParticipant());
                c1seen = true;
            } else if (gp instanceof SectionParticipant) {
                assertTrue(gp.getParticipant().equals(cs11) || gp.getParticipant().equals(cs12));
                if (gp.getParticipant().equals(cs11)) {
                    cs11seen = true;
                } else {
                    cs12seen = true;
                }
            }
        }
        assertTrue(jackseen);
        assertTrue(c1seen);
        assertTrue(cs11seen);
        assertTrue(cs12seen);
    }
}
