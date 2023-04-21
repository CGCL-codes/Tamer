package au.org.tpac.portal.oai;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import au.org.tpac.portal.domain.AnzsrcCode;
import au.org.tpac.portal.domain.Category;
import au.org.tpac.portal.domain.Dataset;
import au.org.tpac.portal.domain.DatasetCoverage;
import au.org.tpac.portal.domain.DatasetMeta;
import au.org.tpac.portal.domain.PartyIndividual;
import au.org.tpac.portal.domain.PartyOrganisation;
import au.org.tpac.portal.domain.RelatedDataset;
import au.org.tpac.portal.domain.RelatedInformation;
import au.org.tpac.portal.domain.RelatedParty;
import au.org.tpac.portal.manager.CategoryManager;
import au.org.tpac.portal.manager.CoverageManager;
import au.org.tpac.portal.manager.DatasetManager;
import au.org.tpac.portal.manager.PartyManager;
import java.io.Writer;
import java.io.CharArrayReader;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The Class OaiRequestHandlerImpl.
 * 
 * 
 */
@Component("oaiRequestHandler")
public class OaiRequestHandlerImpl implements HttpRequestHandler, OaiRequestHandler {

    /** The dataset manager. */
    private OaiRecordResolver recordResolver;

    /** The dataset manager. */
    private DatasetManager datasetManager;

    /** The category manager. */
    private CategoryManager categoryManager;

    /** The party manager. */
    private PartyManager partyManager;

    /** The coverage manager. */
    private CoverageManager coverageManager;

    /** The Constant PARAM_VERB. */
    public static final String PARAM_VERB = "verb";

    /** The Constant PARAM_IDENTIFIER. */
    public static final String PARAM_IDENTIFIER = "identifier";

    /** The Constant PARAM_PREFIX. */
    public static final String PARAM_PREFIX = "metadataPrefix";

    /** The Constant PARAM_FROM. */
    public static final String PARAM_FROM = "from";

    /** The Constant PARAM_UNTIL. */
    public static final String PARAM_UNTIL = "until";

    /** The Constant PARAM_SET. */
    public static final String PARAM_SET = "set";

    /** The Constant PARAM_RESUMPTION_TOKEN. */
    public static final String PARAM_RESUMPTION_TOKEN = "resumptionToken";

    /** The Constant UUID_KEY. */
    public static final String UUID_KEY = "uuid";

    /** The Constant OAIPMH_NS. */
    public static final Namespace OAIPMH_NS = Namespace.getNamespace("oai", "http://www.openarchives.org/OAI/2.0/");

    /** The Constant RIFCS_NS. */
    public static final Namespace RIFCS_NS = Namespace.getNamespace("rif", "http://ands.org.au/standards/rif-cs/registryObjects");

    /** The Constant XSI_NS. */
    public static final Namespace XSI_NS = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

    /** The Constant MIME_TYPE. */
    public static final String MIME_TYPE = "application/xml; charset=UTF-8";

    /** The Constant OAISDTF. */
    public static final SimpleDateFormat OAISDTF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /** The Constant OAISDF. */
    public static final SimpleDateFormat OAISDF = new SimpleDateFormat("yyyy-MM-dd");

    /** The Constant ONE_MIN_MS. */
    public static final long ONE_MIN_MS = 1000 * 60;

    /** The Constant MAX_RETURN_ENTRIES. */
    public static final int MAX_RETURN_ENTRIES = 10;

    /** Logger for this class and subclasses. */
    private final Log logger = LogFactory.getLog(getClass());

    /** The properties. */
    private Properties properties = new Properties();

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("OAI-PMH Request handler - request received");
        Element oaiRoot = new Element("OAI-PMH", OAIPMH_NS);
        oaiRoot.setAttribute("schemaLocation", "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd", XSI_NS);
        OAISDTF.setTimeZone(TimeZone.getTimeZone("UTC"));
        oaiRoot.addContent(new Element("responseDate", OAIPMH_NS).setText(OAISDTF.format(new Date())));
        String verb = request.getParameter(PARAM_VERB);
        String identifier = request.getParameter(PARAM_IDENTIFIER);
        String set = request.getParameter(PARAM_SET);
        String from = request.getParameter(PARAM_FROM);
        String until = request.getParameter(PARAM_UNTIL);
        String metadataPrefix = request.getParameter(PARAM_PREFIX);
        String resumptionTokenString = request.getParameter(PARAM_RESUMPTION_TOKEN);
        Integer setInteger = (set == null ? null : new Integer(set));
        ResumptionToken givenCriteria = new ResumptionToken(setInteger, metadataPrefix, from, until);
        try {
            Element oaiRequest = new Element("request", OAIPMH_NS);
            if (null != verb && verb.length() > 0) {
                oaiRequest.setAttribute(PARAM_VERB, verb);
            }
            if (null != identifier && identifier.length() > 0) {
                oaiRequest.setAttribute(PARAM_IDENTIFIER, identifier);
            }
            if (null != metadataPrefix && metadataPrefix.length() > 0) {
                oaiRequest.setAttribute(PARAM_PREFIX, metadataPrefix);
            }
            if (null != from && from.length() > 0) {
                oaiRequest.setAttribute(PARAM_FROM, from);
            }
            if (null != until && until.length() > 0) {
                oaiRequest.setAttribute(PARAM_UNTIL, until);
            }
            if (resumptionTokenString != null && resumptionTokenString.length() > 0) {
                givenCriteria = new ResumptionToken(resumptionTokenString);
                if ((from != null || set != null || metadataPrefix != null) && !givenCriteria.matchesParameters(from, set, metadataPrefix)) {
                    throw new BadResumptionToken("The resumption token (" + resumptionTokenString + ") does not match the request (from:" + from + " set:" + set + " metadataPrefix:" + metadataPrefix + ").");
                }
                oaiRequest.setAttribute(PARAM_RESUMPTION_TOKEN, givenCriteria.getToken());
            }
            oaiRequest.setText(request.getRequestURL().toString());
            oaiRoot.addContent(oaiRequest);
            Element content = null;
            switch(Verb.getValue(verb)) {
                case IDENTIFY:
                    content = performVerbRequestIdentify();
                    break;
                case LIST_METADATA_FORMATS:
                    content = performVerbRequestListMetadataFormats(givenCriteria);
                    break;
                case LIST_SETS:
                    content = performVerbRequestListSets(givenCriteria);
                    break;
                case LIST_IDENTIFIERS:
                    content = performVerbRequestList(givenCriteria, false);
                    break;
                case LIST_RECORDS:
                    content = performVerbRequestList(givenCriteria, true);
                    break;
                case GET_RECORD:
                    content = performVerbRequestGetRecord(givenCriteria, identifier);
                    break;
            }
            if (content == null) {
                throw new NoRecordsMatch("No content for verb " + verb);
            } else {
                oaiRoot.addContent(content);
            }
            validateXML(oaiRoot);
        } catch (BadVerb e) {
            logger.info("BadVerb occured: " + e.getMessage());
            e.printStackTrace();
            oaiRoot.addContent(createErrorElement("badVerb", e.getMessage()));
        } catch (BadArgument e) {
            e.printStackTrace();
            oaiRoot.addContent(createErrorElement("badArgument", e.getMessage()));
        } catch (IdDoesNotExist e) {
            e.printStackTrace();
            oaiRoot.addContent(createErrorElement("idDoesNotExist", e.getMessage()));
        } catch (NoMetaDataFormats e) {
            e.printStackTrace();
            oaiRoot.addContent(createErrorElement("noMetaDataFormats", e.getMessage()));
        } catch (BadResumptionToken e) {
            e.printStackTrace();
            oaiRoot.addContent(createErrorElement("badResumptionToken", e.getMessage()));
        } catch (NoSetHierarchy e) {
            e.printStackTrace();
            oaiRoot.addContent(createErrorElement("noSetHierarchy", e.getMessage()));
        } catch (CannotDisseminateFormat e) {
            e.printStackTrace();
            oaiRoot.addContent(createErrorElement("cannotDisseminateFormat", e.getMessage()));
        } catch (NoRecordsMatch e) {
            e.printStackTrace();
            oaiRoot.addContent(createErrorElement("noRecordsMatch", e.getMessage()));
        }
        response.setContentType(MIME_TYPE);
        Writer writer = response.getWriter();
        Format xmlFormat = Format.getPrettyFormat();
        xmlFormat.setOmitDeclaration(false);
        xmlFormat.setOmitEncoding(false);
        XMLOutputter outputter = new XMLOutputter(xmlFormat);
        outputter.output(oaiRoot, writer);
        writer.flush();
        writer.close();
        logger.info("The oaipmhRepository request has been served");
    }

    /**
	 * Creates the error element.
	 * 
	 * @param code
	 *            the code
	 * @param message
	 *            the message
	 * @return the element
	 */
    private Element createErrorElement(String code, String message) {
        Element errorElement = new Element("error", OAIPMH_NS);
        errorElement.setAttribute("code", code);
        errorElement.addContent(message);
        return errorElement;
    }

    /**
	 * Perform verb request list.
	 * 
	 * @param givenCriteria
	 *            the given criteria
	 * @param listRecords
	 *            the list records
	 * @return the element
	 * @throws BadArgument
	 *             the bad argument
	 * @throws BadResumptionToken
	 *             the bad resumption token
	 * @throws CannotDisseminateFormat
	 *             the cannot disseminate format
	 * @throws NoRecordsMatch
	 *             the no records match
	 * @throws NoSetHierarchy
	 *             the no set hierarchy
	 * @throws IdDoesNotExist
	 *             the id does not exist
	 * @throws MalformedURLException
	 *             the malformed url exception
	 * @throws NoMetaDataFormats
	 *             the no meta data formats
	 */
    private Element performVerbRequestList(ResumptionToken givenCriteria, boolean listRecords) throws BadArgument, BadResumptionToken, CannotDisseminateFormat, NoRecordsMatch, NoSetHierarchy, IdDoesNotExist, MalformedURLException, NoMetaDataFormats {
        String metadataPrefix = givenCriteria.getPrefix();
        if (metadataPrefix == null) {
            throw new NoMetaDataFormats("No metadata prefix");
        }
        int cursor = 0;
        if (givenCriteria.getPos() != null) {
            cursor = givenCriteria.getPos();
            if (cursor < 0) {
                throw new BadResumptionToken("pos value is less than zero");
            }
        }
        logger.info("Criterian prefix:" + metadataPrefix);
        if (metadataPrefix.equals("oai_dc")) {
            return listDc(givenCriteria, listRecords, metadataPrefix, cursor);
        } else if (metadataPrefix.equals("rif")) {
            return listRif(givenCriteria, listRecords, metadataPrefix, cursor);
        } else {
            throw new NoMetaDataFormats("No metadata formats for prefix " + metadataPrefix);
        }
    }

    /**
	 * List rif.
	 * 
	 * @param givenCriteria
	 *            the given criteria
	 * @param listRecords
	 *            the list records
	 * @param metadataPrefix
	 *            the metadata prefix
	 * @param requestCursorIndex
	 *            the cursor
	 * @return the element
	 * @throws BadArgument
	 *             the bad argument
	 * @throws BadResumptionToken
	 *             the bad resumption token
	 * @throws CannotDisseminateFormat
	 *             the cannot disseminate format
	 * @throws NoRecordsMatch
	 *             the no records match
	 * @throws NoSetHierarchy
	 *             the no set hierarchy
	 * @throws IdDoesNotExist
	 *             the id does not exist
	 * @throws MalformedURLException
	 *             the malformed url exception
	 */
    private Element listRif(ResumptionToken givenCriteria, boolean listRecords, String metadataPrefix, int requestCursorIndex) throws BadArgument, BadResumptionToken, CannotDisseminateFormat, NoRecordsMatch, NoSetHierarchy, IdDoesNotExist, MalformedURLException {
        int oaiRecordListSize = 0;
        List<Dataset> matchingDatasets = recordResolver.getMatchingDatasets(givenCriteria);
        Map<Integer, List<Dataset>> categorisedDataset = categoriseDatasets(matchingDatasets);
        List<Integer> sortedCategories = new ArrayList<Integer>(categorisedDataset.keySet());
        Collections.sort(sortedCategories);
        final boolean INCLUDE_PARTIES = true;
        oaiRecordListSize = sortedCategories.size() + (INCLUDE_PARTIES ? 1 : 0);
        Element result = new Element("ListRecords", OAIPMH_NS);
        int responseCursorIndex;
        for (responseCursorIndex = requestCursorIndex; responseCursorIndex < sortedCategories.size() && responseCursorIndex < requestCursorIndex + MAX_RETURN_ENTRIES; responseCursorIndex++) {
            int category = sortedCategories.get(responseCursorIndex);
            List<Dataset> datasets = categorisedDataset.get(category);
            Element metadata = new Element("metadata", OAIPMH_NS);
            Element registryObjects = new Element("registryObjects", RIFCS_NS);
            registryObjects.addNamespaceDeclaration(RIFCS_NS);
            if (responseCursorIndex == requestCursorIndex) {
                registryObjects.setAttribute("schemaLocation", "http://ands.org.au/standards/rif-cs/registryObjects http://services.ands.org.au/documentation/rifcs/1.2.0/schema/registryObjects.xsd", XSI_NS);
            }
            Date dateModified = null;
            for (Dataset dataset : datasets) {
                Element collectionRecord = createMetadataRecord(dataset, listRecords, metadataPrefix);
                Date date = lastUpdated(dataset);
                if (dateModified == null || (date != null && date.after(dateModified))) {
                    dateModified = date;
                }
                registryObjects.addContent(collectionRecord);
            }
            Element record = generateRecordElement(category, dateModified);
            metadata.addContent(registryObjects);
            record.addContent(metadata);
            result.addContent(record);
        }
        if (responseCursorIndex < requestCursorIndex + MAX_RETURN_ENTRIES) {
            Element metadata = new Element("metadata", OAIPMH_NS);
            Element registryObjects = new Element("registryObjects", RIFCS_NS);
            registryObjects.addNamespaceDeclaration(RIFCS_NS);
            if (responseCursorIndex == requestCursorIndex) {
                registryObjects.setAttribute("schemaLocation", "http://ands.org.au/standards/rif-cs/registryObjects http://services.ands.org.au/documentation/rifcs/1.2.0/schema/registryObjects.xsd", XSI_NS);
            }
            for (PartyIndividual party : partyManager.listIndividuals()) {
                Element collectionRecord = createMetadataRecord(party, metadataPrefix);
                registryObjects.addContent(collectionRecord);
            }
            for (PartyOrganisation party : partyManager.listOrganisations()) {
                Element collectionRecord = createMetadataRecord(party, metadataPrefix);
                registryObjects.addContent(collectionRecord);
            }
            Date d = new Date();
            d.setDate(1);
            Element record = generateRecordElement("party", "party", d);
            metadata.addContent(registryObjects);
            record.addContent(metadata);
            result.addContent(record);
            responseCursorIndex++;
        }
        givenCriteria.setPos(responseCursorIndex);
        result = addResumptionTokenIfNecessary(result, responseCursorIndex, oaiRecordListSize, requestCursorIndex, givenCriteria);
        return result;
    }

    /**
	 * Place datasets from list into a map of category to list of matching datasets.
	 * @param datasets List of datasets to place in map
	 * @return Map of category ids (key) to list of datasets (value).
	 */
    private Map<Integer, List<Dataset>> categoriseDatasets(List<Dataset> datasets) {
        HashMap<Integer, List<Dataset>> map = new HashMap<Integer, List<Dataset>>();
        for (Dataset dataset : datasets) {
            int category = dataset.getCategoryId();
            List<Dataset> categoryList;
            if (map.containsKey(category)) {
                categoryList = map.get(category);
            } else {
                categoryList = new Vector<Dataset>();
                map.put(category, categoryList);
            }
            categoryList.add(dataset);
        }
        return map;
    }

    /**
	 * List dc.
	 * 
	 * @param givenCriteria
	 *            the given criteria
	 * @param listRecords
	 *            the list records
	 * @param metadataPrefix
	 *            the metadata prefix
	 * @param requestCursorIndex
	 *            the cursor
	 * @return the element
	 * @throws BadArgument
	 *             the bad argument
	 * @throws BadResumptionToken
	 *             the bad resumption token
	 * @throws CannotDisseminateFormat
	 *             the cannot disseminate format
	 * @throws NoRecordsMatch
	 *             the no records match
	 * @throws NoSetHierarchy
	 *             the no set hierarchy
	 * @throws IdDoesNotExist
	 *             the id does not exist
	 * @throws MalformedURLException
	 *             the malformed url exception
	 */
    private Element listDc(ResumptionToken givenCriteria, boolean listRecords, String metadataPrefix, int requestCursorIndex) throws BadArgument, BadResumptionToken, CannotDisseminateFormat, NoRecordsMatch, NoSetHierarchy, IdDoesNotExist, MalformedURLException {
        List<Dataset> datasets = recordResolver.getMatchingDatasets(givenCriteria);
        Element result = new Element("ListRecords", OAIPMH_NS);
        Dataset dataset = null;
        int responseCursorIndex = 0;
        for (responseCursorIndex = requestCursorIndex; responseCursorIndex < datasets.size() && responseCursorIndex < requestCursorIndex + MAX_RETURN_ENTRIES; responseCursorIndex++) {
            dataset = datasets.get(responseCursorIndex);
            givenCriteria.setPos(responseCursorIndex);
            Element recordOAI = generateRecordElement(dataset);
            if (listRecords) {
                Element metadataOAI = new Element("metadata", OAIPMH_NS);
                metadataOAI.addContent(createMetadataRecord(dataset, listRecords, metadataPrefix));
                recordOAI.addContent(metadataOAI);
            }
            result.addContent(recordOAI);
        }
        result = addResumptionTokenIfNecessary(result, responseCursorIndex, datasets.size(), requestCursorIndex, givenCriteria);
        return result;
    }

    /**
	 * Adds the resumption token if necessary.
	 * 
	 * @param result
	 *            the result
	 * @param responseCursorIndex
	 *            the i
	 * @param datasetsSize
	 *            the datasets size
	 * @param requestCursorIndex
	 *            the cursor
	 * @param givenCriteria
	 *            the given criteria
	 * @param resumptionToken
	 *            the resumption token
	 * @return the element
	 * @throws NoRecordsMatch
	 *             the no records match
	 */
    private Element addResumptionTokenIfNecessary(Element result, int responseCursorIndex, int datasetsSize, int requestCursorIndex, ResumptionToken givenCriteria) throws NoRecordsMatch {
        if (responseCursorIndex < datasetsSize && responseCursorIndex >= requestCursorIndex + MAX_RETURN_ENTRIES) {
            Date now = new Date();
            Element resTokenEl = new Element("resumptionToken", OAIPMH_NS);
            ResumptionToken rt = (givenCriteria);
            resTokenEl.setText(rt.getKey() + ResumptionToken.SEPARATOR + responseCursorIndex);
            resTokenEl.setAttribute("expirationDate", OAISDTF.format(new Date(now.getTime() + (30 * ONE_MIN_MS))));
            resTokenEl.setAttribute("completeListSize", Integer.toString(datasetsSize));
            resTokenEl.setAttribute("cursor", requestCursorIndex + "");
            result.addContent(resTokenEl);
        }
        if (responseCursorIndex == requestCursorIndex) {
            String msg = "Could not find a record with given criteria";
            logger.error(msg);
            throw new NoRecordsMatch(msg);
        }
        return result;
    }

    /**
	 * Generate record element.
	 * 
	 * @param dataset
	 *            the dataset
	 * @return the element
	 */
    private Element generateRecordElement(Dataset dataset) {
        Element recordOAI = new Element("record", OAIPMH_NS);
        Element headerOAI = new Element("header", OAIPMH_NS);
        headerOAI.addContent(new Element("identifier", OAIPMH_NS).setText(generateIdentifierPrefix(properties) + dataset.getId()));
        headerOAI.addContent(new Element("datestamp", OAIPMH_NS).setText("2009-05-01"));
        headerOAI.addContent(new Element("setSpec", OAIPMH_NS).setText(dataset.getCategoryId() + ""));
        recordOAI.addContent(headerOAI);
        return recordOAI;
    }

    /**
	 * Generate record element.
	 * 
	 * @param dataset
	 *            the dataset
	 * @return the element
	 */
    private Element generateRecordElement(int category, Date lastModified) {
        Element recordOAI = new Element("record", OAIPMH_NS);
        Element headerOAI = new Element("header", OAIPMH_NS);
        headerOAI.addContent(new Element("identifier", OAIPMH_NS).setText(generateIdentifierPrefix(properties, category)));
        headerOAI.addContent(new Element("datestamp", OAIPMH_NS).setText(OaiRequestHandlerImpl.OAISDF.format(lastModified)));
        headerOAI.addContent(new Element("setSpec", OAIPMH_NS).setText(category + ""));
        recordOAI.addContent(headerOAI);
        return recordOAI;
    }

    /**
	 * Generate record element.
	 * 
	 * @param dataset
	 *            the dataset
	 * @return the element
	 */
    private Element generateRecordElement(String identifier, String setSpec, Date lastModified) {
        Element recordOAI = new Element("record", OAIPMH_NS);
        Element headerOAI = new Element("header", OAIPMH_NS);
        headerOAI.addContent(new Element("identifier", OAIPMH_NS).setText(identifier));
        headerOAI.addContent(new Element("datestamp", OAIPMH_NS).setText(OaiRequestHandlerImpl.OAISDF.format(lastModified)));
        headerOAI.addContent(new Element("setSpec", OAIPMH_NS).setText(setSpec));
        recordOAI.addContent(headerOAI);
        return recordOAI;
    }

    /**
	 * Perform verb request identify.
	 * 
	 * @return the element
	 * @throws BadArgument
	 *             the bad argument
	 */
    private Element performVerbRequestIdentify() throws BadArgument {
        Element result = new Element("Identify", OAIPMH_NS);
        result.addContent(new Element("repositoryName", OAIPMH_NS).setText("TPAC Digital Library Portal"));
        result.addContent(new Element("baseURL", OAIPMH_NS).setText("http://localhost:8080/digitallibraryportal/oai"));
        result.addContent(new Element("protocolVersion", OAIPMH_NS).setText("2.0"));
        result.addContent(new Element("adminEmail", OAIPMH_NS).setText("info@tpac.org.au"));
        result.addContent(new Element("earliestDatestamp", OAIPMH_NS).setText("1970-01-01"));
        result.addContent(new Element("deletedRecord", OAIPMH_NS).setText("persistent"));
        result.addContent(new Element("granularity", OAIPMH_NS).setText("YYYY-MM-DD"));
        return result;
    }

    /**
	 * Perform verb request list metadata formats.
	 * 
	 * @param givenCriteria
	 *            the given criteria
	 * @return the element
	 * @throws BadArgument
	 *             the bad argument
	 * @throws IdDoesNotExist
	 *             the id does not exist
	 * @throws NoMetaDataFormats
	 *             the no meta data formats
	 */
    private Element performVerbRequestListMetadataFormats(ResumptionToken givenCriteria) throws BadArgument, IdDoesNotExist, NoMetaDataFormats {
        Element result = new Element("ListMetadataFormats", OAIPMH_NS);
        Element oaiRifFormat = new Element("metadataFormat", OAIPMH_NS);
        oaiRifFormat.addContent(new Element("metadataPrefix", OAIPMH_NS).setText(RIFCS_NS.getPrefix()));
        oaiRifFormat.addContent(new Element("schema", OAIPMH_NS).setText("http://services.ands.org.au/home/orca/schemata/registryObjects.xsd"));
        oaiRifFormat.addContent(new Element("metadataNamespace", OAIPMH_NS).setText(RIFCS_NS.getURI()));
        result.addContent(oaiRifFormat);
        return result;
    }

    /**
	 * Perform verb request list sets.
	 * 
	 * @param resumptionToken
	 *            the resumption token
	 * @return the element
	 * @throws BadArgument
	 *             the bad argument
	 * @throws BadResumptionToken
	 *             the bad resumption token
	 * @throws NoSetHierarchy
	 *             the no set hierarchy
	 */
    private Element performVerbRequestListSets(ResumptionToken resumptionToken) throws BadArgument, BadResumptionToken, NoSetHierarchy {
        List<Category> categories = categoryManager.findCategories();
        if (null == categories || categories.size() < 1) {
            String msg = "No Categories found in DLP DB!!!!";
            logger.error(msg);
            throw new NoSetHierarchy(msg);
        }
        Element result = new Element("ListSets", OAIPMH_NS);
        for (Category category : categories) {
            Element setSpecTypeEl = new Element("set", OAIPMH_NS);
            setSpecTypeEl.addContent(new Element("setSpec", OAIPMH_NS).setText(category.getId() + ""));
            setSpecTypeEl.addContent(new Element("setName", OAIPMH_NS).setText(category.getName()));
            result.addContent(setSpecTypeEl);
        }
        return result;
    }

    /**
	 * Perform verb request get record.
	 * 
	 * @param givenCriteria
	 *            the given criteria
	 * @param identifier
	 *            the identifier
	 * @return the element
	 * @throws BadArgument
	 *             the bad argument
	 * @throws CannotDisseminateFormat
	 *             the cannot disseminate format
	 * @throws IdDoesNotExist
	 *             the id does not exist
	 */
    private Element performVerbRequestGetRecord(ResumptionToken givenCriteria, String identifier) throws BadArgument, CannotDisseminateFormat, IdDoesNotExist {
        if (givenCriteria.getPrefix() == null) {
            throw new CannotDisseminateFormat("provided metadataPrefix was null");
        } else if (givenCriteria.getFrom() != null || givenCriteria.getUntil() != null || givenCriteria.getSet() != null) {
            throw new BadArgument("Invalid arguments for GetRecord request.");
        }
        try {
            logger.info("Attempting to create metadata record with id:" + identifier);
            String datasetIdString = identifier.replace(generateIdentifierPrefix(properties), "");
            Dataset dataset = datasetManager.findDataset(Integer.parseInt(datasetIdString));
            Element recordOAI = generateRecordElement(dataset);
            Element metadataElement = new Element("metadata", OAIPMH_NS);
            Element registryObjects = new Element("registryObjects", RIFCS_NS);
            registryObjects.addNamespaceDeclaration(RIFCS_NS);
            registryObjects.setAttribute("schemaLocation", "http://ands.org.au/standards/rif-cs/registryObjects http://services.ands.org.au/documentation/rifcs/1.2.0/schema/registryObjects.xsd", XSI_NS);
            registryObjects.addContent(createMetadataRecord(dataset, true, givenCriteria.getPrefix()));
            metadataElement.addContent(registryObjects);
            recordOAI.addContent(metadataElement);
            return new Element("GetRecord", OAIPMH_NS).addContent(recordOAI);
        } catch (Exception e) {
            throw new CannotDisseminateFormat("Exception getting record " + e.getMessage());
        }
    }

    /**
	 * Creates the metadata record.
	 * 
	 * @param dataset
	 *            the dataset
	 * @param listRecords
	 *            the list records
	 * @param metadataFormat
	 *            the metadata format
	 * @return the element
	 * @throws CannotDisseminateFormat
	 *             the cannot disseminate format
	 * @throws IdDoesNotExist
	 *             the id does not exist
	 * @throws MalformedURLException
	 *             the malformed url exception
	 */
    private Element createMetadataRecord(Dataset dataset, boolean listRecords, String metadataFormat) throws CannotDisseminateFormat, IdDoesNotExist, MalformedURLException {
        Map<String, String> datasetMeta = null;
        if (dataset != null) {
            datasetMeta = datasetManager.findDatasetMeta(dataset.getId());
            if (datasetMeta == null) {
                throw new IdDoesNotExist("No metadata exists for Id=" + dataset.getId());
            }
        } else {
            throw new IdDoesNotExist("A collection does not exist for the supplied identifier.");
        }
        String uuid = datasetMeta.get("uuid");
        if (uuid == null) {
            uuid = setupDatasetUuid(dataset.getId());
        }
        Element metadataRecord = null;
        if (metadataFormat.equals("oai_dc")) {
            DcMetadataRecord dcMetadataRecord = new DcMetadataRecord(datasetMeta, dataset, properties);
            metadataRecord = dcMetadataRecord.getMetdata();
        } else if (metadataFormat.equals("rif")) {
            DatasetCoverage coverage = coverageManager.findDatasetCoverage(dataset.getId());
            metadataRecord = RifMetadataRecord.createCollectionRecord(datasetMeta, dataset, properties, coverage, datasetManager.findRelatedInformation(dataset.getId()), datasetManager.findRelatedDataset(dataset.getId()), datasetManager.findRelatedParties(dataset.getId()), datasetManager.findAnzsrcCodes(dataset.getId()));
        } else if (metadataFormat.equals("iso19115")) {
            Iso19115MetadataRecord iso19115MetadataRecord = new Iso19115MetadataRecord(datasetMeta);
            metadataRecord = iso19115MetadataRecord.getMetdata();
        } else {
            throw new CannotDisseminateFormat("unsupported metadataPrefix: " + metadataFormat);
        }
        return metadataRecord;
    }

    /**
	 * Creates the metadata record.
	 * 
	 * @param party
	 *            the party
	 * @param listRecords
	 *            the list records
	 * @param metadataFormat
	 *            the metadata format
	 * @return the element
	 * @throws CannotDisseminateFormat
	 *             the cannot disseminate format
	 * @throws IdDoesNotExist
	 *             the id does not exist
	 * @throws MalformedURLException
	 *             the malformed url exception
	 */
    private Element createMetadataRecord(PartyIndividual party, String metadataFormat) throws CannotDisseminateFormat, IdDoesNotExist, MalformedURLException {
        if (party == null) {
            throw new IdDoesNotExist("A collection does not exist for the supplied identifier.");
        }
        Element metadataRecord = null;
        if (metadataFormat.equals("oai_dc")) {
        } else if (metadataFormat.equals("rif")) {
            metadataRecord = RifMetadataRecord.createPartyRecord(party, partyManager.findRelatedParty(party.getKey()), properties);
        } else if (metadataFormat.equals("iso19115")) {
        } else {
            throw new CannotDisseminateFormat("unsupported metadataPrefix: " + metadataFormat);
        }
        return metadataRecord;
    }

    /**
	 * Creates the metadata record.
	 * 
	 * @param party
	 *            the party
	 * @param listRecords
	 *            the list records
	 * @param metadataFormat
	 *            the metadata format
	 * @return the element
	 * @throws CannotDisseminateFormat
	 *             the cannot disseminate format
	 * @throws IdDoesNotExist
	 *             the id does not exist
	 * @throws MalformedURLException
	 *             the malformed url exception
	 */
    private Element createMetadataRecord(PartyOrganisation party, String metadataFormat) throws CannotDisseminateFormat, IdDoesNotExist, MalformedURLException {
        if (party == null) {
            throw new IdDoesNotExist("A collection does not exist for the supplied identifier.");
        }
        Element metadataRecord = null;
        if (metadataFormat.equals("oai_dc")) {
        } else if (metadataFormat.equals("rif")) {
            metadataRecord = RifMetadataRecord.createPartyRecord(party, partyManager.findRelatedParty(party.getKey()), properties);
        } else if (metadataFormat.equals("iso19115")) {
        } else {
            throw new CannotDisseminateFormat("unsupported metadataPrefix: " + metadataFormat);
        }
        return metadataRecord;
    }

    /**
	 * Gets the element string.
	 * 
	 * @param data
	 *            the data
	 * @return the element string
	 */
    public static String getElementString(Element data) {
        XMLOutputter outputter = new XMLOutputter(Format.getRawFormat());
        return outputter.outputString(data);
    }

    /**
	 * Validate xml.
	 * 
	 * @param xml
	 *            the xml
	 * @return true, if successful
	 * @throws ServletException
	 */
    private boolean validateXML(Element xml) throws ServletException {
        SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser", true);
        builder.setFeature("http://apache.org/xml/features/validation/schema", true);
        builder.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", "http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd" + " " + "http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlStr = outputter.outputString(xml);
        try {
            builder.build(new CharArrayReader(outputter.outputString(xml).toCharArray()));
            return true;
        } catch (JDOMException e) {
            System.out.println(xmlStr);
            throw new ServletException("JDOMException validating XML: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(xmlStr);
            throw new ServletException("IOException validating XML: " + e.getMessage());
        }
    }

    /**
	 * Setup dataset uuid.
	 * 
	 * @param datasetId
	 *            the dataset id
	 * @return the string
	 */
    private String setupDatasetUuid(int datasetId) {
        Dataset dataset = datasetManager.findDataset(datasetId);
        Map<String, String> datasetMetaMap = datasetManager.findDatasetMeta(dataset.getId());
        String uuid = datasetMetaMap.get(UUID_KEY);
        try {
            if (datasetMetaMap.containsKey(UUID_KEY) == false) {
                DatasetMeta datasetMeta = new DatasetMeta();
                datasetMeta.setDatasetId(dataset.getId());
                datasetMeta.setFieldKey(UUID_KEY);
                uuid = UUID.randomUUID().toString();
                datasetMeta.setFieldValue(uuid);
                List<DatasetMeta> datasetMetas = new LinkedList<DatasetMeta>();
                datasetMetas.add(datasetMeta);
                datasetManager.insertDatasetMetas(datasetMetas);
            } else {
                logger.info("Dataset:" + dataset.getId() + " already has Uuid:" + uuid);
            }
        } catch (Exception e) {
            logger.error("Exception enountered setting UUID for dataset:" + dataset.getId() + " - " + e.getMessage());
            e.printStackTrace();
        }
        return uuid;
    }

    public static String generateIdentifierPrefix(Properties p) {
        return p.getProperty("url").replace("http://", "") + "/ds/";
    }

    /**
	 * 
	 * @param p
	 * @param category
	 * @return
	 */
    public static String generateIdentifierPrefix(Properties p, int category) {
        return p.getProperty("url").replace("http://", "") + "/category/" + category;
    }

    /**
	 * Sets the properties.
	 * 
	 * @param resource
	 *            the new properties
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public final void setProperties(final Resource resource) throws IOException {
        System.out.println("---------3948ru349--------");
        this.properties.load(resource.getInputStream());
    }

    public final void setPropertiesNA(final Properties p) {
        this.properties = p;
    }

    /**
	 * Sets the record resolver.
	 * 
	 * @param cManager
	 *            the new record resolver
	 */
    public final void setOaiRecordResolver(final OaiRecordResolver cManager) {
        this.recordResolver = cManager;
    }

    /**
	 * Sets the category manager.
	 * 
	 * @param cManager
	 *            the new category manager
	 */
    public final void setCategoryManager(final CategoryManager cManager) {
        this.categoryManager = cManager;
    }

    /**
	 * Sets the party manager.
	 * 
	 * @param dManager
	 *            the new party manager
	 */
    public final void setPartyManager(final PartyManager dManager) {
        this.partyManager = dManager;
    }

    /**
	 * Sets the coverage manager.
	 * 
	 * @param cManager
	 *            the new coverage manager
	 */
    public final void setCoverageManager(final CoverageManager cManager) {
        this.coverageManager = cManager;
    }

    /**
	 * Sets the dataset manager.
	 * 
	 * @param dManager
	 *            the new dataset manager
	 */
    public final void setDatasetManager(final DatasetManager dManager) {
        this.datasetManager = dManager;
    }

    private Date lastUpdated(Dataset dataset) {
        Map<String, String> datasetMeta = datasetManager.findDatasetMeta(dataset.getId());
        return lastUpdatedToDate(datasetMeta.get("last_updated"));
    }

    private Date lastUpdatedToDate(String lastUpdated) {
        if (lastUpdated == null) {
            return null;
        }
        try {
            Long lastUpdatedLong = Long.parseLong(lastUpdated);
            Date lastUpdatedDate = new Date(lastUpdatedLong);
            return lastUpdatedDate;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
