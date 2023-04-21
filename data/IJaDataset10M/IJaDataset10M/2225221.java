package classesbasicas;

public class MusicaNaColecao {

    private int idMusicaNaColecao;

    private String nomeMusica;

    private String letraMusica;

    private int duracaoMusica;

    private String observacaoMusica;

    private String nomeArquivoMusica;

    private int numeroDVD;

    private int numeroFaixa;

    private String cantor;

    private String assunto;

    private String tipo;

    private String qualidade;

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCantor() {
        return cantor;
    }

    public void setCantor(String cantor) {
        this.cantor = cantor;
    }

    public int getDuracaoMusica() {
        return duracaoMusica;
    }

    public void setDuracaoMusica(int duracaoMusica) {
        this.duracaoMusica = duracaoMusica;
    }

    public int getIdMusicaNaColecao() {
        return idMusicaNaColecao;
    }

    public void setIdMusicaNaColecao(int idMusicaNaColecao) {
        this.idMusicaNaColecao = idMusicaNaColecao;
    }

    public String getLetraMusica() {
        return letraMusica;
    }

    public void setLetraMusica(String letraMusica) {
        this.letraMusica = letraMusica;
    }

    public String getNomeArquivoMusica() {
        return nomeArquivoMusica;
    }

    public void setNomeArquivoMusica(String nomeArquivoMusica) {
        this.nomeArquivoMusica = nomeArquivoMusica;
    }

    public String getNomeMusica() {
        return nomeMusica;
    }

    public void setNomeMusica(String nomeMusica) {
        this.nomeMusica = nomeMusica;
    }

    public int getNumeroDVD() {
        return numeroDVD;
    }

    public void setNumeroDVD(int numeroDVD) {
        this.numeroDVD = numeroDVD;
    }

    public int getNumeroFaixa() {
        return numeroFaixa;
    }

    public void setNumeroFaixa(int numeroFaixa) {
        this.numeroFaixa = numeroFaixa;
    }

    public String getObservacaoMusica() {
        return observacaoMusica;
    }

    public void setObservacaoMusica(String observacaoMusica) {
        this.observacaoMusica = observacaoMusica;
    }

    public String getQualidade() {
        return qualidade;
    }

    public void setQualidade(String qualidade) {
        this.qualidade = qualidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Object clone() {
        MusicaNaColecao inst = new MusicaNaColecao();
        inst.idMusicaNaColecao = this.idMusicaNaColecao;
        inst.nomeMusica = this.nomeMusica == null ? null : new String(this.nomeMusica);
        inst.letraMusica = this.letraMusica == null ? null : new String(this.letraMusica);
        inst.duracaoMusica = this.duracaoMusica;
        inst.observacaoMusica = this.observacaoMusica == null ? null : new String(this.observacaoMusica);
        inst.nomeArquivoMusica = this.nomeArquivoMusica == null ? null : new String(this.nomeArquivoMusica);
        inst.numeroDVD = this.numeroDVD;
        inst.numeroFaixa = this.numeroFaixa;
        inst.cantor = this.cantor == null ? null : new String(this.cantor);
        inst.assunto = this.assunto == null ? null : new String(this.assunto);
        inst.tipo = this.tipo == null ? null : new String(this.tipo);
        inst.qualidade = this.qualidade == null ? null : new String(this.qualidade);
        return inst;
    }

    /**
	 * Override hashCode.
	 *
	 * @return the Objects hashcode.
	 */
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + idMusicaNaColecao;
        hashCode = 31 * hashCode + (nomeMusica == null ? 0 : nomeMusica.hashCode());
        hashCode = 31 * hashCode + (letraMusica == null ? 0 : letraMusica.hashCode());
        hashCode = 31 * hashCode + duracaoMusica;
        hashCode = 31 * hashCode + (observacaoMusica == null ? 0 : observacaoMusica.hashCode());
        hashCode = 31 * hashCode + (nomeArquivoMusica == null ? 0 : nomeArquivoMusica.hashCode());
        hashCode = 31 * hashCode + numeroDVD;
        hashCode = 31 * hashCode + numeroFaixa;
        hashCode = 31 * hashCode + (cantor == null ? 0 : cantor.hashCode());
        hashCode = 31 * hashCode + (assunto == null ? 0 : assunto.hashCode());
        hashCode = 31 * hashCode + (tipo == null ? 0 : tipo.hashCode());
        hashCode = 31 * hashCode + (qualidade == null ? 0 : qualidade.hashCode());
        return hashCode;
    }
}
