package net.androy.smugmugtool.vo;

/**
 * This class holds the login information returned by Smugmug API Example:
 * {"stat":"ok","method":"smugmug.login.withPassword","Login{":
 * "User":{"id":303189,"NickName":"mharkus","DisplayName":"mharkus"},
 * "PasswordHash":"$1$Ou3SzvBQ$WDH1x418EWwCgcrEC4laf0",
 * "AccountType":"Standard", "FileSizeLimit":12582912, "SmugVault":false,
 * "Session":{"id":"02f3ad2fcd0a3839e7249374278b096e"}}}
 * 
 * @author marc
 * 
 */
public class LoginInfo {

    private String stat;

    private String method;

    private User User;

    private String passwordHash;

    private String accountType;

    private String fileSizeLimit;

    private boolean smugVault;

    private String session;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getFileSizeLimit() {
        return fileSizeLimit;
    }

    public void setFileSizeLimit(String fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;
    }

    public boolean isSmugVault() {
        return smugVault;
    }

    public void setSmugVault(boolean smugVault) {
        this.smugVault = smugVault;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public LoginInfo() {
    }

    public static class User {

        private int id;

        private String nickname;

        private String displayName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }
}
