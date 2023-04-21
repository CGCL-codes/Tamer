package edu.purdue.rcac.cesm.facade;

import static edu.purdue.rcac.cesm.Constants.APP_NAME;
import static edu.purdue.rcac.cesm.Constants.PBSID;
import static edu.purdue.rcac.cesm.Constants.wrapParam;
import static edu.purdue.rcac.cesm.util.CESMDateUtil.Date2String;
import static edu.purdue.rcac.cesm.util.CESMDateUtil.String2Date;
import static edu.purdue.rcac.cesm.util.CESMDateUtil.addDuration;
import static edu.purdue.rcac.cesm.util.CESMDateUtil.replaceIndicator;
import static edu.purdue.rcac.cesm.util.CESMDateUtil.replaceNumber;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.Interceptors;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.Depends;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import edu.purdue.rcac.cesm.Constants.ParamName;
import edu.purdue.rcac.cesm.define.ComponentSet;
import edu.purdue.rcac.cesm.define.Machine;
import edu.purdue.rcac.cesm.define.Resolution;
import edu.purdue.rcac.cesm.facade.bean.AllCaseInfoBean;
import edu.purdue.rcac.cesm.facade.bean.AllCaseNameBean;
import edu.purdue.rcac.cesm.facade.bean.AllJobInfoBean;
import edu.purdue.rcac.cesm.facade.bean.AllocationsBean;
import edu.purdue.rcac.cesm.facade.bean.CaseConfigInfoBean;
import edu.purdue.rcac.cesm.facade.bean.CaseRuntimeInfoBean;
import edu.purdue.rcac.cesm.facade.bean.ConfigVarBean;
import edu.purdue.rcac.cesm.facade.bean.ConfigVarListBean;
import edu.purdue.rcac.cesm.facade.bean.CreateCaseBean;
import edu.purdue.rcac.cesm.facade.bean.DownloadBean;
import edu.purdue.rcac.cesm.facade.bean.FileBean;
import edu.purdue.rcac.cesm.facade.bean.KeyValueEntry;
import edu.purdue.rcac.cesm.interceptor.AuthenticateInterceptor;
import edu.purdue.rcac.cesm.persistence.dao.CaseManager;
import edu.purdue.rcac.cesm.persistence.model.CESMCaseConfig;
import edu.purdue.rcac.cesm.persistence.model.CESMCaseConfig.CaseStage;
import edu.purdue.rcac.cesm.persistence.model.CESMCaseRuntime;
import edu.purdue.rcac.cesm.persistence.model.CESMCaseRuntime.JobStatus;
import edu.purdue.rcac.cesm.util.StringUtil;
import edu.purdue.rcac.grid.submit.SubmissionException;
import edu.purdue.rcac.service.dispatch.AbstractResponse.Outcome;
import edu.purdue.rcac.service.dispatch.ServiceDispatcherManagement;
import edu.purdue.rcac.service.dispatch.ServiceDispatcherManagement.AuthDatabase;
import edu.purdue.rcac.service.dispatch.ServiceResponse;
import edu.purdue.rcac.service.dispatch.SubmitterRuntimeException;
import edu.purdue.rcac.service.dispatch.ValidationException;

/**
 * This class is an EJB3 stateless session bean implementation of interfaces
 * CESMServiceFacadeLocal and CESMServiceFacadeRemote
 * 
 * @author Han Zhang
 */
@Stateless
@Local(ServiceFacadeLocal.class)
@LocalBinding(jndiBinding = ServiceFacade.LOCAL_JNDI_BINDING)
@Remote(ServiceFacadeRemote.class)
@RemoteBinding(jndiBinding = ServiceFacade.REMOTE_JNDI_BINDING)
@Interceptors({ AuthenticateInterceptor.class })
public class ServiceFacade implements ServiceFacadeLocal, ServiceFacadeRemote {

    protected static final Logger logger = Logger.getLogger(ServiceFacade.class.getName());

    private ServiceDispatcherManagement serviceDispatcher;

    private CaseManager caseManager;

    /**
	 * Constructor: initializes serviceDispatcher, caseManager and tokenManager
	 */
    public ServiceFacade() {
        super();
        MBeanServer server = MBeanServerLocator.locateJBoss();
        try {
            logger.info("Look up object name: " + ServiceDispatcherManagement.OBJECT_NAME);
            serviceDispatcher = (ServiceDispatcherManagement) MBeanProxyExt.create(ServiceDispatcherManagement.class, ServiceDispatcherManagement.OBJECT_NAME, server);
            serviceDispatcher.registerApp(APP_NAME, DEFAULT_SUBMITTER_LANG, DEFAULT_SUBMIT_TYPE);
        } catch (MalformedObjectNameException e) {
            logger.error(e.getMessage(), e);
        }
        try {
            logger.info("Look up object name: " + CaseManager.OBJECT_NAME);
            caseManager = (CaseManager) MBeanProxyExt.create(CaseManager.class, CaseManager.OBJECT_NAME, server);
        } catch (MalformedObjectNameException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
	 * @return action string formated in underscore lower case
	 */
    @ExcludeClassInterceptors
    private String getMethodName() {
        return StringUtil.underscoreLowerCase(Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @ExcludeClassInterceptors
    private Map<String, String> getDefaultArgsMap(String action, String username, String token) {
        Map<String, String> tmp = new HashMap<String, String>();
        tmp.put(ParamName.ACTION.name(), action);
        tmp.put(ParamName.USERNAME.name(), username);
        tmp.put(ParamName.TOKEN.name(), token);
        tmp.put(wrapParam("username"), username);
        return tmp;
    }

    @ExcludeClassInterceptors
    private Map<String, String> getDefaultArgsMap(String action, String username, String casename, String token) {
        Map<String, String> tmp = getDefaultArgsMap(action, username, token);
        tmp.put(ParamName.CASENAME.name(), casename);
        tmp.put(wrapParam("casename"), casename);
        return tmp;
    }

    private CESMCaseConfig getCaseConfig(String userName, String caseName, boolean update) throws Exception {
        if (caseManager.exists(userName, caseName)) {
            CESMCaseConfig caseConfig = caseManager.findByOwnerAndName(userName, caseName);
            if (update && caseConfig.getStage() == CaseStage.RUNNING) updateCaseRuntime(caseConfig);
            return caseConfig;
        } else throw new Exception("no case named '" + caseName + "' owned by '" + userName + "' has been found");
    }

    /**
	 * Updates usedWallTime, usedCPUTime and jobStatus fields of the given case
	 * runtime. The given case is supposed to be in RUNNING stage. Update is
	 * done by submit a globus job to invoke an "update_case" action on remote.
	 * 
	 * @param caseConfig
	 *            CESMCaseConfig object that will be updated
	 * @throws Exception
	 */
    protected void updateCaseRuntime(CESMCaseConfig caseConfig) throws Exception {
        HashMap<String, String> updateArgsMap = new HashMap<String, String>();
        updateArgsMap.put(ParamName.ACTION.name(), "update_case");
        updateArgsMap.put(ParamName.USERNAME.name(), caseConfig.getOwner());
        updateArgsMap.put(ParamName.CASENAME.name(), caseConfig.getName());
        try {
            for (CESMCaseRuntime currRuntime : caseConfig.getRuntimes()) {
                updateArgsMap.put(wrapParam(PBSID), currRuntime.getPbsid());
                if (currRuntime.getJobStatus() == JobStatus.DONE || currRuntime.getJobStatus() == JobStatus.CANCELLED) continue;
                ServiceResponse response = (ServiceResponse) serviceDispatcher.submit(APP_NAME, updateArgsMap);
                logger.debug("------ case job info ------");
                logger.info("RESULT: " + response.getResult());
                logger.debug("---------------------------");
                String[] results = response.getResult().split(",");
                if (results.length != 4) throw new Exception("returned value does not contain 3 entries");
                String usedCPUTime = results[0].trim();
                String usedWallTime = results[1].trim();
                char statusChar = results[2].trim().charAt(0);
                String queue = results[3].trim();
                if (CESMCaseRuntime.JOB_STATUS_MAP.containsKey(statusChar)) {
                    currRuntime.setJobStatus(CESMCaseRuntime.JOB_STATUS_MAP.get(statusChar));
                } else {
                    currRuntime.setJobStatus(JobStatus.SUBMITTED);
                }
                currRuntime.setUsedCPUTime(usedCPUTime);
                currRuntime.setUsedWallTime(usedWallTime);
                currRuntime.setQueue(queue);
            }
            int i = 0;
            for (CESMCaseRuntime caseRuntime : caseConfig.getRuntimes()) {
                if (caseRuntime.getJobStatus() == JobStatus.DONE || caseRuntime.getJobStatus() == JobStatus.CANCELLED) i++;
            }
            if (i == caseConfig.getRuntimes().size()) caseConfig.setStage(CaseStage.DONE);
            caseManager.merge(caseConfig);
        } catch (Exception se) {
            throw new Exception("update case fail", se);
        }
    }

    @ExcludeClassInterceptors
    private ServiceResponse invoke(Map<String, String> argsMap) throws ValidationException, SubmitterRuntimeException, IllegalArgumentException, InstantiationException, SubmissionException {
        logger.debug("Application Name: " + APP_NAME);
        return serviceDispatcher.submit(APP_NAME, argsMap);
    }

    public CaseConfigInfoBean getCaseConfigInfo(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        return new CaseConfigInfoBean(Outcome.SUCCESS.name(), caseConfig);
    }

    public CaseRuntimeInfoBean getCaseRuntimeInfo(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        logger.info("# of runtimes: " + caseConfig.getRuntimes().size());
        for (CESMCaseRuntime rt : caseConfig.getRuntimes()) {
            logger.info("runtime: " + rt + "\n\n");
        }
        CaseRuntimeInfoBean response = new CaseRuntimeInfoBean(caseConfig.getRuntimes());
        response.setOutcome(Outcome.SUCCESS.name());
        return response;
    }

    public String getJobStatus(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, true);
        List<CESMCaseRuntime> runtimes = caseConfig.getRuntimes();
        if (runtimes.size() < 1) throw new Exception("Database fail: There's no CESMCaseRuntime entry for the case whose 'owner=" + caseConfig.getOwner() + "', name=" + caseConfig.getName() + "'");
        CESMCaseRuntime currRuntime = null;
        CESMCaseRuntime latestRuntime = runtimes.get(0);
        for (CESMCaseRuntime caseRuntime : runtimes) {
            if (!caseRuntime.isDone()) {
                currRuntime = caseRuntime;
                break;
            }
            if (caseRuntime.getStartTime().after(latestRuntime.getStartTime())) {
                latestRuntime = caseRuntime;
            }
        }
        if (currRuntime != null) return currRuntime.getJobStatus().toString(); else return latestRuntime.getJobStatus().toString();
    }

    public String getUsedWallTime(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, true);
        List<CESMCaseRuntime> runtimes = caseConfig.getRuntimes();
        if (runtimes.size() < 1) throw new Exception("Database fail: There's no CESMCaseRuntime entry for the case whose 'owner=" + caseConfig.getOwner() + "', name=" + caseConfig.getName() + "'");
        CESMCaseRuntime caseRuntime = runtimes.get(runtimes.size() - 1);
        return caseRuntime.getUsedWallTime();
    }

    public String getUsedCPUTime(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, true);
        List<CESMCaseRuntime> runtimes = caseConfig.getRuntimes();
        if (runtimes.size() < 1) throw new Exception(String.format("Database fail: There's no CeSMCaseRuntime entry for the case whose 'owner=%s', name='%s'", caseConfig.getOwner(), caseConfig.getName()));
        CESMCaseRuntime caseRuntime = runtimes.get(runtimes.size() - 1);
        return caseRuntime.getUsedCPUTime();
    }

    public ConfigVarBean queryConfigVar(String varName, String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        String action = getMethodName();
        Map<String, String> argsMap = getDefaultArgsMap(action, userName, caseName, token);
        argsMap.put(wrapParam("varname"), varName);
        ServiceResponse result = this.invoke(argsMap);
        String varValue = "";
        if (result.getResult().contains(varName)) {
            varValue = result.getResult().trim().split("=")[1];
        }
        return new ConfigVarBean(result.getOutcome().name(), varName, varValue);
    }

    public String setConfigVar(String varName, String varValue, String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        String action = getMethodName();
        Map<String, String> argsMap = getDefaultArgsMap(action, userName, caseName, token);
        argsMap.put(wrapParam("varname"), varName);
        argsMap.put(wrapParam("varvalue"), varValue);
        ServiceResponse result = this.invoke(argsMap);
        if (result.getOutcome() == Outcome.SUCCESS) {
            if (varName.equals("RUN_STARTDATE")) {
                try {
                    String2Date(varValue);
                } catch (ParseException pe) {
                    throw new Exception("date format should be yyyy-MM-dd, but input was'" + varValue + "'");
                }
                caseConfig.setStartDate(varValue);
                caseManager.merge(caseConfig);
            }
            if (varName.equals("RESUBMIT")) {
                caseConfig.setResubmit(Integer.valueOf(varValue));
                caseManager.merge(caseConfig);
            }
            if (varName.equals("STOP_N")) {
                String duration = replaceNumber(caseConfig.getDuration(), Integer.valueOf(varValue));
                Date startDate = String2Date(caseConfig.getStartDate());
                Date endDate = addDuration(startDate, duration);
                caseConfig.setEndDate(Date2String(endDate));
                caseConfig.setDuration(duration);
                caseManager.merge(caseConfig);
            }
            if (varName.equals("STOP_OPTION")) {
                String duration = replaceIndicator(caseConfig.getDuration(), varValue);
                Date startDate = String2Date(caseConfig.getStartDate());
                Date endDate = addDuration(startDate, duration);
                caseConfig.setEndDate(Date2String(endDate));
                caseConfig.setDuration(duration);
                caseManager.merge(caseConfig);
            }
        }
        return result.getOutcome().name();
    }

    public String getPbsOut(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        Map<String, String> argsMap = getDefaultArgsMap("get_file_content", userName, caseName, token);
        argsMap.put(ParamName.FILE.name(), userName + File.separator + caseName + File.separator + "pbs.out");
        return serviceDispatcher.getFileContent(APP_NAME, argsMap);
    }

    public String getPbsErr(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        Map<String, String> argsMap = getDefaultArgsMap("get_file_content", userName, caseName, token);
        argsMap.put(ParamName.FILE.name(), userName + File.separator + caseName + File.separator + "pbs.err");
        return serviceDispatcher.getFileContent(APP_NAME, argsMap);
    }

    @ExcludeClassInterceptors
    public String authenticate(String userName, String password, AuthDatabase db) throws Exception {
        String token = serviceDispatcher.generateToken(userName, password, db);
        logger.debug("NEW TOKEN: " + token);
        if (token.equals("Failed")) {
            throw new Exception("request for new token failed, probable reason: user '" + userName + "' doesn't exist");
        }
        token = (token == null) ? "" : token;
        return token;
    }

    @ExcludeClassInterceptors
    public String createUserHome(String userName, String token) throws Exception {
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, token);
        return this.invoke(argsMap).getOutcome().name();
    }

    @ExcludeClassInterceptors
    public boolean isTokenValid(String userName, String token) {
        try {
            return serviceDispatcher.verifyToken(userName, token);
        } catch (Exception e) {
            return false;
        }
    }

    public CreateCaseBean createCase(Resolution resolution, ComponentSet componentSet, String allocation, String machine, String caseName, String userName, String password, AuthDatabase db) throws Exception {
        long start = System.currentTimeMillis();
        logger.debug("entered createCase");
        if (caseManager.exists(userName, caseName)) throw new Exception("case already exists");
        long curr = System.currentTimeMillis();
        logger.debug(String.format("caseManager.exists cost %d ms = %.3f s", curr - start, (curr - start) / 1000f));
        start = curr;
        String token = serviceDispatcher.generateToken(userName, password, db);
        logger.debug("NEW TOKEN: " + token);
        if (token.equals("Failed")) {
            throw new Exception("request for new token failed, probable reason: user '" + userName + "' doesn't exist");
        }
        curr = System.currentTimeMillis();
        logger.debug(String.format("generate token cost %d ms = %.3f s", curr - start, (curr - start) / 1000f));
        start = curr;
        String action = getMethodName();
        Map<String, String> argsMap = getDefaultArgsMap(action, userName, caseName, "");
        argsMap.remove(ParamName.TOKEN.name());
        argsMap.put(wrapParam("resolution"), resolution.toString());
        argsMap.put(wrapParam("compset"), componentSet.toString());
        argsMap.put(wrapParam("allocation"), allocation);
        argsMap.put(wrapParam("machine"), machine);
        curr = System.currentTimeMillis();
        logger.debug(String.format("generate argsMap cost %d ms = %.3f s", curr - start, (curr - start) / 1000f));
        start = curr;
        ServiceResponse result = invoke(argsMap);
        curr = System.currentTimeMillis();
        logger.debug(String.format("generate service response cost %d ms = %.3f s", curr - start, (curr - start) / 1000f));
        start = curr;
        if (result.getOutcome() == Outcome.SUCCESS) {
            CESMCaseConfig caseConfig = new CESMCaseConfig(userName, caseName, resolution, componentSet);
            caseConfig.setStage(CaseStage.CREATED);
            caseConfig.setMachine(Machine.fromString(machine));
            caseManager.save(caseConfig);
        }
        curr = System.currentTimeMillis();
        logger.debug(String.format("update database cost %d ms = %.3f s", curr - start, (curr - start) / 1000f));
        start = curr;
        return new CreateCaseBean(result.getOutcome().name(), token, result.getLog(), result.getError());
    }

    public String configureCase(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        if (caseConfig.getStage() != CaseStage.CREATED) throw new Exception("stage CREATED expected, but met " + caseConfig.getStage().toString() + ", can not configure");
        String action = getMethodName();
        Map<String, String> argsMap = getDefaultArgsMap(action, userName, caseName, token);
        ServiceResponse result = this.invoke(argsMap);
        if (result.getOutcome() == Outcome.SUCCESS) {
            logger.debug("configure_case: changing stage from " + caseConfig.getStage() + " to CONFIGURED");
            caseConfig.setStage(CaseStage.CONFIGURED);
            caseManager.merge(caseConfig);
        }
        return result.getOutcome().name();
    }

    public String resetConfig(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, true);
        if (caseConfig.getStage() != CaseStage.CONFIGURED && caseConfig.getStage() != CaseStage.CANCELLED && caseConfig.getStage() != CaseStage.DONE) throw new Exception("stage CONFIGURED, CANCELLED or DONE expected, but met " + caseConfig.getStage().toString() + ", can not reset config");
        String action = getMethodName();
        Map<String, String> argsMap = getDefaultArgsMap(action, userName, caseName, token);
        ServiceResponse result = this.invoke(argsMap);
        if (result.getOutcome() == Outcome.SUCCESS) {
            logger.debug("reset_config: changing stage from " + caseConfig.getStage() + " to CREATED");
            caseConfig.setStage(CaseStage.CREATED);
            caseConfig.setRuntimes(null);
            caseManager.merge(caseConfig);
        }
        return result.getOutcome().name();
    }

    public String runCase(String userName, String caseName, String email, String queueToRun, String wallTime, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, true);
        if (caseConfig.getStage() != CaseStage.CONFIGURED && caseConfig.getStage() != CaseStage.CANCELLED && caseConfig.getStage() != CaseStage.DONE) throw new Exception("stage CONFIGURED, CANCELLED or DONE  expected, but met " + caseConfig.getStage().toString() + ", can not run case");
        String action = getMethodName();
        Map<String, String> argsMap = getDefaultArgsMap(action, userName, caseName, token);
        argsMap.put(wrapParam("queue"), queueToRun);
        argsMap.put(wrapParam("walltime"), wallTime);
        argsMap.put(wrapParam("email"), email);
        ServiceResponse result = this.invoke(argsMap);
        try {
            if (result.getResult().contains("pbsid=")) {
                String pbsid = result.getResult().trim().split("=")[1];
                logger.debug("run_case: changing stage from " + caseConfig.getStage() + " to RUNNING");
                logger.info("run_case: storing pbsid: " + pbsid);
                logger.info("run_case: error:" + result.getError());
                caseConfig.setStage(CaseStage.RUNNING);
                List<CESMCaseRuntime> runtimes = caseConfig.getRuntimes();
                CESMCaseRuntime caseRuntime = new CESMCaseRuntime(pbsid, caseConfig);
                caseRuntime.setQueue(queueToRun);
                caseManager.save(caseRuntime);
            }
        } catch (ArrayIndexOutOfBoundsException aie) {
            logger.info("ERROR:" + result.getError());
            throw new Exception(result.getError());
        }
        return result.getOutcome().name();
    }

    public String cancelCase(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        if (caseConfig.getStage() != CaseStage.RUNNING) throw new Exception("stage RUNNING expected, but met " + caseConfig.getStage().toString() + ", can not cancel case");
        String action = getMethodName();
        Map<String, String> argsMap = getDefaultArgsMap(action, userName, caseName, token);
        String pbsids = "";
        for (CESMCaseRuntime rt : caseConfig.getRuntimes()) {
            if (rt.getJobStatus() != JobStatus.DONE && rt.getJobStatus() != JobStatus.CANCELLED) pbsids += rt.getPbsid() + "#";
        }
        argsMap.put(wrapParam(PBSID), pbsids);
        ServiceResponse result = this.invoke(argsMap);
        if (result.getOutcome() == Outcome.SUCCESS) {
            for (CESMCaseRuntime rt : caseConfig.getRuntimes()) {
                if (rt.getJobStatus() != JobStatus.DONE && rt.getJobStatus() != JobStatus.CANCELLED) rt.setJobStatus(JobStatus.CANCELLED);
            }
            logger.debug("cancel_case: changing stage from " + caseConfig.getStage() + " to CANCELLED");
            caseConfig.setStage(CaseStage.CANCELLED);
            caseManager.merge(caseConfig);
            return Outcome.SUCCESS.name();
        } else {
            return Outcome.FAIL.name();
        }
    }

    public String deleteCase(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        String action = getMethodName();
        Map<String, String> argsMap = getDefaultArgsMap(action, userName, caseName, token);
        String pbsids = "";
        for (CESMCaseRuntime rt : caseConfig.getRuntimes()) {
            if (rt.getJobStatus() != JobStatus.DONE && rt.getJobStatus() != JobStatus.CANCELLED) pbsids += rt.getPbsid() + " ";
        }
        argsMap.put(wrapParam(PBSID), pbsids);
        ServiceResponse response = invoke(argsMap);
        if (response.getOutcome() == Outcome.SUCCESS) {
            caseManager.remove(caseConfig);
        }
        return response.getOutcome().name();
    }

    public AllCaseNameBean listAllCaseName(String userName, String token) throws Exception {
        List<CESMCaseConfig> cases = caseManager.findByOwner(userName);
        List<String> casenames = new ArrayList<String>();
        for (CESMCaseConfig caseConfig : cases) {
            casenames.add(caseConfig.getName());
        }
        return new AllCaseNameBean(Outcome.SUCCESS.name(), casenames);
    }

    public ConfigVarListBean getConfigVarList(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        String action = getMethodName();
        Map<String, String> argsMap = getDefaultArgsMap(action, userName, caseName, token);
        List<KeyValueEntry> vars = new ArrayList<KeyValueEntry>();
        ServiceResponse result = this.invoke(argsMap);
        if (result.getOutcome() == Outcome.SUCCESS) {
            System.out.println("RESULT: " + result.getResult());
            for (String exp : result.getResult().split(",")) {
                String[] words = exp.split("=");
                String key = words[0];
                String value = words[1];
                vars.add(new KeyValueEntry(key, value));
            }
        }
        return new ConfigVarListBean(result.getOutcome().name(), vars);
    }

    public AllJobInfoBean listAllJobInfo(String userName, String queueName, String machine, String token) throws Exception {
        String action = getMethodName();
        Map<String, String> argsMap = getDefaultArgsMap(action, userName, token);
        List<String> casenames = listAllCaseName(userName, token).getCasenames();
        if (casenames == null || casenames.size() < 1) {
            return new AllJobInfoBean(Outcome.SUCCESS.name(), "there is no case associated with this user.");
        }
        ServiceResponse result = this.invoke(argsMap);
        StringBuffer buffer = new StringBuffer("");
        if (result.getOutcome() == Outcome.SUCCESS) {
            String[] lines = result.getResult().split("\n");
            for (int i = 0; i < 5; i++) buffer.append(lines[i] + "\n");
            for (int i = 5; i < lines.length; i++) {
                for (String casename : casenames) if (lines[i].contains(casename.length() > 10 ? casename.substring(0, 10) : casename) && lines[i].contains(queueName.length() > 8 ? queueName.substring(0, 8) : queueName)) {
                    buffer.append(lines[i] + "\n");
                    break;
                }
            }
        }
        return new AllJobInfoBean(result.getOutcome().name(), buffer.toString());
    }

    public AllocationsBean listAllocation(String userName, String token) throws Exception {
        return null;
    }

    public String getOutputLocation(String userName, String caseName, String token) throws Exception {
        return "FAKE URL";
    }

    public String setConfigVarList(String userName, String caseName, List<KeyValueEntry> varList, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, caseName, token);
        String separator = "^";
        String delimiter = "#";
        String vars = "";
        for (KeyValueEntry k : varList) {
            vars += k.toString(delimiter) + separator;
        }
        argsMap.put(wrapParam("varlist"), vars);
        argsMap.put(wrapParam("separator"), separator);
        argsMap.put(wrapParam("delimiter"), delimiter);
        ServiceResponse response = this.invoke(argsMap);
        return response.getOutcome().name();
    }

    public AllCaseInfoBean listAllCaseInfo(String userName, String token) throws Exception {
        AllCaseInfoBean response = new AllCaseInfoBean();
        for (CESMCaseConfig conf : caseManager.findByOwner(userName)) {
            if (conf.getStage() == CaseStage.RUNNING || conf.getStage() == CaseStage.DONE) {
                List<CESMCaseRuntime> rts = conf.getRuntimes();
                if (rts != null && rts.size() >= 1) {
                    response.addCaseInfo(rts.get(rts.size() - 1), conf);
                }
            } else {
                response.addCaseInfo(null, conf);
            }
        }
        return response;
    }

    public String upload(String userName, String toPath, DataHandler data, String token) throws Exception {
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, "", token);
        argsMap.put(ParamName.USERNAME.name(), userName);
        InputStream is = data.getInputStream();
        byte[] buffer = new byte[2048];
        logger.info("SIZE: " + is.read(buffer));
        ServiceResponse response = serviceDispatcher.upload(APP_NAME, argsMap, toPath, data);
        return response.getOutcome().name();
    }

    protected String normalize(String path) throws URISyntaxException {
        String normalizedPath = new URI(path).normalize().getPath();
        if (normalizedPath.startsWith("../") || normalizedPath.startsWith("/../")) {
            throw new URISyntaxException("path is incorrect", normalizedPath);
        }
        return normalizedPath;
    }

    public DownloadBean download(String userName, String path, String token) throws Exception {
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, token);
        DownloadBean bean = new DownloadBean();
        DataHandler data = serviceDispatcher.download(APP_NAME, argsMap, path);
        logger.info("XXXXXX");
        bean.setData(data);
        return bean;
    }

    public List<FileBean> list(String userName, String path, String baseDir, String token) throws Exception {
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, token);
        argsMap.put(wrapParam("path"), baseDir + normalize(path));
        String resultStr = this.invoke(argsMap).getResult();
        List<FileBean> resultList = new ArrayList<FileBean>();
        String date, time, filename;
        int size;
        boolean isDirectory;
        for (String line : resultStr.split("\n")) {
            if (!line.trim().equals(",,,")) {
                String[] items = line.split(",");
                size = Integer.valueOf(items[0]);
                date = items[1];
                time = items[2];
                filename = items[3];
                if (filename.endsWith("/")) {
                    filename = filename.substring(0, filename.length() - 1);
                    isDirectory = true;
                } else {
                    isDirectory = false;
                }
                resultList.add(new FileBean(filename, size, date + " " + time, isDirectory));
            }
        }
        return resultList;
    }

    public String mkdir(String userName, String path, String baseDir, String token) throws Exception {
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, token);
        argsMap.put(wrapParam("path"), baseDir + normalize(path));
        return this.invoke(argsMap).getOutcome().name();
    }

    public String remove(String userName, String path, String baseDir, String token) throws Exception {
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, token);
        argsMap.put(wrapParam("path"), baseDir + normalize(path));
        return this.invoke(argsMap).getOutcome().name();
    }

    public String move(String userName, String fromPath, String toPath, String baseDir, String token) throws Exception {
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, token);
        argsMap.put(wrapParam("frompath"), baseDir + normalize(fromPath));
        argsMap.put(wrapParam("topath"), baseDir + normalize(toPath));
        return this.invoke(argsMap).getOutcome().name();
    }

    public String rename(String userName, String fromPath, String toPath, String baseDir, String token) throws Exception {
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, token);
        argsMap.put(wrapParam("frompath"), baseDir + normalize(fromPath));
        argsMap.put(wrapParam("topath"), baseDir + normalize(toPath));
        return this.invoke(argsMap).getOutcome().name();
    }

    public String publish(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, caseName, token);
        return this.invoke(argsMap).getOutcome().name();
    }

    public String unpublish(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, caseName, token);
        return this.invoke(argsMap).getOutcome().name();
    }

    public String getPublishStatus(String userName, String caseName, String token) throws Exception {
        CESMCaseConfig caseConfig = getCaseConfig(userName, caseName, false);
        Map<String, String> argsMap = getDefaultArgsMap(getMethodName(), userName, caseName, token);
        return this.invoke(argsMap).getResult();
    }
}
