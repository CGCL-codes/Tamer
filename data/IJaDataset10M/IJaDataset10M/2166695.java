package lokahi.core.api.jobpool;

import org.apache.log4j.Logger;
import lokahi.dao.broker.BrokerFactory;
import lokahi.dao.broker.TMCBroker;
import lokahi.core.api.project.Project;
import lokahi.dao.Job;
import lokahi.core.common.interfaces.Datable;
import lokahi.core.common.interfaces.Pool;
import lokahi.core.common.interfaces.TMCDao;
import lokahi.util.PropertiesFile;
import lokahi.util.collection.TMCCollectionImpl;
import lokahi.core.api.user.User;
import lokahi.core.api.state.State;
import lokahi.core.api.function.Function;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * @author Stephen Toback
 * @version $Id: JobPool.java,v 1.3 2006/03/07 22:05:21 drtobes Exp $
 */
public class JobPool extends TMCDao implements Pool, Datable {

    static final Logger logger = Logger.getLogger(JobPool.class);

    private static final TMCBroker<JobPool> broker = new BrokerFactory<JobPool>().getBroker();

    private int jobPoolId;

    private String jobPoolName;

    private int projectId = -1;

    private int userId;

    private Date startTime;

    private Date finishTime;

    private String options;

    private int functionId;

    private State state;

    private User user;

    private Project project;

    private Function function;

    private Collection<Job> jobs;

    public JobPool() {
    }

    public JobPool(int projectId, int userId, State state, String options, int functionId) {
        this.setProjectId(projectId);
        this.setUserId(userId);
        this.setState(state);
        this.setOptions(options);
        this.setFunctionId(functionId);
        this.setName(null);
    }

    public JobPool(Project p, User u, State s, String options, Function f) {
        this.setProject(p);
        this.setUser(u);
        this.setState(s);
        this.setOptions(options);
        this.setFunction(f);
        this.setName(null);
    }

    public JobPool(User u, State s, Function f) {
        this.setProjectId(-1);
        this.setUserId(u.getPk());
        this.setState(s);
        this.setFunctionId(f.getPk());
        this.setName(null);
    }

    public JobPool(Project p, User u, String options, Function f) {
        this.setProject(p);
        this.setUserId(u.getPk());
        this.setState(State.NEW);
        this.setOptions(options);
        this.setFunctionId(f.getPk());
        this.setName(null);
    }

    public JobPool(int jobPoolId, String jobPoolName, int projectId, int userId, Date startTime, Date finishTime, int stateId, String options, int functionId) {
        this.jobPoolId = jobPoolId;
        this.jobPoolName = jobPoolName;
        this.projectId = projectId;
        this.userId = userId;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.state = State.getState(stateId);
        this.options = options;
        this.functionId = functionId;
    }

    public int getPk() {
        return jobPoolId;
    }

    public void setPk(int pk) {
        this.jobPoolId = pk;
    }

    public String getName() {
        return jobPoolName;
    }

    public void setName(String name) {
        if (name != null) {
            this.jobPoolName = name;
        } else {
            this.jobPoolName = generateName();
        }
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.project = null;
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.user = null;
        this.userId = userId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getOptions() {
        if (options == null) options = "";
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
        this.function = null;
    }

    public Function getFunction() {
        if (this.function == null) {
            this.function = Function.getFunction(this.functionId);
        }
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
        if (function != null) {
            this.functionId = function.getPk();
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public User getUser() {
        if (user == null) {
            try {
                this.user = User.getUser(this.getUserId());
            } catch (SQLException e) {
                if (logger.isInfoEnabled()) {
                    logger.info("Exception: " + e.getMessage());
                }
            }
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getPk();
        }
    }

    public Project getProject() {
        if (project == null && this.projectId != -1) {
            try {
                this.project = Project.getProject(this.projectId);
            } catch (SQLException e) {
                if (logger.isInfoEnabled()) {
                    logger.info("Exception: " + e.getMessage());
                }
            }
        }
        return project;
    }

    public void setProject(Project project) {
        if (project != null) {
            this.projectId = project.getPk();
        }
        this.project = project;
    }

    public Collection<Job> getJobs() throws SQLException {
        if (jobs == null) {
            jobs = Job.getJobs(this.getPk());
        }
        return jobs;
    }

    public TreeMap<String, TreeMap<Integer, Job>> getSortedJobs() {
        TreeMap<String, TreeMap<Integer, Job>> tm = new TreeMap<String, TreeMap<Integer, Job>>();
        Collection<Job> c = null;
        try {
            c = this.getJobs();
        } catch (SQLException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
        }
        if (c != null) {
            TreeMap<Integer, Job> tm2 = new TreeMap<Integer, Job>();
            for (final Job j : c) {
                if (j.getHardware() != null) {
                    if (tm.keySet().contains(j.getHardware().getName())) {
                        tm2 = tm.get(j.getHardware().getName());
                    }
                    tm2.put(j.getPk(), j);
                    tm.put(j.getHardware().getName(), (TreeMap<Integer, Job>) tm2.clone());
                    tm2.clear();
                }
            }
        }
        return tm;
    }

    public void setJobs(Collection<Job> jobs) {
        this.jobs = jobs;
    }

    public static void update(JobPool jp) throws SQLException {
        broker.update("core.jobpool.update", jp.getPk(), jp.getName(), jp.getProjectId(), jp.getUserId(), jp.getState().getPk(), jp.getOptions(), jp.getFunctionId());
    }

    public JobPool correctStates() throws SQLException {
        if (this.getState().getPk() <= 0 || this.getState().getPk() >= 6 || this.getState().getPk() == 3) {
            this.setState(State.RUNNING);
        }
        if (this.getState().getPk() == 4 || this.getState().getPk() == 5) {
            complete(this);
        }
        return this;
    }

    public static JobPool store(JobPool jp) throws SQLException {
        jp.setPk(broker.save("core.jobpool.store", jp.getProjectId(), jp.getUserId(), jp.getState().getPk(), jp.getOptions(), jp.getFunctionId()));
        jp.setName(jp.getName() + jp.getPk());
        if (jp.getPk() > -1) {
            update(jp);
        }
        return jp;
    }

    public static JobPool reportCompleted(JobPool jp) throws SQLException {
        JobPool ret = jp;
        store(jp);
        if (jp != null) {
            complete(jp);
            ret = jp;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Returning " + ret);
        }
        return ret;
    }

    public static void complete(JobPool jp) throws SQLException {
        broker.update("core.jobpool.finalize", jp.getPk(), jp.getState().getPk());
    }

    public static JobPool getJobPool(int id) throws SQLException {
        JobPool jp;
        try {
            jp = broker.getObject(JobPool.class, "core.jobpool.by.id", false, id);
        } catch (SQLException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
            throw e;
        }
        return jp;
    }

    public static Collection<JobPool> getJobPools(Project p) throws SQLException {
        Collection<JobPool> c;
        try {
            c = broker.getObjects(JobPool.class, false, "core.jobpool.by.project", p.getPk());
        } catch (SQLException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
            throw e;
        }
        return c;
    }

    public static Collection<JobPool> getJobPools(User u) throws SQLException {
        Collection<JobPool> c;
        try {
            c = broker.getObjects(JobPool.class, false, "core.jobpool.by.user", u.getPk());
        } catch (SQLException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
            throw e;
        }
        return c;
    }

    public static TMCCollectionImpl<JobPool> getJobPools(User u, Date startDate, Date endDate, String filter, Function... functions) throws SQLException {
        if (endDate == null) {
            endDate = new Date();
        }
        TMCCollectionImpl<JobPool> c = new TMCCollectionImpl<JobPool>();
        try {
            if (u != null && startDate == null && functions == null) {
                c.addAll(broker.getObjects(JobPool.class, false, "core.jobpool.search.user", u.getPk(), filter));
            }
            if (u != null && startDate != null && functions == null) {
                c.addAll(broker.getObjects(JobPool.class, false, "core.jobpool.search.user.date", u.getPk(), filter, startDate));
            }
            if (u != null && startDate == null && functions != null) {
                for (final Function f : functions) {
                    c.addAll(broker.getObjects(JobPool.class, false, "core.jobpool.search.user.function", u.getPk(), filter, f.getPk()));
                }
            }
        } catch (SQLException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
            throw e;
        }
        return c;
    }

    public static Collection<JobPool> getJobPools(State s) throws SQLException {
        Collection<JobPool> c;
        try {
            c = broker.getObjects(JobPool.class, false, "core.jobpool.by.state", s.getPk());
        } catch (SQLException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
            throw e;
        }
        return c;
    }

    public static Collection<JobPool> getJobPoolsLimited(State s) throws SQLException {
        Collection<JobPool> c;
        try {
            c = broker.getObjects(JobPool.class, false, "core.jobpool.by.state.limited", s.getPk());
        } catch (SQLException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
            throw e;
        }
        return c;
    }

    public boolean setCurrentState() throws SQLException {
        boolean ret = false;
        State s;
        Collection<Job> c = null;
        try {
            c = this.getJobs();
        } catch (SQLException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
        }
        if (this.getState().getPk() <= 3) {
            if (c == null || c.isEmpty()) {
                s = State.COMPLETE;
            } else {
                int lowNum = 4000;
                int finalState = -1;
                for (final Job j : c) {
                    if (j.getState().getPk() > finalState && j.getState().getPk() > 3) {
                        finalState = j.getState().getPk();
                        if (logger.isDebugEnabled()) {
                            logger.debug("finalState=" + finalState);
                        }
                    } else if (j.getState().getPk() < lowNum && j.getState().getPk() <= 3) {
                        lowNum = j.getState().getPk();
                    }
                }
                if (finalState > -1 && lowNum > finalState) {
                    s = State.getState(finalState);
                    ret = true;
                } else {
                    s = this.getState();
                }
            }
        } else {
            s = this.getState();
        }
        if (!s.equals(this.getState())) {
            ret = true;
            this.setState(s);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("ret=" + ret);
        }
        return ret;
    }

    public static Collection<JobPool> getJobPools() throws SQLException {
        Collection<JobPool> c;
        try {
            c = broker.getObjects(JobPool.class, false, "core.jobpool.all", new Object[] {});
        } catch (SQLException e) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e.getMessage());
            }
            throw e;
        }
        return c;
    }

    public JobPool fillObject(ResultSet r) throws SQLException {
        return new JobPool(r.getInt("JOB_POOL_ID"), r.getString("JOB_POOL_NAME"), r.getInt("PROJECT_ID"), r.getInt("USER_ID"), r.getTimestamp("START_TIME"), r.getTimestamp("FINISH_TIME"), r.getInt("STATE_ID"), r.getString("OPTIONS"), r.getInt("FUNCTION_ID"));
    }

    private static String generateName() {
        return "C" + new Date().getTime();
    }

    public StringBuilder buildShortXMLRepresentation() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(3, 1);
        map.put("id", Integer.toString(this.getPk()));
        map.put("name", this.getName());
        map.put("href", PropertiesFile.getConstantValue("rest.servlet.url") + "jobpool/" + this.getPk() + '/');
        return elementBuilder("jobpool", map);
    }

    public StringBuilder buildXMLRepresention() {
        StringBuilder ret = new StringBuilder();
        ret.append("<jobpool>\n");
        ret.append(elementBuilder("id", Integer.toString(this.getPk())));
        ret.append(elementBuilder("name", this.getName()));
        ret.append(elementBuilder("start-time", this.getStartTime().toString()));
        ret.append(elementBuilder("end-time", this.getFinishTime().toString()));
        ret.append(elementBuilder("options", this.getOptions()));
        ret.append(this.getUser().buildShortXMLRepresentation());
        ret.append(this.getProject().buildShortXMLRepresentation());
        ret.append(this.getFunction().buildShortXMLRepresentation());
        ret.append(this.getState().buildShortXMLRepresentation());
        try {
            for (final Job j : this.getJobs()) {
                ret.append(j.buildShortXMLRepresentation());
            }
        } catch (SQLException e1) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + e1.getMessage());
            }
        }
        ret.append("</jobpool>\n");
        return ret;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("JobPool");
        buf.append("{jobPoolId=").append(jobPoolId);
        buf.append(",jobPoolName=").append(jobPoolName);
        buf.append(",projectId=").append(projectId);
        buf.append(",userId=").append(userId);
        buf.append(",startTime=").append(startTime);
        buf.append(",finishTime=").append(finishTime);
        buf.append(",state=").append(state);
        buf.append(",options=").append(options);
        buf.append(",functionId=").append(functionId);
        buf.append(",user=").append(user);
        buf.append(",project=").append(project);
        buf.append(",function=").append(function);
        buf.append(",state=").append(state);
        buf.append(",jobs=").append(jobs);
        buf.append('}');
        return buf.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobPool)) return false;
        final JobPool jobPool = (JobPool) o;
        if (functionId != jobPool.functionId) return false;
        if (jobPoolId != jobPool.jobPoolId) return false;
        if (projectId != jobPool.projectId) return false;
        if (userId != jobPool.userId) return false;
        if (finishTime != null ? !finishTime.equals(jobPool.finishTime) : jobPool.finishTime != null) return false;
        if (jobPoolName != null ? !jobPoolName.equals(jobPool.jobPoolName) : jobPool.jobPoolName != null) return false;
        if (options != null ? !options.equals(jobPool.options) : jobPool.options != null) return false;
        if (startTime != null ? !startTime.equals(jobPool.startTime) : jobPool.startTime != null) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = jobPoolId;
        result = 29 * result + (jobPoolName != null ? jobPoolName.hashCode() : 0);
        result = 29 * result + projectId;
        result = 29 * result + userId;
        result = 29 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 29 * result + (finishTime != null ? finishTime.hashCode() : 0);
        result = 29 * result + state.hashCode();
        result = 29 * result + (options != null ? options.hashCode() : 0);
        result = 29 * result + functionId;
        return result;
    }

    public Date getSortOnDate() {
        if (this.getStartTime() == null) {
            this.setStartTime(new Date());
        }
        return this.getStartTime();
    }
}
