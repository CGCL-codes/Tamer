package MotorRegras;

import MotorGrafico.Indice;
import MotorGrafico.Tabuleiro;

class RegrasTorre {

    public static boolean valMovTorre(Indice origem, Indice alvo, Tabuleiro t) {
        int descI = origem.i - alvo.i;
        int descJ = origem.j - alvo.j;
        Indice deslocador = new Indice(0, 0);
        int quantCasasDeslocar = 0;
        if (descI != 0 && descJ != 0) return false;
        if (descJ == 0) {
            quantCasasDeslocar = Math.abs(descI);
            if (descI > 0) deslocador.set(-1, 0); else deslocador.set(1, 0);
        } else {
            quantCasasDeslocar = Math.abs(descJ);
            if (descJ > 0) deslocador.set(0, -1); else deslocador.set(0, 1);
        }
        return !Regras.haPecaNoCaminho(deslocador, origem, quantCasasDeslocar, t);
    }
}
