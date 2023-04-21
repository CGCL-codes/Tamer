package fr.lgm.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import fr.lgm.shared.Members;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface ScheduleRequest extends RemoteService {

    String greetServer(String name) throws IllegalArgumentException;

    Members[] getUser() throws IllegalArgumentException;

    String[] Lire3() throws IllegalArgumentException;

    String[] getCurrentMonth() throws IllegalArgumentException;
}
