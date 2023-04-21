package pa_WellnessRules2;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.swing.tree.DefaultTreeModel;
import jdrew.oo.td.BackwardReasoner;
import jdrew.oo.util.DefiniteClause;
import jdrew.oo.util.POSLParser;
import jdrew.oo.util.RDFSParser;
import jdrew.oo.util.RuleMLParser;
import jdrew.oo.util.SymbolTable;
import jdrew.oo.util.Types;
import nu.xom.Element;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import com.oreilly.servlet.HttpMessage;

/**
 * WellnessRules (Any) PA:
 * 
 * This class handles POSL method calls for PA's such as generating POSL profiles
 * parsing a RuleML query, and answering an POSL query with OO jDREW.
 * 
 * 
 * @author Taylor Michael Osmun
 */
public class POSLHandler {

    /**
	 * Given the message and sending information, will answer the query and
	 * send it off to the OA.
	 * 
	 * @param contents The message send by the EA.
	 * @param message Another version of the message.
	 * @param address The IP of the mule ESB.
	 * @param port The port of the OA on the server.
	 * @param messageEndpoint The endpoint of the PA.
	 * @param m The message parser containing sending information.
	 * @throws Exception General exception.
	 */
    public static void answerPOSLQuery(String contents, String message, String address, String port, String messageEndpoint, MessageParser m) throws Exception {
        BackwardReasoner br = new BackwardReasoner();
        POSLParser pp = new POSLParser();
        SymbolTable.reset();
        pp.parseDefiniteClauses(contents);
        br.loadClauses(pp.iterator());
        String[] variableOrder = getVariableOrder(message);
        Iterator it = pp.iterator();
        while (it.hasNext()) {
            DefiniteClause d = (DefiniteClause) it.next();
        }
        br = new BackwardReasoner(br.clauses, br.oids);
        Element atom = null;
        try {
            atom = m.parseForContent();
        } catch (Exception e) {
            System.out.println("Invalid Message");
        }
        QueryBuilder q = new QueryBuilder(atom);
        String query = q.generateDoc();
        RuleMLParser qp = new RuleMLParser();
        DefiniteClause dc = null;
        try {
            dc = qp.parseRuleMLQuery(query);
        } catch (Exception e) {
            System.out.println("Invalid Query");
        }
        Iterator solit = null;
        solit = br.iterativeDepthFirstSolutionIterator(dc);
        System.out.println("POSL Query Answered!");
        int varSize = 0;
        System.out.println("Sending Messages Through: " + address + ":" + port + " To Endpoint: " + messageEndpoint);
        int numAnswers = 0;
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
            MessageGenerator g = new MessageGenerator(data, varSize, messageEndpoint, m.getId(), m.getProtocol(), m.getRel(), variableOrder);
            messages = g.Messages(message);
            URL sender = new URL(address + ":" + port);
            HttpMessage msg = new HttpMessage(sender);
            Properties props = new Properties();
            for (int i1 = 0; i1 < data.size(); i1++) {
                props.put("text", messages[i1].toString());
                InputStream in = msg.sendGetMessage(props);
            }
            numAnswers++;
        }
        System.out.println(numAnswers + " messages sent.");
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
                for (int i = (temp.indexOf("Var>") + 4); i < temp.length(); i++) {
                    if (temp.charAt(i) == '<') break; else tempVar = tempVar + temp.charAt(i);
                }
                variables.addElement(tempVar);
            } else if (temp.startsWith("Var type=")) {
                tempVar = "";
                for (int i = (temp.indexOf("\">") + 2); i < temp.length(); i++) {
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

    /**
	 * Get parameters from the request URL.
	 * 
	 * @param request The request URL.
	 * @param The parameters.
	 * 
	 * @return The request parameters.
	 */
    public String getRequestParam(HttpServletRequest request, String param) {
        if (request != null) {
            String paramVal = request.getParameter(param);
            return paramVal;
        }
        return null;
    }

    /**
	 * Registers the types from the RDF file
	 * located at the provided URL.
	 * 
	 * @param rdfAddress The address of the RDF file.
	 * @throws Exception A general exception.
	 */
    public static void registerPOSLTypes(String rdfAddress) throws Exception {
        String url3 = rdfAddress;
        HttpClient client3 = new HttpClient();
        GetMethod method3 = new GetMethod(url3);
        method3.setFollowRedirects(true);
        String typestr = "";
        int statusCode3 = client3.executeMethod(method3);
        if (statusCode3 != -1) {
            typestr = method3.getResponseBodyAsString();
        }
        Types.reset();
        try {
            RDFSParser.parseRDFSString(typestr);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
	 * Generates the POSL profiles from the profile specified and the list of
	 * responsible profiles.
	 * 
	 * @param profileSpecified The profile specified (if none, all are used)
	 * @param responsible The list of profiles that the PA is responsible for
	 * @param globalAddress Address of the global knowledge base.
	 */
    public static String generatePOSLProfiles(String weatherPOSLRepository, String paRootURL, String profileSpecified, String[] responsible, String globalAddress) throws NumberFormatException, IOException {
        String contents = "";
        HttpClient client = new HttpClient();
        GetMethod method;
        int statusCode;
        String url;
        boolean shouldAnswer = false;
        if (profileSpecified.equals("All")) {
            for (int i = 0; i < responsible.length; i++) {
                if (responsible[i].contains(".posl")) {
                    url = paRootURL + responsible[i];
                    try {
                        method = new GetMethod(url);
                        method.setFollowRedirects(true);
                        statusCode = client.executeMethod(method);
                        if (statusCode != -1) {
                            contents = contents + method.getResponseBodyAsString();
                            shouldAnswer = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (!profileSpecified.equals("All")) {
            for (int i = 0; i < responsible.length; i++) {
                if (responsible[i].equals(profileSpecified + ".posl")) {
                    url = paRootURL + responsible[i];
                    try {
                        method = new GetMethod(url);
                        method.setFollowRedirects(true);
                        statusCode = client.executeMethod(method);
                        if (statusCode != -1) {
                            contents = contents + method.getResponseBodyAsString();
                            shouldAnswer = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (shouldAnswer) {
            url = globalAddress;
            try {
                method = new GetMethod(url);
                method.setFollowRedirects(true);
                statusCode = client.executeMethod(method);
                if (statusCode != -1) {
                    contents = contents + method.getResponseBodyAsString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                method = new GetMethod(weatherPOSLRepository);
                method.setFollowRedirects(true);
                statusCode = client.executeMethod(method);
                if (statusCode != -1) {
                    contents = contents + method.getResponseBodyAsString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return contents;
        } else return "NO";
    }
}
