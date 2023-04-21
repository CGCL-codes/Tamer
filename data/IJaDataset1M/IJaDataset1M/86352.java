package org.lifeforachild.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.lifeforachild.domain.Report;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "report", automaticallyMaintainView = false, formBackingObject = Report.class)
@RequestMapping("/report/**")
@Controller
public class ReportController {
}
