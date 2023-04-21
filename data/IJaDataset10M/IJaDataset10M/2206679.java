package us.gibb.dev.gwt.command;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("gwt.rpc")
public interface Dispatch extends RemoteService {

    public <C extends Command<R>, R extends Result> R execute(C command) throws CommandException;
}
