package madiatechrcp.IHM.listener;

import java.util.ArrayList;
import java.util.List;
import madiatechrcp.IHM.InfoProduit;
import madiatechrcp.IHM.vue.FenetrePrincipale;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import fr.unice.produit.Bibliotheque;
import fr.unice.produit.Produit;
import fr.unice.produit.persistance.Server;

/**
 * Cette classe permet d'afficher le resultat de la recherche avancee. <br>
 * Elle est appelee uniquement lorsque que la combobox de la recherche change de valeur. <br>
 * Le resultat de la recherche avancee n'est apparant qu'a partir du moment ou on a au moins trois 
 * caracteres dans un champs de recherche.
 * @author Mydiatech
 *
 */
public class ChampsRechercheListener implements Listener {

    private InformationsProduitListener ipl;

    private int categorie;

    private Bibliotheque bd;

    private String titre = "";

    private String auteur = "";

    private String genre = "";

    private String collection = "";

    private String editeurLivre = "";

    private String isbn = "";

    private String anneeAudio = "";

    private String anneeVideo = "";

    private String editeurJeu = "";

    private boolean pret;

    private List<? extends Produit> resultat = new ArrayList<Produit>();

    private FenetrePrincipale fenetre;

    /**
	 * 
	 * @param ipl : le listener pour recuperer la valeur de chacun des champs de recherche.
	 * @param cat : l'identifiant du support media selectionne (livre, audio...).
	 * @param fen : la vue pour afficher le resultat de la recherche.
	 */
    public ChampsRechercheListener(InformationsProduitListener ipl, int cat, FenetrePrincipale fen) {
        this.ipl = ipl;
        this.categorie = cat;
        this.fenetre = fen;
        bd = Server.getServer();
    }

    /**
	 * Cette methode est appelee automatiquement a chaque fois qu'un champs de texte pour la recherche est modifie. <br/>
	 * Il va donc mettre a jour le resultat de la recherche, l'afficher a l'ecran dans la vue correspondante.
	 */
    public void handleEvent(Event event) {
        resultat = resultatRechercheAvancee();
        Runnable afficheRechercheAvancee = new Runnable() {

            public void run() {
                if (fenetre != null) {
                    if (fenetre.getScrolledComposite() != null) {
                        instancierPage();
                    }
                    if (resultat != null) {
                        fenetre.afficherCollection(resultat);
                    } else {
                        fenetre.setListeAffiche(null);
                        fenetre.afficherNbArticle();
                        fenetre.effaceConteneur();
                    }
                    fenetre.ajusterScrollBar();
                }
            }
        };
        BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), afficheRechercheAvancee);
    }

    /**
	 * Cette methode va ouvrir la vue qui doit afficher le resultat de la recherche avancee, soit {@link FenetrePrincipale}.
	 */
    private void instancierPage() {
        IWorkbenchPage vueActive = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart vueCollection = null;
        try {
            vueCollection = vueActive.showView("MadiaTechRCP.viewFenetrePrincipale", "principal", IWorkbenchPage.VIEW_VISIBLE);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        if (vueCollection != null && vueCollection instanceof FenetrePrincipale) {
            fenetre = (FenetrePrincipale) vueCollection;
        }
    }

    /**
	 * Dans cette methode, on recupere la valeur de chaque critere de recherche et on fait appel a la base de donnees, 
	 * qui nous retourne le resultat correspondant. <br>
	 * La recherche s'effectue a partir de trois caracteres dans un champs de recherche et/ou si la combobox du pret est 
	 * autre que indifferent.
	 * @return liste qui forme le resultat de la recherche
	 */
    public List<? extends Produit> resultatRechercheAvancee() {
        switch(categorie) {
            case InfoProduit.LIVRE:
                titre = ipl.getTextTitreText();
                auteur = ipl.getTextAuteurText();
                genre = ipl.getTextGenreText();
                collection = ipl.getTextCollectionText();
                editeurLivre = ipl.getTextEditeurLivreText();
                isbn = ipl.getTextISBNText();
                switch(ipl.getPretCombo().getSelectionIndex()) {
                    case 1:
                        pret = true;
                        break;
                    case 2:
                        pret = false;
                        break;
                }
                if (titre.length() > 2 || auteur.length() > 2 || genre.length() > 2 || collection.length() > 2 || editeurLivre.length() > 2 || ipl.getPretCombo().getSelectionIndex() != 0) {
                    if (ipl.getPretCombo().getSelectionIndex() == 0) {
                        resultat = bd.rechercheLivre(titre, auteur, genre, editeurLivre, isbn);
                    } else {
                        resultat = bd.rechercheLivre(titre, auteur, genre, editeurLivre, isbn, pret);
                    }
                } else {
                    resultat = null;
                }
                break;
            case InfoProduit.AUDIO:
                titre = ipl.getTextTitreText();
                auteur = ipl.getTextAuteurText();
                genre = ipl.getTextGenreText();
                collection = ipl.getTextCollectionText();
                anneeAudio = ipl.getTextAnneeAudioText();
                switch(ipl.getPretCombo().getSelectionIndex()) {
                    case 1:
                        pret = true;
                        break;
                    case 2:
                        pret = false;
                        break;
                }
                if (titre.length() > 2 || auteur.length() > 2 || genre.length() > 2 || collection.length() > 2 || anneeAudio.length() > 2 || ipl.getPretCombo().getSelectionIndex() != 0) {
                    if (ipl.getPretCombo().getSelectionIndex() == 0) {
                        resultat = bd.rechercheAudio(titre, auteur, genre, anneeAudio);
                    } else {
                        resultat = bd.rechercheAudio(titre, auteur, genre, anneeAudio, pret);
                    }
                } else {
                    resultat = null;
                }
                break;
            case InfoProduit.VIDEO:
                titre = ipl.getTextTitreText();
                auteur = ipl.getTextAuteurText();
                genre = ipl.getTextGenreText();
                collection = ipl.getTextCollectionText();
                anneeVideo = ipl.getTextAnneeVideoText();
                switch(ipl.getPretCombo().getSelectionIndex()) {
                    case 1:
                        pret = true;
                        break;
                    case 2:
                        pret = false;
                        break;
                }
                if (titre.length() > 2 || auteur.length() > 2 || genre.length() > 2 || collection.length() > 2 || anneeAudio.length() > 2 || ipl.getPretCombo().getSelectionIndex() != 0) {
                    if (ipl.getPretCombo().getSelectionIndex() == 0) {
                        resultat = bd.rechercheVideo(titre, auteur, genre, anneeVideo);
                    } else {
                        resultat = bd.rechercheVideo(titre, auteur, genre, anneeAudio, pret);
                    }
                } else {
                    resultat = null;
                }
                break;
            case InfoProduit.JEU:
                titre = ipl.getTextTitreText();
                auteur = ipl.getTextAuteurText();
                genre = ipl.getTextGenreText();
                collection = ipl.getTextCollectionText();
                editeurJeu = ipl.getTextEditeurJeuText();
                switch(ipl.getPretCombo().getSelectionIndex()) {
                    case 1:
                        pret = true;
                        break;
                    case 2:
                        pret = false;
                        break;
                }
                if (titre.length() > 2 || auteur.length() > 2 || genre.length() > 2 || collection.length() > 2 || anneeAudio.length() > 2 || ipl.getPretCombo().getSelectionIndex() != 0) {
                    if (ipl.getPretCombo().getSelectionIndex() == 0) {
                        resultat = bd.rechercheJeu(titre, auteur, genre, editeurJeu);
                    } else {
                        resultat = bd.rechercheJeu(titre, auteur, genre, editeurJeu, pret);
                    }
                } else resultat = null;
                break;
            case InfoProduit.TOUT:
                titre = ipl.getTextTitreText();
                auteur = ipl.getTextAuteurText();
                genre = ipl.getTextGenreText();
                collection = ipl.getTextCollectionText();
                switch(ipl.getPretCombo().getSelectionIndex()) {
                    case 1:
                        pret = true;
                        break;
                    case 2:
                        pret = false;
                        break;
                }
                if (titre.length() > 2 || auteur.length() > 2 || genre.length() > 2 || collection.length() > 2 || anneeAudio.length() > 2 || ipl.getPretCombo().getSelectionIndex() != 0) {
                    if (ipl.getPretCombo().getSelectionIndex() == 0) {
                        resultat = bd.recherche(titre, auteur, genre);
                    } else {
                        resultat = bd.recherche(titre, auteur, genre, pret);
                    }
                } else {
                    resultat = null;
                }
                break;
            default:
                break;
        }
        return resultat;
    }

    /**
	 * @return la liste de resultats de la recherche avancee.
	 */
    public List<? extends Produit> getResultat() {
        return resultat;
    }
}
