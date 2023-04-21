package org.horen.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import org.horen.core.db.DataBase;
import org.horen.core.db.NoSuchObjectException;
import org.horen.core.db.UpdateState;
import org.horen.core.util.DateHelper;
import org.horen.core.util.TimeDiff;
import org.horen.task.export.IcalExport;
import org.horen.task.export.IcalMode;
import org.horen.task.search.TaskSearch;
import org.horen.ui.resources.Resources;

/**
 * Class the represent all tasks that are managed by the software
 * 
 * @author Pascal
 */
public class Task {

    static {
        DataBase.addDatabaseConnectListener(new CacheEreaser());
    }

    public static final String PATH_SEPARATOR = " " + Resources.getDefaultBundle().getString("Task.pathSeparator") + " ";

    static HashMap<ID, Task> m_allTasks = new HashMap<ID, Task>();

    private ID m_id;

    private String m_shortDesc;

    private String m_longDesc;

    private Timestamp m_createTs;

    private Timestamp m_startTs;

    private Timestamp m_finishTs;

    private Timestamp m_modifyTs;

    private Timestamp m_deadlineTs;

    private Type m_type;

    private int m_basePriority;

    private PriorityModel m_priorityModel;

    private Task m_parentTask;

    private Token m_token = null;

    private boolean m_isDeleted;

    private TimeDiff m_plannedTime;

    private TimeDiff m_calcedTime;

    private Integer m_templateId;

    public static PriorityUpdater m_priorityUpdater = new PriorityUpdater();

    /**
	 * this Constructor loads the data from the database
	 * 
	 * @param id
	 * @throws NoSuchObjectException
	 * @throws SQLException
	 */
    private Task(ID id) throws NoSuchObjectException, SQLException {
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("SELECT task_id FROM task WHERE task_id=?");
        stmt.setInt(1, id.value());
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        if (!result.next()) {
            throw new NoSuchObjectException("Task not found");
        }
        result.close();
        stmt.close();
        m_id = id;
        refresh();
    }

    /**
	 * creates a new task entry
	 * 
	 * @param strShortDesc
	 * @throws SQLException 
	 */
    private Task(String strShortDesc) throws SQLException {
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("INSERT INTO task (short_desc, create_ts, modify_ts) VALUES (?,?,?)");
        stmt.setString(1, strShortDesc);
        Timestamp now = DateHelper.now();
        stmt.setTimestamp(2, now);
        stmt.setTimestamp(3, now);
        stmt.execute();
        stmt.close();
        stmt = db.prepareStatement("SELECT task_id FROM task WHERE short_desc=? AND create_ts=? ORDER BY task_id DESC");
        stmt.setString(1, strShortDesc);
        stmt.setTimestamp(2, now);
        stmt.executeQuery();
        ResultSet result = stmt.getResultSet();
        if (result.next()) {
            m_id = new ID(result.getInt(1));
            refresh();
        }
    }

    /**
	 * search the task in the local memory. If the task isn't load, it will be
	 * load and return
	 * 
	 * @param id
	 * @return searched task
	 * @throws NoSuchObjectException
	 * @throws SQLException 
	 */
    public static Task getById(ID id) throws NoSuchObjectException, SQLException {
        if (m_allTasks.containsKey(id)) {
            return m_allTasks.get(id);
        }
        Task task = new Task(id);
        m_allTasks.put(id, task);
        return task;
    }

    /**
	 * refreshes the data from the database
	 * @throws SQLException 
	 */
    public void refresh() throws SQLException {
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("SELECT " + "short_desc, long_desc, create_ts, " + "start_ts, finish_ts, modify_ts, " + "type_id, base_priority, priority_model, " + "parent_task, deadline_ts, is_deleted, " + " planed_time, calced_time, template_id FROM task WHERE task_id=" + m_id.value());
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        result.next();
        m_shortDesc = result.getString("short_desc");
        m_longDesc = result.getString("long_desc");
        if (result.wasNull()) {
            m_longDesc = null;
        }
        m_createTs = result.getTimestamp("create_ts");
        m_finishTs = result.getTimestamp("finish_ts");
        if (result.wasNull()) {
            m_finishTs = null;
        }
        m_startTs = result.getTimestamp("start_ts");
        if (result.wasNull()) {
            m_startTs = null;
        }
        m_deadlineTs = result.getTimestamp("deadline_ts");
        if (result.wasNull()) {
            m_deadlineTs = null;
        }
        m_modifyTs = result.getTimestamp("modify_ts");
        m_basePriority = result.getInt("base_priority");
        int iParentId = result.getInt("parent_task");
        if (result.wasNull()) {
            m_parentTask = null;
        } else {
            m_parentTask = Task.getById(new ID(iParentId));
        }
        m_isDeleted = result.getInt("is_deleted") == 1;
        int id = result.getInt("priority_model");
        if (result.wasNull()) {
            m_priorityModel = null;
        } else {
            m_priorityModel = PriorityModel.getById(id);
        }
        id = result.getInt("type_id");
        if (result.wasNull()) {
            m_type = null;
        } else {
            m_type = Type.getById(id);
        }
        id = result.getInt("template_id");
        if (result.wasNull()) {
            m_templateId = null;
        } else {
            m_templateId = id;
        }
        m_plannedTime = new TimeDiff(result.getLong("planed_time"));
        m_calcedTime = new TimeDiff(result.getLong("calced_time"));
        result.close();
        stmt.close();
    }

    /**
	 * Getter for ID
	 * 
	 * @return ID
	 */
    public ID getId() {
        return m_id;
    }

    /**
	 * creates a new task with the given short description
	 * 
	 * @param strShortDesc
	 * @return new task
	 * @throws SQLException 
	 */
    public static Task create(String strShortDesc) throws SQLException {
        Task task = new Task(strShortDesc);
        m_allTasks.put(task.getId(), task);
        DataBase.sendAction(task, UpdateState.NEW);
        return task;
    }

    /**
	 * Set an table Entry
	 * 
	 * @param strFieldName
	 *        field name
	 * @param strValue
	 *        value
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    private void setStringValue(String strFieldName, String strValue) throws NoValidTokenException, SQLException {
        if (!canChange()) {
            throw new NoValidTokenException();
        }
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("UPDATE task SET " + strFieldName + "=?, modify_ts=? WHERE task_id=?");
        if (strValue == null) {
            strValue = "";
        }
        stmt.setString(1, strValue);
        Timestamp now = DateHelper.now();
        stmt.setTimestamp(2, now);
        stmt.setInt(3, m_id.value());
        stmt.execute();
        m_modifyTs = now;
    }

    /**
	 * Set an table Entry
	 * 
	 * @param strFieldName
	 *        field name
	 * @param iValue
	 *        value
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    private void setIntValue(String strFieldName, Integer iValue) throws NoValidTokenException, SQLException {
        if (!canChange()) {
            throw new NoValidTokenException();
        }
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("UPDATE task SET " + strFieldName + "=?, modify_ts=? WHERE task_id=?");
        if (iValue != null) {
            stmt.setInt(1, iValue);
        } else {
            stmt.setNull(1, Types.INTEGER);
        }
        Timestamp now = DateHelper.now();
        stmt.setTimestamp(2, now);
        stmt.setInt(3, m_id.value());
        stmt.execute();
        m_modifyTs = now;
    }

    /**
	 * Set an table Entry
	 * 
	 * @param strFieldName
	 *        field name
	 * @param iValue
	 *        value
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    private void setLongValue(String strFieldName, Long iValue) throws NoValidTokenException, SQLException {
        if (!canChange()) {
            throw new NoValidTokenException();
        }
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("UPDATE task SET " + strFieldName + "=?, modify_ts=? WHERE task_id=?");
        if (iValue != null) {
            stmt.setLong(1, iValue);
        } else {
            stmt.setNull(1, Types.BIGINT);
        }
        Timestamp now = DateHelper.now();
        stmt.setTimestamp(2, now);
        stmt.setInt(3, m_id.value());
        stmt.execute();
        m_modifyTs = now;
    }

    /**
	 * Set an table Entry
	 * 
	 * @param strFieldName
	 *        field name
	 * @param value
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    private Timestamp setTimestampValue(String strFieldName, Date value) throws NoValidTokenException, SQLException {
        if (!canChange()) {
            throw new NoValidTokenException();
        }
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("UPDATE task SET " + strFieldName + "=?, modify_ts=? WHERE task_id=?");
        Timestamp ts = null;
        if (value != null) {
            ts = new Timestamp(value.getTime());
            stmt.setTimestamp(1, ts);
        } else {
            stmt.setNull(1, Types.TIMESTAMP);
        }
        Timestamp now = DateHelper.now();
        stmt.setTimestamp(2, now);
        stmt.setInt(3, m_id.value());
        stmt.execute();
        m_modifyTs = now;
        return ts;
    }

    /**
	 * set the short description
	 * 
	 * @param strShortDesc
	 *        short description
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void setShortDesc(String strShortDesc) throws NoValidTokenException, SQLException {
        setStringValue("short_desc", strShortDesc);
        m_shortDesc = strShortDesc;
    }

    /**
	 * get the short description
	 * 
	 * @return short description
	 */
    public String getShortDesc() {
        return m_shortDesc;
    }

    /**
	 * set the long description
	 * 
	 * @param strLongDesc
	 *        long description
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void setLongDesc(String strLongDesc) throws NoValidTokenException, SQLException {
        setStringValue("long_desc", strLongDesc);
        m_longDesc = strLongDesc;
    }

    /**
	 * get the long description
	 * 
	 * @return long description
	 */
    public String getLongDesc() {
        return m_longDesc == null ? "" : m_longDesc;
    }

    /**
	 * set the start timestamp
	 * 
	 * @param startTs
	 *        start timestamp
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void setStartTs(Date startTs) throws NoValidTokenException, SQLException {
        m_startTs = setTimestampValue("start_ts", startTs);
    }

    /**
	 * get the start timestamp
	 * 
	 * @return start timestamp
	 */
    public Date getStartTs() {
        return m_startTs;
    }

    /**
	 * set the deadline timestamp
	 * 
	 * @param deadlineTs
	 *        deadline timestamp
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void setDeadlineTs(Date deadlineTs) throws NoValidTokenException, SQLException {
        m_deadlineTs = setTimestampValue("deadline_ts", deadlineTs);
    }

    /**
	 * get the start timestamp
	 * 
	 * @return start timestamp
	 */
    public Date getDeadlineTs() {
        return m_deadlineTs;
    }

    /**
	 * set the finish timestamp
	 * 
	 * @param finishTs
	 *        finish timestamp
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void setFinishTs(Date finishTs) throws NoValidTokenException, SQLException {
        m_finishTs = setTimestampValue("finish_ts", finishTs);
    }

    /**
	 * finish the task.
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void finish() throws NoValidTokenException, SQLException {
        setFinishTs(DateHelper.now());
        if (m_templateId != null) {
            Template tmpl = Template.getById(m_templateId);
            if (tmpl.getCurrentTask() == this) {
                tmpl.createNextTask();
            }
        }
    }

    /**
	 * @return <code>true</code> if the task is currently finished or
	 * <code>false</code> otherwise
	 */
    public boolean isFinished() {
        return m_finishTs != null;
    }

    /**
	 * get the finish timestamp
	 * 
	 * @return finish timestamp
	 */
    public Date getFinishTs() {
        return m_finishTs;
    }

    /**
	 * get the create timestamp
	 * 
	 * @return create timestamp
	 */
    public Date getCreateTs() {
        return m_createTs;
    }

    /**
	 * get the last modify timestamp
	 * 
	 * @return modify time stamp
	 */
    public Date getModifyTs() {
        return m_modifyTs;
    }

    /**
	 * set the base priority. Only value between 0 and 100 are excepted
	 * 
	 * @param iBasePriority
	 *        base priority
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void setBasePriority(int iBasePriority) throws NoValidTokenException, SQLException {
        setIntValue("base_priority", iBasePriority);
        m_basePriority = iBasePriority;
    }

    /**
	 * get the base priority
	 * 
	 * @return base priority
	 */
    public int getBasePriority() {
        return m_basePriority;
    }

    /**
	 * calc the priority
	 * @return priority [0..100]
	 * @throws SQLException 
	 */
    public float getPriority() throws SQLException {
        if (m_priorityModel != null) {
            return m_priorityModel.calcPriority(this);
        } else {
            return m_basePriority;
        }
    }

    /**
	 * set the type of the task
	 * 
	 * @param type
	 *        type of the task
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void setTaskType(Type type) throws NoValidTokenException, SQLException {
        if (!canChange()) {
            throw new NoValidTokenException();
        }
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt;
        stmt = db.prepareStatement("UPDATE task SET type_id=?, modify_ts=? WHERE task_id=?");
        stmt.setInt(1, type.getId());
        stmt.setTimestamp(2, DateHelper.now());
        stmt.setInt(3, getId().value());
        stmt.execute();
        m_type = type;
    }

    /**
	 * get the template of the task. This can be null if there is noch template.
	 * 
	 * @return template
	 * @throws SQLException 
	 * @throws NoSuchObjectException 
	 */
    public Template getTemplate() throws NoSuchObjectException, SQLException {
        if (m_templateId != null) {
            return Template.getById(m_templateId);
        } else {
            return null;
        }
    }

    /**
	 * create a new template to the task, if no one exists. Otherwise the current template
	 * will be return
	 * 
	 * @return template
	 * @throws SQLException 
	 * @throws NoValidTokenException 
	 */
    public Template createTemplate() throws NoValidTokenException, SQLException {
        if (m_templateId == null) {
            Template tmpl = Template.create(this);
            setTemplate(tmpl);
        }
        return Template.getById(m_templateId);
    }

    /**
	 * set the template of the task
	 * 
	 * @param template
	 *        template of the task
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    private void setTemplate(Template template) throws NoValidTokenException, SQLException {
        if (!canChange()) {
            throw new NoValidTokenException();
        }
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt;
        stmt = db.prepareStatement("UPDATE task SET template_id=?, modify_ts=? WHERE task_id=?");
        stmt.setInt(1, template.getId());
        stmt.setTimestamp(2, DateHelper.now());
        stmt.setInt(3, getId().value());
        stmt.execute();
        m_templateId = template.getId();
    }

    /**
	 * get the type of the task
	 * 
	 * @return type
	 */
    public Type getTaskType() {
        return m_type;
    }

    /**
	 * set the used priority model
	 * 
	 * @param model
	 *        priority model
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void setPriorityModel(PriorityModel model) throws NoValidTokenException, SQLException {
        setIntValue("priority_model", model.getId());
        m_priorityModel = model;
    }

    /**
	 * get the used priority model
	 * 
	 * @return priority model
	 */
    public PriorityModel getPriorityModel() {
        return m_priorityModel;
    }

    /**
	 * set the parent task
	 * 
	 * @param parentTask
	 *        parent task
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void setParentTask(Task parentTask) throws NoValidTokenException, SQLException {
        Hashtable<Task, UpdateState> changed = new Hashtable<Task, UpdateState>();
        UpdateState state = UpdateState.MOVED;
        if (parentTask != null) {
            state.setParent(m_parentTask);
            setIntValue("parent_task", parentTask.getId().value());
        } else {
            setIntValue("parent_task", null);
        }
        m_parentTask = parentTask;
        changed.put(this, state);
        DataBase.sendAction(changed);
    }

    /**
	 * get the parent task
	 * 
	 * @return parent task
	 */
    public Task getParentTask() {
        return m_parentTask;
    }

    /**
	 * set the parent of the argument to this task
	 * 
	 * @param child
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void addChildTask(Task child) throws NoValidTokenException, SQLException {
        child.setParentTask(this);
    }

    /**
	 * get a list of all child tasks
	 * 
	 * @return child tasks
	 * @throws SQLException 
	 */
    public Task[] getChildTasks(boolean ignoreDeleted) throws SQLException {
        PreparedStatement stmt = DataBase.getInstance().prepareStatement("SELECT DISTINCT task_id FROM task WHERE parent_task=" + m_id.value() + (ignoreDeleted ? " AND is_deleted=0" : ""));
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        ArrayList<Task> tasks = new ArrayList<Task>();
        while (result.next()) {
            tasks.add(Task.getById(new ID(result.getInt("task_id"))));
        }
        result.close();
        stmt.close();
        return tasks.toArray(new Task[0]);
    }

    public Task[] getChildTasks() throws SQLException {
        return this.getChildTasks(false);
    }

    /**
	 * tests if this task has children.
	 * @param ignoreDeleted if true deleted tasks where ignored
	 * @return true if there are children.
	 * @throws SQLException 
	 */
    public boolean hasChildTasks(boolean ignoreDeleted) throws SQLException {
        PreparedStatement stmt;
        ResultSet result;
        if (ignoreDeleted) {
            stmt = DataBase.getInstance().prepareStatement("SELECT task_id FROM task WHERE is_deleted=0 AND parent_task=" + m_id.value());
        } else {
            stmt = DataBase.getInstance().prepareStatement("SELECT task_id FROM task WHERE parent_task=" + m_id.value());
        }
        stmt.execute();
        result = stmt.getResultSet();
        boolean res = result.next();
        result.close();
        stmt.close();
        return res;
    }

    /**
	 * tests if this task has children.
	 * 
	 * @return true if there are children.
	 * @throws SQLException 
	 */
    public boolean hasChildTasks() throws SQLException {
        return hasChildTasks(true);
    }

    /**
	 * add a working time to the task
	 * 
	 * @param time
	 *        working time
	 * @throws SQLException 
	 */
    public WorkingTime addWorkingTime(TimeDiff time, Date start) throws SQLException {
        return WorkingTime.create(start, time, this);
    }

    /**
	 * removes a working time from the task
	 * 
	 * @param wtime
	 *        working time
	 * @throws SQLException 
	 */
    public void delWorkingTime(WorkingTime wtime) throws SQLException {
        wtime.delete();
    }

    /**
	 * get the sum of all working times
	 * 
	 * @return working time
	 * @throws SQLException 
	 */
    public TimeDiff getWorkingTime() throws SQLException {
        PreparedStatement stmt = DataBase.getInstance().prepareStatement("SELECT SUM(time) AS wtime FROM workingtime WHERE task_id=" + m_id.value());
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        result.next();
        TimeDiff td = new TimeDiff(result.getLong("wtime"));
        result.close();
        stmt.close();
        return td;
    }

    /**
	 * get a list of all working times
	 * 
	 * @return working times
	 * @throws SQLException 
	 */
    public WorkingTime[] getWorkingTimes() throws SQLException {
        PreparedStatement stmt = DataBase.getInstance().prepareStatement("SELECT workingtime_id FROM workingtime WHERE task_id=" + m_id.value());
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        ArrayList<WorkingTime> wtimes = new ArrayList<WorkingTime>();
        while (result.next()) {
            wtimes.add(WorkingTime.getById(result.getInt("workingtime_id")));
        }
        result.close();
        stmt.close();
        return (wtimes.toArray(new WorkingTime[0]));
    }

    /**
	 * check if the task has workingtimes
	 * 
	 * @return true if there are workingtimes
	 * @throws SQLException 
	 */
    public boolean hasWorkingTimes() throws SQLException {
        PreparedStatement stmt = DataBase.getInstance().prepareStatement("SELECT workingtime_id FROM workingtime WHERE task_id=" + m_id.value());
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        boolean hasNext = result.next();
        result.close();
        stmt.close();
        return hasNext;
    }

    /**
	 * add a label to the task
	 * 
	 * @param label
	 * @return true if the label was added
	 * @throws SQLException 
	 */
    public boolean addLabel(Label label) throws SQLException {
        return label.addTask(this);
    }

    /**
	 * tests if the task has the label
	 * 
	 * @param label
	 * @return true if the task has the label
	 * @throws SQLException 
	 */
    public boolean hasLabel(Label label) throws SQLException {
        PreparedStatement stmt = DataBase.getInstance().prepareStatement("SELECT * FROM task_label_map WHERE label_id=" + label.getId() + " AND task_id=" + m_id.value());
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        boolean res = result.next();
        result.close();
        stmt.close();
        return res;
    }

    /**
	 * deletes a label from a task
	 * 
	 * @param label
	 * @return true if the label was found and deleted
	 * @throws SQLException 
	 */
    public boolean delLabel(Label label) throws SQLException {
        return label.delTask(this);
    }

    /**
	 * get a list of all labels
	 * 
	 * @return list of labels
	 * @throws SQLException 
	 */
    public Label[] getLabels() throws SQLException {
        PreparedStatement stmt = DataBase.getInstance().prepareStatement("SELECT label_id FROM task_label_map WHERE task_id=" + m_id.value());
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        ArrayList<Label> labels = new ArrayList<Label>();
        while (result.next()) {
            labels.add(Label.getById(result.getInt("label_id")));
        }
        result.close();
        stmt.close();
        return (labels.toArray(new Label[0]));
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Task) {
            return ((Task) other).m_id.equals(m_id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return m_id.hashCode();
    }

    @Override
    public String toString() {
        return "#" + m_id + ": " + m_shortDesc;
    }

    /**
	 * @return The found tasks or null if an exception has occurred and been
	 * handled.
	 * @throws SQLException 
	 */
    public static Task[] search(TaskSearch condition) throws SQLException {
        PreparedStatement stmt = DataBase.getInstance().prepareStatement(condition.getSql());
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        ArrayList<Task> tasks = new ArrayList<Task>();
        while (result.next()) {
            tasks.add(getById(new ID(result.getInt("task_id"))));
        }
        result.close();
        stmt.close();
        return tasks.toArray(new Task[0]);
    }

    /**
	 * Get the path in the task hierarchy and uses "/" as Seperator
	 * @return
	 */
    public String getPath() {
        return getPath(PATH_SEPARATOR);
    }

    /**
	 * Get the path in the task hierarchy 
	 * @param strSeperator Seperator
	 * @return path as String
	 */
    public String getPath(String strSeperator) {
        String path = getShortDesc();
        Task t = this;
        while ((t = t.getParentTask()) != null) {
            path = t.getShortDesc() + strSeperator + path;
        }
        return path;
    }

    /**
	 * get the status of the Task
	 * FINISHED - task has a finished-timestamp
	 * PLANED - task is planed, the start timestamp is in the future
	 * ACTIVE - task has workingtimes and the start is in the past
	 * READY - task has no workingtimes and the start is in the past
	 * @return status
	 * @throws SQLException 
	 */
    public TaskStatus getStatus() throws SQLException {
        if (m_isDeleted) {
            return TaskStatus.DELETED;
        }
        if (m_finishTs != null) {
            return TaskStatus.FINISHED;
        }
        if (m_startTs != null && m_startTs.after(DateHelper.now())) {
            return TaskStatus.PLANED;
        }
        if (hasWorkingTimes()) {
            return TaskStatus.ACTIVE;
        } else {
            return TaskStatus.READY;
        }
    }

    /** 
	 * checks if there is a valid token to edit this task
	 * @return true if you can change the task
	 * @throws SQLException 
	 */
    public boolean canChange() throws SQLException {
        if (m_token == null) {
            return false;
        }
        if (!m_token.check()) {
            m_token.delete();
            m_token = null;
            return false;
        }
        return true;
    }

    /**
	 * trys to create a change token. If there is an other token, false will
	 * be returned
	 * @return true if a token was created successful 
	 * @throws SQLException 
	 */
    public boolean startEdit() throws SQLException {
        if (m_token != null) {
            return false;
        }
        m_token = new Token();
        if (m_token.lockTable("task", "task_id", m_id.value())) {
            return true;
        } else {
            m_token.delete();
            m_token = null;
            return false;
        }
    }

    /**
	 * delete the change token.
	 * @throws SQLException 
	 */
    public void endEdit() throws SQLException {
        if (m_token != null) {
            m_token.delete();
            m_token = null;
        }
        DataBase.sendAction(this, UpdateState.CHANGED);
    }

    /**
	 * checks if the task is loaded
	 * @param id
	 * @return true if the task is loaded
	 */
    public static boolean isLoad(ID id) {
        return m_allTasks.containsKey(id);
    }

    /**
	 * check if the task is deleted
	 * @return true if the task is deleted
	 */
    public boolean isDeleted() {
        return m_isDeleted;
    }

    /**
	 * mark the task as deleted
	 * @throws SQLException 
	 */
    public void delete() throws SQLException {
        DataBase.getInstance().sendUpdate("UPDATE task SET is_deleted=1 WHERE task_id=" + m_id.value());
        m_isDeleted = true;
        DataBase.sendAction(this, UpdateState.DELETED);
    }

    /**
	 * mark the task as deleted
	 * @param recursiv if true also all sub tasks will be delted
	 * @throws SQLException 
	 */
    public void delete(boolean recursiv) throws SQLException {
        delete();
        if (recursiv) {
            for (Task t : getChildTasks()) {
                t.delete(recursiv);
            }
        }
    }

    /**
	 * remove the delete mark
	 * @throws SQLException 
	 */
    public void restore() throws SQLException {
        DataBase.getInstance().sendUpdate("UPDATE task SET is_deleted=0 WHERE task_id=" + m_id.value());
        m_isDeleted = false;
        for (Task t : getPredecessors()) {
            removePredecessor(t);
        }
        for (Task t : getSuccessors()) {
            removeSuccessor(t);
        }
        DataBase.sendAction(this, UpdateState.NEW);
    }

    /**
	 * Set the planed time
	 * @param time
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public void setPlannedTime(TimeDiff time) throws NoValidTokenException, SQLException {
        setLongValue("planed_time", time.getValue());
        m_plannedTime = time;
        recalcTime();
    }

    /**
	 * recalc and save needed time, when possible
	 * @return calced time
	 * @throws SQLException 
	 * @throws NoValidTokenException 
	 */
    private TimeDiff recalcTime() throws NoValidTokenException, SQLException {
        boolean started = false;
        TimeDiff calced_time = TaskTimeCalculator.calcTime(this);
        if (!calced_time.equals(m_calcedTime)) {
            if (!canChange()) {
                started = startEdit();
            }
            if (canChange()) {
                setLongValue("calced_time", calced_time.getValue());
                m_calcedTime = calced_time;
            }
            if (started) {
                endEdit();
            }
        }
        return calced_time;
    }

    /**
	 * get the planed time
	 * @return planed time
	 */
    public TimeDiff getPlannedTime() {
        return m_plannedTime;
    }

    /**
	 * get the calced time. It will be calced when not calced yet.
	 * @return calced time
	 * @throws SQLException 
	 * @throws NoValidTokenException 
	 */
    public TimeDiff getCalcedTime() throws NoValidTokenException, SQLException {
        if (m_calcedTime.equals(new TimeDiff())) {
            return recalcTime();
        }
        return m_calcedTime;
    }

    /**
	 * add a successor to this task. If the task is already a successor nothing happen.
	 * @param Succcessor
	 * @throws SQLException 
	 */
    public void addSuccessor(Task suc) throws SQLException {
        DataBase db = DataBase.getInstance();
        ResultSet result;
        PreparedStatement stmt = db.prepareStatement("SELECT * FROM dependencies WHERE task_1=? AND task_2=?");
        stmt.setInt(1, getId().value());
        stmt.setInt(2, suc.getId().value());
        stmt.execute();
        result = stmt.getResultSet();
        if (result.next()) {
            result.close();
            stmt.close();
            return;
        }
        result.close();
        stmt.close();
        stmt = db.prepareStatement("INSERT INTO dependencies (task_1,task_2) VALUES (?,?)");
        stmt.setInt(1, getId().value());
        stmt.setInt(2, suc.getId().value());
        stmt.execute();
        stmt = db.prepareStatement("UPDATE task SET modify_ts=? WHERE task_id=? OR task_id=?");
        stmt.setTimestamp(1, DateHelper.now());
        stmt.setInt(2, getId().value());
        stmt.setInt(3, suc.getId().value());
        stmt.execute();
        Hashtable<Task, UpdateState> changes = new Hashtable<Task, UpdateState>();
        changes.put(this, UpdateState.CHANGED);
        changes.put(suc, UpdateState.CHANGED);
        DataBase.sendAction(changes);
    }

    /**
     * add a predecessor to this task. If the task is already a predecessor nothing happen.
     * @param Predecessor
     * @throws SQLException 
     */
    public void addPredecessor(Task pred) throws SQLException {
        pred.addSuccessor(this);
    }

    /**
     * removes a successor from this task
     * @param successor
     * @throws SQLException 
     */
    public void removeSuccessor(Task suc) throws SQLException {
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("DELETE FROM dependencies WHERE (task_1= ? AND task_2= ? )");
        stmt.setInt(1, getId().value());
        stmt.setInt(2, suc.getId().value());
        stmt.execute();
        stmt.close();
        stmt = db.prepareStatement("UPDATE task SET modify_ts=? WHERE task_id=? OR task_id=?");
        stmt.setTimestamp(1, DateHelper.now());
        stmt.setInt(2, getId().value());
        stmt.setInt(3, suc.getId().value());
        stmt.execute();
        stmt.close();
        Hashtable<Task, UpdateState> changes = new Hashtable<Task, UpdateState>();
        changes.put(this, UpdateState.CHANGED);
        changes.put(suc, UpdateState.CHANGED);
        DataBase.sendAction(changes);
    }

    /**
     * removes a predecessor from this task
     * @param predecessor
     * @throws SQLException 
     */
    public void removePredecessor(Task pred) throws SQLException {
        pred.removeSuccessor(this);
    }

    public Task[] getSuccessors() throws NoSuchObjectException, SQLException {
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("SELECT task_2 FROM dependencies WHERE task_1=?");
        stmt.setInt(1, getId().value());
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        ArrayList<Task> tasks = new ArrayList<Task>();
        while (result.next()) {
            Task t = Task.getById(new ID(result.getInt("task_2")));
            if (!t.isDeleted()) {
                tasks.add(t);
            }
        }
        result.close();
        stmt.close();
        return tasks.toArray(new Task[0]);
    }

    public Task[] getPredecessors() throws SQLException {
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("SELECT task_1 FROM dependencies WHERE task_2=?");
        stmt.setInt(1, getId().value());
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        ArrayList<Task> tasks = new ArrayList<Task>();
        while (result.next()) {
            Task t = Task.getById(new ID(result.getInt("task_1")));
            if (!t.isDeleted()) {
                tasks.add(t);
            }
        }
        result.close();
        stmt.close();
        return tasks.toArray(new Task[0]);
    }

    /**
     * clone the task, but not the children
     * @throws SQLException 
     * @throws NoValidTokenException 
     * @throws SQLException 
     * @throws NoValidTokenException 
     */
    public Task duplicate() throws NoValidTokenException, SQLException {
        return duplicate(false);
    }

    /**
     * clone the task. If recursive also children will be cloned
     * @param recursive
     * @return
     * @throws SQLException 
     * @throws NoValidTokenException 
     */
    public Task duplicate(boolean recursive) throws SQLException, NoValidTokenException {
        Task t = create(m_shortDesc);
        if (recursive) {
            for (Task t_child : getChildTasks()) {
                if (t_child.isDeleted()) {
                    continue;
                }
                Task tNewChild = t_child.duplicate(recursive);
                tNewChild.startEdit();
                tNewChild.setParentTask(t);
                tNewChild.endEdit();
            }
        }
        t.startEdit();
        DataBase db = DataBase.getInstance();
        db.sendUpdate("INSERT INTO task_label_map (task_id, label_id) SELECT " + t.getId().value() + ", label_id FROM task_label_map WHERE task_id=" + m_id.value());
        db.sendUpdate("INSERT INTO dependencies (task_1, task_2) SELECT " + t.getId().value() + ", task_2 FROM dependencies WHERE task_1=" + m_id.value());
        db.sendUpdate("INSERT INTO dependencies (task_1, task_2) SELECT task_1, " + t.getId().value() + " FROM dependencies WHERE task_2=" + m_id.value());
        PreparedStatement stmt = db.prepareStatement("UPDATE task SET " + "short_desc=?, long_desc=?, " + "start_ts=?, modify_ts=?, " + "type_id=?, base_priority=?, priority_model=?, " + "parent_task=?, deadline_ts=?, " + "planed_time=?, template_id=? WHERE task_id=?");
        stmt.setString(1, m_shortDesc);
        if (m_longDesc != null) {
            stmt.setString(2, m_longDesc);
        } else {
            stmt.setNull(2, Types.VARCHAR);
        }
        if (m_startTs != null) {
            stmt.setTimestamp(3, m_startTs);
        } else {
            stmt.setNull(3, Types.TIMESTAMP);
        }
        stmt.setTimestamp(4, DateHelper.now());
        if (m_type != null) {
            stmt.setInt(5, m_type.getId());
        } else {
            stmt.setNull(5, Types.INTEGER);
        }
        if (m_type != null) {
            stmt.setInt(6, m_basePriority);
        } else {
            stmt.setNull(6, Types.INTEGER);
        }
        stmt.setInt(6, m_basePriority);
        if (m_priorityModel != null) {
            stmt.setInt(7, m_priorityModel.getId());
        } else {
            stmt.setNull(7, Types.INTEGER);
        }
        if (m_parentTask != null) {
            stmt.setInt(8, m_parentTask.getId().value());
        } else {
            stmt.setNull(8, Types.INTEGER);
        }
        if (m_deadlineTs != null) {
            stmt.setTimestamp(9, m_deadlineTs);
        } else {
            stmt.setNull(9, Types.TIMESTAMP);
        }
        if (m_plannedTime != null) {
            stmt.setLong(10, m_plannedTime.getValue());
        } else {
            stmt.setNull(10, Types.INTEGER);
        }
        if (m_templateId != null) {
            stmt.setInt(11, m_templateId);
        } else {
            stmt.setNull(11, Types.INTEGER);
        }
        stmt.setInt(12, t.getId().value());
        stmt.execute();
        stmt.close();
        t.refresh();
        t.endEdit();
        return t;
    }

    public TaskWarning[] getWarnings() throws SQLException {
        ArrayList<TaskWarning> warnings = new ArrayList<TaskWarning>();
        if (getFinishTs() != null) {
            return warnings.toArray(new TaskWarning[0]);
        }
        if (getDeadlineTs() == null) {
            warnings.add(TaskWarning.NO_DEADLINE);
        } else {
            if (getStartTs() != null && getStartTs().after(getDeadlineTs())) {
                warnings.add(TaskWarning.START_AFTER_DEADLINE);
            }
            if (getDeadlineTs().before(DateHelper.now())) {
                warnings.add(TaskWarning.DEADLINE_IN_PAST);
            }
            if (getPlannedTime() != null) {
                Date possible_finish = new Date(DateHelper.now().getTime() + getPlannedTime().getValue() - getWorkingTime().getValue());
                if (possible_finish.after(getDeadlineTs())) {
                    warnings.add(TaskWarning.DEADLINE_NOT_REACHABLE);
                }
            }
            if (getParentTask() != null && getParentTask().getDeadlineTs() != null && getDeadlineTs().after(getParentTask().getDeadlineTs())) {
                warnings.add(TaskWarning.DEADLINE_AFTER_PARENT_DEADLINE);
            }
        }
        if (getStartTs() != null && getParentTask() != null && getParentTask().getStartTs() != null && getStartTs().before(getParentTask().getStartTs())) {
            warnings.add(TaskWarning.START_BEFORE_PARENT_START);
        }
        return warnings.toArray(new TaskWarning[warnings.size()]);
    }

    public static Task[] getCachedTasks() {
        return m_allTasks.values().toArray(new Task[m_allTasks.size()]);
    }

    /**
	 * get the task information as ical
	 * @param mode
	 * @return
	 * @throws SQLException 
	 * @throws NoSuchObjectException 
	 */
    public String getIcal(IcalMode mode) throws SQLException, NoSuchObjectException {
        StringBuffer msg = new StringBuffer();
        if (mode == IcalMode.TODO) {
            msg.append("BEGIN:VTODO\r\n");
        } else {
            msg.append("BEGIN:VEVENT\r\n");
        }
        msg.append("UID:");
        msg.append(getId().value());
        msg.append("Horen\r\n");
        msg.append("DTSTAMP:");
        msg.append(IcalExport.formatDate(getCreateTs()));
        msg.append("Z\r\n");
        msg.append("SUMMARY:");
        msg.append(IcalExport.escape(getShortDesc()));
        msg.append("\r\n");
        msg.append("PRIORITY:");
        int prio = 10 - (int) (((int) getPriority() + 10) / 11);
        if (prio > 9) {
            prio = 9;
        }
        if (prio < 0) {
            prio = 0;
        }
        msg.append(prio);
        msg.append("\r\n");
        msg.append("CREATED:");
        msg.append(IcalExport.formatDate(getCreateTs()));
        msg.append("\r\n");
        msg.append("LAST-MODIFIED:");
        msg.append(IcalExport.formatDate(getModifyTs()));
        msg.append("\r\n");
        if (getLongDesc() != null) {
            msg.append("DESCRIPTION:");
            msg.append(IcalExport.escape(getLongDesc()));
            msg.append("\r\n");
        }
        if (getDeadlineTs() != null) {
            msg.append("DUE;TZID=");
            msg.append(IcalExport.getTZID());
            msg.append(":\r\n ");
            msg.append(IcalExport.formatDate(getDeadlineTs()));
            msg.append("\r\n");
        }
        if (getStartTs() != null) {
            msg.append("DTSTART;TZID=");
            msg.append(IcalExport.getTZID());
            msg.append(":\r\n ");
            msg.append(IcalExport.formatDate(getStartTs()));
            msg.append("\r\n");
        }
        msg.append("X-HOREN-BASEPRIORITY:");
        msg.append(getBasePriority());
        msg.append("\r\n");
        Label[] labels = getLabels();
        if (labels != null && labels.length > 0) {
            msg.append("CATEGORIES:");
            boolean first = true;
            for (Label l : labels) {
                if (!first) {
                    msg.append(",");
                } else {
                    first = false;
                }
                msg.append(l.getName());
            }
            msg.append("\r\n");
        }
        if (getPlannedTime() != null) {
            msg.append("X-HOREN-PLANNEDTIME:");
            msg.append(getPlannedTime().getValue());
            msg.append("\r\n");
        }
        Task[] predecessors = getPredecessors();
        if (predecessors != null && predecessors.length > 0) {
            msg.append("X-HOREN-PREDECESSORS:");
            boolean first = true;
            for (Task t : predecessors) {
                if (!first) {
                    msg.append(",");
                } else {
                    first = false;
                }
                msg.append(t.getId().value());
            }
            msg.append("\r\n");
        }
        Task[] successors = getSuccessors();
        if (successors != null && successors.length > 0) {
            msg.append("X-HOREN-SUCCESSORS:");
            boolean first = true;
            for (Task t : successors) {
                if (!first) {
                    msg.append(",");
                } else {
                    first = false;
                }
                msg.append(t.getId().value());
            }
            msg.append("\r\n");
        }
        if (getParentTask() != null) {
            msg.append("X-HOREN-PARENT:");
            msg.append(getParentTask().getId().value());
            msg.append("\r\n");
        }
        if (getTemplate() != null && getTemplate().getCurrentTask() == this) {
            msg.append("RRULE:FREQ=");
            msg.append(getTemplate().getIntervalDesc().getIcalString());
            msg.append(";INTERVAL=");
            msg.append(getTemplate().getIntervalCount());
            boolean useCount = true;
            boolean useDeadline = true;
            if (getTemplate().getMaxMoreTask() != null && getTemplate().getLatestDeadline() != null && getDeadlineTs() != null) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(getDeadlineTs());
                for (int i = 0; i < getTemplate().getMaxMoreTask(); i++) {
                    calendar.add(getTemplate().getIntervalDesc().calenderField(), getTemplate().getIntervalCount());
                }
                if (calendar.getTime().after(getTemplate().getLatestDeadline())) {
                    useCount = false;
                } else {
                    useDeadline = false;
                }
            }
            if (getTemplate().getLatestDeadline() != null && useDeadline) {
                msg.append(";UNTIL=");
                msg.append(IcalExport.formatDate(getTemplate().getLatestDeadline()));
            }
            if (getTemplate().getMaxMoreTask() != null && useCount) {
                msg.append(";COUNT=");
                msg.append(getTemplate().getMaxMoreTask());
            }
            msg.append("\r\n");
        }
        if (getTaskType() != null) {
            msg.append("X-HOREN-TYPE-NAME:");
            msg.append(getTaskType().getName());
            msg.append("\r\n");
        }
        msg.append("CLASS:PUBLIC\r\n");
        if (mode == IcalMode.TODO) {
            msg.append("END:VTODO\r\n");
        } else {
            msg.append("END:VEVENT\r\n");
        }
        return msg.toString();
    }
}
