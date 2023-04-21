package org.s3b.description.mms;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.s3b.db.ConfigKeeper;

public class MMS {

    public static final String BIBTEX = "BIBTEX";

    public static final String BIBTEXML = "BIBTEXML";

    public static final String BIBTEXRDF = "BIBTEXRDF";

    public static final String DUBLINCORE = "DUBLINCORE";

    private static Logger logger = Logger.getLogger("org.jeromedl.description.mms");

    public static final String MARC21 = "MARC21";

    public static final String MARCONT = "MARCONT";

    public static final String MARCRDF = "MARCRDF";

    public static final String MARCXML = "MARCXML";

    public static final String SWRC = "SWRC";

    public static String translate(String srcDescription, String srcType, String dstType) {
        URL targetEndPoint = null;
        try {
            targetEndPoint = new URL(ConfigKeeper.getProperty("jeromedl.mms.targetEndPoint"));
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, e.toString());
        }
        Call call = null;
        String dstDescription = null;
        try {
            call = (Call) new Service().createCall();
        } catch (ServiceException e) {
            logger.log(Level.SEVERE, e.toString());
        } finally {
            if (call != null) {
                call.setTargetEndpointAddress(targetEndPoint);
                call.setOperationName("translate");
                call.addParameter("rdf", XMLType.XSD_STRING, ParameterMode.IN);
                call.addParameter("srcType", XMLType.XSD_STRING, ParameterMode.IN);
                call.addParameter("destType", XMLType.XSD_STRING, ParameterMode.IN);
                call.setReturnType(XMLType.XSD_STRING);
                try {
                    dstDescription = (String) call.invoke(new Object[] { srcDescription, srcType, dstType });
                } catch (RemoteException e) {
                    logger.log(Level.SEVERE, e.toString());
                }
            }
        }
        return dstDescription;
    }
}
