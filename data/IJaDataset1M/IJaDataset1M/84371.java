package net.sf.bookright.web;

import java.util.ArrayList;
import net.sf.bookright.entity.Venue;
import net.sf.bookright.entity.VenueConfig;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RooWebScaffold(path = "venueconfigs", formBackingObject = VenueConfig.class)
@RequestMapping("/venueconfigs")
@Controller
public class VenueConfigController {

    @RequestMapping(params = { "form", "venue" }, method = RequestMethod.GET)
    public String createFormGivenVenue(Model uiModel, @RequestParam(value = "venue") String venueId) {
        Venue venue = Venue.findVenue(new Long(venueId));
        VenueConfig venueConfig = new VenueConfig();
        venueConfig.setVenue(venue);
        uiModel.addAttribute("venueConfig", venueConfig);
        ArrayList<Venue> venueList = new ArrayList<Venue>();
        venueList.add(venue);
        uiModel.addAttribute("venues", venueList);
        ArrayList<String[]> dependencies = new ArrayList<String[]>();
        if (Venue.countVenues() == 0) {
            dependencies.add(new String[] { "venue", "venues" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "venueconfigs/create";
    }
}
