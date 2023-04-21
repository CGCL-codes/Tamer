package movepaint.listener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import movepaint.GestureRecognition;
import movepaint.controller.ToolBarAction;
import movepaint.view.ToolBarOutils;

/**
 * Classe qui gere les actions effectuées sur la barre d'outils
 * @author Paulino A.
 * @version 1.0
 */
public class ToolBarOutilListener implements ActionListener {

    private GestureRecognition _frameMain = null;

    private ToolBarAction _toolBarAction = null;

    private int _id;

    /**
	 *	Constructeur de l'écouteur de la barre d'outils
	 *
	 *	@param frameMain
	 *			Référence a la frame principale de l'application
	 *	@param id
	 *			ID associé a l'option de la barre d'outils
	 *			<br>
	 *			<b>Exemple: </b> ID MenuMain.IMPORTER
	 **/
    public ToolBarOutilListener(JToolBar toolBarMain, GestureRecognition frameMain, int id) {
        _frameMain = frameMain;
        _id = id;
        _toolBarAction = new ToolBarAction(_frameMain);
    }

    /**
	 *	Methode qui gere les cliques effectués dans la barre d'outils
	 *
	 *	@param ae
	 *			Evenement de type ActionEvent.
	 *
	 **/
    public void actionPerformed(ActionEvent ae) {
        if (_id == ToolBarOutils.ID_SELECTIONNER_DEPLACER) {
            _toolBarAction.selectionnerDeplacer();
        } else if (_id == ToolBarOutils.ID_CRAYON) {
            _toolBarAction.crayon();
        } else if (_id == ToolBarOutils.ID_ASSISTE) {
            _toolBarAction.assiste();
        }
    }
}
