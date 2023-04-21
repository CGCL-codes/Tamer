package adressepostale;

import java.util.List;

/**
 * @see "http://fr.wikipedia.org/wiki/Adresse_postale"
 */
public interface Adresse extends Ligneable {

    /**
     * Retourne les lignes de l'adresse.
     * 
     * <ol>
     * <strong>Identification du destinataire</strong>
     * <li>Identité du destinataire</li>
     * <li>Qualité, profession ou fonction du destinataire<br>
     * Complément d'identification du destinataire (étage, service...)</li><br>
     * <strong>Distribution postale</strong>
     * <li>Identification du point de remise (bâtiment, immeuble...)</li>
     * <li>N°, type et libellé de la voie</li><br>
     * <strong>Acheminement</strong>
     * <li>Lieu-dit</li>
     * <li>Code postal et localité</li>
     * <li>Pays, ignoré si dans le même pays (ie: en France).</li>
     * </ol>
     * 
     * <strong>Limitations pour certaines implémentations :</strong>
     * <ul>
     * <li>Aligné à gauche
     * <li>Aucune ligne ne doit faire plus de 38 caractères (utilisation possible d'abréviations)
     * <li>Aucune ligne ne doit être blanche
     * <li>Aucune ligne ne doit contenir de caractères spéciaux tel que {@code ( , - ' " . : ; / § !
     * ) }, seul les caractères alphanumériques sont autorisés
     * <li>Aucune ligne ne doit contenir de double espaces
     * </ul>
     * 
     * @return les lignes de l'adresse.
     */
    List<String> getLignes();

    /**
     * @return
     */
    Destinataire getDestinataire();

    /**
     * @return
     */
    Distribution getDistribution();

    /**
     * @return
     */
    Acheminement getAcheminement();

    /**
     * Retourne une représentation en String de l'adresse avec des sauts de ligne fonction du
     * système.
     * 
     * @return une représentation en String de l'adresse avec des sauts de ligne fonction du
     *         système.
     * 
     * @see #getLignes()
     */
    String toString();
}
