package gov.cdc.ncphi.phgrid.services.amds.common.datatypes.impl;

public class SpatialGroupingSetTypeHandler extends org.apache.ws.jaxme.impl.JMSAXElementParser {

    /** The current state. The following values are valid states:
   *  0 = Before parsing the element
   *  1 = While or after parsing the child element {http://ncphi.phgrid.net/schemas/amds/v1}SpatialGrouping
   * 
   */
    private int __state;

    public boolean startElement(java.lang.String pNamespaceURI, java.lang.String pLocalName, java.lang.String pQName, org.xml.sax.Attributes pAttr) throws org.xml.sax.SAXException {
        org.apache.ws.jaxme.impl.JMUnmarshallerHandlerImpl _1 = getHandler();
        switch(__state) {
            case 0:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "SpatialGrouping".equals(pLocalName)) {
                    __state = 1;
                    org.apache.ws.jaxme.JMManager _2 = getHandler().getJMUnmarshaller().getJAXBContextImpl().getManagerS(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingType.class);
                    java.lang.Object _3 = _2.getElementS();
                    org.apache.ws.jaxme.impl.JMSAXElementParser _4 = _2.getHandler();
                    _4.init(_1, _3, "http://ncphi.phgrid.net/schemas/amds/v1", "SpatialGrouping", _1.getLevel());
                    _4.setAttributes(pAttr);
                    _1.addElementParser(_4);
                    return true;
                }
                break;
            case 1:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "SpatialGrouping".equals(pLocalName)) {
                    __state = 1;
                    org.apache.ws.jaxme.JMManager _5 = getHandler().getJMUnmarshaller().getJAXBContextImpl().getManagerS(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingType.class);
                    java.lang.Object _6 = _5.getElementS();
                    org.apache.ws.jaxme.impl.JMSAXElementParser _7 = _5.getHandler();
                    _7.init(_1, _6, "http://ncphi.phgrid.net/schemas/amds/v1", "SpatialGrouping", _1.getLevel());
                    _7.setAttributes(pAttr);
                    _1.addElementParser(_7);
                    return true;
                }
                break;
            default:
                throw new java.lang.IllegalStateException("Invalid state: " + __state);
        }
        return false;
    }

    public void endElement(java.lang.String pNamespaceURI, java.lang.String pLocalName, java.lang.String pQName, java.lang.Object pResult) throws org.xml.sax.SAXException {
        gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingSetType _1 = (gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingSetType) result;
        switch(__state) {
            case 1:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "SpatialGrouping".equals(pLocalName)) {
                    _1.getSpatialGrouping().add(pResult);
                    return;
                }
                break;
            default:
                throw new java.lang.IllegalStateException("Illegal state: " + __state);
        }
    }

    public boolean isFinished() {
        switch(__state) {
            case 1:
                return true;
            default:
                return false;
        }
    }
}
