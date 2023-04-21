package org.japura.examples.decorator;

import java.awt.Component;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import org.japura.examples.AbstractExample;
import org.japura.gui.Anchor;
import org.japura.gui.Decorator;

public class Example2 extends AbstractExample {

    @Override
    protected Component buildExampleComponent() {
        Class<?> cls = Example2.class;
        URL urlChecked = cls.getResource("/resources/images/checked.png");
        JTextField field = new JTextField(11);
        Decorator decorator = new Decorator(field, Anchor.EAST);
        decorator.setMargin(new Insets(0, 0, 0, 7));
        decorator.addDecoration(new ImageIcon(urlChecked));
        return decorator;
    }

    public static void main(String args[]) {
        Example2 example = new Example2();
        example.runExample();
    }
}
