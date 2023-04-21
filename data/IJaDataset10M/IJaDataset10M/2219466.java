package uk.ac.lkl.migen.system.server.converter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.lkl.common.util.reflect.*;
import uk.ac.lkl.common.util.restlet.*;
import uk.ac.lkl.migen.system.ai.feedback.intervention.*;
import uk.ac.lkl.migen.system.ai.feedback.strategy.*;
import uk.ac.lkl.migen.system.util.StaticNumberObjectUtilities;

public class InterventionXMLConverter extends XMLConverter<Intervention> {

    private StaticInstanceMap<FeedbackStrategyType> feedbackStrategyMap;

    public InterventionXMLConverter() {
        feedbackStrategyMap = StaticInstanceMapManager.getStaticInstanceMap(FeedbackStrategyType.class);
    }

    @Override
    public Intervention convertToObject(Element element, XMLConversionContext context) {
        int interactivityLevelId = Integer.parseInt(element.getAttribute("interactivityLevel"));
        FeedbackInteractivityLevel interactivityLevel = StaticNumberObjectUtilities.get(FeedbackInteractivityLevel.class, interactivityLevelId);
        int interruptionLevelId = Integer.parseInt(element.getAttribute("interruptionLevel"));
        FeedbackInterruptionLevel interruptionLevel = StaticNumberObjectUtilities.get(FeedbackInterruptionLevel.class, interruptionLevelId);
        int messageTypeId = Integer.parseInt(element.getAttribute("messageType"));
        FeedbackMessageType messageType = StaticNumberObjectUtilities.get(FeedbackMessageType.class, messageTypeId);
        int modalityId = Integer.parseInt(element.getAttribute("modality"));
        FeedbackModality modality = StaticNumberObjectUtilities.get(FeedbackModality.class, modalityId);
        int feedbackStrategyTypeId = Integer.parseInt(element.getAttribute("feedbackStrategyType"));
        FeedbackStrategyType feedbackStrategyType = feedbackStrategyMap.getInstance(feedbackStrategyTypeId);
        @SuppressWarnings("unused") Element feedbackStrategyElement = (Element) element.getChildNodes().item(0);
        return new Intervention(feedbackStrategyType, interruptionLevel, interactivityLevel, modality, messageType);
    }

    @Override
    protected void convertToXML(Document document, Element element, Intervention intervention, XMLConversionContext context) {
        FeedbackInteractivityLevel interactivityLevel = intervention.getInteractivity();
        element.setAttribute("interactivityLevel", Integer.toString(interactivityLevel.getId()));
        FeedbackInterruptionLevel interruptionLevel = intervention.getInterruption();
        element.setAttribute("interruptionLevel", Integer.toString(interruptionLevel.getId()));
        FeedbackMessageType messageType = intervention.getMessageType();
        element.setAttribute("messageType", Integer.toString(messageType.getId()));
        FeedbackModality modality = intervention.getModality();
        element.setAttribute("modality", Integer.toString(modality.getId()));
        FeedbackStrategyType feedbackStrategyType = intervention.getFeedbackStrategyType();
        element.setAttribute("feedbackStrategyType", Integer.toString(feedbackStrategyType.getId()));
    }

    @Override
    public GenericClass<Intervention> getEntityClass() {
        return GenericClass.getSimple(Intervention.class);
    }
}
