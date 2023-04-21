package net.cygeek.tech.client.service;

import java.util.ArrayList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.cygeek.tech.client.logic.EducationLogic;
import net.cygeek.tech.client.HsHrEducation;
import net.cygeek.tech.client.data.Education;
import java.util.Date;

/**
 * Author: Thilina Hasantha
 */
public class EducationServiceImpl extends RemoteServiceServlet implements EducationService {

    EducationLogic logic = EducationLogic.getInstance();

    public ArrayList getEducations() {
        ArrayList a = new ArrayList();
        ArrayList<HsHrEducation> k = logic.getEducations();
        for (HsHrEducation h : k) {
            a.add(Education.getProxy(h));
        }
        return a;
    }

    public Boolean addEducation(Education mEducation, boolean isNew) {
        HsHrEducation bean = Education.getClass(mEducation);
        bean.setNew(isNew);
        return logic.addEducation(bean);
    }

    public Boolean deleteEducation(String eduCode) {
        return logic.delEducation(eduCode);
    }

    public Education getEducation(String eduCode) {
        HsHrEducation k = logic.getEducation(eduCode);
        return Education.getProxy(k);
    }
}
