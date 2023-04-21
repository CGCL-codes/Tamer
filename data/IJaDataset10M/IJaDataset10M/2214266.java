package main;

import Utilidades.Analizador;
import Utilidades.Tupla;
import XML.Diccionario;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Miguel González y Jaime Bárez
 * Clase encargada de las traducciones
 */
public class Traductor {

    Diccionario diccionario;

    Analizador analizador;

    public Traductor() {
        diccionario = new Diccionario();
        analizador = new Analizador();
    }

    public HashMap<String, String> getConsultasTraducidas(String consultaSQL) {
        HashMap<String, String> consultasTraducidas = new HashMap<String, String>();
        ArrayList<Tupla> consultaTroceada = analizador.desmembrar(consultaSQL);
        for (int i = 0; i < diccionario.numDiccionarios(); i++) {
            String cadenaSqlTraducida = "";
            String nombreDiccionario = diccionario.getNombreDiccionario(i);
            for (int j = 0; j < consultaTroceada.size(); j++) {
                if (consultaTroceada.get(j).isEsSeparador()) {
                    cadenaSqlTraducida += consultaTroceada.get(j).getPalabra();
                } else {
                    cadenaSqlTraducida += diccionario.getTraduccionPalabra(nombreDiccionario, consultaTroceada.get(j).getPalabra());
                }
            }
            consultasTraducidas.put(nombreDiccionario, cadenaSqlTraducida);
        }
        return consultasTraducidas;
    }
}
