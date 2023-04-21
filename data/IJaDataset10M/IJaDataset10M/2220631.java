package com.zhiyun.zyxy.web.action;

import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import net.sf.json.JSONObject;
import atg.taglib.json.util.JSONArray;
import com.zhiyun.estore.common.Page;
import com.zhiyun.estore.common.action.BaseActionSupport;
import com.zhiyun.zyxy.entity.XySchool;
import com.zhiyun.zyxy.entity.XySchoolMaster;
import com.zhiyun.zyxy.service.SchoolService;
import com.zhiyun.zyxy.service.UserService;

public class AdminAction extends BaseActionSupport {

    private static final long serialVersionUID = 1L;

    private SchoolService schoolService;

    private UserService userService;

    public void getSchoolMasterUser() {
        renderJSON(userService.getSchoolMasterUser(Boolean.valueOf(getStringParam("isSchoolMaster")), getIntegerParam("schoolId")));
    }

    public void listSchools() {
        Integer start = getIntegerParam("start");
        Integer limit = getIntegerParam("limit");
        JSONObject result = new JSONObject();
        if (start != null && limit != null) {
            Page<XySchool> schoolPage = schoolService.pagedQuery(start, limit);
            JSONArray data = getSchoolJson(schoolPage.getData());
            result.accumulate("totalCount", schoolPage.getTotalCount());
            result.accumulate("data", data);
        } else {
            result.accumulate("data", getSchoolJson(schoolService.getAll()));
        }
        renderJSON(result.toString());
    }

    @SuppressWarnings("unchecked")
    private JSONArray getSchoolJson(List<XySchool> schools) {
        JSONArray data = new JSONArray();
        for (XySchool xySchool : schools) {
            JSONObject schoolJson = new JSONObject();
            schoolJson.accumulate("id", xySchool.getId());
            schoolJson.accumulate("schoolName", xySchool.getSchoolName());
            schoolJson.accumulate("xySchoolType", xySchool.getXySchoolType().getId());
            Set<XySchoolMaster> xySchoolMasters = xySchool.getXySchoolMasters();
            String[] schoolMastersName = new String[xySchoolMasters.size()];
            int i = 0;
            for (XySchoolMaster xySchoolMaster : xySchoolMasters) {
                schoolMastersName[i++] = (xySchoolMaster.getXyUser().getUserName());
            }
            schoolJson.accumulate("xySchoolMasters", StringUtils.join(schoolMastersName, ","));
            schoolJson.accumulate("xyClasses", xySchool.getXyClasses().size());
            data.add(schoolJson);
        }
        return data;
    }

    public void saveSchoolInfo() {
        Integer id = getIntegerParam("id");
        Integer schoolType = getIntegerParam("xySchoolType");
        String schoolMasters = getStringParam("xySchoolMasters");
        String schoolName = getStringParam("schoolName");
        schoolService.saveSchoolInfo(id, schoolType, schoolMasters, schoolName);
        renderJSON("{success:true}");
    }

    public void deleteSchoolInfo() {
        Integer id = getIntegerParam("id");
        schoolService.delete(schoolService.get(id));
        renderJSON("{success:true}");
    }

    public void addSchoolInfo() {
        Integer schoolType = getIntegerParam("xySchoolType");
        String schoolMasters = getStringParam("xySchoolMasters");
        String schoolName = getStringParam("schoolName");
        schoolService.addSchoolInfo(schoolType, schoolMasters, schoolName);
        renderJSON("{success:true}");
    }

    public SchoolService getSchoolService() {
        return schoolService;
    }

    public void setSchoolService(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
