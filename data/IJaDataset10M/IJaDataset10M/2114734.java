package Model;

import java.io.Serializable;

public class Contato implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;

    private String email;

    private String telefone;

    private String endereco;

    /**
	  * Construtor da classe.
	  * Cada contato tera um nome, e-mail, telefone e endereco.
	  * @param nome Nome do candidato
	  * @param email Endereco eletronico
	  * @param telefone Telefone para contato
	  * @param endereco Endereco do candidato
	  */
    public Contato(String nome, String email, String telefone, String endereco) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Contato() {
    }

    /**
	  * Retorna o endereco eletronico do candidato.
	  * @return email E-mail do candidato
	  */
    public String getEmail() {
        return email;
    }

    /**
     * Altera o e-mail do candidato
     * @param email Novo e-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * Retorna o endereco fisico do candidato
	 * @return endereco  Endereco fisico
	 */
    public String getEndereco() {
        return endereco;
    }

    /**
	 * Altera o endereco do candidato.
	 * @param endereco Novo endereco
	 */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
	 * Retorna o nome do candidato.
	 * @return nome Nome do candidato
	 */
    public String getNome() {
        return nome;
    }

    /**
	 * Altera o nome do Candidato.
	 * @param nome Novo nome do candidato
	 */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
	 * Retorna o telefone do candidato, se houver.
	 * @return telefone Telfone para contato
	 */
    public String getTelefone() {
        return telefone;
    }

    /**
	 * Altera o telefone do candidato.
	 * @param telefone Novo telefone para contato
	 */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
