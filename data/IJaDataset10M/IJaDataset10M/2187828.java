package gwtm.client.services.tm;

import com.google.gwt.user.client.rpc.AsyncCallback;
import gwtm.client.services.tm.virtual.RoleVirtual;
import gwtm.client.services.tm.virtual.TopicVirtual;

/**
 *
 * @author User
 */
public interface RoleServiceAsync {

    public void getAssociation(RoleVirtual rv, AsyncCallback callback);

    public void getPlayer(RoleVirtual rv, AsyncCallback callback);

    public void getReifier(RoleVirtual rv, AsyncCallback callback);

    public void getType(RoleVirtual rv, AsyncCallback callback);

    public void remove(RoleVirtual rv, AsyncCallback callback);

    public void setPlayer(RoleVirtual rv, TopicVirtual playerV, AsyncCallback callback);

    public void setType(RoleVirtual rv, TopicVirtual typeV, AsyncCallback callback);
}
