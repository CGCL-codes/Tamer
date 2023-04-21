package gov.cdc.ncphi.phgrid.services.amds.common.datatypes.impl;

public class SpatialGroupingTypeHandler extends org.apache.ws.jaxme.impl.JMSAXElementParser {

    /** The current state. The following values are valid states:
   *  0 = Before parsing the element
   *  1 = While or after parsing the child element {http://ncphi.phgrid.net/schemas/amds/v1}SpatialArea
   * 
   */
    private int __state;

    public void addAttribute(java.lang.String pURI, java.lang.String pLocalName, java.lang.String pValue) throws org.xml.sax.SAXException {
        if (pURI == null) {
            pURI = "";
        }
        gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingType _1 = (gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingType) result;
        if ("".equals(pURI)) {
            if ("name".equals(pLocalName)) {
                _1.setName((java.lang.String) pValue);
                return;
            } else if ("type".equals(pLocalName)) {
                _1.setType((java.lang.String) pValue);
                return;
            }
        }
        super.addAttribute(pURI, pLocalName, pValue);
    }

    public boolean startElement(java.lang.String pNamespaceURI, java.lang.String pLocalName, java.lang.String pQName, org.xml.sax.Attributes pAttr) throws org.xml.sax.SAXException {
        org.apache.ws.jaxme.impl.JMUnmarshallerHandlerImpl _1 = getHandler();
        switch(__state) {
            case 0:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "SpatialArea".equals(pLocalName)) {
                    __state = 1;
                    _1.addSimpleAtomicState();
                    return true;
                }
                break;
            case 1:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "SpatialArea".equals(pLocalName)) {
                    __state = 1;
                    _1.addSimpleAtomicState();
                    return true;
                }
                break;
            default:
                throw new java.lang.IllegalStateException("Invalid state: " + __state);
        }
        return false;
    }

    public void endElement(java.lang.String pNamespaceURI, java.lang.String pLocalName, java.lang.String pQName, java.lang.Object pResult) throws org.xml.sax.SAXException {
        gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingType _1 = (gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingType) result;
        switch(__state) {
            case 1:
                if ("http://ncphi.phgrid.net/schemas/amds/v1".equals(pNamespaceURI) && "SpatialArea".equals(pLocalName)) {
                    _1.getSpatialArea().add((java.lang.String) pResult);
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
