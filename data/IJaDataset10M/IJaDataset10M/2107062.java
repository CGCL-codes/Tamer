package uturismu.controller.util;

import javax.servlet.http.HttpSession;
import uturismu.bean.AccountBean;
import uturismu.dto.enumtype.AccountType;

public class SessionCheck {

    private SessionCheck() {
    }

    public static boolean isActiveSession(HttpSession session) {
        AccountBean account = null;
        account = (AccountBean) session.getAttribute("account");
        if (account == null) {
            return false;
        }
        return true;
    }

    public static boolean isTourOperator(HttpSession session) {
        AccountBean account = null;
        account = (AccountBean) session.getAttribute("account");
        if (isActiveSession(session) && account.getType().equals(AccountType.TOUR_OPERATOR)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBooker(HttpSession session) {
        AccountBean account = null;
        account = (AccountBean) session.getAttribute("account");
        if (isActiveSession(session) && account.getType().equals(AccountType.BOOKER)) {
            return true;
        } else {
            return false;
        }
    }
}
