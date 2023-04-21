package org.juddi.handler;

import java.util.Vector;
import org.juddi.datatype.RegistryObject;
import org.juddi.datatype.response.TModelInfos;
import org.juddi.datatype.response.TModelList;
import org.juddi.registry.Registry;
import org.juddi.util.XMLUtils;
import org.w3c.dom.Element;

/**
 * "Knows about the creation and populating of TModelList objects.
 * Returns TModelList."
 *
 * @author Steve Viens (sviens@users.sourceforge.net)
 */
public class TModelListHandler extends AbstractHandler {

    public static final String TAG_NAME = "tModelList";

    private HandlerMaker maker = null;

    protected TModelListHandler(HandlerMaker maker) {
        this.maker = maker;
    }

    public RegistryObject unmarshal(Element element) {
        TModelList obj = new TModelList();
        Vector nodeList = null;
        AbstractHandler handler = null;
        obj.setGeneric(element.getAttribute("generic"));
        obj.setOperator(element.getAttribute("operator"));
        String truncValue = element.getAttribute("truncated");
        if (truncValue != null) obj.setTruncated(truncValue.equalsIgnoreCase("true"));
        nodeList = XMLUtils.getChildElementsByTagName(element, TModelInfosHandler.TAG_NAME);
        if (nodeList.size() > 0) {
            handler = maker.lookup(TModelInfosHandler.TAG_NAME);
            obj.setTModelInfos((TModelInfos) handler.unmarshal((Element) nodeList.elementAt(0)));
        }
        return obj;
    }

    public void marshal(RegistryObject object, Element parent) {
        TModelList list = (TModelList) object;
        Element element = parent.getOwnerDocument().createElement(TAG_NAME);
        AbstractHandler handler = null;
        String generic = list.getGeneric();
        if (generic != null) {
            element.setAttribute("generic", generic);
            if (generic.equals(Registry.UDDI_V1_GENERIC)) element.setAttribute("xmlns", Registry.UDDI_V1_NAMESPACE); else if (generic.equals(Registry.UDDI_V2_GENERIC)) element.setAttribute("xmlns", Registry.UDDI_V2_NAMESPACE); else if (generic.equals(Registry.UDDI_V3_GENERIC)) element.setAttribute("xmlns", Registry.UDDI_V3_NAMESPACE);
        }
        String operator = list.getOperator();
        if (operator != null) element.setAttribute("operator", operator);
        boolean truncated = list.isTruncated();
        if (truncated) element.setAttribute("truncated", "true");
        TModelInfos infos = list.getTModelInfos();
        if (infos != null) {
            handler = maker.lookup(TModelInfosHandler.TAG_NAME);
            handler.marshal(infos, element);
        }
        parent.appendChild(element);
    }

    /***************************************************************************/
    public static void main(String args[]) throws Exception {
    }
}
