package br.com.rnavarro.padroes.estrutural.adapter;

public class ClienteAntigo implements UsuarioAntigo {

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
