package org.javalid.examples.core.example02;

import java.util.List;
import org.javalid.core.AnnotationValidator;
import org.javalid.core.AnnotationValidatorImpl;
import org.javalid.core.ValidationMessage;

public class Test {

    public Test() {
    }

    public static void main(String[] args) {
        AnnotationValidator validator = null;
        List<ValidationMessage> messages = null;
        Employee emp = null;
        validator = new AnnotationValidatorImpl();
        emp = new Employee();
        emp.setFirstName("Pete");
        emp.setLastName("Unknown");
        messages = validator.validateObject(emp, "create");
        System.out.println("Employee errors(0)=" + messages.size());
        messages = validator.validateObject(emp, "edit");
        System.out.println("Employee errors(1)=" + messages.size());
        System.out.println("Message=" + messages.get(0));
        emp.setLastName("a");
        messages = validator.validateObject(emp, "create");
        System.out.println("Employee errors(1)=" + messages.size());
        System.out.println("Message=" + messages.get(0));
        messages = validator.validateObject(emp, "edit");
        System.out.println("Employee errors(2)=" + messages.size());
        System.out.println("Messages=" + messages);
    }
}
