package com.octane.dyna;

import java.lang.reflect.Method;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DynamicMain {

    public static final Object createPDFWindowInstance(final Object obj) throws Exception {
        System.out.println("Service Class Loader.3: " + obj.getClass().getClassLoader());
        final Class winClass = Class.forName("com.octane.dyna.SecondDynamicMain");
        return winClass.newInstance();
    }

    public static void main(final String[] args) throws Exception {
        System.out.println("Running Dynamic Main");
        new Shell();
        Object o = createPDFWindowInstance(new DynamicMain());
        final Class invoked_class = Class.forName("com.octane.dyna.SecondDynamicMain");
        final Method main = invoked_class.getDeclaredMethod("main2", new Class[0]);
        main.invoke(o, new Object[0]);
        System.out.println("Done");
    }
}
