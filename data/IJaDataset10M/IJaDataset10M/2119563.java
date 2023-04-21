package model;

import java.util.Collection;

public class Professor_Recomendante {

    private static final long serialVersionUID = 1L;

    private String nome;

    private String email;

    private String areaDeAtuacao;

    private Collection<Carta> cartas;

    private String instituicao;

    private String telefone;

    private String endereco;

    private String senha;

    private int id;

    /**
		 * Retorna o id do professor. O id e apenas um Identificador. Provavelmente sera usado apenas para
		 * funcionamento interno do programa.
		 * @return id Identificador do professor
		 */
    public int getId() {
        return id;
    }

    /**
		  * Muda o id do professor.
		  * @param id Novo identificador do professor
		  */
    public void setId(int id) {
        this.id = id;
    }

    /**
		 * Retorna o endereco eletronico do professor recomendante
		 * @return email e-mail do professor
		 */
    public String getEmail() {
        return email;
    }

    /**
		 * Retorna a senha de acesso do professor
		 * @return senha Senha de acesso para o professor
		 */
    public String getSenha() {
        return senha;
    }

    /**
		 * Muda a senha de acesso do professor
		 * @param senha Nova senha de acesso
		 */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
	     * Muda o endereco eletronico do recomendante
	     * @param email Novo e-mail do recomendante
	     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
		 * Retorna o endereco fisico do recomendante
		 * @return endereco Endereco do professor
		 */
    public String getEndereco() {
        return endereco;
    }

    /**
		 * Muda o endereco fisico do professor
		 * @param endereco Novo endereco
		 */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
		 * Retorna o nome do recomendante
		 * @return Nome do professor
		 */
    public String getNome() {
        return nome;
    }

    /**
		 * Muda o nome do professor
		 * @param nome Novo nome do recomendante
		 */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
		 * Retorna o telefone do recomendante
		 * @return telefone Telefone para contato do professor
		 */
    public String getTelefone() {
        return telefone;
    }

    /**
		 * Altera o telefone do professor recomedante
		 * @param telefone Novo telefone para contato
		 */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
