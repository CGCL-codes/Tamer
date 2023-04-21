package fr.lip6.sma.simulacion.simbar3;

import fr.lip6.sma.simulacion.app.GameApplicationImpl;
import fr.lip6.sma.simulacion.app.GameController;
import fr.lip6.sma.simulacion.app.GameModel;
import fr.lip6.sma.simulacion.app.SplashWindow;
import fr.lip6.sma.simulacion.avatar.Avatar;

/**
 * Classe pour l'application des habitu�s du bar.
 *
 * @author Paul Guyot <paulguyot@acm.org>
 * @version $Revision: 1.9 $
 *
 * @see "aucun test d�fini."
 */
public class RegularApplication extends GameApplicationImpl implements SimBarApplication {

    /**
	 * Nom de l'avatar choisi en ligne de commande.
	 * (<code>null</code> si aucun avatar n'a �t� choisi en ligne de commande).
	 */
    private final String mAvatarName;

    /**
	 * R�f�rence sur l'avatar courant.
	 * D�termin� par la fen�tre de configuration ou par
	 * la ligne de commande.
	 */
    private Avatar mAvatar;

    /**
	 * R�f�rence sur la fen�tre de configuration.
	 * <code>null</code> lorsqu'elle n'est pas affich�e.
	 * Cette fen�tre permet de choisir son avatar.
	 */
    private SimBarSetupWindow mSetupWindow;

    /**
	 * R�f�rence sur la fen�tre principale.
	 * <code>null</code> avant qu'elle soit affich�e.
	 */
    private SimBarMainWindow mMainWindow;

    /**
	 * Mod�le du jeu.
	 */
    private SimBarModel mGameModel;

    /**
	 * Constructeur � partir du nom de l'avatar pour le joueur.
	 * Cr�e le serveur.
	 *
	 * @param inAvatarName	nom de l'avatar ou <code>null</code> si on doit
	 *						afficher la fen�tre de configuration.
	 */
    public RegularApplication(String inAvatarName) {
        super("etc/simbar3.xml");
        mAvatarName = inAvatarName;
    }

    /**
	 * Cr�e et retourne la fen�tre de d�marrage.
	 * Cette m�thode n'est appel�e qu'une seule fois.
	 *
	 * @return	une fen�tre de d�marrage.
	 */
    protected SplashWindow getSplashWindow() {
        return new SimBarSplashWindow();
    }

    /**
	 * Accesseur sur le mod�le.
	 *
	 * @return le mod�le du jeu.
	 */
    public final GameModel getGameModel() {
        return mGameModel;
    }

    /**
	 * Accesseur le contr�leur (i.e. la fen�tre principale).
	 *
	 * @return	le contr�leur.
	 */
    public final GameController getGameController() {
        return mMainWindow;
    }

    /**
	 * Affiche la fen�tre de s�lection (sauf si l'avatar a d�j� �t� choisi)
	 * et ouvre la fen�tre principale.
	 */
    public void startGame() {
        if (mAvatarName == null) {
            showSetupWindow();
        } else {
            mAvatar = getAvatar(mAvatarName);
            if (mAvatar == null) {
                throw new Error("Aucun avatar de ce nom n'existe: '" + mAvatarName + "'");
            }
            final String theAgentName = getPlayerNameFromAvatarName(mAvatarName);
            init(theAgentName);
            showMainWindow();
        }
    }

    /**
	 * Accesseur sur l'avatar courant.
	 *
	 * @return	l'avatar courant ou <code>null</code> si aucun avatar
	 *			n'a �t� d�fini.
	 */
    public Avatar getAvatar() {
        return mAvatar;
    }

    /**
	 * M�thode appel�e par la fen�tre de configuration pour indiquer
	 * que la configuration est termin�e.
	 */
    public final void finishSetup() {
        mSetupWindow.setVisible(false);
        mSetupWindow.dispose();
        mSetupWindow = null;
        mAvatar = getAvatar(getLocalPlayer().getAvatarName());
        showMainWindow();
    }

    /**
	 * Show the setup window.
	 */
    private void showSetupWindow() {
        mSetupWindow = new SimBarSetupWindow(this);
        mSetupWindow.setVisible(true);
    }

    /**
	 * Affiche la fen�tre principale de l'application.
	 * Lorsque cette m�thode est appel�e, l'avatar du joueur est connu et fixe.
	 */
    private void showMainWindow() {
        mGameModel = new SimBarModel(getAgentListManager(), getConfiguration(), getLocalPlayer().getAgentName());
        mMainWindow = new SimBarMainWindow(this);
        mMainWindow.finishSetup();
        mMainWindow.setVisible(true);
    }

    /**
	 * Point d'entr�e de l'application.
	 *
	 * @param inArgs	arguments sur la ligne de commande
	 */
    public static void main(String[] inArgs) {
        String thePlayerName = null;
        if (inArgs.length == 1) {
            thePlayerName = inArgs[0];
        } else if (inArgs.length > 1) {
            System.err.println("Nombre incorrect d'arguments.");
            System.err.println("Syntaxe: " + RegularApplication.class + " [agent]");
            System.exit(1);
        }
        new RegularApplication(thePlayerName);
    }
}
