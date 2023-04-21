package org.opensih.gdq.Utils.Converters;

public class Encoder {

    public static String encodeStr(String arg) {
        String salida = arg.toUpperCase().replaceAll("�", "&#xe1;").replaceAll("�", "&#xc1;").replaceAll("�", "&#xe9;").replaceAll("�", "&#xc9;").replaceAll("�", "&#xed;").replaceAll("�", "&#xcd;").replaceAll("�", "&#xf3;").replaceAll("�", "&#xd3;").replaceAll("�", "&#xfa;").replaceAll("�", "&#xda;").replaceAll("�", "&#xe4;").replaceAll("�", "&#xc4;").replaceAll("�", "&#xeb;").replaceAll("�", "&#xcb;").replaceAll("�", "&#xef;").replaceAll("�", "&#xcf;").replaceAll("�", "&#xf6;").replaceAll("�", "&#xd6;").replaceAll("�", "&#xfc;").replaceAll("�", "&#xdc;").replaceAll("�", "&#xe0;").replaceAll("�", "&#xc0;").replaceAll("�", "&#xe8;").replaceAll("�", "&#xc8;").replaceAll("�", "&#xec;").replaceAll("�", "&#xcc;").replaceAll("�", "&#xf2;").replaceAll("�", "&#xd2;").replaceAll("�", "&#xf9;").replaceAll("�", "&#xd9;").replaceAll("�", "&#xf1;").replaceAll("�", "&#xd1;").replaceAll("�", "&#xbf;").replaceAll("\\?", "&#x3f;").replaceAll("�", "&#xa1;").replaceAll("!", "&#x21;").replaceAll("�", "&#xb4;").replaceAll("`", "&#x60;").replaceAll("�", "&#xe7;").replaceAll("�", "&#xc7;");
        return salida;
    }

    public static String encodeStr2(String arg) {
        String salida = arg.replaceAll("�", "&#xe1;").replaceAll("�", "&#xc1;").replaceAll("�", "&#xe9;").replaceAll("�", "&#xc9;").replaceAll("�", "&#xed;").replaceAll("�", "&#xcd;").replaceAll("�", "&#xf3;").replaceAll("�", "&#xd3;").replaceAll("�", "&#xfa;").replaceAll("�", "&#xda;").replaceAll("�", "&#xe4;").replaceAll("�", "&#xc4;").replaceAll("�", "&#xeb;").replaceAll("�", "&#xcb;").replaceAll("�", "&#xef;").replaceAll("�", "&#xcf;").replaceAll("�", "&#xf6;").replaceAll("�", "&#xd6;").replaceAll("�", "&#xfc;").replaceAll("�", "&#xdc;").replaceAll("�", "&#xe0;").replaceAll("�", "&#xc0;").replaceAll("�", "&#xe8;").replaceAll("�", "&#xc8;").replaceAll("�", "&#xec;").replaceAll("�", "&#xcc;").replaceAll("�", "&#xf2;").replaceAll("�", "&#xd2;").replaceAll("�", "&#xf9;").replaceAll("�", "&#xd9;").replaceAll("�", "&#xf1;").replaceAll("�", "&#xd1;").replaceAll("�", "&#xbf;").replaceAll("\\?", "&#x3f;").replaceAll("�", "&#xa1;").replaceAll("!", "&#x21;").replaceAll("�", "&#xb4;").replaceAll("`", "&#x60;").replaceAll("�", "&#xe7;").replaceAll("�", "&#xc7;");
        return salida;
    }

    public static String decryptStr(String arg) {
        String salida = arg.replaceAll("&#xe1;", "�").replaceAll("&#xc1;", "�").replaceAll("&#xe9;", "�").replaceAll("&#xc9;", "�").replaceAll("&#xed;", "�").replaceAll("&#xcd;", "�").replaceAll("&#xf3;", "�").replaceAll("&#xd3;", "�").replaceAll("&#xfa;", "�").replaceAll("&#xda;", "�").replaceAll("&#xe4;", "�").replaceAll("&#xc4;", "�").replaceAll("&#xeb;", "�").replaceAll("&#xcb;", "�").replaceAll("&#xef;", "�").replaceAll("&#xcf;", "�").replaceAll("&#xf6;", "�").replaceAll("&#xd6;", "�").replaceAll("&#xfc;", "�").replaceAll("&#xdc;", "�").replaceAll("&#xe0;", "�").replaceAll("&#xc0;", "�").replaceAll("&#xe8;", "�").replaceAll("&#xc8;", "�").replaceAll("&#xec;", "�").replaceAll("&#xcc;", "�").replaceAll("&#xf2;", "�").replaceAll("&#xd2;", "�").replaceAll("&#xf9;", "�").replaceAll("&#xd9;", "�").replaceAll("&#xf1;", "�").replaceAll("&#xd1;", "�").replaceAll("&#xbf;", "�").replaceAll("&#x3f;", "\\?").replaceAll("&#xa1;", "�").replaceAll("&#x21;", "!").replaceAll("&#xb4;", "�").replaceAll("&#x60;", "`").replaceAll("&#xe7;", "�").replaceAll("&#xc7;", "�");
        return salida;
    }

    public static String parseXML(String s) {
        return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&#39;");
    }
}
