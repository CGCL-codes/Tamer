package hu.sztaki.lpds.pgportal.services.quota;

import hu.sztaki.lpds.pgportal.services.pgrade.SZGWorkflow;
import hu.sztaki.lpds.pgportal.services.pgrade.SZGWorkflowListGetFacade;
import hu.sztaki.lpds.pgportal.services.utils.MiscUtils;
import java.io.File;
import java.util.Hashtable;

public class workflowBean implements Comparable<workflowBean> {

    private String wfName = "";

    private long size = 0L;

    private long outputSize = 0L;

    private long traceSize = 0L;

    private String status = "";

    private Hashtable<String, traceBean> traceFiles = new Hashtable<String, traceBean>();

    private userBean parent = null;

    private boolean active = false;

    /** Creates a new instance of workflowBean */
    protected workflowBean(userBean parent, String pName, long pSize, long outputSize) {
        this.parent = parent;
        wfName = pName;
        size = pSize;
        this.outputSize = outputSize;
    }

    public String getName() {
        return wfName;
    }

    public long getOutputSize() {
        return this.outputSize;
    }

    public String getStatus() {
        return status;
    }

    public long getSize() {
        return this.size;
    }

    public void setName(String name) {
        this.wfName = name;
    }

    public void setOutputSize(long size) {
        this.outputSize = size;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isRunning() {
        boolean retValue = false;
        SZGWorkflow wf = SZGWorkflowListGetFacade.getWorkflow(parent.getUserName(), this.wfName);
        if (wf != null) {
            int status = wf.getStatus();
            retValue = (status == SZGWorkflow.STATUS_SUBMITTED || status == SZGWorkflow.STATUS_RESCUE || status == SZGWorkflow.STATUS_RUNNING);
        }
        return retValue;
    }

    public String getUserName() {
        return parent.getUserName();
    }

    public userBean getParent() {
        return this.parent;
    }

    public void setParent(userBean parent) {
        this.parent = parent;
    }

    public boolean isSuspending() {
        SZGWorkflow wf = SZGWorkflowListGetFacade.getWorkflow(parent.getUserName(), this.wfName);
        if (wf != null) return wf.isSuspending(); else return false;
    }

    public boolean isExistsabortfile() {
        SZGWorkflow wf = SZGWorkflowListGetFacade.getWorkflow(parent.getUserName(), this.wfName);
        if (wf != null) return wf.existsAbortFile(); else return false;
    }

    public int compareTo(workflowBean other) {
        return wfName.compareTo(other.getName());
    }

    public void addTraceFile(String fileName, String pJobId) {
        this.addTraceFile(fileName, pJobId, 0L);
    }

    private void addTraceFile(String fileName, String pJobId, long pSize) {
        if (traceFiles.containsKey(pJobId)) {
            deleteTraceFile(pJobId);
        }
        traceBean tBean = new traceBean(this, fileName, pJobId, pSize);
        traceFiles.put(pJobId, tBean);
        this.traceSize += pSize;
    }

    protected void addTraceBean(traceBean tBean) {
        this.addTraceFile(tBean.getFileName(), tBean.getJobName(), tBean.getSize());
    }

    public void deleteTraceFile(String pKey) {
        if (traceFiles.get(pKey) != null) {
            traceBean tmpDelete = (traceBean) traceFiles.get(pKey);
            if (tmpDelete != null) {
                File f = new File(tmpDelete.getFileName());
                if (f.delete()) {
                    traceFiles.remove(pKey);
                    this.traceSize -= tmpDelete.getSize();
                }
            }
        }
    }

    public long getTraceSize() {
        return traceSize;
    }

    public void setTraceSize(long traceSize) {
        this.traceSize = traceSize;
    }

    public long getAllWorkflowSize() {
        return (this.size + this.outputSize + this.traceSize);
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSizeStr() {
        return MiscUtils.getSpaceSizeFromByte(this.getSize());
    }

    public String getTraceSizeStr() {
        return MiscUtils.getSpaceSizeFromByte(this.getTraceSize());
    }

    public String getAllWorkflowSizeStr() {
        return MiscUtils.getSpaceSizeFromByte(this.getAllWorkflowSize());
    }

    public String toString() {
        return "workflowBean --> name: " + this.wfName + " ; user:" + parent.getUserName() + "; size:" + size + "; outputsize:" + outputSize + "; tracesize:" + traceSize + "; allsize:" + getAllWorkflowSize() + "; isactive:" + isActive() + "; isrunning:" + isRunning();
    }
}
