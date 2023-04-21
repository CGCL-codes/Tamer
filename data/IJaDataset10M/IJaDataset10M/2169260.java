package org.codegallery.dynawebappgal.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.codegallery.dynawebappgal.entity.Spitter;
import org.codegallery.dynawebappgal.service.SpitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/spitters/*")
public class SpittersController {

    @Autowired
    private SpitterService spitterService;

    public SpitterService getSpitterService() {
        return spitterService;
    }

    public void setSpitterService(SpitterService spitterService) {
        this.spitterService = spitterService;
    }

    @RequestMapping("create")
    public String create(Map<String, Object> model) {
        Spitter spitter = new Spitter();
        model.put("spitter", spitter);
        return "spitters/create";
    }

    @RequestMapping("createProcess")
    public String createProcess(@ModelAttribute("spitter") Spitter spitter, Map<String, Object> model) {
        spitterService.makePersistent(spitter);
        List<String> list = new ArrayList<String>();
        list.add("Spitter save successful");
        model.put("successMessages", list);
        return "spitters/create";
    }

    @RequestMapping("list")
    public String list(Map<String, Object> model) {
        List<Spitter> spitters = spitterService.findAll();
        model.put("spitters", spitters);
        return "spitters/list";
    }
}
