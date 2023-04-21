package com.kosongkosong.controller;

import com.kosongkosong.model.Story;
import com.kosongkosong.model.id.StoryId;
import com.kosongkosong.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ifnu
 */
@Controller
public class HeadLineController {

    @Autowired
    DataService dataService;

    @RequestMapping(value = "/headline", method = RequestMethod.GET)
    public ModelAndView headline() {
        Story s = dataService.getHeadLine();
        ModelAndView view = new ModelAndView();
        view.addObject("story", s);
        return view;
    }

    @RequestMapping(value = "/headline/id", method = RequestMethod.GET)
    public ModelAndView headlineId() {
        StoryId s = dataService.getHeadLineId();
        ModelAndView view = new ModelAndView();
        view.addObject("story", s);
        return view;
    }
}
