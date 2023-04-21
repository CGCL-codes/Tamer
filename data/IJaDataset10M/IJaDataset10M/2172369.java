package ch.unibas.jmeter.snmp.expect.control;

import java.beans.PropertyDescriptor;
import org.apache.jmeter.testbeans.BeanInfoSupport;

public class ExpectControllerBeanInfo extends BeanInfoSupport {

    public ExpectControllerBeanInfo() {
        super(ExpectController.class);
        PropertyDescriptor p;
        createPropertyGroup("ExpectConfig", new String[] { "executable" });
        p = property("executable");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");
    }
}
