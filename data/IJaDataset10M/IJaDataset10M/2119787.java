package rtm4java.impl.task;

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import rtm4java.Identifier;
import rtm4java.List;
import rtm4java.Note;
import rtm4java.Priority;
import rtm4java.Tag;
import rtm4java.impl.DomainObject;
import rtm4java.location.Location;
import rtm4java.task.Task;

public class TaskImpl extends DomainObject implements Task {

    private TaskSeries _taskSeries;

    private Date _dueDate;

    private String _estimate;

    private boolean _hasDueTime;

    private Date _dateAdded;

    private Date _completed;

    private Date _deleted;

    private Priority _priority;

    private int _postponed;

    public static Task newTask(String name, Date created, Date modified, String source, URL url, Location location, String recurrence, Priority priority, Collection<Tag> tags, Collection<Note> notes, Date dueDate, String estimate, boolean hasDueTime, Date dateAdded, Date completed, Date deleted, int postponed) {
        TaskSeries taskSeries = new TaskSeries(null, name, created, modified, source, url, location, recurrence, tags, notes);
        TaskImpl task = new TaskImpl(null, taskSeries, dueDate, estimate, hasDueTime, dateAdded, completed, deleted, priority, postponed);
        task.markNew();
        return task;
    }

    public TaskImpl(Identifier id, TaskSeries taskSeries, Date dueDate, String estimate, boolean hasDueTime, Date dateAdded, Date completed, Date deleted, Priority priority, int postponed) {
        super(id);
        _taskSeries = taskSeries;
        _dueDate = dueDate;
        _estimate = estimate;
        _hasDueTime = hasDueTime;
        _dateAdded = dateAdded;
        _completed = completed;
        _deleted = deleted;
        _priority = priority;
        _postponed = postponed;
        markClean();
    }

    /**
	 * Derived classes may use this to create an empty task
	 */
    protected TaskImpl(Identifier id) {
        super(id);
        markClean();
    }

    @Override
    public void addTags(Collection<Tag> tags) {
        markDirty();
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void complete() {
        markDirty();
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void delete() {
        markDirty();
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public Date getDateAdded() {
        return _dateAdded;
    }

    @Override
    public Date getDueDate() {
        return _dueDate;
    }

    @Override
    public String getEstimate() {
        return _estimate;
    }

    @Override
    public Location getLocation() {
        if (null == _taskSeries) return null;
        return _taskSeries.getLocation();
    }

    @Override
    public int getPostponed() {
        return _postponed;
    }

    @Override
    public Priority getPriority() {
        return _priority;
    }

    @Override
    public URL getURL() {
        if (null == _taskSeries) return null;
        return _taskSeries.getURL();
    }

    @Override
    public boolean hasDueTime() {
        return _hasDueTime;
    }

    @Override
    public boolean isCompleted() {
        return (null != _completed);
    }

    @Override
    public boolean isDeleted() {
        return (null != _deleted);
    }

    @Override
    public void moveTo(List other) {
        markDirty();
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void postpone() {
        markDirty();
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void removeTags(Collection<Tag> tags) {
        markDirty();
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void setEstimate(String e) {
        markDirty();
        _estimate = e;
    }

    @Override
    public void setLocation(Location l) {
        if (null == _taskSeries) _taskSeries = TaskSeries.newTaskSeries();
        _taskSeries.setLocation(l);
    }

    @Override
    public void setRecurrence(String r) {
        if (null == _taskSeries) _taskSeries = TaskSeries.newTaskSeries();
        _taskSeries.setRecurrence(r);
    }

    @Override
    public void setTags(Collection<Tag> tags) {
        if (null == _taskSeries) _taskSeries = TaskSeries.newTaskSeries();
        _taskSeries.setTags(tags);
    }

    @Override
    public void setURL(URL u) {
        if (null == _taskSeries) _taskSeries = TaskSeries.newTaskSeries();
        _taskSeries.setUrl(u);
    }

    @Override
    public void uncomplete() {
        markDirty();
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public void rename(String n) {
        markDirty();
        throw new RuntimeException("Not yet implemented");
    }

    public void setTaskSeries(TaskSeries series) {
        markDirty();
        _taskSeries = series;
    }

    public TaskSeries getTaskSeries() {
        return _taskSeries;
    }

    public void setHasDueTime(String dt) {
        markDirty();
        _hasDueTime = dt.equals("1") ? true : false;
    }

    public void setDateAdded(Date da) {
        markDirty();
        _dateAdded = da;
    }

    public void setCompleted(Date c) {
        markDirty();
        _completed = c;
    }

    public void setDeleted(Date d) {
        markDirty();
        _deleted = d;
    }

    public void setPostponed(int pp) {
        markDirty();
        _postponed = pp;
    }

    @Override
    public String getName() {
        if (null == _taskSeries) return null;
        return _taskSeries.getName();
    }

    @Override
    public String getRecurrence() {
        if (null == _taskSeries) return null;
        return _taskSeries.getRecurrence();
    }

    @Override
    public void setDueDate(Date due) {
        markDirty();
        _dueDate = due;
    }

    @Override
    public void addNotes(Collection<Note> notes) {
        if (null == _taskSeries) setTaskSeries(TaskSeries.newTaskSeries());
        _taskSeries.addNotes(notes);
    }

    @Override
    public void removeNotes(Collection<Note> notes) {
        if (null == _taskSeries) setTaskSeries(TaskSeries.newTaskSeries());
        _taskSeries.removeNotes(notes);
    }

    @Override
    public void setNotes(Collection<Note> notes) {
        if (null == _taskSeries) setTaskSeries(TaskSeries.newTaskSeries());
        _taskSeries.setNotes(notes);
    }

    @Override
    public String getSource() {
        if (null == _taskSeries) return null;
        return _taskSeries.getSource();
    }

    @Override
    public void decreasePriority() {
        _priority.decrease();
        markDirty();
    }

    @Override
    public void increasePriority() {
        _priority.increase();
        markDirty();
    }

    @Override
    public void setPriority(Priority p) {
        _priority = p;
        markDirty();
    }
}
