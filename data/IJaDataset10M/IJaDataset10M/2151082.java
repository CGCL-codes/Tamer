package test;

import javax.swing.JFrame;
import org.jdesktop.application.Application;

public class TestEditFrame extends Application {

    private JFrame appFrame = null;

    @Override
    protected void startup() {
    }

    public static void main(String[] args) {
        Application.launch(TestEditFrame.class, args);
    }
}
