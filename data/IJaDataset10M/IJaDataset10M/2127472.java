package netgest.bo.xwc.xeo.workplaces.admin.connectors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import netgest.bo.system.boApplication;
import netgest.bo.system.boSession;
import netgest.bo.xwc.xeo.workplaces.admin.localization.MainAdminBeanMessages;

public class SessionsDataListConnector extends GenericDataListConnector {

    public SessionsDataListConnector() {
        super();
        this.createColumn("USER", MainAdminBeanMessages.USER_NAME.toString());
        this.createColumn("CLIENT_NAME", MainAdminBeanMessages.CLIENT_NAME.toString());
        this.createColumn("CREATED_TIME", MainAdminBeanMessages.CREATED_TIME.toString());
        this.createColumn("LAST_ACTIVITY", MainAdminBeanMessages.LAST_ACTIVITY.toString());
        this.createColumn("ACTIVE", MainAdminBeanMessages.ACTIVE.toString());
    }

    @Override
    public void refresh() {
        super.refresh();
        Calendar expire = java.util.Calendar.getInstance();
        expire.roll(Calendar.HOUR_OF_DAY, -2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        boSession[] sessionsActive = boApplication.currentContext().getEboContext().getBoSession().getApplication().getSessions().getActiveSessions();
        for (int i = 0; i < sessionsActive.length; i++) {
            if (sessionsActive[i].getUser().getName().equalsIgnoreCase("SYSTEM")) continue;
            this.createRow();
            this.createRowAttribute("USER", sessionsActive[i].getUser().getName());
            this.createRowAttribute("CLIENT_NAME", sessionsActive[i].getClientName());
            this.createRowAttribute("CREATED_TIME", dateFormat.format(sessionsActive[i].getCreatedTime()));
            this.createRowAttribute("LAST_ACTIVITY", dateFormat.format(sessionsActive[i].getLastActivity()));
            if (expire.getTime().after(sessionsActive[i].getLastActivity())) {
                this.createRowAttribute("ACTIVE", "No");
                sessionsActive[i].closeSession();
            } else {
                this.createRowAttribute("ACTIVE", "Yes");
            }
        }
    }
}
