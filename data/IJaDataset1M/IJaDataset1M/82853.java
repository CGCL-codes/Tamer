package ru.arriah.servicedesk.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.sql.DataSource;
import ru.arriah.common.ejb.GenericSessionBean;
import ru.arriah.servicedesk.bean.EmployeeBean;
import ru.arriah.servicedesk.bean.EmployeeTypeBean;
import ru.arriah.servicedesk.bean.GroupBean;
import ru.arriah.servicedesk.help.DbUtils;
import ru.arriah.servicedesk.help.Utils;

public class EmployeeManager extends GenericSessionBean {

    protected DataSource dataSource;

    enum EmployeeOrderByEnum {

        N, name, type, group
    }

    ;

    public EmployeeManager() {
        super();
        dataSource = null;
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {
        super.setSessionContext(sessionContext);
        try {
            this.dataSource = Utils.getDataSource();
        } catch (Exception e) {
            handleException(e, "setSessionContext");
            throw new EJBException(e);
        }
    }

    private EmployeeBean resultSetToBean(ResultSet resultSet) {
        try {
            EmployeeBean employeeBean = new EmployeeBean();
            employeeBean.setId(resultSet.getInt("user.id"));
            employeeBean.setEmail(resultSet.getString("user.email"));
            employeeBean.setFirstName(resultSet.getString("user.firstName"));
            employeeBean.setLastName(resultSet.getString("user.lastName"));
            employeeBean.setMiddleName(resultSet.getString("user.middleName"));
            employeeBean.setGroupId(resultSet.getInt("employee_settings.group_id"));
            employeeBean.setTypeId(resultSet.getInt("employee_settings.employeetype_id"));
            employeeBean.setStatus(resultSet.getString("status"));
            employeeBean.setLogin(resultSet.getString("login"));
            employeeBean.setPassword(resultSet.getString("password"));
            employeeBean.setRePassword(resultSet.getString("password"));
            EmployeeTypeBean type = new EmployeeTypeBean();
            type.setId(resultSet.getInt("employee_settings.employeetype_id"));
            type.setName(resultSet.getString("employeetype.employeetype_name"));
            employeeBean.setType(type);
            GroupBean group = new GroupBean();
            group.setId(resultSet.getInt("employee_settings.group_id"));
            group.setName(resultSet.getString("group.group_name"));
            employeeBean.setGroup(group);
            return employeeBean;
        } catch (Exception e) {
            handleException(e, "resultSetToBean");
            throw new EJBException(e);
        }
    }

    private Collection<EmployeeBean> resultSetToCollection(ResultSet resultSet) {
        try {
            ArrayList<EmployeeBean> employes = new ArrayList<EmployeeBean>();
            while (resultSet.next()) {
                employes.add(resultSetToBean(resultSet));
            }
            return employes;
        } catch (Exception e) {
            handleException(e, "resultSetToCollection");
            throw new EJBException(e);
        }
    }

    public int authorize(String login, String password, int employeeTypeId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT `user`.`id`, `user`.`password` as `password` FROM" + " `user`," + " `employee_settings`" + " WHERE `user`.`id` = `employee_settings`.`user_id`" + " AND `user`.`status` = 'active'" + " AND `user`.`user_type` = 'employee'" + " AND `employee_settings`.`employeetype_id` = ?" + " AND `user`.`login` = ?");
            preparedStatement.setInt(1, employeeTypeId);
            preparedStatement.setString(2, login);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (!resultSet.getString("password").equals(password)) {
                    throw new IllegalArgumentException("Password is incorrect");
                } else id = resultSet.getInt("id");
            } else throw new IllegalArgumentException("Password is incorrect");
            return id;
        } catch (Exception e) {
            handleException(e, "authorize");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    public int selectLeadExecutorId(int groupId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = 0;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT `user_id` FROM" + " `employee_settings`" + " WHERE `employeetype_id` = 2" + " AND `group_id` = ?");
            preparedStatement.setInt(1, groupId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("user_id");
            }
            return id;
        } catch (Exception e) {
            handleException(e, "selectLeadExecutorId");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    public Collection<EmployeeBean> selectAll() {
        return this.selectAll("N", "DESC", "'active'");
    }

    public Collection<EmployeeBean> selectAll(String orderBy, String order, String status) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            order = (order.equals("ASC") ? "ASC" : "DESC");
            try {
                EmployeeOrderByEnum orderEnum = EmployeeOrderByEnum.valueOf(orderBy);
                switch(orderEnum) {
                    case N:
                        orderBy = "`user`.`id`";
                        break;
                    case name:
                        orderBy = "`user`.`lastname`";
                        break;
                    case group:
                        orderBy = "`employee_settings`.`group_id`";
                        break;
                    case type:
                        orderBy = "`employee_settings`.`employeetype_id`";
                        break;
                    default:
                        orderBy = "`user`.`id`";
                        break;
                }
            } catch (Exception e) {
                orderBy = "`user`.`id`";
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM" + " `user`," + " `employeetype`," + " {oj `employee_settings` LEFT OUTER JOIN `group` ON `group`.`id` = `employee_settings`.`group_id`}" + " WHERE `user`.`status` IN (" + status + ")" + " AND `user`.`id` = `employee_settings`.`user_id`" + " AND `user`.`user_type` = 'employee'" + " AND `employeetype`.`id` = `employee_settings`.`employeetype_id`" + " ORDER BY " + orderBy + " " + order);
            resultSet = preparedStatement.executeQuery();
            Collection<EmployeeBean> employeesList = this.resultSetToCollection(resultSet);
            return employeesList;
        } catch (Exception e) {
            handleException(e, "selectAll");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    /**
    * Select subordinates
    * 
    * @param userId
    * @return
    */
    public Collection<EmployeeBean> selectSubordinates(int userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM" + " `user`," + " `chief_xref_user`," + " `employeetype`," + " {oj `employee_settings` LEFT OUTER JOIN `group` ON `group`.`id` = `employee_settings`.`group_id`}" + " WHERE `user`.`id` = `employee_settings`.`user_id`" + " AND `user`.`status` = 'active'" + " AND `user`.`user_type` = 'employee'" + " AND `user`.`id` = `chief_xref_user`.`user`" + " AND `employeetype`.`id` = `employee_settings`.`employeetype_id`" + " AND `chief_xref_user`.`chief` = ?");
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            Collection<EmployeeBean> employeesList = this.resultSetToCollection(resultSet);
            return employeesList;
        } catch (Exception e) {
            handleException(e, "selectSubordinates");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    public Collection<EmployeeBean> selectNotSubordinates(int userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM" + " `user` " + " WHERE `user`.`user_type` = 'employee'" + " AND `user`.`id` != ?" + " AND `user`.`id` NOT IN " + "(" + "  SELECT `user`.`id` FROM `user`" + "   INNER JOIN `chief_xref_user` ON `user`.`id` =  `chief_xref_user`.`user`" + "   WHERE `chief_xref_user`.`chief` = ?" + "   AND `user`.`user_type` = 'employee'" + ")");
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            resultSet = preparedStatement.executeQuery();
            ArrayList<EmployeeBean> employesList = new ArrayList<EmployeeBean>();
            while (resultSet.next()) {
                EmployeeBean employeeBean = new EmployeeBean();
                employeeBean.setId(resultSet.getInt("user.id"));
                employeeBean.setEmail(resultSet.getString("user.email"));
                employeeBean.setFirstName(resultSet.getString("user.firstName"));
                employeeBean.setLastName(resultSet.getString("user.lastName"));
                employeeBean.setMiddleName(resultSet.getString("user.middleName"));
                employesList.add(employeeBean);
            }
            return employesList;
        } catch (Exception e) {
            handleException(e, "selectNotSubordinates");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    public boolean addSubordinate(int ownerId, int subordinateId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO `chief_xref_user`" + " SET `chief` = ?," + " `user` = ?");
            preparedStatement.setInt(1, ownerId);
            preparedStatement.setInt(2, subordinateId);
            int result = preparedStatement.executeUpdate();
            return (result != 0);
        } catch (Exception e) {
            handleException(e, "addSubordinate");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement);
        }
    }

    public boolean deleteSubordinate(int ownerId, int subordinateId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM `chief_xref_user`" + " WHERE `chief` = ?" + " AND `user` = ?");
            preparedStatement.setInt(1, ownerId);
            preparedStatement.setInt(2, subordinateId);
            int result = preparedStatement.executeUpdate();
            return (result != 0);
        } catch (Exception e) {
            handleException(e, "deleteSubordinate");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement);
        }
    }

    public Collection<EmployeeBean> selectByGroupId(int groupId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM" + " `user`," + " `employeetype`," + " {oj `employee_settings` LEFT OUTER JOIN `group` ON `group`.`id` = `employee_settings`.`group_id`}" + " WHERE `user`.`id` = `employee_settings`.`user_id`" + " AND `user`.`status` = 'active'" + " AND `user`.`user_type` = 'employee'" + " AND `employeetype`.`id` = `employee_settings`.`employeetype_id`" + " AND `employee_settings`.`group_id` = ?");
            preparedStatement.setInt(1, groupId);
            resultSet = preparedStatement.executeQuery();
            Collection<EmployeeBean> employeesList = this.resultSetToCollection(resultSet);
            return employeesList;
        } catch (Exception e) {
            handleException(e, "selectByGroupId");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    public EmployeeBean selectDispatcher() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM" + " `user`," + " `employeetype`," + " {oj `employee_settings` LEFT OUTER JOIN `group` ON `group`.`id` = `employee_settings`.`group_id`}" + " WHERE `user`.`id` = `employee_settings`.`user_id`" + " AND `user`.`status` = 'active'" + " AND `user`.`user_type` = 'employee'" + " AND `employeetype`.`id` = `employee_settings`.`employeetype_id`" + " AND `employee_settings`.`employeetype_id` = 4");
            EmployeeBean dispatcher = null;
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) dispatcher = this.resultSetToBean(resultSet); else throw new Exception("dispatcher unable");
            return dispatcher;
        } catch (Exception e) {
            handleException(e, "selectDispatcher");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    public boolean isLoginUnique(String login, int type, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = true;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM" + " `user`," + " `employee_settings`" + " WHERE `user`.`id` = `employee_settings`.`user_id`" + " AND `user`.`user_type` = 'employee'" + " AND `employee_settings`.`employeetype_id` = ?" + " AND `user`.`login` = ?");
            preparedStatement.setInt(1, type);
            preparedStatement.setString(2, login);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getInt("user.id") != id) result = false;
            }
            return result;
        } catch (Exception e) {
            handleException(e, "isLoginUnique");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    public EmployeeBean selectEmployee(int employeeId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM" + " `user`," + " `employeetype`," + " {oj `employee_settings` LEFT OUTER JOIN `group` ON `group`.`id` = `employee_settings`.`group_id`}" + " WHERE `user`.`id` = `employee_settings`.`user_id`" + " AND `user`.`user_type` = 'employee'" + " AND `employeetype`.`id` = `employee_settings`.`employeetype_id`" + " AND `user`.`id` = ?");
            preparedStatement.setInt(1, employeeId);
            EmployeeBean employee = null;
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) employee = this.resultSetToBean(resultSet);
            return employee;
        } catch (Exception e) {
            handleException(e, "selectEmployee");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    public Collection<EmployeeBean> selectLeadExecutors() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM" + " `user`," + " `employeetype`," + " {oj `employee_settings` LEFT OUTER JOIN `group` ON `group`.`id` = `employee_settings`.`group_id`}" + " WHERE `user`.`id` = `employee_settings`.`user_id`" + " AND `user`.`user_type` = 'employee'" + " AND `employeetype`.`id` = `employee_settings`.`employeetype_id`" + " AND `employeetype`.`id` = 2");
            resultSet = preparedStatement.executeQuery();
            Collection<EmployeeBean> employeesList = this.resultSetToCollection(resultSet);
            return employeesList;
        } catch (Exception e) {
            handleException(e, "selectLeadExecutors");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    private boolean setEmployeeStatus(int id, String status) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE `user` SET `status` = ?" + " WHERE `id` = ?");
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, id);
            int result = preparedStatement.executeUpdate();
            return (result != 0);
        } catch (Exception e) {
            handleException(e, "setEmployeeStatus");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement);
        }
    }

    public boolean deleteEmployee(int id) {
        return this.setEmployeeStatus(id, "inactive");
    }

    public boolean restoreEmployee(int id) {
        return this.setEmployeeStatus(id, "active");
    }

    public Collection<EmployeeTypeBean> selectEmployeeTypes() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            ArrayList<EmployeeTypeBean> typesList = new ArrayList<EmployeeTypeBean>();
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM `employeetype`");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                EmployeeTypeBean typeBean = new EmployeeTypeBean(resultSet);
                ;
                typesList.add(typeBean);
            }
            return typesList;
        } catch (Exception e) {
            handleException(e, "selectEmployeeTypes");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    public boolean addEmployee(EmployeeBean employeeBean) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO `user`" + " SET `firstname` = ?," + " `lastname` = ?," + " `middlename` = ?," + " `login` = ?," + " `password` = ?," + " `email` = ?," + " `user_type` = 'employee'");
            preparedStatement.setString(1, employeeBean.getFirstName());
            preparedStatement.setString(2, employeeBean.getLastName());
            preparedStatement.setString(3, employeeBean.getMiddleName());
            preparedStatement.setString(4, employeeBean.getLogin());
            preparedStatement.setString(5, employeeBean.getPassword());
            preparedStatement.setString(6, employeeBean.getEmail());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                preparedStatement = connection.prepareStatement("INSERT INTO `employee_settings`" + " SET `user_id` = ?," + " `employeetype_id` = ?," + " `group_id` = ?");
                preparedStatement.setInt(1, resultSet.getInt(1));
                preparedStatement.setInt(2, employeeBean.getTypeId());
                preparedStatement.setInt(3, employeeBean.getGroupId());
                preparedStatement.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            handleException(e, "addEmployee");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement, resultSet);
        }
    }

    public boolean updateEmployee(EmployeeBean employeeBean) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE `user`" + " SET `firstname` = ?," + " `lastname` = ?," + " `middlename` = ?," + " `login` = ?," + " `password` = ?," + " `email` = ?," + " `user_type` = 'employee'" + " WHERE `user`.`id` = ?");
            preparedStatement.setString(1, employeeBean.getFirstName());
            preparedStatement.setString(2, employeeBean.getLastName());
            preparedStatement.setString(3, employeeBean.getMiddleName());
            preparedStatement.setString(4, employeeBean.getLogin());
            preparedStatement.setString(5, employeeBean.getPassword());
            preparedStatement.setString(6, employeeBean.getEmail());
            preparedStatement.setInt(7, employeeBean.getId());
            int result = preparedStatement.executeUpdate();
            if (result != 0) {
                preparedStatement = connection.prepareStatement("UPDATE `employee_settings`" + " SET `employeetype_id` = ?," + " `group_id` = ?" + " WHERE `employee_settings`.`user_id` = ?");
                preparedStatement.setInt(1, employeeBean.getTypeId());
                preparedStatement.setInt(2, employeeBean.getGroupId());
                preparedStatement.setInt(3, employeeBean.getId());
                preparedStatement.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            handleException(e, "updateEmployee");
            throw new EJBException(e);
        } finally {
            DbUtils.closeQuietly(connection, preparedStatement);
        }
    }
}
