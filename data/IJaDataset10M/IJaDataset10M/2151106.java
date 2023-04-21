package com.peterhi.util;

public class TestConsole {

    public static void main(String[] args) throws Exception {
        AccountTable at = new AccountTable();
        at.add(new Account("account1", "password1", 1, true, "account1@gmail.com"));
        at.add(new Account("account2", "password2", 2, false, "account2@gmail.com"));
        at.add(new Account("account3", "password3", 3, true, "account3@hotmail.com"));
        at.add(new Account("account4", "password4", 4, false, "account4@gmail.com"));
        at.add(new Account("account5", "password5", 5, true, "account5@gmail.com"));
        System.out.println("Hello World!");
        String text = Console.toString(at, false);
        System.out.println(text);
    }
}
