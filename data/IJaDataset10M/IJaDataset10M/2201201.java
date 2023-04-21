package unibg.overencrypt.client.managers;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import unibg.overencrypt.client.ClientConfiguration;
import unibg.overencrypt.client.LocalPrivateResource;
import unibg.overencrypt.client.TokensResource;
import unibg.overencrypt.client.view.CancelListener;
import unibg.overencrypt.client.view.IndefiniteProgressDialog;
import unibg.overencrypt.protocol.ClientPrimitives;
import unibg.overencrypt.protocol.OperationType;
import unibg.overencrypt.protocol.OverEncryptRequest;
import unibg.overencrypt.protocol.ServerPrimitives;
import unibg.overencrypt.protocol.TokenStruct;
import unibg.overencrypt.protocol.OverEncryptRequest.OverEncryptRequestType;
import unibg.overencrypt.utility.FileSystemUtils;
import unibg.overencrypt.utility.RunnerExecutables;

/**
 * Manages process of uploading file on the WebDAV Server.
 * 
 * @author Flavio Giovarruscio & Riccardo Tribbia
 * @version 1.0
 *
 */
public class ClientUploadFileManager {

    /** Logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(ClientUploadFileManager.class);

    /** The logged user id. */
    private static String userId = "";

    /** The passphrase. */
    private static String passphrase = "";

    /** The decrypted Access Control Lists of BEL level. */
    private static String aclBEL = "";

    /**
	 * Start the upload file process.
	 *
	 * @param filePath the file path on client file system.
	 */
    public static void uploadStarted(String filePath) {
        if (filePath.equals(ClientConfiguration.getURL_WEBDAV_SERVER())) {
            JOptionPane.showMessageDialog(null, "Update file in root directory not yet developed", "Not allowed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (filePath.contains(ClientConfiguration.getURL_WEBDAV_SERVER() + "/Shared")) {
            JOptionPane.showMessageDialog(null, "Update file in in Shared directory is not allowed", "Not allowed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        String fileName = fileChooser.getSelectedFile().getName();
        String realFilePath = fileChooser.getSelectedFile().getParent();
        JFrame frame = new JFrame();
        IndefiniteProgressDialog inpd = new IndefiniteProgressDialog(frame, "File encryption process", "Encrypting file...", new CancelListener(frame));
        inpd.setVisible(true);
        inpd.setMessage("Retrieve local resources...");
        LocalPrivateResource localPrivateResource = new LocalPrivateResource();
        try {
            passphrase = localPrivateResource.retrieveValue("UserInfo.pin");
            userId = localPrivateResource.retrieveValue("UserInfo.userId");
        } catch (Exception e) {
            LOGGER.error("Error while retrieving user id from local private file - " + e.getMessage());
        }
        inpd.setMessage("Retrieve token resource...");
        LOGGER.debug("filePath: " + filePath);
        boolean requestDone = OverEncryptRequest.generateRequest(ClientPrimitives.OE_UPLMANAGER_FILE, OverEncryptRequestType.LOCK, userId, filePath, "", userId);
        if (requestDone) {
            String[] returnedValues = ResponseManager.retrieveCommunication(filePath, ServerPrimitives.OE_LOCK, "value");
            LOGGER.debug("returned values [0]: " + returnedValues[0]);
            if (!Boolean.parseBoolean(returnedValues[0])) {
                JOptionPane.showMessageDialog(null, "The folder is locked. Please, retry later", "Locked", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
        TokensResource token = new TokensResource(filePath.substring(0, filePath.lastIndexOf("/")));
        TokenStruct tokenStruct = token.getTokens(filePath.substring(filePath.lastIndexOf("/") + 1));
        aclBEL = ClientTokensManager.getACLsFromToken(filePath, OperationType.DOWNLOAD, tokenStruct, inpd)[0];
        inpd.setVisible(true);
        inpd.setMessage("Starting upload file process..");
        if (UploadManager.initUploadFile(filePath, aclBEL, userId)) {
            ResponseManager.retrieveCommunication(filePath, ServerPrimitives.OE_SUCCESSFUL);
        }
        inpd.setMessage("DSA generation...");
        String dsaFileName = DSARetriever.getDSA(filePath, userId, userId, true);
        String aesFileName = ClientTokensManager.getAESKeyFromToken(filePath, tokenStruct, aclBEL, "BEL", true, inpd);
        String command[] = { ClientConfiguration.getEXECUTABLES_PATH() + "/wpes2_linux", "eBEL", ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/" + dsaFileName, ClientConfiguration.getLOCAL_PRIVATE_RESOURCES_PATH() + "/" + aesFileName, realFilePath + "/" + fileName, ClientConfiguration.getLOCAL_TMP_PATH() + "/" + fileName, passphrase };
        RunnerExecutables.execute(command);
        if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "salva la chiave BEL di cifratura");
        String localFilePath = ClientConfiguration.getLOCAL_TMP_PATH() + "/" + fileName;
        WebDAVClientManager.uploadFileIntoWebDAV(localFilePath, filePath + "/" + fileName);
        LOGGER.debug("File uploaded to server.");
        File fileEncryptedBEL = new File(localFilePath);
        fileEncryptedBEL.delete();
        LOGGER.debug("Local file deleted");
        String[] splitted = aclBEL.split("-");
        String users = "";
        for (int i = 0; i < splitted.length; i++) {
            if (!splitted[i].equals(userId)) {
                users += splitted[i];
            }
        }
        users = userId + "," + users;
        if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "users to encrypt SEL Level: " + users);
        if (tokenStruct.hasSEL) {
            if (UploadManager.encryptSEL(filePath, userId, fileName, users)) {
                inpd.setMessage("Encryption SEL started...");
                ResponseManager.retrieveCommunication(filePath, ServerPrimitives.OE_SUCCESSFUL);
            }
        }
        OverEncryptRequest.generateRequest(ClientPrimitives.OE_ENCRYPT_SEL, OverEncryptRequestType.UNLOCK, userId, filePath);
        inpd.dispose();
        if (ClientConfiguration.debug) JOptionPane.showMessageDialog(null, "File encrypted");
        FileSystemUtils.deletePrivateFile();
    }
}
