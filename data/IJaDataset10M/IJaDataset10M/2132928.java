package controlClasses;

public class LoginStatus {

    private int loginStatus = 0;

    private static int homeClickCount = 0;

    public LoginStatus(int loginStatus, int homeClickCount2) {
        super();
        this.loginStatus = loginStatus;
        LoginStatus.homeClickCount = homeClickCount2;
    }

    public LoginStatus(int homeClickCount) {
        super();
        LoginStatus.homeClickCount = homeClickCount;
    }

    public LoginStatus() {
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public static int getHomeClickCount() {
        return homeClickCount;
    }

    public static void setHomeClickCount(int homeClickCount2) {
        homeClickCount = homeClickCount2;
    }
}
