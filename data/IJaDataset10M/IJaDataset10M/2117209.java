package propres;

import java.util.*;

/**
 *
 * @author neochange
 */
public class CtrlVistalimento {

    private CtrlDominioalimento driver;

    public CtrlVistalimento() {
        driver = new CtrlDominioalimento();
    }

    public CtrlDominioalimento get_Ctrldom() {
        return driver;
    }

    public List<String> gestion_evento(String acc, String[] caix, boolean[] G, boolean[] Q) throws Exception {
        List<String> P = new ArrayList();
        if (acc.equals("Añadir alimento")) {
            P = driver.intalimento(caix, G, Q);
        } else if (acc.equals("Modificar alimento")) {
            P = driver.modalimento(caix, G, Q);
        } else if (acc.equals("Consultar alimento")) {
            P = driver.consalimento(caix[0]);
        } else if (acc.equals("Eliminar alimento")) {
            P = driver.elialimento(caix[0]);
        } else if (acc.equals("Consultar alimentos")) {
            P = driver.listalimentos();
        } else if (acc.equals("Guardar disco")) {
            P = driver.guardar(caix[0]);
        } else if (acc.equals("Cargar disco")) {
            P = driver.cargar(caix[0]);
        }
        return P;
    }

    public List<String> lalims() throws Exception {
        return driver.listalimentos();
    }
}
