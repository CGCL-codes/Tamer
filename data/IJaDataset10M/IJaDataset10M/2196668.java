package gov.cdc.ncphi.phgrid.services.amds.common.datatypes.impl;

public class TimeSpatialQueryRequestTypeHandler extends org.apache.ws.jaxme.impl.JMSAXElementParser {

    /** The current state. The following values are valid states:
   *  0 = Before parsing the element
   *  1 = While or after parsing the child element {http://ncphi.phgrid.net/schemas/amds/v1}Date
   *  2 = While or after parsing the child element {http://ncphi.phgrid.net/schemas/amds/v1}SyndromeSet
   *  3 = While or after parsing the child element {http://ncphi.phgrid.net/schemas/amds/v1}SpactialGroupingSet
   *  4 = While or after parsing the child element {http://ncphi.phgrid.net/schemas/amds/v1}TimeAggregation
   * 
   */
    private int __state;

    public boolean startElement(java.lang.String pNamespaceURI, java.lang.String pLocalName, java.lang.String pQName, org.xml.sax.Attributes pAttr) throws org.xml.sax.SAXException {
        org.apache.ws.jaxme.impl.JMUnmarshallerHandlerImpl _1 = getHandler();
        switch(__state) {
            case 0:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "Date".equals(pLocalName)) {
                    __state = 1;
                    org.apache.ws.jaxme.JMManager _2 = getHandler().getJMUnmarshaller().getJAXBContextImpl().getManagerS(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.DateType.class);
                    java.lang.Object _3 = _2.getElementS();
                    org.apache.ws.jaxme.impl.JMSAXElementParser _4 = _2.getHandler();
                    _4.init(_1, _3, "http://ncphi.phgrid.net/schemas/amds/v1", "Date", _1.getLevel());
                    _4.setAttributes(pAttr);
                    _1.addElementParser(_4);
                    return true;
                }
                break;
            case 1:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "SyndromeSet".equals(pLocalName)) {
                    __state = 2;
                    org.apache.ws.jaxme.JMManager _5 = getHandler().getJMUnmarshaller().getJAXBContextImpl().getManagerS(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SyndromeSetType.class);
                    java.lang.Object _6 = _5.getElementS();
                    org.apache.ws.jaxme.impl.JMSAXElementParser _7 = _5.getHandler();
                    _7.init(_1, _6, "http://ncphi.phgrid.net/schemas/amds/v1", "SyndromeSet", _1.getLevel());
                    _7.setAttributes(pAttr);
                    _1.addElementParser(_7);
                    return true;
                }
                break;
            case 2:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "SpactialGroupingSet".equals(pLocalName)) {
                    __state = 3;
                    org.apache.ws.jaxme.JMManager _8 = getHandler().getJMUnmarshaller().getJAXBContextImpl().getManagerS(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingSetType.class);
                    java.lang.Object _9 = _8.getElementS();
                    org.apache.ws.jaxme.impl.JMSAXElementParser _10 = _8.getHandler();
                    _10.init(_1, _9, "http://ncphi.phgrid.net/schemas/amds/v1", "SpactialGroupingSet", _1.getLevel());
                    _10.setAttributes(pAttr);
                    _1.addElementParser(_10);
                    return true;
                }
                break;
            case 3:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "TimeAggregation".equals(pLocalName)) {
                    __state = 4;
                    _1.addSimpleAtomicState();
                    return true;
                }
                break;
            case 4:
                break;
            default:
                throw new java.lang.IllegalStateException("Invalid state: " + __state);
        }
        return false;
    }

    public void endElement(java.lang.String pNamespaceURI, java.lang.String pLocalName, java.lang.String pQName, java.lang.Object pResult) throws org.xml.sax.SAXException {
        gov.cdc.ncphi.phgrid.services.amds.common.datatypes.TimeSpatialQueryRequestType _1 = (gov.cdc.ncphi.phgrid.services.amds.common.datatypes.TimeSpatialQueryRequestType) result;
        switch(__state) {
            case 1:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "Date".equals(pLocalName)) {
                    _1.setDate(((gov.cdc.ncphi.phgrid.services.amds.common.datatypes.DateType) pResult));
                    return;
                }
                break;
            case 2:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "SyndromeSet".equals(pLocalName)) {
                    _1.setSyndromeSet(((gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SyndromeSetType) pResult));
                    return;
                }
                break;
            case 3:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "SpactialGroupingSet".equals(pLocalName)) {
                    _1.setSpactialGroupingSet(((gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingSetType) pResult));
                    return;
                }
                break;
            case 4:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "TimeAggregation".equals(pLocalName)) {
                    _1.setTimeAggregation((java.lang.String) pResult);
                    return;
                }
                break;
            default:
                throw new java.lang.IllegalStateException("Illegal state: " + __state);
        }
    }

    public boolean isFinished() {
        switch(__state) {
            case 4:
                return true;
            default:
                return false;
        }
    }
}
