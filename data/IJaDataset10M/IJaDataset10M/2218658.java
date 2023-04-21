package de.t5book.services.impl;

import de.t5book.services.AnotherService;
import de.t5book.services.MyService;

public class MyServiceImpl implements MyService {

    private AnotherService anotherService;

    public MyServiceImpl(AnotherService anotherService) {
        this.anotherService = anotherService;
    }

    public String doBusiness() {
        return this.anotherService.doAnotherBusiness();
    }
}
