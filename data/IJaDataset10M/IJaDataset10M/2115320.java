package tec;

public class PassagerStandard extends PassagerAbstrait {

    public PassagerStandard(String Nom, int destination) {
        super(Nom, destination);
    }

    public PassagerStandard(int destination) {
        super(destination);
    }

    PassagerStandard(String Nom, int destination, EtatPassager.Etat Etat) {
        super(Nom, destination, Etat);
    }

    void choixPlaceMontee(Bus bus) {
        if (bus.aPlaceAssise()) bus.demanderPlaceAssise(this); else if (bus.aPlaceDebout()) bus.demanderPlaceDebout(this);
    }

    void choixChangerPlace(Bus b, int arret) {
    }
}
