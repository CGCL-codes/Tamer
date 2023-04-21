package uk.co.westhawk.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import uk.co.westhawk.snmp.stack.*;
import uk.co.westhawk.snmp.pdu.*;
import java.util.*;
import java.io.*;
import java.text.*;

/**
 * <p>
 * The Interfaces servlet collects information about all the interfaces
 * on a SNMP server. 
 * </p>
 *
 * <p>
 * It will collect continuously information (depending on the update
 * interval) unregarded it being accessed.
 * It will start when the servlet is 
 * initialized (that is when the web server is started and loading all its 
 * servlets) until it is destroyed.
 * </p>
 *
 * <p>
 * <ul>
 * The following parameters should be configured when installing this
 * servlet on a Java based webserver:
 * <li>HOST the hostname or IP address of the SNMP server, default
 * <em>localhost</em></li>
 * <li>PORT the port number of the SNMP server, default
 * <em>161</em></li>
 * <li>INTERVAL the update interval, default <em>10 sec</em></li>
 * </ul>
 * </p>
 *
 * <p>
 * When accessing this servlet (with a GET action) it will print its 
 * last updated information to your webpage. 
 * The servlet will respond quickly, since it already has the
 * information.
 * This information can easily be parsed
 * so you can to combine it with an application.
 * </p>
 *
 * <a name="_general_"></a>
 * <p>
 * In order to use a servlet you need to have a Java based webserver
 * where you can add and removed servlets. 
 * </p>
 *
 * <p>
 * <ul>
 * The benefit of an servlet is that:
 * <li>this servlet is the only one to collect this information</li>
 * <li>every one (that is allowed by the web server) can access
 * this information</li> 
 * <li>the network traffic will be kept low</li>
 * <li>the chance of success is bigger, since a servlet has much more 
 * privileges than an application</li>
 * <li>no application is needed if the servlet returns the information as
 * (html) text</li>
 * </ul>
 * </p>
 *
 * @see InterfaceGetNextPdu
 * @see uk.co.westhawk.examplev1.getAllInterfaces
 * @author <a href="mailto:snmp@westhawk.co.uk">Birgit Arkesteijn</a>
 * @version $Revision: 3.14 $ $Date: 2006/02/02 15:49:39 $
 */
public class Interfaces extends HttpServlet implements Observer, Runnable {

    private static final String version_id = "@(#)$Id: Interfaces.java,v 3.14 2006/02/02 15:49:39 birgit Exp $ Copyright Westhawk Ltd";

    private InterfaceGetNextPdu pdu;

    private Hashtable allInt;

    private SnmpContext context = null;

    private String hashKeyPart;

    private boolean mayLoopStart;

    private Date lastUpdateDate;

    private SimpleDateFormat dateFormat;

    private Thread me;

    private String host;

    private int port;

    private int sleep_time;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        letItBegin(config);
    }

    public synchronized void destroy() {
        super.destroy();
        mayLoopStart = false;
        context.destroy();
        context = null;
        allInt.clear();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (context != null) {
            showAllInterface(res);
        } else {
            letItBegin(null);
        }
    }

    public void setHost(String h) {
        host = h;
    }

    public void setPort(String p) {
        port = convertInt(p, 161);
    }

    /**
 * Set the update interval time in sec.
 */
    public void setInterval(String s) {
        sleep_time = convertInt(s, 10);
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return ("" + port);
    }

    public String getInterval() {
        return ("" + sleep_time);
    }

    protected void letItBegin(ServletConfig config) throws ServletException {
        String tmp;
        tmp = getStringParameter(config, "HOST", "localhost");
        setHost(tmp);
        tmp = getStringParameter(config, "PORT", "161");
        setPort(tmp);
        tmp = getStringParameter(config, "INTERVAL", "10");
        setInterval(tmp);
        hashKeyPart = new String(host + String.valueOf(port));
        try {
            context = new SnmpContext(host, port);
            context.setCommunity("public");
            allInt = new Hashtable();
            mayLoopStart = true;
            me = new Thread(this);
            me.setPriority(Thread.MIN_PRIORITY);
            me.start();
        } catch (java.io.IOException exc) {
            throw new ServletException("IOException: " + exc.getMessage());
        }
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        lastUpdateDate = new Date();
    }

    protected long getLastModified(HttpServletRequest req) {
        return lastUpdateDate.getTime();
    }

    public void run() {
        while (context != null) {
            if (mayLoopStart == true) {
                mayLoopStart = false;
                pdu = new InterfaceGetNextPdu(context);
                pdu.addObserver(this);
                pdu.addOids(null);
                try {
                    pdu.send();
                } catch (java.io.IOException exc) {
                    System.out.println("run(): IOException " + exc.getMessage());
                } catch (PduException exc) {
                    System.out.println("run(): PduException " + exc.getMessage());
                }
            }
            try {
                Thread.sleep(sleep_time * 1000);
            } catch (InterruptedException ix) {
                ;
            }
        }
    }

    public void update(Observable obs, Object ov) {
        InterfaceGetNextPdu prev;
        String hashKey;
        if (pdu.getErrorStatus() == AsnObject.SNMP_ERR_NOERROR) {
            lastUpdateDate = new Date();
            hashKey = hashKeyPart + pdu.getIfIndex();
            if ((prev = (InterfaceGetNextPdu) allInt.get(hashKey)) != null) {
                pdu.getSpeed(prev);
            }
            allInt.put(hashKey, pdu);
            prev = pdu;
            pdu = new InterfaceGetNextPdu(context);
            pdu.addObserver(this);
            pdu.addOids(prev);
            try {
                pdu.send();
            } catch (java.io.IOException exc) {
                System.out.println("update(): IOException " + exc.getMessage());
            } catch (PduException exc) {
                System.out.println("update(): PduException " + exc.getMessage());
            }
        } else {
            mayLoopStart = true;
        }
    }

    protected synchronized void showAllInterface(HttpServletResponse res) {
        res.setStatus(res.SC_OK);
        res.setContentType("text/plain");
        InterfaceGetNextPdu tpdu;
        Enumeration e = allInt.elements();
        PrintWriter out;
        try {
            out = res.getWriter();
            out.println(allInt.size());
            while (e.hasMoreElements()) {
                tpdu = (InterfaceGetNextPdu) e.nextElement();
                printInterface(tpdu, out);
            }
            out.println(dateFormat.format(lastUpdateDate));
            out.flush();
            out.close();
        } catch (IOException exc) {
            ;
        }
    }

    protected synchronized void showAllInterface(HttpServletResponse res, int index) {
        res.setStatus(res.SC_OK);
        res.setContentType("text/plain");
        InterfaceGetNextPdu tpdu;
        String hashKey;
        PrintWriter out;
        hashKey = hashKeyPart + index;
        try {
            out = res.getWriter();
            if ((tpdu = (InterfaceGetNextPdu) allInt.get(hashKey)) != null) {
                printInterface(tpdu, out);
            } else {
                out.println(index);
            }
            out.flush();
            out.close();
        } catch (IOException exc) {
            ;
        }
    }

    public String getServletInfo() {
        return "Returns all the interfaces of host " + host + " on port " + port;
    }

    private void printInterface(InterfaceGetNextPdu tpdu, PrintWriter out) {
        int index = tpdu.getIfIndex();
        long speed = tpdu.getSpeed();
        int opr = tpdu.getIfOperStatus();
        String descr = tpdu.getIfDescr();
        out.println(index + ", " + speed + ", " + opr + ", " + descr);
    }

    private String getStringParameter(ServletConfig config, String str, String def) {
        String value;
        if (config != null) {
            value = config.getInitParameter(str);
        } else {
            value = getInitParameter(str);
        }
        if (value == null) {
            value = def;
        }
        return value;
    }

    private int convertInt(String val, int def) {
        int value = def;
        try {
            value = Integer.valueOf(val).intValue();
        } catch (NumberFormatException exc) {
            ;
        }
        return value;
    }
}
