package org.jia.ptrack.domain.hibernate;

import java.util.List;
import net.chrisrichardson.ormunit.hibernate.HibernatePersistenceTests;
import org.jia.ptrack.domain.ArtifactType;
import org.jia.ptrack.domain.Operation;
import org.jia.ptrack.domain.Project;
import org.jia.ptrack.domain.User;

public class HibernateLoadAllProjectsExampleTests extends HibernatePersistenceTests<HibernateLoadAllProjectsExampleTests> {

    protected String[] getConfigLocations() {
        return HibernatePTrackTestConstants.PTRACK_APP_CTXS;
    }

    private PtrackDatabaseInitializer initializer;

    public void setInitializer(PtrackDatabaseInitializer initializer) {
        this.initializer = initializer;
    }

    public void testLoadProjectsCreatedByUser() {
        User user = initializer.getProjectInCompleteState().getInitiatedBy();
        txnThis.loadProjects(user);
    }

    void loadProjects(User user) {
        List<Project> projects = getHibernateTemplate().findByNamedQuery(Project.class.getName() + ".findProjectsCreatedByUser", user);
        List<Project> projects2 = getHibernateTemplate().findByNamedQuery(Project.class.getName() + ".findProjectArtifactsCreatedByUser", user);
        for (Project project : projects) {
            System.out.println("===Testing project");
            assertNotNull(project.getStatus().getName());
            List<Operation> history = project.getHistory();
            if (!history.isEmpty()) {
                assertNotNull(history.get(0).getToStatus().getName());
                assertNotNull(history.get(0).getFromStatus().getName());
                assertNotNull(history.get(0).getUser().getFirstName());
            }
            ArtifactType[] types = project.getArtifacts();
            assertNotNull(types);
        }
    }
}
