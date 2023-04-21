package org.coenraets.directory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SoftyDAO {

    public List<UserPass> findAll() {
        List<UserPass> list = new ArrayList<UserPass>();
        Connection c = null;
        String sql = "SELECT up.utilizatorid as uid , up.parola as password FROM userpass as up ";
        try {
            c = ConnectionHelperPg.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                list.add(processSummaryRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
        return list;
    }

    public List<Employee> findByName(String name) {
        List<Employee> list = new ArrayList<Employee>();
        Connection c = null;
        String sql = "SELECT e.id, e.firstName, e.lastName, e.title, e.picture, count(r.id) reportCount FROM employee as e " + "LEFT JOIN employee r ON e.id = r.managerId " + "WHERE UPPER(CONCAT(e.firstName, ' ', e.lastName)) LIKE ? " + "GROUP BY e.id " + "ORDER BY e.firstName, e.lastName";
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%" + name.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
        return list;
    }

    public List<Employee> findByManager(int managerId) {
        List<Employee> list = new ArrayList<Employee>();
        Connection c = null;
        String sql = "SELECT e.id, e.firstName, e.lastName, e.title, e.picture, count(r.id) reportCount FROM employee as e " + "LEFT JOIN employee r ON e.id = r.managerId " + "WHERE e.managerId=? " + "GROUP BY e.id " + "ORDER BY e.firstName, e.lastName";
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
        return list;
    }

    public Employee findById(int id) {
        String sql = "SELECT e.id, e.firstName, e.lastName, e.managerId, e.title, e.department, e.city, e.officePhone, e.cellPhone, " + "e.email, e.picture, m.firstName managerFirstName, m.lastName managerLastName, count(r.id) reportCount FROM employee as e " + "LEFT JOIN employee m ON e.managerId = m.id " + "LEFT JOIN employee r ON e.id = r.managerId " + "WHERE e.id = ? " + "GROUP BY e.id";
        Employee employee = null;
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                employee = processRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
        return employee;
    }

    public Employee save(Employee employee) {
        return employee.getId() > 0 ? update(employee) : create(employee);
    }

    public Employee create(Employee employee) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = ConnectionHelper.getConnection();
            ps = c.prepareStatement("INSERT INTO employee (firstName, lastName, title, department, managerId, city, officePhone, cellPhone, email, picture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[] { "ID" });
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getTitle());
            ps.setString(4, employee.getDepartment());
            ps.setInt(5, employee.getManager() == null ? 0 : employee.getManager().getId());
            ps.setString(6, employee.getCity());
            ps.setString(7, employee.getOfficePhone());
            ps.setString(8, employee.getCellPhone());
            ps.setString(9, employee.getEmail());
            ps.setString(10, employee.getPicture());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            employee.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
        return employee;
    }

    public Employee update(Employee employee) {
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement("UPDATE employee SET firstName=?, lastName=?, title=?, deptartment=?, managerId=?, city=?, officePhone, cellPhone=?, email=?, picture=? WHERE id=?");
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getTitle());
            ps.setString(4, employee.getDepartment());
            ps.setInt(5, employee.getManager().getId());
            ps.setString(6, employee.getCity());
            ps.setString(7, employee.getOfficePhone());
            ps.setString(8, employee.getCellPhone());
            ps.setString(9, employee.getEmail());
            ps.setString(10, employee.getPicture());
            ps.setInt(11, employee.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
        return employee;
    }

    public boolean remove(Employee employee) {
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement("DELETE FROM employee WHERE id=?");
            ps.setInt(1, employee.getId());
            int count = ps.executeUpdate();
            return count == 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
    }

    protected Employee processRow(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setFirstName(rs.getString("firstName"));
        employee.setLastName(rs.getString("lastName"));
        employee.setTitle(rs.getString("title"));
        employee.setDepartment(rs.getString("department"));
        employee.setCity(rs.getString("city"));
        employee.setOfficePhone(rs.getString("officePhone"));
        employee.setCellPhone(rs.getString("cellPhone"));
        employee.setEmail(rs.getString("email"));
        employee.setPicture(rs.getString("picture"));
        int managerId = rs.getInt("managerId");
        if (managerId > 0) {
            Employee manager = new Employee();
            manager.setId(managerId);
            manager.setFirstName(rs.getString("managerFirstName"));
            manager.setLastName(rs.getString("managerLastName"));
            employee.setManager(manager);
        }
        employee.setReportCount(rs.getInt("reportCount"));
        return employee;
    }

    protected UserPass processSummaryRow(ResultSet rs) throws SQLException {
        UserPass uP = new UserPass();
        uP.setId(rs.getInt("uid"));
        uP.setPassword(rs.getString("password"));
        return uP;
    }
}
