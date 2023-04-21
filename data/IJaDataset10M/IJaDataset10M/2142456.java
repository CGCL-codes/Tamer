package net.sf.locale4j.test.bundle;

import java.io.FileInputStream;
import net.sf.locale4j.bundle.Locale4jBundle;
import net.sf.locale4j.format.Tmx20;

/**
 * A ResourceBundle used for testing.
 */
public class MyBundle extends Locale4jBundle {

    /**
     * This method is responsible for setting up the LocaleStore object.
     */
    protected void loadBundle() {
        try {
            FileInputStream istream = new FileInputStream("src/test/resources/Tmx20Test.000.xml");
            Tmx20 tmx = new Tmx20();
            this.setStore(tmx.read(istream));
            istream.close();
        } catch (Exception e) {
            ;
        }
    }
}
