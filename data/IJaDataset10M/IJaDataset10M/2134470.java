package fr.ign.cogit.geoxygene.feature.type;

/**
 * @author Balley
 *
 * GF_Constraint propos� par le General Feature Model de la norme ISO1909.
 */
public interface GF_Constraint {

    /** Renvoie la description formul�e en langage naturel ou formel. */
    public String getDescription();

    /** Affecte une description. */
    public void setDescription(String Description);
}
