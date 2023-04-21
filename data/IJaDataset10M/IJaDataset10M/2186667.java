package com.mymail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.mymail.entity.Group;
import com.mymail.entity.User;

public class GroupDAO extends DAOImp implements DAO {

    @SuppressWarnings("unchecked")
    public ArrayList<Group> getGroupsByUser(User user) {
        String sql = "select * from `group` where user_id=? ";
        return findArray(sql, new Object[] { user.getId() });
    }

    public boolean getByID(Integer id, Object obj) {
        Group group = (Group) obj;
        String sql = "select * from `group` where group_id=?";
        return findOne(sql, new Integer[] { id }, group);
    }

    public boolean addNew(Object obj) {
        Group group = (Group) obj;
        String sql = "insert into `group` (group_name,user_id,group_other) values (?,?,?)";
        int i = insert(sql, new Object[] { group.getName(), group.getUser(), group.getOther() });
        if (i > 0) {
            group.setId(i);
            return true;
        }
        return false;
    }

    public boolean delete(Object obj) {
        Group group = (Group) obj;
        String sql = "delete from `group` where group_id=?";
        return exe(sql, new Object[] { group.getId() });
    }

    public boolean toDatabase(Object obj) {
        Group group = (Group) obj;
        if (group.getId() == null) {
            return addNew(group);
        } else {
            return update(group);
        }
    }

    public boolean update(Object obj) {
        Group group = (Group) obj;
        String sql = "update `group` set group_name=?,group_other=? where group_id=?";
        return exe(sql, new Object[] { group.getName(), group.getOther(), group.getId() });
    }

    protected Group getEntity(ResultSet result) throws SQLException {
        Group group = new Group();
        setAtribute(result, group);
        return group;
    }

    protected void setAtribute(ResultSet result, Object obj) throws SQLException {
        Group group = (Group) obj;
        group.setId(result.getInt("group_id"));
        group.setName(result.getString("group_name"));
        group.setOther(result.getString("group_other"));
        group.setUser(result.getInt("user_id"));
    }
}
