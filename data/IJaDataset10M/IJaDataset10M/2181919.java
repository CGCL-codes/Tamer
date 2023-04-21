package tesis.test;

import tesis.extensiones.EventBuilder;
import tesis.extensiones.XMLContextoBusquedaReader;
import tesis.extensiones.XMLEventBuilderReader;

public class TestXMLContextoBusquedaReader {

    public static void main(String[] args) {
        String path = ".\\src\\tesis\\Ejemplo_05\\";
        EventBuilder eb = new EventBuilder(new XMLEventBuilderReader(path + "Events.xml"));
        XMLContextoBusquedaReader a = new XMLContextoBusquedaReader(path + "ProblemContext.xml", eb);
        System.out.println("Estado inicial: " + a.estadoInicial());
        System.out.println("Estados finales: " + a.estadoFinal());
        System.out.println("Transiciones: " + a.transiciones());
        System.out.println("Modo c: " + a.modoContexto());
    }
}
