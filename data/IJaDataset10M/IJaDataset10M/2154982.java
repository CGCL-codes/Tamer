package me.w70.bot.account;

public class Account {

    private String password = "";

    private String username = "";

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
