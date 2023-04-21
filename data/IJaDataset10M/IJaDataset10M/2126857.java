package com.actionbazaar.persistence;

public class Notification {

    public Notification() {
    }

    public static void sendEmailAlert(Long categoryId, String categoryName, String firstName, String lastName) {
        String emailText = "Category with Id: " + categoryId + " with name : " + categoryName + " was created by : " + firstName + "  " + lastName;
        System.out.println(emailText);
    }
}
