package be.vanvlerken.bert.logmonitor.actions;

import javax.swing.JFileChooser;

/**
 * Shows a File chooser dialog for Database files
 */
public class DatabaseFileChooser extends JFileChooser {

    private static DatabaseFileChooser instance = null;

    protected DatabaseFileChooser() {
        super(System.getProperty("user.dir"));
        DatabaseFileFilter filter = new DatabaseFileFilter();
        setFileFilter(filter);
    }

    public static DatabaseFileChooser getInstance() {
        if (instance == null) {
            instance = new DatabaseFileChooser();
        }
        return instance;
    }
}
