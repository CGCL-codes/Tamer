package edu.kit.cm.kitcampusguide.controller.form;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import edu.kit.cm.kitcampusguide.model.Group;
import edu.kit.cm.kitcampusguide.service.user.MemberService;
import edu.kit.cm.kitcampusguide.validator.PoiValidator;
import edu.kit.cm.kitcampusguide.ws.poi.PoiFacade;
import edu.kit.tm.cm.kitcampusguide.poiservice.CreateRequestComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.ExecuteFault;
import edu.kit.tm.cm.kitcampusguide.poiservice.ExecuteRequestComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.Poi;

@Controller
@RequestMapping(value = "poi/create.htm")
public class CreatePoiForm {

    private static final Logger log = Logger.getLogger(CreatePoiForm.class);

    @Autowired
    private PoiFacade poiFacade;

    @Autowired
    private MemberService memberService;

    public CreatePoiForm() {
        super();
    }

    public CreatePoiForm(PoiFacade poiFacade, MemberService memberService) {
        super();
        this.poiFacade = poiFacade;
        this.memberService = memberService;
    }

    @RequestMapping(method = RequestMethod.GET)
    protected String setUpForm(Model model) {
        log.debug("Setting up form for creating poi.");
        Poi poi = new Poi();
        model.addAttribute(poi);
        Collection<Group> groups = this.memberService.getGroups();
        model.addAttribute("groups", groups);
        return "poi/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    protected String onSubmit(@ModelAttribute Poi poi, BindingResult result, Model model) {
        log.debug("Create request for poi. Data: " + poi);
        String viewName;
        new PoiValidator().validate(poi, result);
        if (result.hasErrors()) {
            viewName = "poi/form";
        } else {
            try {
                tryToCreatePoi(poi, model);
                viewName = "redirect:/pois.htm";
            } catch (ExecuteFault e) {
                handleCreateException(model, e);
                viewName = "poi/form";
            }
        }
        return viewName;
    }

    private void handleCreateException(Model model, ExecuteFault e) {
        log.error("Error while creating POI.", e);
        model.addAttribute("faultMessage", "error.creatingPoi");
    }

    private void tryToCreatePoi(Poi poi, Model model) throws ExecuteFault {
        CreateRequestComplexType createRequest = new CreateRequestComplexType();
        createRequest.setPoi(poi);
        ExecuteRequestComplexType executeRequest = new ExecuteRequestComplexType();
        executeRequest.getCreateRequestsOrReadRequestsOrUpdateRequests().add(createRequest);
        poiFacade.execute(executeRequest);
        model.addAttribute("successMessage", "success.creatingPoi");
    }
}
