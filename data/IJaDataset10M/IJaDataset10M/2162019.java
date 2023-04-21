package ch.ethz.dcg.spamato.filter.razor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import ch.ethz.dcg.spamato.base.common.util.hash.HashSHA1;
import ch.ethz.dcg.spamato.filter.razor.RazorMail.Part;
import ch.ethz.dcg.spamato.filter.razor.config.RazorConfiguration;
import ch.ethz.dcg.spamato.filter.razor.config.RazorConstants;
import ch.ethz.dcg.spamato.filter.razor.hash.HexToBase64Converter;

/**
 * @author simon
 */
public class RazorCommandoGenerator {

    protected static final char X36CHAR = (char) Integer.decode("0x36").intValue();

    protected static final char X5CCHAR = (char) Integer.decode("0x5C").intValue();

    protected static final StringBuffer X36ARRAY;

    protected static final StringBuffer X5CARRAY;

    static {
        StringBuffer toFill;
        for (toFill = new StringBuffer(); toFill.length() < 64; ) toFill.append(X36CHAR);
        X36ARRAY = toFill;
        for (toFill = new StringBuffer(); toFill.length() < 64; ) toFill.append(X5CCHAR);
        X5CARRAY = toFill;
    }

    public static String getClientIdentificationString() {
        RazorConfiguration razorConfig = RazorConfiguration.getInstance();
        return "cn=" + RazorConstants.RAZOR_VERSION_NAME + "&cv=" + RazorConstants.RAZOR_VERSION_NUMBER;
    }

    /**
	 * @return
	 */
    private static Object getRegistrarString() {
        return "registrar=" + RazorConstants.RAZOR_REGISTRAR_NAME_AND_VERSION;
    }

    public static String getStateRequestString() {
        return "a=g&pm=state";
    }

    public static String getGoodbyeMessage() {
        return "a=q";
    }

    /**
	 * @param username
	 * @param password
	 * @return
	 */
    public static String getLoginRequestString(String username, String password) {
        if (username == null || password == null) throw new IllegalArgumentException("The provided username and password must not be null. Provided Values: username: " + String.valueOf(username) + " password: " + String.valueOf(password));
        StringBuffer loginString = new StringBuffer("a=ai&user=");
        loginString.append(escapeString(username));
        loginString.append("&");
        loginString.append(getClientIdentificationString());
        return loginString.toString();
    }

    protected static String escapeString(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8").toLowerCase();
        } catch (UnsupportedEncodingException usee) {
            usee.printStackTrace();
            return string.replaceAll("[@:/]", "");
        }
    }

    public static String getCheckSingleString(String hashkey, String engine) {
        return "a=c&s=" + hashkey + "&e=" + engine;
    }

    /**
	 * @param password
	 * @param challenge
	 * @return
	 */
    public static String calculateChallengeAnswer(String password, String challenge) {
        StringBuffer iv1 = X36ARRAY;
        StringBuffer iv2 = X5CARRAY;
        StringBuffer iv1xored = new StringBuffer();
        for (int i = 0; i < password.length(); i++) iv1xored.append((char) (password.charAt(i) ^ iv1.charAt(i)));
        StringBuffer iv2xored = new StringBuffer();
        for (int i = 0; i < password.length(); i++) iv2xored.append((char) (password.charAt(i) ^ iv2.charAt(i)));
        HashSHA1 sha1 = new HashSHA1();
        sha1.init();
        sha1.updateASCII(iv2xored.toString());
        sha1.updateASCII(challenge);
        String digest = sha1.digout();
        sha1.init();
        sha1.updateASCII(iv1xored.toString());
        sha1.updateASCII(digest);
        digest = sha1.digout();
        String auth = HexToBase64Converter.hexToBase64(digest);
        return auth;
    }

    /**
	 * @param challengeAnswer
	 * @return
	 */
    public static String getChallengeAnswerString(String challengeAnswer) {
        return "a=auth&aresp=" + challengeAnswer;
    }

    /**
	 * @return
	 */
    public static String getReportFullMailActionString() {
        return "r";
    }

    /**
	 * @return
	 */
    public static String getRevokeFullMailActionString() {
        return "revoke";
    }

    public static StringBuffer getNominateFullMailMessage(String nominateAction, RazorMail mail, int bqs) {
        StringBuffer nominateString = new StringBuffer("-a=" + nominateAction + "&message=*\n");
        StringBuffer line = new StringBuffer();
        line.append(mail.getHeaders());
        for (int partIndex = 0; partIndex < mail.getPartSize(); partIndex++) {
            Part part = mail.getPart(partIndex);
            String body = part.getBody();
            if (body.length() + line.length() >= (bqs * 1024)) {
                break;
            }
            line.append("\n");
            line.append(body);
        }
        nominateString.append(line);
        nominateString.append("\n.\n");
        return nominateString;
    }

    public static String getMultiCheckString(String[] hashkeys, String[] engines) {
        StringBuffer query = new StringBuffer("-a=c&s=?&e=?\n");
        for (int i = 0; i < engines.length; i++) {
            query.append(hashkeys[i] + "," + engines[i] + "\n");
        }
        query.append(".");
        return query.toString();
    }

    /**
	 * @param engines
	 * @param sigs
	 * @return
	 */
    public static String getNominateSigsString(String[] engines, String[] sigs, String nominationMode) {
        if (engines == null || sigs == null || engines.length != sigs.length) {
            return null;
        }
        StringBuffer query;
        if (sigs.length > 1) {
            query = new StringBuffer("-");
        } else {
            query = new StringBuffer();
        }
        for (int i = 0; i < sigs.length; i++) {
            query.append("a=" + nominationMode + "&e=" + engines[i] + "&s=" + sigs[i] + "\n");
        }
        String reportString;
        if (sigs.length > 1) {
            query.append(".");
            reportString = query.toString();
        } else {
            reportString = query.toString().trim();
        }
        return reportString;
    }

    /**
	 * @param username
	 * @param password
	 * @return
	 */
    public static String getRegisterString(String username, String password) {
        StringBuffer regString = new StringBuffer("a=reg&user=");
        regString.append(escapeString(username));
        regString.append("&pass=");
        regString.append(password);
        regString.append("&");
        regString.append(getRegistrarString());
        return regString.toString();
    }
}
