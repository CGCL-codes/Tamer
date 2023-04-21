package interfaz;

import dominio.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        Sistema sistema = new Sistema(Sistema.PATH + "/data/data.dat");
        VentanaPrincipal vp = new VentanaPrincipal(sistema);
        vp.setVisible(true);
    }
}
