package com.gwt.tirso.gymkhana.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("regService")
public interface IRegistrationService extends RemoteService {

    public Person registerUser(Person p) throws IllegalArgumentException;

    public String checkEmail(String email);
}
