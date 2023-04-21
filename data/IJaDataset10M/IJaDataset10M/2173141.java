package org.infoeng.x2006.x05.ictp;

/**
 * A ICTP WS-Resource.
 * <p />
 * NOTE: This file is generated, but is meant to be modified.
 *       It will NOT be overwritten by subsequent runs of Wsdl2Java.
 */
public class ICTPResource extends AbstractICTPResource {

    /**
     * Initializes this resource's state (properties, etc.).
     */
    public void init() {
        super.init();
        org.apache.ws.resource.properties.ResourcePropertySet resourcePropertySet = getResourcePropertySet();
        org.apache.ws.resource.properties.ResourceProperty resourceProperty;
        try {
            resourceProperty = resourcePropertySet.get(ICTPPropertyQNames.TOPICEXPRESSIONDIALECTS);
            org.oasisOpen.docs.wsn.x2004.x06.wsnWSBaseNotification12Draft01.TopicExpressionDialectsDocument prop_topicexpressiondialects = org.oasisOpen.docs.wsn.x2004.x06.wsnWSBaseNotification12Draft01.TopicExpressionDialectsDocument.Factory.newInstance();
            resourceProperty.add(prop_topicexpressiondialects);
            resourceProperty = resourcePropertySet.get(ICTPPropertyQNames.FIXEDTOPICSET);
            org.oasisOpen.docs.wsn.x2004.x06.wsnWSBaseNotification12Draft01.FixedTopicSetDocument prop_fixedtopicset = org.oasisOpen.docs.wsn.x2004.x06.wsnWSBaseNotification12Draft01.FixedTopicSetDocument.Factory.newInstance();
            resourceProperty.add(prop_fixedtopicset);
            resourceProperty = resourcePropertySet.get(ICTPPropertyQNames.TOPIC);
            org.oasisOpen.docs.wsn.x2004.x06.wsnWSBaseNotification12Draft01.TopicDocument prop_topic = org.oasisOpen.docs.wsn.x2004.x06.wsnWSBaseNotification12Draft01.TopicDocument.Factory.newInstance();
            resourceProperty.add(prop_topic);
        } catch (Exception e) {
            throw new javax.xml.rpc.JAXRPCException("There was a problem in initializing your resource properties.  Please check your init() method. Cause: " + e.getLocalizedMessage());
        }
    }
}
