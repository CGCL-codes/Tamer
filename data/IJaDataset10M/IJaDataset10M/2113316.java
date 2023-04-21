package org.cyberaide.services.cxf.service;

import javax.jws.WebService;
import javax.jws.WebMethod;

@WebService
public class MediatorImpl implements IMediator {

    @WebMethod
    public String jobsubmit(String strJob) {
        return ("You submitted job: " + strJob);
    }
}
