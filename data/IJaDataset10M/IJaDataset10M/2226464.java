package foo.bar;

import java.util.Date;
import java.util.Enumeration;

/** DateEnumeration for Java 1.3 */
public class DateEnumeration {

    Enumeration iEnumeration;

    public DateEnumeration(Enumeration rEnumeration) {
        iEnumeration = rEnumeration;
    }

    public boolean hasMoreElements() {
        return iEnumeration.hasMoreElements();
    }

    public Date nextElement() {
        Object object = iEnumeration.nextElement();
        Date object_Date = (Date) object;
        Date value_Date = object_Date;
        return value_Date;
    }

    public Enumeration getEnumeration() {
        return iEnumeration;
    }
}
