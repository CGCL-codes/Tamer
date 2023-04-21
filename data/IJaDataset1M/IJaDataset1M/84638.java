package fr.lip6.sma.simulacion.simbar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import fr.lip6.sma.simulacion.server.LocalPlayer;

/**
 * Classe pour les informations sur le joueur local (affich�es � droite).
 *
 * @author Paul Guyot <paulguyot@acm.org>
 * @version $Revision: 1.7 $
 *
 * @see "aucun test d�fini."
 */
public class LocalPlayerInfoPanel extends PlayerInfoPanel implements PropertyChangeListener {

    /**
	 * Constructeur � partir d'une r�f�rence sur le joueur local.
	 *
	 * @param inPlayer	joueur local
	 */
    public LocalPlayerInfoPanel(LocalPlayer inPlayer) {
        setSize(169, 195);
        inPlayer.addPropertyChangeListener(this);
        updateValues();
    }

    /**
	 * Mise � jour des diff�rentes valeurs.
	 */
    private void updateValues() {
    }

    /**
	 * M�thode appel�e lorsqu'une propri�t� change.
	 *
	 * @param inEvent	�v�nement du changement.
	 */
    public final void propertyChange(PropertyChangeEvent inEvent) {
        updateValues();
    }
}

;
