package Donnees;

public class Joueur {

    protected static int idAuto = 1;

    private String nom;

    protected int id, score, nbWagons;

    protected EnsembleCartes main;

    protected EnsembleCartes objectifs;

    public Joueur(String nom) {
        this.id = idAuto;
        this.nom = nom;
        this.score = 0;
        this.nbWagons = 45;
        idAuto++;
        main = new EnsembleCartes("Main de " + nom, "wagon");
        objectifs = new EnsembleCartes("Objectifs de " + nom, "objectif");
        System.out.println("Creation de: " + this);
    }

    public int GetNbCartesWagonsCouleur(int couleur) {
        int nb = 0;
        for (int i = 0; i < main.getNbCartes(); i++) {
            CarteWagon c = (CarteWagon) main.GetCartes().get(i);
            if (c.getIntCouleur() == couleur) {
                nb++;
            }
        }
        return nb;
    }

    public void RetirerWagon() {
        this.nbWagons--;
    }

    public void JouerCarte(int couleur) {
        for (int i = 0; i < main.getNbCartes(); i++) {
            CarteWagon c = (CarteWagon) main.GetCartes().get(i);
            if (couleur == c.getIntCouleur()) {
                main.RetirerCarte(c);
                break;
            }
        }
        System.out.println(nom + " defausse une carte de couleur " + couleur);
    }

    public int nbCartesWagons() {
        return main.getNbCartes();
    }

    public int GetNbCartesObjectifs() {
        return objectifs.getNbCartes();
    }

    public int GetNbCartesWagonsCouleur(String couleur) {
        int cpt = 0;
        for (int i = 0; i < main.getNbCartes(); i++) {
            CarteWagon c = (CarteWagon) main.GetCartes().get(i);
            if (couleur.equalsIgnoreCase(c.getStringCouleur())) {
                cpt++;
            }
        }
        return cpt;
    }

    public EnsembleCartes getWagons() {
        return main;
    }

    public EnsembleCartes getObjectifs() {
        return objectifs;
    }

    public void PiocherWagon(EnsembleCartes e) {
        Carte c = e.RetirerCarte();
        System.out.println(nom + " pioche " + c.toString());
        main.ajouterCarte(c);
    }

    public void PiocherObjectif(EnsembleCartes e) {
        Carte c = e.RetirerCarte();
        System.out.println(nom + " pioche " + c.toString());
        objectifs.ajouterCarte(c);
    }

    public int getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public int getScore() {
        return this.score;
    }

    public int getNbWagons() {
        return this.nbWagons;
    }

    public String toString() {
        return "Le joueur n�" + getId() + " portant le nom: " + getNom() + ", possedant " + getNbWagons() + " wagons et ayant un score de " + getScore() + ".";
    }

    public void Resume() {
        System.out.println("=============");
        System.out.println("Joueur : " + this.nom);
        System.out.println("Wagons restants : " + this.nbWagons);
        System.out.println("Cartes Objectifs (" + objectifs.getNbCartes() + "): ");
        for (int i = 0; i < objectifs.getNbCartes(); i++) {
            System.out.println("--" + objectifs.GetCarteI(i).getId());
        }
        System.out.println("________________");
        System.out.println("Cartes Wagons (" + main.getNbCartes() + "): ");
        for (int i = 0; i < main.getNbCartes(); i++) {
            CarteWagon cw = (CarteWagon) main.GetCarteI(i);
            System.out.println("--" + cw.getId() + " , " + cw.getStringCouleur());
        }
        System.out.println("=============");
    }
}
