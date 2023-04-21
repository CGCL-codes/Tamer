package fr.lip6.sma.simulacion.avatar;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Classe pour une collection d'images pour toutes les attitudes
 * d'un avatar pour un affichage particulier.
 *
 * @author Paul Guyot <paulguyot@acm.org>
 * @version $Revision: 115 $
 *
 * @see fr.lip6.sma.simulacion.avatar.Test
 */
public class AvatarImageCollection implements Serializable {

    /**
	 * Cl� pour les orientations des couches.
	 */
    public static final String LAYER_ORIENTATION_KEY = "orientation";

    /**
	 * Orientation horizontale.
	 */
    public static final String LAYER_HORIZONTAL_ORIENTATION = "horizontal";

    /**
	 * Orientation verticale.
	 */
    public static final String LAYER_VERTICAL_ORIENTATION = "vertical";

    /**
	 * Cl� pour les tranches des couches.
	 */
    public static final String LAYER_SLICES_KEY = "slices";

    /**
	 * R�f�rence sur le dictionnaire contenant les images pour les attitudes
	 */
    private final Map<String, Map[]> mAttitudes;

    /**
	 * Constructeur � partir d'un tableau (dictionnaire).
	 *
	 * <p>Les cl�s du tableau sont les noms des attitudes.
	 * Les valeurs sont des tableaux repr�sentant les couches. On a, pour
	 * chaque couche, l'orientation (cl� LAYER_ORIENTATION_KEY) et les
	 * tranches correspondantes (cl� LAYER_SLICES_KEY). Les tranches sont
	 * des vecteurs, chaque �l�ment repr�sentant une tranche et �tant lui-
	 * m�me un vecteur d'image (pour l'animation).</p>
	 *
	 * <p>Je reprends avec un dessin pour ceux qui ne suivent pas au fond.</p>
	 * <ul>
	 *	   <li>attitude 1
	 *	   <ul>
	 *		   <li>couche 1
	 *		   <ul>
	 *			   <li>orientation
	 *			   <li>tranches
	 *			   <ul>
	 *				   <li>tranche 1
	 *				   <ul>
	 *					   <li>image 1
	 *					   <li>image 2
	 *				   </ul>
	 *				   <li>tranche 2
	 *				   <ul>
	 *					   <li>image 1
	 *					   <li>image 2
	 *					   <li>image 3
	 *				   </ul>
	 *			   </ul>
	 *		   </ul>
	 *		   <li>couche 2
	 *		   <ul>
	 *			   <li>...
	 *		   </ul>
	 *		   <li>...
	 *	   </ul>
	 *	   <li>attitude 2
	 *	   <ul>
	 *		   <li>...
	 *	   </ul>
	 *	   <li>...
	 * </ul>
	 *
	 * @param inAttitudes	dictionnaire contenant les images
	 */
    public AvatarImageCollection(Map<String, Map[]> inAttitudes) {
        mAttitudes = inAttitudes;
    }

    /**
	 * Accesseur sur l'ensemble des attitudes (s�quences d'images).
	 *
	 * @return l'ensemble des attitudes
	 */
    public final Set<String> getAttitudes() {
        return mAttitudes.keySet();
    }

    /**
	 * Accesseur sur les couches pour une attitude particuli�re.
	 *
	 * @param	inAttitude attitude dont on veut les couches.
	 * @return	les couches pour cette attitude ou <code>null</code> si
	 *			l'attitude n'est pas d�finie.
	 */
    public final Map[] getLayers(String inAttitude) {
        return mAttitudes.get(inAttitude);
    }
}
