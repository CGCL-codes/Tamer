package com.c2b2.ipoint.presentation.forms.fieldtypes;

import com.c2b2.ipoint.model.Page;
import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.model.Portlet;
import com.c2b2.ipoint.model.PortletType;
import com.c2b2.ipoint.model.ViewLocationProperty;
import com.c2b2.ipoint.presentation.PresentationException;
import com.c2b2.ipoint.presentation.forms.FieldRenderer;
import com.c2b2.ipoint.processing.PortalRequest;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class PortletsOnPageField extends FieldRenderer {

    public PortletsOnPageField() {
        this("", "", "", null, null);
    }

    /**
   * 
   */
    public PortletsOnPageField(String name, String label, String value, Page page, PortletType type) {
        super(name, label, value, "PortletsOnPage");
        myPage = page;
        myType = type;
    }

    /**
   * 
   */
    public void renderField(PrintWriter out) throws PresentationException {
        try {
            PortalRequest pr = PortalRequest.getCurrentRequest();
            out.print("<select name='" + getName() + "' class='portlettext'");
            renderJavaScriptHandlers(out);
            out.println(">");
            List portletList = myPage.getPortletsOnPage();
            Iterator portlets = portletList.iterator();
            while (portlets.hasNext()) {
                Portlet portlet = ((ViewLocationProperty) portlets.next()).getPortlet();
                if (portlet.isVisibleTo(pr.getCurrentUser())) {
                    String selected = "";
                    if (getValue().equals(Long.toString(portlet.getID()))) {
                        selected = "selected=\"selected\"";
                    }
                    if (myType == null || portlet.getType().getID() == myType.getID()) {
                        out.println("<option class='portlettext' " + selected + " value='" + portlet.getID() + "'>" + portlet.getName() + "</option>");
                    }
                }
            }
            out.println("</select>");
        } catch (PersistentModelException e) {
            throw new PresentationException("Unable to find all the portlets for field " + getName(), e);
        }
    }

    public Object getNativeValue() throws PresentationException {
        if (validate()) {
            return myPortlet;
        }
        throw new PresentationException("Portlet value " + getValue() + " is not a valid Portlet identifier");
    }

    public boolean validate() {
        boolean result = false;
        if (getValue() != null) {
            long portletID = Long.parseLong(getValue());
            try {
                myPortlet = Portlet.getPortlet(portletID);
                result = true;
            } catch (PersistentModelException e) {
                setErrorText("Unable to find portlet with ID " + getValue());
                getLogger().log(Level.INFO, "Unable to find Portlet id " + getValue(), e);
            }
        } else {
            setErrorText("The value must be set");
        }
        return result;
    }

    private Portlet myPortlet = null;

    private Page myPage;

    private PortletType myType;
}
