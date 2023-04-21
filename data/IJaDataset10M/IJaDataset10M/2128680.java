package myflit.web;

import myflit.Tabs;
import myflit.Vehicles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

@Controller
public class MyFlitController {

    @Autowired
    private Tabs tabs;

    @Autowired
    private Vehicles vehicles;

    @RequestMapping("/home.htm")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @RequestMapping("/map_area.htm")
    public ModelAndView map(@RequestParam("id") String str_vehicleid) {
        String vehicleInfo = vehicles.searchId(str_vehicleid);
        return new ModelAndView("map_area", new ModelMap("vehicleInfo", vehicleInfo));
    }

    @RequestMapping("/tab_area.htm")
    public ModelAndView tab() {
        return new ModelAndView("tab_area", new ModelMap("tabList", tabs.search()));
    }
}
