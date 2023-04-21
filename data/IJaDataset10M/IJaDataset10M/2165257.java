package tabellone;

import interfaccia.DataOption;
import partita.Player;

/**
 * Classe deputata alla gestione delle caselle speciali come 
 * "Tassa di lusso" e "Tassa patrimoniale".
 * @author Delfino & Di Marco
 *
 */
public class Tasse_Crediti extends Speciali {

    private int var_credito;

    /**
	 * Costruttore della casella speciale.
	 * @param pos Posizione della casella
	 * @param name Nome della casella
	 * @param valore Variazione economica imposta dalla casella
	 */
    Tasse_Crediti(int pos, String name, int valore) {
        super(pos, name);
        var_credito = valore;
    }

    /**
	 * Getter della variazione economica.
	 * @return La variazione economica imposta dalla casella.
	 */
    public int getCredito() {
        return var_credito;
    }

    public DataOption evento(Player giocatore) {
        DataOption opzioni = new DataOption(this, getNome(), getCredito(), 0);
        return opzioni;
    }
}
