package cn.myapps.core.deploy.application.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import cn.myapps.base.action.BaseHelper;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;

public class ApplicationHelper extends BaseHelper {

    static Logger logger = Logger.getLogger(ApplicationHelper.class);

    /**
	 * @param args
	 */
    public ApplicationHelper() throws ClassNotFoundException {
        super(ProcessFactory.createProcess(ApplicationProcess.class));
    }

    public Collection getAppList() {
        Collection rtn = new ArrayList();
        try {
            Collection colls = process.doSimpleQuery(null, null);
            if (colls != null && colls.size() > 0) {
                rtn = colls;
            }
        } catch (Exception e) {
            logger.error("Create instance select error");
            e.printStackTrace();
        }
        return rtn;
    }

    /**
	 * init application type
	 * 
	 * @return
	 */
    public Map getApplicationType() {
        Map map = new LinkedHashMap();
        map.put("", "");
        map.put("00", "{*[Mobile_Applications]*}");
        map.put("01", "{*[Financial_Management]*}");
        map.put("02", "{*[Market_Management]*}");
        map.put("03", "{*[Human_Resources]*}");
        map.put("04", "{*[Customer_Management]*}");
        map.put("05", "{*[Software_Development]*}");
        return map;
    }

    public String getDesc(String application) {
        String rtn = "";
        try {
            ApplicationVO vo = (ApplicationVO) process.doView(application);
            if (vo != null) {
                rtn = vo.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    public ApplicationVO getApplicationById(String id) throws Exception {
        ApplicationVO vo = (ApplicationVO) process.doView(id);
        return vo;
    }

    public String getApplicationNameById(String id) throws Exception {
        return getApplicationById(id).getName();
    }

    public Collection queryApplications(WebUser user, int page, int line) throws Exception {
        ApplicationProcess process = (ApplicationProcess) ProcessFactory.createProcess(ApplicationProcess.class);
        return process.queryApplications(user.getId(), page, line);
    }

    public Collection getListByWebUser(WebUser user) throws Exception {
        return ((ApplicationProcess) process).queryByDomain(user.getDomainid());
    }
}
