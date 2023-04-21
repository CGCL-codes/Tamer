package vaj2cvs;

import com.ibm.ivj.util.base.IvjException;
import javax.swing.*;

/**
 * The class called by the <I>Tools, CVS, login...</I> menu.
 * <P>
 * It will log out before attempting to log in.  Only the pserver access is
 * supported.  A connection will be attempted to confirm that the login is good.
 */
public final class CvsLogin implements CvsConstants {

    private static final String SCRAMBLE_FROM = " !\"" + "#$%&'()*+,-./" + "0123456789" + ":;<=>?" + "@ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "[\\]^" + "_" + "`abcdefghijklmnopqrstuvwxyz" + "{|}~";

    private static final String SCRAMBLE_TO = "rx5" + "O`mHlF@LCtJDW" + "o4Kw1\"RQ_A" + "pVvnzi" + ")9S+.f(Y&g-2*{[#}76B|~;/\\Gs" + "NXkj" + "8" + "$yuhedEIc?^]'%=0:q Z,b<3!a>" + "MTPU";

    /**
 * Starts the application.
 * @param args an array of command-line arguments
 */
    public static void main(java.lang.String[] args) {
        CvsProgressDialog progressDialog = new CvsProgressDialog();
        progressDialog.setDefaultSize();
        progressDialog.show();
        CvsLoginDialog loginDialog = null;
        try {
            if (CvsData.getRootString(PASSWORD) != null) {
                progressDialog.appendProgress("Logging out...");
                CvsData.setRootString(PASSWORD, null);
                CvsData.commit();
                progressDialog.appendProgress("Logged out.");
            }
            loginDialog = new CvsLoginDialog();
            loginDialog.setServerHostname(CvsData.getRootString(SERVER_HOSTNAME));
            loginDialog.setRepositoryPath(CvsData.getRootString(REPOSITORY_PATH));
            loginDialog.setUsername(CvsData.getRootString(USERNAME));
            loginDialog.setUsingSSH(Boolean.valueOf(CvsData.getRootString(USING_SSH)).booleanValue());
            loginDialog.setSSHIdentityFile(CvsData.getRootString(SSH_IDENTITY));
            loginDialog.pack();
            Util.centerWindowOnScreen(loginDialog);
            while (true) {
                progressDialog.appendProgress("Prompting for login parameters...");
                progressDialog.setStatus("Prompting for input...");
                loginDialog.show();
                progressDialog.setStatus("Processing...");
                if (!loginDialog.getChoice().equals("OK")) {
                    progressDialog.appendProgress("Login attempt cancelled.");
                    break;
                }
                progressDialog.appendProgress("Attempting connection...");
                CvsConnection conn = null;
                try {
                    CvsData.setRootString(SSH_IDENTITY, loginDialog.getSSHIdentityFile());
                    conn = CvsConnection.getNewConnection(progressDialog, loginDialog.getServerHostname(), loginDialog.getRepositoryPath(), loginDialog.getUsername(), scramblePassword(loginDialog.getPassword()), loginDialog.isUsingSSH());
                    progressDialog.appendProgress("Connection OK.");
                    progressDialog.appendProgress("Saving login info...");
                    CvsData.setRootString(SERVER_HOSTNAME, loginDialog.getServerHostname());
                    CvsData.setRootString(REPOSITORY_PATH, loginDialog.getRepositoryPath());
                    CvsData.setRootString(USERNAME, loginDialog.getUsername());
                    String password = scramblePassword(loginDialog.getPassword());
                    CvsData.setRootString(PASSWORD, password);
                    CvsData.setRootString(USING_SSH, (new Boolean(loginDialog.isUsingSSH())).toString());
                    CvsData.commit();
                    progressDialog.appendProgress("Successful save.");
                    break;
                } catch (Exception e) {
                    progressDialog.appendProgress("Login failed.");
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.close();
                    }
                    conn = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progressDialog.appendProgress("----- Done. -----");
            progressDialog.setStatus("Done.");
            if (loginDialog != null) {
                loginDialog.dispose();
            }
        }
    }

    /**
 * This method was created in VisualAge.
 * @return java.lang.String
 * @param password java.lang.String
 */
    private static String scramblePassword(final String password) {
        StringBuffer sb = new StringBuffer(password.length() + 1);
        sb.append('A');
        for (int i = 0; i < password.length(); ++i) {
            int p = SCRAMBLE_FROM.indexOf(password.charAt(i));
            sb.append(SCRAMBLE_TO.charAt(p));
        }
        return sb.toString();
    }
}
