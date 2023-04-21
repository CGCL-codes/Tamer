package binky.reportrunner.ui.actions.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import binky.reportrunner.scheduler.Scheduler;
import binky.reportrunner.ui.actions.base.StandardRunnerAction;

public class SchedulerStateChange extends StandardRunnerAction {

    private static final long serialVersionUID = 778488274157345048L;

    private Scheduler scheduler;

    private int schedulerState;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String execute() throws Exception {
        switch(schedulerState) {
            case 1:
                scheduler.startScheduler();
                break;
            case 0:
                scheduler.stopScheduler();
                break;
        }
        return SUCCESS;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public int getSchedulerState() {
        return schedulerState;
    }

    public void setSchedulerState(int schedulerState) {
        this.schedulerState = schedulerState;
    }
}
