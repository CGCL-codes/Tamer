package org.openi.xmla;

import com.tonbeller.jpivot.core.ModelFactory;
import com.tonbeller.jpivot.olap.model.Dimension;
import com.tonbeller.jpivot.olap.model.OlapException;
import com.tonbeller.jpivot.olap.model.OlapItem;
import com.tonbeller.jpivot.xmla.XMLA_Dimension;
import com.tonbeller.jpivot.xmla.XMLA_Model;
import com.tonbeller.jpivot.xmla.XMLA_SOAP;
import org.apache.log4j.Logger;
import org.openi.analysis.Datasource;
import org.openi.application.Application;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * does the work of connecting to an xmla server, queries, parses results. 
 * Goal is to isolate from jpivot xmla_soap
 */
public class XmlaConnector {

    private static Logger logger = Logger.getLogger(XmlaConnector.class);

    /**
     * Constructor
     * @param arg0
     */
    public XmlaConnector() {
    }

    public XMLA_Model query(Datasource datasource, String mdxQuery) throws IOException, OlapException, SAXException {
        long start = System.currentTimeMillis();
        URL confUrl = XMLA_Model.class.getResource("config.xml");
        XMLA_Model model = (XMLA_Model) ModelFactory.instance(confUrl);
        model.setMdxQuery(mdxQuery);
        model.setUri(datasource.getServer());
        model.setCatalog(datasource.getCatalog());
        model.initialize();
        return model;
    }

    /**
     * returns cube list from the olap server using XMLA_SOAP's discoverCube method
     * @param server String
     * @param catalog String
     * @param user String
     * @param pwd String
     * @return List
     * @throws OlapException
     * @throws Exception
     */
    public List getCubeList(Datasource datasource) throws OlapException {
        XMLA_SOAP olap;
        List cubeList = null;
        if (Application.getInstance().isBasicAuthentication()) {
            olap = new XMLA_SOAP(datasource.getServer(), datasource.getUsername(), datasource.getPassword());
        } else {
            olap = new XMLA_SOAP(datasource.getServer(), "", "");
        }
        cubeList = getCubesWithoutPerspectives(datasource.getCatalog(), olap);
        return cubeList;
    }

    /**
     * Returns a List of dimension names as Strings
     * Includes the Measures! 
     * 
     * @param uri
     * @param catalog
     * @param cubeName
     * @param user
     * @param pwd
     * @return
     * @throws OlapException
     */
    public List getDimensionList(Datasource datasource, String cubeName) throws OlapException {
        List dimensions = new ArrayList();
        XMLA_SOAP olap = new XMLA_SOAP(datasource.getServer(), datasource.getUsername(), datasource.getPassword());
        Iterator olapItems = olap.discoverDim(datasource.getCatalog(), cubeName).iterator();
        while (olapItems.hasNext()) {
            OlapItem item = (OlapItem) olapItems.next();
            dimensions.add(item.getName());
        }
        return dimensions;
    }

    /**
	 * SSAS 2005 uses concept of Perspectives
	 * (http://msdn2.microsoft.com/en-us/library/ms167223.aspx) for a cube.
	 * Discover Cube SOAP request retrieves perspectives along with cubes.
	 * Since dimensions used in a cube may not available in a perspective for the cube,
	 * when a perspective is selected from the list of cubes in new analysis,
	 * some members/levels are not found and exception is thrown. This method
	 * filter perspectives.
	 * 
	 * @param catalog String
	 * @param olap XMLA_SOAP
	 * @return List
	 */
    private List getCubesWithoutPerspectives(String catalog, XMLA_SOAP olap) throws OlapException {
        List cubesWithoutPerspectives = null;
        if (olap != null) {
            Iterator iterator = olap.discoverCube(catalog).iterator();
            cubesWithoutPerspectives = new ArrayList();
            while (iterator.hasNext()) {
                OlapItem item = (OlapItem) iterator.next();
                if (item.getProperty("BASE_CUBE_NAME") != null && !"".equalsIgnoreCase(item.getProperty("BASE_CUBE_NAME"))) {
                    continue;
                }
                cubesWithoutPerspectives.add(item.getName());
            }
        }
        return cubesWithoutPerspectives;
    }

    public List discoverHier(Datasource datasource, String cube, String dimension) throws OlapException {
        XMLA_SOAP xmla = new XMLA_SOAP(datasource.getServer(), datasource.getUsername(), datasource.getPassword());
        List hierarchies = new LinkedList();
        Iterator hiers = xmla.discoverHier(datasource.getCatalog(), cube, "[" + dimension + "]").iterator();
        while ((hiers != null) && hiers.hasNext()) {
            OlapItem currentHierarchy = (OlapItem) hiers.next();
            String parsedName = currentHierarchy.getName().replaceAll("\\[", "");
            parsedName = parsedName.replaceAll("\\[", "");
            hierarchies.add(parsedName);
        }
        return hierarchies;
    }

    /**
     * Generates an MDX statement from the cube param, and the dimension param. 
     * sample result:
     * SELECT {[Measures].DefaultMember} on columns, {[Category].Children} on rows FROM [Budget]
     * 
     * If your dimension has multiple hierarchies (SSAS 2005), we use first discovered hierarchy:
     * SELECT {[Measures].DefaultMember} on columns, {[Product Class].[Product Category].Children
     *
     * And a variation is if you want to pass in a specific list of measures, the result would look like this:
     * SELECT 
     * {[Measures].[Gross Weight], [Measures].[Net Weight]} on columns, 
     * {[Product Class].[Product Category].Children} on rows 
     * FROM [Foodmart 2005]
     * 
     * @param datasource
     * @param cube
     * @param dimension
     * @param measures if you want the mdx to use a specific list of measures use this param
     * 					otherwise, you will get [Measures].DefaultMember
     * @return
     * @throws OlapException
     */
    public String createDefaultMdx(Datasource datasource, String cube, String dimension, List measures) throws OlapException {
        XmlaConnector xmla = new XmlaConnector();
        String firstHier = "";
        String columnsClause = "";
        String rowsClause = "";
        String fromClause = "";
        if (measures != null) {
            Iterator itMeas = measures.iterator();
            columnsClause = "{";
            while (itMeas.hasNext()) {
                columnsClause += "[Measures].[" + (String) itMeas.next() + "]";
                if (itMeas.hasNext()) {
                    columnsClause += ", ";
                }
            }
            columnsClause += "}";
        } else {
            columnsClause = "{[Measures].DefaultMember}";
        }
        if ("Measures".equalsIgnoreCase(dimension)) {
            throw new OlapException("Cannot autocreate MDX, dimension passed in is the measures");
        }
        List hiers = xmla.discoverHier(datasource, cube, dimension);
        if ((hiers != null) && (hiers.size() > 1)) {
            firstHier = (String) hiers.get(0);
            if (firstHier.startsWith("[") && firstHier.endsWith("]")) {
                firstHier = "." + firstHier;
            } else {
                firstHier = ".[" + firstHier + "]";
            }
        }
        rowsClause = "{[" + dimension + "]" + firstHier + ".Children}";
        fromClause = "[" + cube + "]";
        String mdxQuery = "SELECT " + columnsClause + " on columns, " + rowsClause + " on rows FROM " + fromClause;
        logger.debug("MDX generated as: " + mdxQuery);
        return mdxQuery;
    }

    public List discoverMeasures(Datasource datasource, String cube) throws OlapException {
        List measures = new LinkedList();
        XMLA_SOAP xmlaSoap = new XMLA_SOAP(datasource.getServer(), datasource.getUsername(), datasource.getPassword());
        Iterator olapItems = xmlaSoap.discoverMem(datasource.getCatalog(), cube, "Measures", null, null).iterator();
        while (olapItems.hasNext()) {
            OlapItem item = (OlapItem) olapItems.next();
            measures.add(item.getName());
        }
        return measures;
    }
}
