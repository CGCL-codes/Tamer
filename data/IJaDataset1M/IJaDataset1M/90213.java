package cn.com.believer.songyuanframework.openapi.storage.box.examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import cn.com.believer.songyuanframework.openapi.storage.box.BoxExternalAPI;
import cn.com.believer.songyuanframework.openapi.storage.box.factories.BoxRequestFactory;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.CreateFolderRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.CreateFolderResponse;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.DeleteRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.DownloadRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.GetAccountTreeRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.GetAuthTokenResponse;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.LogoutRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.PublicShareRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.RegisterNewUserRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.UploadRequest;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.UploadResponse;
import cn.com.believer.songyuanframework.openapi.storage.box.impl.simple.SimpleBoxImpl;
import cn.com.believer.songyuanframework.openapi.storage.box.objects.BoxException;
import cn.com.believer.songyuanframework.openapi.storage.box.objects.UploadResult;

/**
 * @author jjia
 * 
 */
public final class Tutorial {

    /** enter your api key here. */
    public static final String API_KEY = "e7ak8t2je0rxoq97k9sl2fh2mld1dn6x";

    /**
     * 
     */
    private Tutorial() {
    }

    /**
     * @param args
     *            no use
     */
    public static void main(String[] args) {
        BoxExternalAPI iBoxExternalAPI = new SimpleBoxImpl();
        try {
            String email = "test" + System.currentTimeMillis() + "@test.com";
            String password = "888888";
            RegisterNewUserRequest registerNewUserRequest = BoxRequestFactory.createRegisterNewUserRequest(Tutorial.API_KEY, email, password);
            iBoxExternalAPI.registerNewUser(registerNewUserRequest);
            GetAuthTokenResponse getAuthTokenResponse = iBoxExternalAPI.authentication(email, password, Tutorial.API_KEY);
            String authToken = getAuthTokenResponse.getAuthToken();
            CreateFolderRequest createFolderRequest = BoxRequestFactory.createCreateFolderRequest(Tutorial.API_KEY, authToken, "0", "folderName" + System.currentTimeMillis(), false);
            CreateFolderResponse createFolderResponse = iBoxExternalAPI.createFolder(createFolderRequest);
            String createdFolderId = createFolderResponse.getFolder().getFolderId();
            File tmpFile = File.createTempFile("This-is-a-Temp-File" + System.currentTimeMillis(), ".txt");
            tmpFile.deleteOnExit();
            BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile));
            out.write("this is a test file for upload" + +System.currentTimeMillis());
            out.close();
            Map fileMap = new HashMap();
            fileMap.put(tmpFile.getName(), tmpFile);
            UploadRequest uploadRequest = BoxRequestFactory.createUploadRequest(authToken, true, createdFolderId, fileMap);
            UploadResponse uploadResponse = iBoxExternalAPI.upload(uploadRequest);
            UploadResult uploadResult = (UploadResult) uploadResponse.getUploadResultList().get(0);
            String uploadedFileId = uploadResult.getFile().getFileId();
            HashMap nameBytesHashMap = new HashMap();
            nameBytesHashMap.put("fileName.txt", "fileName.txt".getBytes());
            uploadRequest = BoxRequestFactory.createUploadRequest(authToken, false, createdFolderId, nameBytesHashMap);
            iBoxExternalAPI.upload(uploadRequest);
            PublicShareRequest publicShareRequest = BoxRequestFactory.createPublicShareRequest(Tutorial.API_KEY, authToken, "folder", "888888", createdFolderId, "this is my public folder !", null);
            iBoxExternalAPI.publicShare(publicShareRequest);
            String[] params = { "nozip" };
            GetAccountTreeRequest getAccountTreeRequest = BoxRequestFactory.createGetAccountTreeRequest(Tutorial.API_KEY, authToken, "0", params);
            iBoxExternalAPI.getAccountTree(getAccountTreeRequest);
            File tmpFile2 = new File("downloadedFileNo." + System.currentTimeMillis() + ".txt");
            tmpFile2.createNewFile();
            DownloadRequest downloadRequest = BoxRequestFactory.createDownloadRequest(authToken, uploadedFileId, true, tmpFile2);
            iBoxExternalAPI.download(downloadRequest);
            DeleteRequest deleteRequest = BoxRequestFactory.createDeleteRequest(Tutorial.API_KEY, authToken, "file", uploadedFileId);
            iBoxExternalAPI.delete(deleteRequest);
            LogoutRequest logoutRequest = BoxRequestFactory.createLogoutRequest(Tutorial.API_KEY, authToken);
            iBoxExternalAPI.logout(logoutRequest);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BoxException e) {
            e.printStackTrace();
        }
    }
}
