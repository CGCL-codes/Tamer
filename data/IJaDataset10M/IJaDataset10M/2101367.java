package br.unb.cic.gerval.client;

public class Atributo {

    private String descricao;

    private String id;

    public Atributo(String name, String text) {
        setId(name);
        setDescricao(text);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
