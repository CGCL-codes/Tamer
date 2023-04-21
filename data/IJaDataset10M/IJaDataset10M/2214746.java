package org.in4ama.documentengine.mailshots;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.in4ama.documentautomator.exception.DocumentException;
import org.in4ama.documentautomator.util.XmlHelper;
import org.in4ama.documentengine.exception.MailshotException;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/** Manages mailshot scripts */
public class MailshotScriptMgr {

    private Map<String, MailshotScript> scripts = new HashMap<String, MailshotScript>();

    /** Returns the names of available scripts. */
    public Collection<String> getScriptNames() {
        return scripts.keySet();
    }

    /** Returns all available scripts. */
    public Collection<MailshotScript> getScripts() {
        return scripts.values();
    }

    /** Stores the specified script in this manager. */
    public void saveScript(MailshotScript script) throws MailshotException {
        scripts.put(script.getName(), script);
    }

    /** Returns the XML document describing the specified script. */
    public Document getDocumentScript(String scriptName) throws MailshotException {
        MailshotScript script = scripts.get(scriptName);
        Document doc = null;
        try {
            doc = XmlHelper.createDocument();
            Element mailshotElem = doc.createElement("mailshot");
            doc.appendChild(mailshotElem);
            mailshotElem.setAttribute("name", script.getName());
            Element datasetElem = doc.createElement("dataset");
            datasetElem.setAttribute("name", script.getDataSetName());
            mailshotElem.appendChild(datasetElem);
            for (String name : script.getDocuments()) {
                Element documentElem = doc.createElement("document");
                documentElem.setAttribute("name", name);
                mailshotElem.appendChild(documentElem);
            }
            for (String name : script.getPacks()) {
                Element packElem = doc.createElement("pack");
                packElem.setAttribute("name", name);
                mailshotElem.appendChild(packElem);
            }
            if (script.getActions().getEmailAction()) {
                Element actionElem = doc.createElement("action");
                mailshotElem.appendChild(actionElem);
                actionElem.setAttribute("name", "email");
                Element emailToElem = doc.createElement("property");
                actionElem.appendChild(emailToElem);
                emailToElem.setAttribute("name", "emailTo");
                Text emailToText = doc.createTextNode(script.getActions().getEmailTo());
                emailToElem.appendChild(emailToText);
                Element emailCCElem = doc.createElement("property");
                actionElem.appendChild(emailCCElem);
                emailCCElem.setAttribute("name", "emailCC");
                Text emailCCText = doc.createTextNode(script.getActions().getEmailCC());
                emailCCElem.appendChild(emailCCText);
                Element emailBCCElem = doc.createElement("property");
                actionElem.appendChild(emailBCCElem);
                emailBCCElem.setAttribute("name", "emailBCC");
                Text emailBCCText = doc.createTextNode(script.getActions().getEmailBCC());
                emailBCCElem.appendChild(emailBCCText);
            }
            if (script.getActions().getPrintAction()) {
                Element actionElem = doc.createElement("action");
                mailshotElem.appendChild(actionElem);
                actionElem.setAttribute("name", "print");
                Element printDestinationElem = doc.createElement("property");
                actionElem.appendChild(printDestinationElem);
                printDestinationElem.setAttribute("name", "printDestination");
                Text printDestinationText = doc.createTextNode(script.getActions().getPrintDestination());
                printDestinationElem.appendChild(printDestinationText);
            }
            Set<String> paramNames = script.getParameters().keySet();
            for (String paramName : paramNames) {
                Element paramElem = doc.createElement("parameter");
                mailshotElem.appendChild(paramElem);
                paramElem.setAttribute("name", paramName);
                Text paramContent = doc.createTextNode(script.getParameters().get(paramName));
                paramElem.appendChild(paramContent);
            }
            Element listenerElem = doc.createElement("listener");
            mailshotElem.appendChild(listenerElem);
            listenerElem.setAttribute("name", "default");
            {
                Element beforeEmailDocElem = doc.createElement("property");
                listenerElem.appendChild(beforeEmailDocElem);
                beforeEmailDocElem.setAttribute("name", "beforeEmailDoc");
                CDATASection beforeEmailDocContent = doc.createCDATASection(script.getActions().getBeforeEmailDoc());
                beforeEmailDocElem.appendChild(beforeEmailDocContent);
            }
            {
                Element afterEmailDocElem = doc.createElement("property");
                listenerElem.appendChild(afterEmailDocElem);
                afterEmailDocElem.setAttribute("name", "afterEmailDoc");
                CDATASection afterEmailDocContent = doc.createCDATASection(script.getActions().getAfterEmailDoc());
                afterEmailDocElem.appendChild(afterEmailDocContent);
            }
            {
                Element beforePrintDocElem = doc.createElement("property");
                listenerElem.appendChild(beforePrintDocElem);
                beforePrintDocElem.setAttribute("name", "beforePrintDoc");
                CDATASection beforePrintDocContent = doc.createCDATASection(script.getActions().getBeforePrintDoc());
                beforePrintDocElem.appendChild(beforePrintDocContent);
            }
            {
                Element afterPrintDocElem = doc.createElement("property");
                listenerElem.appendChild(afterPrintDocElem);
                afterPrintDocElem.setAttribute("name", "afterPrintDoc");
                CDATASection afterPrintDocContent = doc.createCDATASection(script.getActions().getAfterPrintDoc());
                afterPrintDocElem.appendChild(afterPrintDocContent);
            }
            {
                Element beforeEmailPackElem = doc.createElement("property");
                listenerElem.appendChild(beforeEmailPackElem);
                beforeEmailPackElem.setAttribute("name", "beforeEmailPack");
                CDATASection beforeEmailPackContent = doc.createCDATASection(script.getActions().getBeforeEmailPack());
                beforeEmailPackElem.appendChild(beforeEmailPackContent);
            }
            {
                Element afterEmailPackElem = doc.createElement("property");
                listenerElem.appendChild(afterEmailPackElem);
                afterEmailPackElem.setAttribute("name", "afterEmailPack");
                CDATASection afterEmailPackContent = doc.createCDATASection(script.getActions().getAfterEmailPack());
                afterEmailPackElem.appendChild(afterEmailPackContent);
            }
            {
                Element beforePrintPackElem = doc.createElement("property");
                listenerElem.appendChild(beforePrintPackElem);
                beforePrintPackElem.setAttribute("name", "beforePrintPack");
                CDATASection beforePrintPackContent = doc.createCDATASection(script.getActions().getBeforePrintPack());
                beforePrintPackElem.appendChild(beforePrintPackContent);
            }
            {
                Element afterPrintPackElem = doc.createElement("property");
                listenerElem.appendChild(afterPrintPackElem);
                afterPrintPackElem.setAttribute("name", "afterPrintPack");
                CDATASection afterPrintPackContent = doc.createCDATASection(script.getActions().getAfterPrintPack());
                afterPrintPackElem.appendChild(afterPrintPackContent);
            }
            Element saveFilterElem = doc.createElement("savefilter");
            mailshotElem.appendChild(saveFilterElem);
            listenerElem.setAttribute("name", "default");
            {
                Element saveFilterDocElem = doc.createElement("property");
                saveFilterElem.appendChild(saveFilterDocElem);
                saveFilterDocElem.setAttribute("name", "saveFilterDoc");
                CDATASection saveFilterDocContent = doc.createCDATASection(script.getActions().getSaveFilterDoc());
                saveFilterDocElem.appendChild(saveFilterDocContent);
            }
            {
                Element saveFilterPackElem = doc.createElement("property");
                saveFilterElem.appendChild(saveFilterPackElem);
                saveFilterPackElem.setAttribute("name", "saveFilterPack");
                CDATASection saveFilterPackContent = doc.createCDATASection(script.getActions().getSaveFilterPack());
                saveFilterPackElem.appendChild(saveFilterPackContent);
            }
        } catch (Exception ex) {
            String msg = "Unable to retrieve the mailshot script.";
            throw new MailshotException(msg, ex);
        }
        return doc;
    }

    /** Open the scripts for the specified name. */
    public MailshotScript openScript(String name) {
        return scripts.get(name);
    }

    /** Stores the specified scripts. */
    public void putDocumentScripts(Collection<Document> docs) throws MailshotException, DocumentException {
        for (Document doc : docs) {
            putDocumentScript(doc);
        }
    }

    /** Builds and stores the script from the specified XML document. */
    public void putDocumentScript(Document doc) throws MailshotException, DocumentException {
        MailshotScript mail = null;
        try {
            mail = new MailshotScript();
            Element mailshotElem = (Element) doc.getElementsByTagName("mailshot").item(0);
            String mailshotName = mailshotElem.getAttribute("name");
            mail.setName(mailshotName);
            String datasetName = "";
            NodeList datasetNodeList = mailshotElem.getElementsByTagName("dataset");
            if (datasetNodeList.getLength() == 1) {
                datasetName = ((Element) datasetNodeList.item(0)).getAttribute("name");
            }
            mail.setDataSetName(datasetName);
            NodeList documentNodeList = mailshotElem.getElementsByTagName("document");
            for (int i = 0; i < documentNodeList.getLength(); i++) {
                Element documentElem = (Element) documentNodeList.item(i);
                String documentName = documentElem.getAttribute("name");
                mail.getDocuments().add(documentName);
            }
            NodeList packNodeList = mailshotElem.getElementsByTagName("pack");
            for (int i = 0; i < packNodeList.getLength(); i++) {
                Element packElem = (Element) packNodeList.item(i);
                String packName = packElem.getAttribute("name");
                mail.getPacks().add(packName);
            }
            NodeList actionNodeList = mailshotElem.getElementsByTagName("action");
            for (int i = 0; i < actionNodeList.getLength(); i++) {
                Element actionElem = (Element) actionNodeList.item(i);
                String actionName = actionElem.getAttribute("name");
                if ("email".equals(actionName)) {
                    mail.getActions().setEmailAction(true);
                    NodeList propertyNodeList = actionElem.getElementsByTagName("property");
                    for (int j = 0; j < propertyNodeList.getLength(); j++) {
                        Element propertyElem = (Element) propertyNodeList.item(j);
                        String propertyName = propertyElem.getAttribute("name");
                        String propertyValue = null;
                        if (propertyElem.getChildNodes().getLength() == 1) {
                            propertyValue = propertyElem.getChildNodes().item(0).getNodeValue();
                        }
                        if ("emailTo".equals(propertyName)) {
                            mail.getActions().setEmailTo(propertyValue);
                        } else if ("emailCC".equals(propertyName)) {
                            mail.getActions().setEmailCC(propertyValue);
                        } else if ("emailBCC".equals(propertyName)) {
                            mail.getActions().setEmailBCC(propertyValue);
                        }
                    }
                } else if ("print".equals(actionName)) {
                    mail.getActions().setPrintAction(true);
                    NodeList propertyNodeList = actionElem.getElementsByTagName("property");
                    for (int j = 0; j < propertyNodeList.getLength(); j++) {
                        Element propertyElem = (Element) propertyNodeList.item(j);
                        String propertyName = propertyElem.getAttribute("name");
                        String propertyValue = null;
                        if (propertyElem.getChildNodes().getLength() == 1) {
                            propertyValue = propertyElem.getChildNodes().item(0).getNodeValue();
                        }
                        if ("printDestination".equals(propertyName)) {
                            mail.getActions().setPrintDestination(propertyValue);
                        }
                    }
                }
            }
            NodeList parametersNodeList = mailshotElem.getElementsByTagName("parameter");
            for (int i = 0; i < parametersNodeList.getLength(); i++) {
                Element parameterElem = (Element) parametersNodeList.item(i);
                String parameterName = parameterElem.getAttribute("name");
                String parameterValue = parameterElem.getChildNodes().item(0).getNodeValue();
                mail.getParameters().put(parameterName, parameterValue);
            }
            NodeList listenerNodeList = mailshotElem.getElementsByTagName("listener");
            if (listenerNodeList.getLength() > 0) {
                Element listenerElem = (Element) listenerNodeList.item(0);
                NodeList propertyNodeList = listenerElem.getElementsByTagName("property");
                for (int i = 0; i < propertyNodeList.getLength(); i++) {
                    Element propertyElem = (Element) propertyNodeList.item(i);
                    String propertyName = propertyElem.getAttribute("name");
                    String propertyValue = null;
                    if (propertyElem.getChildNodes().getLength() == 1) {
                        propertyValue = propertyElem.getChildNodes().item(0).getNodeValue();
                    }
                    if ("beforeEmailDoc".equals(propertyName)) {
                        mail.getActions().setBeforeEmailDoc(propertyValue);
                    } else if ("afterEmailDoc".equals(propertyName)) {
                        mail.getActions().setAfterEmailDoc(propertyValue);
                    } else if ("beforePrintDoc".equals(propertyName)) {
                        mail.getActions().setBeforePrintDoc(propertyValue);
                    } else if ("afterPrintDoc".equals(propertyName)) {
                        mail.getActions().setAfterPrintDoc(propertyValue);
                    } else if ("beforeEmailPack".equals(propertyName)) {
                        mail.getActions().setBeforeEmailPack(propertyValue);
                    } else if ("afterEmailPack".equals(propertyName)) {
                        mail.getActions().setAfterEmailPack(propertyValue);
                    } else if ("beforePrintPack".equals(propertyName)) {
                        mail.getActions().setBeforePrintPack(propertyValue);
                    } else if ("afterEmailPack".equals(propertyName)) {
                        mail.getActions().setAfterEmailPack(propertyValue);
                    }
                }
            }
            NodeList saveFilterNodeList = mailshotElem.getElementsByTagName("savefilter");
            if (listenerNodeList.getLength() > 0) {
                Element filterElem = (Element) saveFilterNodeList.item(0);
                NodeList propertyNodeList = filterElem.getElementsByTagName("property");
                for (int i = 0; i < propertyNodeList.getLength(); i++) {
                    Element propertyElem = (Element) propertyNodeList.item(i);
                    String propertyName = propertyElem.getAttribute("name");
                    String propertyValue = null;
                    if (propertyElem.getChildNodes().getLength() == 1) {
                        propertyValue = propertyElem.getChildNodes().item(0).getNodeValue();
                    }
                    if ("saveFilterDoc".equals(propertyName)) {
                        mail.getActions().setSaveFilterDoc(propertyValue);
                    } else if ("saveFilterPack".equals(propertyName)) {
                        mail.getActions().setSaveFilterPack(propertyValue);
                    }
                }
            }
            scripts.put(mail.getName(), mail);
        } catch (Exception ex) {
            String msg = "Unable to store a mailshot script.";
            throw new MailshotException(msg, ex);
        }
    }

    /** Removes the specified script. */
    public void deleteScript(String scriptName) {
        scripts.remove(scriptName);
    }
}
