package checks;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import oast.Oast;
import oast.SwtFrame;
import oast.SwtFrame.UserInputCluster;

/**
 * Enables/disables username/password controls if auth-user-pass present/missed.
 * Do nothing if config file not exists.
 *
 */
public class UserPassConfChecker implements Checker {

    private static final String AUTH_OPTION = "auth-user-pass";

    public boolean coninueValidation() {
        return true;
    }

    public String getMessage() {
        return null;
    }

    public boolean validate(SwtFrame swtFrame) {
        String configFileName = Oast.getConfigurationManager().getConfigFile();
        File configFile = new File(configFileName);
        try {
            if (configFile.exists()) {
                boolean userAuth = needUserAuthorization(configFile);
                enableControls(swtFrame, userAuth);
            }
        } catch (IOException e) {
            System.out.println("Failed to parse config file: " + e.getMessage());
        }
        return true;
    }

    private boolean needUserAuthorization(File configFile) throws IOException {
        boolean authUserPassOptionPresent = false;
        Scanner in = new Scanner(configFile);
        while (in.hasNextLine()) {
            String line = in.nextLine().trim();
            if (line.startsWith(AUTH_OPTION)) {
                authUserPassOptionPresent = true;
                break;
            }
        }
        return authUserPassOptionPresent;
    }

    private void enableControls(SwtFrame swtFrame, boolean enable) {
        UserInputCluster cluster = swtFrame.getUserInputCluster();
        if (cluster == null) {
            System.out.println("WARN: cluster was null, this should not happen");
            return;
        }
        cluster.userName.setEnabled(enable);
        cluster.userPasswd.setEnabled(enable);
        cluster.btnRemember.setEnabled(enable);
        if (!enable) {
            cluster.userName.setText("");
            cluster.userPasswd.setText("");
            Oast.getConfigurationManager().setStoreLogData(false);
            cluster.btnRemember.setSelection(false);
        }
    }
}
