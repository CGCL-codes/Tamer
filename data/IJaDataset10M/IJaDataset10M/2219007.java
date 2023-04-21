package uk.ac.osswatch.simal.importData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.ac.osswatch.simal.SimalProperties;
import uk.ac.osswatch.simal.SimalRepositoryFactory;
import uk.ac.osswatch.simal.rdf.SimalException;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;
import uk.ac.osswatch.simal.rdf.io.RDFUtils;

/**
 * Pulls data from Ohloh and converts it to data of use to Simal.
 * 
 */
public class Ohloh {

    private static final Logger logger = LoggerFactory.getLogger(Ohloh.class);

    private static final String OHLOH_BASE_URI = "http://www.ohloh.net";

    /**
   * Adds a project from OhLoh to the repository.
   * 
   * @param projectID
   * @return
   * @throws SimalException
   */
    public void addProjectToSimal(String projectID) throws SimalException, ImportException {
        try {
            DOMResult domProjectResult = getProjectDataAsDOAP(projectID);
            DOMResult domContributorResult = getContributorDataAsFOAF(projectID);
            Element resultRoot = mergeProjectAndContributorData(domProjectResult, domContributorResult).getDocumentElement();
            SimalRepositoryFactory.getProjectService().createProject(resultRoot.getOwnerDocument());
        } catch (TransformerConfigurationException e) {
            throw new SimalRepositoryException("Unable to create XSL Transformer", e);
        } catch (TransformerException e) {
            throw new SimalRepositoryException("Unable to transform Ohloh data", e);
        } catch (MalformedURLException e) {
            throw new SimalRepositoryException("Malformed URL for an Ohloh resource", e);
        } catch (IOException e) {
            throw new SimalRepositoryException("Unable to read osloh to doap XSL", e);
        }
    }

    private DOMResult getProjectDataAsDOAP(String projectID) throws TransformerConfigurationException, IOException, TransformerException, SimalException {
        Document ohlohProject = getProjectData(projectID);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        URL xsl = Ohloh.class.getResource("/stylesheet/ohlohProject-to-doap.xsl");
        Transformer transformer = tFactory.newTransformer(new StreamSource(xsl.openStream()));
        DOMResult domProjectResult = new DOMResult();
        transformer.transform(new DOMSource(ohlohProject), domProjectResult);
        return domProjectResult;
    }

    private DOMResult getContributorDataAsFOAF(String projectID) throws TransformerConfigurationException, IOException, TransformerException, SimalException {
        Document ohlohContributors = getContributorData(projectID);
        NodeList contributors = ohlohContributors.getElementsByTagName("contributor_fact");
        for (int i = 0; i < contributors.getLength(); i++) {
            Element contributor = (Element) contributors.item(i);
            Node accountIDNode = contributor.getElementsByTagName("account_id").item(0);
            if (accountIDNode != null) {
                String accountID = accountIDNode.getTextContent();
                if (accountID.length() > 0) {
                    Element ohlohAccount = getAccountData(accountID).getDocumentElement();
                    contributor.appendChild(ohlohContributors.importNode(ohlohAccount, true));
                }
            }
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        URL xsl = Ohloh.class.getResource("/stylesheet/ohlohContributor-to-foaf.xsl");
        Transformer transformer = tFactory.newTransformer(new StreamSource(xsl.openStream()));
        DOMResult domContributorResult = new DOMResult();
        transformer.transform(new DOMSource(ohlohContributors), domContributorResult);
        return domContributorResult;
    }

    private Document mergeProjectAndContributorData(DOMResult domProjectResult, DOMResult domContributorResult) {
        Document result = (Document) domProjectResult.getNode();
        Element resultRoot = result.getDocumentElement();
        NodeList contributors = ((Document) domContributorResult.getNode()).getDocumentElement().getElementsByTagNameNS(RDFUtils.FOAF_NS, "Person");
        for (int i = 0; i < contributors.getLength(); i++) {
            Node contributor = contributors.item(i);
            Node developer = result.createElementNS("http://usefulinc.com/ns/doap#", "developer");
            developer.appendChild(result.importNode(contributor, true));
            resultRoot.getFirstChild().appendChild(developer);
        }
        return result;
    }

    /**
   * Get the Ohloh project data as an Ohloh XML response document.
   * 
   * @see https://www.ohloh.net/api/getting_started
   * 
   * @param projectID
   * @return
   * @throws SimalException
   */
    protected Document getProjectData(String projectID) throws SimalException {
        String apiKey = getApiKey();
        StringBuilder sb = new StringBuilder(OHLOH_BASE_URI);
        sb.append("/projects/");
        sb.append(projectID);
        sb.append(".xml");
        sb.append("?api_key=");
        sb.append(apiKey);
        String urlString = sb.toString();
        Document doc = getOhlohResponse(urlString);
        return doc;
    }

    protected Document getOhlohResponse(String urlString) throws SimalException {
        URLConnection con;
        try {
            URL url = new URL(urlString);
            con = url.openConnection();
            if (!con.getHeaderField("Status").startsWith("200")) {
                throw new SimalException("Unable to open connection to " + url + " status: " + con.getHeaderField("Status"));
            }
        } catch (MalformedURLException e) {
            throw new SimalException("The Ohloh URL is malformed, how can that happen since it is hard coded?", e);
        } catch (IOException e) {
            throw new SimalException("Unable to open connection to Ohloh", e);
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = null;
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse(con.getInputStream());
        } catch (ParserConfigurationException e) {
            throw new SimalException("Unable to configure XML parser", e);
        } catch (SAXException e) {
            throw new SimalException("Unable to parse XML document", e);
        } catch (IOException e) {
            throw new SimalException("Unable to read XML response", e);
        }
        return doc;
    }

    /**
   * Get the API key for this application. This should be set in
   * local.simal.properties with the parameter name ohloh.api.key
   * 
   * @return
   * @throws ImportException if the ohloh.api.key property has not been set
   * @throws SimalRepositoryException if there is a problem retrieving the ohloh.api.key from properties 
   */
    protected String getApiKey() throws ImportException, SimalRepositoryException {
        String apiKey = SimalProperties.getProperty(SimalProperties.PROPERTY_OHLOH_API_KEY);
        if (apiKey == null || apiKey.length() == 0 || apiKey.contains("has not been set")) {
            throw new ImportException("To import from Ohloh it is necessary to provide an Ohloh API key. Please set ohloh.api.key in local.simal.properties");
        }
        return apiKey;
    }

    /**
   * Get a list of contributors to a given project as an Ohloh XML response
   * document.
   * 
   * @param projectID
   * @return
   * @throws SimalException
   */
    public Document getContributorData(String projectID) throws SimalException {
        String apiKey = getApiKey();
        StringBuilder sb = new StringBuilder(OHLOH_BASE_URI);
        sb.append("/projects/");
        sb.append(projectID);
        sb.append("/contributors.xml");
        sb.append("?api_key=");
        sb.append(apiKey);
        String urlString = sb.toString();
        Document doc = getOhlohResponse(urlString);
        return doc;
    }

    /**
   * Get the Ohloh data for a specific account as an Ohloh XML response
   * document.
   * 
   * @param accountID
   * @return
   * @throws SimalException
   */
    public Document getAccountData(String accountID) throws SimalException {
        String apiKey = getApiKey();
        StringBuilder sb = new StringBuilder(OHLOH_BASE_URI);
        sb.append("/accounts/");
        sb.append(accountID);
        sb.append(".xml");
        sb.append("?api_key=");
        sb.append(apiKey);
        String urlString = sb.toString();
        Document doc = getOhlohResponse(urlString);
        return doc;
    }

    public String getProjectPage(String projectID) {
        StringBuilder source = new StringBuilder(OHLOH_BASE_URI);
        source.append("/projects/");
        source.append(projectID);
        return source.toString();
    }

    /**
   * Import all the projects listed in the supplied file. The file lists
   * a series of ohloh file identifiers, each on a separate line. Lines
   * that start with '#' will be ignored. Blank lines will also be
   * ignored. For example:
   * 
   * <pre>
   * # projects to import from Ohloh
   * simal
   * 
   * # Apache projects
   * apache
   * forrest
   * </pre>
   * 
   * @param file the file of projects to import.
   * @throws IOException if there is a problem reading the file 
   */
    public void importProjects(File file) throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String ohlohProjectID;
            while ((ohlohProjectID = in.readLine()) != null) {
                if (ohlohProjectID.trim().length() != 0 && !ohlohProjectID.startsWith("#")) {
                    try {
                        addProjectToSimal(ohlohProjectID);
                    } catch (ImportException e) {
                        logger.warn("Unable to import project from Ohloh: " + ohlohProjectID, e);
                    } catch (SimalException e) {
                        logger.warn("Unable to add project from Ohloh: " + ohlohProjectID, e);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (in != null) in.close();
        }
    }
}
