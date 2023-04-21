package fr.lip6.sma.simulacion.simbar;

import java.awt.Container;
import fr.lip6.sma.simulacion.app.GameController;

/**
 * Interface du contr�leur du jeu SimBar.
 *
 * @author Paul Guyot <paulguyot@acm.org>
 * @version $Revision: 1.6 $
 *
 * @see "aucun test d�fini."
 */
public interface SimBarController extends GameController {

    /**
	 * M�thode pour voter "Go To Bar".
	 */
    void voteGoToBar();

    /**
	 * M�thode pour voter "Stay Home".
	 */
    void voteStayHome();

    /**
	 * M�thode pour voter "Undecided".
	 */
    void voteUndecided();

    /**
	 * M�thode pour poser la question � un joueur.
	 *
	 * @param inAgentName	nom de l'agent qui pose la question.
	 */
    void askOtherRegular(String inAgentName);

    /**
	 * M�thode pour r�pondre � un joueur.
	 *
	 * @param inAgentName	nom de l'agent � qui r�pondre.
	 * @param inReply		notre r�ponse.
	 */
    void replyToOtherRegular(String inAgentName, String inReply);

    /**
	 * M�thode pour indiquer qu'un agent nous pose la question: viens-tu?
	 *
	 * @param inAgentName	nom de l'agent qui pose la question.
	 */
    void willYouCome(String inAgentName);

    /**
	 * Traduit un conteneur fra�chement initialis�.
	 *
	 * HACK. Cette m�thode ne devrait pas se trouver ici.
	 *
	 * @param inContainer	objet � traduire
	 */
    void localizeContainer(Container inContainer);
}
