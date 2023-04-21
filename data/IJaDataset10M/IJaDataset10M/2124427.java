package net.taylor.jsf;

import java.io.IOException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class HtmlTableRow extends UIComponentBase {

    public static final String COMPONENT_FAMILY = "net.taylor.jsf.HtmlTableRow";

    public static final String COMPONENT_TYPE = "net.taylor.jsf.HtmlTableRow";

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (!isRendered()) return;
        super.encodeBegin(context);
        ResponseWriter response = context.getResponseWriter();
        response.startElement("tr", this);
        response.writeAttribute("id", getClientId(context), "id");
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        if (!isRendered()) return;
        ResponseWriter response = context.getResponseWriter();
        response.endElement("tr");
        response.flush();
        super.encodeEnd(context);
    }
}
