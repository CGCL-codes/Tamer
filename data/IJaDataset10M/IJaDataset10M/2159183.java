package xml.persistence.xstreamImpl;

import java.util.ArrayList;
import java.util.List;
import com.accljob.model.JobAction;

public class JobActionList {

    private List<JobAction> jobActions = new ArrayList<JobAction>();

    public List<JobAction> getJobActions() {
        return jobActions;
    }

    public void setJobActions(List<JobAction> jobActions) {
        this.jobActions = jobActions;
    }
}
