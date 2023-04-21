package com.narirelays.ems.utils;

public class MyUUIDGen {

    public static String getUUID() {
        return java.util.UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] argv) {
        String dep_id = "DEPT::0";
        int comma = dep_id.lastIndexOf("::");
        if (comma > 0) {
            dep_id = dep_id.substring(comma + 2);
        }
        System.out.println(dep_id);
        String media_id = "MEDIAHIER::MEDIA::MEDIA_11";
        comma = media_id.lastIndexOf("_");
        if (comma > 0) {
            media_id = media_id.substring(comma + 1);
        }
        System.out.println(media_id);
    }
}
