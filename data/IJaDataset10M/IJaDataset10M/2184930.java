package com.wwwc.util.web;

import java.io.*;
import java.text.*;
import java.util.*;

public class MyImageResize {

    public static String smaller(String image, int nw, int nh) {
        String default_image = "<img src=/index/images/apps/image_not_found.jpg";
        String ow = null;
        String oh = null;
        if (image == null || image.length() < 1 || image.indexOf("<") == -1 || image.indexOf("width=") == -1 || image.indexOf(">") == -1) {
            return default_image + " width=" + nw + " height=" + nh + "><ERROR name=ImageResize code=101></ERROR>";
        }
        int wp = image.indexOf("width=");
        int hp = image.indexOf("height=");
        ow = image.substring(wp + 6, hp - 1);
        oh = image.substring(hp + 7);
        if (ow.indexOf(">") != -1) {
            ow = ow.substring(0, ow.indexOf(">"));
        }
        if (oh.indexOf(">") != -1) {
            oh = oh.substring(0, oh.indexOf(">"));
        }
        if (ow.indexOf(".") != -1) {
            ow = ow.substring(0, ow.indexOf("."));
        }
        if (oh.indexOf(".") != -1) {
            oh = oh.substring(0, oh.indexOf("."));
        }
        int owi = Integer.parseInt(ow);
        int ohi = Integer.parseInt(oh);
        if (owi > nw || ohi > nh) {
            while (owi > nw || ohi > nh) {
                owi = owi - owi / 10;
                ohi = ohi - ohi / 10;
            }
        }
        default_image = image.substring(0, wp) + " width=" + owi + " height=" + ohi + ">";
        return default_image;
    }
}
