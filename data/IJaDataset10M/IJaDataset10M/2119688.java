package org.jia.ptrack.domain.hibernate;

import java.util.Collections;
import java.util.Date;
import net.chrisrichardson.ormunit.hibernate.HibernatePersistenceTests;
import org.jia.ptrack.domain.AuditEntry;
import org.jia.ptrack.domain.AuditEntryRepository;
import org.jia.ptrack.domain.EntityIdAndClass;
import org.jia.ptrack.domain.Project;

public class HibernateAuditEntryRepositoryTests extends HibernatePersistenceTests {

    private AuditEntryRepository auditEntryRepository;

    protected String[] getConfigLocations() {
        return HibernatePTrackTestConstants.PTRACK_APP_CTXS;
    }

    public void setAuditEntryRepository(AuditEntryRepository projectRepository) {
        this.auditEntryRepository = projectRepository;
    }

    public void testAddAuditEntry() {
        AuditEntry auditEntry = new AuditEntry("foo", new Date(), "fooBar", Collections.singleton(new EntityIdAndClass(new Project())));
        auditEntryRepository.add(auditEntry);
        assertNotNull(getHibernateTemplate().get(AuditEntry.class, new Integer(auditEntry.getId())));
    }
}
