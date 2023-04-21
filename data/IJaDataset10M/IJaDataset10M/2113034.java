package hu.sztaki.lpds.pgportal.services.quota;

import hu.sztaki.lpds.pgportal.services.pgrade.SZGJob;
import hu.sztaki.lpds.pgportal.services.pgrade.SZGWorkflowListGetFacade;
import hu.sztaki.lpds.pgportal.services.utils.WorkflowUtils;
import java.util.Hashtable;
import hu.sztaki.lpds.pgportal.services.utils.PropertyLoader;
import hu.sztaki.lpds.pgportal.services.utils.directoryUtil;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class userBean {

    private String userName = null;

    private long quotaSize = 0;

    private Hashtable<String, workflowBean> workflows = new Hashtable<String, workflowBean>();

    public userBean() {
    }

    protected userBean(String pUserName) {
        this.userName = pUserName;
        setStorageData();
    }

    protected void setStorageData() {
        String[] workflowNames = WorkflowUtils.getWorkflowNames(userName);
        workflows.clear();
        if (null != workflowNames) {
            for (String workflowName : workflowNames) {
                addWorkflow(workflowName);
            }
        }
    }

    public List<workflowBean> getAllWorkflow() {
        List<workflowBean> list = new LinkedList<workflowBean>(workflows.values());
        Collections.sort(list);
        return list;
    }

    public workflowBean getWorkflow(String pName) {
        return workflows.get(pName);
    }

    public void setQuotaSize(long pSize) {
        quotaSize = pSize;
    }

    public long getQuotaeSize() {
        return quotaSize;
    }

    public long getStorageSize() {
        long retValue = 0L;
        for (Iterator<workflowBean> i = workflows.values().iterator(); i.hasNext(); ) {
            retValue += i.next().getAllWorkflowSize();
        }
        return retValue;
    }

    public String getUserName() {
        return userName;
    }

    public void deleteWorkflow(String pWrkName) {
        if (workflows.get(pWrkName) != null) {
            workflows.remove(pWrkName);
        }
    }

    public synchronized void realocateWorkflowSize(String pWrkName) {
        addWorkflow(pWrkName);
    }

    public void addWorkflow(String pWrkName) {
        directoryUtil dru = new directoryUtil();
        long allWorkflowSize = dru.getDirSize(PropertyLoader.getPrefixDir() + "users/" + userName + "/" + pWrkName + "_files");
        workflowBean wfBean = new workflowBean(this, pWrkName, allWorkflowSize, 0L);
        wfBean.setActive(SZGWorkflowListGetFacade.getWorkflow(wfBean.getUserName(), wfBean.getName()) != null);
        long traceSize = 0L;
        String[] jobNames = WorkflowUtils.getJobNames(userName, pWrkName);
        for (String jobName : jobNames) {
            SZGJob job = SZGWorkflowListGetFacade.getJob(userName, pWrkName, jobName);
            if (job != null) {
                String tracePath = job.getTraceFilePath();
                File traceFile = new File(tracePath);
                if (traceFile.exists()) {
                    traceBean tBean = new traceBean(wfBean, tracePath, jobName, traceFile.length());
                    traceSize += tBean.getSize();
                    wfBean.addTraceBean(tBean);
                }
            }
        }
        wfBean.setTraceSize(traceSize);
        workflows.put(pWrkName, wfBean);
    }

    public boolean isStorageHasFreeSpace() {
        return isStorageHasFreeSpaceFor(0L);
    }

    public boolean isStorageHasFreeSpaceFor(long pValue) {
        return (((double) (getStorageSize() + pValue) / (double) quotaSize) <= quotaStaticService.writeLimit);
    }

    public void setWorkflows(Hashtable<String, workflowBean> workflows) {
        this.workflows = workflows;
    }

    public String getAllUsagePercentage() {
        double perc = (double) getStorageSize() / (double) getQuotaeSize();
        perc *= 100.0;
        return String.format("%.2f", perc);
    }

    public String getUsagePercentage(String wfName) {
        double perc = (double) getWorkflow(wfName).getAllWorkflowSize() / (double) getQuotaeSize();
        perc *= 100.0;
        return String.format("%.2f", perc);
    }

    public boolean isQuotaOver() {
        return !isStorageHasFreeSpace();
    }
}
