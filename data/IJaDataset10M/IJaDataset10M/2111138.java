package editorMode;

import gui.Mode;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class EditorMode extends Mode {

    private JTextArea area;

    public static final String TITLE = "Editor Mode";

    public EditorMode() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(makeMain());
    }

    private JComponent makeMain() {
        area = new JTextArea();
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        JScrollPane scrolly = new JScrollPane(area, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrolly.setBorder(BorderFactory.createEmptyBorder());
        return scrolly;
    }

    public void startMode() {
    }

    public void endMode() {
    }

    public String getTitle() {
        return TITLE;
    }

    public void createNew() {
        area.setText("");
    }

    public void open(String s) {
        area.setText(s);
    }

    public void save(File f) {
        PrintWriter pw = null;
        try {
            FileOutputStream fos = new FileOutputStream(f);
            pw = new PrintWriter(fos);
            pw.append(area.getText());
        } catch (IOException e) {
            System.err.println("There was an error opening the input file!");
        } finally {
            if (pw != null) pw.close();
        }
    }

    public String getEditorText() {
        return area.getText();
    }
}
