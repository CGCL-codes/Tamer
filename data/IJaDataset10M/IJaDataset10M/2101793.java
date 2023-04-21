package unibg.overencrypt.client.managers;

import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import unibg.overencrypt.client.ClientConfiguration;
import unibg.overencrypt.client.LocalPrivateResource;
import unibg.overencrypt.client.TokensResource;
import unibg.overencrypt.client.view.CancelListener;
import unibg.overencrypt.client.view.Finder;
import unibg.overencrypt.client.view.IndefiniteProgressDialog;
import unibg.overencrypt.protocol.ClientPrimitives;
import unibg.overencrypt.protocol.OperationType;
import unibg.overencrypt.protocol.OverEncryptRequest;
import unibg.overencrypt.protocol.ServerPrimitives;
import unibg.overencrypt.protocol.OverEncryptRequest.OverEncryptRequestType;
import unibg.overencrypt.utility.FileSystemUtils;
import unibg.overencrypt.utility.RunnerExecutables;

/**
 * Manages process of uploading folder on the WebDAV Server.
 *
 * @author Flavio Giovarruscio & Riccardo Tribbia
 * @version 1.0
 */
public class ClientUploadFolderManager {

    /** Logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(ClientUploadFolderManager.class);

    /** The owner of the folder. */
    private static String owner = "";

    /** The owner path. */
    private static String ownerPath = "";

    /** The passphrase. */
    private static String passphrase = "";

    /** The new folder name. */
    private static String folderName = "";

    /** The folder path. */
    private static String path = "";

    /** The decrypted Access Control Lists of BEL level. */
    private static String aclBEL = "";

    /** The decrypted Access Control Lists of SEL level. */
    private static String aclSEL = "''";

    /** The folder id in token's graph. */
    private static String folderIdGraph = "";

    /** The folder id in database. */
    private static String folderIdDB = "";

    /** The SEL encryption done. */
    private static boolean hasSEL;

    /**
	 * Start the upload file process showing the users selection GUI.
	 *
	 * @param folderPath the actual path to communication with server throught .response / .request
	 */
    public static void startUploadFolder(String folderPath) {
        path = folderPath;
        if (folderPath.contains(ClientConfiguration.getURL_WEBDAV_SERVER() + "/Shared")) {
            JOptionPane.showMessageDialog(null, "Creation folder in Shared directory is not allowed", "Not allowed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JTextField folderField = new JTextField();
        JCheckBox hasSELBox = new JCheckBox("encrypt SEL");
        Object[] elements = { new String("Enter folder name:"), folderField, hasSELBox };
        if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(null, elements, "Create Encrypt Folder", JOptionPane.OK_CANCEL_OPTION)) {
            return;
        }
        if (folderField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Folder name must not be empty.\nRetry, please.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (folderField.getText().contains("Shared")) {
            JOptionPane.showMessageDialog(null, "Folder name must not contain \"Shared\".\nRetry, please.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        folderName = folderField.getText();
        hasSEL = hasSELBox.isSelected();
        JFrame frame = new JFrame();
        IndefiniteProgressDialog inpd = new IndefiniteProgressDialog(frame, "Creating new folder...", "Retrieving your friends...", new CancelListener(frame));
        LocalPrivateResource localPrivateResource = new LocalPrivateResource();
        try {
            owner = localPrivateResource.retrieveValue("UserInfo.userId");
            passphrase = localPrivateResource.retrieveValue("UserInfo.pin");
        } catch (Exception e) {
            LOGGER.error("Error while retrieving user id from local private file - " + e.getMessage());
        }
        Finder.showFinder(folderPath, owner, null, OperationType.UPLOAD);
        inpd.dispose();
    }

    /**
	 * Main uploading folder process method.
	 *
	 * @param usersSelected the users selected
	 */
    public static void generateFolder(String usersSelected, IndefiniteProgressDialog inpd) {
        inpd.setMessage("Starting upload folder process...");
        if (UploadManager.setUploadManager(path, owner, usersSelected, path, folderName)) {
            String acl = "";
            String returnedValues[] = new String[2];
            returnedValues = ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_TOKEN_DATA1, "folderId", "acl");
            folderIdGraph = returnedValues[0];
            acl = returnedValues[1];
            LOGGER.debug("folderIdGraph: " + folderIdGraph);
            LOGGER.debug("acl: " + acl);
            if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "folderId generated: " + folderIdGraph);
            String relativePath = path.replace(ClientConfiguration.getURL_WEBDAV_SERVER(), "");
            ownerPath = "/" + owner + relativePath + "/" + folderName;
            LOGGER.debug("ownerPath: " + ownerPath);
            if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "ownerPath generated: " + ownerPath);
            uploadFolder(usersSelected, acl, true, inpd);
            uploadCompleted(usersSelected, inpd);
        } else {
            inpd.dispose();
        }
    }

    /**
	 * Upload folder.
	 */
    private static void uploadFolder(String usersSelected, String acl, boolean sel, IndefiniteProgressDialog inpd) {
        String nextToken = "";
        int aclType = 0;
        boolean destinationACLReached = false;
        boolean result3 = false;
        boolean firstStep = true;
        boolean firstAesKey = true;
        FileSystemUtils.deletePrivateFile();
        UpdateTokenManager.updateToken(path, owner, passphrase, acl, inpd);
        do {
            if (UploadManager.firstStepCreateJson(path, owner)) {
                aclType = Integer.valueOf(ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_FIRST_STEP_JSON, "result")[0]);
                LOGGER.debug("aclType: " + aclType);
                inpd.setMessage("Result generated");
                if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "result generated - aclType: " + aclType);
            }
            if (aclType == 1) {
                do {
                    firstStep = true;
                    do {
                        String json1 = "";
                        if (UploadManager.createJsonUpload(path, owner, nextToken, firstStep)) {
                            json1 = ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_JSON_UPLOAD, "json")[0];
                            if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "json1 generated: " + json1);
                        }
                        if (!("{\"values\":[]}".equals(json1))) {
                            FileSystemUtils.writePrivateFile("json.txt", json1);
                            if (firstStep) {
                                if (firstAesKey) {
                                    String command[] = { ClientConfiguration.getEXECUTABLES_PATH() + "/wpes1_linux", "new1", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/json.txt", passphrase, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKey.txt", folderIdGraph, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/next.txt", acl };
                                    RunnerExecutables.execute(command);
                                } else {
                                    String command[] = { ClientConfiguration.getEXECUTABLES_PATH() + "/wpes1_linux", "new1", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/json.txt", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKeyFin.txt", passphrase, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKey.txt", folderIdGraph, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/next.txt", acl };
                                    RunnerExecutables.execute(command);
                                }
                                firstStep = false;
                            } else {
                                if (firstAesKey) {
                                    String command[] = { ClientConfiguration.getEXECUTABLES_PATH() + "/wpes1_linux", "newn", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/json.txt", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKey.txt", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKey.txt", folderIdGraph, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/next.txt", acl };
                                    RunnerExecutables.execute(command);
                                } else {
                                    String command[] = { ClientConfiguration.getEXECUTABLES_PATH() + "/wpes1_linux", "newn", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/json.txt", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKeyFin.txt", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKey.txt", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKey.txt", folderIdGraph, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/next.txt", acl };
                                    RunnerExecutables.execute(command);
                                }
                            }
                            String next = FileSystemUtils.readPrivateFile("next.txt");
                            nextToken = next;
                            if (UploadManager.finishedULToken(path, owner, next)) {
                                destinationACLReached = Boolean.valueOf(ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_FINISHED_UPLOAD_TOKEN, "result")[0]);
                                inpd.setMessage("Result 2 generated");
                                LOGGER.debug("next: " + next);
                                LOGGER.debug("destinationACLReached: " + destinationACLReached);
                                if (ClientConfiguration.debug) JOptionPane.showConfirmDialog(null, "destinationACLReached: " + destinationACLReached);
                            }
                            String jsonout = FileSystemUtils.readPrivateFile("jsonout.txt");
                            LOGGER.debug("jsonout: " + jsonout);
                            if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "Jsonout generated: " + jsonout);
                            if (UploadManager.putToken(path, owner, jsonout)) {
                                ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_SUCCESSFUL);
                                inpd.setMessage("Token generated...");
                                if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "success generated.");
                            }
                            LOGGER.debug("DELETING JSON.TXT: " + FileSystemUtils.deletePrivateFile("Json.txt"));
                        } else {
                            destinationACLReached = true;
                        }
                    } while (destinationACLReached == false);
                    File aesKeyFile = new File(ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKey.txt");
                    if (firstAesKey && aesKeyFile.exists()) {
                        aesKeyFile.renameTo(new File(ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKeyFin.txt"));
                        firstAesKey = false;
                    }
                    if (UploadManager.moreUsersAcl(path, owner)) {
                        result3 = Boolean.valueOf(ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_MORE_USERS_ACL, "result")[0]);
                        inpd.setMessage("Result 3 generated");
                        LOGGER.debug("result3:" + result3);
                        if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "result3 generated: " + result3);
                    }
                } while (result3 == true);
            } else if (aclType == 3) {
                String json2 = "";
                if (UploadManager.createJsonUpload(path, owner, "", true)) {
                    json2 = ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_JSON_UPLOAD, "json")[0];
                }
                FileSystemUtils.writePrivateFile("json.txt", json2);
                if (firstAesKey) {
                    String command[] = { ClientConfiguration.getEXECUTABLES_PATH() + "/wpes1_linux", "new1", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/json.txt", passphrase, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKey.txt", folderIdGraph, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/next.txt", acl };
                    RunnerExecutables.execute(command);
                    firstAesKey = false;
                    File aesKeyFile = new File(ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKey.txt");
                    aesKeyFile.renameTo(new File(ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKeyFin.txt"));
                } else {
                    String command[] = { ClientConfiguration.getEXECUTABLES_PATH() + "/wpes1_linux", "new1", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/json.txt", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKeyFin.txt", passphrase, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKey.txt", folderIdGraph, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/next.txt", acl };
                    RunnerExecutables.execute(command);
                }
                String jsonout = "";
                jsonout = FileSystemUtils.readPrivateFile("jsonout.txt");
                LOGGER.debug("acl: " + acl);
                LOGGER.debug("jsonout: " + jsonout);
                if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "success generated, look at debug");
                if (UploadManager.putToken(path, owner, jsonout)) {
                    ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_SUCCESSFUL);
                    inpd.setMessage("Token generated...");
                }
            } else if (aclType == 2) {
                String act = "";
                String notes = "";
                if (UploadManager.currentNote(path, owner)) {
                    String values[] = ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_CURRENT_NOTE, "act", "notes");
                    act = values[0];
                    notes = values[1];
                    inpd.setMessage("Acl note generated");
                    LOGGER.debug("act: " + act);
                    LOGGER.debug("notes: " + notes);
                }
                FileSystemUtils.writePrivateFile("NoteJson.txt", notes);
                if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "NoteJson.txt generated");
                String command[] = { ClientConfiguration.getEXECUTABLES_PATH() + "/wpes1_linux", "note", act, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/AesKeyFin.txt", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/Note.txt", folderIdGraph, acl, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/NoteJson.txt", passphrase };
                RunnerExecutables.execute(command);
                if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "Note.txt generated. ");
                String note = FileSystemUtils.readPrivateFile("Note.txt");
                if (UploadManager.putNote(path, owner, note)) {
                    ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_SUCCESSFUL);
                    inpd.setMessage("Note inserted in DB");
                    if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "note inserted in db");
                }
                LOGGER.debug("DELETING NOTEJSON.TXT: " + FileSystemUtils.deletePrivateFile("NoteJson.txt"));
            } else {
                aclType = 0;
            }
        } while (aclType != 0);
        if (UploadManager.putFolder(path, owner, ownerPath)) {
            folderIdDB = ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_FOLDER_IDDB, "folderIdDB")[0];
            inpd.setMessage("Folder generated");
            LOGGER.debug("folderIdDB: " + folderIdDB);
            if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "Folder generated. ");
        }
        aclBEL = acl;
        if (sel) {
            aclSEL = "0-" + acl;
        }
    }

    /**
	 * Upload completed.
	 * @param users with who owner has shared new folder
	 */
    private static void uploadCompleted(String users, IndefiniteProgressDialog inpd) {
        FileSystemUtils.deletePrivateFile();
        String json = "";
        if (UploadManager.getView(path, owner, aclBEL, true)) {
            json = ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_GET_VIEW, "json")[0];
            inpd.setMessage("Upload completation... - Getting user informations");
            if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "json generated.");
        }
        FileSystemUtils.writePrivateFile("json.txt", json);
        String command[] = { ClientConfiguration.getEXECUTABLES_PATH() + "/wpes1_linux", "eACL", aclBEL, aclSEL, folderIdDB, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/json.txt", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/jsonout.txt", passphrase, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/aclBEL.txt", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/aclSEL.txt" };
        RunnerExecutables.execute(command);
        String jsout = "";
        jsout = FileSystemUtils.readPrivateFile("jsonout.txt");
        LOGGER.debug("jsout: " + jsout);
        if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "jsout view setted.");
        if (UploadManager.setView(owner, path, jsout)) {
            ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_SUCCESSFUL);
            inpd.setMessage("Upload completation...");
            if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "view setted. ");
        }
        String encAclBEL = FileSystemUtils.readPrivateFile("aclBEL.txt");
        String encAclSEL = FileSystemUtils.readPrivateFile("aclSEL.txt");
        LOGGER.debug("aclBEL: " + encAclBEL);
        LOGGER.debug("aclSEL: " + encAclSEL);
        if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "aclBEl aclSEL setted. ");
        if (UploadManager.putAclDB(owner, path, encAclSEL, encAclBEL)) {
            ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_SUCCESSFUL);
            inpd.setMessage("Upload completation... - Acl inserted in database");
        }
        TokensResource tokenFile = new TokensResource(path);
        tokenFile.saveTokens(folderName, folderIdDB, folderIdGraph, owner, String.valueOf(hasSEL), encAclBEL, encAclSEL);
        inpd.setMessage("Upload completation - File .tokens generated");
        if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "file .tokens generated. ");
        folderIdDB = "";
        aclBEL = "";
        aclSEL = "''";
        FileSystemUtils.deletePrivateFile();
        if (UploadManager.commit(path, owner)) {
            ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_SUCCESSFUL);
            inpd.setMessage("Upload completed - Commit done");
            if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "commit done. ");
        }
        String folderPath = path.replace(ClientConfiguration.getURL_WEBDAV_SERVER(), "");
        folderPath = "/" + owner + ((folderPath.isEmpty()) ? "" : folderPath) + "/" + folderName;
        LOGGER.debug("folderPath: " + folderPath);
        LOGGER.debug("path: " + path);
        if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "debug.");
        if (UploadManager.updatePermission(path, owner, folderPath, users)) {
            ResponseManager.retrieveCommunication(path, ServerPrimitives.OE_SUCCESSFUL);
        }
        OverEncryptRequest.generateRequest(ClientPrimitives.OE_UPDATE_PERMS_AFT_FOLDER, OverEncryptRequestType.UNLOCK, owner, path);
        inpd.setMessage("Permissions updated correctly");
        inpd.dispose();
        return;
    }
}
