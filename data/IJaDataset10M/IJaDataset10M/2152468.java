package dades;

import java.util.Calendar;

public class RepositoriFranges extends Controlador<Calendar, domini.FranjaHoraria> {

    public RepositoriFranges() {
        super("domini.FranjaHoraria", "RepositoriFranges.db");
    }

    /**
     *  Obte la franja a que es troba en la posicio especificada, si es possible. 
     *  @param index La posicio de la franja a obtenir. 
     *  @pre    index >= 0 i menor que el nombre total de franges.
     *  @post   -
     *  @return El Programa que es troba a l'index, null si no hi és.
     */
    public Object getFranja(int index) {
        return listaObject().get(index);
    }

    /**
     * Donat un nom de fitxer, guarda a disc totes les franges del repositori
     * a disc.
     * @param nomFitxer On es guardaran les franges.
     * @return cert Si s'han guardat correctament
     * @pre El repositori de franges no es buit.
     * @post S'han guardat les franges al fitxer de disc.
     * @throws dades.GestorDiscException
     */
    public boolean exportaFranges(String nomFitxer) throws GestorDiscException {
        actualizar(nomFitxer);
        return true;
    }

    /**
     * Donat un nom de fitxer, carrega a memoria totes les franges del
     * repositori que esta a disc.
     * @param nomFitxer On es troba el repositori de franges
     * @pre El fitxer existeix i es valid
     * @post S'han carregat les franges a memoria
     * @return cert si s'ha fet correctament
     */
    public boolean importaFranges(String nomFitxer) {
        return carregarDades(nomFitxer);
    }
}
