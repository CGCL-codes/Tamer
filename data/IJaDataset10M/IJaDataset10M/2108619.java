package analyse;

/**
 * Classe EnteteCSV<br/>
 * <b>EnteteCSV permet d'identifier ou de donner la valeur d'une ent�te.</b>
 * <p>
 * Cette classe est une classe static. Elle est compos�e d'un constructeur par d�faut, 
 * de trois constantes, qui sont les trois d'ent�tes et de ses accesseurs. Les ent�tes
 * pr�cisent comment sont �crits une carte, une route ou un croisement en format csv.
 * 
 * @author groupe 2
 * @version 1.0
 *</p>
 */
public class EnteteCSV {

    /**
	 * ENTETECARTENUE est une cha�ne de caract�res qui contient l'ent�te d'une carte nue.
	 */
    private static final String ENTETECARTENUE = "ID,X,Y,LISTE DES LIAISONS";

    /**
	 * ENTETEROUTE est une cha�ne de caract�res qui contient l'ent�te des routes.
	 */
    private static final String ENTETEROUTE = "ID,NOM,VITESSE";

    /**
	 * ENTETECROISEMENT est une cha�ne de caract�res qui contient l'ent�te des croisements.
	 */
    private static final String ENTETECROISEMENT = "ID,NOM,TYPE";

    /**
	 * Constructeur de EnteteCSV<br/>
	 * <p>
	 * Constructeur sans param�tre, il cr�e uniquement un espace m�moire.
	 * </p>
	 */
    public EnteteCSV() {
    }

    /**
	 * Retourne le code correspondant � une ent�te pass�e en param�tre.
	 * @param entete, l'ent�te � tester 
	 * @return un entier qui correspond une ent�te <ul>
	 * <li>retourne 1 si l'ent�te est de type carte nue.</li>
	 * <li>retourne 2 si l'ent�te est de type route.</li>
	 * <li>retourne 3 si l'ent�te est de type croisement.</li>
	 * <li>retourne 0 si ce n'est pas une de ces trois ent�tes.</li>
	 * </ul>
	 */
    public static int entete(String entete) {
        if (entete.equals(ENTETECARTENUE)) return 1; else if (entete.equals(ENTETEROUTE)) return 2; else if (entete.equals(ENTETECROISEMENT)) return 3;
        return 0;
    }

    /**
	 * Accesseur de l'ent�te de la carte nue
	 * @return entetecartenue : l'ent�te d'une carte nue.
	 */
    public static String getEntetecartenue() {
        return ENTETECARTENUE;
    }

    /**
	 * Accesseur de l'ent�te de la route.
	 * @return enteteroute : l'ent�te d'une route.
	 */
    public static String getEnteteroute() {
        return ENTETEROUTE;
    }

    /**
	 * Accesseur de l'ent�te des croisements.
	 * @return entetecroisements : l'ent�te des croisements.
	 */
    public static String getEntetecroisement() {
        return ENTETECROISEMENT;
    }
}
