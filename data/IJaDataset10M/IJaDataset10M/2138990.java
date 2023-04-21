package gui;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import audio.*;
import java.sql.SQLException;

public class WindowManager {

    private Display display;

    private MainWindow main;

    public WindowManager() {
        display = new Display();
    }

    public void openMain() throws SQLException {
        main = new MainWindow(display);
    }

    public void finalize() {
        display.dispose();
    }
}
