package co.fxl.gui.input.test;

import java.lang.reflect.InvocationTargetException;
import co.fxl.gui.api.IDisplay;
import co.fxl.gui.rtf.api.IRTFWidget;
import co.fxl.gui.rtf.api.IRTFWidget.IComposite;
import co.fxl.gui.rtf.impl.RTFWidgetImplProvider;

public class RTFWidgetTest {

    public void run(IDisplay display) {
        display.register(new RTFWidgetImplProvider());
        IRTFWidget w = (IRTFWidget) display.container().widget(IRTFWidget.class);
        w.addToken("#", "{#}");
        w.addToken("name", "{name}");
        IComposite c = w.addComposite();
        IComposite rc = c.addComposite("ReleaseCycle");
        rc.addComposite("id").token("ReleaseCycle.id");
        rc.addComposite("version").token("ReleaseCycle.version");
        IComposite rf = c.addComposite("ReleaseFolder");
        rf.addComposite("id").token("ReleaseFolder.id");
        rf.addComposite("version").token("ReleaseFolder.version");
        w.visible(true);
        display.visible(true);
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
        Class<?> clazz = Class.forName("co.fxl.gui.swing.SwingDisplay");
        IDisplay display = (IDisplay) clazz.getMethod("instance", new Class<?>[0]).invoke(null, new Object[0]);
        new RTFWidgetTest().run(display);
    }
}
