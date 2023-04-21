package example.spring.web.controller;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import example.spring.service.messaging.ResultMessage;
import example.spring.web.dto.BoundObject11;

@Controller
@RequestMapping(value = "/post11")
public final class Post11Controller {

    private final transient Logger log = LoggerFactory.getLogger(getClass());

    private static final String viewName = "post11";

    @Autowired
    private ResultMessage resultMessage;

    @RequestMapping(method = RequestMethod.GET)
    public void getStub(@RequestParam(value = "tagName", required = false) final String tagName, @RequestParam(value = "page", required = false) final Integer page) {
        log.debug("tagName: {}, page: {}", tagName, page);
    }

    @ModelAttribute("boundObject11")
    public BoundObject11 formBindingObject() {
        log.debug("called");
        return (new BoundObject11("sample attr"));
    }

    @ModelAttribute("resultMessage")
    public String responseMessage() {
        log.debug("called");
        return (resultMessage.getResultMessage("210"));
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(@ModelAttribute("boundObject11") final BoundObject11 boundObject11, final BindingResult result11, final SessionStatus status, final WebRequest webRequest, final HttpServletRequest request) {
        log.debug("boundObject11: {}", boundObject11);
        log.debug("result11: {}", result11);
        log.debug("status: {}", status);
        log.debug("webRequest: {}", webRequest);
        final PropertyValues propertyValues = new ServletRequestParameterPropertyValues(request);
        log.debug("propertyValues: {}", propertyValues);
        status.setComplete();
        return (viewName);
    }
}
