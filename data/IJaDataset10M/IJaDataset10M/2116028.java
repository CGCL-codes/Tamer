package uk.ac.cam.ch.wwmm.ws;

import java.io.StringReader;
import java.util.logging.*;
import java.util.Iterator;
import java.util.List;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.XPath;
import org.xml.sax.InputSource;
import uk.ac.cam.ch.wwmm.Utils;

/**
 *  This Web Service is used to convert a Script file to Script(Bash, Batch, ...) files for "non-interactive" computation.
 *
 *@author     Yong Zhang <yz237@cam.ac.uk>
 *@created    2005-01-07
 */
public class XSculf2Script {

    private static Logger logger = Logger.getLogger("uk.ac.cam.ch.wwmm.ws.XSculf2Script");

    private String propsFilePath = System.getProperty("user.dir") + "/webapps/wwmm/wwmm.properties";

    private Utils u = new Utils();

    /**
     *  Run Web Services in the script on the input data.
     *
     *@param  script  WWMM Web Service Computing script
     *@param  inputData molecules in any format
     *
     *@return result of the execution of the batch of Web Services
     */
    public String transfer(String script, String inputData) {
        logger.info("Running WSComputing script ...");
        String output = "";
        SAXReader xmlReader = new SAXReader();
        Document doc = null;
        try {
            doc = xmlReader.read(new InputSource(new StringReader(script)));
        } catch (DocumentException de) {
            logger.severe("Fatal: " + de);
            return "<WWMMWSComputing><step><name>Fatal</name><result>" + de + "</result></step></WWMMWSComputing>";
        }
        XPath xpathSelector = DocumentHelper.createXPath("/WWMMWSComputing/step");
        List results = xpathSelector.selectNodes(doc);
        Node step;
        for (Iterator iter = results.iterator(); iter.hasNext(); ) {
            step = (Node) iter.next();
            String ws = ((Node) step.selectNodes("name").get(0)).getText();
            logger.info("ws = " + ws);
            String method = ((Node) step.selectNodes("method").get(0)).getText();
            logger.info("method = " + method);
            List nl = step.selectNodes("parameters/parameter");
            int pCount = nl.size();
            Object[] pList = new Object[pCount];
            for (int i = 0; i < pCount; i++) {
                String p = ((Node) nl.get(i)).getText();
                if (p.equalsIgnoreCase("Output of the previous step")) {
                    logger.info("parameter " + i + " = " + inputData);
                    pList[i] = (Object) inputData;
                } else {
                    logger.info("parameter " + i + " = " + p);
                    pList[i] = (Object) p;
                }
            }
            String endpoint = Utils.getProperty(propsFilePath, ws + "WS");
            String resCall = null;
            try {
                Service service = new Service();
                Call call = (Call) service.createCall();
                call.setTargetEndpointAddress(new java.net.URL(endpoint));
                call.setOperationName(method);
                resCall = (String) call.invoke(pList);
            } catch (Exception e) {
                logger.severe("Error! " + e.toString());
                e.printStackTrace();
            }
            if (resCall == null) {
                String error = "Web Service " + ws + " failed!";
                logger.severe(error);
                return "<WWMMWSComputing><step><name>" + ws + "</name><result>null</result></step></WWMMWSComputing>";
            } else {
                inputData = resCall;
                logger.info("res = " + inputData);
            }
            Element re = ((Element) step).addElement("result").addCDATA(inputData);
        }
        logger.info("Done!");
        output = Utils.node2String((Node) doc.getRootElement());
        return output;
    }
}
