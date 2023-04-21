package model;

public class umUsuario {

    /**
     * Propriedade fixa e necessaria para identificar o registro
     */
    private int oid;

    private String nome;

    private String login;

    private String senha;

    /**
     * Construtor dessa classe
     */
    public umUsuario() {
    }

    /**
    * @return oid
    */
    public int getOid() {
        return oid;
    }

    /**
    * @param umResponsavel
    */
    public void setOid(int oid) {
        this.oid = oid;
    }

    /**
     * @return Nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param umNome
     */
    public void setNome(String umNome) {
        nome = umNome;
    }

    /**
     * @return Login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param umLogin
     */
    public void setLogin(String umLogin) {
        login = umLogin;
    }

    /**
     * @return Senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param umSenha
     */
    public void setSenha(String umSenha) {
        senha = umSenha;
    }
}
