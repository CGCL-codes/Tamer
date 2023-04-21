package br.org.model;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Gerente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long seq;

    private String nome;

    private Filial filial;

    public Gerente() {
        seq = new Long(new Random().nextInt());
    }

    public List getTodosGerente() {
        return null;
    }

    public Gerente getGerenteBySeq() {
        return null;
    }

    public void insertGerente() {
    }

    public void deletGerente() {
    }

    /**
	 * @return Returns the filial.
	 */
    public Filial getFilial() {
        return filial;
    }

    /**
	 * @param filial The filial to set.
	 */
    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    /**
	 * @return Returns the nome.
	 */
    public String getNome() {
        return nome;
    }

    /**
	 * @param nome The nome to set.
	 */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
	 * @return Returns the seq.
	 */
    public Long getSeq() {
        return seq;
    }

    /**
	 * @param seq The seq to set.
	 */
    public void setSeq(Long seq) {
        this.seq = seq;
    }
}
