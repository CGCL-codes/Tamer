package Driver;

import Controller.*;
import View.PIMFrame;

/**
 *
 * @author Christian Juul Wendell, Søren Svejstrup, Thorsten Lyby Vestergaard.
 */
public class StartupDriver {

    /**
     * Denne klasse står for oprettelse af progammets kontrollere og
     * programframet. firstUpdate metoden som bliver kaldt i
     * AppointmentController sørger for, at alle gemte informationer bliver
     * updateret til viewet når programmet startes.
     */
    public static void start() {
        NoteController noteCtrl = new NoteController();
        ContactController contactCtrl = new ContactController();
        AppointmentController appointmentCtrl = new AppointmentController();
        PIMFrame pimFrame = new PIMFrame(noteCtrl, contactCtrl, appointmentCtrl);
        pimFrame.setVisible(true);
        appointmentCtrl.firstUpdate();
    }
}
