package gtf.client.Service;

import gtf.client.Data.ACL_Group_Data;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ACL_Group_ServerServiceAsync {

    void listGroupData(AsyncCallback callback);

    void getGroupData(String group_name, AsyncCallback callback);

    void insertGroupData(ACL_Group_Data group, AsyncCallback callback);

    void updateGroupData(ACL_Group_Data group, AsyncCallback callback);

    void deleteGroupData(ACL_Group_Data group, AsyncCallback callback);
}
