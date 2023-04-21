package creadores;

import utilitarios.PuntoXY;
import modelo.Bonus;
import modelo.Escenario;

public interface CreadorBonus {

    Bonus crear(Escenario escenario, PuntoXY posicion);
}
