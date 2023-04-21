package com.mange.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import com.mange.entity.Employee;
import com.mange.services.EmployeeServices;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author coohow
 * @2011-10-31 @上午08:52:56
 * 
 */
@Controller("EmployeeAction")
public class EmployeeAction extends ActionSupport {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Autowired
    private EmployeeServices services;

    private List<Employee> elist;

    /**
	 * 列出所有雇员
	 * 
	 * @return
	 * @throws DataAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
    public String allE() throws DataAccessException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        elist = services.findAllEmployee(null);
        System.out.println("列出所有雇员size=" + elist.size());
        return SUCCESS;
    }

    /**
	 * 保存雇员
	 * 
	 * @return
	 */
    public String saveE() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String workId = request.getParameter("workId");
        String name = request.getParameter("name");
        String technology = request.getParameter("technology");
        Integer workYear = Integer.parseInt(request.getParameter("workYear"));
        Employee entity = new Employee();
        entity.setName(name);
        entity.setTechnology(technology);
        entity.setWorkId(workId);
        entity.setWorkYear(workYear);
        services.addEmployee(entity);
        System.out.println("添加雇员【" + entity.getName() + "】");
        return SUCCESS;
    }

    /**
	 * 修改雇员
	 * 
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
    public String updateE() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        HttpServletRequest request = ServletActionContext.getRequest();
        Integer id = Integer.parseInt(request.getParameter("id"));
        String workId = request.getParameter("workId");
        String name = request.getParameter("name");
        String technology = request.getParameter("technology");
        Integer workYear = Integer.parseInt(request.getParameter("workYear"));
        Employee entity = new Employee();
        entity.setId(id);
        entity.setName(name);
        entity.setTechnology(technology);
        entity.setWorkId(workId);
        entity.setWorkYear(workYear);
        services.modifyEmployee(entity);
        System.out.println("修改雇员【" + entity.getName() + "】");
        return SUCCESS;
    }

    /**
	 * 删除雇员
	 * 
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
    public String deleteE() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HttpServletRequest request = ServletActionContext.getRequest();
        Integer id = Integer.parseInt(request.getParameter("id"));
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(id);
        services.remove(ids);
        System.out.println("删除雇员");
        return SUCCESS;
    }

    public List<Employee> getElist() {
        return elist;
    }

    public void setElist(List<Employee> elist) {
        this.elist = elist;
    }
}
