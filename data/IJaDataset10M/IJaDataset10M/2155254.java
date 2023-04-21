package com.sun.j2ee.blueprints.airlinesupplier.powebservice;

import javax.ejb.*;
import java.rmi.*;
import javax.naming.*;
import javax.jms.*;
import com.sun.j2ee.blueprints.airlinesupplier.JNDINames;
import com.sun.j2ee.blueprints.servicelocator.*;
import com.sun.j2ee.blueprints.servicelocator.ejb.*;

/**
 *  This class is the entry point for purchase orders submitted 
 *  by OPC to the airline suppliers;
 */
public class AirlinePOEndpointBean implements SessionBean {

    private SessionContext sc;

    public AirlinePOEndpointBean() {
    }

    public void ejbCreate() throws CreateException {
    }

    /**
     * Receive an order, preprocess the request, submit request to workflow
     * and return the order id so that the caller can have a correlation id
     * for the order
     */
    public String submitAirlineReservationDetails(String xmlPO) throws InvalidOrderException, OrderSubmissionException, RemoteException {
        AirlineOrder flightObj = preProcessInput(xmlPO);
        submitRequest(flightObj);
        return ("FLIGHT1234");
    }

    private AirlineOrder preProcessInput(String po) throws InvalidOrderException {
        return (AirlineOrder.fromXML(po));
    }

    private void submitRequest(AirlineOrder flight) throws OrderSubmissionException {
        ConnectionFactory jmsConnectionFactory = null;
        Destination dest = null;
        Connection jmsConnection = null;
        Session jmsSession = null;
        MessageProducer jmsSender = null;
        try {
            ServiceLocator sl = new ServiceLocator();
            jmsConnectionFactory = (ConnectionFactory) sl.getJMSConnectionFactory(JNDINames.AIRLINE_QUEUECONNECTIONFACTORY);
            dest = sl.getJMSDestination(JNDINames.AIRLINE_QUEUE);
            jmsConnection = jmsConnectionFactory.createConnection();
            jmsSession = jmsConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            jmsSender = jmsSession.createProducer(dest);
            ObjectMessage message = jmsSession.createObjectMessage();
            message.setObject(flight);
            jmsSender.send(message);
        } catch (ServiceLocatorException se) {
            throw new OrderSubmissionException("Error while sending a message:" + se.getMessage());
        } catch (JMSException e) {
            throw new OrderSubmissionException("Error while sending a message:" + e.getMessage());
        } finally {
            if (jmsSender != null) {
                try {
                    jmsSender.close();
                } catch (JMSException e) {
                    throw new OrderSubmissionException("Error sender close");
                }
            }
            if (jmsSession != null) {
                try {
                    jmsSession.close();
                } catch (JMSException e) {
                    throw new OrderSubmissionException("Error session close");
                }
            }
            if (jmsConnection != null) {
                try {
                    jmsConnection.close();
                } catch (JMSException e) {
                    throw new OrderSubmissionException("Error Connection close");
                }
            }
        }
    }

    public void setSessionContext(SessionContext sc) {
        this.sc = sc;
    }

    public void ejbRemove() throws RemoteException {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }
}
