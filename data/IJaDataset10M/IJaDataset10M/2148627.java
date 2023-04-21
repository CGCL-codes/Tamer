package gwtm.client.services.tm;

import com.google.gwt.user.client.rpc.AsyncCallback;
import gwtm.client.services.tm.virtual.AssociationVirtual;
import gwtm.client.services.tm.virtual.TopicVirtual;

/**
 *
 * @author User
 */
public interface AssociationServiceAsync {

    public void createRole(AssociationVirtual aV, TopicVirtual player, TopicVirtual type, AsyncCallback callback);

    public void getRoles(AssociationVirtual aV, AsyncCallback callback);

    public void getReifier(AssociationVirtual aV, AsyncCallback callback);

    public void getType(AssociationVirtual aV, AsyncCallback callback);

    public void remove(AssociationVirtual aV, AsyncCallback callback);

    public void setType(AssociationVirtual aV, TopicVirtual type, AsyncCallback callback);
}
