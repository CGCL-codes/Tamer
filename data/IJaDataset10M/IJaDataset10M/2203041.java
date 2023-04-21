package com.rise.rois.ui.util;

import java.util.StringTokenizer;
import com.rise.rois.server.SessionManagerWrapper;
import com.rise.rois.server.SessionManagerStub.SessionDAO;

public class SessionUtil {

    public static int LEARNING_ID = 1;

    public static int INDIVIDUAL_ID = 2;

    public static int SCHEDULED_INDIVIDUAL_ID = 3;

    public static boolean isComputerInActiveSession(int computer_id) {
        SessionDAO[] sessionDAOs = SessionManagerWrapper.getSessionOfType(SessionUtil.INDIVIDUAL_ID, true);
        if (sessionDAOs != null) {
            for (SessionDAO sessionDAO : sessionDAOs) {
                if (sessionDAO.getComputerId() == computer_id) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isUserInActiveSession(int user_id) {
        SessionDAO[] sessionDAOs = SessionManagerWrapper.getSessionOfType(SessionUtil.INDIVIDUAL_ID, true);
        if (sessionDAOs != null) {
            for (SessionDAO sessionDAO : sessionDAOs) {
                if (sessionDAO.getUserId() == user_id) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getSessionId(String text) {
        StringTokenizer stringTokenizer = new StringTokenizer(text, ":");
        stringTokenizer.nextToken();
        String idAsString = stringTokenizer.nextToken();
        int id = -1;
        try {
            id = Integer.parseInt(idAsString);
        } catch (NumberFormatException numberFormatException) {
            id = -1;
        }
        return id;
    }
}
