package cn.com.believer.songyuanframework.openapi.storage.box.factories;

import java.io.File;
import java.util.Map;
import org.apache.log4j.Logger;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.AddToMyBoxRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.AddToTagRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.CreateFolderRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.DeleteRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.DownloadRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.ExportTagsRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.GetAccountTreeRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.GetAuthTokenRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.GetFileInfoRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.GetFriendsRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.GetTicketRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.LogoutRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.MoveRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.PrivateShareRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.PublicShareRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.PublicUnshareRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.RegisterNewUserRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.RenameRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.RequestFriendsRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.SetDescriptionRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.UploadRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.VerifyRegistrationEmailRequest;

/**
 * @author Jimmy
 * 
 */
public class BoxRequestFactory extends BaseBoxFactory {

    /** log4j object. */
    protected static final Logger LOGGER = Logger.getLogger(BoxRequestFactory.class);

    /**
     * create request object.
     * 
     * @return request object
     */
    public static AddToMyBoxRequest createAddToMyBoxRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.AddToMyBoxRequest");
        return (AddToMyBoxRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param fileId
     *            file id
     * @param publicName
     *            public name
     * @param folderId
     *            folder id
     * @param tags
     *            tag string array
     * @return request object
     */
    public static AddToMyBoxRequest createAddToMyBoxRequest(String apiKey, String authToken, String fileId, String publicName, String folderId, String[] tags) {
        AddToMyBoxRequest addToMyBoxRequest = createAddToMyBoxRequest();
        addToMyBoxRequest.setApiKey(apiKey);
        addToMyBoxRequest.setAuthToken(authToken);
        addToMyBoxRequest.setFileId(fileId);
        addToMyBoxRequest.setPublicName(publicName);
        addToMyBoxRequest.setFolderId(folderId);
        addToMyBoxRequest.setTags(tags);
        return addToMyBoxRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static AddToTagRequest createAddToTagRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.AddToTagRequest");
        return (AddToTagRequest) obj;
    }

    /**
     * 
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param tags
     *            tag string array
     * @param target
     *            target
     * @param targetId
     *            target id
     * @return request object
     */
    public static AddToTagRequest createAddToTagRequest(String apiKey, String authToken, String[] tags, String target, String targetId) {
        AddToTagRequest addToTagRequest = createAddToTagRequest();
        addToTagRequest.setApiKey(apiKey);
        addToTagRequest.setAuthToken(authToken);
        addToTagRequest.setTags(tags);
        addToTagRequest.setTarget(target);
        addToTagRequest.setTargetId(targetId);
        return addToTagRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static CreateFolderRequest createCreateFolderRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.CreateFolderRequest");
        return (CreateFolderRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param parentFolderId
     *            parent folder id
     * @param folderName
     *            folder name
     * @param share
     *            share flag
     * 
     * @return request object
     */
    public static CreateFolderRequest createCreateFolderRequest(String apiKey, String authToken, String parentFolderId, String folderName, boolean share) {
        CreateFolderRequest createFolderRequest = createCreateFolderRequest();
        createFolderRequest.setApiKey(apiKey);
        createFolderRequest.setAuthToken(authToken);
        createFolderRequest.setParentFolderId(parentFolderId);
        createFolderRequest.setFolderName(folderName);
        createFolderRequest.setShare(share);
        return createFolderRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static DeleteRequest createDeleteRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.DeleteRequest");
        return (DeleteRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param target
     *            target
     * @param targetId
     *            target id
     * @return request object
     */
    public static DeleteRequest createDeleteRequest(String apiKey, String authToken, String target, String targetId) {
        DeleteRequest deleteRequest = createDeleteRequest();
        deleteRequest.setApiKey(apiKey);
        deleteRequest.setAuthToken(authToken);
        deleteRequest.setTarget(target);
        deleteRequest.setTargetId(targetId);
        return deleteRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static DownloadRequest createDownloadRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.DownloadRequest");
        return (DownloadRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param authToken
     *            auth token
     * @param fileId
     *            file id
     * @param asFile
     *            if download as JAVA file object
     * @param inFile
     *            input JAVA File object
     * @return request object
     */
    public static DownloadRequest createDownloadRequest(String authToken, String fileId, boolean asFile, File inFile) {
        DownloadRequest downloadRequest = createDownloadRequest();
        downloadRequest.setAuthToken(authToken);
        downloadRequest.setFileId(fileId);
        downloadRequest.setAsFile(asFile);
        downloadRequest.setInFile(inFile);
        return downloadRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static ExportTagsRequest createExportTagsRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.ExportTagsRequest");
        return (ExportTagsRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * 
     * @return request object
     */
    public static ExportTagsRequest createExportTagsRequest(String apiKey, String authToken) {
        ExportTagsRequest exportTagsRequest = createExportTagsRequest();
        exportTagsRequest.setApiKey(apiKey);
        exportTagsRequest.setAuthToken(authToken);
        return exportTagsRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static GetAccountTreeRequest createGetAccountTreeRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.GetAccountTreeRequest");
        return (GetAccountTreeRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param folderId
     *            folder id
     * @param params
     *            parameters
     * 
     * @return request object
     */
    public static GetAccountTreeRequest createGetAccountTreeRequest(String apiKey, String authToken, String folderId, String[] params) {
        GetAccountTreeRequest getAccountTreeRequest = createGetAccountTreeRequest();
        getAccountTreeRequest.setApiKey(apiKey);
        getAccountTreeRequest.setAuthToken(authToken);
        getAccountTreeRequest.setFolderId(folderId);
        getAccountTreeRequest.setParams(params);
        return getAccountTreeRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static GetAuthTokenRequest createGetAuthTokenRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.GetAuthTokenRequest");
        return (GetAuthTokenRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param ticket
     *            the ticket
     * 
     * @return request object
     */
    public static GetAuthTokenRequest createGetAuthTokenRequest(String apiKey, String ticket) {
        GetAuthTokenRequest getAuthTokenRequest = createGetAuthTokenRequest();
        getAuthTokenRequest.setApiKey(apiKey);
        getAuthTokenRequest.setTicket(ticket);
        return getAuthTokenRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static GetFileInfoRequest createGetFileInfoRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.GetFileInfoRequest");
        return (GetFileInfoRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param fileId
     *            file id
     * 
     * @return request object
     */
    public static GetFileInfoRequest createGetFileInfoRequest(String apiKey, String authToken, String fileId) {
        GetFileInfoRequest getFileInfoRequest = createGetFileInfoRequest();
        getFileInfoRequest.setApiKey(apiKey);
        getFileInfoRequest.setAuthToken(authToken);
        getFileInfoRequest.setFileId(fileId);
        return getFileInfoRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static GetFriendsRequest createGetFriendsRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.GetFriendsRequest");
        return (GetFriendsRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param params
     *            parameters
     * 
     * @return request object
     */
    public static GetFriendsRequest createGetFriendsRequest(String apiKey, String authToken, String[] params) {
        GetFriendsRequest getFriendsRequest = createGetFriendsRequest();
        getFriendsRequest.setApiKey(apiKey);
        getFriendsRequest.setAuthToken(authToken);
        getFriendsRequest.setParams(params);
        return getFriendsRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static GetTicketRequest createGetTicketRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.GetTicketRequest");
        return (GetTicketRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * 
     * @return request object
     */
    public static GetTicketRequest createGetTicketRequest(String apiKey) {
        GetTicketRequest getTicketRequest = createGetTicketRequest();
        getTicketRequest.setApiKey(apiKey);
        return getTicketRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static LogoutRequest createLogoutRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.LogoutRequest");
        return (LogoutRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * 
     * @return request object
     */
    public static LogoutRequest createLogoutRequest(String apiKey, String authToken) {
        LogoutRequest logoutRequest = createLogoutRequest();
        logoutRequest.setApiKey(apiKey);
        logoutRequest.setAuthToken(authToken);
        return logoutRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static MoveRequest createMoveRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.MoveRequest");
        return (MoveRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param target
     *            target
     * @param targetId
     *            target id
     * @param destinationId
     *            destination id
     * 
     * @return request object
     */
    public static MoveRequest createMoveRequest(String apiKey, String authToken, String target, String targetId, String destinationId) {
        MoveRequest moveRequest = createMoveRequest();
        moveRequest.setApiKey(apiKey);
        moveRequest.setAuthToken(authToken);
        moveRequest.setTarget(target);
        moveRequest.setTargetId(targetId);
        moveRequest.setDestinationId(destinationId);
        return moveRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static PrivateShareRequest createPrivateShareRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.PrivateShareRequest");
        return (PrivateShareRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param target
     *            target
     * @param targetId
     *            target id
     * @param emails
     *            email array
     * @param message
     *            message
     * @param nofity
     *            notify flag
     * @return request object
     */
    public static PrivateShareRequest createPrivateShareRequest(String apiKey, String authToken, String target, String targetId, String[] emails, String message, boolean nofity) {
        PrivateShareRequest privateShareRequest = createPrivateShareRequest();
        privateShareRequest.setApiKey(apiKey);
        privateShareRequest.setAuthToken(authToken);
        privateShareRequest.setTarget(target);
        privateShareRequest.setTargetId(targetId);
        privateShareRequest.setEmails(emails);
        privateShareRequest.setMessage(message);
        privateShareRequest.setNofity(nofity);
        return privateShareRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static PublicShareRequest createPublicShareRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.PublicShareRequest");
        return (PublicShareRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param target
     *            target
     * @param targetId
     *            target id
     * @param password
     *            password to protect the item
     * @param message
     *            message of the item
     * @param emails
     *            email array
     * @return request object
     */
    public static PublicShareRequest createPublicShareRequest(String apiKey, String authToken, String target, String targetId, String password, String message, String[] emails) {
        PublicShareRequest publicShareRequest = createPublicShareRequest();
        publicShareRequest.setApiKey(apiKey);
        publicShareRequest.setAuthToken(authToken);
        publicShareRequest.setTarget(target);
        publicShareRequest.setTargetId(targetId);
        publicShareRequest.setPassword(password);
        publicShareRequest.setMessage(message);
        publicShareRequest.setEmails(emails);
        return publicShareRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static PublicUnshareRequest createPublicUnshareRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.PublicUnshareRequest");
        return (PublicUnshareRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param target
     *            target
     * @param targetId
     *            target id
     * @return request object
     */
    public static PublicUnshareRequest createPublicUnshareRequest(String apiKey, String authToken, String target, String targetId) {
        PublicUnshareRequest publicUnshareRequest = createPublicUnshareRequest();
        publicUnshareRequest.setApiKey(apiKey);
        publicUnshareRequest.setAuthToken(authToken);
        publicUnshareRequest.setTarget(target);
        publicUnshareRequest.setTargetId(targetId);
        return publicUnshareRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static RegisterNewUserRequest createRegisterNewUserRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.RegisterNewUserRequest");
        return (RegisterNewUserRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param loginName
     *            login name
     * @param password
     *            password
     * @return request object
     */
    public static RegisterNewUserRequest createRegisterNewUserRequest(String apiKey, String loginName, String password) {
        RegisterNewUserRequest registerNewUserRequest = createRegisterNewUserRequest();
        registerNewUserRequest.setApiKey(apiKey);
        registerNewUserRequest.setLoginName(loginName);
        registerNewUserRequest.setPassword(password);
        return registerNewUserRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static RenameRequest createRenameRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.RenameRequest");
        return (RenameRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param target
     *            target
     * @param targetId
     *            target id
     * @param newName
     *            new name of item
     * @return request object
     */
    public static RenameRequest createRenameRequest(String apiKey, String authToken, String target, String targetId, String newName) {
        RenameRequest renameRequest = createRenameRequest();
        renameRequest.setApiKey(apiKey);
        renameRequest.setAuthToken(authToken);
        renameRequest.setTarget(target);
        renameRequest.setTargetId(targetId);
        renameRequest.setNewName(newName);
        return renameRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static RequestFriendsRequest createRequestFriendsRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.RequestFriendsRequest");
        return (RequestFriendsRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param emails
     *            email arrays
     * @param message
     *            message
     * @param params
     *            parameters
     * @return request object
     */
    public static RequestFriendsRequest createRequestFriendsRequest(String apiKey, String authToken, String[] emails, String message, String[] params) {
        RequestFriendsRequest requestFriendsRequest = createRequestFriendsRequest();
        requestFriendsRequest.setApiKey(apiKey);
        requestFriendsRequest.setAuthToken(authToken);
        requestFriendsRequest.setEmails(emails);
        requestFriendsRequest.setMessage(message);
        requestFriendsRequest.setParams(params);
        return requestFriendsRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static SetDescriptionRequest createSetDescriptionRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.SetDescriptionRequest");
        return (SetDescriptionRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param authToken
     *            auth token
     * @param target
     *            target
     * @param targetId
     *            target id
     * @param description
     *            description
     * @return request object
     */
    public static SetDescriptionRequest createSetDescriptionRequest(String apiKey, String authToken, String target, String targetId, String description) {
        SetDescriptionRequest setDescriptionRequest = createSetDescriptionRequest();
        setDescriptionRequest.setApiKey(apiKey);
        setDescriptionRequest.setAuthToken(authToken);
        setDescriptionRequest.setTarget(target);
        setDescriptionRequest.setTargetId(targetId);
        setDescriptionRequest.setDescription(description);
        return setDescriptionRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static UploadRequest createUploadRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.UploadRequest");
        return (UploadRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param authToken
     *            auth token
     * @param asFile
     *            upload as file flag
     * @param parentFolderId
     *            parent folder id
     * @param nameValueMap
     *            key is file name, value could be file or byte array.
     * @return request object
     */
    public static UploadRequest createUploadRequest(String authToken, boolean asFile, String parentFolderId, Map nameValueMap) {
        UploadRequest uploadRequest = createUploadRequest();
        uploadRequest.setAuthToken(authToken);
        uploadRequest.setAsFile(asFile);
        uploadRequest.setFolderId(parentFolderId);
        uploadRequest.setDataMap(nameValueMap);
        return uploadRequest;
    }

    /**
     * create request object.
     * 
     * @return request object
     */
    public static VerifyRegistrationEmailRequest createVerifyRegistrationEmailRequest() {
        Object obj = newInstanceOf("box4j.config.requestfactory.VerifyRegistrationEmailRequest");
        return (VerifyRegistrationEmailRequest) obj;
    }

    /**
     * create request object.
     * 
     * @param apiKey
     *            API key
     * @param loginName
     *            login name
     * @return request object
     */
    public static VerifyRegistrationEmailRequest createVerifyRegistrationEmailRequest(String apiKey, String loginName) {
        VerifyRegistrationEmailRequest verifyRegistrationEmailRequest = createVerifyRegistrationEmailRequest();
        verifyRegistrationEmailRequest.setApiKey(apiKey);
        verifyRegistrationEmailRequest.setLoginName(loginName);
        return verifyRegistrationEmailRequest;
    }
}
