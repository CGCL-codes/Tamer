package com.enoram.training1.register.shared;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is note translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

    public static boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        return name.length() > 3;
    }

    public static boolean checkPhone(String phone) {
        String validChars = "0123456789-";
        boolean validNumber = true;
        char character;
        int phoneLength = phone.length();
        int i = 0;
        if (phoneLength != 10) {
            validNumber = false;
        } else if (phoneLength == 10) {
            for (i = 0; i < phoneLength && validNumber == true; i++) {
                character = phone.charAt(i);
                if (validChars.indexOf(character) == -1) {
                    validNumber = false;
                }
            }
        }
        return validNumber;
    }

    public static boolean checkZip(String zip) {
        String validChars = "0123456789";
        boolean validNumber = true;
        char character;
        int zipLength = zip.length();
        int i = 0;
        if (zipLength != 5) {
            validNumber = false;
        } else if (zipLength == 5) {
            for (i = 0; i < zipLength && validNumber == true; i++) {
                character = zip.charAt(i);
                if (validChars.indexOf(character) == -1) {
                    validNumber = false;
                }
            }
        }
        return validNumber;
    }
}
