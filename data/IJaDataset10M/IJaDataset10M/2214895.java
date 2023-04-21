package edu.gsbme.MMLParser2.Validation.Naming;

import java.util.regex.Pattern;

public class CheckName {

    public static boolean LegalCharacters(String s) {
        return Pattern.matches("\\w+", s);
    }
}
