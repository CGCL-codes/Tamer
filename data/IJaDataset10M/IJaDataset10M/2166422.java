package org.cubicunit.internal.selenium;

import java.util.Map;
import org.cubicunit.Form;

public class SeleniumForm extends SeleniumElementContainer implements Form {

    public SeleniumForm(CubicSelenium selenium, String id, Map<String, Object> properties) {
        super(selenium, id, properties);
    }
}
