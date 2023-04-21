package laLogica;

import capaDatosejb.VacasFacadeLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import capaDatos.Vacas;

/**
 *
 * @author user
 */
@Stateless
public class AgregacionVaca implements AgregacionVacaLocal {

    @EJB
    private VacasFacadeLocal vacasFacade;

    public void NuevaVaca(String codVaca) {
        Vacas lavaca = new Vacas();
        lavaca.setCodigo(codVaca);
        vacasFacade.create(lavaca);
    }
}
