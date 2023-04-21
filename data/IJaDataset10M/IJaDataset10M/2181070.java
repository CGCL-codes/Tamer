package util;

public class LinkerUtil {

    public static String[] parseOptionFile(String fname) throws java.io.IOException {
        java.util.Vector v = new java.util.Vector();
        java.io.StreamTokenizer in;
        in = new java.io.StreamTokenizer(new java.io.BufferedInputStream(new java.io.FileInputStream(fname)));
        in.resetSyntax();
        in.eolIsSignificant(false);
        in.whitespaceChars(0, 0x20);
        in.wordChars('!', '~');
        in.commentChar('#');
        while (in.nextToken() != java.io.StreamTokenizer.TT_EOF) {
            v.addElement(in.sval);
        }
        int n = v.size();
        String olist[] = new String[n];
        v.copyInto(olist);
        return olist;
    }

    public static final String mainName = "main";

    public static final String mainSig = "([Ljava/lang/String;)V";

    public static final String constructorName = "<init>";

    public static final String constructorSig = "()V";

    public static final String staticInitializerName = "<clinit>";

    public static final String staticInitializerSig = "()V";

    public static String sanitizeClassname(String classname) {
        return classname.replace('.', '/').intern();
    }

    public static int sigOff(String n) {
        return n.indexOf('(');
    }

    public static int methodOff(String n) {
        int moff = n.lastIndexOf('.');
        if (moff >= 0) return moff;
        int ending = n.indexOf('(');
        if (ending < 0) ending = n.length();
        moff = n.lastIndexOf('/', ending);
        return moff;
    }
}
