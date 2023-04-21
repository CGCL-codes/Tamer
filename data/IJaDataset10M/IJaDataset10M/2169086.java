package test;

import java.util.Map;
import org.zkoss.bind.BindComposer;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.bind.Form;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;

/**
 * @author henrichen
 *
 */
public class ZKBindSave1Composer extends BindComposer {

    private Person _p1;

    public ZKBindSave1Composer() {
        _p1 = new Person("Henri", "Chen");
        addConverter("fullName", new Converter() {

            public Object coerceToUi(Object val, Component component, BindContext ctx) {
                if (val instanceof Form) {
                    final Form bean = (Form) val;
                    return "" + bean.getField("firstName") + " " + bean.getField("lastName");
                } else {
                    final Person person = (Person) val;
                    return person.getFirstName() + " " + person.getLastName();
                }
            }

            public Object coerceToBean(Object val, Component component, BindContext ctx) {
                return val;
            }
        });
        addConverter("firstName", new Converter() {

            public Object coerceToUi(Object val, Component component, BindContext ctx) {
                return val;
            }

            public Object coerceToBean(Object val, Component component, BindContext ctx) {
                return val;
            }
        });
        addConverter("lastName", new Converter() {

            public Object coerceToUi(Object val, Component component, BindContext ctx) {
                return val;
            }

            public Object coerceToBean(Object val, Component component, BindContext ctx) {
                return val;
            }
        });
    }

    public Person getP1() {
        return _p1;
    }

    @NotifyChange
    public void setP1(Person p) {
        _p1 = p;
    }

    @Command
    @NotifyChange("p1")
    public void myCommand() {
        System.out.println("myCommand executed:");
    }

    private String _fieldName = "firstName";

    public void setFieldName(String fieldName) {
        _fieldName = fieldName;
    }

    public String getFieldName() {
        return _fieldName;
    }

    @NotifyChange("fieldName")
    @Command
    public void changeFieldName(@BindingParam("fieldName") String fieldName) {
        setFieldName(fieldName);
    }
}
