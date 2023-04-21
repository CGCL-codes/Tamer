package com.rwoar.moo.client.utils;

import java.awt.Color;
import java.awt.Font;

public class Parser {

    public static Color parseColor(String paramString, Color paramColor) {
        try {
            if (paramString.startsWith("#")) paramString = paramString.substring(1); else if (paramString.startsWith("0x")) paramString = paramString.substring(2);
            return new Color(Integer.parseInt(paramString, 16));
        } catch (Exception localException) {
        }
        return paramColor;
    }

    public static Font parseFont(String paramString, Font paramFont1) {
        if (paramString == null) return paramFont1;
        int j = paramFont1.getSize();
        int k = paramFont1.getStyle();
        int i;
        try {
            if ((i = paramString.lastIndexOf('.')) != -1) {
                j = Integer.parseInt(paramString.substring(i + 1));
                paramString = paramString.substring(0, i);
            }
        } catch (Exception localException1) {
        }
        try {
            if ((i = paramString.lastIndexOf('-')) != -1) {
                String str = paramString.substring(i + 1);
                if (str.equalsIgnoreCase("bold")) k = 1; else if (str.equalsIgnoreCase("italic")) k = 2; else if (str.equalsIgnoreCase("bolditalic")) k = 3; else k = 0;
                paramString = paramString.substring(0, i);
            }
        } catch (Exception localException2) {
        }
        if (paramString.equals("")) paramString = paramFont1.getName();
        return new Font(paramString, k, j);
    }
}
