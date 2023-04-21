package be.vds.jtbdive.core.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiverUsedException;
import be.vds.jtbdive.core.model.Diver;
import be.vds.jtbdive.core.model.DiverManagerFacade;
import be.vds.jtbdive.core.view.listener.DiverSelectionListener;

public class DeleteDiverAction extends AbstractAction implements DiverSelectionListener {

    private DiverManagerFacade diverManagerFacade;

    private Window parentWindow;

    private Diver currentDiver;

    public DeleteDiverAction(Window parentWindow, DiverManagerFacade diverFacade) {
        this.parentWindow = parentWindow;
        this.diverManagerFacade = diverFacade;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int option = JOptionPane.showConfirmDialog(parentWindow, "Are you sure you want to delete " + currentDiver.toString() + " ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            try {
                boolean b = diverManagerFacade.deleteDiver(currentDiver);
            } catch (DataStoreException e1) {
                e1.printStackTrace();
            } catch (DiverUsedException e2) {
                String message = "Error encoutered while persisting:\r\n" + e2.getMessage();
                JOptionPane.showMessageDialog(parentWindow, message, "Diver Used Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void diverSelected(Diver diver) {
        this.currentDiver = diver;
    }
}
