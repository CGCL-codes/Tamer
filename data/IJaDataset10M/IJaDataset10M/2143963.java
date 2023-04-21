package br.com.srv.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static String getDirecao(String dir) {
        String direcao = "";
        if (dir.equals("0")) {
            direcao = "Norte";
        } else if (dir.equals("1")) {
            direcao = "Nordeste";
        } else if (dir.equals("2")) {
            direcao = "Leste";
        } else if (dir.equals("3")) {
            direcao = "Sudeste";
        } else if (dir.equals("4")) {
            direcao = "Sul";
        } else if (dir.equals("5")) {
            direcao = "Sudoeste";
        } else if (dir.equals("6")) {
            direcao = "Oeste";
        } else if (dir.equals("7")) {
            direcao = "Noroeste";
        } else {
            direcao = "Qualquer dire��o";
        }
        return direcao;
    }

    public static boolean isCommand(String command, String text) {
        String expression = "\\w*" + command + "\\S*";
        Pattern p = Pattern.compile(expression);
        Matcher m = p.matcher(text);
        return m.matches();
    }

    public static boolean isCommand(List<String> listaComandos, String param) {
        for (String comando : listaComandos) {
            if (isCommand(comando, param)) {
                return true;
            }
        }
        return false;
    }

    /**
	  * M�todo respons�vel por receber uma String e devolve-la criptografada.
	  * @param password
	  * @return Senha criptografada
	  */
    public static String encrypt(String password) {
        String sign = password;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(sign.getBytes());
            byte[] hash = md.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) hexString.append("0" + Integer.toHexString((0xFF & hash[i]))); else hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
            sign = hexString.toString();
        } catch (Exception nsae) {
            nsae.printStackTrace();
        }
        return sign;
    }
}
