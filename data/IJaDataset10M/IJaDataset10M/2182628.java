package modelo.naves;

import vista.VistaFactory;
import vista.VistaMovil;
import modelo.PuntosInteligentes;
import modelo.util.Vector;

public class CazaEspecial extends Caza {

    public CazaEspecial(Vector pos, Vector dir) {
        super(pos, dir);
    }

    public void chocarConAlgo42(NaveAlgo42 algo42) {
        super.chocarConAlgo42(algo42);
        puntos = new PuntosInteligentes();
    }

    public VistaMovil getVista(VistaFactory vistaFactory) {
        return vistaFactory.getVistaCazaEspecial(this);
    }
}
