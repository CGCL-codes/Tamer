package org.nexopenframework.audit.management;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.management.Notification;
import javax.management.ObjectName;
import org.nexopenframework.jmx.OperationInvokedNotificationContext;
import org.nexopenframework.services.ha.AttributeChangeNotification;
import org.nexopenframework.services.ha.HAManager;
import org.nexopenframework.services.ha.OperationInvokedNotification;
import org.springframework.util.ReflectionUtils;

/**
 * 
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @see HAManager#getHANotificationBroadcaster()
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class AuditHASupport extends AuditSupport implements AuditHASupportMBean {

    public AuditHASupport(ObjectName objName) {
        super(objName);
    }

    /**
	 * <p>Update properties from a cluster</p>
	 * 
	 * @see org.nexopenframework.audit.management.AuditHASupportMBean#updateDistributedAttribute(java.lang.String, java.io.Serializable)
	 */
    public void updateDistributedAttribute(final String propertyName, final Serializable value) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received property [" + propertyName + "] with value [" + value + "]");
        }
        ReflectionUtils.doWithFields(this.getClass(), new ReflectionUtils.FieldCallback() {

            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if (matchsField(propertyName, field)) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.set(AuditHASupport.this, value);
                }
            }
        });
    }

    /**
	 * 
	 * @see org.nexopenframework.services.ha.management.DistributedUpdatableMBean#updateDistributedOperation(java.lang.String, java.io.Serializable[], java.lang.String[])
	 */
    public void updateDistributedOperation(final String operationName, final Serializable[] values, final String[] arguments) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received operation [" + operationName + "] with values [" + values + "]");
        }
        if (operationName != null && operationName.equals("incrementFailed")) {
            failed.incrementAndGet();
        }
    }

    /**
	 * <p>Sends notification and brodcasts to other nodes in the cluster</p>
	 * 
	 * @see HAManager#getHANotificationBroadcaster()
	 * @see javax.management.NotificationBroadcasterSupport#sendNotification(javax.management.Notification)
	 */
    public void sendNotification(Notification notification) {
        try {
            super.sendNotification(notification);
        } finally {
            if (notification instanceof javax.management.AttributeChangeNotification) {
                javax.management.AttributeChangeNotification acn = (javax.management.AttributeChangeNotification) notification;
                AttributeChangeNotification haNotification = new AttributeChangeNotification(this.getObjectName(), acn.getAttributeName(), (Serializable) acn.getNewValue());
                HAManager.getHANotificationBroadcaster().sendNotification(haNotification);
            } else {
                String type = notification.getType();
                if (type.equals(OPERATION_INVOKED)) {
                    OperationInvokedNotificationContext userData = (OperationInvokedNotificationContext) notification.getUserData();
                    OperationInvokedNotification haNotification = new OperationInvokedNotification(this.getObjectName(), userData.getOperationName(), userData.getValues(), userData.getArguments());
                    HAManager.getHANotificationBroadcaster().sendNotification(haNotification);
                }
            }
        }
    }

    /**
	 * @param propertyName
	 * @param field
	 * @return
	 */
    protected boolean matchsField(String propertyName, Field field) {
        return (field.getName().equals(propertyName));
    }

    /**
	 * @param propertyName
	 * @param field
	 * @return
	 */
    protected boolean matchsMethod(String methodName, String[] arguments, Method m) {
        return (m.getName().equals(methodName) && m.getParameterTypes().length == arguments.length);
    }
}
