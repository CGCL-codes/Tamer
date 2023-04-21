package edu.kit.cm.kitcampusguide.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import edu.kit.cm.kitcampusguide.controller.form.UpdatePoiForm;
import edu.kit.cm.kitcampusguide.ws.poi.PoiFacade;
import edu.kit.tm.cm.kitcampusguide.poiservice.DeleteRequestComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.ExecuteFault;
import edu.kit.tm.cm.kitcampusguide.poiservice.ExecuteRequestComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.Names;
import edu.kit.tm.cm.kitcampusguide.poiservice.SelectRequestComplexType;
import edu.kit.tm.cm.kitcampusguide.poiservice.SelectResponseComplexType;

@Controller
public class PoisController {

    private static final Logger log = Logger.getLogger(UpdatePoiForm.class);

    @Autowired
    private PoiFacade poiFacade;

    public PoisController() {
        super();
    }

    public PoisController(PoiFacade poiFacade) {
        super();
        this.poiFacade = poiFacade;
    }

    @RequestMapping("pois.htm")
    public String setUpPoiList(Model model) throws Exception {
        log.debug("Request for pois.");
        SelectRequestComplexType selectRequest = new SelectRequestComplexType();
        Names names = new Names();
        names.getName().add("%");
        selectRequest.setFindByNamesLike(names);
        final ExecuteRequestComplexType executeRequest = new ExecuteRequestComplexType();
        executeRequest.getCreateRequestsOrReadRequestsOrUpdateRequests().add(selectRequest);
        model.addAttribute("pois", ((SelectResponseComplexType) poiFacade.execute(executeRequest).getCreateResponsesOrReadResponsesOrUpdateResponses().get(0)).getPoi());
        return "poi/list";
    }

    @RequestMapping(value = "poi/{uid}/delete.htm")
    public ModelAndView deletePoi(@PathVariable Integer uid) {
        log.debug("Delete request for poi with id " + uid);
        ModelAndView mv = new ModelAndView("redirect:/pois.htm");
        try {
            tryToDeletePoiWithId(uid, mv);
        } catch (ExecuteFault ex) {
            handleErrorWhileDeletingPoi(mv, ex);
        }
        return mv;
    }

    private void handleErrorWhileDeletingPoi(ModelAndView mv, ExecuteFault ex) {
        log.error("Poi could not be deleted", ex);
        mv.addObject("faultMessage", "error.deletingPoi");
    }

    private void tryToDeletePoiWithId(Integer uid, ModelAndView mv) throws ExecuteFault {
        DeleteRequestComplexType deleteRequest = new DeleteRequestComplexType();
        deleteRequest.setId(uid);
        ExecuteRequestComplexType executeRequest = new ExecuteRequestComplexType();
        executeRequest.getCreateRequestsOrReadRequestsOrUpdateRequests().add(deleteRequest);
        poiFacade.execute(executeRequest);
        mv.addObject("successMessage", "success.deletingPoi");
    }

    public void setPoiService(PoiFacade poiFacade) {
        this.poiFacade = poiFacade;
    }
}
