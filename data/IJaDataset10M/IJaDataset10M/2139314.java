package tuner3d.util.swing;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

public class RecentMenuItem extends JMenuItem {

    private String file, path, extension;

    public RecentMenuItem(String path, String extension, ActionListener al) {
        super(path);
        this.path = path;
        this.extension = extension;
        addActionListener(al);
    }

    public RecentMenuItem(String file, String path, String extension, ActionListener al) {
        super(file);
        this.file = file;
        this.path = path;
        this.extension = extension;
        addActionListener(al);
    }

    public String getPath() {
        return path;
    }

    public String getExtension() {
        return extension;
    }
}
