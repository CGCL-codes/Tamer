package org.efs.openreports.interceptors;

import org.efs.openreports.objects.ReportUser;

public class SchedulerRoleInterceptor extends SecurityInterceptor {

    private static final long serialVersionUID = 4584702403973653860L;

    protected boolean isAuthorized(ReportUser user) {
        return user.isScheduler();
    }
}
