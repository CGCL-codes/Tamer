package tms.client.services;

import java.util.HashMap;
import tms.client.accesscontrol.User;
import tms.client.accesscontrol.UserAccessRights;
import tms.client.accesscontrol.UserCategory;
import tms.client.accesscontrol.UserCategoryAccessRights;
import tms.client.entities.InputField;
import tms.client.exceptions.DataOperationException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("accessRightsRetriever")
public interface AccessRightsRetrievalService extends RemoteService {

    public HashMap<UserCategory, UserCategoryAccessRights> retrieveUserCategoryAccessRightsForField(String authToken, InputField field) throws DataOperationException;

    public HashMap<User, UserAccessRights> retrieveUserAccessRightsForField(String authToken, InputField field) throws DataOperationException;
}
