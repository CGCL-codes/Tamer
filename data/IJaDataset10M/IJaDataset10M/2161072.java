package creadores;

import modelo.Escenario;
import modelo.TanqueEnemigo;
import modelo.TanqueMirage;
import utilitarios.PuntoXY;

public class CreadorTanqueMirage implements CreadorEnemigos {

    @Override
    public TanqueEnemigo crear(Escenario escenario, PuntoXY posicion) {
        return new TanqueMirage(escenario, posicion, escenario.getCuartel());
    }
}
