package fhi.bg.server.actions;

import javax.servlet.http.HttpServletRequest;
import org.jdom.Element;
import fhi.bg.fachklassen.*;
import fhi.bg.logik.*;

public class SL06_UnterstuetzungstoolsBeantragenAction extends Action {

    public String getActionXMLName() {
        return "toolBeantragen";
    }

    public boolean istSpielleiterAction() {
        return true;
    }

    public Element[] execute(Element action) {
        int gruppenNummer = 0;
        try {
            gruppenNummer = new Integer(action.getAttributeValue("gruppenNummer"));
        } catch (NumberFormatException e) {
            Element toolBeantragen = new Element("toolBeantragen");
            toolBeantragen.setAttribute("status", "false");
            return new Element[] { toolBeantragen };
        }
        String tool = action.getAttributeValue("tool");
        try {
            SL06_UnterstuetzungstoolsBeantragen.beantrageUnterstuetzungstool(gruppenNummer, tool);
        } catch (Exception e) {
            System.out.println(e.toString());
            Element toolBeantragen = new Element("toolBeantragen");
            toolBeantragen.setAttribute("status", "false");
            return new Element[] { toolBeantragen };
        }
        Element toolBeantragen = new Element("toolBeantragen");
        toolBeantragen.setAttribute("status", "true");
        return new Element[] { toolBeantragen };
    }

    public Element createActionXML(HttpServletRequest request) throws InvalidParameterException {
        Element element = new Element(getActionXMLName());
        this.addAttributeFromParameter(element, request, "gruppenNummer");
        this.addAttributeFromParameter(element, request, "tool");
        return element;
    }
}
