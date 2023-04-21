package Protocol.app;

import java.io.Serializable;
import java.util.ArrayList;
import Protocol.threadStatus.ThreadStatus;
import Protocol.user.DetailPersonInfo;

public class VisitFriendSpace_Q implements Serializable {

    DetailPersonInfo detaiInfo;

    ArrayList<ThreadStatus> threadLogList;

    ArrayList<PlanJournal> planList;

    public DetailPersonInfo getDetaiInfo() {
        return detaiInfo;
    }

    public void setDetaiInfo(DetailPersonInfo detaiInfo) {
        this.detaiInfo = detaiInfo;
    }

    public ArrayList<ThreadStatus> getThreadLogList() {
        return threadLogList;
    }

    public void setThreadLogList(ArrayList<ThreadStatus> threadLogList) {
        this.threadLogList = threadLogList;
    }

    public ArrayList<PlanJournal> getPlanList() {
        return planList;
    }

    public void setPlanList(ArrayList<PlanJournal> planList) {
        this.planList = planList;
    }
}
