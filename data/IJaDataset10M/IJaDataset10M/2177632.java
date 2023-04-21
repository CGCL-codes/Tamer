package outils;

public class ConvMinMaj {

    public String Change(String s) {
        char[] tabc = s.toCharArray();
        char[] newTabc = new char[s.length()];
        String ss = "";
        for (int i = 0; i < s.length(); i++) {
            char c = tabc[i];
            if (((int) c > 122) || ((int) c < 65) || (((int) c > 90) && ((int) c < 97))) {
                ss = ss + c;
            } else if ((int) c < 91) {
                newTabc[i] = (char) ((int) c + 32);
                ss = ss + newTabc[i];
            } else {
            }
        }
        return ss;
    }
}
