package fr.unice.miage.projetjava.managers;

import javax.ejb.Local;

/**
 *
 * @author Olivier
 */
@Local
public interface GestionnaireDePanierLocal {

    public void ajouterProduit(fr.unice.miage.projetjava.entities.Produit prod);

    public void retirerProduit(fr.unice.miage.projetjava.entities.Produit prod);

    public void viderPanier();

    public fr.unice.miage.projetjava.entities.LigneDePanier getLigneDePanier(fr.unice.miage.projetjava.entities.Produit prod);

    public float getTotalPanier();

    public int getNombreProduit();

    public java.util.Collection<fr.unice.miage.projetjava.entities.LigneDePanier> getListeldp();

    public void setListeldp(java.util.Collection<fr.unice.miage.projetjava.entities.LigneDePanier> listeldp);
}
