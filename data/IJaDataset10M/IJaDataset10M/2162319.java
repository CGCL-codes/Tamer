package dniegp2pclient;

import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gruposp2p.dnie.utils.FileUtils;
import org.gruposp2p.dnie.model.DNIeUser;

/**
 *
 * @author jj
 */
public class RuntimeData {

    private static Logger logger = LoggerFactory.getLogger(RuntimeData.class);

    private static RuntimeData instance;

    public String signedDocument;

    public String signedDocumentServerUrl = "";

    public DNIeUser user = new DNIeUser();

    public boolean isDNIeLoaded = false;

    public boolean isSignedDocumentLoaded = false;

    public String dniePassword;

    private RuntimeData() {
    }

    public static RuntimeData getInstance() {
        if (instance == null) {
            instance = new RuntimeData();
            instance.checkFiles();
        }
        return instance;
    }

    private void checkFiles() {
        boolean appDir = new File(FileUtils.APPDIR).mkdir();
        if (appDir) {
            File propertiesFile = new File(FileUtils.PROPERTIES_FILE);
            try {
                propertiesFile.createNewFile();
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
