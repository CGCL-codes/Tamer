package bddeductive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import deduction.Fait;
import deduction.Parent;
import deduction.Probleme;
import deduction.Regle;
import dessin.fabrique.Rapport;

/**
 * Contient les informations volatiles de la base de
 * donnees deductive telles que les points existants,
 * les correspondances, l'ennonce du probleme...
 */
public class BDDeduction extends NoyauBDDeduction {

    /**
	 * Base de donnees contenant les points.
	 */
    protected BDPoints points;

    /**
	 * Probleme a resoudre.
	 */
    protected Probleme probleme;

    /**
	 * Hypotheses de depart.
	 */
    protected List<Fait> hypotheses;

    /**
	 * Faits connus.
	 */
    protected List<Fait> anciensFaits;

    /**
	 * Faits deduits.
	 */
    protected List<Fait> nouveauxFaits;

    /**
	 * Liste des parents.
	 */
    protected Map<Fait, Parent> parents;

    /**
	 * Regles connues.
	 */
    protected List<Regle> regles;

    /**
	 * Temps mis pour la resolution.
	 */
    protected long tempsEcoule;

    /**
	 * Temps mis pour la preuve.
	 */
    protected long tempsPreuve;

    /**
	 * Resolution interrompue.
	 */
    private boolean interrupt;

    /**
	 * Etat de sortie.
	 */
    protected char etatFinal;

    /**
	 * Le probleme est resolu.
	 */
    public static final char RESOLU = 1;

    /**
	 * Le probleme n'est pas resolu.
	 */
    public static final char NON_RESOLU = 2;

    /**
	 * Pas assez de memoire.
	 */
    public static final char MEMOIRE_TROP_PETITE = 3;

    /**
	 * Temps ecoule.
	 */
    public static final char TEMPS_ECOULE = 4;

    /**
	 * Constructeur par defaut.
	 */
    public BDDeduction() {
        super();
        points = new BDPoints();
        regles = new ArrayList<Regle>();
    }

    /**
	 * Ajoute une nouvelle regle connue a la base de
	 * donnees deductive.
	 * @param regle regle a ajouter.
	 * @return si elle a ete ajoutee.
	 */
    public boolean ajouterRegleConnue(final Regle regle) {
        return regles.add(regle);
    }

    /**
	 * Prepare la resolution du probleme.
	 * @param probleme probleme a resoudre.
	 */
    protected void preparerResolution(final Probleme probleme) {
        Rapport.add("-- Initialisation --");
        this.probleme = probleme;
        points.reset();
        etatFinal = NON_RESOLU;
        nouveauxFaits = new ArrayList<Fait>();
        hypotheses = new ArrayList<Fait>();
        Fait[] hyp = probleme.getHypotheses();
        for (Fait f : hyp) {
            Rapport.add("Ajout hypothese: " + f);
            hypotheses.add(f);
            points.ajouterPointProbleme(f.getPoints());
        }
        anciensFaits = new ArrayList<Fait>();
        Fait c = probleme.getConclusion();
        nouveauxFaits.add(c);
        points.ajouterPointConclusion(c.getPoints());
        parents = new HashMap<Fait, Parent>();
        copierReglesNoyau();
        Rapport.add("A prouver: " + probleme.getConclusion());
        String s = "";
        Iterator<String> it = points.pointsProbleme.iterator();
        while (it.hasNext()) {
            s = s + " " + it.next();
        }
        Rapport.add("Points du probleme:" + s);
        s = "";
        it = points.pointsConclusion.iterator();
        while (it.hasNext()) {
            s = s + " " + it.next();
        }
        Rapport.add("Points de la conclusion:" + s);
    }

    /**
	 * Lance la resolution du probleme.
	 */
    protected void lanceResolution() {
        boolean continuer = true;
        interrupt = false;
        tempsEcoule = System.currentTimeMillis();
        while (continuer && !interrupt) {
            int total = appliquerRegles();
            continuer = (!(memoireInsuffisante()) && !(contientHypotheses()) && total != 0);
        }
        tempsEcoule = System.currentTimeMillis() - tempsEcoule;
    }

    /**
	 * Applique les regles connues aux nouveaux faits.
	 * Note: on fait une boucle ou l'on prend le premier
	 * fait de la liste, on lui applique les regles
	 * possibles et on ajoute les nouveaux faits a la
	 * fin de la liste. On s'arrete quand on a retire le
	 * dernier fait qui etait precedement connu et on
	 * retourne le nombre de faits qui ont ete deduits.
	 * @return le nombre de faits deduits.
	 */
    private int appliquerRegles() {
        int taille = nouveauxFaits.size();
        int total = 0;
        for (int i = 0; i < taille; ++i) {
            Fait ancien = nouveauxFaits.remove(0);
            total += appliquerRegles(ancien);
            anciensFaits.add(ancien);
        }
        return total;
    }

    /**
	 * Applique les regles connues au fait donne. Les
	 * faits deduits sont ajoutes a la fin de la liste
	 * des nouveaux faits.
	 * @param fait ancien fait.
	 * @return le nombre de faits deduits.
	 */
    private int appliquerRegles(final Fait fait) {
        int total = 0;
        for (Regle r : regles) {
            total += appliquerRegle(fait, r);
        }
        return total;
    }

    /**
	 * Tente d'appliquer la regle au fait donne. Les
	 * nouveaux faits sont ajoutes a la liste.
	 * @param fait fait connu.
	 * @param regle regle a appliquer.
	 * @return le nombre de faits deduits.
	 */
    private int appliquerRegle(final Fait fait, final Regle regle) {
        Fait c = regle.getConclusion();
        if (!fait.getNom().equals(c.getNom())) {
            return 0;
        }
        String[] pointsFait = fait.getPoints();
        String[] pointsRegle = c.getPoints();
        if (pointsFait.length != pointsRegle.length) {
            return 0;
        }
        BDCorrespondance correspondances = new BDCorrespondance();
        if (!correspondances.creer(pointsRegle, pointsFait)) {
            return 0;
        }
        int total = 0;
        Fait[] h = regle.getHypotheses();
        Parent p = parents.get(fait);
        if (p == null) {
            p = new Parent(fait);
        }
        for (Fait f : h) {
            Fait n = appliquerCorrespondances(f, correspondances);
            if (existeDeja(n) == null && !n.equals(fait)) {
                nouveauxFaits.add(n);
                p.ajouter(n, regle, f);
                ++total;
            }
        }
        if (total > 0) {
            parents.put(fait, p);
        }
        return total;
    }

    /**
	 * Applique les correspondances existantes sur le
	 * fait donne afin d'en creer un nouveau.
	 * @param fait fait a modifier.
	 * @param correspondances correspondances a appliquer.
	 * @return le nouveau fait.
	 */
    private Fait appliquerCorrespondances(final Fait fait, final BDCorrespondance correspondances) {
        String[] pointsFait = fait.getPoints();
        String[] nouveaux = new String[pointsFait.length];
        for (int i = 0; i < pointsFait.length; ++i) {
            String p = pointsFait[i];
            String n = correspondances.get(p);
            if (n == null) {
                n = points.pointLibre();
                correspondances.put(p, n);
            }
            nouveaux[i] = n;
        }
        return Fait.creer(fait.getNom(), nouveaux);
    }

    /**
	 * Retourne si le fait donne existe deja.
	 * @param fait fait a chercher.
	 * @return le resultat.
	 */
    private Fait existeDeja(final Fait fait) {
        for (Fait f : anciensFaits) {
            if (estHypothese(fait, f)) {
                return f;
            }
        }
        for (Fait f : nouveauxFaits) {
            if (estHypothese(fait, f)) {
                return f;
            }
        }
        return null;
    }

    /**
	 * Retourne si la base de donnees contient les
	 * hypotheses du probleme.
	 * @return resultat.
	 */
    private boolean contientHypotheses() {
        boolean resultat = true;
        int taille = hypotheses.size();
        for (int i = 0; i < taille; ++i) {
            Fait h = hypotheses.get(i);
            if (contientHypothese(h)) {
                hypotheses.remove(h);
                --taille;
                --i;
            } else {
                resultat = false;
            }
        }
        return resultat;
    }

    /**
	 * Retourne si la base de donnees contient l'hypothese
	 * donnee.
	 * @param fait hypothese a trouver.
	 * @return si elle existe.
	 */
    private boolean contientHypothese(final Fait fait) {
        for (Fait f : anciensFaits) {
            if (estHypothese(f, fait)) {
                return true;
            }
        }
        for (Fait f : nouveauxFaits) {
            if (estHypothese(f, fait)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Retourne si le fait donne correspond a l'hypothese.
	 * @param fait fait a verifier.
	 * @param hyp hypothese a trouver.
	 * @return resultat.
	 */
    private boolean estHypothese(final Fait fait, final Fait hyp) {
        if (!fait.getNom().equals(hyp.getNom())) {
            return false;
        }
        String[] pointsFait = fait.getPoints();
        String[] pointsHyp = hyp.getPoints();
        if (pointsFait.length != pointsHyp.length) {
            return false;
        }
        BDCorrespondance correspondances = new BDCorrespondance();
        for (int i = 0; i < pointsFait.length; ++i) {
            String pointFait = pointsFait[i];
            String pointHyp = pointsHyp[i];
            if (points.estPointConclusion(pointHyp)) {
                if (!pointFait.equals(pointHyp)) {
                    return false;
                } else {
                    correspondances.put(pointHyp, pointFait);
                }
            } else {
                if (points.estPointConclusion(pointFait)) {
                    return false;
                } else {
                    String p = correspondances.get(pointHyp);
                    if (p != null) {
                        if (!pointFait.equals(p)) {
                            return false;
                        }
                    } else {
                        correspondances.put(pointHyp, pointFait);
                    }
                }
            }
        }
        return true;
    }

    /**
	 * Trouve l'hypothese a laquelle correspond le fait.
	 * @param fait fait a chercher.
	 * @return l'hypothese.
	 */
    public Fait trouverHypothese(final Fait fait) {
        Iterator<Fait> it = hypotheses.iterator();
        while (it.hasNext()) {
            Fait f = it.next();
            if (estHypothese(fait, f)) {
                return f;
            }
        }
        return null;
    }

    /**
	 * Copie les regles du noyau dans la liste des
	 * regles connues.
	 */
    private void copierReglesNoyau() {
        Regle[] r = getRegles();
        for (int i = 0; i < r.length; ++i) {
            regles.add(r[i]);
        }
    }

    /**
	 * Retrace la preuve du probleme.
	 * @return si elle a ete creee.
	 */
    protected boolean retracerPreuve() {
        tempsPreuve = System.currentTimeMillis();
        Fait[] hyp = probleme.getHypotheses();
        int nbHyp = 0;
        for (int i = 0; i < hyp.length; ++i) {
            hypotheses.add(hyp[i]);
            ++nbHyp;
        }
        Rapport.add("\n-- Optimisation de la preuve --");
        supprimerFauxParents();
        Rapport.add("\n-- Preuve generale --");
        ecrirePreuveGenerale();
        Rapport.add("\n-- Preuve du probleme --");
        ecrirePreuveInconnues();
        Rapport.add("\n-- Preuve du probleme (finale) --");
        ecrirePreuveFinale();
        tempsPreuve = System.currentTimeMillis() - tempsPreuve;
        return true;
    }

    /**
	 * Supprime les faux parents (parents auxquels l'application
	 * d'une regle n'amene pas aux hypotheses).
	 */
    public void supprimerFauxParents() {
        int total = parents.size();
        Rapport.add("Optimisation par algorithme recursif des faux parents:");
        Rapport.add("- Depart: " + total + " parents.");
        Fait conclusion = probleme.getConclusion();
        long time = System.currentTimeMillis();
        supprimerFauxParents(conclusion);
        time = System.currentTimeMillis() - time;
        int reste = parents.size();
        Rapport.add("- Resultat: " + reste + " parents.");
        Rapport.add("- Amelioration de " + ((100 * (total - reste)) / total) + "% en " + time + "ms.");
    }

    /**
	 * Supprime ce fait si c'est un faux parent.
	 * @param parent fait a tester.
	 * @return si c'est un faux parent.
	 */
    public boolean supprimerFauxParents(final Fait parent) {
        Parent p = parents.get(parent);
        if (p == null) {
            if (estHypothese(parent)) {
                return false;
            }
            return true;
        }
        Set<Fait> s = p.enfants();
        Iterator<Fait> it = s.iterator();
        while (it.hasNext()) {
            Fait enfant = it.next();
            if (enfant.equals(parent)) {
                it.remove();
                p.retirer(enfant);
            } else if (supprimerFauxParents(enfant)) {
                it.remove();
                p.retirer(enfant);
            }
        }
        if (s.size() <= 0) {
            if (estHypothese(parent)) {
                return false;
            }
            parents.remove(parent);
            return true;
        }
        return false;
    }

    /**
	 * Regarde si le fait donne est une hypothese.
	 * @param fait fait a verifier.
	 * @return le resultat.
	 */
    public boolean estHypothese(final Fait fait) {
        Iterator<Fait> it = hypotheses.iterator();
        while (it.hasNext()) {
            Fait hyp = it.next();
            if (estHypothese(fait, hyp)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Ecrit la preuve generale sous forme lisible.
	 */
    public void ecrirePreuveGenerale() {
        Fait conclusion = probleme.getConclusion();
        ecrirePreuveGenerale(conclusion);
    }

    /**
	 * Retourne l'ecriture de la preuve generale a partir du
	 * fait donne.
	 * @param parent conclusion a atteindre.
	 */
    public void ecrirePreuveGenerale(final Fait parent) {
        if (estHypothese(parent)) {
            return;
        }
        Parent p = parents.get(parent);
        if (p == null) {
            return;
        }
        Set<Fait> enfants = p.enfants();
        Iterator<Fait> it = enfants.iterator();
        while (it.hasNext()) {
            Fait enfant = it.next();
            ecrirePreuveGenerale(enfant);
        }
        Regle[] r = p.regles();
        for (int i = 0; i < r.length; ++i) {
            Rapport.add(r[i].toString());
        }
    }

    /**
	 * Ecriture de la preuve avec uniquement les points de la
	 * conclusion.
	 */
    public void ecrirePreuveInconnues() {
        Fait conclusion = probleme.getConclusion();
        ecrirePreuveInconnues(conclusion);
    }

    /**
	 * Ecriture de la preuve avec uniquement les points de la
	 * conclusion.
	 * @param parent conclusion a atteindre.
	 */
    public void ecrirePreuveInconnues(final Fait parent) {
        if (estHypothese(parent)) {
            return;
        }
        Parent p = parents.get(parent);
        if (p == null) {
            return;
        }
        Set<Fait> enfants = p.enfants();
        Iterator<Fait> it = enfants.iterator();
        while (it.hasNext()) {
            Fait enfant = it.next();
            ecrirePreuveInconnues(enfant);
        }
        Regle[] r = p.reglesInconnues(points);
        for (int i = 0; i < r.length; ++i) {
            Rapport.add(r[i].toString());
        }
    }

    /**
	 * Ecriture de la preuve avec uniquement les points de la
	 * conclusion et du probleme.
	 */
    public void ecrirePreuveFinale() {
        Fait conclusion = probleme.getConclusion();
        ecrirePreuveFinale(conclusion);
    }

    /**
	 * Ecriture de la preuve avec uniquement les points de la
	 * conclusion et du probleme a partir du fait donne.
	 * @param parent conclusion a atteindre.
	 * @return l'ecriture finale du fait.
	 */
    public Fait ecrirePreuveFinale(final Fait parent) {
        if (estHypothese(parent)) {
            return parent;
        }
        Parent p = parents.get(parent);
        if (p == null) {
            return parent;
        }
        Set<Fait> s = p.enfants();
        Fait[] enfants = s.toArray(new Fait[s.size()]);
        for (int i = 0; i < enfants.length; ++i) {
            Fait enfant = enfants[i];
            Fait n = ecrirePreuveFinale(enfant);
            Regle r = p.regle(enfant);
            Fait h = p.hypothese(enfant);
            p.retirer(enfant);
            p.ajouter(n, r, h);
        }
        Regle[] r = p.reglesFinales(points, this);
        for (int i = 0; i < r.length; ++i) {
            Rapport.add(r[i].toString());
        }
        return r[0].getConclusion();
    }

    /**
	 * Retourne si la memoire est insuffisante.
	 * @return resultat.
	 */
    public boolean memoireInsuffisante() {
        return false;
    }

    /**
	 * Finalisation du rapport.
	 */
    protected void finaliserRapport() {
        ajouterEtatAuRapport();
        ajouterStatistiquesAuRapport();
    }

    /**
	 * Ajoute l'etat final au rapport.
	 */
    protected void ajouterEtatAuRapport() {
        Rapport.add("\n-- Etat final --");
        if (interrupt) {
            etatFinal = TEMPS_ECOULE;
            Rapport.add("Le delai maximal est ecoule.");
        } else if (!contientHypotheses()) {
            etatFinal = NON_RESOLU;
            Rapport.add("Le probleme n'a pas ete resolu.");
        } else if (memoireInsuffisante()) {
            etatFinal = MEMOIRE_TROP_PETITE;
            Rapport.add("La memoire est insuffisante.");
        } else {
            etatFinal = RESOLU;
            Rapport.add("Le probleme a ete resolu.");
            Regle r = Regle.creer("Nom", probleme.getHypotheses(), probleme.getConclusion());
            Rapport.add(r.toString());
        }
    }

    /**
	 * Ajout des statistiques au rapport.
	 */
    protected void ajouterStatistiquesAuRapport() {
        Rapport.add("\n-- Statistiques --");
        Rapport.add("- " + tempsEcoule + " ms.");
        Rapport.add("- " + points.points.size() + " points.");
        Rapport.add("- " + (anciensFaits.size() + nouveauxFaits.size()) + " faits.");
        Rapport.add("- " + (parents.size()) + " regles appliquees.");
    }

    /**
	 * Permet d'interrompre la resolution du probleme.
	 */
    protected void interrompre() {
        interrupt = true;
        tempsEcoule = System.currentTimeMillis() - tempsEcoule;
    }
}
