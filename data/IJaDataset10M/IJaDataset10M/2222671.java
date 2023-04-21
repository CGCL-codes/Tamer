package ru.whale.ide;

import ru.whale.ide.editor.EditorModel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;

public class TreePanel extends JPanel {

    EditorModel myModel = null;

    public TreePanel(EditorModel model) {
        myModel = model;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        traverse("./whale_src/");
    }

    public void traverse(String path) {
        FilenameFilter list = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".whale");
            }
        };
        for (File file : (new File(path)).listFiles(list)) {
            final String name = file.getName();
            add(new JButton(new AbstractAction(name) {

                public void actionPerformed(ActionEvent e) {
                    IO.fileName = "./whale_src/" + name;
                    myModel.setText(IO.read());
                    myModel.fireResultUpdate();
                }
            }));
        }
    }

    public JPanel getPanel(EditorModel model) {
        return new TreePanel(model);
    }
}
