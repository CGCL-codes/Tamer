package model.business;

public class OrcamentoFrio extends model.business.Orcamento {

    public OrcamentoFrio() {
        super();
        this.kind = 1;
    }

    public Ordem copy() {
        OrcamentoFrio oc = new OrcamentoFrio();
        oc.setSetor(this.getSetor());
        oc.setAtencao(this.getAtencao());
        oc.setBisel(this.getBisel());
        oc.setCliente(this.getCliente());
        oc.setCondicaoPagamento(this.getCondicaoPagamento());
        oc.setCorte(this.getCorte());
        oc.setDataEntrada(this.getDataEntrada());
        oc.setEmail(this.getEmail());
        oc.setEmbalagem(this.getEmbalagem());
        oc.setEmitente(this.getEmitente());
        oc.setFax(this.getFax());
        oc.setNumero(this.getNumero());
        oc.setReferencia(this.getReferencia());
        oc.setTelefone(this.getTelefone());
        oc.setTransporte(this.getTransporte());
        oc.setTrechosRetos(this.getTrechosRetos());
        oc.setValidade(this.getValidade());
        oc.setMedianteAprovacaoCadastro(this.getMedianteAprovacaoCadastro());
        oc.setRevisao(this.getRevisao());
        oc.setImposto(this.getImposto());
        oc.setIpi(this.getIpi());
        oc.setPrazo(this.getPrazo());
        oc.setNivelInspecao(this.getNivelInspecao());
        oc.setUtilizacao(this.getUtilizacao());
        return oc;
    }
}
