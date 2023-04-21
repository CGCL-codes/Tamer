package minimundo.saude;

import java.util.List;
import minimundo.geografia.Endereco;

/**
 *
 * @author Welington Rocha
 */
public class Posto_Saude {

    private Endereco endereço;

    private String nome;

    private List<Ficha_Cadastro> usuarios;

    /**
     * @return the endereço
     */
    public Endereco getEndereço() {
        return endereço;
    }

    /**
     * @param endereço the endereço to set
     */
    public void setEndereço(Endereco endereço) {
        this.endereço = endereço;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the usuarios
     */
    public List<Ficha_Cadastro> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(List<Ficha_Cadastro> usuarios) {
        this.usuarios = usuarios;
    }
}
