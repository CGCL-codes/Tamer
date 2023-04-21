package br.com.fabrica_ti.model;

public interface IUsuario {

    public void setIdUsuario(Integer idUsuario);

    public Integer getIdUsuario();

    public String getTelefone();

    public void setTelefone(String telefone);

    public String getNome();

    public void setNome(String nome);

    public String getCpf();

    public void setCpf(String cpf);

    public String getEmail();

    public void setEmail(String email);

    public String getLogin();

    public void setLogin(String login);

    public String getSenha();

    public void setSenha(String senha);

    public void entrarSistema();

    public void sairSistema();
}
