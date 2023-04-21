package br.pucpr.reidasruas.dto;

public class RetornoBasico {

    private int codMensagem;

    private String descMensagem;

    private boolean isTrue;

    public RetornoBasico(int codMensagem, String descMensagem) {
        super();
        this.codMensagem = codMensagem;
        this.descMensagem = descMensagem;
    }

    public RetornoBasico(String descMensagem) {
        super();
        this.descMensagem = descMensagem;
    }

    public RetornoBasico(boolean isTrue) {
        super();
        this.isTrue = isTrue;
    }

    public int getCodMensagem() {
        return codMensagem;
    }

    public void setCodMensagem(int codMensagem) {
        this.codMensagem = codMensagem;
    }

    public String getDescMensagem() {
        return descMensagem;
    }

    public void setDescMensagem(String descMensagem) {
        this.descMensagem = descMensagem;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean isTrue) {
        this.isTrue = isTrue;
    }
}
