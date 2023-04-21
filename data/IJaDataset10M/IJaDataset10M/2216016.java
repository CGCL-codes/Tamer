package net.simpleframework.organization.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.EJobType;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.IJobRule;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Job extends Catalog implements IJob, IEntityBeanAware {

    private static final long serialVersionUID = -1147698313735221898L;

    private EJobType jobType;

    private String ruleHandle, ruleScript;

    @Override
    public ID getJobChartId() {
        return getDocumentId();
    }

    @Override
    public void setJobChartId(final ID jobChartId) {
        setDocumentId(jobChartId);
    }

    @Override
    public EJobType getJobType() {
        return jobType == null ? EJobType.normal : jobType;
    }

    @Override
    public void setJobType(final EJobType jobType) {
        this.jobType = jobType;
    }

    @Override
    public String getRuleHandle() {
        return ruleHandle;
    }

    @Override
    public void setRuleHandle(final String ruleHandle) {
        this.ruleHandle = ruleHandle;
        jobRuleHandle = null;
    }

    @Override
    public String getRuleScript() {
        return ruleScript;
    }

    @Override
    public void setRuleScript(final String ruleScript) {
        this.ruleScript = ruleScript;
    }

    private static Map<String, Column> columns = new HashMap<String, Column>();

    static {
        columns.put("id", new Column("id"));
        columns.put("jobChartId", new Column("jobchartid"));
        columns.put("parentId", new Column("parentid"));
        columns.put("name", new Column("name"));
        columns.put("text", new Column("text"));
        columns.put("jobType", new Column("jobtype"));
        columns.put("ruleHandle", new Column("rulehandle"));
        columns.put("ruleScript", new Column("rulescript"));
        columns.put("description", new Column("description"));
        columns.put("oorder", new Column("oorder"));
    }

    @Override
    public Map<String, Column> getTableColumnDefinition() {
        return columns;
    }

    private IJobRule jobRuleHandle;

    @Override
    public IJobRule jobRuleHandle() {
        String hdl;
        if (jobRuleHandle == null && getJobType() == EJobType.handle && StringUtils.hasText(hdl = getRuleHandle())) {
            jobRuleHandle = (IJobRule) BeanUtils.newInstance(hdl);
        }
        return jobRuleHandle;
    }

    @Override
    public IJob parent() {
        return OrgUtils.jm().queryForObjectById(getParentId());
    }

    @Override
    public Collection<? extends IJob> children() {
        return OrgUtils.jm().children(this);
    }

    @Override
    public Collection<? extends IJob> root() {
        return OrgUtils.jm().children(jobChart());
    }

    @Override
    public IJobChart jobChart() {
        return OrgUtils.jcm().queryForObjectById(getJobChartId());
    }

    @Override
    public Collection<? extends IUser> users() {
        return OrgUtils.jm().users(this);
    }

    @Override
    public boolean member(final IUser user) {
        return OrgUtils.jm().member(user, this);
    }
}
