package br.unioeste.modelo;

/**
 *
 * @author Moises
 */
public class TVege {

    int id_tvege;

    String nome_tvege;

    public TVege(int id_tvege, String nome_tvege) {
        this.id_tvege = id_tvege;
        this.nome_tvege = nome_tvege;
    }

    public TVege() {
        this.id_tvege = 0;
        this.nome_tvege = new String();
    }

    public int getId_tvege() {
        return id_tvege;
    }

    public void setId_tvege(int id_tvege) {
        this.id_tvege = id_tvege;
    }

    public String getNome_tvege() {
        return nome_tvege;
    }

    public void setNome_tvege(String nome_tvege) {
        this.nome_tvege = nome_tvege;
    }
}
