package org.mak.dominator;

import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.w3c.dom.Node;

/**
 * @author Marcel
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DateHandler implements Handler {

    private DateFormat dateFormat;

    public DateHandler() {
        super();
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.ENGLISH);
    }

    public boolean handlesClass(Class classType) {
        return Date.class.isAssignableFrom(classType);
    }

    public void marshall(DOMinator dominator, Object object, Node node) throws Exception {
        if (!(object instanceof Date)) throw new Exception(this.getClass().getName() + " error:" + object.getClass().getName() + " is not a Date.");
        Node value = dominator.createTextNode(dateFormat.format((Date) object));
        node.appendChild(value);
    }

    public Object unMarshall(DOMinator dominator, Node node, Class objectClass) throws Exception {
        if (!handlesClass(objectClass)) throw new Exception(this.getClass().getName() + " error:" + objectClass.getName() + " is not a Date.");
        String value = dominator.getNodeValue(node);
        if (objectClass.equals(Date.class)) {
            return dateFormat.parse(value);
        } else {
            Date date = dateFormat.parse(value);
            Constructor constructor = objectClass.getConstructor(new Class[] { long.class });
            Object object = constructor.newInstance(new Object[] { new Long(date.getTime()) });
            return object;
        }
    }
}
