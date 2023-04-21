package org.eclipse.swt.examples.helloworld;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import java.util.ResourceBundle;

public class HelloWorld2 {

    private static ResourceBundle resHello = ResourceBundle.getBundle("examples_helloworld");

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new HelloWorld2().open(display);
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    public Shell open(Display display) {
        Shell shell = new Shell(display);
        Label label = new Label(shell, SWT.CENTER);
        label.setText(resHello.getString("Hello_world"));
        label.setBounds(shell.getClientArea());
        shell.open();
        return shell;
    }
}
