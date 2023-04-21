package gov.cdc.ncphi.phgrid.services.amds.common.datatypes.impl;

public class MetaDataRecordTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

    public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
        org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
        return _1;
    }

    public java.lang.String getPreferredPrefix(java.lang.String pURI) {
        return null;
    }

    public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
        gov.cdc.ncphi.phgrid.services.amds.common.datatypes.MetaDataRecordType _1 = (gov.cdc.ncphi.phgrid.services.amds.common.datatypes.MetaDataRecordType) pObject;
        gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialAggregationsSupportedType _2 = _1.getSpatialAggregationsSupported();
        if (_2 != null) {
            org.apache.ws.jaxme.impl.JMSAXDriver _3 = pController.getJMMarshaller().getJAXBContextImpl().getManagerS(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialAggregationsSupportedType.class).getDriver();
            pController.marshal(_3, "http://ncphi.phgrid.net/schemas/amds/v1", "SpatialAggregationsSupported", _1.getSpatialAggregationsSupported());
        }
        gov.cdc.ncphi.phgrid.services.amds.common.datatypes.TimeAggregationsSupportedType _4 = _1.getTimeAggregationsSupported();
        if (_4 != null) {
            org.apache.ws.jaxme.impl.JMSAXDriver _5 = pController.getJMMarshaller().getJAXBContextImpl().getManagerS(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.TimeAggregationsSupportedType.class).getDriver();
            pController.marshal(_5, "http://ncphi.phgrid.net/schemas/amds/v1", "TimeAggregationsSupported", _1.getTimeAggregationsSupported());
        }
        gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SyndromesSupportedType _6 = _1.getSyndromesSupported();
        if (_6 != null) {
            org.apache.ws.jaxme.impl.JMSAXDriver _7 = pController.getJMMarshaller().getJAXBContextImpl().getManagerS(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SyndromesSupportedType.class).getDriver();
            pController.marshal(_7, "http://ncphi.phgrid.net/schemas/amds/v1", "SyndromesSupported", _1.getSyndromesSupported());
        }
        gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SyndromeClassifiersSupportedType _8 = _1.getSyndromClassifiersSupported();
        if (_8 != null) {
            org.apache.ws.jaxme.impl.JMSAXDriver _9 = pController.getJMMarshaller().getJAXBContextImpl().getManagerS(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SyndromeClassifiersSupportedType.class).getDriver();
            pController.marshal(_9, "http://ncphi.phgrid.net/schemas/amds/v1", "SyndromClassifiersSupported", _1.getSyndromClassifiersSupported());
        }
    }
}
