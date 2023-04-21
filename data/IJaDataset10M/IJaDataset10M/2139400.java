package org.mobicents.example.slee.connection;

import javax.naming.InitialContext;
import javax.slee.EventTypeID;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;
import javax.slee.connection.SleeConnectionFactory;
import org.apache.log4j.Logger;
import org.mobicents.example.slee.connection.SleeConnectionTestMBean;
import org.mobicents.slee.service.events.CustomEvent;

public class SleeConnectionTest implements SleeConnectionTestMBean {

    private static final Logger logger = Logger.getLogger(SleeConnectionTest.class);

    private static final String eventName = "org.mobicents.slee.service.connectivity.Event_1";

    private static final String eventVendor = "org.mobicents";

    private static final String eventVersion = "1.0";

    public void fireEvent(String messagePassed) {
        logger.info("Attempting call to SleeConnectionFactory.");
        try {
            InitialContext ic = new InitialContext();
            SleeConnectionFactory factory = (SleeConnectionFactory) ic.lookup("java:/MobicentsConnectionFactory");
            SleeConnection conn1 = null;
            try {
                conn1 = factory.getConnection();
                ExternalActivityHandle handle = conn1.createActivityHandle();
                EventTypeID requestType = conn1.getEventTypeID(eventName, eventVendor, eventVersion);
                CustomEvent customEvent = new CustomEvent();
                customEvent.setMessage(messagePassed);
                logger.info("The event type is: " + requestType);
                conn1.fireEvent(customEvent, requestType, handle, null);
            } finally {
                if (conn1 != null) conn1.close();
            }
        } catch (Exception e) {
            logger.error("Exception caught in event fire method!", e);
        }
    }
}
