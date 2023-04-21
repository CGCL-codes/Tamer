package eu.vph.predict.vre.in_silico.client.spring.portlet.controller.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.EventMapping;
import uk.ac.ox.sddag.predict.aureus.portlet.event.LinkCall;
import uk.ac.ox.sddag.predict.aureus.portlet.event.PublicationAssociationLookupCall;
import eu.vph.predict.vre.base.client.BaseClientIdentifiers;
import eu.vph.predict.vre.extensions.springmvc.SessionParam;
import eu.vph.predict.vre.in_silico.business.util.InSilicoUtil;
import eu.vph.predict.vre.in_silico.client.InSilicoClientIdentifiers;
import eu.vph.predict.vre.in_silico.client.InSilicoClientIdentifiers.SIMULATION_MODE;
import eu.vph.predict.vre.in_silico.entity.simulation.AbstractSimulation;
import eu.vph.predict.vre.in_silico.entity.simulation.SimulationAPPreDiCT;
import eu.vph.predict.vre.in_silico.entity.simulation.SimulationChaste;
import eu.vph.predict.vre.in_silico.entity.simulation.SimulationQTPreDiCT;
import eu.vph.predict.vre.in_silico.entity.simulation.SimulationTorsadePreDiCT;

/**
 * Controller handling the requests to view metadata.
 *
 * @author Geoff Williams
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes(value = { InSilicoClientIdentifiers.MODEL_ATTRIBUTE_ACTIVE_PARAMETER_ID, InSilicoClientIdentifiers.MODEL_ATTRIBUTE_SIMULATION, InSilicoClientIdentifiers.MODEL_ATTRIBUTE_METADATA_LINK_VALUES, InSilicoClientIdentifiers.MODEL_ATTRIBUTE_METHOD_IDS })
public class ControllerSimulationEvent extends AbstractSimulationController {

    public static final Log log = LogFactory.getLog(ControllerSimulationEvent.class);

    @SuppressWarnings("unused")
    @ActionMapping(params = BaseClientIdentifiers.SPECIAL_PARAM_NAME_ACTION + "=" + InSilicoClientIdentifiers.ACTION_SIMULATION_VIEW_METADATA)
    private void actionEventProcessingFromPage3(final ActionResponse actionResponse, @ModelAttribute(InSilicoClientIdentifiers.MODEL_ATTRIBUTE_SIMULATION) final AbstractSimulation simulation, @ModelAttribute(InSilicoClientIdentifiers.MODEL_ATTRIBUTE_METHOD_IDS) final String[] selectedMethodIds, @RequestParam(required = false, value = InSilicoClientIdentifiers.PARAM_NAME_LINK_VALUE) final String linkValue, @RequestParam(required = false, value = BaseClientIdentifiers.PARAM_NAME_NAVIGATION_TO_PAGE) final String renderPage3) {
        log.debug("~actionEventProcessingFromPage3(..) : Placing lookupcall event on ActionResponse");
        log.debug("~actionEventProcessingFromPage3(..) : renderPage3 [" + renderPage3 + "]");
        log.debug("~actionEventProcessingFromPage3(..) : simulation [" + simulation + "]");
        log.debug("~actionEventProcessingFromPage3(..) : linkValue [" + linkValue + "]");
        log.debug("~actionEventProcessingFromPage3(..) : selectedMethodIds [" + selectedMethodIds + "]");
        final PublicationAssociationLookupCall publicationAssociationLookupCall = new PublicationAssociationLookupCall();
        publicationAssociationLookupCall.setAssociation(linkValue);
        actionResponse.setEvent(PublicationAssociationLookupCall.QNAME, publicationAssociationLookupCall);
        actionResponse.setRenderParameter(BaseClientIdentifiers.SPECIAL_PARAM_NAME_RENDER, renderPage3);
        actionResponse.setRenderParameter(InSilicoClientIdentifiers.PARAM_NAME_SIMULATION_ID, simulation.getId().toString());
        actionResponse.setRenderParameter(InSilicoClientIdentifiers.PARAM_NAME_METHOD_IDS, selectedMethodIds);
    }

    @SuppressWarnings("unused")
    @EventMapping("{http:event.portlet.aureus.predict.sddag.ox.ac.uk/linkcall}LinkCall")
    private void eventLinkCall(final EventRequest eventRequest, final EventResponse eventResponse, final Model model, @ModelAttribute(InSilicoClientIdentifiers.MODEL_ATTRIBUTE_ACTIVE_PARAMETER_ID) final String activeParameterId, @ModelAttribute(InSilicoClientIdentifiers.MODEL_ATTRIBUTE_METADATA_LINK_VALUES) Map<String, Collection<String>> metadataLinkValues, @RequestParam(required = true, value = InSilicoClientIdentifiers.PARAM_NAME_SIMULATION_ID) final Long simulationId, @RequestParam(required = true, value = InSilicoClientIdentifiers.PARAM_NAME_METHOD_IDS) final String[] selectedMethodIds, @SessionParam(required = true, value = InSilicoClientIdentifiers.SESSION_ATTRIBUTE_SIMULATION_MODE) final SIMULATION_MODE simulationMode) {
        log.debug("~eventLinkCall(..) : simulationMode [" + simulationMode + "]");
        log.debug("~eventLinkCall(..) : simulation [" + simulationId + "]");
        log.debug("~eventLinkCall(..) : selectedMethodIds [" + selectedMethodIds + "]");
        log.debug("~eventLinkCall(..) : activeParameterId [" + activeParameterId + "]");
        try {
            if (IS_NEW_MODE(simulationMode)) {
                final Event event = eventRequest.getEvent();
                final LinkCall linkCall = (LinkCall) event.getValue();
                log.debug("~eventLinkCall(..) : UID [" + linkCall.getUID() + "]");
                log.debug("~eventLinkCall(..) : PubId [" + linkCall.getPubId() + "]");
                final AbstractSimulation simulation = getSimulationService().retrieveSimulation(simulationId);
                final String uid = linkCall.getUID();
                if (uid == null) {
                    log.error("~eventLinkCall(..) : Event LinkCall UID was null");
                } else {
                    if (metadataLinkValues == null) {
                        log.error("~eventLinkCall(..) : MetadataLinkValues was null");
                    } else {
                        final String metadataIdentifier = InSilicoUtil.retrieveMetadataIdentifier(simulationId.toString(), activeParameterId.toString());
                        final Collection<String> linkValues = metadataLinkValues.get(metadataIdentifier);
                        if (linkValues == null) {
                            log.debug("~eventLinkCall(..) : Creating new link value [" + uid + "] for identifier [" + metadataIdentifier + "]");
                            final List<String> newLinkValues = new ArrayList<String>();
                            newLinkValues.add(uid);
                            metadataLinkValues.put(metadataIdentifier, newLinkValues);
                        } else {
                            log.debug("~eventLinkCall(..) : Appending new link value [" + uid + "] for identifier [" + metadataIdentifier + "]");
                            linkValues.add(uid);
                        }
                        getSimulationService().assignUserInputMetadataLinks(simulation, metadataLinkValues);
                        getSimulationService().saveSimulation(simulation);
                    }
                }
                final String simulationType = simulation.getSimulationType().getName();
                final String renderSimulationPage = ControllerSimulationEvent.determineRenderPage3(simulationType);
                eventResponse.setRenderParameter(BaseClientIdentifiers.SPECIAL_PARAM_NAME_RENDER, renderSimulationPage);
                eventResponse.setRenderParameter(InSilicoClientIdentifiers.PARAM_NAME_SIMULATION_ID, simulationId.toString());
                eventResponse.setRenderParameter(InSilicoClientIdentifiers.PARAM_NAME_METHOD_IDS, selectedMethodIds);
            }
        } catch (Exception exception) {
            log.error("~eventLinkCall(..) : Exception [" + exception.getMessage() + "]");
            exception.printStackTrace();
            model.addAttribute(BaseClientIdentifiers.MODEL_ATTRIBUTE_MESSAGE_ERROR, new InterfaceMessage(exception, eventRequest.getLocale()));
        }
    }

    /**
   * Determine the page 3 to render given a particular simulation type value.
   * 
   * @param simulationType Simulation type.
   * @return Page 3 name to render.
   */
    public static String determineRenderPage3(final String simulationType) {
        log.debug("~determineRenderPage3(String) : Determine which page to render for [" + simulationType + "]");
        String renderSimulationPage = null;
        if (SimulationAPPreDiCT.FIXED_NAME.equals(simulationType)) {
            renderSimulationPage = InSilicoClientIdentifiers.RENDER_SIMULATION_PAGE_3_APPREDICT;
        } else if (SimulationTorsadePreDiCT.FIXED_NAME.equals(simulationType)) {
            renderSimulationPage = InSilicoClientIdentifiers.RENDER_SIMULATION_PAGE_3_TORSADEPREDICT;
        } else if (SimulationChaste.FIXED_NAME.equals(simulationType)) {
            renderSimulationPage = InSilicoClientIdentifiers.RENDER_SIMULATION_PAGE_3_CHASTE;
        } else if (SimulationQTPreDiCT.FIXED_NAME.equals(simulationType)) {
            renderSimulationPage = InSilicoClientIdentifiers.RENDER_SIMULATION_PAGE_3_QTPREDICT;
        } else {
            final String errorMessage = "Received event for simulation of type [" + simulationType + "]";
            log.error("~determineRenderPage3(String) " + errorMessage);
            throw new UnsupportedOperationException(errorMessage);
        }
        return renderSimulationPage;
    }
}
