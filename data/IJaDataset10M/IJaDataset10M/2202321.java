package MotorRegras;

import MotorGrafico.Indice;
import MotorGrafico.Tabuleiro;

class RegrasCavalo {

    /**
	 * Verifica se o movimento do cavalo � v�lido.
	 * 
	 * @param t
	 *            Tabuleiro
	 * @param origem
	 *            Posi��o da pe�a que se quer validar o movimento.
	 * @param alvo
	 *            Posi��o alvo da movimenta��o da pe�a.
	 * @return true, se movimento v�lido, false, ao contr�rio.
	 */
    public static boolean valMovCavalo(Indice origem, Indice alvo, Tabuleiro t) {
        int descI = origem.i - alvo.i;
        int descJ = origem.j - alvo.j;
        Indice deslocador = new Indice(0, 0);
        String Sdeslocador = Integer.toString(descI) + Integer.toString(Math.abs(descJ));
        if (movimentoCavalo(Sdeslocador)) {
            deslocador.set(descI, descJ);
            return true;
        }
        return false;
    }

    /**
	 * Possiveis Movimenta��es do Cavalo.
	 * 
	 * @param Sdeslocador
	 *            - Deslocamento do cavalo
	 * @return boolean True, se movimento � v�lido, false ao contr�rio.
	 */
    public static boolean movimentoCavalo(String Sdeslocador) {
        switch(Math.abs(Integer.parseInt(Sdeslocador))) {
            case 8:
                return true;
            case 19:
                return true;
            case 21:
                return true;
            case 12:
                return true;
            default:
                return false;
        }
    }
}
