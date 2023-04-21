package be.khleuven.KevinVranken.vgo;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class internFrameMetAndereVerwijderActie extends interneFrames {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Override
    public void verwijderOnderdeel(JList lijst, DefaultListModel listData, JButton verwijderenKnop) {
        int response = JOptionPane.showConfirmDialog(null, "Bent u zeker dat u deze naam wil verwijderen?", "Een naam verwijderen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
        } else if (response == JOptionPane.YES_OPTION) {
            super.verwijderOnderdeel(lijst, listData, verwijderenKnop);
        } else if (response == JOptionPane.CLOSED_OPTION) {
        }
    }

    public static void main(String args[]) {
        new internFrameMetAndereVerwijderActie();
    }
}
