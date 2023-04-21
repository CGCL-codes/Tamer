package com.ems.client.action.biz.basicInfo.projectManager;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import com.ems.biz.basicInfo.bs.IBasicInfoBS;
import com.ems.common.model.vo.ProjectVO;
import com.ems.common.util.BeanUtils;
import com.ems.system.client.DirectAction;
import com.ems.system.client.vo.ExtFormVO;
import com.ems.system.client.vo.ExtPagingVO;
import com.google.gson.JsonArray;
import com.softwarementors.extjs.djn.config.annotations.DirectFormPostMethod;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import com.softwarementors.extjs.djn.servlet.ssm.ActionScope;
import com.softwarementors.extjs.djn.servlet.ssm.Scope;
import conf.hibernate.ProjectBO;

/**
 * 项目管理
 * @author zhuchaole
 *
 */
@ActionScope(scope = Scope.APPLICATION)
public class ProjectAction extends DirectAction {

    private IBasicInfoBS basicInfoBS = this.getBean("basicInfoBS", IBasicInfoBS.class);

    @DirectMethod
    public ExtPagingVO loadProject(JsonArray params) {
        ProjectVO projectVO_qry = BeanUtils.toBeanFromJsonFirst(params, ProjectVO.class);
        List<ProjectBO> projects = basicInfoBS.findProjectByVO(projectVO_qry);
        return new ExtPagingVO(projects);
    }

    @DirectFormPostMethod
    public ExtFormVO create(Map<String, String> formParameters, Map<String, FileItem> fileFields) {
        ProjectBO projectBO = BeanUtils.toBeanFromMap(formParameters, ProjectBO.class);
        ExtFormVO result = new ExtFormVO();
        List<ProjectBO> projects = basicInfoBS.getAll(ProjectBO.class, null);
        for (ProjectBO project_ : projects) {
            if (project_.getProjectName().equals(projectBO.getProjectName())) {
                result.addError("ProjectName", String.format("项目中文名[%s]已重复", projectBO.getProjectName()));
                return result;
            }
            if (project_.getProjectNameEn().equals(projectBO.getProjectNameEn())) {
                result.addError("ProjectNameEn", String.format("项目英文名[%s]已重复", projectBO.getProjectNameEn()));
                return result;
            }
        }
        projectBO.setCreateTime(new Date());
        basicInfoBS.save(projectBO);
        return result;
    }

    @DirectMethod
    public ExtFormVO read(Integer id) {
        ProjectBO project = null;
        if (id != null) {
            project = basicInfoBS.findById(ProjectBO.class, id);
        }
        return new ExtFormVO(project);
    }

    @DirectFormPostMethod
    public ExtFormVO update(Map<String, String> formParameters, Map<String, FileItem> fileFields) {
        ProjectBO projectBO = BeanUtils.toBeanFromMap(formParameters, ProjectBO.class);
        ExtFormVO result = new ExtFormVO();
        projectBO.setUpdateTime(new Date());
        basicInfoBS.update(projectBO);
        return result;
    }

    @DirectMethod
    public ExtFormVO delete(Integer[] ids) {
        for (Integer id : ids) {
            basicInfoBS.delete(basicInfoBS.findById(ProjectBO.class, id));
        }
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
