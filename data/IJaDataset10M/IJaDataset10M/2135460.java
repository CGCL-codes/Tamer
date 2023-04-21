package de.sreindl.amavisadmin;

import com.sun.data.provider.impl.TableRowDataProvider;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.rave.web.ui.component.Body;
import com.sun.rave.web.ui.component.Button;
import com.sun.rave.web.ui.component.Form;
import com.sun.rave.web.ui.component.Head;
import com.sun.rave.web.ui.component.Html;
import com.sun.rave.web.ui.component.Hyperlink;
import com.sun.rave.web.ui.component.Link;
import com.sun.rave.web.ui.component.Page;
import com.sun.rave.web.ui.component.PanelGroup;
import com.sun.rave.web.ui.component.StaticText;
import com.sun.rave.web.ui.component.Table;
import com.sun.rave.web.ui.component.TableColumn;
import com.sun.rave.web.ui.component.TableRowGroup;
import de.sreindl.amavisadmin.db.Job;
import de.sreindl.amavisadmin.db.util.HibernateSessionFactory;
import de.sreindl.amavisadmin.jobs.AdminNotifierJob;
import de.sreindl.amavisadmin.jobs.BaseJob;
import de.sreindl.amavisadmin.jobs.MailSendJob;
import de.sreindl.amavisadmin.jobs.MessagesCleanupJob;
import de.sreindl.amavisadmin.jobs.NewMailNotifierJob;
import de.sreindl.amavisadmin.jobs.ReleaseMailJob;
import de.sreindl.amavisadmin.jobs.ReminderJob;
import de.sreindl.amavisadmin.jobs.RetentionCleanupJob;
import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 */
public class EditJobs extends AbstractPageBean {

    private int __placeholder;

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private Page page1 = new Page();

    public Page getPage1() {
        return page1;
    }

    public void setPage1(Page p) {
        this.page1 = p;
    }

    private Html html1 = new Html();

    public Html getHtml1() {
        return html1;
    }

    public void setHtml1(Html h) {
        this.html1 = h;
    }

    private Head head1 = new Head();

    public Head getHead1() {
        return head1;
    }

    public void setHead1(Head h) {
        this.head1 = h;
    }

    private Link link1 = new Link();

    public Link getLink1() {
        return link1;
    }

    public void setLink1(Link l) {
        this.link1 = l;
    }

    private Body body1 = new Body();

    public Body getBody1() {
        return body1;
    }

    public void setBody1(Body b) {
        this.body1 = b;
    }

    private Form form1 = new Form();

    public Form getForm1() {
        return form1;
    }

    public void setForm1(Form f) {
        this.form1 = f;
    }

    private Table table1 = new Table();

    public Table getTable1() {
        return table1;
    }

    public void setTable1(Table t) {
        this.table1 = t;
    }

    private TableRowGroup tableRowGroup1 = new TableRowGroup();

    public TableRowGroup getTableRowGroup1() {
        return tableRowGroup1;
    }

    public void setTableRowGroup1(TableRowGroup trg) {
        this.tableRowGroup1 = trg;
    }

    private TableColumn tableColumn1 = new TableColumn();

    public TableColumn getTableColumn1() {
        return tableColumn1;
    }

    public void setTableColumn1(TableColumn tc) {
        this.tableColumn1 = tc;
    }

    private TableColumn tableColumn2 = new TableColumn();

    public TableColumn getTableColumn2() {
        return tableColumn2;
    }

    public void setTableColumn2(TableColumn tc) {
        this.tableColumn2 = tc;
    }

    private StaticText staticText2 = new StaticText();

    public StaticText getStaticText2() {
        return staticText2;
    }

    public void setStaticText2(StaticText st) {
        this.staticText2 = st;
    }

    private TableColumn tableColumn3 = new TableColumn();

    public TableColumn getTableColumn3() {
        return tableColumn3;
    }

    public void setTableColumn3(TableColumn tc) {
        this.tableColumn3 = tc;
    }

    private StaticText staticText3 = new StaticText();

    public StaticText getStaticText3() {
        return staticText3;
    }

    public void setStaticText3(StaticText st) {
        this.staticText3 = st;
    }

    private TableColumn tableColumn6 = new TableColumn();

    public TableColumn getTableColumn6() {
        return tableColumn6;
    }

    public void setTableColumn6(TableColumn tc) {
        this.tableColumn6 = tc;
    }

    private StaticText staticText5 = new StaticText();

    public StaticText getStaticText5() {
        return staticText5;
    }

    public void setStaticText5(StaticText st) {
        this.staticText5 = st;
    }

    private TableColumn tableColumn4 = new TableColumn();

    public TableColumn getTableColumn4() {
        return tableColumn4;
    }

    public void setTableColumn4(TableColumn tc) {
        this.tableColumn4 = tc;
    }

    private Hyperlink hlToogle = new Hyperlink();

    public Hyperlink getHlToogle() {
        return hlToogle;
    }

    public void setHlToogle(Hyperlink h) {
        this.hlToogle = h;
    }

    private Button createDefault = new Button();

    public Button getCreateDefault() {
        return createDefault;
    }

    public void setCreateDefault(Button b) {
        this.createDefault = b;
    }

    private Button btnAddNewEntry = new Button();

    public Button getBtnAddNewEntry() {
        return btnAddNewEntry;
    }

    public void setBtnAddNewEntry(Button b) {
        this.btnAddNewEntry = b;
    }

    private Hyperlink hlEditEntry = new Hyperlink();

    public Hyperlink getHlEditEntry() {
        return hlEditEntry;
    }

    public void setHlEditEntry(Hyperlink h) {
        this.hlEditEntry = h;
    }

    private TableColumn tableColumn5 = new TableColumn();

    public TableColumn getTableColumn5() {
        return tableColumn5;
    }

    public void setTableColumn5(TableColumn tc) {
        this.tableColumn5 = tc;
    }

    private Button btnDeleteJob = new Button();

    public Button getBtnDeleteJob() {
        return btnDeleteJob;
    }

    public void setBtnDeleteJob(Button b) {
        this.btnDeleteJob = b;
    }

    private PanelGroup groupPanel1 = new PanelGroup();

    public PanelGroup getGroupPanel1() {
        return groupPanel1;
    }

    public void setGroupPanel1(PanelGroup pg) {
        this.groupPanel1 = pg;
    }

    private PanelGroup groupPanel2 = new PanelGroup();

    public PanelGroup getGroupPanel2() {
        return groupPanel2;
    }

    public void setGroupPanel2(PanelGroup pg) {
        this.groupPanel2 = pg;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public EditJobs() {
    }

    /**
     * <p>Callback method that is called whenever a page is navigated to,
     * either directly via a URL, or indirectly via page navigation.
     * Customize this method to acquire resources that will be needed
     * for event handlers and lifecycle methods, whether or not this
     * page is performing post back processing.</p>
     *
     * <p>Note that, if the current request is a postback, the property
     * values of the components do <strong>not</strong> represent any
     * values submitted with this request.  Instead, they represent the
     * property values that were saved for this view when it was rendered.</p>
     */
    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("EditJobs Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
        String redirect = null;
        if (!getSessionBean1().getLoggedOn()) {
            redirect = "Login.jsp";
        } else if (!getSessionBean1().getAdminUser()) {
            redirect = "MyRequests.jsp";
        }
        if (redirect != null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(redirect);
                return;
            } catch (IOException ioe) {
                log(ioe.getMessage(), ioe);
                return;
            }
        }
    }

    /**
     * <p>Callback method that is called after the component tree has been
     * restored, but before any event processing takes place.  This method
     * will <strong>only</strong> be called on a postback request that
     * is processing a form submit.  Customize this method to allocate
     * resources that will be required in your event handlers.</p>
     */
    public void preprocess() {
    }

    /**
     * <p>Callback method that is called just before rendering takes place.
     * This method will <strong>only</strong> be called for the page that
     * will actually be rendered (and not, for example, on a page that
     * handled a postback and then navigated to a different page).  Customize
     * this method to allocate resources that will be required for rendering
     * this page.</p>
     */
    public void prerender() {
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called (regardless of whether
     * or not this was the page that was actually rendered).  Customize this
     * method to release resources acquired in the <code>init()</code>,
     * <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).</p>
     */
    public void destroy() {
        HibernateSessionFactory.closeSession();
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    public String getEnabledDisabledText() {
        TableRowDataProvider trdp = (TableRowDataProvider) getBean("currentRow");
        if (trdp == null) {
            return "???";
        }
        Boolean content = (Boolean) trdp.getValue("job_enabled");
        if (content.booleanValue()) {
            return "Enabled";
        } else {
            return "Disabled";
        }
    }

    public String hlEditEntry_action() {
        TableRowDataProvider trdp = (TableRowDataProvider) getBean("currentRow");
        if (trdp == null) {
            error("Cannot retrieve current row");
            return null;
        }
        String jobName = (String) trdp.getValue("job_name");
        getSessionBean1().setParameter(EditSingleJob.KEY_JOB_NAME, jobName);
        getSessionBean1().setParameter(EditSingleJob.KEY_JOB, null);
        return "editJob";
    }

    public String hlToogle_action() {
        Session session = HibernateSessionFactory.getSession();
        Transaction trx = null;
        Job job = null;
        TableRowDataProvider trdp = (TableRowDataProvider) getBean("currentRow");
        if (trdp == null) {
            error("Cannot retrieve current row?");
            return null;
        }
        String jobName = (String) trdp.getValue("job_name");
        try {
            trx = session.beginTransaction();
            job = (Job) session.load(Job.class, jobName);
            assert (job != null);
            job.setEnabled(Boolean.valueOf(!job.getEnabled().booleanValue()));
            session.update(job);
            trx.commit();
            getSessionBean1().getJobsDataProvider().refresh();
            StdSchedulerFactory factory = (StdSchedulerFactory) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
            BaseJob.scheduleJobs(factory);
        } catch (HibernateException he) {
            error(he.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot update entry " + jobName, he.getMessage()));
            try {
                if (session != null) {
                    if (trx != null && trx.isActive()) {
                        trx.rollback();
                    }
                }
            } catch (HibernateException he1) {
                log(he1.getMessage(), he1);
            }
        }
        return null;
    }

    public String createDefault_action() {
        Session session = HibernateSessionFactory.getSession();
        Transaction trx = null;
        Job job = null;
        try {
            trx = session.beginTransaction();
            job = new Job();
            job.setJobName("Message Cleanup");
            job.setDescription("Removes messages marked for deletion or orphans");
            job.setCronSettings("0 3 0/2 * * ?");
            job.setJobClass(MessagesCleanupJob.class);
            job.setEnabled(Boolean.FALSE);
            session.save(job);
            job = new Job();
            job.setJobName("Message Retention");
            job.setDescription("Marks mails as to be deleted according to user settings");
            job.setCronSettings("0 3 1/2 * * ?");
            job.setJobClass(RetentionCleanupJob.class);
            job.setEnabled(Boolean.FALSE);
            session.save(job);
            job = new Job();
            job.setJobName("Mail Delivery");
            job.setDescription("Mail Delivery Job: Please review parameters before actually starting the job");
            job.setCronSettings("0 0/15 * * * ?");
            job.setJobClass(MailSendJob.class);
            job.setEnabled(Boolean.FALSE);
            session.save(job);
            job = new Job();
            job.setJobName("Mail Warnings");
            job.setDescription("Send Warning mails to users about mails going to be deleted soon. Please Review parameters before starting this job");
            job.setCronSettings("0 0 0 * * ?");
            job.setJobClass(ReminderJob.class);
            job.setEnabled(Boolean.FALSE);
            session.save(job);
            job = new Job();
            job.setJobName("Admin Warnings Mo-Fr");
            job.setDescription("Send Mails to admins during the week");
            job.setCronSettings("0 0 8-16/2 ? * MON-FRI");
            job.setJobClass(AdminNotifierJob.class);
            job.setEnabled(Boolean.FALSE);
            session.save(job);
            job = new Job();
            job.setJobName("Admin Warnings Weekend");
            job.setDescription("Send Mails to admins during for the weekends");
            job.setCronSettings("0 0 8,16 ? * SUN,SAT");
            job.setJobClass(AdminNotifierJob.class);
            job.setEnabled(Boolean.FALSE);
            session.save(job);
            job = new Job();
            job.setJobName("New SPAM Mail Notifier");
            job.setDescription("Send mails to users about new mails arrived. Please review parameters before enabling this job!");
            job.setCronSettings("0 0 7 * * ?");
            job.setJobClass(NewMailNotifierJob.class);
            job.setEnabled(Boolean.FALSE);
            session.save(job);
            job = new Job();
            job.setJobName("Release Mail Daemon");
            job.setDescription("Job responsible for releasing mails approved by an admin. Please review parameters before enabling this job!");
            job.setCronSettings("0 2/15 * * * ?");
            job.setJobClass(ReleaseMailJob.class);
            job.setEnabled(Boolean.FALSE);
            session.save(job);
            trx.commit();
            getSessionBean1().getJobsDataProvider().refresh();
            StdSchedulerFactory factory = (StdSchedulerFactory) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
            BaseJob.scheduleJobs(factory);
        } catch (HibernateException he) {
            error(he.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot create default job entries", he.getMessage()));
            try {
                if (session != null) {
                    if (trx != null && trx.isActive()) {
                        trx.rollback();
                    }
                }
            } catch (HibernateException he1) {
                log(he1.getMessage(), he1);
            }
        }
        return null;
    }

    public String btnAddNewEntry_action() {
        getSessionBean1().setParameter(EditSingleJob.KEY_JOB_NAME, null);
        getSessionBean1().setParameter(EditSingleJob.KEY_JOB, null);
        return "editJob";
    }
}
