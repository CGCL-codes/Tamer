package com.strangebreeds.therefromhere;

import java.lang.Math;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

public class JobServlet extends HttpServlet {

    private static final long serialVersionUID = 20041227;

    private Logger logger = Logger.getLogger(JobServlet.class);

    {
        org.apache.log4j.PropertyConfigurator.configure("log4j.properties");
        logger.setLevel(Level.DEBUG);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fromPage = request.getParameter("frompage");
        String toPage = request.getParameter("topage");
        String searchStrategy = request.getParameter("strategy");
        String emailAddress = request.getParameter("emailaddress");
        Integer jobID = submitJob(fromPage, toPage, searchStrategy, emailAddress);
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        String xmlReceipt = writeReceipt(fromPage, toPage, searchStrategy, emailAddress, jobID);
        logger.debug("Wrote receipt: " + xmlReceipt);
        out.println(xmlReceipt);
        out.close();
    }

    private String writeReceipt(String fromPage, String toPage, String searchStrategy, String emailAddress, Integer jobID) {
        XMLCreator xmlResponse = new XMLCreator();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Element receiptElement = xmlResponse.createXMLElement("receipt");
        Element requestElement = xmlResponse.createXMLElement("request");
        requestElement.setAttribute("submit_time", df.format(new Date()));
        Element fromElement = xmlResponse.createXMLElement("from");
        fromElement.appendChild(xmlResponse.createXMLTextNode(fromPage));
        requestElement.appendChild(fromElement);
        Element toElement = xmlResponse.createXMLElement("to");
        toElement.appendChild(xmlResponse.createXMLTextNode(toPage));
        requestElement.appendChild(toElement);
        Element strategyElement = xmlResponse.createXMLElement("strategy");
        strategyElement.appendChild(xmlResponse.createXMLTextNode(searchStrategy));
        requestElement.appendChild(strategyElement);
        Element emailElement = xmlResponse.createXMLElement("email_address");
        emailElement.appendChild(xmlResponse.createXMLTextNode(emailAddress));
        requestElement.appendChild(emailElement);
        receiptElement.appendChild(requestElement);
        Element jobElement = xmlResponse.createXMLElement("submitted_job");
        jobElement.setAttribute("id", jobID.toString());
        receiptElement.appendChild(jobElement);
        xmlResponse.addNode(receiptElement);
        return xmlResponse.getDocAsXMLString(true);
    }

    private Integer submitJob(String fromPage, String toPage, String searchStrategy, String emailAddress) {
        Integer jobNumber = Math.abs(new Random().nextInt());
        try {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
            properties.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
            properties.put(Context.PROVIDER_URL, "localhost:1099");
            Context context = new InitialContext(properties);
            QueueSession session;
            Queue queue;
            QueueConnection cnn;
            queue = (Queue) context.lookup("queue/tfh_job_q");
            QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
            cnn = factory.createQueueConnection();
            session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            QueueSender sender = session.createSender(queue);
            logger.debug("Creating job " + jobNumber + " with the following parameters: fromPage = " + fromPage + " toPage = " + toPage + " searchStrategy = " + searchStrategy + " emailAddress = " + emailAddress);
            MapMessage msg = session.createMapMessage();
            msg.setInt("jobNumber", jobNumber);
            msg.setString("fromPage", fromPage);
            msg.setString("toPage", toPage);
            msg.setString("searchStrategy", searchStrategy);
            msg.setString("emailAddress", emailAddress);
            sender = session.createSender(queue);
            sender.send(msg);
            logger.debug("Job " + jobNumber + " submitted");
        } catch (NamingException ne) {
            logger.error("Got a NamingException in the servlet: " + ne.getMessage(), ne);
        } catch (JMSException je) {
            logger.error("Got a JMSException in the servlet: " + je.getMessage(), je);
        }
        return jobNumber;
    }
}
