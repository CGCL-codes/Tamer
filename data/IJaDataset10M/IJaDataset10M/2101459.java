package core;

public class DocumentoIdentidade {

    private int numeroDocumento;

    private String dataEmissao;

    private String orgaoEmissor;

    private String uf;

    public DocumentoIdentidade() {
    }

    public DocumentoIdentidade(int numero, String data, String orgao, String uf) {
        this.numeroDocumento = numero;
        this.dataEmissao = data;
        this.orgaoEmissor = orgao;
        this.uf = uf;
    }

    public void setNumeroDocumento(int numero) {
        this.numeroDocumento = numero;
    }

    public void setDataDeEmissao(String data) {
        this.dataEmissao = data;
    }

    public void setOrgaoEmissor(String orgao) {
        this.orgaoEmissor = orgao;
    }

    public void setUFEmissor(String uf) {
        this.uf = uf;
    }
}
