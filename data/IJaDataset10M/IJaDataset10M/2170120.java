package com.ems.client.action.biz.stuMag.addNewStu;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import com.ems.biz.stuMag.bs.IStudentManageBS;
import com.ems.common.util.BeanUtils;
import com.ems.system.client.DirectAction;
import com.ems.system.client.vo.ExtFormVO;
import com.ems.system.client.vo.ExtPagingVO;
import com.google.gson.JsonArray;
import com.softwarementors.extjs.djn.config.annotations.DirectFormPostMethod;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import com.softwarementors.extjs.djn.servlet.ssm.ActionScope;
import com.softwarementors.extjs.djn.servlet.ssm.Scope;
import conf.hibernate.StudentBO;

@ActionScope(scope = Scope.APPLICATION)
public class StuAction extends DirectAction {

    private IStudentManageBS studentManageBS = this.getBean("studentManageBS", IStudentManageBS.class);

    @DirectMethod
    public ExtPagingVO loadStus(JsonArray params) {
        StudentBO studentBO_qry = BeanUtils.toBeanFromJsonFirst(params, StudentBO.class);
        List<StudentBO> students = studentManageBS.findByStudentBO(studentBO_qry);
        return new ExtPagingVO(students);
    }

    @DirectFormPostMethod
    public ExtFormVO create(Map<String, String> formParameters, Map<String, FileItem> fileFields) {
        StudentBO studentBO = BeanUtils.toBeanFromMap(formParameters, StudentBO.class);
        ExtFormVO result = new ExtFormVO();
        studentManageBS.create(studentBO);
        return result;
    }

    @DirectMethod
    public ExtFormVO read(Integer id) {
        StudentBO studentBO = null;
        if (id != null) {
            studentBO = studentManageBS.findById(id);
        }
        return new ExtFormVO(studentBO);
    }

    @DirectFormPostMethod
    public ExtFormVO update(Map<String, String> formParameters, Map<String, FileItem> fileFields) {
        StudentBO studentBO = BeanUtils.toBeanFromMap(formParameters, StudentBO.class);
        ExtFormVO result = new ExtFormVO();
        studentManageBS.update(studentBO);
        return result;
    }

    @DirectMethod
    public ExtFormVO delete(Integer[] ids) {
        studentManageBS.deleteByIds(ids);
        return new ExtFormVO();
    }

    @DirectMethod
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    @DirectMethod
    public void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    @DirectFormPostMethod
    public ExtFormVO batchImport(Map<String, String> formParameters, Map<String, FileItem> fileFields) throws IOException {
        ExtFormVO formVO = new ExtFormVO();
        return formVO;
    }
}
