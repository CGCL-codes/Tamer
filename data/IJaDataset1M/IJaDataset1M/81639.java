package net.sf.metarbe.example.foparser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sf.metarbe.ActionEvent;
import net.sf.metarbe.RuleContextValidation;
import net.sf.metarbe.RuleContextValidationException;
import net.sf.metarbe.DuplicateNameException;
import net.sf.metarbe.EventGenerator;
import net.sf.metarbe.RuleAction;
import net.sf.metarbe.RuleContext;
import net.sf.metarbe.RuleSemanticModel;
import net.sf.metarbe.RuleRuntime;
import net.sf.metarbe.RuleServiceProvider;
import net.sf.metarbe.RuleSession;
import net.sf.metarbe.RuleSessionEvent;
import net.sf.metarbe.RuleSessionType;
import net.sf.metarbe.SessionFactoryIsNotSetException;
import net.sf.metarbe.StatefulRuleSession;
import net.sf.metarbe.impl.RuleSetManagerImpl;
import net.sf.metarbe.impl.RuleServiceProviderImpl;
import net.sf.metarbe.rsxml.ProcessingSessionEvent;
import net.sf.metarbe.rsxml.TagAttributeEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FoPostProcessor {

    private Set<String> elementsWithId = new HashSet<String>();

    private StatefulRuleSession statefulRuleSession;

    private RuleSetManagerImpl ruleSetManagerImpl;

    public FoPostProcessor() {
        initProcessor();
    }

    protected void initProcessor() {
        RuleServiceProvider rsp = RuleServiceProviderImpl.getRuleServiceProvider();
        RuleRuntime rr = rsp.getRuleRuntime();
        rr.registerRuleSessionFactory(new FoParsingSessionFactory(), RuleSessionType.STATEFUL_SESSION);
        final RuleSemanticModel ruleLayout = new RuleSemanticModel();
        try {
            ruleLayout.addContext(FoLayoutContexts.CTX_FO_PROCESSING);
            ruleLayout.addContext(FoLayoutContexts.CTX_FO_ELEMENT);
            ruleLayout.addContext(FoLayoutContexts.CTX_FO_ELEMENT_ATTRIBUTE);
            ruleLayout.addContext(FoLayoutContexts.CTX_FO_ELEMENT_ID);
            ruleLayout.addState(FoLayoutContexts.STATE_FOUND);
            ruleLayout.addState(FoLayoutContexts.STATE_FAILED);
            ruleLayout.addState(FoLayoutContexts.STATE_NOT_UNIQUE);
        } catch (DuplicateNameException e) {
            e.printStackTrace();
        }
        RuleContextValidation elementIdCachingStrategy = new RuleContextValidation() {

            public void validate(RuleContext aRuleContext, RuleSession aSession) throws RuleContextValidationException {
                if (!(aSession.getValue("tag") instanceof Element)) {
                    throw new RuleContextValidationException("not an element", new ClassCastException());
                }
                Element element = (Element) aSession.getValue("tag");
                String id = element.getAttribute("id");
                if (!elementsWithId.contains(id)) {
                    elementsWithId.add(id);
                } else {
                    throw new RuleContextValidationException("Element [" + element + "] with id=" + id + " has already exists!");
                }
            }
        };
        final EventGenerator processingEventGenerator = new EventGenerator() {

            public RuleSessionEvent createEventInContext(RuleContext aRuleContext, RuleSession aSession) {
                return new ProcessingSessionEvent(aRuleContext, aSession.getValue("source"), aSession.getValue("message").toString());
            }
        };
        final RuleContext idFoundContext = ruleLayout.getRuleContext("id:found");
        final RuleContext idNotUniqContext = ruleLayout.getRuleContext("id:not-unique");
        final RuleContext procFailedContext = ruleLayout.getRuleContext("processing:failed");
        final RuleContext attrFoundContext = ruleLayout.getRuleContext("attr:found");
        EventGenerator tagIdEventGenerator = new EventGenerator() {

            public RuleSessionEvent createEventInContext(RuleContext aRuleContext, RuleSession aSession) {
                StatefulRuleSession statefulRuleSession = (StatefulRuleSession) aSession;
                Element tag = (Element) aSession.getValue("tag");
                try {
                    statefulRuleSession.getContextValidation(aRuleContext).validate(aRuleContext, aSession);
                } catch (RuleContextValidationException e) {
                    if (e.getCause() == null) {
                        return new NotUniqueElementIdEvent(idNotUniqContext, tag);
                    } else {
                        e.printStackTrace();
                        aSession.bindParameter("message", e.getCause().getMessage());
                        return processingEventGenerator.createEventInContext(procFailedContext, aSession);
                    }
                }
                return new ElementIdFoundEvent(idFoundContext, tag);
            }
        };
        EventGenerator tagAttrEventGenerator = new EventGenerator() {

            public RuleSessionEvent createEventInContext(RuleContext aRuleContext, RuleSession aSession) {
                Element tag = (Element) aSession.getValue("tag");
                String attr = (String) aSession.getValue("attr");
                return new TagAttributeEvent(attrFoundContext, tag, attr);
            }
        };
        try {
            statefulRuleSession = (StatefulRuleSession) rr.createRuleSession(ruleLayout, RuleSessionType.STATEFUL_SESSION);
            statefulRuleSession.addContextValidation(idFoundContext, elementIdCachingStrategy);
            statefulRuleSession.addEventGenerator(idFoundContext, tagIdEventGenerator);
            statefulRuleSession.addEventGenerator(attrFoundContext, tagAttrEventGenerator);
            statefulRuleSession.addEventGenerator(procFailedContext, processingEventGenerator);
        } catch (SessionFactoryIsNotSetException e) {
            e.printStackTrace();
            return;
        }
        ruleSetManagerImpl = new RuleSetManagerImpl("Fo postprocessing", statefulRuleSession);
        ruleSetManagerImpl.createRule("Increment tag id", idNotUniqContext, new RuleAction() {

            public void onActionEvent(ActionEvent evt) {
                Element element = (Element) evt.getMatchEvent().getSource();
                String newId = element.getAttribute("id") + elementsWithId.size();
                System.err.println("Tag: <" + element.getTagName() + "> - replacing [" + element.getAttribute("id") + "] with [" + newId + "]");
                element.setAttribute("id", newId);
                elementsWithId.add(newId);
            }
        });
        ruleSetManagerImpl.createRule("Filter out fo:list-item/list-style attribute", attrFoundContext, new FoListItemTagMatch(), new RuleAction() {

            public void onActionEvent(ActionEvent evt) {
                Element element = (Element) evt.getMatchEvent().getSource();
                String attrName = ((TagAttributeEvent) evt.getMatchEvent()).getAttrName();
                if ("list-style".equalsIgnoreCase(attrName)) {
                    System.err.println("Removing list-style attr from node:" + element);
                    element.removeAttribute("list-style");
                }
            }
        });
        ruleSetManagerImpl.createRule("Processing has failed", procFailedContext, new RuleAction() {

            public void onActionEvent(ActionEvent evt) {
                System.err.println(evt.getRuleSessionEvent().getMatchedValue());
            }
        });
    }

    public String processFoDocument(String aFoFilename, boolean rename) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(aFoFilename);
        ruleSetManagerImpl.executeRules(document);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        File outputFile = new File(aFoFilename);
        File fopFile = new File((outputFile.getParent() != null ? outputFile.getParent() : "") + File.separator + outputFile.getName() + ".fop");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new FileWriter(fopFile));
        transformer.transform(source, result);
        if (rename) {
            if (!fopFile.renameTo(outputFile)) throw new IOException("Unable to rename processed file into " + outputFile);
            fopFile.delete();
        }
        return rename ? outputFile.getName() : fopFile.getName();
    }

    public Document processFoDocument(Document document) throws Exception {
        ruleSetManagerImpl.executeRules(document);
        return document;
    }

    public Document processFoDocument(String aFoFilename) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(aFoFilename);
        ruleSetManagerImpl.executeRules(document);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        File outputFile = new File(aFoFilename);
        File fopFile = new File((outputFile.getParent() != null ? outputFile.getParent() : "") + File.separator + outputFile.getName() + ".fop");
        DOMSource source = new DOMSource(document);
        DOMResult domResult = new DOMResult();
        transformer.transform(source, domResult);
        return document;
    }
}
