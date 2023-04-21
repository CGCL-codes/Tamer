package com.dokumentarchiv.plugins.ixosecr;

import ixos.dms.ACL;
import ixos.dms.AccessRight;
import ixos.dms.AccessibilityEnum;
import ixos.dms.AnnotationRenditionEnum;
import ixos.dms.Component;
import ixos.dms.ConstraintException;
import ixos.dms.ContentSource;
import ixos.dms.DataTypeEnum;
import ixos.dms.DefaultDeliveryEnum;
import ixos.dms.Entity;
import ixos.dms.EntityType;
import ixos.dms.Identifier;
import ixos.dms.LocalizedString;
import ixos.dms.PrimitiveDescriptor;
import ixos.dms.PrimitiveType;
import ixos.dms.QueryResult;
import ixos.dms.Record;
import ixos.dms.Rendition;
import ixos.dms.VersioningBehaviourEnum;
import ixos.dms.services.DataDictionary;
import ixos.dms.services.DmsService;
import ixos.dms.services.DmsServiceFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.Plugin;
import com.dokumentarchiv.ServerControlClient;
import com.dokumentarchiv.plugins.IArchive;
import com.dokumentarchiv.plugins.PluginHelper;
import com.dokumentarchiv.search.ListEntry;
import com.dokumentarchiv.search.Search;
import com.dokumentarchiv.search.SearchExpression;
import com.dokumentarchiv.search.SearchFunction;
import de.inovox.AdvancedMimeMessage;

/**
 * Archiving plugin that works with an OpenText PDMS Context Server
 * 
 * @author Carsten Burghardt
 * @version $Id: ECRPlugin.java 619 2008-03-12 22:01:39Z carsten $
 */
public class ECRPlugin extends Plugin implements IArchive {

    private static final long serialVersionUID = 5707811965388250219L;

    private static Log log = LogFactory.getLog(ECRPlugin.class);

    private static String SUPER_ENTITY_TYPE = "ixos.dms:Entity";

    private String from;

    private String to;

    private String subject;

    private String date;

    private DmsService dmsService;

    private EntityType entityType = null;

    private String ftxtid;

    private String contentType;

    private static String CONTENT_TYPE_AUTO = "auto";

    /**
     * Constructor
     * @param manager
     * @param descr
     */
    public ECRPlugin() {
        super();
    }

    /**
     * Constructor for JUnit
     * @param init
     * @throws Exception
     */
    public ECRPlugin(boolean init) throws Exception {
        if (init) {
            doStart();
        }
    }

    /**
     * @see org.java.plugin.Plugin#doStart()
     */
    protected void doStart() throws Exception {
        Configuration config = null;
        try {
            URL configUrl = getManager().getPathResolver().resolvePath(getDescriptor(), CONFIGNAME);
            config = new PropertiesConfiguration(configUrl);
        } catch (ConfigurationException e) {
            log.error("Can not read properties", e);
            getManager().disablePlugin(getDescriptor());
            return;
        }
        String host = config.getString("ecr.host");
        int port = config.getInt("ecr.port", 18080);
        String protocol = config.getString("ecr.protocol", "http");
        String user = config.getString("ecr.user.login");
        String encrypted = config.getString("ecr.user.password");
        String decrypted = ServerControlClient.decrypt(encrypted);
        if (decrypted == null) {
            decrypted = encrypted;
        }
        String domain = config.getString("ecr.user.domain", "_internal");
        String clientele = config.getString("ecr.user.clientele", "defaultclientele");
        log.info("Connecting to ECR server " + host + ":" + port + " as user " + user);
        dmsService = DmsServiceFactory.getInstance().connect(clientele, domain, user, decrypted, protocol, host, port, "");
        log.info("Connected");
        String myNamespace = config.getString("datadictionary.namespace");
        if (!myNamespace.endsWith(":")) {
            myNamespace += ":";
        }
        String recordType = myNamespace + config.getString("datadictionary.recordtype", "Email");
        from = myNamespace + ISearchField.FROM;
        to = myNamespace + ISearchField.TO;
        subject = myNamespace + ISearchField.SUBJECT;
        date = myNamespace + ISearchField.DATE;
        entityType = dmsService.getDataDictionary().getEntityType(recordType);
        if (entityType != null) {
            log.info("Recordtype " + recordType + " found");
        } else {
            log.error("Recordtype " + recordType + " not found, creating it");
            Long ownerId = dmsService.getUserInfo().getUserId();
            LocalizedString shortDisplayName = new LocalizedString("Email");
            String archiveId = config.getString("ecr.archive.id");
            entityType = createEntityType(dmsService.getDataDictionary(), ownerId, recordType, SUPER_ENTITY_TYPE, shortDisplayName, null, archiveId);
        }
        ftxtid = config.getString("ecr.fulltext.id");
        if (ftxtid == null || ftxtid.length() == 0) {
            log.info("Fulltext search disabled");
        } else {
            log.info("Using Fulltext-ID \"" + ftxtid + "\"");
        }
        contentType = config.getString("ecr.content.type", "auto");
    }

    /**
     * Create the record type
     * @param dataDict
     * @param ownerId
     * @param identifier
     * @param superEntityId
     * @param shortDisplayName
     * @param longDisplayName
     * @param archiveId
     * @return
     * @throws Exception
     */
    private EntityType createEntityType(DataDictionary dataDict, Long ownerId, String identifier, String superEntityId, LocalizedString shortDisplayName, LocalizedString longDisplayName, String archiveId) throws Exception {
        EntityType entityType = null;
        log.debug("Adding record type " + identifier);
        entityType = dataDict.getEntityType(superEntityId);
        entityType = dataDict.createEntityType(entityType);
        entityType.setId(identifier);
        if (shortDisplayName != null) {
            entityType.setShortDisplayName(shortDisplayName);
        }
        if (longDisplayName != null) {
            entityType.setLongDisplayName(longDisplayName);
        }
        entityType.setFinal(Boolean.FALSE);
        entityType.setVersionable(Boolean.FALSE);
        entityType.setCanHaveContent(Boolean.TRUE);
        entityType.setAnnotationRendition(AnnotationRenditionEnum.DEFAULT);
        entityType.setBurningDisabled(Boolean.FALSE);
        entityType.setDefaultDelivery(DefaultDeliveryEnum.DEFAULT);
        entityType.setDefaultArchive(archiveId);
        PrimitiveType from = createPrimitiveType(dataDict, this.from, DataTypeEnum.STRING, Boolean.FALSE, new Long(200), new Long(0), Boolean.TRUE, new LocalizedString(ISearchField.FROM), null);
        PrimitiveDescriptor desc = entityType.createPrimitiveDescriptor(from);
        desc.setMandatory(Boolean.TRUE);
        desc.setVersioningBehaviour(VersioningBehaviourEnum.FREEZABLE_PROPERTY);
        desc.setAccessibility(AccessibilityEnum.WRITEABLE);
        PrimitiveType to = createPrimitiveType(dataDict, this.to, DataTypeEnum.STRING, Boolean.TRUE, new Long(200), new Long(0), Boolean.TRUE, new LocalizedString(ISearchField.TO), null);
        desc = entityType.createPrimitiveDescriptor(to);
        desc.setMandatory(Boolean.TRUE);
        desc.setVersioningBehaviour(VersioningBehaviourEnum.FREEZABLE_PROPERTY);
        desc.setAccessibility(AccessibilityEnum.WRITEABLE);
        PrimitiveType subject = createPrimitiveType(dataDict, this.subject, DataTypeEnum.STRING, Boolean.FALSE, new Long(200), new Long(0), Boolean.TRUE, new LocalizedString(ISearchField.SUBJECT), null);
        desc = entityType.createPrimitiveDescriptor(subject);
        desc.setMandatory(Boolean.FALSE);
        desc.setVersioningBehaviour(VersioningBehaviourEnum.FREEZABLE_PROPERTY);
        desc.setAccessibility(AccessibilityEnum.WRITEABLE);
        PrimitiveType date = createPrimitiveType(dataDict, this.date, DataTypeEnum.DATE_TIME, Boolean.FALSE, new Long(0), new Long(0), Boolean.FALSE, new LocalizedString(ISearchField.DATE), null);
        desc = entityType.createPrimitiveDescriptor(date);
        desc.setMandatory(Boolean.FALSE);
        desc.setVersioningBehaviour(VersioningBehaviourEnum.FREEZABLE_PROPERTY);
        desc.setAccessibility(AccessibilityEnum.WRITEABLE);
        ACL defAcl = entityType.getDefaultACL();
        defAcl.addRight(ownerId, AccessRight.ALL);
        dataDict.commit(entityType);
        log.debug("Done adding record type " + identifier);
        return entityType;
    }

    /**
     * Create a property type
     * @param dataDict
     * @param identifier
     * @param dataType
     * @param multivalue
     * @param precision
     * @param scale
     * @param ciSearch
     * @param shortDisplayName
     * @param longDisplayName
     * @return
     * @throws Exception
     */
    private PrimitiveType createPrimitiveType(DataDictionary dataDict, String identifier, DataTypeEnum dataType, Boolean multivalue, Long precision, Long scale, Boolean ciSearch, LocalizedString shortDisplayName, LocalizedString longDisplayName) throws Exception {
        PrimitiveType primitiveType = null;
        try {
            primitiveType = (PrimitiveType) dataDict.getPropertyType(identifier);
        } catch (Exception e) {
            primitiveType = null;
        }
        if (primitiveType == null) {
            log.debug("Adding property type " + identifier);
            primitiveType = dataDict.createPrimitiveType(dataType);
            primitiveType.setId(identifier);
            if (shortDisplayName != null) {
                primitiveType.setShortDisplayName(shortDisplayName);
            }
            if (longDisplayName != null) {
                primitiveType.setLongDisplayName(longDisplayName);
            }
            primitiveType.setMultivalue(multivalue);
            primitiveType.setPrecision(precision);
            primitiveType.setScale(scale);
            primitiveType.setUnit("none");
            primitiveType.setCaseInsensitiveSearchable(ciSearch);
            dataDict.commit(primitiveType);
            log.debug("Done adding property type " + identifier);
        }
        return primitiveType;
    }

    /**
     * @see org.java.plugin.Plugin#doStop()
     */
    protected void doStop() throws Exception {
        dmsService.logoff();
    }

    /**
     * Archive EMail for the specified user
     * @see com.dokumentarchiv.plugins.core.IArchive#archiveEMail(MimeMessage, String)
     */
    public boolean archiveEMail(AdvancedMimeMessage msg) {
        try {
            Entity entity = dmsService.createEntity(entityType);
            Rendition rend = entity.createRendition();
            Component comp = rend.createComponent("Default");
            String contentType = getContentType(msg);
            byte[] bytes = msg.getBytes();
            Long contentLength = new Long(bytes.length);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            comp.setOutputSource(in, contentLength);
            comp.setContentType(contentType);
            entity.setProperty(subject, msg.getSubject());
            entity.setProperty(from, PluginHelper.addressToString(msg.getFrom()));
            entity.setProperty(to, PluginHelper.adressToList(msg.getRecipients(Message.RecipientType.TO)));
            Timestamp timestamp = new Timestamp(msg.getSentDate().getTime());
            entity.setProperty(date, timestamp);
            dmsService.commit(entity);
            log.debug("Created entity " + entity.getId());
        } catch (Exception e) {
            log.error("Failed to create entity", e);
            return false;
        }
        return true;
    }

    /**
     * Get the content type of the Message
     * @param msg
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    private String getContentType(MimeMessage msg) throws MessagingException, IOException {
        if (contentType.equals(CONTENT_TYPE_AUTO)) {
            String contentType = msg.getContentType().trim();
            if (contentType == null || contentType.length() < 1) {
                Object content = msg.getContent();
                if (content instanceof MimeMultipart) {
                    MimeMultipart mmp = (MimeMultipart) content;
                    contentType = mmp.getContentType();
                } else {
                    contentType = "text/plain";
                }
            }
            int pos = contentType.indexOf(";");
            if (pos != -1) {
                contentType = contentType.substring(0, pos);
            }
        } else {
            return contentType;
        }
        return contentType;
    }

    /**
     * @see com.dokumentarchiv.core.IArchive#findDocuments(String)
     */
    public List findDocuments(Search search) {
        String searchString = searchExpressionToString(search);
        String query = "<query version=\"1.0\">" + "  <select>" + "    <property name=\"" + from + "\"/>" + "    <property name=\"" + to + "\"/>" + "    <property name=\"" + subject + "\"/>" + "    <property name=\"" + date + "\"/>" + "  </select>" + searchString + "</query>";
        log.debug(query);
        QueryResult recordList = null;
        try {
            recordList = dmsService.getQueryService().query(query, null, 0);
        } catch (ConstraintException e) {
            log.error("Query error", e);
            return null;
        }
        List records = recordList.getRecords();
        Vector result = new Vector();
        Iterator it = records.iterator();
        while (it.hasNext()) {
            Record record = (Record) it.next();
            ListEntry entry = new ListEntry();
            entry.setId((String) record.getId());
            entry.setDate((Date) record.getProperty(date));
            entry.setFrom((String) record.getProperty(from));
            entry.setTo((String) record.getProperty(to));
            entry.setSubject((String) record.getProperty(subject));
            result.add(entry);
        }
        return result;
    }

    private String getXMLOperator(String operator, boolean opening) {
        String op = operator.toLowerCase();
        String ret;
        if (opening) {
            ret = "<" + op + ">";
        } else {
            ret = "</" + op + ">";
        }
        return ret;
    }

    /**
     * Generate an IXQL query string that represents the search expressions
     * @param search
     * @return
     */
    private String searchExpressionToString(Search search) {
        boolean containsFulltext = false;
        StringBuffer whereResult = new StringBuffer();
        StringBuffer fulltextResult = new StringBuffer();
        whereResult.append("<where>");
        whereResult.append(getXMLOperator(search.getOperator(), true));
        whereResult.append("<isa entitytype=\"" + entityType.getId() + "\"/>");
        String operator = search.getOperator().equals(Search.OPAND) ? "ftand" : "ftor";
        if (isEnableFulltext()) {
            fulltextResult.append("<fulltext><fulltextquery>\n");
            fulltextResult.append("<fulltext-id name=\"" + ftxtid + "\"/>\n");
            fulltextResult.append(getXMLOperator(operator, true));
        }
        Iterator it = search.getChildren().iterator();
        String part = null;
        boolean fulltextSearch;
        while (it.hasNext()) {
            fulltextSearch = false;
            SearchExpression ex = (SearchExpression) it.next();
            String field = ex.getField();
            String value = ex.getValue();
            if (!field.equals(ISearchField.CONTENT) && (ex.getFunction() == SearchFunction.CONTAINS || ex.getFunction() == SearchFunction.CONTAINSNOT)) {
                value = addWildcard(value);
            }
            if (field.equals(ISearchField.FROM)) {
                part = "<property name=\"" + from + "\"/>" + "<literal datatype=\"string\" value=\"" + value + "\"/>";
            } else if (field.equals(ISearchField.TO)) {
                part = "<property name=\"" + to + "\"/>" + "<literal datatype=\"string\" value=\"" + value + "\"/>";
            } else if (field.equals(ISearchField.SUBJECT)) {
                part = "<property name=\"" + subject + "\"/>" + "<literal datatype=\"string\" value=\"" + value + "\"/>";
            } else if (field.equals(ISearchField.DATE)) {
                part = "<property name=\"" + date + "\"/>" + "<literal datatype=\"datetime\" value=\"" + value + "\"/>";
            } else if (field.equals(ISearchField.CONTENT) && isEnableFulltext()) {
                fulltextSearch = true;
                containsFulltext = true;
                part = "<word value=\"" + value + "\" operator=\"";
                if (ex.getFunction() == SearchFunction.CONTAINS) {
                    part += "contains";
                } else if (ex.getFunction() == SearchFunction.EQUALS) {
                    part += "equal";
                } else if (ex.getFunction() == SearchFunction.ISLESS) {
                    part += "less";
                } else if (ex.getFunction() == SearchFunction.ISLESSOREQUAL) {
                    part += "lessequal";
                } else if (ex.getFunction() == SearchFunction.ISGREATER) {
                    part += "greater";
                } else if (ex.getFunction() == SearchFunction.ISGREATEROREQUAL) {
                    part += "greaterequal";
                }
                part += "\"/>";
            }
            if (!fulltextSearch) {
                if (ex.getFunction() == SearchFunction.EQUALS) {
                    part = "<equal>" + part + "</equal>";
                } else if (ex.getFunction() == SearchFunction.EQUALSNOT) {
                    part = "<not><equal>" + part + "</equal></not>";
                } else if (ex.getFunction() == SearchFunction.CONTAINS) {
                    part = "<like>" + part + "</like>";
                } else if (ex.getFunction() == SearchFunction.CONTAINSNOT) {
                    part = "<not><like>" + part + "</like></not>";
                }
            }
            if (fulltextSearch) {
                fulltextResult.append(part);
            } else {
                whereResult.append(part);
            }
        }
        whereResult.append(getXMLOperator(search.getOperator(), false));
        whereResult.append("</where>");
        if (containsFulltext) {
            fulltextResult.append(getXMLOperator(operator, false));
            fulltextResult.append("</fulltextquery></fulltext>");
        }
        return whereResult.toString() + (containsFulltext ? fulltextResult.toString() : "");
    }

    /**
     * Adds wildcard % to the search string
     * @param part
     * @return
     */
    private String addWildcard(String part) {
        part.replaceAll("\\*", "%");
        if (!part.startsWith("%")) {
            part = "%" + part;
        }
        if (!part.endsWith("%")) {
            part = part + "%";
        }
        return part;
    }

    /**
     * @see com.dokumentarchiv.core.IArchive#getDocumentByID(String)
     */
    public InputStream getDocumentByID(String id) {
        log.debug("Get document " + id);
        Identifier ident = dmsService.createIdentifier(id);
        Entity entity = dmsService.getEntity(ident);
        if (entity == null) {
            log.error("Got no entity from DmsService");
            return null;
        }
        try {
            Rendition rend = entity.getDefaultRendition();
            Component comp = rend.getMainComponent();
            log.debug("Length=" + comp.getContentLength() + ",type=" + comp.getContentType());
            ContentSource src = comp.getInputSource();
            return src.getInputStream();
        } catch (Exception e) {
            log.error("Error getting content", e);
        }
        return null;
    }

    public HashMap getSupportedFunctions() {
        HashMap map = new HashMap();
        Vector functions = new Vector();
        functions.add(SearchFunction.getStringForFunction(SearchFunction.CONTAINS));
        functions.add(SearchFunction.getStringForFunction(SearchFunction.CONTAINSNOT));
        functions.add(SearchFunction.getStringForFunction(SearchFunction.EQUALS));
        functions.add(SearchFunction.getStringForFunction(SearchFunction.EQUALSNOT));
        map.put(ISearchField.FROM, functions);
        map.put(ISearchField.TO, functions);
        map.put(ISearchField.SUBJECT, functions);
        map.put(ISearchField.DATE, functions);
        if (isEnableFulltext()) {
            functions.clear();
            functions.add(SearchFunction.getStringForFunction(SearchFunction.CONTAINS));
            functions.add(SearchFunction.getStringForFunction(SearchFunction.EQUALS));
            map.put(ISearchField.CONTENT, functions);
        }
        return map;
    }

    protected boolean isEnableFulltext() {
        return (ftxtid != null && ftxtid.length() > 0);
    }
}
