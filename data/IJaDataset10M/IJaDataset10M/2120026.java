package com.ca.www.UnicenterServicePlus.ServiceDesk;

public interface USD_WebServiceSoap extends java.rmi.Remote {

    public java.lang.String transfer(int sid, java.lang.String creator, java.lang.String objectHandle, java.lang.String description, boolean setAssignee, java.lang.String newAssigneeHandle, boolean setGroup, java.lang.String newGroupHandle, boolean setOrganization, java.lang.String newOrganizationHandle) throws java.rmi.RemoteException;

    public java.lang.String search(int sid, java.lang.String problem, int resultSize, java.lang.String properties, java.lang.String sortBy, boolean descending, boolean relatedCategories, int searchType, int matchType, int searchField, java.lang.String categoryPath, java.lang.String whereClause, int maxDocIDs) throws java.rmi.RemoteException;

    public java.lang.String getDocument(int sid, int docId, java.lang.String propertyList, boolean relatedDoc, boolean getAttmnt, boolean getHistory, boolean getComments, boolean getNotiList) throws java.rmi.RemoteException;

    public java.lang.String getObjectValues(int sid, java.lang.String objectHandle, java.lang.String[] attributes) throws java.rmi.RemoteException;

    public java.lang.String getLrelValues(int sid, java.lang.String contextObject, java.lang.String lrelName, int startIndex, int endIndex, java.lang.String[] attributes) throws java.rmi.RemoteException;

    public void addAssetLog(int sid, java.lang.String assetHandle, java.lang.String contactHandle, java.lang.String logText) throws java.rmi.RemoteException;

    public void createLrelRelationships(int sid, java.lang.String contextObject, java.lang.String lrelName, java.lang.String[] addObjectHandles) throws java.rmi.RemoteException;

    public void addMemberToGroup(int sid, java.lang.String contactHandle, java.lang.String groupHandle) throws java.rmi.RemoteException;

    public java.lang.String attachChangeToRequest(int sid, java.lang.String creator, java.lang.String requestHandle, java.lang.String changeHandle, java.lang.String[] changeAttrVals, java.lang.String description) throws java.rmi.RemoteException;

    public java.lang.String callServerMethod(int sid, java.lang.String methodName, java.lang.String factoryName, java.lang.String formatList, java.lang.String[] parameters) throws java.rmi.RemoteException;

    public java.lang.String changeStatus(int sid, java.lang.String creator, java.lang.String objectHandle, java.lang.String description, java.lang.String newStatusHandle) throws java.rmi.RemoteException;

    public int clearNotification(int sid, java.lang.String lrObject, java.lang.String clearBy) throws java.rmi.RemoteException;

    public java.lang.String createActivityLog(int sid, java.lang.String creator, java.lang.String objectHandle, java.lang.String description, java.lang.String logType, int timeSpent, boolean internal) throws java.rmi.RemoteException;

    public void createAsset(int sid, java.lang.String[] attrVals, java.lang.String[] attributes, javax.xml.rpc.holders.StringHolder createAssetResult, javax.xml.rpc.holders.StringHolder newAssetHandle, javax.xml.rpc.holders.StringHolder newExtensionHandle, javax.xml.rpc.holders.StringHolder newExtensionName) throws java.rmi.RemoteException;

    public java.lang.String createAssetParentChildRelationship(int sid, java.lang.String parentHandle, java.lang.String childHandle) throws java.rmi.RemoteException;

    public java.lang.String createAttachment(int sid, java.lang.String repositoryHandle, java.lang.String objectHandle, java.lang.String description, java.lang.String fileName) throws java.rmi.RemoteException;

    public int removeAttachment(int sid, java.lang.String attHandle) throws java.rmi.RemoteException;

    public java.lang.String createChangeOrder(int sid, java.lang.String creatorHandle, java.lang.String[] attrVals, java.lang.String[] propertyValues, java.lang.String template, java.lang.String[] attributes, javax.xml.rpc.holders.StringHolder newChangeHandle, javax.xml.rpc.holders.StringHolder newChangeNumber) throws java.rmi.RemoteException;

    public java.lang.String createIssue(int sid, java.lang.String creatorHandle, java.lang.String[] attrVals, java.lang.String[] propertyValues, java.lang.String template, java.lang.String[] attributes, javax.xml.rpc.holders.StringHolder newIssueHandle, javax.xml.rpc.holders.StringHolder newIssueNumber) throws java.rmi.RemoteException;

    public void createWorkFlowTask(int sid, java.lang.String[] attrVals, java.lang.String objectHandle, java.lang.String creatorHandle, java.lang.String selectedWorkFlow, java.lang.String taskType, java.lang.String[] attributes, javax.xml.rpc.holders.StringHolder createWorkFlowTaskResult, javax.xml.rpc.holders.StringHolder newHandle) throws java.rmi.RemoteException;

    public void deleteWorkFlowTask(int sid, java.lang.String workFlowHandle, java.lang.String objectHandle) throws java.rmi.RemoteException;

    public void createObject(int sid, java.lang.String objectType, java.lang.String[] attrVals, java.lang.String[] attributes, javax.xml.rpc.holders.StringHolder createObjectResult, javax.xml.rpc.holders.StringHolder newHandle) throws java.rmi.RemoteException;

    public java.lang.String createRequest(int sid, java.lang.String creatorHandle, java.lang.String[] attrVals, java.lang.String[] propertyValues, java.lang.String template, java.lang.String[] attributes, javax.xml.rpc.holders.StringHolder newRequestHandle, javax.xml.rpc.holders.StringHolder newRequestNumber) throws java.rmi.RemoteException;

    public java.lang.String createTicket(int sid, java.lang.String description, java.lang.String problem_type, java.lang.String userid, java.lang.String asset, java.lang.String duplication_id, javax.xml.rpc.holders.StringHolder newTicketHandle, javax.xml.rpc.holders.StringHolder newTicketNumber, javax.xml.rpc.holders.StringHolder returnUserData, javax.xml.rpc.holders.StringHolder returnApplicationData) throws java.rmi.RemoteException;

    public java.lang.String createQuickTicket(int sid, java.lang.String customerHandle, java.lang.String description, javax.xml.rpc.holders.StringHolder newTicketHandle, javax.xml.rpc.holders.StringHolder newTicketNumber) throws java.rmi.RemoteException;

    public java.lang.String closeTicket(int sid, java.lang.String description, java.lang.String ticketHandle) throws java.rmi.RemoteException;

    public void logComment(int sid, java.lang.String ticketHandle, java.lang.String comment, int internalFlag) throws java.rmi.RemoteException;

    public java.lang.String getPolicyInfo(int sid) throws java.rmi.RemoteException;

    public java.lang.String detachChangeFromRequest(int sid, java.lang.String creator, java.lang.String requestHandle, java.lang.String description) throws java.rmi.RemoteException;

    public java.lang.String doSelect(int sid, java.lang.String objectType, java.lang.String whereClause, int maxRows, java.lang.String[] attributes) throws java.rmi.RemoteException;

    public com.ca.www.UnicenterServicePlus.ServiceDesk.ListResult doQuery(int sid, java.lang.String objectType, java.lang.String whereClause) throws java.rmi.RemoteException;

    public java.lang.String escalate(int sid, java.lang.String creator, java.lang.String objectHandle, java.lang.String description, boolean setAssignee, java.lang.String newAssigneeHandle, boolean setGroup, java.lang.String newGroupHandle, boolean setOrganization, java.lang.String newOrganizationHandle, boolean setPriority, java.lang.String newPriorityHandle) throws java.rmi.RemoteException;

    public void freeListHandles(int sid, int[] handles) throws java.rmi.RemoteException;

    public void getAssetExtensionInformation(int sid, java.lang.String assetHandle, java.lang.String[] attributes, javax.xml.rpc.holders.StringHolder getAssetExtInfoResult, javax.xml.rpc.holders.StringHolder extensionHandle, javax.xml.rpc.holders.StringHolder extensionName) throws java.rmi.RemoteException;

    public java.lang.String getConfigurationMode(int sid) throws java.rmi.RemoteException;

    public java.lang.String getGroupMemberListValues(int sid, java.lang.String whereClause, int numToFetch, java.lang.String[] attributes) throws java.rmi.RemoteException;

    public java.lang.String getObjectTypeInformation(int sid, java.lang.String factory) throws java.rmi.RemoteException;

    public java.lang.String getHandleForUserid(int sid, java.lang.String userID) throws java.rmi.RemoteException;

    public java.lang.String getAccessTypeForContact(int sid, java.lang.String contactHandle) throws java.rmi.RemoteException;

    public java.lang.String getListValues(int sid, int listHandle, int startIndex, int endIndex, java.lang.String[] attributeNames) throws java.rmi.RemoteException;

    public int getLrelLength(int sid, java.lang.String contextObject, java.lang.String lrelName) throws java.rmi.RemoteException;

    public com.ca.www.UnicenterServicePlus.ServiceDesk.ListResult getNotificationsForContact(int sid, java.lang.String contactHandle, int queryStatus) throws java.rmi.RemoteException;

    public com.ca.www.UnicenterServicePlus.ServiceDesk.ListResult getPendingChangeTaskListForContact(int sid, java.lang.String contactHandle) throws java.rmi.RemoteException;

    public com.ca.www.UnicenterServicePlus.ServiceDesk.ListResult getPendingIssueTaskListForContact(int sid, java.lang.String contactHandle) throws java.rmi.RemoteException;

    public java.lang.String getPropertyInfoForCategory(int sid, java.lang.String categoryHandle, java.lang.String[] attributes) throws java.rmi.RemoteException;

    public com.ca.www.UnicenterServicePlus.ServiceDesk.ListResult getRelatedList(int sid, java.lang.String objectHandle, java.lang.String listName) throws java.rmi.RemoteException;

    public void getRelatedListValues(int sid, java.lang.String objectHandle, java.lang.String listName, int numToFetch, java.lang.String[] attributes, javax.xml.rpc.holders.StringHolder getRelatedListValuesResult, javax.xml.rpc.holders.IntHolder numRowsFound) throws java.rmi.RemoteException;

    public java.lang.String getWorkFlowTemplates(int sid, java.lang.String objectHandle, java.lang.String[] attributes) throws java.rmi.RemoteException;

    public java.lang.String getTaskListValues(int sid, java.lang.String objectHandle, java.lang.String[] attributes) throws java.rmi.RemoteException;

    public java.lang.String getValidTaskTransitions(int sid, java.lang.String taskHandle, java.lang.String[] attributes) throws java.rmi.RemoteException;

    public int login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;

    public int loginService(java.lang.String username, java.lang.String password, java.lang.String policy) throws java.rmi.RemoteException;

    public java.lang.String loginServiceManaged(java.lang.String policy, java.lang.String encrypted_policy) throws java.rmi.RemoteException;

    public int impersonate(int sid, java.lang.String userid) throws java.rmi.RemoteException;

    public void logout(int sid) throws java.rmi.RemoteException;

    public java.lang.String notifyContacts(int sid, java.lang.String creator, java.lang.String contextObject, java.lang.String messageTitle, java.lang.String messageBody, int notifyLevel, java.lang.String[] notifyees, boolean internal) throws java.rmi.RemoteException;

    public void removeLrelRelationships(int sid, java.lang.String contextObject, java.lang.String lrelName, java.lang.String[] removeObjectHandles) throws java.rmi.RemoteException;

    public void removeMemberFromGroup(int sid, java.lang.String contactHandle, java.lang.String groupHandle) throws java.rmi.RemoteException;

    public int serverStatus(int sid) throws java.rmi.RemoteException;

    public java.lang.String updateObject(int sid, java.lang.String objectHandle, java.lang.String[] attrVals, java.lang.String[] attributes) throws java.rmi.RemoteException;

    public java.lang.String getBopsid(int sid, java.lang.String contact) throws java.rmi.RemoteException;

    public java.lang.String createAttmnt(int sid, java.lang.String repositoryHandle, int folderId, int objectHandle, java.lang.String description, java.lang.String fileName) throws java.rmi.RemoteException;

    public java.lang.String getDocumentsByIDs(int sid, java.lang.String docIds, java.lang.String propertyList, java.lang.String sortBy, boolean descending) throws java.rmi.RemoteException;

    public java.lang.String doSelectKD(int sid, java.lang.String whereClause, java.lang.String sortBy, boolean desc, int maxRows, java.lang.String[] attributes, int skip) throws java.rmi.RemoteException;

    public java.lang.String getDecisionTrees(int sid, java.lang.String propertyList, java.lang.String sortBy, boolean descending) throws java.rmi.RemoteException;

    public java.lang.String getComments(int sid, java.lang.String docIds) throws java.rmi.RemoteException;

    public int deleteDocument(int sid, int docId) throws java.rmi.RemoteException;

    public java.lang.String getCategory(int sid, int catId, boolean getCategoryPaths) throws java.rmi.RemoteException;

    public java.lang.String getStatuses(int sid) throws java.rmi.RemoteException;

    public java.lang.String getBookmarks(int sid, java.lang.String contactId) throws java.rmi.RemoteException;

    public java.lang.String getQuestionsAsked(int sid, int resultSize, boolean descending) throws java.rmi.RemoteException;

    public java.lang.String getPriorities(int sid) throws java.rmi.RemoteException;

    public java.lang.String getDocumentTypes(int sid) throws java.rmi.RemoteException;

    public java.lang.String getTemplateList(int sid) throws java.rmi.RemoteException;

    public java.lang.String getWorkflowTemplateList(int sid) throws java.rmi.RemoteException;

    public java.lang.String addComment(int sid, java.lang.String comment, int docId, java.lang.String email, java.lang.String username, java.lang.String contactId) throws java.rmi.RemoteException;

    public int deleteComment(int sid, int commentId) throws java.rmi.RemoteException;

    public java.lang.String createDocument(int sid, java.lang.String[] kdAttributes) throws java.rmi.RemoteException;

    public java.lang.String modifyDocument(int sid, int docId, java.lang.String[] kdAttributes) throws java.rmi.RemoteException;

    public java.lang.String rateDocument(int sid, int docId, int rating, int multiplier, java.lang.String ticketPerId, boolean onTicketAccept, boolean solveUserProblem, boolean isDefault) throws java.rmi.RemoteException;

    public java.lang.String findContacts(int sid, java.lang.String userName, java.lang.String lastName, java.lang.String firstName, java.lang.String email, java.lang.String accessType, int inactiveFlag) throws java.rmi.RemoteException;

    public java.lang.String getPermissionGroups(int sid, int groupId) throws java.rmi.RemoteException;

    public java.lang.String getContact(int sid, java.lang.String contactId) throws java.rmi.RemoteException;

    public java.lang.String addBookmark(int sid, java.lang.String contactId, int docId) throws java.rmi.RemoteException;

    public java.lang.String updateRating(int sid, int buId, int rate) throws java.rmi.RemoteException;

    public java.lang.String getFolderList(int sid, int parentFolderId, int repId) throws java.rmi.RemoteException;

    public java.lang.String getFolderInfo(int sid, int folderId) throws java.rmi.RemoteException;

    public java.lang.String getAttmntList(int sid, int folderId, int repId) throws java.rmi.RemoteException;

    public java.lang.String getAttmntInfo(int sid, int attmntId) throws java.rmi.RemoteException;

    public java.lang.String getRepositoryInfo(int sid, int repositoryId) throws java.rmi.RemoteException;

    public java.lang.String createFolder(int sid, int parentFolderId, int repId, int folderType, java.lang.String description, java.lang.String folderName) throws java.rmi.RemoteException;

    public java.lang.String faq(int sid, java.lang.String categoryIds, int resultSize, java.lang.String propertyList, java.lang.String sortBy, boolean descending, java.lang.String whereClause, int maxDocIDs) throws java.rmi.RemoteException;

    public int attmntFolderLinkCount(int sid, int folderId) throws java.rmi.RemoteException;

    public int attachURLLink(int sid, int docId, java.lang.String url, java.lang.String attmntName, java.lang.String description) throws java.rmi.RemoteException;

    public int deleteBookmark(int sid, java.lang.String contactId, int docId) throws java.rmi.RemoteException;

    public java.lang.String getKDListPerAttmnt(int sid, int attmntId) throws java.rmi.RemoteException;

    public java.lang.String getAttmntListPerKD(int sid, int docId) throws java.rmi.RemoteException;

    public int isAttmntLinkedKD(int sid, int attmntId) throws java.rmi.RemoteException;
}
