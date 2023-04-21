package it.unipg.bipod.dataModel;

/**
 * Corso mappa un'entita' "corso" di BiPoD.<br>
 * Le sue proprieta' rispecchiano 1-1 i campi dell'entia' corrispondente.
 * 
 * @author Lorenzo Porzi
 * 
 */
public class Corso {

    private int idCorso;

    private String nome;

    /**
	 * Costruisce un nuovo corso "vuoto".
	 */
    public Corso() {
        this(0, "");
    }

    /**
	 * Costruisce un bando con le proprieta' specificate.
	 * 
	 * @param idCorso
	 * @param nome
	 */
    public Corso(int idCorso, String nome) {
        setIdCorso(idCorso);
        setNome(nome);
    }

    public void setIdCorso(int idCorso) {
        this.idCorso = idCorso;
    }

    public int getIdCorso() {
        return idCorso;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object corso) {
        if (corso instanceof Corso) return getIdCorso() == ((Corso) corso).getIdCorso();
        return false;
    }
}
