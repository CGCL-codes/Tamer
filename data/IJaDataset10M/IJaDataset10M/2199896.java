package pf.alaudes.mastermahjong.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import pf.alaudes.mastermahjong.metier.impl.Combinaison;
import pf.alaudes.mastermahjong.metier.impl.Joueur;
import pf.alaudes.mastermahjong.metier.impl.Partie;
import pf.alaudes.mastermahjong.metier.impl.Tour;
import pf.alaudes.mastermahjong.metier.stats.impl.CombinaisonGagnante;
import pf.alaudes.mastermahjong.metier.stats.impl.StatJoueur;
import pf.alaudes.mastermahjong.service.ServiceMahjong;
import pf.alaudes.mastermahjong.service.dao.DaoImpl;

public class ServiceMahjongHibernate implements ServiceMahjong {

    private DaoImpl dao;

    private String config;

    @Override
    public void enregistrer(final Joueur joueur) {
        getDao().rendrePersistant(joueur);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Joueur> rechercherJoueurs() {
        return (List<Joueur>) getDao().rechercherTous(Joueur.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Combinaison> getCombinaisons() {
        return (List<Combinaison>) getDao().rechercherTous(Combinaison.class);
    }

    @Override
    public void enregistrer(final Combinaison combinaison) {
        Combinaison combinaisonEnBase = rechercherCombinaison(combinaison.getLibelle());
        if (combinaisonEnBase == null) {
            getDao().rendrePersistant(combinaison);
        }
    }

    public DaoImpl getDao() {
        return this.dao;
    }

    public void setDao(final DaoImpl dao) {
        this.dao = dao;
    }

    @Override
    public void enregistrer(Partie partie) {
        getDao().rendrePersistant(partie);
    }

    @Override
    public Joueur rechercherJoueur(final Long identifiant) {
        return (Joueur) getDao().rechercherUniqueRequete("from pf.alaudes.mastermahjong.metier.impl.Joueur joueur " + "where joueur.identifiantObjet=?", identifiant);
    }

    @Override
    public Partie rechercherPartie(Long identifiant) {
        return (Partie) getDao().rechercherUniqueRequete("from pf.alaudes.mastermahjong.metier.impl.Partie partie " + "where partie.identifiantObjet=?", identifiant);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Partie> rechercherParties() {
        return (List<Partie>) getDao().rechercherTous(Partie.class);
    }

    @Override
    public Tour rechercherTour(final Long identifiant) {
        return (Tour) getDao().rechercherUniqueRequete("from pf.alaudes.mastermahjong.metier.impl.Tour tour " + "where tour.identifiantObjet=?", identifiant);
    }

    @Override
    public void enregistrer(final Tour tour) {
        getDao().rendrePersistant(tour);
    }

    public void setConfigCombinaisons(final String config) {
        this.config = config;
    }

    public void initialiser() {
        String[] configs = StringUtils.split(this.config, ",");
        for (String config : configs) {
            enregistrer(new Combinaison(config));
        }
    }

    @Override
    public Collection<CombinaisonGagnante> creer(final Partie partie) {
        final Map<Combinaison, CombinaisonGagnante> resultat = new HashMap<Combinaison, CombinaisonGagnante>();
        for (Tour tour : partie.getTours()) {
            for (Combinaison combinaison : tour.getCombinaisons()) {
                CombinaisonGagnante combinaisonGagnante = resultat.get(combinaison);
                if (combinaisonGagnante == null) {
                    combinaisonGagnante = new CombinaisonGagnante();
                    combinaisonGagnante.setCombinaison(combinaison);
                }
                combinaisonGagnante.ajouter();
                resultat.put(combinaison, combinaisonGagnante);
            }
        }
        return resultat.values();
    }

    @Override
    public void terminer(final Partie partie) {
        final Collection<CombinaisonGagnante> resultat = creer(partie);
        for (CombinaisonGagnante combinaisonGagnante : resultat) {
            CombinaisonGagnante statEnBase = rechercherCombinaisonGagnante(combinaisonGagnante.getLibelleCombinaison());
            if (statEnBase == null) {
                enregistrer(combinaisonGagnante);
            } else {
                statEnBase.ajouter(combinaisonGagnante.getNombre());
                enregistrer(statEnBase);
            }
        }
        partie.setTermine(true);
        for (Joueur joueur : partie.getJoueurs()) {
            mettreAJourStat(joueur, partie);
        }
        enregistrer(partie);
    }

    private void mettreAJourStat(final Joueur joueur, final Partie partie) {
        StatJoueur statJoueur = rechercherStatJoueur(joueur.getLogin());
        if (statJoueur == null) {
            statJoueur = new StatJoueur();
            statJoueur.setJoueur(joueur);
        }
        statJoueur.mettreAJourPartie(partie);
        enregistrer(statJoueur);
    }

    private CombinaisonGagnante rechercherCombinaisonGagnante(final String libelleCombinaison) {
        final String sql = "from " + CombinaisonGagnante.class.getName() + " combinaisonGagnante " + "where combinaisonGagnante.libelleCombinaison = ?";
        return (CombinaisonGagnante) getDao().rechercherUniqueRequete(sql, libelleCombinaison);
    }

    private Combinaison rechercherCombinaison(final String libelle) {
        final String sql = "from " + Combinaison.class.getName() + " combinaison " + "where combinaison.libelle = ?";
        return (Combinaison) getDao().rechercherUniqueRequete(sql, libelle);
    }

    private StatJoueur rechercherStatJoueur(final String login) {
        final String sql = "from " + StatJoueur.class.getName() + " statJoueur " + "where statJoueur.joueur.login = ?";
        return (StatJoueur) getDao().rechercherUniqueRequete(sql, login);
    }

    private void enregistrer(final CombinaisonGagnante combinaisonGagnante) {
        getDao().rendrePersistant(combinaisonGagnante);
    }

    private void enregistrer(final StatJoueur statJoueur) {
        getDao().rendrePersistant(statJoueur);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CombinaisonGagnante> rechercherCombinaisonGagnantes() {
        return (List<CombinaisonGagnante>) getDao().rechercherTous(CombinaisonGagnante.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<StatJoueur> rechercherStatJoueurs() {
        return (List<StatJoueur>) getDao().rechercherTous(StatJoueur.class);
    }

    @Override
    public List<StatJoueur> rechercherStatJoueursParClassement(int nombreDeJoueurs) {
        return rechercherStatJoueursPar(nombreDeJoueurs, "points");
    }

    @Override
    public List<StatJoueur> rechercherStatJoueursParCombinaison(int nombreDeJoueurs) {
        return rechercherStatJoueursPar(nombreDeJoueurs, "nbPointMaxTour");
    }

    @SuppressWarnings("unchecked")
    private List<StatJoueur> rechercherStatJoueursPar(final int nombreDeJoueurs, final String propriete) {
        final StringBuilder hql = new StringBuilder("from ");
        hql.append(StatJoueur.class.getName());
        hql.append(" statJoueur order by statJoueur.");
        hql.append(propriete);
        hql.append(" desc max ");
        hql.append(nombreDeJoueurs);
        return (List<StatJoueur>) getDao().rechercher(hql.toString());
    }
}
