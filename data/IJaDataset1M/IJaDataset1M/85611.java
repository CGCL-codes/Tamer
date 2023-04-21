package org.upfrost.mapping.classes;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A Test class with no annotation on the properties. By default, the method accessors should be used.
 * 
 * @author Rodrigo Reyes
 *
 */
@Table(name = "test3")
public class TestClass3 {

    private int id;

    private String strValue;

    private Date someDate;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "when")
    public Date getSomeDate() {
        return someDate;
    }

    public void setSomeDate(Date someDate) {
        this.someDate = someDate;
    }

    @Column(name = "value")
    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TestClass3) {
            TestClass3 other = (TestClass3) obj;
            long t1 = this.someDate.getTime() / 1000;
            long t2 = other.someDate.getTime() / 1000;
            return other.id == this.id && other.strValue.equals(this.strValue) && t1 == t2;
        }
        return super.equals(obj);
    }
}
