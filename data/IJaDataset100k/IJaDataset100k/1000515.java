package scrum.server.project;

import ilarkesto.core.base.Str;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import scrum.client.common.LabelSupport;
import scrum.client.common.ReferenceSupport;
import scrum.server.admin.User;
import scrum.server.common.Numbered;
import scrum.server.estimation.RequirementEstimationVote;
import scrum.server.journal.Change;
import scrum.server.journal.ChangeDao;
import scrum.server.sprint.Sprint;
import scrum.server.sprint.Task;

public class Requirement extends GRequirement implements Numbered, ReferenceSupport, LabelSupport {

    private transient Comparator<Task> tasksOrderComparator;

    private static transient ChangeDao changeDao;

    public static void setChangeDao(ChangeDao changeDao) {
        Requirement.changeDao = changeDao;
    }

    public String getHistoryLabel(final Sprint sprint) {
        if (sprint == null) return getLabel();
        Set<Change> changes = changeDao.getChangesByParent(this);
        for (Change change : changes) {
            String key = change.getKey();
            if (!change.isNewValue(sprint.getId())) continue;
            if (scrum.client.journal.Change.REQ_COMPLETED_IN_SPRINT.equals(key) || scrum.client.journal.Change.REQ_REJECTED_IN_SPRINT.equals(key)) return change.getOldValue();
        }
        return getLabel();
    }

    public Set<Change> getSprintSwitchChanges() {
        Set<Change> changes = changeDao.getChangesByParent(this);
        Iterator<Change> iterator = changes.iterator();
        while (iterator.hasNext()) {
            Change change = iterator.next();
            if (change.isKey(scrum.client.journal.Change.REQ_COMPLETED_IN_SPRINT)) continue;
            if (change.isKey(scrum.client.journal.Change.REQ_REJECTED_IN_SPRINT)) continue;
            iterator.remove();
        }
        return changes;
    }

    public List<Task> getTasksAsList() {
        List<Task> tasks = new ArrayList<Task>(getTasksInSprint());
        Collections.sort(tasks, getTasksOrderComparator());
        return tasks;
    }

    public Set<Task> getTasksInSprint() {
        return getTasksInSprint(getProject().getCurrentSprint());
    }

    public Set<Task> getTasksInSprint(Sprint sprint) {
        Set<Task> tasks = getTasks();
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.isClosedInPastSprintSet() || !task.isSprint(sprint)) iterator.remove();
        }
        return tasks;
    }

    public String getWorkDescriptionAsString() {
        if (!isInCurrentSprint()) return Str.appendIfNotBlank(getEstimatedWorkAsString(), "SP");
        int remaining = getRemainingWork();
        int burned = getBurnedWork();
        int total = remaining + burned;
        if (total == 0) return null;
        float percent = burned * 100f / total;
        return burned + "/" + total + " hrs. (" + Math.round(percent) + "%), " + getEstimatedWorkAsString() + " SP";
    }

    public int getTotalWork() {
        return getRemainingWork() + getBurnedWork();
    }

    public int getRemainingWork() {
        int work = 0;
        for (Task task : getTasksInSprint()) {
            work += task.getRemainingWork();
        }
        return work;
    }

    public int getBurnedWork() {
        int work = 0;
        for (Task task : getTasksInSprint()) {
            work += task.getBurnedWork();
        }
        return work;
    }

    public String getEstimatedWorkAsString() {
        Float work = getEstimatedWork();
        if (work == null) return null;
        if (work <= 0.5f) return work.toString();
        return String.valueOf(work.intValue());
    }

    public void setEstimatedWorkAsString(String value) {
        if (Str.isBlank(value)) {
            setEstimatedWork(null);
            return;
        }
        setEstimatedWork(Float.parseFloat(value));
    }

    public boolean isInCurrentSprint() {
        if (!isSprintSet()) return false;
        return isSprint(getProject().getCurrentSprint());
    }

    public void initializeEstimationVotes() {
        for (User user : getProject().getTeamMembers()) {
            RequirementEstimationVote vote = getEstimationVote(user);
            if (vote == null) vote = requirementEstimationVoteDao.postVote(this, user);
            vote.setEstimatedWork(null);
        }
    }

    private RequirementEstimationVote getEstimationVote(User user) {
        return requirementEstimationVoteDao.getRequirementEstimationVoteByUser(this, user);
    }

    public Set<RequirementEstimationVote> getEstimationVotes() {
        return requirementEstimationVoteDao.getRequirementEstimationVotesByRequirement(this);
    }

    public void clearEstimationVotes() {
        for (RequirementEstimationVote vote : getEstimationVotes()) {
            requirementEstimationVoteDao.deleteEntity(vote);
        }
    }

    public boolean isTasksClosed() {
        for (Task task : getTasksInSprint()) {
            if (!task.isClosed()) return false;
        }
        return true;
    }

    public String getReferenceAndLabel() {
        StringBuilder sb = new StringBuilder();
        sb.append(getReference());
        sb.append(" ");
        sb.append(getLabel());
        if (!isThemesEmpty()) {
            sb.append(" (").append(getThemesAsCommaSeparatedString()).append(")");
        }
        return sb.toString();
    }

    @Override
    public String getReference() {
        return scrum.client.project.Requirement.REFERENCE_PREFIX + getNumber();
    }

    @Override
    public void updateNumber() {
        if (getNumber() == 0) setNumber(getProject().generateRequirementNumber());
    }

    public Comparator<Task> getTasksOrderComparator() {
        if (tasksOrderComparator == null) tasksOrderComparator = new Comparator<Task>() {

            @Override
            public int compare(Task a, Task b) {
                List<String> order = getTasksOrderIds();
                int additional = order.size();
                int ia = order.indexOf(a.getId());
                if (ia < 0) {
                    ia = additional;
                    additional++;
                }
                int ib = order.indexOf(b.getId());
                if (ib < 0) {
                    ib = additional;
                    additional++;
                }
                return ia - ib;
            }
        };
        return tasksOrderComparator;
    }

    @Override
    public void ensureIntegrity() {
        super.ensureIntegrity();
        updateNumber();
        if (isClosed() || isSprintSet()) clearEstimationVotes();
    }

    @Override
    public boolean isVisibleFor(User user) {
        return getProject().isVisibleFor(user);
    }

    @Override
    public String toString() {
        return getReferenceAndLabel();
    }

    public List<Task> getClosedTasksAsList() {
        List<Task> tasks = new ArrayList<Task>();
        for (Task task : getTasksInSprint()) {
            if (task.isClosed()) tasks.add(task);
        }
        Collections.sort(tasks, getTasksOrderComparator());
        return tasks;
    }

    public List<Task> getOpenTasksAsList() {
        List<Task> tasks = new ArrayList<Task>();
        for (Task task : getTasksInSprint()) {
            if (!task.isClosed()) tasks.add(task);
        }
        Collections.sort(tasks, getTasksOrderComparator());
        return tasks;
    }
}
