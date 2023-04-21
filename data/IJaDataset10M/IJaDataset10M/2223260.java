package br.com.jops.cdp;

/**
 * Created by IntelliJ IDEA.
 * User: Welton
 * Date: 17/08/2007
 * Time: 03:32:59
 * Entidade Grupo
 */
public class Grupo {

    /**
     * Identificador unico
     * da entidade
     */
    private String id;

    /**
     * Nome ou denomina��o do grupo
     */
    private String nome;

    public Grupo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
