package CompDomini;

import javax.swing.UIManager;
import java.awt.*;
import Vistes.*;
import CompVistes.*;

/**
 * <p>Title: Inicializador de la aplicacion</p>
 * <p>Description: Inicializa la aplicacion</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Gustavo Moreno Calvo
 * @version 1.0
 */
public class iniAp {

    int nTipusUsuari = 0;

    /**
   * Inicializa la aplicacion
   */
    public iniAp() {
        VisAcceso visAUser = new VisAcceso();
        visAUser.visualitzar();
        while (visAUser.isVisible()) {
        }
        this.nTipusUsuari = visAUser.getTipoUser();
    }

    /**
  * Consulta tipo de usuario que ha entrado al sistema
  * @return 0-> normal; 1->administrador
  */
    public int getTipusUsuari() {
        return (this.nTipusUsuari);
    }
}
