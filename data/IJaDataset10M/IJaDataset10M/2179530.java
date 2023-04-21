package galaxiia.ui.dialogues;

import java.awt.*;
import javax.swing.*;

public class Dialogue extends JDialog {

    private static final long serialVersionUID = 1L;

    Dialogue(Frame frame, String titre, Dimension dimensions, boolean modale) {
        super(frame, titre);
        setModal(modale);
        setPreferredSize(dimensions);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    Dialogue(Dialog dialog, String titre, Dimension dimensions, boolean modale) {
        super(dialog, titre);
        setModal(modale);
        setPreferredSize(dimensions);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
