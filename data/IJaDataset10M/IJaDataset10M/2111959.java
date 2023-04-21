package reservasdeportivas;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Shaka
 */
public class Clientes {

    public ArrayList DatCte = new ArrayList();

    String sCliente;

    public void llenaCtes() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Documents and Settings\\Administrador\\Mis documentos\\NetBeansProjects\\ReservasDeportivas\\src\\reservasdeportivas\\Clientes.clt"));
            while ((sCliente = br.readLine()) != null) {
                DatCte.add(sCliente);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error " + e.toString());
        }
    }
}
