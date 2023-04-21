package controllers;

import java.util.Iterator;
import play.data.validation.Validation;
import play.mvc.Before;

public class AntiCSRFCheck extends AntiCSRF {

    @Before
    public static void checkCSRF() {
        Iterator<String> iterator = AntiCSRF.tokens.keySet().iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (params.all().containsKey(key)) {
                if (params.get(key).equals(tokens.get(key))) {
                    found = true;
                    tokens.remove(key);
                    break;
                }
            }
        }
        if (!found) {
            validation.addError("ALL", "CSRF!");
        }
    }
}
