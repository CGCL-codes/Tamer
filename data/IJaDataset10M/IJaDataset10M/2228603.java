package modelo.armas;

public class BandoEnemigo implements Actitud {

    public Balas disparar(int danio, int alcance) {
        return new BalasEnemigas(danio, alcance);
    }
}
