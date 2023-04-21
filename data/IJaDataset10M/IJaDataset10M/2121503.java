package Classe;

/**
 *
 * @author gladson
 */
public class Estado {

    private String idUf;

    private String nomeEstado;

    private String faixaCepIni;

    private String faixaCepFim;

    private String faixaCep2Ini;

    private String faixaCep2Fim;

    public String toString() {
        return idUf;
    }

    public String getFaixaCep2Fim() {
        return faixaCep2Fim;
    }

    public void setFaixaCep2Fim(String faixaCep2Fim) {
        this.faixaCep2Fim = faixaCep2Fim;
    }

    public String getFaixaCep2Ini() {
        return faixaCep2Ini;
    }

    public void setFaixaCep2Ini(String faixaCep2Ini) {
        this.faixaCep2Ini = faixaCep2Ini;
    }

    public String getFaixaCepFim() {
        return faixaCepFim;
    }

    public void setFaixaCepFim(String faixaCepFim) {
        this.faixaCepFim = faixaCepFim;
    }

    public String getFaixaCepIni() {
        return faixaCepIni;
    }

    public void setFaixaCepIni(String faixaCepIni) {
        this.faixaCepIni = faixaCepIni;
    }

    public String getIdUf() {
        return idUf;
    }

    public void setIdUf(String idUf) {
        this.idUf = idUf;
    }

    public String getNomeEstado() {
        return nomeEstado;
    }

    public void setNomeEstado(String nomeEstado) {
        this.nomeEstado = nomeEstado;
    }
}
