package org.horen.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import org.horen.core.db.DataBase;
import org.horen.core.db.NoSuchObjectException;
import org.horen.core.db.UpdateState;
import org.horen.core.util.DateHelper;
import org.horen.ui.resources.Resources;

/**
 * represent a label.
 * 
 * @author Pascal
 */
public class Label {

    static HashMap<Integer, Label> m_allLabels = new HashMap<Integer, Label>();

    private Integer m_id;

    private String m_name;

    private Token m_token;

    private static boolean m_allCached = false;

    /**
	 * load the label with the given id from the database
	 * 
	 * @param id
	 * @throws NoSuchObjectException
	 * @throws SQLException 
	 */
    private Label(int id) throws NoSuchObjectException, SQLException {
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("SELECT label_id FROM label WHERE label_id=" + id);
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        boolean res = result.next();
        result.close();
        stmt.close();
        if (!res) {
            throw new NoSuchObjectException("Label not found");
        }
        m_id = id;
        refresh();
    }

    /**
	 * write a new label entry to the database
	 * 
	 * @param strName
	 *        label text
	 * @throws SQLException 
	 */
    private Label(String strName) throws SQLException {
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("INSERT INTO label (name) VALUES (?)");
        stmt.setString(1, strName);
        stmt.execute();
        stmt.close();
        stmt = db.prepareStatement("SELECT label_id FROM label WHERE name=? ORDER BY label_id DESC");
        stmt.setString(1, strName);
        stmt.executeQuery();
        ResultSet result = stmt.getResultSet();
        if (result.next()) {
            m_id = result.getInt(1);
            refresh();
        }
    }

    /**
	 * search the label in the local memory. If the label isn't load, it will be
	 * load and return
	 * 
	 * @param id
	 * @return label
	 * @throws NoSuchObjectException
	 * @throws SQLException 
	 */
    public static Label getById(int id) throws NoSuchObjectException, SQLException {
        if (m_allLabels.containsKey(id)) {
            return m_allLabels.get(id);
        }
        Label label = new Label(id);
        m_allLabels.put(id, label);
        return label;
    }

    /**
	 * refresh the values from the database
	 * @throws SQLException 
	 */
    public void refresh() throws SQLException {
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("SELECT name FROM label WHERE label_id=" + m_id);
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        result.next();
        m_name = result.getString("name");
        result.close();
        stmt.close();
    }

    /**
	 * get the id
	 * 
	 * @return id
	 */
    public int getId() {
        return m_id;
    }

    /**
	 * create a new label and save it in the local memory
	 * 
	 * @param strName
	 *        label text
	 * @return new label
	 * @throws SQLException 
	 */
    public static Label create(String strName) throws SQLException {
        Label label = new Label(strName);
        m_allLabels.put(label.getId(), label);
        DataBase.sendAction(label, UpdateState.NEW);
        return label;
    }

    /**
	 * set the name of the label
	 * 
	 * @param strName
	 *        name of the label
	 * @return true if the label name was changed
	 * @throws NoValidTokenException
	 * @throws SQLException 
	 */
    public boolean setName(String strName) throws NoValidTokenException, SQLException {
        if (!canChange()) {
            throw new NoValidTokenException();
        }
        PreparedStatement stmt = DataBase.getInstance().prepareStatement("UPDATE label SET name=? WHERE label_id=?");
        stmt.setString(1, strName);
        stmt.setInt(2, m_id);
        stmt.execute();
        m_name = strName;
        DataBase.sendAction(this, UpdateState.CHANGED);
        return stmt.getUpdateCount() > 0;
    }

    /**
	 * get the name of the label
	 * 
	 * @return name of the label
	 */
    public String getName() {
        return m_name;
    }

    /**
	 * add label to task
	 * 
	 * @param task
	 * @return true if the label was added
	 * @throws SQLException 
	 */
    public boolean addTask(Task task) throws SQLException {
        DataBase db = DataBase.getInstance();
        ResultSet result;
        PreparedStatement stmt = db.prepareStatement("SELECT label_id FROM task_label_map WHERE label_id=" + m_id + " AND task_id=" + task.getId().value());
        stmt.execute();
        result = stmt.getResultSet();
        if (result.next()) {
            result.close();
            stmt.close();
            return false;
        }
        DataBase.sendAction(task, UpdateState.CHANGED);
        result.close();
        stmt.close();
        return db.sendUpdate("INSERT INTO task_label_map (task_id, label_id) VALUES (" + task.getId().value() + ", " + m_id + ")") > 0;
    }

    /**
	 * delete label from task
	 * 
	 * @param task
	 * @return true if the label was deleted
	 * @throws SQLException 
	 */
    public boolean delTask(Task task) throws SQLException {
        DataBase.sendAction(task, UpdateState.CHANGED);
        return DataBase.getInstance().sendUpdate("DELETE FROM task_label_map WHERE label_id=" + m_id + " AND task_id=" + task.getId().value()) > 0;
    }

    /**
	 * return list of all tasks with this label
	 * 
	 * @return list of tasks
	 * @throws SQLException 
	 */
    public Task[] allTasks() throws SQLException {
        PreparedStatement stmt = DataBase.getInstance().prepareStatement("SELECT task_id FROM task_label_map WHERE label_id=" + m_id);
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

    @Override
    public String toString() {
        return m_name;
    }

    /**
	 * check if the labes is loaded
	 * @param id
	 * @return true if the label is loaded
	 */
    public static boolean isLoaded(Integer id) {
        return m_allLabels.containsKey(id);
    }

    /** 
	 * checks if there is a valid token to edit this label
	 * @return true if you can change the label
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
        if (m_token.lockTable("label", "label_id", m_id)) {
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
	 * select all labels
	 * @return Array of labels
	 * @throws SQLException 
	 * @throws NoSuchObjectException 
	 */
    public static Label[] getAll() throws NoSuchObjectException, SQLException {
        if (!m_allCached) {
            ArrayList<Label> labels = new ArrayList<Label>();
            PreparedStatement stmt = DataBase.getInstance().prepareStatement("SELECT label_id FROM label");
            stmt.execute();
            ResultSet result = stmt.getResultSet();
            while (result.next()) {
                labels.add(Label.getById(result.getInt("label_id")));
            }
            m_allCached = true;
        }
        return m_allLabels.values().toArray(new Label[0]);
    }

    /**
	 * delete the label
	 * @throws SQLException 
	 */
    public void delete() throws SQLException {
        DataBase db = DataBase.getInstance();
        PreparedStatement stmt = db.prepareStatement("SELECT task_id FROM task_label_map WHERE label_id=?");
        stmt.setInt(1, m_id);
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        Hashtable<Object, UpdateState> changes = new Hashtable<Object, UpdateState>();
        while (result.next()) {
            changes.put(Task.getById(new ID(result.getInt("task_id"))), UpdateState.CHANGED);
        }
        stmt = db.prepareStatement("UPDATE task SET modify_ts=? WHERE task_id IN (SELECT task_id FROM task_label_map WHERE label_id=?)");
        stmt.setTimestamp(1, DateHelper.now());
        stmt.setInt(2, m_id);
        stmt.execute();
        stmt.close();
        db.sendUpdate("DELETE FROM task_label_map WHERE label_id=" + m_id);
        db.sendUpdate("DELETE FROM label WHERE label_id=" + m_id);
        changes.put(this, UpdateState.DELETED);
        DataBase.sendAction(changes);
    }

    public static void createDefault() throws SQLException {
        create(Resources.getDefaultBundle().getString("label.appointment"));
        create(Resources.getDefaultBundle().getString("label.birthday"));
        create(Resources.getDefaultBundle().getString("label.business"));
        create(Resources.getDefaultBundle().getString("label.education"));
        create(Resources.getDefaultBundle().getString("label.holiday"));
        create(Resources.getDefaultBundle().getString("label.meeting"));
        create(Resources.getDefaultBundle().getString("label.personal"));
        create(Resources.getDefaultBundle().getString("label.phonecall"));
        create(Resources.getDefaultBundle().getString("label.travel"));
        create(Resources.getDefaultBundle().getString("label.vacation"));
    }
}
