package presentation.controler.actions;

import core.SystemRegException;
import core.data_tier.entities.Participant;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import presentation.Globals;
import presentation.MainFrame;

/**
 *
 * @author Lahvi
 */
public class DeleteAction extends AbstractObserverAction {

    private static DeleteAction deleteAction;

    public static DeleteAction getDeleteAction() {
        if (deleteAction == null) {
            deleteAction = new DeleteAction();
        }
        return deleteAction;
    }

    private DeleteAction() {
        super("Odstranit účastníka");
    }

    @Override
    public boolean isEnabledInState() {
        Participant selectedP = Globals.getInstance().getSelectedParticipant();
        if (selectedP != null) {
            return true;
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Participant selectedP = Globals.getInstance().getSelectedParticipant();
            int option = MainFrame.showOptionDialog("Opravdu chcete účastníka odstranit?");
            if (option == JOptionPane.YES_OPTION) {
                Globals.getInstance().getParticipantOps().deleteParticipant(selectedP.getId());
                Globals.getInstance().refreshData();
            }
        } catch (SystemRegException ex) {
            Globals.showErr(MainFrame.getMainFrame(), ex);
        }
    }
}
