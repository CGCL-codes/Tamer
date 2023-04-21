package fr.amille.animebrowser.model.alert;

import fr.amille.animebrowser.view.popup.AlertPopup;

/**
 * @author amille
 * 
 */
public class AnimeBrowserAlert {

    public static void errorAlert(final String message) {
        System.err.println(message);
    }

    public static void infoAlert(final String message) {
        AlertPopup.modalSimpleAlert(message);
    }
}
