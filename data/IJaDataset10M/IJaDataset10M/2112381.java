package org.libreplan.web.common;

import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.impl.InputElement;

/**
 * Class for checking if a component is completely valid (checks all constraints
 * within a component)
 *
 * @author Diego Pino García <dpino@igalia.com>
 * @author Manuel Rego Casasnovas <rego@igalia.com>
 */
public class ConstraintChecker {

    @SuppressWarnings("unchecked")
    public static void isValid(Component component) {
        checkIsValid(component);
        checkIsValid(component.getChildren());
    }

    private static void checkIsValid(List<Component> components) {
        for (Component component : components) {
            isValid(component);
        }
    }

    private static void checkIsValid(Component child) {
        if (child instanceof InputElement) {
            inputIsValid((InputElement) child);
        }
    }

    private static void inputIsValid(InputElement input) {
        if (!input.isValid()) {
            input.getText();
        }
    }
}
