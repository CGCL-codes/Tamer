package amplia;

import unbbayes.prs.Node;

public final class DiagnosticoAssociado {

    private Node nodoAssociado;

    private String diagnostico;

    public DiagnosticoAssociado(Node nodoAssociado, String diagnostico) {
        this.nodoAssociado = nodoAssociado;
        this.diagnostico = diagnostico;
    }

    public Node getNodoAssociado() {
        return nodoAssociado;
    }

    public void setNodoAssociado(Node nodoAssociado) {
        this.nodoAssociado = nodoAssociado;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
}
