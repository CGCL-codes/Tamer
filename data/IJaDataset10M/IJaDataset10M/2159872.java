package org.helianto.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Date;
import org.helianto.core.Entity;
import org.helianto.core.def.PrivacyLevel;
import org.helianto.core.def.Resolution;
import org.helianto.core.test.EntityTestSupport;
import org.helianto.document.base.AbstractJournal;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class JournalTests {

    @Test
    public void defaultConstructor() {
        Date issueDate = new Date();
        journal.setIssueDate(issueDate);
        assertEquals("SUMMARY", journal.getSummary());
        assertEquals(Resolution.PRELIMINARY.getValue(), journal.getResolution());
        assertEquals(PrivacyLevel.PUBLIC.getValue(), journal.getPrivacyLevel());
        assertEquals(' ', journal.getPriority());
        assertTrue(journal.getNextCheckDate() instanceof Date);
    }

    @Test
    public void actualDuration() {
        journal.setActualStartDate(new Date(1000000));
        journal.setActualEndDate(new Date(2000000));
        journal.setActualStartDate(null);
        journal.setActualEndDate(null);
        journal.setActualStartDate(new Date(500000));
        journal.setActualEndDate(new Date(2500000));
        assertEquals(2000000, journal.getActualDuration());
    }

    private AbstractJournal journal;

    @Before
    public void setUp() {
        journal = new EventStub();
        Entity entity = EntityTestSupport.createEntity();
        journal.setEntity(entity);
    }

    @SuppressWarnings("serial")
    public class EventStub extends AbstractJournal {

        @Override
        public String getSummary() {
            return "SUMMARY";
        }

        public String getInternalNumberKey() {
            return null;
        }
    }
}
