package org.javathena.core.data;

public class Friend {

    private int account_id;

    private int char_id;

    private String name;

    public Friend(int account_id, int char_id, String name) {
        this.account_id = account_id;
        this.char_id = char_id;
        this.name = name;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getChar_id() {
        return char_id;
    }

    public void setChar_id(int char_id) {
        this.char_id = char_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
