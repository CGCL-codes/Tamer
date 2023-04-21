package br.edu.ufcg.ourgridportal.client.ui;

import br.edu.ufcg.ourgridportal.client.beans.JobBean;
import com.gwtext.client.widgets.tree.TreeNode;

public class JobNode extends TreeNode {

    private JobBean jobBean;

    public JobNode(JobBean jobBean) {
        super();
        this.setJobBean(jobBean);
        init();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((jobBean == null) ? 0 : jobBean.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        JobNode other = (JobNode) obj;
        if (jobBean == null) {
            if (other.jobBean != null) return false;
        } else if (!jobBean.equals(other.jobBean)) return false;
        return true;
    }

    public void init() {
        this.setText("Job " + jobBean.getIdJob() + " : " + jobBean.getLabelJob() + " [" + jobBean.getState() + "]");
        String state = jobBean.getState();
        updateIcon(state);
    }

    public void updateIcon(String state) {
        if (state.equals(Status.UNSTARTED)) {
            this.setIcon("images/ready.gif");
        } else if (state.equals(Status.RUNNING)) {
            this.setIcon("images/running.gif");
        } else if (state.equals(Status.FINISHED)) {
            this.setIcon("images/finished.gif");
        } else if (state.equals(Status.FAILED)) {
            this.setIcon("images/error.gif");
        } else if (state.equals(Status.CANCELLED)) {
            this.setIcon("images/cancelled.gif");
        }
    }

    public void setJobBean(JobBean jobBean) {
        this.jobBean = jobBean;
    }

    public JobBean getJobBean() {
        return jobBean;
    }
}
