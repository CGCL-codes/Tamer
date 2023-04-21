package org.codegallery.dynawebappgal.web;

import java.util.Map;
import javax.inject.Inject;
import org.codegallery.dynawebappgal.service.HomeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private HomeService homeService;

    public static final int DEFAULT_SPITTLES_PER_PAGE = 25;

    private int spittlesPerPage = DEFAULT_SPITTLES_PER_PAGE;

    public void setSpittlesPerPage(int spittlesPerPage) {
        this.spittlesPerPage = spittlesPerPage;
    }

    public int getSpittlesPerPage() {
        return spittlesPerPage;
    }

    @Inject
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @RequestMapping("/home")
    public String showHomePage(Map<String, Object> model) {
        model.put("spittles", homeService.getRecentSpittles(spittlesPerPage));
        return "home";
    }

    @RequestMapping("/home1")
    @ResponseBody
    public String showHomePage1(Map<String, Object> model) {
        return "Hell World";
    }
}
