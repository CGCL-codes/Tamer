package org.travelfusion.xmlclient.ri.handler.misc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.stream.XMLStreamConstants;
import org.travelfusion.xmlclient.handler.HandlesRequestsFor;
import org.travelfusion.xmlclient.impl.handler.AbstractXmlToolRequestStAXResponseHandler;
import org.travelfusion.xmlclient.ri.xobject.misc.XFindAlternativeFlightsRequest;
import org.travelfusion.xmlclient.ri.xobject.misc.XGetSupplierRoutesConstants;
import org.travelfusion.xmlclient.ri.xobject.misc.XGetSupplierRoutesDetails;
import org.travelfusion.xmlclient.ri.xobject.misc.XGetSupplierRoutesItem;
import org.travelfusion.xmlclient.ri.xobject.misc.XGetSupplierRoutesRequest;
import org.travelfusion.xmlclient.ri.xobject.misc.XGetSupplierRoutesResponse;

@HandlesRequestsFor(XGetSupplierRoutesRequest.class)
public class GetSupplierRoutesHandler extends AbstractXmlToolRequestStAXResponseHandler<XGetSupplierRoutesRequest, XGetSupplierRoutesResponse> {

    @Override
    protected void fillTemplate() throws Exception {
        template.gotoChild();
        template.gotoChild(XGetSupplierRoutesConstants.TEMPLATE_SUPPLIER).addText(request.getSupplier());
    }

    @Override
    public XGetSupplierRoutesResponse handleResponse() throws Exception {
        StringBuilder builder = new StringBuilder(32);
        List<XGetSupplierRoutesItem> itemList = new ArrayList<XGetSupplierRoutesItem>();
        String currentName = null;
        XGetSupplierRoutesItem item = null;
        XGetSupplierRoutesDetails origin = null;
        XGetSupplierRoutesDetails destination = null;
        done: for (int event; ((event = responseReader.next()) != XMLStreamConstants.END_DOCUMENT); ) {
            switch(event) {
                case XMLStreamConstants.START_ELEMENT:
                    builder.setLength(0);
                    currentName = responseReader.getName().getLocalPart();
                    if (XGetSupplierRoutesConstants.RESPONSE_ROUTE.equals(currentName)) {
                        item = new XGetSupplierRoutesItem();
                    } else if (XGetSupplierRoutesConstants.RESPONSE_FROM.equals(currentName)) {
                        origin = new XGetSupplierRoutesDetails();
                    } else if (XGetSupplierRoutesConstants.RESPONSE_TO.equals(currentName)) {
                        destination = new XGetSupplierRoutesDetails();
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (item != null) {
                        builder.append(responseReader.getTextCharacters(), responseReader.getTextStart(), responseReader.getTextLength());
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (item != null) {
                        currentName = responseReader.getName().getLocalPart();
                        if (item != null) {
                            if (XGetSupplierRoutesConstants.RESPONSE_TYPE.equals(currentName)) {
                                if (origin != null && destination == null) {
                                    origin.setType(builder.toString().trim());
                                } else if (destination != null) {
                                    destination.setType(builder.toString().trim());
                                }
                            } else if (XGetSupplierRoutesConstants.RESPONSE_DESCRIPTOR.equals(currentName)) {
                                if (origin != null && destination == null) {
                                    origin.setDescriptor(builder.toString().trim());
                                } else if (destination != null) {
                                    destination.setDescriptor(builder.toString().trim());
                                }
                            } else if (XGetSupplierRoutesConstants.RESPONSE_ROUTE.equals(currentName)) {
                                item.setOrigin(origin);
                                item.setDestination(destination);
                                itemList.add(item);
                                item = null;
                                origin = null;
                                destination = null;
                            }
                            break;
                        }
                    } else {
                        currentName = responseReader.getName().getLocalPart();
                        if (XGetSupplierRoutesConstants.RESPONSE_ROUTE_LIST.equals(currentName)) {
                            break done;
                        }
                    }
                    currentName = null;
                    break;
            }
        }
        XGetSupplierRoutesResponse response = new XGetSupplierRoutesResponse();
        response.setItemList(itemList);
        return response;
    }
}
