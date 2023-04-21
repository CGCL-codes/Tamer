package org.jrecruiter.web.actions;

import org.jrecruiter.model.ServerSettings;
import org.jrecruiter.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Retrieves all jobs and returns an XML document. The structure conforms to the layout
 * defined by Indeed.com
 *
 * @author Gunnar Hillert
 * @version $Id:UserService.java 128 2007-07-27 03:55:54Z ghillert $
 */
@Controller
public class IndexController {

    @Autowired
    private JobService jobService;

    @Autowired
    private ServerSettings serverSettings;

    /** serialVersionUID. */
    private static final long serialVersionUID = -3422780336408883930L;

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/index")
    public String execute(ModelMap model) {
        model.addAttribute("something", this);
        return "index";
    }
}
