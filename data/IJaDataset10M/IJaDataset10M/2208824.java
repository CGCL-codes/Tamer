package net.simpleframework.organization;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.applets.notification.ENotificationEvent;
import net.simpleframework.applets.notification.MailMessageNotification;
import net.simpleframework.applets.notification.NotificationLogBean;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.core.ApplicationModuleException;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.account.Account;
import net.simpleframework.organization.account.AccountContext;
import net.simpleframework.organization.account.AccountLog;
import net.simpleframework.organization.account.AccountManager;
import net.simpleframework.organization.account.AccountViewLog;
import net.simpleframework.organization.account.EAccountStatus;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.component.register.UserRegisterUtils;
import net.simpleframework.organization.impl.Department;
import net.simpleframework.organization.impl.DepartmentManager;
import net.simpleframework.organization.impl.Job;
import net.simpleframework.organization.impl.JobChart;
import net.simpleframework.organization.impl.JobChartManager;
import net.simpleframework.organization.impl.JobManager;
import net.simpleframework.organization.impl.JobMember;
import net.simpleframework.organization.impl.JobMemberManager;
import net.simpleframework.organization.impl.User;
import net.simpleframework.organization.impl.UserLob;
import net.simpleframework.organization.impl.UserManager;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class OrganizationApplicationModule extends AbstractWebApplicationModule implements IOrganizationApplicationModule {

    @Override
    protected void putTables(final Map<Class<?>, Table> tables) {
        tables.put(Account.class, new Table("simple_account"));
        tables.put(AccountLog.class, new Table("simple_account_log"));
        tables.put(AccountViewLog.class, new Table("simple_account_view_log"));
        tables.put(User.class, new Table("simple_user"));
        tables.put(Job.class, new Table("simple_job"));
        tables.put(Department.class, new Table("simple_department"));
        tables.put(JobChart.class, new Table("simple_chart"));
        tables.put(JobMember.class, new Table("simple_job_member", new String[] { "jobId", "memberType", "memberId" }));
        tables.put(UserLob.class, new Table("simple_user_lob", true));
    }

    private UserManager userMgr;

    public UserManager getUserMgr() {
        if (userMgr == null) {
            userMgr = new UserManager();
        }
        return userMgr;
    }

    public void setUserMgr(final UserManager userMgr) {
        this.userMgr = userMgr;
    }

    private DepartmentManager departmentMgr;

    public DepartmentManager getDepartmentMgr() {
        if (departmentMgr == null) {
            departmentMgr = new DepartmentManager();
        }
        return departmentMgr;
    }

    public void setDepartmentMgr(final DepartmentManager departmentMgr) {
        this.departmentMgr = departmentMgr;
    }

    private AccountManager accountMgr;

    public AccountManager getAccountMgr() {
        if (accountMgr == null) {
            accountMgr = new AccountManager();
        }
        return accountMgr;
    }

    public void setAccountMgr(final AccountManager accountMgr) {
        this.accountMgr = accountMgr;
    }

    private JobManager jobMgr;

    public JobManager getJobMgr() {
        if (jobMgr == null) {
            jobMgr = new JobManager();
        }
        return jobMgr;
    }

    public void setJobMgr(final JobManager jobMgr) {
        this.jobMgr = jobMgr;
    }

    private JobMemberManager jobMemberMgr;

    public JobMemberManager getJobMemberMgr() {
        if (jobMemberMgr == null) {
            jobMemberMgr = new JobMemberManager();
        }
        return jobMemberMgr;
    }

    public void setJobMemberMgr(final JobMemberManager jobMemberMgr) {
        this.jobMemberMgr = jobMemberMgr;
    }

    private JobChartManager jobChartMgr;

    public JobChartManager getJobChartMgr() {
        if (jobChartMgr == null) {
            jobChartMgr = new JobChartManager();
        }
        return jobChartMgr;
    }

    public void setJobChartMgr(final JobChartManager jobChartMgr) {
        this.jobChartMgr = jobChartMgr;
    }

    @Override
    public void init(final IInitializer initializer) {
        super.init(initializer);
        LocaleI18n.addBasename(OrganizationApplicationModule.class);
        doInit(OrgUtils.class, OrgInitializer.deployName);
        try {
            JobChartManager.init();
            AccountContext.init();
        } catch (final IOException e) {
            throw ApplicationModuleException.wrapException(e);
        }
        ((ITaskExecutorAware) getApplication()).getTaskExecutor().addScheduledTask(DateUtils.HOUR_PERIOD, DateUtils.DAY_PERIOD * 2, new ExecutorRunnable() {

            @Override
            protected void task() throws IOException {
                doAccountMail();
            }
        });
    }

    @Override
    public String getAccountRuleUrl(final PageRequestResponse requestResponse) {
        return OrgUtils.deployPath + "jsp/account_rule.jsp";
    }

    private String loginUrl;

    @Override
    public String getLoginUrl(final PageRequestResponse requestResponse) {
        return StringUtils.text(loginUrl, OrgUtils.deployPath + "jsp/default_login.jsp");
    }

    public void setLoginUrl(final String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    public void mailRegistActivation2(final IAccount account) {
        UserRegisterUtils.sentMailActivation(account, OrganizationApplicationModule.class, "account_active2.html");
    }

    @Override
    public void mailNoLogin(final IAccount account) {
        final MailMessageNotification mailMessage = new MailMessageNotification();
        mailMessage.setHtmlContent(true);
        final IUser user = account.user();
        if (user == null) {
            return;
        }
        mailMessage.getTo().add(user);
        mailMessage.setSubject(LocaleI18n.getMessage("OrganizationApplicationModule.0", getApplication().getApplicationConfig().getTitle()));
        final Map<String, Object> variable = new HashMap<String, Object>();
        variable.put("serverUrl", getApplication().getApplicationConfig().getServerUrl());
        variable.put("lastlogindate", ConvertUtils.toDateString(account.getLastLoginDate()));
        variable.put("account", user.getName());
        try {
            mailMessage.setTextBody(ScriptEvalUtils.replaceExpr(variable, IoUtils.getStringFromInputStream(OrganizationApplicationModule.class.getClassLoader().getResourceAsStream("net/simpleframework/organization/account_no_login.html"))));
            NotificationUtils.sendMessage(mailMessage);
        } catch (final IOException e) {
            throw ApplicationModuleException.wrapException(e);
        }
    }

    protected void doAccountMail() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        IQueryEntitySet<?> qs = OrgUtils.am().query(new ExpressionValue("status=?", new Object[] { EAccountStatus.register }));
        IAccount account;
        while ((account = (IAccount) qs.next()) != null) {
            if (account.getCreateDate().before(cal.getTime())) {
                OrgUtils.um().delete(new ExpressionValue("id=?", new Object[] { account.getId() }));
            } else {
                mailRegistActivation2(account);
            }
        }
        final ITableEntityManager log_mgr = NotificationUtils.getTableEntityManager(NotificationLogBean.class);
        qs = OrgUtils.am().query(new ExpressionValue("lastlogindate<?", new Object[] { cal.getTime() }));
        while ((account = (IAccount) qs.next()) != null) {
            if (account.getStatus() == EAccountStatus.normal) {
                NotificationLogBean log = log_mgr.queryForObject(new ExpressionValue("toId=? and notificationEvent=? order by sentDate desc", new Object[] { account.getId(), ENotificationEvent.mail_no_login }), NotificationLogBean.class);
                if (log != null && log.getSentDate().after(cal.getTime())) {
                    continue;
                }
                mailNoLogin(account);
                log = new NotificationLogBean();
                log.setNotificationEvent(ENotificationEvent.mail_no_login);
                log.setToId(account.getId());
                log.setSentDate(new Date());
                log_mgr.insert(log);
            }
        }
    }
}
