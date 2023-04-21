package br.org.ged.direto.model.entity.dwr;

public class CarteiraDWR {

    private int idCarteira;

    private String cartDesc;

    private String cartAbr;

    private int idFuncao;

    private int idSecao;

    private int idOM;

    public int getIdCarteira() {
        return idCarteira;
    }

    public void setIdCarteira(int idCarteira) {
        this.idCarteira = idCarteira;
    }

    public String getCartDesc() {
        return cartDesc;
    }

    public void setCartDesc(String cartDesc) {
        this.cartDesc = cartDesc;
    }

    public String getCartAbr() {
        return cartAbr;
    }

    public void setCartAbr(String cartAbr) {
        this.cartAbr = cartAbr;
    }

    public int getIdFuncao() {
        return idFuncao;
    }

    public void setIdFuncao(int idFuncao) {
        this.idFuncao = idFuncao;
    }

    public int getIdSecao() {
        return idSecao;
    }

    public void setIdSecao(int idSecao) {
        this.idSecao = idSecao;
    }

    public int getIdOM() {
        return idOM;
    }

    public void setIdOM(int idOM) {
        this.idOM = idOM;
    }
}
