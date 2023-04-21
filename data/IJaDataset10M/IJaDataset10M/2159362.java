package rbsla.rmltoprova;

/**
 * RuleMLtoProva
 * 
 * Translates RuleML 0.8x - 0.86 and RBSLA 0.01 files into Prova scripts
 * 
 * @author <A HREF="mailto:adrian.paschke@gmx.de">Adrian Paschke</A>
 * @version 0.1 <1 Feb 2005>
 * @since 0.1
 * @deprecated Since RuleML 0.87 and RBSLA 0.1 XSLT Stylesheets are provided for translation
 */
public class RuleMLtoProva {

    public static void main(String[] argv) {
        if (argv.length != 2) {
            System.out.println("RuleMLtoProva - please specify an input RuleML 0.8x to 0.86 or RBSLA 0.01 file and a Prova output file for translation.");
            return;
        }
        Convertor test = new Convertor(argv[0], argv[1]);
        test.output();
    }
}
