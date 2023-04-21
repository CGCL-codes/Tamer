package visugraph.gview;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

/**
 * Interface pour un objet "traçable" sur un GraphComponent.
 * Cette interface permet de rajouter des objets autre que des noeuds ou des arêtes sur un GraphComponent.
 * Les coordonnées et positions considérées sont celles du graphe.
 * Les objets peuvent se placer au dessus, en dessous ou entre les noeuds et arêtes selon la zPosition.
 */
public interface Drawable {

    /**
	 * Trace l'élément à l'aide du Graphics fourni.
	 * @param g Graphics sur lequel doit être tracé l'élément.
	 */
    void draw(Graphics g);

    /**
	 * Retourne les limites actuelles du traçé.
	 * Le dessin doit entièrement être compris dans ces limites.
	 */
    Rectangle2D getBounds();

    /**
	 * Retourne la position de l'élément à tracer.
	 * 2 choix sont possibles : au premier plan ou à l'arrière plan.
	 * Cette valeur peut varier tout au long de la vie de l'objet.
	 */
    Position zPosition();

    /**
	 * Enum contenant les différentes positions pour le tracé du composant.
	 */
    public enum Position {

        BACKGROUND, MIDDLEGROUND, FOREGROUND
    }
}
