package com.sibnet.JSP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.sql.DataSource;

public class EmployeeHome extends HomeBase {

    public ArrayList<Employee> findAll() {
        ArrayList<Employee> empList = new ArrayList<Employee>();
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<Integer> idList = new ArrayList<Integer>();
        Connection conn = getConnection();
        DataSource ds = getDataSource();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT name FROM admin.EMPLOYEE");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String s = rs.getString(1);
                nameList.add(s);
                System.out.println("name :" + s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT id FROM admin.EMPLOYEE");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String s = rs.getString(1);
                idList.add(Integer.parseInt(s));
                System.out.println("id: " + s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Integer i : idList) {
            Employee emp = new Employee();
            emp.setId(i);
            int index = idList.indexOf(i);
            String name = nameList.get(index);
            emp.setName(name);
            empList.add(emp);
        }
        System.out.println(empList.size());
        return empList;
    }

    public void insertIt(Employee emp) {
        Connection conn = getConnection();
        DataSource ds = getDataSource();
        try {
            PreparedStatement ps = conn.prepareStatement("insert into admin.employee (id,name) values (" + emp.getId() + ",'" + emp.getName() + "')");
            int res = ps.executeUpdate();
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Employee findEmployee(int id) {
        Employee emp = new Employee();
        Connection conn = getConnection();
        DataSource ds = getDataSource();
        ArrayList<Employee> empList = new ArrayList<Employee>();
        try {
            String sql = "SELECT name From admin.employee where id =" + id;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String s = rs.getString(1);
                Employee e = new Employee();
                e.setId(id);
                e.setName(s);
                emp = e;
                empList.add(e);
                System.out.println("name :" + s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (empList.size() > 1) {
            return empList.get(1);
        } else {
            return emp;
        }
    }
}
