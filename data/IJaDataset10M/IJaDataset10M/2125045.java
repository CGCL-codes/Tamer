package com.flash.system.util;

import java.util.Random;

public class RandomString {

    String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

    String[] emailHost = { "@gmail.com", "@yahoo.com", "@sampathzone.com", "@hotmail.com" };

    String[] webHost = { ".com", ".lk", ".net", ".edu", ".org" };

    Random rand = new Random();

    public String getRandomString(int size) {
        String randString = "";
        for (int i = 0; i < size; i++) {
            if (i == 0) randString += alphabet[rand.nextInt(24)].toUpperCase(); else randString += alphabet[rand.nextInt(24)];
        }
        return randString;
    }

    public String getRandomEmail(int size) {
        String randEmail = "";
        for (int i = 0; i < size; i++) {
            randEmail += alphabet[rand.nextInt(24)];
        }
        randEmail += emailHost[rand.nextInt(4)];
        return randEmail;
    }

    public String getRandomUrl(int size) {
        String randUrl = "www.";
        for (int i = 0; i < size; i++) {
            randUrl += alphabet[rand.nextInt(24)];
        }
        randUrl += webHost[rand.nextInt(5)];
        return randUrl;
    }
}
