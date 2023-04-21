package org.netbeans.server.uihandler;

import org.netbeans.server.uihandler.api.Authenticator;
import org.netbeans.server.uihandler.api.Authenticator.AuthToken;
import org.netbeans.server.uihandler.api.bugs.BugReporter;

/**
 *
 * @author Jindrich Sedek
 */
public class PreparedParams {

    private static final int MAXSUMMARY = 200;

    private static final int MAXCOMMENT = 1000;

    private static final int OS_IDX = 0;

    private static final int VM_IDX = 1;

    private static final int VERSION_IDX = 2;

    private static final int USER_NAME_IDX = 3;

    private static final int SUMMARY_IDX = 4;

    private static final int COMMENT_IDX = 5;

    private static final int PASSWD_IDX = 6;

    private final String os;

    private final String vm;

    private final String version;

    private final String summary;

    private final String comment;

    private final Authenticator.AuthToken userToken;

    private PreparedParams(String os, String vm, String version, String summary, String comment, AuthToken userToken) {
        this.os = os;
        this.vm = vm;
        this.version = version;
        this.summary = summary;
        this.comment = comment;
        this.userToken = userToken;
    }

    public static PreparedParams prepare(Object[] oParams, Throwable thrown) {
        String[] params = new String[Math.max(7, oParams.length)];
        for (int i = 0; i < oParams.length; i++) {
            params[i] = oParams[i].toString();
        }
        if (params.length > 8) {
            int buildDateStart = Math.max(params.length - 6, 4);
            for (int i = buildDateStart; i < params.length; i++) {
                params[i] = "";
            }
        }
        if (params[USER_NAME_IDX] == null || params[USER_NAME_IDX].length() == 0) {
            params[USER_NAME_IDX] = BugReporter.GUEST_USER;
        }
        if (params[SUMMARY_IDX] != null) {
            if ((params[SUMMARY_IDX].length() == 0) && (thrown != null)) {
                params[SUMMARY_IDX] = thrown.getMessage();
            }
            if (params[SUMMARY_IDX].length() > MAXSUMMARY) {
                params[SUMMARY_IDX] = params[SUMMARY_IDX].substring(0, MAXSUMMARY);
            }
        }
        if (params[COMMENT_IDX] != null) {
            if (params[COMMENT_IDX].length() > MAXCOMMENT) {
                params[COMMENT_IDX] = params[COMMENT_IDX].substring(0, MAXCOMMENT);
            }
        }
        String userName = params[USER_NAME_IDX];
        String passwd = "";
        if ((params.length > PASSWD_IDX) && (params[PASSWD_IDX] != null)) {
            passwd = params[PASSWD_IDX];
        }
        Authenticator auth = Authenticate.getAuthenticator();
        Authenticator.AuthToken userToken = null;
        if (auth != null) {
            userToken = auth.authenticate(userName, passwd);
            if (userToken == null) {
                params[USER_NAME_IDX] = BugReporter.GUEST_USER;
                params[PASSWD_IDX] = null;
            }
        }
        return new PreparedParams(params[OS_IDX], params[VM_IDX], params[VERSION_IDX], params[SUMMARY_IDX], params[COMMENT_IDX], userToken);
    }

    public String getComment() {
        return comment;
    }

    public String getOs() {
        return os;
    }

    public String getSummary() {
        return summary;
    }

    public AuthToken getUserToken() {
        return userToken;
    }

    public String getUserName() {
        if (userToken != null) {
            return userToken.getUserName();
        } else {
            return BugReporter.GUEST_USER;
        }
    }

    public String getVersion() {
        return version;
    }

    public String getVm() {
        return vm;
    }

    public Long getBuildNumber() {
        if (version != null) {
            String buildNumber = org.netbeans.modules.exceptions.utils.Utils.getBuildNumber(version);
            buildNumber = org.netbeans.modules.exceptions.utils.Utils.getCustomBuildFormat(buildNumber);
            if ((buildNumber != null) && (buildNumber.length() > 0)) {
                return Long.valueOf(buildNumber);
            }
        }
        return null;
    }
}
