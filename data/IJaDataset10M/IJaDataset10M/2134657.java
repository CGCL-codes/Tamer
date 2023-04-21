package net.sourceforge.domian.test.factoryclasses;

import java.util.Date;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * A rather public case...
 *
 * @author Eirik Torske
 */
public class SomeSetterOnlyClass {

    private String field1;

    private Long field2;

    private Date field3;

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public Long getField2() {
        return field2;
    }

    public void setField2(Long field2) {
        this.field2 = field2;
    }

    public Date getField3() {
        return field3;
    }

    public void setField3(Date field3) {
        this.field3 = field3;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }
}
