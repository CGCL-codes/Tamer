package tests;

import com.jsoft.linkbuild.listenerAndServerLibrary.*;

/**
 *
 * @author sbrandollo
 */
public class RegistrationTest {

    RegistrationManager rm = RegistrationManager.getInstance();

    Rule r;

    public RegistrationTest() {
        String a[] = { "Ciao", "pippo", "smacco" };
        r = new Rule();
        r.setFields(a);
    }

    public static void main(String[] args) {
        new RegistrationTest();
    }
}

class Rule implements RegistrationRule {

    String[] fields;

    int max_fields = 2;

    public boolean checkLength(String[] fields) {
        return (fields.length == max_fields);
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public boolean acceptRegistration() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean acceptRegistration(String address, String[] remote_fields) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
