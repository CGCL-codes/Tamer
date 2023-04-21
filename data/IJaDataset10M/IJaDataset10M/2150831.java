package com.simconomy.twitter.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class HomeController extends AbstractController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        List<Integer> intList = new ArrayList<Integer>();
        Random random = new Random(now.getTime());
        for (int i = 0; i < 10; ++i) intList.add(random.nextInt());
        String greeting = "Morning";
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour == 12) greeting = "Day"; else if (hour > 18) greeting = "Evening"; else if (hour > 12) greeting = "Afternoon";
        ModelAndView mv = new ModelAndView();
        mv.addObject("time", now);
        mv.addObject("randList", intList);
        mv.addObject("greeting", greeting);
        mv.setViewName("home");
        return mv;
    }
}
