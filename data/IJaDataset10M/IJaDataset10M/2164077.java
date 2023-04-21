package org.gbif.portal.service.provider.impl;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.gbif.portal.dao.occurrence.OccurrenceRecordDAO;
import org.gbif.portal.model.occurrence.OccurrenceRecord;
import org.gbif.portal.model.resources.PropertyStoreNamespace;
import org.gbif.portal.model.resources.ResourceAccessPoint;
import org.gbif.portal.service.ServiceException;
import org.gbif.portal.service.provider.DataProviderServices;
import org.gbif.portal.service.provider.EndpointUnreachableException;
import org.gbif.portal.service.provider.InsufficientDataInIndexException;
import org.gbif.portal.util.mhf.criteria.CollectionCriteria;
import org.gbif.portal.util.mhf.criteria.Criteria;
import org.gbif.portal.util.mhf.criteria.TripletCriteria;
import org.gbif.portal.util.mhf.message.Message;
import org.gbif.portal.util.mhf.message.MessageFactory;
import org.gbif.portal.util.mhf.message.MessageUtils;
import org.gbif.portal.util.propertystore.MisconfiguredPropertyException;
import org.gbif.portal.util.propertystore.PropertyNotFoundException;
import org.gbif.portal.util.propertystore.PropertyStore;
import org.gbif.portal.util.request.RequestUtils;
import org.gbif.portal.util.request.ResponseReader;
import org.gbif.portal.util.request.TemplateUtils;
import org.gbif.portal.util.text.DateFormatter;

/**
 * An implementation using the request utils
 * 
 * @author trobertson
 */
public class DataProviderServicesImpl implements DataProviderServices {

    /**
	 * Logger
	 */
    protected static Log logger = LogFactory.getLog(DataProviderServicesImpl.class);

    /**
	 * The utilities
	 */
    protected RequestUtils requestUtils;

    protected MessageUtils messageUtils;

    protected PropertyStore propertyStore;

    protected TemplateUtils templateUtils;

    /**
	 * DAOs
	 */
    protected OccurrenceRecordDAO occurrenceRecordDAO;

    /**
	 * @see org.gbif.portal.service.request.DataProviderServices#getOccurrence(java.lang.String)
	 */
    public String getOccurrence(String gbifOccurrenceKey) throws EndpointUnreachableException, ServiceException, InsufficientDataInIndexException {
        if (logger.isDebugEnabled()) logger.debug("Extracting IDs for occurrence record: " + gbifOccurrenceKey);
        try {
            OccurrenceRecord occurrenceRecord = occurrenceRecordDAO.getOccurrenceRecordFor(Long.valueOf(gbifOccurrenceKey));
            ResourceAccessPoint rap = occurrenceRecord.getRawOccurrenceRecord().getResourceAccessPoint();
            final List<String> namespaces = new LinkedList<String>();
            for (PropertyStoreNamespace psn : rap.getPropertyStoreNamespaces()) {
                namespaces.add(psn.getNamespace());
            }
            String request = getOccurrenceRecordRequest(occurrenceRecord);
            logger.info(request);
            logger.info(rap.getUrl());
            Message response = (Message) requestUtils.executeGetRequest(rap.getUrl(), request, new ResponseReader() {

                @SuppressWarnings("unchecked")
                public Object read(InputStream is) throws Exception {
                    MessageFactory factory = (MessageFactory) propertyStore.getProperty(namespaces, "MESSAGE.FACTORY", MessageFactory.class);
                    return factory.build(is);
                }
            });
            logger.info(response.getLoggableData());
            return response.getLoggableData();
        } catch (PropertyNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (MisconfiguredPropertyException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
	 * Create a occurrence record request for this occurrence record.
	 * 
	 * @param gbifOccurrenceKey
	 * @return
	 * @throws ServiceException
	 */
    public String getOccurrenceRecordRequest(String gbifOccurrenceKey) throws ServiceException {
        OccurrenceRecord occurrenceRecord = occurrenceRecordDAO.getOccurrenceRecordFor(Long.valueOf(gbifOccurrenceKey));
        if (occurrenceRecord != null) {
            return getOccurrenceRecordRequest(occurrenceRecord);
        }
        return null;
    }

    public boolean hasRaw(String gbifOccurrenceKey) throws ServiceException {
        OccurrenceRecord occurrenceRecord = occurrenceRecordDAO.getOccurrenceRecordFor(Long.valueOf(gbifOccurrenceKey));
        ResourceAccessPoint rap = occurrenceRecord.getRawOccurrenceRecord().getResourceAccessPoint();
        for (PropertyStoreNamespace psn : rap.getPropertyStoreNamespaces()) {
            if (("http://rs.tdwg.org/dwc/terms/Occurrence").equals(psn.getNamespace())) return false;
        }
        return true;
    }

    /**
	 * Create a occurrence record request for this occurrence record.
	 * 
	 * @param occurrenceRecord
	 * @return
	 * @throws ServiceException
	 */
    private String getOccurrenceRecordRequest(OccurrenceRecord occurrenceRecord) throws ServiceException {
        ResourceAccessPoint rap = occurrenceRecord.getRawOccurrenceRecord().getResourceAccessPoint();
        String institutionCode = occurrenceRecord.getInstitutionCode().getCode();
        String collectionCode = occurrenceRecord.getCollectionCode().getCode();
        String catalogueNumber = occurrenceRecord.getCatalogueNumber().getCode();
        if (logger.isDebugEnabled()) logger.debug("Institution[" + institutionCode + "], Collection[" + collectionCode + "], Catalog[" + catalogueNumber + "]");
        final List<String> namespaces = new LinkedList<String>();
        for (PropertyStoreNamespace psn : rap.getPropertyStoreNamespaces()) {
            namespaces.add(psn.getNamespace());
        }
        if (logger.isDebugEnabled()) logger.debug("Using property store namespaces: " + namespaces);
        try {
            Criteria criteria = new CollectionCriteria("and").add(new TripletCriteria(StringUtils.trimToEmpty((String) propertyStore.getProperty(namespaces, "REQUEST.INSTITUTION.CODE", String.class)), "equals", institutionCode)).add(new CollectionCriteria("and").add(new TripletCriteria(StringUtils.trimToEmpty((String) propertyStore.getProperty(namespaces, "REQUEST.COLLECTION.CODE", String.class)), "equals", collectionCode)).add(new TripletCriteria(StringUtils.trimToEmpty((String) propertyStore.getProperty(namespaces, "REQUEST.CATALOGUE.NUMBER", String.class)), "equals", catalogueNumber)));
            VelocityContext velocity = new VelocityContext();
            velocity.put("DateFormatter", new DateFormatter());
            velocity.put("hostAddress", InetAddress.getLocalHost().getHostAddress());
            velocity.put("criteria", criteria);
            velocity.put("destination", rap.getUrl());
            String remoteIdentifier = rap.getRemoteIdAtUrl();
            if (StringUtils.isNotEmpty(remoteIdentifier)) {
                velocity.put("resource", remoteIdentifier);
            }
            try {
                String contentNamespace = (String) propertyStore.getProperty(namespaces, "REQUEST.CONTENT.NAMESPACE", String.class);
                if (logger.isDebugEnabled()) {
                    logger.debug("Setting content namespace to: " + contentNamespace);
                }
                velocity.put("contentNamespace", contentNamespace);
            } catch (PropertyNotFoundException e) {
                logger.debug(e.getMessage(), e);
                String contentNamespace = namespaces.get(namespaces.size() - 1);
                if (logger.isDebugEnabled()) {
                    logger.debug("Setting content namespace to: " + contentNamespace);
                }
                velocity.put("contentNamespace", namespaces.get(namespaces.size() - 1));
            }
            String template = (String) propertyStore.getProperty(namespaces, "REQUEST.SEARCH.TEMPLATE", String.class);
            if (logger.isDebugEnabled()) {
                logger.debug("Using template: " + template);
            }
            return templateUtils.getAndMerge(template, velocity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e);
        }
    }

    public String getResourceUrl(String gbifOccurrenceKey) {
        OccurrenceRecord occurrenceRecord = occurrenceRecordDAO.getOccurrenceRecordFor(Long.valueOf(gbifOccurrenceKey));
        if (occurrenceRecord != null && occurrenceRecord.getRawOccurrenceRecord() != null & occurrenceRecord.getRawOccurrenceRecord().getResourceAccessPoint() != null) {
            return occurrenceRecord.getRawOccurrenceRecord().getResourceAccessPoint().getUrl();
        }
        return null;
    }

    /**
	 * @param requestUtils The requestUtils to set.
	 */
    public void setRequestUtils(RequestUtils requestUtils) {
        this.requestUtils = requestUtils;
    }

    /**
	 * @param occurrenceRecordDAO The occurrenceRecordDAO to set.
	 */
    public void setOccurrenceRecordDAO(OccurrenceRecordDAO occurrenceRecordDAO) {
        this.occurrenceRecordDAO = occurrenceRecordDAO;
    }

    /**
	 * @param messageUtils The messageUtils to set.
	 */
    public void setMessageUtils(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    /**
	 * @param propertyStore The propertyStore to set.
	 */
    public void setPropertyStore(PropertyStore propertyStore) {
        this.propertyStore = propertyStore;
    }

    /**
	 * @param templateUtils The templateUtils to set.
	 */
    public void setTemplateUtils(TemplateUtils templateUtils) {
        this.templateUtils = templateUtils;
    }
}
