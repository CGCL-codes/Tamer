package com.dsp.services.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import com.core.exception.StandardException;
import com.core.util.DspConnection;
import com.core.util.DspUtil;
import com.core.util.GenericServiceHibernate;
import com.dsp.bean.Calc;
import com.dsp.bean.Company;
import com.dsp.bean.CompanyAccount;
import com.dsp.bean.Employee;
import com.dsp.bean.InsureResult;
import com.dsp.bean.InsureResultsSub;
import com.dsp.bean.JobsMessage;
import com.dsp.bean.PersonalAccount;
import com.dsp.bean.Seq;
import com.dsp.services.CalcService;
import com.dsp.services.CompanyAccountService;
import com.dsp.services.CompanyService;
import com.dsp.services.EmployeeService;
import com.dsp.services.InsureResultService;
import com.dsp.services.InsureResultsSubService;
import com.dsp.services.PersonalAccountService;
import com.dsp.services.SeqService;

@Service("calcService")
public class CalcServiceImpl extends GenericServiceHibernate<Calc, Long> implements CalcService {

    private Long jobId;

    /**
	 * 员工
	 */
    private EmployeeService employeeService = null;

    /**
	 * 公司
	 */
    private CompanyService companyService = null;

    /**
	 * 社保个人
	 */
    private PersonalAccountService personalAccountService = null;

    /**
	 * 社保公司
	 */
    private CompanyAccountService companyAccountService = null;

    private SeqService seqService = null;

    /**
	 * 执行社保计算作业
	 * 
	 * @throws Exception
	 */
    @Override
    public Integer calc(Long jobId, Calc c, List<JobsMessage> error) throws Exception {
        this.jobId = jobId;
        List<Employee> list = this.getEmployee(c);
        List<Company> company = this.getCompany(c);
        if (DspUtil.isEmptyList(list)) {
            error.add(addMessage("该范围下没有可计算的员工"));
            return 1;
        }
        Map<String, List<PersonalAccount>> mpa = this.getPersonalAccount(company);
        Map<String, List<CompanyAccount>> mca = this.getCompanyAccount(company);
        List<InsureResult> result = new ArrayList<InsureResult>();
        List<InsureResultsSub> resultSub = new ArrayList<InsureResultsSub>();
        Seq seq = this.getId();
        long RQ = seq.getSeqValue().longValue();
        InsureResult ir = null;
        InsureResultsSub irs = null;
        for (Employee emp : list) {
            boolean pBoolean = true, cBoolean = true;
            Company co = this.getCompany(emp.getCompany(), company);
            List<PersonalAccount> lpa = mpa.get(co.getProvince() + ";" + co.getCity() + ";" + co.getArea() + ";" + emp.getHukou());
            if (DspUtil.isEmptyList(lpa)) {
                error.add(addMessage(co.getCname() + "没有对应的社保个人明细信息"));
            }
            List<CompanyAccount> lca = mca.get(co.getProvince() + ";" + co.getCity() + ";" + co.getArea() + ";" + emp.getHukou());
            if (DspUtil.isEmptyList(lca)) {
                error.add(addMessage(co.getCname() + "没有对应的社保公司明细信息"));
            }
            ir = new InsureResult();
            ir.setId(RQ++);
            ir.setCompany(emp.getCompany());
            ir.setEmpId(emp.getId());
            ir.setEmpName(emp.getEmpname());
            ir.setHukou(emp.getHukou().longValue());
            ir.setYearMonth(c.getCalcYearMonth());
            if (lpa == null) {
                error.add(addMessage("员工:" + emp.getEmpname() + ",没有设置个人社保"));
                pBoolean = false;
            } else {
                for (PersonalAccount pa : lpa) {
                    irs = new InsureResultsSub();
                    irs.setMainId(ir.getId());
                    irs.setInsureType(1);
                    irs.setInsureItem(Long.valueOf(pa.getItemId()));
                    double value = 0;
                    BigDecimal bd = null;
                    bd = pa.getItemId() == 0 ? emp.getProvidentBase() : emp.getInsureBase();
                    if (bd == null) continue;
                    irs.setInsureBase(bd);
                    if (pa.getPersonalType() == 1) {
                        value = bd.doubleValue() * pa.getRate().doubleValue() / 100;
                    } else {
                        value = pa.getFixValue().doubleValue();
                    }
                    value = compare(pa.getPersonalLower(), pa.getPersonalLimit(), value);
                    irs.setInsureMoney(BigDecimal.valueOf(value));
                    resultSub.add(irs);
                }
            }
            if (lca == null) {
                error.add(addMessage("员工:" + emp.getEmpname() + ",没有设置公司社保"));
                cBoolean = false;
            } else {
                for (CompanyAccount ca : lca) {
                    irs = new InsureResultsSub();
                    irs.setMainId(ir.getId());
                    irs.setInsureType(2);
                    irs.setInsureItem(Long.valueOf(ca.getItemId()));
                    BigDecimal bd = null;
                    bd = ca.getItemId() == 0 ? emp.getProvidentBase() : emp.getInsureBase();
                    if (bd == null) continue;
                    irs.setInsureBase(bd);
                    double value = 0;
                    if (ca.getCompanyType() == 1) {
                        value = bd.doubleValue() * ca.getRate().doubleValue() / 100;
                    } else {
                        value = ca.getFixValue().doubleValue();
                    }
                    value = compare(ca.getCompanyLower(), ca.getCompanyLimit(), value);
                    irs.setInsureMoney(BigDecimal.valueOf(value));
                    resultSub.add(irs);
                }
            }
            if (pBoolean && cBoolean) {
                result.add(ir);
            }
        }
        this.save(result, resultSub);
        seq.setSeqValue(Integer.valueOf(RQ + ""));
        this.update(seq);
        return error.size() > 0 ? 1 : 2;
    }

    /**
	 * 保存数据
	 * 
	 * @param list
	 * @param subList
	 * @throws StandardException
	 */
    private void save(List<InsureResult> list, List<InsureResultsSub> subList) throws Exception {
        InsureResultService resultService = (InsureResultService) DspUtil.getBean("insureResultService");
        InsureResultsSubService resultsSubService = (InsureResultsSubService) DspUtil.getBean("insureResultsSubService");
        DetachedCriteria dc = null;
        List<InsureResult> tmpList = null;
        for (InsureResult ir : list) {
            dc = DetachedCriteria.forClass(InsureResult.class);
            dc.add(Restrictions.eq("yearMonth", ir.getYearMonth()));
            dc.add(Restrictions.eq("empId", ir.getEmpId()));
            dc.add(Restrictions.eq("company", ir.getCompany()));
            dc.add(Restrictions.eq("hukou", ir.getHukou()));
            tmpList = resultService.likeBy(dc);
            if (DspUtil.isEmptyList(tmpList) == false) {
                InsureResult __iR = tmpList.get(0);
                resultService.delete(__iR);
                DspConnection.delete(getInsureSub(__iR.getId()));
            }
            resultService.save(ir);
        }
        for (InsureResultsSub irs : subList) {
            resultsSubService.save(irs);
        }
    }

    /**
	 * 比较
	 * 
	 * @param lower
	 * @param limit
	 * @param value
	 * @return
	 */
    private double compare(BigDecimal lower, BigDecimal limit, double value) {
        value = value < lower.doubleValue() ? lower.doubleValue() : value;
        value = value > limit.doubleValue() ? limit.doubleValue() : value;
        return value;
    }

    /**
	 * 主键策略
	 * 
	 * @return
	 */
    private Seq getId() {
        this.seqService = (SeqService) DspUtil.getBean("seqService");
        return this.seqService.findById(Long.valueOf("1"));
    }

    /**
	 * 更新流水号
	 * 
	 * @param seq
	 */
    private void update(Seq seq) throws Exception {
        this.seqService.update(seq);
    }

    /**
	 * 通过key查找对应的company信息
	 * 
	 * @param key
	 * @param listCompany
	 * @return
	 */
    private Company getCompany(Long key, List<Company> listCompany) {
        for (Company c : listCompany) {
            if (c.getId().longValue() == key.longValue()) {
                return c;
            }
        }
        return null;
    }

    private List<Employee> getEmployee(Calc calc) {
        DetachedCriteria dc = DetachedCriteria.forClass(Employee.class);
        if (calc.getCompany() != null && calc.getCompany().size() > 0) {
            dc.add(Restrictions.in("company", calc.getCompany()));
        }
        this.employeeService = (EmployeeService) DspUtil.getBean("employeeService");
        return this.employeeService.likeBy(dc);
    }

    /**
	 * 初始化公司信息
	 * 
	 * @param calc
	 * @return
	 */
    private List<Company> getCompany(Calc calc) {
        DetachedCriteria dc = DetachedCriteria.forClass(Company.class);
        if (calc.getCompany() != null && calc.getCompany().size() > 0) {
            dc.add(Restrictions.in("id", calc.getCompany()));
        }
        this.companyService = (CompanyService) DspUtil.getBean("companyService");
        return this.companyService.likeBy(dc);
    }

    /**
	 * 初始化社保个人信息
	 * 
	 * @return
	 */
    private Map<String, List<PersonalAccount>> getPersonalAccount(List<Company> companyList) {
        DetachedCriteria dc = DetachedCriteria.forClass(PersonalAccount.class);
        Map<String, List<PersonalAccount>> map = new HashMap<String, List<PersonalAccount>>();
        getCondition(companyList, dc);
        this.personalAccountService = (PersonalAccountService) DspUtil.getBean("personalAccountService");
        List<PersonalAccount> personalList = this.personalAccountService.likeBy(dc);
        List<PersonalAccount> tmpList = null;
        String key = null;
        for (PersonalAccount personal : personalList) {
            key = personal.getProvince() + ";" + personal.getCity() + ";" + personal.getArea() + ";" + personal.getHukou();
            if (map.get(key) != null) {
                tmpList = (List<PersonalAccount>) map.get(key);
            } else {
                tmpList = new ArrayList<PersonalAccount>();
                map.put(key, tmpList);
            }
            tmpList.add(personal);
        }
        return map;
    }

    /**
	 * 公司社保信息
	 * 
	 * @param companyList
	 * @return
	 */
    private Map<String, List<CompanyAccount>> getCompanyAccount(List<Company> companyList) {
        DetachedCriteria dc = DetachedCriteria.forClass(CompanyAccount.class);
        Map<String, List<CompanyAccount>> map = new HashMap<String, List<CompanyAccount>>();
        getCondition(companyList, dc);
        this.companyAccountService = (CompanyAccountService) DspUtil.getBean("companyAccountService");
        List<CompanyAccount> companyAccount = this.companyAccountService.likeBy(dc);
        List<CompanyAccount> tmpList = null;
        String key = null;
        for (CompanyAccount personal : companyAccount) {
            key = personal.getProvince() + ";" + personal.getCity() + ";" + personal.getArea() + ";" + personal.getHukou();
            if (map.get(key) != null) {
                tmpList = (List<CompanyAccount>) map.get(key);
            } else {
                tmpList = new ArrayList<CompanyAccount>();
                map.put(key, tmpList);
            }
            tmpList.add(personal);
        }
        return map;
    }

    /**
	 * 得到查询条件
	 * 
	 * @param companyList
	 * @param dc
	 */
    private void getCondition(List<Company> companyList, DetachedCriteria dc) {
        List<String> province = new ArrayList<String>();
        List<String> city = new ArrayList<String>();
        List<String> area = new ArrayList<String>();
        for (Company company : companyList) {
            province.add(company.getProvince());
            city.add(company.getCity());
            area.add(company.getArea());
        }
        if (DspUtil.isEmptyList(province) == false) dc.add(Restrictions.in("province", province));
        if (DspUtil.isEmptyList(city) == false) dc.add(Restrictions.in("city", city));
        if (DspUtil.isEmptyList(area) == false) dc.add(Restrictions.in("area", area));
    }

    private Date[] getDate(String yearMonth) {
        return null;
    }

    /**
	 * 异常信息
	 * 
	 * @param message
	 * @return
	 */
    private JobsMessage addMessage(String message) {
        JobsMessage jm = new JobsMessage();
        jm.setParent(jobId);
        jm.setErrorInfo(message);
        return jm;
    }

    private String getInsureSub(Long fk) {
        return "delete from insure_results_sub where main_id=" + fk;
    }
}
