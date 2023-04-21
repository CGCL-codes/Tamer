package owl.client.managed.request;

import com.google.gwt.requestfactory.shared.InstanceRequest;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtMirroredFrom;

@RooGwtMirroredFrom("owl.server.data.OWLUser")
@ServiceName("owl.server.data.OWLUser")
public interface OWLUserRequest extends RequestContext {

    abstract Request<java.lang.Long> countOWLUsers();

    abstract Request<java.util.List<owl.client.managed.request.OWLUserProxy>> findAllOWLUsers();

    abstract Request<java.util.List<owl.client.managed.request.OWLUserProxy>> findOWLUserEntries(int firstResult, int maxResults);

    abstract Request<owl.client.managed.request.OWLUserProxy> findOWLUser(Long id);

    abstract InstanceRequest<owl.client.managed.request.OWLUserProxy, java.lang.Void> remove();

    abstract InstanceRequest<owl.client.managed.request.OWLUserProxy, java.lang.Void> persist();
}
