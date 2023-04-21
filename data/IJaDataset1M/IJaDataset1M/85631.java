package org.fao.geonet.csw.common.requests;

import java.util.ArrayList;
import java.util.List;
import org.fao.geonet.csw.common.Csw;
import org.fao.geonet.csw.common.ElementSetName;
import org.jdom.Element;

/** Params:
  *  - elementSetName (0..1) Can be 'brief', 'summary', 'full'. Default is 'summary'
  *  - id             (1..n)
  */
public class GetRecordByIdRequest extends CatalogRequest {

    private ElementSetName setName;

    private List<String> alIds = new ArrayList<String>();

    public GetRecordByIdRequest() {
    }

    public void setElementSetName(ElementSetName name) {
        setName = name;
    }

    public void addId(String id) {
        alIds.add(id);
    }

    public void clearIds() {
        alIds.clear();
    }

    protected String getRequestName() {
        return "GetRecordById";
    }

    protected void setupGetParams() {
        addParam("request", getRequestName());
        addParam("service", Csw.SERVICE);
        addParam("version", Csw.CSW_VERSION);
        if (outputSchema != null) {
            addParam("outputSchema", outputSchema);
        }
        if (setName != null) {
            addParam("elementSetName", setName);
        }
        fill("id", alIds);
    }

    protected Element getPostParams() {
        Element params = new Element(getRequestName(), Csw.NAMESPACE_CSW);
        params.setAttribute("service", Csw.SERVICE);
        params.setAttribute("version", Csw.CSW_VERSION);
        if (outputSchema != null) {
            params.setAttribute("outputSchema", outputSchema);
        }
        fill(params, "Id", alIds);
        if (setName != null) {
            Element elem = new Element("ElementSetName", Csw.NAMESPACE_CSW);
            elem.setText(setName.toString());
            params.addContent(elem);
        }
        return params;
    }
}
