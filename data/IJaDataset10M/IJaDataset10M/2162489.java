package pa_SymposiumPlanner;

import javax.swing.tree.*;
import javax.servlet.*;
import java.io.*;
import java.net.URL;
import nu.xom.*;
import jdrew.oo.td.*;
import jdrew.oo.util.*;
import java.util.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import pa_Configuration.BadConfigurationException;
import pa_Configuration.PAConfiguration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.oreilly.servlet.HttpMessage;

public class RuleML2008PanelChair extends HttpServlet {

    public static final String FS = System.getProperty("file.separator");

    private final String instantiation = "SymposiumPlanner08";

    private final String topic = "PanelChair";

    private String address;

    private String port;

    private String poslAddress;

    private String rdfAddress;

    private String messageEndpoint;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        System.out.println("Publicty Chair Servlet RuleML-2009");
        out.println("Publicty Chair Servlet RuleML-2009");
        Calendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String date;
        if (month == 10 || month == 11 || month == 12) date = "" + year + month + day; else date = "" + year + "0" + month + day;
        date = "date(" + date + ":integer).";
        System.out.println("Publicty Chair Servlet Console update:");
        System.out.println(date);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            address = PAConfiguration.getAddress();
            port = PAConfiguration.getPort(instantiation);
            poslAddress = PAConfiguration.getPOSL(instantiation, topic);
            rdfAddress = PAConfiguration.getRDFTaxonomy(instantiation);
            messageEndpoint = PAConfiguration.getEndpointName(instantiation, topic);
        } catch (BadConfigurationException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            System.out.println("5 Publicty Chair Servlet");
            System.out.println(response.toString());
            BufferedReader brd = request.getReader();
            String input = "";
            String message = "";
            while (!input.equals("</RuleML>")) {
                input = brd.readLine();
                message = message + input;
            }
            String[] varOrder = getVariableOrder(message);
            System.out.println("Received Message: " + message);
            POSLParser pp = new POSLParser();
            Date t1 = new GregorianCalendar().getTime();
            System.out.println(t1.getHours() + ":" + t1.getMinutes());
            System.out.println("day: " + t1.getDay());
            System.out.println("day: " + t1.getYear());
            System.out.println("day: " + t1.getMonth());
            String time = "time(" + t1.getHours() + ":integer).";
            System.out.println(time);
            String url = poslAddress;
            String contents = "";
            int day = t1.getDay();
            boolean weekday = true;
            if (day == 0 || day == 6) {
                weekday = false;
            }
            String dayOfWeek;
            if (weekday) {
                dayOfWeek = "day(weekday).";
            } else {
                dayOfWeek = "day(weekend).";
            }
            Calendar cal = new GregorianCalendar();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day2 = cal.get(Calendar.DAY_OF_MONTH);
            String date;
            String day3 = "" + day2;
            if (day2 == 1 || day2 == 2 || day2 == 3 || day2 == 4 || day2 == 5 || day2 == 6 || day2 == 7 || day2 == 8 || day2 == 9) {
                day3 = "0" + day2;
            }
            if (month == 10 || month == 11 || month == 12) date = "" + year + month + day3; else date = "" + year + "0" + month + day3;
            date = "date(" + date + ":integer).";
            System.out.println(date);
            String url2 = rdfAddress;
            HttpClient client2 = new HttpClient();
            GetMethod method2 = new GetMethod(url2);
            method2.setFollowRedirects(true);
            String typestr = "";
            int statusCode2 = client2.executeMethod(method2);
            if (statusCode2 != -1) {
                typestr = method2.getResponseBodyAsString();
            }
            System.out.println("Types: " + typestr);
            Types.reset();
            RDFSParser.parseRDFSString(typestr);
            try {
                HttpClient client = new HttpClient();
                GetMethod method = new GetMethod(url);
                method.setFollowRedirects(true);
                int statusCode = client.executeMethod(method);
                if (statusCode != -1) {
                    contents = method.getResponseBodyAsString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            contents = contents + "\n" + time;
            contents = contents + "\n" + dayOfWeek;
            contents = contents + "\n" + date;
            BackwardReasoner br = new BackwardReasoner();
            Iterator solit = null;
            DefiniteClause dc = null;
            SymbolTable.reset();
            pp.parseDefiniteClauses(contents);
            br.loadClauses(pp.iterator());
            System.out.println("TEST");
            Iterator it = pp.iterator();
            while (it.hasNext()) {
                DefiniteClause d = (DefiniteClause) it.next();
                System.out.println("Loaded clause: " + d.toPOSLString());
            }
            br = new BackwardReasoner(br.clauses, br.oids);
            MessageParser m = new MessageParser(message);
            Element atom = null;
            try {
                atom = m.parseForContent();
            } catch (Exception e) {
                System.out.println("Invalid Message");
            }
            QueryBuilder q = new QueryBuilder(atom);
            String query = q.generateDoc();
            System.out.println("ABOUT TO INPUT THIS QUERY:" + query);
            RuleMLParser qp = new RuleMLParser();
            try {
                dc = qp.parseRuleMLQuery(query);
            } catch (Exception e) {
                System.out.println("Invalid Query");
            }
            solit = br.iterativeDepthFirstSolutionIterator(dc);
            int varSize = 0;
            while (solit.hasNext()) {
                Vector data = new Vector();
                BackwardReasoner.GoalList gl = (BackwardReasoner.GoalList) solit.next();
                Hashtable varbind = gl.varBindings;
                javax.swing.tree.DefaultMutableTreeNode root = br.toTree();
                root.setAllowsChildren(true);
                javax.swing.tree.DefaultTreeModel dtm = new DefaultTreeModel(root);
                int i = 0;
                Object[][] rowdata = new Object[varbind.size()][2];
                varSize = varbind.size();
                Enumeration e = varbind.keys();
                while (e.hasMoreElements()) {
                    Object k = e.nextElement();
                    Object val = varbind.get(k);
                    String ks = (String) k;
                    rowdata[i][0] = ks;
                    rowdata[i][1] = val;
                    i++;
                }
                data.addElement(rowdata);
                String[] messages = new String[data.size()];
                MessageGenerator g = new MessageGenerator(data, varSize, messageEndpoint, m.getId(), m.getProtocol(), m.getRel(), varOrder);
                messages = g.Messages2();
                String appender = "";
                URL sender = new URL(address + ":" + port);
                HttpMessage msg = new HttpMessage(sender);
                Properties props = new Properties();
                for (int i1 = 0; i1 < data.size(); i1++) {
                    System.out.println(i1 + ")" + messages[i1].toString());
                    props.put("text", messages[i1].toString());
                    InputStream in = msg.sendGetMessage(props);
                }
                System.out.println("NEXT MESSAGE");
            }
            MessageGenerator g = new MessageGenerator(null, varSize, messageEndpoint, m.getId(), m.getProtocol(), m.getRel());
            URL sender = new URL(address + ":" + port);
            HttpMessage msg = new HttpMessage(sender);
            Properties props = new Properties();
            String finalMessage = g.finalMessage(query);
            System.out.println(finalMessage);
            props.put("text", finalMessage);
            InputStream in = msg.sendGetMessage(props);
            System.out.println("Stop_Communication");
        } catch (Exception e) {
            System.out.println("ERROR has occured : " + e.toString());
        }
        out.close();
    }

    String getRequestParam(HttpServletRequest request, String param) {
        if (request != null) {
            String paramVal = request.getParameter(param);
            return paramVal;
        }
        return null;
    }

    /**
	 * Is used to remember the order of variables so that the message returned
	 * to the OA does not contain randomly ordered data.
	 * 
	 * @param message The RuleML message
	 * 
	 * @return An array containing the order of the variables.
	 */
    public static String[] getVariableOrder(String message) {
        Vector<String> variables = new Vector<String>();
        String[] variableList;
        StringTokenizer st = new StringTokenizer(message, "<");
        String temp = "";
        String tempVar = "";
        while (st.hasMoreTokens()) {
            temp = st.nextToken();
            if (temp.startsWith("Var>")) {
                tempVar = "";
                for (int i = 4; i < temp.length(); i++) {
                    if (temp.charAt(i) == '<') break; else tempVar = tempVar + temp.charAt(i);
                }
                variables.addElement(tempVar);
            }
        }
        variableList = new String[variables.size()];
        for (int i = 0; i < variables.size(); i++) {
            variableList[i] = variables.elementAt(i);
        }
        return variableList;
    }
}
