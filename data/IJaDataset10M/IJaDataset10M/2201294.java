package application.config;

public class Reference {

    public static enum TypeBien {

        Arme, Armure, Bouclier, Herbe, Nourriture, Objet, Poison, Service, Vehicule, Vetement
    }

    public static enum TypeArme {

        Corps, Distance
    }

    public static enum TailleArme {

        Petit, Moyen, Grand
    }

    public static enum Rarete {

        Toujours, Courant, Frequent, Parfois, Rare, TresRare, Jamais
    }

    public static String getValeurRarete(Rarete rarete) {
        if (rarete.equals(Rarete.Toujours)) return "*******";
        if (rarete.equals(Rarete.Courant)) return "******";
        if (rarete.equals(Rarete.Frequent)) return "*****";
        if (rarete.equals(Rarete.Parfois)) return "****";
        if (rarete.equals(Rarete.Rare)) return "***";
        if (rarete.equals(Rarete.TresRare)) return "**";
        if (rarete.equals(Rarete.Jamais)) return "*";
        return "?????";
    }

    public static Rarete getRarete(String valeur) {
        if (valeur.equalsIgnoreCase("*******")) return Rarete.Toujours;
        if (valeur.equalsIgnoreCase("******")) return Rarete.Courant;
        if (valeur.equalsIgnoreCase("*****")) return Rarete.Frequent;
        if (valeur.equalsIgnoreCase("****")) return Rarete.Parfois;
        if (valeur.equalsIgnoreCase("***")) return Rarete.Rare;
        if (valeur.equalsIgnoreCase("**")) return Rarete.TresRare;
        if (valeur.equalsIgnoreCase("*")) return Rarete.Jamais;
        return Rarete.Jamais;
    }

    public static enum TypePoison {

        Inhalation, Contact, Blessure, Ingestion
    }

    public static enum Incubation {

        Immediatement, un_round, une_minute, vingt_minutes, une_heure, six_heures, un_jour
    }

    public static enum DureePhase {

        Round, Minute, Heure, Jour, Semaine, Mois, Annee
    }

    public static enum Sexe {

        M, F
    }

    public static enum EtrePositionCorps {

        Debout, Assis, Accroupi, Allongé
    }

    public static enum EtrePositionRelativeEnnemi {

        Normal, PlusHaut, PlusBas
    }

    public static enum EtreTaille {

        Minuscule, Petit, Normal, Grand, Géant
    }

    private static int getNumTailleEtre(EtreTaille taille) {
        if (taille == EtreTaille.Minuscule) return 0;
        if (taille == EtreTaille.Petit) return 1;
        if (taille == EtreTaille.Normal) return 2;
        if (taille == EtreTaille.Grand) return 3;
        if (taille == EtreTaille.Géant) return 4;
        return 2;
    }

    public static int getDifferenceTaille(EtreTaille taille1, EtreTaille taille2) {
        return Reference.getNumTailleEtre(taille1) - Reference.getNumTailleEtre(taille2);
    }

    public static enum EtreTailleRelativeEnnemi {

        Identique, PlusGrand, PlusPetit
    }

    public static enum EtrePositionCouvert {

        Découvert, MiCouvert, Couvert, Caché
    }

    public static enum StatutAffichageSceneEtre {

        Affiché, Caché
    }

    public static enum TypeMagasin {

        Armurerie, Auberge, Batelier, Ecurie, Ferme, Herboristerie, Quincaillerie, Tailleur
    }

    public static enum NiveauVendeur {

        Débutant, Normal, Expert
    }

    public static enum ContexteEconomiqueMagasin {

        Abondance, Normal, Pénurie
    }

    public static enum TypeAction {

        Artisanat, Art, Bataille, Combat, Divers, Discrétion, Environnement, Erudition, Magie, Mouvement, Social, Soins
    }

    public static enum UniteAction {

        Action, Round, Minute, Heure, Jour, Mois, Année
    }

    public static enum TypeTest {

        Physique, Social, Académique
    }

    public static enum NiveauResultatTest {

        EchecCritique, Echec, Egalite, Reussite, ReussiteCritique
    }

    public static enum TypeFenetre {

        Etre, Regles, Equipement, OutilDeJeu, Divers
    }

    public static enum EtatMusique {

        Stop, Play, Pause
    }

    public static enum TypeImageFond {

        Unique, Redimensionnee, Mosaic
    }
}
