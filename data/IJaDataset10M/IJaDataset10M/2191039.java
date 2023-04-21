package capoeira.berimbau.tab;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import capoeira.berimbau.tab.note.Note;
import capoeira.berimbau.tab.note.NoteFactory;

public class NoteHelpDialog extends JDialog {

    public NoteHelpDialog(NoteSheet sheet, String title) {
        super(UIUtilities.getFrame(sheet), title);
        Vector notes = new Vector();
        notes.add(new Note(Note.SAMPLE_BUZZ, true));
        notes.add(new Note(Note.SAMPLE_LOW, true));
        notes.add(new Note(Note.SAMPLE_HIGH, true));
        notes.add(new Note(Note.SAMPLE_HAMMER, true));
        JPanel noteListPanel = new JPanel();
        noteListPanel.setBackground(Color.WHITE);
        noteListPanel.setLayout(null);
        int offset = 10;
        for (int i = 0; i < notes.size(); i++) {
            Note note = (Note) notes.get(i);
            noteListPanel.add(note);
            Dimension d = note.getPreferredSize();
            note.setBounds(offset, offset + i * (offset + d.height), d.width, d.height);
        }
        noteListPanel.setPreferredSize(new Dimension((NoteFactory.WIDTH * 2) + 2 * offset, (NoteFactory.HEIGHT + offset) * notes.size()));
        this.setContentPane(new JScrollPane(noteListPanel));
    }
}
