package br.usp.ime.dojo.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("IdentificationService")
public interface IdentificationService extends RemoteService {

    UserDTO identify(String name) throws DojoException;

    UserDTO getUser(String name);

    SessionDTO getSession(String sessionName);

    void addSession(SessionDTO session);
}
