package org.compas.utils;

import java.util.Vector;
import javax.swing.JTree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class RunService {

    protected JTree projectTree;

    protected Vector<String> sequenceVector;

    protected Vector<String> headerVector;

    protected String serviceName;

    protected boolean error;

    protected boolean done;

    protected String epr;

    public RunService(JTree t, String serviceName) {
        this.serviceName = serviceName;
        this.projectTree = t;
        getSequences();
        error = false;
        done = false;
        getEprFromTree();
    }

    public String getEpr() {
        return epr;
    }

    public void setEpr(String epr) {
        this.epr = epr;
    }

    public void setErrorOccurred(boolean b) {
        setIsDone();
        error = b;
    }

    public boolean getErrorOccurred() {
        return error;
    }

    public void getEprFromTree() {
        Document doc = ((ProjectTreeModel) projectTree.getModel()).getDocument();
        NodeList services = doc.getElementsByTagName("services");
        services = ((Element) services.item(0)).getElementsByTagName("service");
        Element service = doc.getDocumentElement();
        for (int i = 0; i < services.getLength(); i++) {
            service = (Element) services.item(i);
            Element name = (Element) service.getElementsByTagName("name").item(0);
            if (name.getTextContent().equals(serviceName)) {
                Element eprElement = (Element) service.getElementsByTagName("epr").item(0);
                setEpr(eprElement.getTextContent());
                break;
            }
        }
    }

    public void refreshTree(JTree t) {
        projectTree = t;
        getSequences();
    }

    protected void getSequences() {
        Document doc = ((ProjectTreeModel) projectTree.getModel()).getDocument();
        NodeList sequences = doc.getElementsByTagName("sequenceElement");
        sequenceVector = new Vector<String>();
        headerVector = new Vector<String>();
        for (int i = 0; i < sequences.getLength(); i++) {
            Element n = (Element) sequences.item(i);
            String seq = n.getElementsByTagName("sequence").item(0).getTextContent();
            sequenceVector.add(seq.replaceAll("\\s", ""));
            String header = n.getElementsByTagName("header").item(0).getTextContent();
            headerVector.add(header);
        }
    }

    public abstract void run() throws ServiceException;

    public void startService() throws ServiceException {
        error = false;
        done = false;
        run();
    }

    protected String getParameterValueString(String parameterName) {
        ProjectTreeModel model = (ProjectTreeModel) projectTree.getModel();
        Document doc = model.getDocument();
        NodeList services = doc.getElementsByTagName("services");
        services = ((Element) services.item(0)).getElementsByTagName("service");
        Element service = doc.getDocumentElement();
        for (int i = 0; i < services.getLength(); i++) {
            service = (Element) services.item(i);
            Element name = (Element) service.getElementsByTagName("name").item(0);
            if (name.getTextContent().equals(serviceName)) {
                break;
            }
        }
        NodeList parameters = service.getElementsByTagName("parameter");
        for (int i = 0; i < parameters.getLength(); i++) {
            Element parameter = (Element) parameters.item(i);
            Element name = (Element) parameter.getElementsByTagName("name").item(0);
            if (name.getTextContent().equals(parameterName)) {
                Element value = (Element) parameter.getElementsByTagName("value").item(0);
                return value.getTextContent();
            }
        }
        return null;
    }

    public void setIsDone() {
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    protected int getTimeout() {
        Element docEl = ((ProjectTreeModel) projectTree.getModel()).getDocument().getDocumentElement();
        int timeout = Integer.parseInt(docEl.getElementsByTagName("timeout").item(0).getTextContent());
        return timeout * 1000;
    }
}
