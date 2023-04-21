package net.emotivecloud.gui;

public class User {

    String user;

    String password;

    String message;

    public User() {
    }

    public User(String string, String string2, String string3) {
        this.user = string;
        this.password = string2;
        this.message = string3;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return this.message;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return user.trim() + " " + password.trim() + " " + message.trim();
    }

    public static User fromString(String str) throws Exception {
        String[] parts = str.split(" ");
        if (parts.length != 3) {
            System.out.println("[User.java] Error");
            throw new Exception("[User.java] error");
        }
        return new User(parts[0], parts[1], parts[2]);
    }
}
