package net.sf.brightside.mobilestock.service.hibernate;

import java.util.Iterator;
import java.util.List;
import net.sf.brightside.mobilestock.metamodel.beans.MonitoringSystemBean;
import net.sf.brightside.mobilestock.service.IGetType;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

public class GetTestNoMocks {

    Logger log = Logger.getLogger(GetTestNoMocks.class);

    @SuppressWarnings("unchecked")
    @Test
    public void getCustomers() {
        IGetType<MonitoringSystemBean> getCommand = new GetTypeImpl();
        getCommand.setType(MonitoringSystemBean.class);
        List<MonitoringSystemBean> list = getCommand.execute();
        log.debug("Items in table: " + list.size());
        if (list.size() > 0) {
            log.debug("Found more than one Monitoring System, giving more infos");
            int count = 1;
            for (Iterator<MonitoringSystemBean> iterator = list.iterator(); iterator.hasNext(); ) {
                MonitoringSystemBean monitoringSystemBean = iterator.next();
                log.debug("Info for system No. " + count);
                if (monitoringSystemBean.getUser() != null) {
                    log.debug("User name: " + monitoringSystemBean.getUser().getName());
                } else log.debug("No user for this system got from database");
                if (monitoringSystemBean.getShares().size() != 0) {
                    log.debug("There are shares for this user");
                } else {
                    log.debug("There are no shares for this user loaded from database");
                }
                count++;
            }
        }
    }
}
