package movepaint.listener;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import movepaint.GestureRecognition;
import movepaint.view.MenuMain;
import movepaint.controller.MenuAction;

/**
 * Classe qui gere les actions effectuées sur le menu
 * @author Paulino A.
 * @version 1.0
 */
public class MenuListener implements ActionListener {

    private GestureRecognition _frameMain = null;

    private MenuAction _menuAction = null;

    private int _id;

    /**
	 *	Constructeur de l'écouteur du menu
	 *
	 *	@param frameMain
	 *			Référence a la frame principale de l'application
	 *	@param id
	 *			ID associé a l'option du menu.
	 *			<br>
	 *			<b>Exemple: </b> ID MenuMain.NOUVELLE_CATEGORIE
	 *
	 **/
    public MenuListener(GestureRecognition frameMain, int id) {
        _frameMain = frameMain;
        _id = id;
        _menuAction = new MenuAction(_frameMain);
    }

    /**
	 *	Methode qui gere les cliques effectués dans le menu
	 *
	 *	@param ae
	 *			Evenement de type ActionEvent.
	 *
	 **/
    public void actionPerformed(ActionEvent ae) {
        JMenuItem source = (JMenuItem) ae.getSource();
        if (_id == MenuMain.ID_NOUVEAU) {
            _menuAction.nouveau();
        } else if (_id == MenuMain.ID_OUVRIR) {
            _menuAction.ouvrir();
        } else if (_id == MenuMain.ID_ENREGISTER) {
            _menuAction.enregistrer();
        } else if (_id == MenuMain.ID_ENREGISTRER_SOUS) {
            _menuAction.enregistrer_sous();
        } else if (_id == MenuMain.ID_IMPRIMER) {
            _menuAction.imprimer();
        } else if (_id == MenuMain.ID_QUITTER) {
            _menuAction.quitter();
        } else if (_id == MenuMain.ID_COPIER) {
            _menuAction.copier();
        } else if (_id == MenuMain.ID_COLLER) {
            _menuAction.coller();
        } else if (_id == MenuMain.ID_EFFACER) {
            _menuAction.effacer();
        } else if (_id == MenuMain.ID_TOUS_SELECTIONNER) {
            _menuAction.tousSelectionner();
        } else if (_id == MenuMain.ID_PREFERENCE) {
            _menuAction.preference();
        } else if (_id == MenuMain.ID_LOOK) {
            _menuAction.changeLaF(source.getText());
        } else if (_id == MenuMain.ID_A_PROPOS) {
            _menuAction.aPropos();
        } else if (_id == MenuMain.ID_MANUEL) {
            _menuAction.manuel();
        } else if (_id == MenuMain.ID_PAGE_ACCUEIL) {
            _menuAction.accueil();
        }
    }

    /**
	 *	Methode qui retourne une référence à l'objet d'action du menu
	 *
	 *	@return
	 *				Référence à l'objet d'action du menu
	 **/
    public MenuAction getMenuAction() {
        return _menuAction;
    }
}
