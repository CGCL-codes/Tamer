package thinwire.apps.handbook.chapter2;

import java.util.List;
import thinwire.ui.*;

public class MultipleDialogs {

    public static void run(Container<Component> canvas) {
        Dialog nonModal = new Dialog("Non Modal");
        nonModal.setModal(false);
        nonModal.setBounds(25, 25, 200, 150);
        nonModal.setVisible(true);
        Dialog stationary = new Dialog("Stationary");
        stationary.setModal(false);
        stationary.setRepositionAllowed(false);
        stationary.setBounds(250, 25, 200, 150);
        stationary.setVisible(true);
        Dialog resize = new Dialog("Resizable!");
        resize.setModal(false);
        resize.setResizeAllowed(true);
        resize.setBounds(25, 200, 200, 150);
        resize.setVisible(true);
        Dialog waitForMe = new Dialog("Wait For Me!");
        waitForMe.setModal(false);
        waitForMe.setWaitForWindow(true);
        waitForMe.setBounds(250, 200, 200, 150);
        waitForMe.setVisible(true);
        Dialog modal = new Dialog("I'm Modal");
        modal.setBounds(138, 112, 200, 150);
        modal.setVisible(true);
        List<Dialog> diags = Application.current().getFrame().getDialogs();
        modal.setTitle(modal.getTitle() + " - " + diags.size() + " Dialogs visible");
    }

    public static void main(String[] args) {
        run(Application.current().getFrame());
    }
}
