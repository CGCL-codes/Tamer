package modeloConceptual;

import utiles.Utilidades;

public class Caravanserai implements IEdificioAccionStrategy {

    public Caravanserai() {
        new Edificio("Caravanserai", 3, 3);
    }

    @Override
    public String getNombre() {
        return "Caravanserai";
    }

    public void ejecuta(int num, Jugador j) {
        if (num == 0) {
            if (j.getNumMonedas() >= 3) {
                j.getTableroIndividual().construir(this.getNombre());
                j.setNumMonedas(j.getNumMonedas() - 3);
                Utilidades.imprime("N�mero de camellos actuales " + j.getNumCamellos());
                Utilidades.imprime("N�mero de monedas actuales " + j.getNumMonedas());
            } else {
                Utilidades.imprime("No se puede contruir el edificio porque no hay monedas suficientes");
            }
        } else {
            if (j.getNumCamellos() >= 3) {
                j.getTableroIndividual().construir(this.getNombre());
                j.setNumCamellos(j.getNumCamellos() - 3);
                Utilidades.imprime("N�mero de camellos actuales " + j.getNumCamellos());
                Utilidades.imprime("N�mero de monedas actuales " + j.getNumMonedas());
            } else {
                Utilidades.imprime("No se puede contruir el edificio porque no hay monedas suficientes");
            }
        }
    }

    @Override
    public Edificio getEdificio() {
        return null;
    }

    @Override
    public void setEdificio(Edificio edificio) {
    }
}
