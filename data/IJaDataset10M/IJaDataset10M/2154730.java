package org.isthisjava.service;

import org.springframework.web.servlet.mvc.SimpleFormController;

public class ServiceController extends SimpleFormController {

    private ExampleServiceImpl exampleService = null;

    public ServiceController(ExampleServiceImpl exampleService) {
        super();
        this.exampleService = exampleService;
        setCommandClass(ServiceCommand.class);
        setCommandName("command");
        setFormView("/form");
        setSuccessView("/succes");
    }

    @Override
    protected void doSubmitAction(Object command) throws Exception {
        ServiceCommand serviceCommand = (ServiceCommand) command;
        int numberOfCalls = exampleService.getNumberOfCalls();
        serviceCommand.setNumberOfCalls(numberOfCalls);
    }
}
