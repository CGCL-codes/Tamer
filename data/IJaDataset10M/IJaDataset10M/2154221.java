package assets.plan.webapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import assets.company.model.Company;
import assets.plan.model.InvPlan;
import assets.plan.service.InvPlanManager;
import com.systop.common.modules.security.user.LoginUserService;
import com.systop.core.Constants;
import com.systop.core.util.ReflectUtil;
import com.systop.core.webapp.struts2.action.ExtJsCrudAction;

@SuppressWarnings("unchecked")
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InvPlanAction extends ExtJsCrudAction<InvPlan, InvPlanManager> {

    private String yearFrom;

    private String yearTo;

    private Map planMap;

    @Autowired
    private LoginUserService lus;

    @Override
    public String index() {
        StringBuffer hql = new StringBuffer("select i from InvPlan i where 1=1");
        List args = new ArrayList();
        Company company = lus.getLoginUserCompany(getRequest());
        if (company == null) {
            throw new org.acegisecurity.AccessDeniedException("请登录！");
        }
        if (!StringUtils.equals(Constants.YES, company.getIsHead())) {
            hql.append(" and i.company.id=").append(company.getId());
        }
        if (getModel() != null && getModel().getCompany() != null && getModel().getCompany().getId() != null) {
            hql.append(" and i.company.id=?");
            args.add(getModel().getCompany().getId());
        }
        if (StringUtils.isNotBlank(yearFrom)) {
            hql.append(" and i.planYear>=?");
            args.add(yearFrom);
        }
        if (StringUtils.isNotBlank(yearTo)) {
            hql.append(" and i.planYear<=?");
            args.add(yearTo);
        }
        items = getManager().query(hql.toString(), args.toArray());
        return INDEX;
    }

    @Override
    public String edit() {
        if (isJsonReq()) {
            planMap = ReflectUtil.toMap(getModel(), null, true);
            planMap.put("company", getModel().getCompany().getName());
        }
        return (isJsonReq()) ? JSON_EDIT : INPUT;
    }

    /**
   * 返回公司列表，用于页面选择公司
   */
    public List<Company> getCompanies() {
        List list = new ArrayList();
        Company company = lus.getLoginUserCompany(getRequest());
        if (!StringUtils.equals(Constants.YES, company.getIsHead())) {
            list.add(company);
        } else {
            list = getManager().getDao().query("select c from Company c order by c.isHead desc");
        }
        return list;
    }

    public String getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(String yearFrom) {
        this.yearFrom = yearFrom;
    }

    public String getYearTo() {
        return yearTo;
    }

    public void setYearTo(String yearTo) {
        this.yearTo = yearTo;
    }

    public Map getPlanMap() {
        return planMap;
    }
}
