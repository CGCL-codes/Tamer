package pl.edu.agh.ssm.server.quartzStuff;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import pl.edu.agh.ssm.beans.PropValue;
import pl.edu.agh.ssm.core.webservices.api.MBeanController;
import pl.edu.agh.ssm.core.webservices.impl.WSObjectNameImpl;
import pl.edu.agh.ssm.persistence.INotification;
import pl.edu.agh.ssm.persistence.IPropValue;
import pl.edu.agh.ssm.persistence.IProperty;
import pl.edu.agh.ssm.persistence.dao.NotificationAccessDAO;
import pl.edu.agh.ssm.persistence.dao.PropValueAccessDAO;

public class JobValueReader implements Job {

    static Logger logger = Logger.getLogger(JobValueReader.class);

    public JobValueReader() {
    }

    private boolean compare(double obj1, double obj2, int condition) {
        switch(condition) {
            case 0:
                return obj1 == obj2;
            case 1:
                return obj1 != obj2;
            case 2:
                return obj1 > obj2;
            case 3:
                return obj1 < obj2;
            case 4:
                return obj1 <= obj2;
            case 5:
                return obj1 >= obj2;
        }
        return false;
    }

    private boolean comparator(Object obj1, String obj2, int condition, String type) {
        if (Integer.class.getName().equals(type)) {
            return compare((Integer) obj1, Integer.parseInt(obj2), condition);
        } else if (Double.class.getName().equals(type)) {
            return compare((Double) obj1, Integer.parseInt(obj2), condition);
        } else if (int.class.getName().equals(type)) {
            return compare(Integer.parseInt(obj2.toString()), Integer.parseInt(obj2), condition);
        } else if (double.class.getName().equals(type)) {
            return compare(Double.parseDouble(obj2.toString()), Integer.parseInt(obj2), condition);
        } else if (String.class.getName().equals(type)) {
            switch(condition) {
                case 0:
                    return ((String) obj1).equals(obj2);
                case 1:
                    return !((String) obj1).equals(obj2);
            }
        }
        return false;
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        String instName = context.getJobDetail().getName();
        String instGroup = context.getJobDetail().getGroup();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        PropValueAccessDAO propValueAccessDAO = (PropValueAccessDAO) dataMap.get("propValueAccessDAO");
        String ssmComponentName = (String) dataMap.get("ssmComponentName");
        WSObjectNameImpl objectName = (WSObjectNameImpl) dataMap.get("objectName");
        String attributeName = (String) dataMap.get("attributeName");
        IProperty property = (IProperty) dataMap.get("property");
        MBeanController mBeanController = (MBeanController) dataMap.get("mBeanController");
        SSMSender ssmSender = (SSMSender) dataMap.get("ssmSender");
        Object value = mBeanController.getAttribute(ssmComponentName, objectName, attributeName);
        logger.info("job invoke name " + instName + " group " + instGroup + " value: " + value);
        IPropValue propValue = new PropValue();
        propValue.setDate(new Date());
        propValue.setVal(value.toString());
        propValue.setOwnerPropertyID(property.getId());
        propValueAccessDAO.create(propValue);
        NotificationAccessDAO notificationAccessDAO = (NotificationAccessDAO) dataMap.get("notificationAccessDAO");
        List<INotification> notfications = notificationAccessDAO.findActiveNotification(property);
        for (INotification notification : notfications) {
            if (value.toString().equals(notification.getLastReadedValue())) {
                continue;
            }
            notification.setLastReadedValue(value.toString());
            notificationAccessDAO.update(notification);
            if (notification.getBorderValue() != null) if (comparator(value, notification.getBorderValue(), notification.getNotificationCondition(), property.getAttributetype())) {
                ssmSender.send(notification);
            }
        }
    }
}
