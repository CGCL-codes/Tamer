package presentation.controler.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import presentation.Globals;
import presentation.dialogs.ParticipantDialog;
import sun.awt.GlobalCursorManager;

/**
 *
 * @author Lahvi
 */
public class CreateParticipantAction extends AbstractAction {

    private static CreateParticipantAction instance;

    public static CreateParticipantAction getInstance() {
        if (instance == null) instance = new CreateParticipantAction();
        return instance;
    }

    public CreateParticipantAction() {
        super("Nový účastník");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new ParticipantDialog().setVisible(true);
        Globals.getInstance().refreshData();
    }
}
