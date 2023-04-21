package Actors;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 19/03/2009, 12:48:17 PM
 */
public class User {

    private static String userId;

    private static String userPwd;

    private static String userType;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        User.userId = userId;
    }

    public static String getUserPwd() {
        return userPwd;
    }

    public static void setUserPwd(String userPwd) {
        User.userPwd = userPwd;
    }

    public static String getUserType() {
        return userType;
    }

    public static void setUserType(String userType) {
        User.userType = userType;
    }
}
