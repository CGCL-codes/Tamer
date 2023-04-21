package scrum.server.project;

import ilarkesto.base.time.Date;
import scrum.server.admin.User;
import scrum.server.common.BurndownSnapshot;

public class ProjectSprintSnapshot extends GProjectSprintSnapshot implements Comparable<ProjectSprintSnapshot>, BurndownSnapshot {

    @Override
    public int getBurnedWorkTotal() {
        return getBurnedWork();
    }

    public void update() {
    }

    public boolean isProject(Project project) {
        return isSprintSet() && getSprint().isProject(project);
    }

    public Project getProject() {
        return getSprint().getProject();
    }

    @Override
    public Date getDate() {
        return getSprint().getEnd();
    }

    @Override
    public int compareTo(ProjectSprintSnapshot other) {
        return getDate().compareTo(other.getDate());
    }

    @Override
    public boolean isVisibleFor(User user) {
        return getProject().isVisibleFor(user);
    }

    public boolean isEditableBy(User user) {
        return false;
    }

    @Override
    public String toString() {
        return getDate() + ": " + getBurnedWork() + ", " + getRemainingWork();
    }
}
