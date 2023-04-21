package com.sjt.pi.model;

import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Project extends Identity {

    public enum Progress {

        STARTED(0), FINISHED(1), IN_PROGRESS(2), FREEZE(3);

        int value;

        Progress() {
        }

        Progress(int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }

        public static Progress getProgress(final int idOfProgress) {
            for (Progress p : values()) {
                if (p.getValue() == idOfProgress) {
                    return p;
                }
            }
            throw new IllegalStateException("Can not find a member of Progress enumeration for id [" + idOfProgress + "]");
        }
    }

    private String description;

    private Date start_date;

    private Date end_date;

    private Member owner;

    private Progress progress;

    private List<Discussion> discussions;

    private List<Task> tasks;

    private Map<Role, Map<Action, Permission>> rolePermissions;

    private Map<Member, List<Role>> members;

    public Project() {
        super(0, "");
        description = "";
        progress = progress.STARTED;
    }

    public Project(int id, String name, String description, Date startDate, Date endDate, Member owner, int progress, Map<Member, List<Role>> members, List<Task> tasks, List<Discussion> discussions) {
        super(id, name);
        this.description = description;
        start_date = startDate;
        end_date = endDate;
        setProgress(progress);
        this.members = members;
        this.tasks = tasks;
        this.discussions = discussions;
    }

    public Project(ResultSet rs) throws Exception {
        super(rs.getInt(1), rs.getString(2));
        description = rs.getString(3);
        start_date = rs.getDate(4);
        end_date = rs.getDate(5);
        setProgress(rs.getInt(6));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date startDate) {
        start_date = startDate;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date endDate) {
        end_date = endDate;
    }

    public Member getOwner() {
        return owner;
    }

    /**
	 * 
	 * @param owner
	 *            could be null according to DB design
	 */
    public void setOwner(final Member owner) {
        this.owner = owner;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = Progress.getProgress(progress);
    }

    public List<Discussion> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(List<Discussion> discussions) {
        this.discussions = discussions;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Map<Role, Map<Action, Permission>> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(Map<Role, Map<Action, Permission>> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    public Map<Member, List<Role>> getMembers() {
        return members;
    }

    public void setMembers(Map<Member, List<Role>> members) {
        this.members = members;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}
