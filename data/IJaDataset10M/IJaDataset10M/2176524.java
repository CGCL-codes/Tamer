package org.tripcom.ws.discover.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.tripcom.api.execution.CoreAPIImplementation;
import org.tripcom.api.execution.ExtendedAPIImplementation;
import org.tripcom.api.execution.FurtherExtendedAPIImplementation;
import org.tripcom.api.execution.ManagementImpl;
import org.tripcom.api.execution.StringExtendedAPIImplementation;
import org.tripcom.api.interfaces.CoreAPI;
import org.tripcom.api.interfaces.ExtendedAPI;
import org.tripcom.api.interfaces.FurtherExtendedAPI;
import org.tripcom.api.management.ManagementAPI;
import org.tripcom.integration.entry.SpaceURI;
import org.tripcom.integration.exception.TSAPIException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Utils for communicating with Triple Space
 * @author Neng Wang,Zhangbing Zhou,Brahmananda Sapkota
 */
public class SpaceUtil {

    private static Logger logger = Logger.getLogger(SpaceUtil.class);

    private static String signature = null;

    static ManagementImpl managementAPI = new ManagementImpl(null, null, null);

    static StringExtendedAPIImplementation myAPI = new StringExtendedAPIImplementation(null, null, null);

    static CoreAPIImplementation coreAPI = new CoreAPIImplementation(null, null, null);

    static ExtendedAPIImplementation extendAPI = new ExtendedAPIImplementation(null, null, null);

    static FurtherExtendedAPIImplementation fextendAPI = new FurtherExtendedAPIImplementation(null, null, null);

    static ValueFactoryImpl vFactory = new ValueFactoryImpl();

    static final String ROOT, SWS, SWS_NAME;

    static org.openrdf.model.URI TRIPCOM = vFactory.createURI("tsc://www.tripcom.org");

    static org.openrdf.model.URI HASSPACE = vFactory.createURI("tsc://www.tripcom.org/sws#hasSWS");

    static final String QUERY = "CONSTRUCT {?s ?p ?o} WHERE {?s ?p ?o .}";

    static final int TIMEOUT = Integer.MAX_VALUE;

    static {
        PropertyConfigurator.configure("conf/log4j.properties");
        ROOT = "tsc://localhost:8080";
        SWS = "tsc://localhost:8080/sws/";
        SWS_NAME = "tsc://localhost:8080/sws/name";
    }

    public static void saveWSML2TS(String wsml, String spaceName) {
        try {
            URI space = managementAPI.create(new SpaceURI(SWS + spaceName), null);
            Set<Statement> statements = WSMLWrapper.getStatementsFromWSML(wsml);
            System.err.println(space);
            Thread.sleep(2000);
            String result = extendAPI.out(statements, space, true, signature);
            logger.info(result);
            Statement sta = vFactory.createStatement(TRIPCOM, HASSPACE, vFactory.createURI(space.toString()));
            result = coreAPI.out(sta, new URI(SWS_NAME), true, signature);
            logger.info(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteWSMLFromTS(String spaceURI) {
        try {
            managementAPI.destroy(new SpaceURI(spaceURI), signature);
            String query = "CONSTRUCT {?s ?p <" + spaceURI.toString() + "> } WHERE {?s ?p <" + spaceURI.toString() + "> .}";
            fextendAPI.in(query, new URI(SWS_NAME), Integer.MAX_VALUE, false, signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Get semantic web services from TS
	 * 
	 * @throws URISyntaxException
	 * 
	 * @throws URISyntaxException
	 * @throws TSAPIException
	 */
    public static ArrayList<String> getAllSWS() {
        logger.info("begin get all SWSs");
        Set<Set<Statement>> webServicesStatements;
        ArrayList<String> webServices = new ArrayList<String>();
        try {
            ArrayList<URI> spaces = getAllURI4SWS();
            for (int i = 0; i < spaces.size(); i++) {
                webServicesStatements = extendAPI.rdmultiple(QUERY, spaces.get(i), TIMEOUT, false, signature);
                Set<Statement> stas = webServicesStatements.iterator().next();
                String webService = WSMLWrapper.getWSMLformStatements(stas);
                webServices.add(webService);
            }
            logger.info("get all SWSs successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webServices;
    }

    /**
	 * get all SWS spaces' URI form sws name space
	 * 
	 * @return
	 * @throws URISyntaxException
	 * @throws TSAPIException
	 */
    public static ArrayList<URI> getAllURI4SWS() {
        ArrayList<URI> spaces = new ArrayList<URI>();
        logger.info("begin get URI of SWS spaces");
        try {
            Set<Set<Statement>> swsNameSpaces = extendAPI.rdmultiple(QUERY, new URI(SWS_NAME), Integer.MAX_VALUE, null);
            Set<Statement> statements = (Set<Statement>) swsNameSpaces.iterator().next();
            Iterator it = statements.iterator();
            while (it.hasNext()) {
                Statement s = (Statement) it.next();
                logger.info(s.getObject());
                spaces.add(new URI(s.getObject().stringValue()));
            }
            logger.info("get spaces URI successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spaces;
    }

    public static String readWSMLFile(String fileName) {
        String wsml = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            byte[] readBytes = new byte[in.available()];
            in.read(readBytes);
            wsml = new String(readBytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wsml;
    }
}
