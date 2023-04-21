package cowsultants.itracker.ejb.beans.webservice;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import cowsultants.itracker.ejb.client.exceptions.*;
import cowsultants.itracker.ejb.client.models.*;

/**
  * Provides client access to ITracker issue services.
  */
public interface IssueService extends EJBObject {

    public static final String JNDI_NAME = "ejb/webservice/IssueService";

    /**
      * Returns a single issue.  The user must have at least view permission for the issue.
      * @param login the userid requesting the issue
      * @param authentication the user's authentication information, if known
      * @param authType the type of authentication information being provided
      * @return the IssueModel or null if the user does not have permission
      */
    public IssueModel getIssue(Integer issueId, String login, String authentication, int authType) throws RemoteException, IssueException;

    /**
      * Returns all the issues a user can view for all available projects.
      * @param login the userid requesting the issue list
      * @param authentication the user's authentication information, if known
      * @param authType the type of authentication information being provided
      * @return an array of IssueModels
      */
    public IssueModel[] getAllIssues(String login, String authentication, int authType) throws RemoteException, IssueException;

    /**
      * Creates a new issue for a project.  If the issue should have an initial owner, the ownerLogin
      * should be populated with the appropriate login.  Attachments are supported by adding
      * AttachmentModels to the IssueModel, and also then using SOAP attachments.  The contentId of the
      * SOAP attachment must match the originalFileName set in the AttachmentModel.  Since attachment
      * order is not guarenteed in the SOAP message, that is the way the data is matched to the details
      * of the attachment.
      * @param issue IssueModel containing all the data on the issue
      * @param login the userid of the user creating the issue
      * @param authentication the user's authentication information, if known
      * @param authType the type of authentication information being provided
      * @return an IssueModel containing the newly created issue
      */
    public IssueModel createIssue(IssueModel issue, String login, String authentication, int authType) throws RemoteException, IssueException;

    /**
      * Updates an issue.  The new detailed description should be a single IssueHistoryModel in the
      * IssueModel.  If the issue should have an updated owner, the ownerLogin should be populated with the
      * appropriate login.  Attachments are supported by adding AttachmentModels to the IssueModel, and also
      * then using SOAP attachments.  The contentId of the SOAP attachment must match the originalFileName
      * set in the AttachmentModel.  Since attachment order is not guarenteed in the SOAP message, that is
      * the way the data is matched to the details of the attachment.
      * @param issue IssueModel containing all the updated data on the issue
      * @param login the userid of the user creating the issue
      * @param authentication the user's authentication information, if known
      * @param authType the type of authentication information being provided
      * @return an IssueModel containing the updated issue
      */
    public IssueModel updateIssue(IssueModel issue, String login, String authentication, int authType) throws RemoteException, IssueException;

    /**
      * Assigns an issue to a new owner.
      * @param issueId the id of the issue to assign
      * @param ownerLogin the userid of the new owner
      * @param login the userid of the user creating the issue
      * @param authentication the user's authentication information, if known
      * @param authType the type of authentication information being provided
      */
    public void assignIssue(Integer issueId, String ownerLogin, String login, String authentication, int authType) throws RemoteException, IssueException;
}
