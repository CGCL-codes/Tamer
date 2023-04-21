package colonsdelutbm;

/** G�re un controle continu, sa position, son appartenance*/
public class ControleContinu {

    public ControleContinu(int xG, int yG, int xD, int yD, int xDB, int yDB, int xGB, int yGB) {
        positionGauche = new Position(xG, yG);
        positionDroitBas = new Position(xDB, yDB);
        positionDroit = new Position(xD, yD);
        positionGaucheBas = new Position(xGB, yGB);
        status = TypeCC.nonaffiche;
    }

    public ControleContinu(Position positionG, Position positionD, Position positionDB, Position positionGB) {
        positionGauche = new Position(positionG);
        positionDroit = new Position(positionD);
        positionDroitBas = new Position(positionDB);
        positionGaucheBas = new Position(positionGB);
        status = TypeCC.nonaffiche;
    }

    public ControleContinu(ControleContinu Controle) {
        positionGauche = Controle.positionGauche;
        positionDroitBas = Controle.positionDroitBas;
        positionDroit = Controle.positionDroit;
        positionGaucheBas = Controle.positionGaucheBas;
        status = Controle.status;
        joueur = Controle.joueur;
    }

    public enum TypeCC {

        nonaffiche, construit
    }

    ;

    protected TypeCC status;

    protected Position positionGauche, positionDroit, positionDroitBas, positionGaucheBas;

    protected int joueur;

    public boolean compter;
}
