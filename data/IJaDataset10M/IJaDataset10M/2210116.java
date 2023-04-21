package test;

import com.steadystate.css.parser.CSSOMParser;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

/**
 *
 * @author  David Schweinsberg
 * @version $Release$
 */
public class TestSerializable {

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        try {
            Reader r = new FileReader("c:\\working\\css2parser\\stylesheets\\html40.css");
            CSSOMParser parser = new CSSOMParser();
            InputSource is = new InputSource(r);
            CSSStyleSheet stylesheet = parser.parseStyleSheet(is);
            FileOutputStream fo = new FileOutputStream("c:\\working\\css2parser\\stylesheets\\tmp");
            ObjectOutput oo = new ObjectOutputStream(fo);
            oo.writeObject(stylesheet);
            oo.flush();
            FileInputStream fi = new FileInputStream("c:\\working\\css2parser\\stylesheets\\tmp");
            ObjectInput oi = new ObjectInputStream(fi);
            CSSStyleSheet stylesheet2 = (CSSStyleSheet) oi.readObject();
            CSSRuleList rules = stylesheet2.getCssRules();
            for (int i = 0; i < rules.getLength(); i++) {
                CSSRule rule = rules.item(i);
                System.out.println(rule.getCssText());
            }
        } catch (Exception e) {
            System.out.println("Error.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
