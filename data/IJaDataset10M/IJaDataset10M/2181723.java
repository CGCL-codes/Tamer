package org.go.core;

import org.go.Work;
import org.go.WorkKey;

public class WorkWrapper {

    public Work work;

    public WorkKey workKey;

    public WorkWrapper(Work jobDetail) {
        this.work = jobDetail;
        workKey = jobDetail.getKey();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WorkWrapper) {
            WorkWrapper jw = (WorkWrapper) obj;
            if (jw.workKey.equals(this.workKey)) {
                return true;
            }
        }
        return false;
    }

    public Work getWorkerDesc() {
        return work;
    }

    public WorkKey getWorkKey() {
        return workKey;
    }

    @Override
    public int hashCode() {
        return workKey.hashCode();
    }

    public void setWorkerDesc(Work work) {
        this.work = work;
    }

    public void setWorkKey(WorkKey workKey) {
        this.workKey = workKey;
    }
}
