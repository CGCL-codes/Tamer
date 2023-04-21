package etp.client;

import java.util.Date;

public class Apontamento extends Observable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4074271547601087777L;

    private String cod;

    private String codTarefa;

    private String codRecurso;

    private Date dataApontamento;

    private Date inicio;

    private Date fim;

    private float parcelaConcluida;

    private String descricao;

    public Apontamento(String codTarefa) {
        this.setCodTarefa(codTarefa);
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
        this.notifyObservers("cod");
    }

    public String getCodTarefa() {
        return codTarefa;
    }

    public void setCodTarefa(String codTarefa) {
        this.codTarefa = codTarefa;
        this.notifyObservers("codTarefa");
    }

    public Date getDataApontamento() {
        return dataApontamento;
    }

    public void setDataApontamento(Date dataApontamento) {
        this.dataApontamento = dataApontamento;
        this.notifyObservers("dataApontamento");
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
        this.notifyObservers("inicio");
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
        this.notifyObservers("fim");
    }

    public float getParcelaConcluida() {
        return parcelaConcluida;
    }

    public void setParcelaConcluida(float parcelaConcluida) {
        this.parcelaConcluida = parcelaConcluida;
        this.notifyObservers("parcelaConcluida");
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
        this.notifyObservers("parcelaConcluida");
    }
}
