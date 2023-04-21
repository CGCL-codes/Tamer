package it.eng.bxmodeller.application;

import java.util.Iterator;
import java.util.List;
import it.eng.bxmodeller.EndPoint;
import it.eng.bxmodeller.WebServiceFaultCatch;
import it.eng.bxmodeller.WebServiceOperation;
import org.jdom.Attribute;
import org.jdom.Element;

public class WebService {

    private String inputMsgName = null;

    private String outputMsgName = null;

    private WebServiceOperation webServiceOperation = null;

    private WebServiceFaultCatch webServiceFaultCatch = null;

    public String getInputMsgName() {
        return inputMsgName;
    }

    public void setInputMsgName(String inputMsgName) {
        this.inputMsgName = inputMsgName;
    }

    public String getOutputMsgName() {
        return outputMsgName;
    }

    public void setOutputMsgName(String outputMsgName) {
        this.outputMsgName = outputMsgName;
    }

    public WebServiceFaultCatch getWebServiceFaultCatch() {
        return webServiceFaultCatch;
    }

    public void setWebServiceFaultCatch(WebServiceFaultCatch webServiceFaultCatch) {
        this.webServiceFaultCatch = webServiceFaultCatch;
    }

    public WebServiceOperation getWebServiceOperation() {
        return webServiceOperation;
    }

    public void setWebServiceOperation(WebServiceOperation webServiceOperation) {
        this.webServiceOperation = webServiceOperation;
    }

    public void generateClass(Element elementoAppoggio) {
        if (elementoAppoggio.getAttributeValue("InputMsgName") != null) {
            this.setInputMsgName(elementoAppoggio.getAttributeValue("InputMsgName"));
        }
        if (elementoAppoggio.getAttributeValue("OutputMsgName") != null) {
            this.setOutputMsgName(elementoAppoggio.getAttributeValue("OutputMsgName"));
        }
        List elencoFigli = elementoAppoggio.getChildren();
        Iterator iteratore = elencoFigli.iterator();
        Element elementoAppoggioFigli = null;
        String nomeTagAppoggio = "";
        while (iteratore.hasNext()) {
            elementoAppoggioFigli = (Element) iteratore.next();
            nomeTagAppoggio = elementoAppoggioFigli.getName();
            if (nomeTagAppoggio.equals("WebServiceOperation")) {
                WebServiceOperation tmp = new WebServiceOperation();
                tmp.generateClass(elementoAppoggioFigli);
                this.setWebServiceOperation(tmp);
            }
            if (nomeTagAppoggio.equals("WebServiceFaultCatch")) {
                WebServiceFaultCatch tmp = new WebServiceFaultCatch();
                tmp.generateClass(elementoAppoggioFigli);
                this.setWebServiceFaultCatch(tmp);
            }
        }
    }

    public Element generateXPDL() {
        Element item = new Element("WebService");
        if (inputMsgName != null) {
            Attribute x_inputMsgName = new Attribute("InputMsgName", inputMsgName);
            item.setAttribute(x_inputMsgName);
        }
        if (outputMsgName != null) {
            Attribute x_outputMsgName = new Attribute("OutputMsgName", outputMsgName);
            item.setAttribute(x_outputMsgName);
        }
        if (webServiceOperation != null) item.addContent(webServiceOperation.generateXPDL());
        if (webServiceFaultCatch != null) item.addContent(webServiceFaultCatch.generateXPDL());
        return item;
    }
}
