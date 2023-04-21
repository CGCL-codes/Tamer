package sagex.remote.factory.request;

import java.util.Map;
import sagex.remote.RemoteRequest;
import sagex.remote.xmlrpc.RequestHelper;

public class UserRecordAPIFactory {

    public static RemoteRequest createRequest(String context, String command, String[] parameters) {
        if (command.equals("AddUserRecord")) {
            return sagex.remote.xmlrpc.RequestHelper.createRequest(context, "AddUserRecord", parameters, java.lang.String.class, java.lang.String.class);
        }
        if (command.equals("GetUserRecordData")) {
            return sagex.remote.xmlrpc.RequestHelper.createRequest(context, "GetUserRecordData", parameters, Object.class, java.lang.String.class);
        }
        if (command.equals("SetUserRecordData")) {
            return sagex.remote.xmlrpc.RequestHelper.createRequest(context, "SetUserRecordData", parameters, Object.class, java.lang.String.class, java.lang.String.class);
        }
        if (command.equals("GetUserRecord")) {
            return sagex.remote.xmlrpc.RequestHelper.createRequest(context, "GetUserRecord", parameters, java.lang.String.class, java.lang.String.class);
        }
        if (command.equals("DeleteUserRecord")) {
            return sagex.remote.xmlrpc.RequestHelper.createRequest(context, "DeleteUserRecord", parameters, Object.class);
        }
        if (command.equals("GetAllUserRecords")) {
            return sagex.remote.xmlrpc.RequestHelper.createRequest(context, "GetAllUserRecords", parameters, java.lang.String.class);
        }
        if (command.equals("GetAllUserStores")) {
            return sagex.remote.xmlrpc.RequestHelper.createRequest(context, "GetAllUserStores", parameters, null);
        }
        if (command.equals("DeleteAllUserRecords")) {
            return sagex.remote.xmlrpc.RequestHelper.createRequest(context, "DeleteAllUserRecords", parameters, java.lang.String.class);
        }
        if (command.equals("IsUserRecordObject")) {
            return sagex.remote.xmlrpc.RequestHelper.createRequest(context, "IsUserRecordObject", parameters, java.lang.Object.class);
        }
        if (command.equals("GetUserRecordNames")) {
            return sagex.remote.xmlrpc.RequestHelper.createRequest(context, "GetUserRecordNames", parameters, Object.class);
        }
        throw new RuntimeException("Invalid UserRecordAPIFactory Command: " + command);
    }
}
