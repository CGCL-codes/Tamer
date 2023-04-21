package org.openprojectservices.opsadmin.dao.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openprojectservices.opsadmin.ImplementationTask;
import org.openprojectservices.opsadmin.Person;
import org.openprojectservices.opsadmin.Project;
import org.openprojectservices.opsadmin.ProjectStatus;
import org.openprojectservices.opsadmin.QuoteTask;
import org.openprojectservices.opsadmin.dao.DAOException;
import org.openprojectservices.opsadmin.dao.ProjectConstraint;
import org.openprojectservices.opsadmin.dao.ProjectDAO;
import org.openprojectservices.opsadmin.util.IdDescriptionTuple;

public class MockProjectDao implements ProjectDAO {

    private static Log log = LogFactory.getLog(MockProjectDao.class);

    private final List<Project> mockProjects = new ArrayList<Project>();

    public MockProjectDao() {
        for (int i = 1; i < 15; i++) {
            final Project p = new Project();
            p.setName("TestProject" + i);
            p.setUid("0000-000000000-00000-00" + i);
            p.setManager(new Person("000" + i, "MockPerson Number" + i));
            p.setStartDate(new Date());
            final List<Person> members = new ArrayList<Person>();
            for (int j = i; j < (i + 3); j++) {
                members.add(new Person("000" + j, "MockPerson Number" + j));
            }
            p.setMembers(members);
            ProjectStatus mockStatus;
            if (i % 3 == 0) {
                mockStatus = ProjectStatus.closed;
                p.setEndDate(new Date());
            } else {
                mockStatus = ProjectStatus.active;
            }
            p.setStatus(mockStatus);
            p.setMailFolder("TestProjectMailFolder" + i);
            final List<ImplementationTask> mockiTasks = new ArrayList<ImplementationTask>();
            for (int j = 0; j < 9; j++) {
                mockiTasks.add(new ImplementationTask("MockITask" + j));
            }
            final Iterator<ImplementationTask> iterator = mockiTasks.iterator();
            final Long uur = 1000 * 60 * 60L;
            for (int j = i; j < (i + 3); j++) {
                final QuoteTask qTask = new QuoteTask("MockQTask" + j);
                qTask.setEstimate(30 * uur);
                for (int n = 0; n < 3; n++) {
                    if (iterator.hasNext()) {
                        qTask.addImplementationTask(iterator.next());
                    }
                }
                p.addQuoteTask(qTask);
            }
            mockProjects.add(p);
        }
    }

    public void createProject(final Project project) throws DAOException {
        log.debug("Project would be created now");
    }

    public Project getProject(final String projectID) throws DAOException {
        for (final Project p : mockProjects) {
            if (p.getUid().equals(projectID)) {
                return p;
            }
        }
        return null;
    }

    public boolean isNew(final Project project) throws DAOException {
        if (project.getUid() == null) {
            return true;
        }
        return false;
    }

    public List<Project> listProjects(final ProjectConstraint projectConstraint) {
        final List<Project> result = new ArrayList<Project>();
        final String name = projectConstraint.getProjectName();
        for (final Project project : mockProjects) {
            final Pattern pattern = Pattern.compile(name);
            final Matcher matcher = pattern.matcher(project.getName());
            if (matcher.find()) {
                result.add(project);
            }
        }
        return result;
    }

    public void updateProject(final Project oldProject, final Project newProject) throws DAOException {
        log.debug("Project would be updated now");
    }

    @Override
    public List<IdDescriptionTuple> getProjectNamesAndUids(final ProjectConstraint projectConstraint) throws DAOException {
        final List<IdDescriptionTuple> tuples = new ArrayList<IdDescriptionTuple>();
        for (final Project project : mockProjects) {
            tuples.add(new IdDescriptionTuple(project.getUid(), project.getName()));
        }
        return tuples;
    }

    @Override
    public List<Project> getProjectByMailFolder(final String mailFolder) {
        return getProjectList(mailFolder);
    }

    @Override
    public List<Project> getProjectByName(final String projectName) {
        return getProjectList(projectName);
    }

    private List<Project> getProjectList(final String name) {
        final List<Project> projectList = new ArrayList<Project>();
        if (name.equals("exists")) {
            final Project p = new Project("000", "exists");
            projectList.add(p);
        } else if (name.equals("REDALERT")) {
            final Project p1 = new Project("0001", "REDALERT");
            projectList.add(p1);
            final Project p2 = new Project("0002", "REDALERT");
            projectList.add(p2);
        }
        return projectList;
    }
}
