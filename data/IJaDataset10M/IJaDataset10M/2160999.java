package com.darwinit.regexptester;

import java.io.Console;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexTestHarness {

    public static void pl(String text) {
        System.out.println(text);
    }

    public static void match(String regex, String searchString) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(searchString);
        boolean found = false;
        int i = 0;
        while (matcher.find()) {
            pl(i + ": I found the text \"" + matcher.group() + "\" starting at " + "index " + matcher.start() + " and ending at index " + matcher.end() + "");
            found = true;
            i++;
        }
        if (!found) {
            pl("No match found for \"" + regex + "\", in: \"" + searchString + "\"");
        }
    }

    public static void main(String[] args) {
        String regex = "foo";
        String searchString = "foo ";
        RegexTestHarness.match("foo", "foo");
        RegexTestHarness.match("[bcr]at", "bat");
        RegexTestHarness.match("[bcr]at", "cat");
        RegexTestHarness.match("[bcr]at", "rat");
        RegexTestHarness.match("[bcr]at", "hat");
        regex = "[^bcr]at";
        RegexTestHarness.pl(regex);
        RegexTestHarness.match(regex, "bat");
        RegexTestHarness.match(regex, "cat");
        RegexTestHarness.match(regex, "rat");
        RegexTestHarness.match(regex, "hat");
        regex = "[0-4[6-8]]";
        RegexTestHarness.pl(regex);
        RegexTestHarness.match(regex, "0");
        RegexTestHarness.match(regex, "5");
        RegexTestHarness.match(regex, "6");
        RegexTestHarness.match(regex, "8");
        RegexTestHarness.match(regex, "9");
        regex = "[[0-4][6-8]]";
        RegexTestHarness.pl(regex);
        RegexTestHarness.match(regex, "0");
        RegexTestHarness.match(regex, "5");
        RegexTestHarness.match(regex, "6");
        RegexTestHarness.match(regex, "8");
        RegexTestHarness.match(regex, "9");
    }
}
