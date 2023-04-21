package com.germinus.portlet.groovyexplorer;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.germinus.portlet.ScribeController;

@RequestMapping("VIEW")
public class GroovyExplorerController extends ScribeController {

    @RequestMapping
    public String view() {
        return "groovyexplorer/" + "view";
    }

    @RequestMapping
    public void executeScript(@RequestParam("script") String script, ActionRequest actionRequest, ActionResponse actionResponse, Model model) {
        Script scriptParsed = new GroovyShell().parse(script);
        scriptParsed.setMetaClass(new GroovyExplorerMetaClass(scriptParsed.getMetaClass(), actionRequest));
        Object evaluate = scriptParsed.run();
        model.addAttribute("evaluation", evaluate);
    }
}
