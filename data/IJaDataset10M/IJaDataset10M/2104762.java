package debug.javaframework.capadeaplicación.utilidades;

import javaframework.capadeaplicación.utilidades.PropiedadesDelSistema;

public class Test_PropiedadesDelSistema {

    public static void main(String[] args) {
        final PropiedadesDelSistema PS = new PropiedadesDelSistema(null);
        System.out.println(PS.getNombreSistemaOperativo());
        System.out.println(PS.getMemoriaDisponibleParaLaJVM());
        PS.liberarRecursos();
    }
}
