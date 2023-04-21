package serverside.local;

import java.io.Serializable;

/**
 * Classe giocatore; NOTA: l'id viene assegnato dal server nel momento
 * in cui riceve una richiesta
 * @author nicola
 */
public class Player implements Serializable {

    public int id;

    public String name;

    public String toString() {
        return "Giocatore " + id + " - nome: " + name;
    }
}
