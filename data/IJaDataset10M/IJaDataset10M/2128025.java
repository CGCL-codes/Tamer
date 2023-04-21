package uk.org.sgj.YAT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import uk.org.sgj.SGJNifty.Icons;

class EditingControlPanel extends JPanel implements ActionListener {

    JButton delete, addnew;

    private VocabChapterTableViewingPanel chapterTableViewingPanel;

    EditingControlPanel(VocabChapterTableViewingPanel chapterTableViewingPanel) {
        this.chapterTableViewingPanel = chapterTableViewingPanel;
        delete = Icons.addJButton("icons/edittrash1.png", "Deletes the selected words.", KeyEvent.VK_X, "cut", this, YAT.class);
        addnew = Icons.addJButton("icons/newChapter.png", "Creates a new word.", KeyEvent.VK_N, "addnew", this, YAT.class);
        add(addnew);
        add(delete);
        setLayout(new FlowLayout());
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("cut")) {
            this.chapterTableViewingPanel.deleteSelected();
        } else if (command.equals("addnew")) {
            this.chapterTableViewingPanel.addNewWord();
        }
    }
}
