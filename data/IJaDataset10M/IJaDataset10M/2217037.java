package org.ddmf.person.action;

import java.util.List;
import javax.annotation.Resource;
import org.ddmf.frame.action.SpringJson;
import org.ddmf.person.model.People;
import org.ddmf.person.service.PeopleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PersonServiceAction implements SpringJson {

    @Resource(name = "peopleService")
    private PeopleService peopleService;

    @RequestMapping(value = "/PersonServiceAction/queryPeopleByName.do")
    public String queryPeopleByName(String names, Model model) {
        List<People> list = peopleService.queryPeopleByName(names);
        model.addAttribute("list", list);
        return JSONVIEW;
    }
}
