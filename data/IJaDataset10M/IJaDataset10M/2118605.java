package graphics.champs;

import java.awt.Color;
import javax.swing.JComponent;

/**
 * Cette interface propose les m�canismes pour obtenir une
 * couleur de fond et le texte d'une info-bulle.
 * 
 * @author Simon
 *
 */
public interface BackgroundAndToolTip {

    /**
	 * Retourne la couleur de fond du composant.
	 * 
	 * @return la couleur de fond du composant
	 * 
	 * @see JComponent#getBackground()
	 */
    public Color getBackground();

    /**
	 * Retourne le texte d'infobulle du composant.
	 * 
	 * @return le texte d'infobulle du composant
	 * 
	 * @see JComponent#getToolTipText()
	 */
    public String getToolTipText();
}
