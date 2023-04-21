package classesbasicas;

import java.io.Serializable;
import java.util.Date;
import util.Util;

public class Log implements Serializable {

    private int idLog;

    private String chaveUnica;

    private String chaveUnicaObjeto;

    public enum TipoOperacao {

        CADASTRO, ALTERACAO, DELECAO, ADICAO_CANTOR_A_MUSICA
    }

    ;

    TipoOperacao tipoOperacao;

    private String classeObjeto;

    Object objeto;

    Date data;

    public String getClasseObjeto() {
        return classeObjeto;
    }

    public void setClasseObjeto(String classeObjeto) {
        this.classeObjeto = classeObjeto;
    }

    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public String getChaveUnica() {
        if (chaveUnica == null) {
            String s = "" + idLog + tipoOperacao.ordinal() + classeObjeto + objeto.toString() + data.getTime();
            chaveUnica = Util.gerarChaveUnica(s);
        }
        return chaveUnica;
    }

    public void setChaveUnica(String chaveUnica) {
        this.chaveUnica = chaveUnica;
    }

    public String getChaveUnicaObjeto() {
        return chaveUnicaObjeto;
    }

    public void setChaveUnicaObjeto(String chaveUnicaObjeto) {
        this.chaveUnicaObjeto = chaveUnicaObjeto;
    }
}
