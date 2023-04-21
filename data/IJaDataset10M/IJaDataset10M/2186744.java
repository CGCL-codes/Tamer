package gtf.client.Service;

import gtf.client.Data.ACL_Group_Permission_Data;
import com.google.gwt.user.client.rpc.RemoteService;

public interface ACL_Group_Permission_ServerService extends RemoteService {

    public String[][] listGroup_PermissionData();

    public ACL_Group_Permission_Data getGroup_PermissionData(String group_name, int resource);

    public String insertGroup_PermissionData(ACL_Group_Permission_Data group_permission);

    public String deleteGroup_PermissionData(ACL_Group_Permission_Data group_permission);
}
