package swing;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.progeeks.meta.*;
import org.progeeks.meta.beans.*;
import org.progeeks.meta.swing.*;
import org.progeeks.util.log.Log;

/**
 *  An example of using Meta-JB to create simple screens
 *  for viewing and editing an object.  This example bypasses
 *  some of the framework to show a little bit about what goes
 *  on behind the scenes.
 *
 *  @version   $Revision: 1.7 $
 *  @author    Paul Speed
 */
public class Demo1 {

    public static void main(String[] args) throws IntrospectionException {
        Log.initialize();
        MetaClass personClass = BeanUtils.createBeanMetaClass(Person.class);
        Person person = new Person();
        MetaObject obj = new BeanMetaObject(person, personClass);
        obj.setProperty("name", "John Doe");
        obj.setProperty("age", new Integer(42));
        List fields = new ArrayList();
        fields.add("name");
        fields.add("age");
        fields.add("occupation");
        fields.add("geekQuotient");
        JFrame editFrame = new JFrame("Edit Demo 1");
        MetaPropertyContext viewContext = new MetaPropertyContext(null, null, true);
        MultiColumnPanel editPanel = new MultiColumnPanel(viewContext, obj, fields);
        editFrame.getContentPane().add(editPanel, "Center");
        editFrame.setSize(250, 200);
        editFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editFrame.setVisible(true);
        JFrame viewFrame = new JFrame("View Demo 1");
        viewContext = new MetaPropertyContext(null, null, false);
        MultiColumnPanel viewPanel = new MultiColumnPanel(viewContext, obj, fields);
        viewFrame.getContentPane().add(viewPanel, "Center");
        viewFrame.setSize(250, 200);
        viewFrame.setLocation(260, 0);
        viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewFrame.setVisible(true);
    }
}
